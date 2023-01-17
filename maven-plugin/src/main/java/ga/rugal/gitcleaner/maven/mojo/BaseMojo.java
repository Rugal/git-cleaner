package ga.rugal.gitcleaner.maven.mojo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import config.Constant;
import config.DaggerGitCleaner;
import config.GitCleaner;
import config.RepositoryModule;

import ga.rugal.gitcleaner.exception.RepositoryNotFoundRuntimeException;
import ga.rugal.gitcleaner.exception.RepositoryUnreadableRuntimeException;
import ga.rugal.gitcleaner.maven.entity.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Base class for basic and common logic, usually to prepare configuration.
 *
 * @author Rugal
 */
@Slf4j
public abstract class BaseMojo extends AbstractMojo {

  /**
   * If this execution will skip.<BR>
   * Also configurable through Maven or System property {@link Constant#SKIP}
   */
  @Parameter(name = Constant.SKIP, defaultValue = "false")
  private boolean skip = false;

  /**
   * Fail the build if execution is fine but there is neglectable failure.<BR>
   * Also configurable through Maven or System property: {@link Constant#FAIL_ON_ERROR}
   */
  @Parameter(name = Constant.FAIL_ON_ERROR, defaultValue = "true")
  private boolean failOnError = true;

  /**
   * The Git folder that store git object and everything.<BR>
   * Also configurable through Maven or System property: {@link Constant#GIT_FOLDER}
   */
  @Parameter(name = Constant.GIT_FOLDER, defaultValue = ".git")
  private String gitFolder = ".git";

  /**
   * To search compressive file from pack file. Also configurable through Maven or System property:
   * {@link Constant#IS_COMPRESSIVE}
   */
  @Parameter(name = Constant.IS_COMPRESSIVE, defaultValue = "false")
  private boolean isCompressive = false;

  /**
   * To search file that is >= this value. The unit is in byte. <BR>
   * Also configurable through Maven or System property: {@link Constant#SIZE_TO_FILTER}
   *
   * <ul>
   * <li>true <strong>(default)</strong> in this mode the compiler plugin determines whether any JAR
   * files the current module depends on have changed in the current build run; or any source file
   * was added, removed or changed since the last compilation. If this is the case, the compiler
   * plugin recompiles all sources.</li>
   * <li>false <strong>(not recommended)</strong> this only compiles source files which are newer
   * than their corresponding class files, namely which have changed since the last compilation.
   * This does not recompile other classes which use the changed class, potentially leaving them
   * with references to methods that no longer exist, leading to errors at runtime.</li>
   * </ul>
   *
   * @since 3.1
   */
  @Parameter(name = Constant.SIZE_TO_FILTER, defaultValue = "1024")
  private int sizeToFilter = 1024;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  protected GitCleaner cleaner;

  /**
   * Read single value from maven system property.
   */
  private void read(final Map<String, String> map, final String key) {
    final String property = System.getProperty(key);
    if (null != property) {
      map.put(key, property);
    }
  }

  /**
   * Group maven system property into map.
   *
   * @return the system property map
   */
  private Map<String, String> readSystemProperty() {
    final Map<String, String> map = new HashMap<>();
    this.read(map, Constant.FAIL_ON_ERROR);
    this.read(map, Constant.SKIP);
    this.read(map, Constant.IS_COMPRESSIVE);
    this.read(map, Constant.SIZE_TO_FILTER);
    this.read(map, Constant.GIT_FOLDER);
    return map;
  }

  /**
   * Create configuration object that contains the complete configuration, whether it is from maven
   * system property or from {@code pom.xml}.<BR>
   * 1. get default parameter value<BR>
   * 2. get plugin configuration<BR>
   * 3. get maven system property<BR>
   *
   * @return configuration which is correctly overwritten
   */
  private Configuration getConfiguration() throws IOException, InterruptedException {
    final Map<String, String> map = this.readSystemProperty();

    // build final configuration
    return Configuration.builder()
      .failOnError(
        Boolean.parseBoolean(map.getOrDefault(Constant.FAIL_ON_ERROR,
                                              Boolean.toString(this.failOnError))))
      .skip(
        Boolean.parseBoolean(map.getOrDefault(Constant.SKIP,
                                              Boolean.toString(this.skip))))
      .isCompressive(
        Boolean.parseBoolean(map.getOrDefault(Constant.IS_COMPRESSIVE,
                                              Boolean.toString(this.isCompressive))))
      .sizeToFilter(
        Integer.parseInt(map.getOrDefault(Constant.SIZE_TO_FILTER,
                                          Integer.toString(this.sizeToFilter))))
      .gitFolder(map.getOrDefault(Constant.GIT_FOLDER, this.gitFolder))
      .log(this.getLog())
      .project(this.project)
      .build();
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    final Configuration c;
    try {
      c = this.getConfiguration();

      this.cleaner = DaggerGitCleaner.builder()
        .repositoryModule(new RepositoryModule(c.getGitFolder()))
        .build();
      // deliberately invoke it so as to verify repository existence
      this.cleaner.gitService();

      this.getLog().debug(c.toString());
    } catch (final RepositoryNotFoundRuntimeException ex) {
      throw new MojoExecutionException(String.format("Repository not found [%s]", ex.getGitDir()));
    } catch (final RepositoryUnreadableRuntimeException ex) {
      throw new MojoExecutionException(String.format(
        "Repository found but unable to read configuration [{}]",
        ex.getGitDir()));
    } catch (IOException | InterruptedException ex) {
      LOG.error(ex.getMessage());
      throw new MojoExecutionException("Unable to initialize maven plugin configuration");
    }

    if (c.isSkip()) {
      this.getLog().info("Skip execution due to configuration");
      return;
    }
    try {
      this.execute(c);
    } catch (final MojoFailureException e) {
      if (c.isFailOnError()) {
        throw e;
      }
    }
  }

  /**
   * Execute mojo.
   *
   * @param configuration all preset configuration, priority is 1. system property 2. configuration
   *                      3. default value
   *
   * @throws MojoExecutionException when execution has any problem
   * @throws MojoFailureException   when fail the execution
   */
  public abstract void execute(Configuration configuration) throws MojoExecutionException,
                                                                   MojoFailureException;
}
