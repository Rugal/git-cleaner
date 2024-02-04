package ga.rugal.gitcleaner.core.model

/**
 * Pack index entry, without path.
 *
 * @author Rugal Bernstein
 */
data class GitPackIndexEntry(
  /**
   * SHA-1 of the git object: blob.
   */
  val id: String,
  /**
   * File size byte after compression, it is offset difference.
   */
  val size: Long,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GitPackIndexEntry

    return id == other.id
  }

  override fun hashCode(): Int = id.hashCode()
}
