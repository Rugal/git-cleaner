package ga.rugal.gitcleaner.shell

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

/**
 * Find git objects by size.
 */
@ShellComponent
class FilterCommand {

  /**
   * Find git objects by size.
   */
  @ShellMethod(key = ["filter"])
  fun filter(
    @ShellOption(defaultValue = "1024") size: Long,
  ): String {
    return "Hello world $size"
  }
}
