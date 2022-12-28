package ga.rugal.git.dao.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;

import ga.rugal.git.dao.FileFilter;
import ga.rugal.git.dto.ProblematicFile;
import ga.rugal.git.model.GitPackIndexEntry;

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
 * A file filter that look into pack index file. Means the file size is calculated after
 * compression.
 *
 * @author Rugal Bernstein
 */
@Slf4j
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CompressiveFileFilter implements FileFilter {

  private final Repository repository;

  @Inject
  public CompressiveFileFilter(final Repository repository) {
    this.repository = repository;
  }

  /**
   * Filter file with given threshold.
   *
   * @param sizeInByte size threshold
   * @return list of problematic file, the element has no commit id
   * @throws IOException unable to read repository
   */
  @Override
  public Collection<ProblematicFile> filter(final int sizeInByte) throws IOException {
    final var pathMap = this.getPathMap();
    final var files = this.findLargeFile(this.getPackIndexHash(), sizeInByte);
    return files.parallelStream()
      .map(a -> new ProblematicFile(a.getId(), pathMap.get(a.getId()), a.getSize()))
      .filter(a -> Objects.nonNull(a.path()))
      .collect(Collectors.toList());
  }

  private String getPackIndexHash() throws IOException {
    final var packFolder = new File(this.repository.getDirectory(), "objects/pack");
    if (!packFolder.exists() || !packFolder.isDirectory()) {
      throw new FileNotFoundException(String.format("Pack folder not found with [%s]",
                                                    packFolder.getAbsolutePath()));
    }
    final var files = packFolder.listFiles((File dir, String name) -> name.endsWith("idx"));
    if (null == files) {
      throw new FileNotFoundException(String.format("Pack index file not found in [%s]",
                                                    packFolder.getAbsolutePath()));
    }
    // pack-e0a9004716c7bd3e7b0e6dea68dad4d74b8a15c4.idx
    final var indexFile = files[0].getName();
    LOG.debug("Get pack index [{}]", indexFile);
    final var hash = indexFile.substring(indexFile.indexOf("-") + 1, indexFile.lastIndexOf(".idx"));
    // now we get the hash part e0a9004716c7bd3e7b0e6dea68dad4d74b8a15c4
    LOG.debug("Extract hash [{}] from pack index", hash);
    return hash;
  }

  /**
   * Read pack index and pack file to find large file, of which compressed size is larger than given
   * threshold.
   *
   * @param hash       pack index hash
   * @param sizeInByte threshold size
   * @return list of pack index entry that has large file
   * @throws IOException unable to read from repository
   */
  private List<GitPackIndexEntry> findLargeFile(final String hash, final int sizeInByte)
    throws IOException {
    final var index = new File(this.repository.getDirectory(),
      String.format("objects/pack/pack-%s.idx", hash));
    final var packIndex = PackIndex.open(index);
    final var list = StreamSupport.stream(packIndex.spliterator(), false)
      .map(m -> new GitPackIndexEntry(m.name(), m.getOffset()))
      .sorted((a, b) -> (int) (a.getOffset() - b.getOffset()))
      .collect(Collectors.toList());

    for (int i = 0; i < list.size() - 1; ++i) {
      list.get(i).setSize(list.get(i + 1).getOffset() - list.get(i).getOffset());
    }
    final var last = list.get(list.size() - 1);
    final var pack = new File(this.repository.getDirectory(),
      String.format("objects/pack/pack-%s.pack", hash));
    last.setSize(pack.length() - last.getOffset());

    return list.stream()
      .filter(a -> a.getSize() >= sizeInByte)
      .collect(Collectors.toList());
  }

  /**
   * Traverse all refs, commits and trees to get a map from blob id to path.
   *
   * @return a map from blob id to path
   * @throws IOException Unable to read from repository
   */
  private Map<String, String> getPathMap() throws IOException {
    return this.repository.getRefDatabase()
      .getRefs().stream()
      .map(Ref::getLeaf)
      .distinct()
      .map(m -> this.wrapTraverseRef(m))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .flatMap(a -> a.entrySet().stream())
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (String a, String b) -> Objects.nonNull(a) ? a : b)
      );
  }

  private Optional<Map<String, String>> wrapTraverseRef(final Ref ref) {
    try {
      return Optional.of(this.traverseRef(ref));
    } catch (final IOException ex) {
      return Optional.empty();
    }
  }

  private Map<String, String> traverseRef(final Ref ref) throws IOException {
    try (var walk = new RevWalk(this.repository)) {
      walk.markStart(walk.parseCommit(ref.getObjectId()));
      LOG.debug("Start traverse ref [{}]", ref.getName());

      final var result = StreamSupport.stream(walk.spliterator(), true)
        .map(commit -> this.wrapTraverseCommit(commit))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(a -> a.entrySet().stream())
        .collect(Collectors.toMap(
          Map.Entry::getKey,
          Map.Entry::getValue,
          (a, b) -> Objects.nonNull(a) ? a : b)
        );
      walk.dispose();
      return result;
    }
  }

  private Optional<HashMap<String, String>> wrapTraverseCommit(final RevCommit commit) {
    try {
      return Optional.of(this.traverseCommit(commit));
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
  private HashMap<String, String> traverseCommit(final RevCommit commit)
    throws IOException {

    try (var walk = new TreeWalk(this.repository)) {
      walk.reset(commit.getTree().getId());
      walk.setRecursive(true); // recurse into entire tree
      LOG.trace("Start traverse the tree of commit [{}]", commit.getName());

      final var result = new HashMap<String, String>();
      while (walk.next()) {
        if (!walk.isSubtree() // will recurse into them anyway
            // ignore symbolic link
            && walk.getFileMode(0).equals(FileMode.REGULAR_FILE)) {
          result.put(walk.getObjectId(0).getName(), walk.getPathString());
        }
      }
      return result;
    }
  }
}
