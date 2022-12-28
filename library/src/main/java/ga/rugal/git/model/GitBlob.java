package ga.rugal.git.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Problematic file.
 *
 * @author Rugal Bernstein
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@Value
public class GitBlob {

  /**
   * SHA-1 of the git object: blob.
   */
  @EqualsAndHashCode.Include
  String id;

  /**
   * The file path in repository.
   */
  String path;

  /**
   * File size in file system.
   */
  long size;
}
