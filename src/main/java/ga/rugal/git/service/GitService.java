package ga.rugal.git.service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import ga.rugal.git.model.GitBlob;
import ga.rugal.git.model.GitCommit;
import ga.rugal.git.model.GitPackIndexEntry;
import ga.rugal.git.model.GitRef;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.internal.storage.file.PackIndex;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * Git service.
 *
 * @author Rugal Bernstein
 */
@Slf4j
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class GitService {

  private final Repository repository;

  @Inject
  public GitService(final Repository repository) {
    this.repository = repository;
  }

  /**
   * Find large file from all refs in remote.
   *
   * @param sizeInByte size to filter
   * @return All problematic refs
   * @throws IOException unable to read from file system
   */
  public Set<GitRef> filter(final int sizeInByte) throws IOException {
    return this.repository.getRefDatabase()
      .getRefs().stream()
      .map(Ref::getLeaf)
      .distinct()
      .map(m -> this.wrapTraverseRef(m, sizeInByte))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toSet());
  }

  private Optional<GitRef> wrapTraverseRef(final Ref ref, final int sizeInByte) {
    try {
      return Optional.of(this.traverseRef(ref, sizeInByte));
    } catch (final IOException ex) {
      return Optional.empty();
    }
  }

  private GitRef traverseRef(final Ref ref, final int sizeInByte)
    throws IOException {

    final var walk = new RevWalk(this.repository);
    walk.markStart(walk.parseCommit(ref.getObjectId()));
    LOG.debug("Start traverse ref [{}]", ref.getName());

    final var list = StreamSupport.stream(walk.spliterator(), true)
      .map(commit -> this.wrapTraverseCommit(commit, sizeInByte))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
    for (int i = 0; i < list.size(); ++i) {
      for (int j = i + 1; j < list.size(); ++j) {
        list.get(i).deduplicate(list.get(j));
      }
    }
    return new GitRef(ref.getName(), list);
  }

  private Optional<GitCommit> wrapTraverseCommit(final RevCommit commit, final int sizeInByte) {
    try {
      final var c = this.traverseCommit(commit, sizeInByte);
      return c;
    } catch (final IOException ex) {
      return Optional.empty();
    }
  }

  /**
   * Get all large file inside a commit, including all sub tree. It would traverse all sub-tree
   * automatically.
   *
   * @param commit     the commit object to look into
   * @param sizeInByte size to filter
   * @return problematic commit object
   * @throws IOException unable to read from file system
   */
  private Optional<GitCommit> traverseCommit(final RevCommit commit, final int sizeInByte)
    throws IOException {

    final var treeWalk = new TreeWalk(this.repository);
    treeWalk.reset(commit.getTree().getId());
    treeWalk.setRecursive(true); // recurse into entire tree

    final var result = new HashSet<GitBlob>();
    while (treeWalk.next()) {
      final var mode = treeWalk.getFileMode(0);
      if (treeWalk.isSubtree() // will recurse into them anyway
          || !mode.equals(FileMode.REGULAR_FILE)) { // ignore symbolic link
        LOG.trace("Skip this node");
        continue;
      }
      final var optional = this.getLargeFile(treeWalk, sizeInByte);
      if (optional.isPresent()) {
        final var file = optional.get();
        LOG.trace("Has large file id [{}] path [{}] with size [{}]",
                  file.getId(),
                  file.getPath(),
                  file.getSize());
        result.add(file);
      }
    }
    return result.isEmpty()
           ? Optional.empty()
           : Optional.of(new GitCommit(commit.getName(), result));
  }

  /**
   * Get the large file object from the current tree entry.
   *
   * @param tree       the root node
   * @param sizeInByte size to filter
   * @return optional problematic blob
   * @throws IOException Unable to read from file system
   */
  @Nonnull
  private Optional<GitBlob> getLargeFile(final TreeWalk tree, final int sizeInByte)
    throws IOException {

    final var blob = tree.getObjectId(0);
    final var loader = this.repository.open(blob);

    final var file = loader.getSize() >= sizeInByte
                 ? new GitBlob(blob.getName(),
                               tree.getPathString(),
                               loader.getSize())
                 : null;
    return Optional.ofNullable(file);
  }

  public void test() throws IOException {
    final var file = new File(
      ".git/objects/pack/pack-c67ad3cb7fe7be5e8242a7a6a83a837c9dfcc24c.idx");
    final var packIndex = PackIndex.open(file);
    final var list = StreamSupport.stream(packIndex.spliterator(), true)
      .map(m -> new GitPackIndexEntry(m.name(), m.getOffset()))
      .sorted((a, b) -> (int) (a.getOffset() - b.getOffset()))
      .collect(Collectors.toList());

    for (int i = 0; i < list.size() - 1; ++i) {
      list.get(i).setSize(list.get(i + 1).getOffset() - list.get(i).getOffset());
    }
    final var last = list.get(list.size() - 1);
    // TODO: need to read pack rather than pack index
    last.setSize(file.length() - last.getOffset());
  }
}
