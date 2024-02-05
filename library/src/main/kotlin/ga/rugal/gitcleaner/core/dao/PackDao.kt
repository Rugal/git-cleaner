package ga.rugal.gitcleaner.core.dao

import java.io.File
import java.io.IOException
import ga.rugal.gitcleaner.core.model.GitPackIndexEntry
import org.eclipse.jgit.internal.storage.file.PackIndex
import org.eclipse.jgit.lib.Repository

/**
 * File filter that read objects from pack index and compute after compression size by offset.
 * This should be very efficient ever with large repository.
 *
 * @author Rugal Bernstein
 */
internal class PackDao(val repository: Repository) {

  /**
   * Get all objects in this pack, with object size computed(after compression), then filter the large file by given size.
   */
  private fun PackIndex.objects(sizeInByte: Long): List<GitPackIndexEntry> {
    var base = 0L
    return this.map { it.name() to it.offset }
      .sortedBy { it.second }
      .map { p ->
        GitPackIndexEntry(p.first, p.second - base).also {
          base = p.second // update base everytime
        }
      }
      .filter { it.size >= sizeInByte }
  }

  /**
   * List all pack index under this repository.
   *
   * sample file name [pack-5b5e93a0df6e3931a57692ac37cdd5f6c910a752.idx]
   */
  @Throws(IOException::class)
  fun filter(sizeInByte: Long): List<GitPackIndexEntry> =
    (File("${repository.directory}/objects/pack").listFiles { _, name ->
      name.startsWith("pack-") && name.endsWith(".idx")
    } ?: arrayOf<File>())
      .flatMap { PackIndex.open(it).objects(sizeInByte) }
}
