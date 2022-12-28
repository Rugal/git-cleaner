package ga.rugal.gitcleaner.model;

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
  @EqualsAndHashCode.Include
  String id;

  /**
   * List of large file. <BR>
   * Need to remove duplication. The earlier commit that introduce the large file win.
   */
  Set<GitBlob> files;

  /**
   * Remove duplicated blob from current object.
   *
   * @param old this must be older commit, that is created earlier than current one
   * @return {@code true} if this set changed as a result of the call
   */
  public boolean deduplicate(final GitCommit old) {
    return this.files.removeAll(old.files);
  }
}
