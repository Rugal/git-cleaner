package ga.rugal.gitcleaner.core.dao

import java.io.File
import java.io.IOException
import ga.rugal.gitcleaner.core.model.GitBlob
import org.eclipse.jgit.internal.storage.file.PackIndex
import org.eclipse.jgit.lib.Repository

class CompressedBlobFilter(val repository: Repository) {
  @Throws(IOException::class)
  fun filter(sizeInByte: Long): List<GitBlob> {
    val pack = PackIndex.open(File("aoe")).toSet()
    return listOf()
  }
}
