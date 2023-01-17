package ga.rugal.gitcleaner.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * To indicate that this repository is found but not able to read due to configuration file issue.
 * Made it unchecked for Dagger requirement.
 *
 * @author Rugal Bernstein
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class RepositoryUnreadableRuntimeException extends RuntimeException {

  String gitDir;
}
