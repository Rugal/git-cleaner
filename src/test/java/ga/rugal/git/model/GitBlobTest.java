package ga.rugal.git.model;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GitBlobTest {

  @BeforeEach
  public void setUp() {
  }

  @Test
  public void testEquals() {
    final var set = new HashSet<GitBlob>();
    Assertions.assertEquals(0, set.size());
    set.add(new GitBlob("1", "1", 0));
    Assertions.assertEquals(1, set.size());
    set.add(new GitBlob("1", "1", 0));
    Assertions.assertEquals(1, set.size());
    set.add(new GitBlob("1", "3", 0));
    Assertions.assertEquals(1, set.size());
    set.add(new GitBlob("2", "3", 0));
    Assertions.assertEquals(2, set.size());
  }
}
