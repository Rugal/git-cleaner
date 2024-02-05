package ga.rugal.gitcleaner.shell

import ga.rugal.gitcleaner.core.service.RepositoryService
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
    @ShellOption(defaultValue = ".git", help = "Path to a valid '.git' folder") repository: String,
    @ShellOption(defaultValue = "1024", help = "Unit in byte") size: Long,
    @ShellOption(
      defaultValue = "pack",
      help = """
        1. Pack search is always preferred. It is super performant, but does not provide file path. Please recognize it by using other git command.
        2. Direct search can be used for small repository, it could reveal file path, but has serious performance issue when facing git pack file.
      """,
      value = ["pack", "direct"],
    ) method: String,
  ): String {

    val result = when (method) {
      "pack" -> RepositoryService(repository).filter(size, true)
      "direct" -> RepositoryService(repository).filter(size, true)
      else -> listOf()
    }
    return "Filter large blob completed"
  }
}
