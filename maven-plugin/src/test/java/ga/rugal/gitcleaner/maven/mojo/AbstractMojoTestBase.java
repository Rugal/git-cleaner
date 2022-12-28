package ga.rugal.gitcleaner.maven.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.runners.Parameterized.Parameter;

public abstract class AbstractMojoTestBase extends AbstractMojoTestCase {

  public static final String SUCCESS = "success";

  public static final String FAIL = "fail";

  private static final String TEMPLATE = "src/test/resources/unittest/%s/%s.xml";

  @Parameter
  public String test;

  /**
   * @see
   * https://maven.apache.org/plugin-testing/maven-plugin-testing-harness/getting-started/index.html
   */
  @Rule
  public MojoRule rule = new MojoRule();

  /**
   * Get Mojo object from each pom file.
   *
   * @param mojoName name of mojo
   * @param test     string case format
   * @param result   expected result
   *
   * @return created mojo
   *
   * @throws Exception unable to find target mojo
   */
  protected AbstractMojo getMojo(final String mojoName,
                                 final String test,
                                 final String result) throws Exception {
    final File pom = new File(String.format(TEMPLATE, test, result));
    Assert.assertTrue(pom.exists());

    final AbstractMojo mojo = (AbstractMojo) this.rule.lookupMojo(mojoName, pom);
    Assert.assertNotNull(mojo);
    return mojo;
  }
}
