package ga.rugal.gitcleaner.core.dao

import java.io.File
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Test

class FlatFileFilterTest {

  @Test
  fun filter() {
    val r = Git.open(File("../.git")).repository

    FlatFileFilter(r).filter(1024).forEach {
      println("[${it.id}] [${it.path}] [${it.size}]")
    }
  }
}
