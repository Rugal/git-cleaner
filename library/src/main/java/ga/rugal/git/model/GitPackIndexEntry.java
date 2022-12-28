package ga.rugal.git.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Pack index entry.
 *
 * @author Rugal Bernstein
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@ToString
public class GitPackIndexEntry {

  /**
   * Blob id.
   */
  private final String id;

  /**
   * Byte offset notated in pack index file.
   */
  private final long offset;

  /**
   * File size after compression.
   */
  @Setter
  private long size;
}
