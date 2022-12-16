package ga.rugal.git.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Pack index entry.
 *
 * @author Rugal Bernstein
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@RequiredArgsConstructor
public class GitPackIndexEntry {

  private final String id;

  private final long offset;

  @Setter
  private long size;
}
