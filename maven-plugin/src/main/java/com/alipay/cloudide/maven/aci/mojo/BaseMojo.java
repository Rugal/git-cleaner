package com.alipay.cloudide.maven.aci.mojo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alipay.cloudide.maven.aci.config.Constant;
import com.alipay.cloudide.maven.aci.core.entity.Configuration;
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
public abstract class BaseMojo extends AbstractMojo {

  /**
   * If this execution will skip.<BR>
   * Also configurable through Maven or System property {@link Constant#SKIP}
   */
  @Parameter
  private boolean skip = false;

  /**
   * Fail the build if execution check not pass.<BR>
   * Also configurable through Maven or System property: {@link Constant#FAIL_ON_ERROR}
   */
  @Parameter
  private boolean failOnError = true;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

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
      .failOnError(Boolean.parseBoolean(map.getOrDefault(Constant.FAIL_ON_ERROR,
                                                         Boolean.toString(this.failOnError))))
      .skip(Boolean.parseBoolean(map.getOrDefault(Constant.SKIP,
                                                  Boolean.toString(this.skip))))
      .log(this.getLog())
      .project(this.project)
      .build();
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    final Configuration c;
    try {
      c = this.getConfiguration();
      this.getLog().debug(c.toString());
    } catch (IOException | InterruptedException ex) {
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
