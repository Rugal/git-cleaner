package ga.rugal.gitcleaner.core.model

/**
 * Git blob mapping.
 *
 * @author Rugal Bernstein
 */
data class GitBlob(
  /**
   * SHA-1 of the git object: blob.
   */
  val id: String,
  /**
   * The file path in repository.
   */
  val path: String,
  /**
   * File size byte in file system.
   */
  val size: Long,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GitBlob

    return id == other.id
  }

  override fun hashCode(): Int = id.hashCode()
}
