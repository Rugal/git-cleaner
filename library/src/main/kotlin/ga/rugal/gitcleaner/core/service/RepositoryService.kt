package ga.rugal.gitcleaner.core.service

import java.io.File
import ga.rugal.gitcleaner.core.dao.DirectDao
import ga.rugal.gitcleaner.core.dao.PackDao
import ga.rugal.gitcleaner.core.model.GitBlob
import ga.rugal.gitcleaner.core.model.GitPackIndexEntry
import ga.rugal.gitcleaner.shell.dto.ProblematicBlob
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository

class RepositoryService(private val path: String) {

  private val repository: Repository by lazy { Git.open(File(path)).repository }

  private fun GitBlob.toProblematicBlob(): ProblematicBlob = ProblematicBlob(this.id, this.size)

  private fun GitPackIndexEntry.toProblematicBlob(): ProblematicBlob = ProblematicBlob(this.id, this.size)

  fun filter(sizeInByte: Long, usePack: Boolean = true): List<ProblematicBlob> =
    if (usePack) PackDao(this.repository).filter(sizeInByte).map { it.toProblematicBlob() }
    else DirectDao(this.repository).filter(sizeInByte).map { it.toProblematicBlob() }
}
