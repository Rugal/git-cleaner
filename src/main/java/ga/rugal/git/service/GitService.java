package ga.rugal.git.service;

import java.io.IOException;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * Git service.
 *
 * @author Rugal Bernstein
 */
@Slf4j
public class GitService {

  private final Repository repository;

  @Inject
  public GitService(final Repository repository) {
    this.repository = repository;
  }

  public void filter(final int sizeInByte) throws IOException {
    for (var refs : this.repository.getRefDatabase().getRefs()) {
      var ref = refs.getLeaf();
      LOG.info("-[{}]-", ref.getName());
      RevWalk walk = new RevWalk(this.repository);
      walk.markStart(walk.parseCommit(ref.getObjectId()));

      for (var commit : walk) {
        this.traverseTree(commit.getTree(), sizeInByte);
      }
    }
  }

  private void traverseTree(final RevTree revTree, final int sizeInByte) throws IOException {
    final var tree = new TreeWalk(this.repository);
    tree.reset(revTree.getId());
    tree.setRecursive(true);

    while (tree.next()) {
      if (tree.isSubtree()) {
        // will recurse into them anyway
        continue;
      }

      final var mode = tree.getFileMode(0);
      if (!mode.equals(FileMode.REGULAR_FILE)) {
        // ignore symbolic link
        continue;
      }
      final var loader = repository.open(tree.getObjectId(0));
      if (loader.getSize() >= sizeInByte) {
        LOG.info("{} {} {}", mode.getBits(), tree.getNameString(), loader.getSize());
      }
    }
  }
}
