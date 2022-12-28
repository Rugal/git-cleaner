package ga.rugal.gitcleaner.maven.mojo;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RugalMojoTest extends AbstractMojoTestBase {

  @Parameters
  public static String[] data() {
    return new String[]{
      "not_found",
    };
  }

  @SneakyThrows
  @Test
  public void canSuccess() {
    if (this.test.equals("not_found")) {
      System.out.println(String.format("[%s] on [%s]", SUCCESS, this.test));
      this.getMojo(RugalMojo.GOAL, this.test, SUCCESS).execute();
    }
  }
}
