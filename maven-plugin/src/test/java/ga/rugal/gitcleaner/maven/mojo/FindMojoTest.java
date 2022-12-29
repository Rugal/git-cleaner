package ga.rugal.gitcleaner.maven.mojo;

import lombok.SneakyThrows;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FindMojoTest extends AbstractMojoTestBase {

  @Parameters
  public static String[] data() {
    return new String[]{
      "git_folder",
    };
  }

  @SneakyThrows
  @Test
  public void canSuccess() {
    System.out.println(String.format("[%s] on [%s]", SUCCESS, this.test));
    this.getMojo(FindMojo.GOAL, this.test, SUCCESS).execute();
  }

  /**
   * These tests should fail.
   */
  @SneakyThrows
  @Test(expected = MojoFailureException.class)
  public void canFail() {
    System.out.println(String.format("[%s] on [%s]", FAIL, this.test));
    this.getMojo(FindMojo.GOAL, this.test, FAIL).execute();
  }
}
