package ga.rugal.gitcleaner.shell.dto

data class ProblematicBlob(
  /**
   * SHA-1 of the git object: blob.
   */
  val id: String,
  /**
   * File size byte after compression, it is offset difference.
   */
  val size: Long,
)
