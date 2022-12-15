package ga.rugal.git.model;

import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Problematic ref that contains large file.
 *
 * @author Rugal Bernstein
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@Value
public class GitRef {

  /**
   * Ref name.
   */
  String name;

  /**
   * Commits that contain large file.
   */
  Set<GitCommit> commits;
}
