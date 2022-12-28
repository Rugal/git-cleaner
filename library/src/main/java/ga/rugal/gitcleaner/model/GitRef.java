package ga.rugal.gitcleaner.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
  @EqualsAndHashCode.Include
  String name;

  /**
   * Commits that contain large file.
   */
  Set<GitCommit> commits;

  /**
   * Create ref with list of commit. Remove duplicated commit and blob.
   *
   * @param name ref name
   * @param list list of commit
   */
  public GitRef(final String name, final List<GitCommit> list) {
    this.name = name;

    for (int i = 0; i < list.size(); ++i) {
      for (int j = i + 1; j < list.size(); ++j) {
        list.get(i).deduplicate(list.get(j));
      }
    }
    this.commits = list.stream()
      .filter(p -> !p.getFiles().isEmpty())
      .collect(Collectors.toSet());
  }
}
