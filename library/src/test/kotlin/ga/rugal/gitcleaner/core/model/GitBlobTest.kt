package ga.rugal.gitcleaner.core.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.HashSet

class GitBlobTest {

  @Test
  fun test() {
    val set = HashSet<GitBlob>()
    Assertions.assertEquals(0, set.size)
    set.add(GitBlob("1", "1", 0))
    Assertions.assertEquals(1, set.size)
    set.add(GitBlob("1", "1", 0))
    Assertions.assertEquals(1, set.size)
    set.add(GitBlob("1", "3", 0))
    Assertions.assertEquals(1, set.size)
    set.add(GitBlob("2", "3", 0))
    Assertions.assertEquals(2, set.size)
  }
}
