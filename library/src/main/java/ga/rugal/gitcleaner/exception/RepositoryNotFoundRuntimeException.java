package ga.rugal.gitcleaner.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * To indicate that this repository is not found. Made it unchecked for Dagger requirement.
 *
 * @author Rugal Bernstein
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class RepositoryNotFoundRuntimeException extends RuntimeException {

  String gitDir;
}
