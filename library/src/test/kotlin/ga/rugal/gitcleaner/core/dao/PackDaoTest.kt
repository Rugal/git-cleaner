package ga.rugal.gitcleaner.core.dao

import java.io.File
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Test

class PackDaoTest {

  @Test
  fun filter() {
    val r = Git.open(File("../.git")).repository

    PackDao(r).filter(1024).forEach {
      println("[${it.id}] [${it.size}]")
    }
  }
}
