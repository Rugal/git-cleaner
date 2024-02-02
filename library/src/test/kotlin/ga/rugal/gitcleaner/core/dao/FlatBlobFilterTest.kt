package ga.rugal.gitcleaner.core.dao

import java.io.File
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Test

class FlatBlobFilterTest {

  @Test
  fun filter() {
    val r = Git.open(File("../.git")).repository

    FlatBlobFilter(r).filter(10240).forEach {
      println("[${it.id}] [${it.path}] [${it.size}]")
    }
  }
}
