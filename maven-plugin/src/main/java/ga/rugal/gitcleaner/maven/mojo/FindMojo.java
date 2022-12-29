package ga.rugal.gitcleaner.maven.mojo;

import java.io.IOException;

import ga.rugal.gitcleaner.maven.entity.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * To upload module JAR file for deployment.
 *
 * @author Rugal
 */
@Slf4j
@Mojo(name = FindMojo.GOAL, requiresProject = true)
public class FindMojo extends BaseMojo {

  public static final String GOAL = "find";

  /**
   * Need projectId + aciStateFile + branch + ymlGlobalPath.
   *
   * @param c all preset configuration, priority is 1. system property 2. configuration 3. default
   *          value
   *
   * @throws MojoExecutionException when error occurs in exception that is not neglectable
   * @throws MojoFailureException   when error occurs in exception that is neglectable
   */
  @Override
  public void execute(final Configuration c) throws MojoExecutionException, MojoFailureException {
    try {
      this.cleaner.gitService().findLargeFile(c.getSizeToFilter(), c.isCompressive());
    } catch (final IOException ex) {
      throw new MojoFailureException(String.format(
        "Unable to read git repository [%s]",
        c.getGitFolder()));
    }
  }
}
