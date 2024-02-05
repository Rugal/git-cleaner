package ga.rugal.gitcleaner.core.dao

import java.io.IOException
import ga.rugal.gitcleaner.core.model.GitBlob
import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.TreeWalk

/**
 * File filter that traverses ref, commit and tree to get all blob.
 * This could be super slow if contains pack file.
 *
 * @author Rugal Bernstein
 */
@Deprecated(
  message = "Deprecate due to heavy performance issue when processing pack file",
  replaceWith = ReplaceWith("CompressedBlobFilter(repository)", imports = arrayOf("ga.rugal.gitcleaner.core.dao.CompressedBlobFilter")),
  level = DeprecationLevel.WARNING
)
internal class DirectDao(val repository: Repository) {
  private val LOG = KotlinLogging.logger { }

  private val ObjectId.size: Long
    get() = repository.open(this).size

  private val RevCommit.blobs: MutableSet<GitBlob>
    get() = TreeWalk(repository).use { walk ->
      walk.addTree(this.tree)
      walk.isRecursive = true // set to false if you want to see directories

      HashSet<GitBlob>().also { set ->
        while (walk.next()) {
          walk.getObjectId(0).also {
            set.add(GitBlob(it.name, walk.pathString, it.size))
          }
        }
      }
    }

  private val Ref.commits: MutableSet<RevCommit>
    get() {
      LOG.info { "Traverse ref ${this.name}" }
      return RevWalk(repository).use {
        it.markStart(it.parseCommit(this.objectId))
        it.toMutableSet()
      }
    }

  @Throws(IOException::class)
  fun filter(sizeInByte: Long): List<GitBlob> = this.repository.refDatabase.refs
    .asSequence()
    .map { it.leaf }        // get all actual ref
    .distinctBy { it.name } // distinct by name
    .map { it.commits } // get all commit of a ref
    .reduce { acc, revCommits -> acc.also { it.addAll(revCommits) } }
    .map { it.blobs }
    .reduce { acc, blobs -> acc.also { it.addAll(blobs) } }
    .filter { it.size >= sizeInByte }
}
