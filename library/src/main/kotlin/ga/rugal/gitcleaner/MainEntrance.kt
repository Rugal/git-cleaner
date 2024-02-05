package ga.rugal.gitcleaner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main entrance.
 *
 * @author Rugal Bernstein
 */
@SpringBootApplication
class MainEntrance

fun main(args: Array<String>) {
  runApplication<MainEntrance>(*args)
}
