package ga.rugal.gitcleaner.core.dao

import ga.rugal.gitcleaner.core.model.GitBlob
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.TreeWalk
import java.io.IOException

/**
 * File filter that traverses commit trees to find problematic files.
 *
 * @author Rugal Bernstein
 */
class FlatFileFilter(val repository: Repository) {

  private fun getSize(objectId: ObjectId) = repository.open(objectId).size

  private fun getBlob(commit: RevCommit): HashSet<GitBlob> = TreeWalk(repository).use { walk ->
    walk.addTree(commit.tree)
    walk.isRecursive = true // set to false if you want to see directories

    val set = HashSet<GitBlob>()
    while (walk.next()) {
      walk.getObjectId(0).also {
        set.add(GitBlob(walk.pathString, it.name, getSize(it)))
      }
    }
    set
  }

  private fun getCommits(ref: Ref): HashSet<RevCommit> = RevWalk(repository).use {
    it.markStart(it.parseCommit(ref.objectId))
    it.toHashSet()
  }

  @Throws(IOException::class)
  fun filter(sizeInByte: Long) {
    this.repository.refDatabase.refs
      .asSequence()
      .map { it.leaf }        // get all actual ref
      .distinctBy { it.name } // distinct by name
      .map { getCommits(it) } // get all commit of a ref
      .reduce { acc, revCommits -> acc.also { it.addAll(revCommits) } }
      .map { }
      .toList()
  }
}