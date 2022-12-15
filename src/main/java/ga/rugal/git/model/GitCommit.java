package ga.rugal.git.model;

import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Problematic commit that contains large file.
 *
 * @author Rugal Bernstein
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@Value
public class GitCommit {

  /**
   * SHA-1 commit id.
   */
  String id;

  /**
   * List of large file. <BR>
   * Need to remove duplication. The earlier commit that introduce the large file win.
   */
  Set<GitBlob> files;
}
