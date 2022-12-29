package ga.rugal.gitcleaner.maven.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Store finalized configuration parameters.
 *
 * @author Rugal Bernstein
 */
@Builder
@Getter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@ToString
public class Configuration {

  /**
   * If this execution will skip.<BR>
   * Also configurable through Maven or System property {@link Constant#SKIP}
   */
  private final boolean skip;

  /**
   * Fail the build if execution check not pass.<BR>
   * Also configurable through Maven or System property: {@link Constant#FAIL_ON_ERROR}
   */
  private final boolean failOnError;

  private final MavenProject project;

  /**
   * To search compressive file from pack file. Also configurable through Maven or System property:
   * {@link Constant#IS_COMPRESSIVE}
   */
  private final boolean isCompressive;

  /**
   * To search file that is >= this value. The unit is byte. <BR>
   * Also configurable through Maven or System property: {@link Constant#SIZE_TO_FILTER}
   */
  private final int sizeToFilter;

  /**
   * The Git folder that store git object and everything.<BR>
   * Also configurable through Maven or System property: {@link Constant#GIT_FOLDER}
   */
  private final String gitFolder;

  @Setter
  private Log log;
}
