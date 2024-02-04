package ga.rugal.gitcleaner.core.dao

import java.io.File
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Test

class PackFilterTest {

  @Test
  fun filter() {
    val r = Git.open(File("../.git")).repository

    PackFilter(r).filter(1024).forEach {
      println("[${it.id}] [${it.size}]")
    }
  }
}
