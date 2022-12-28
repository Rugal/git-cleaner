package config;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Module regarding Git.
 *
 * @author Rugal Bernstein
 */
@AllArgsConstructor
@Module
@NoArgsConstructor
public class RepositoryModule {

  private String gitFolder = ".git";

  /**
   * Provide Git repository object.
   *
   * @return Git repository object
   * @throws RuntimeException Unable to read from file system
   */
  @Provides
  public Repository provideRepository() {
    try {
      return FileRepositoryBuilder.create(new File(this.gitFolder));
    } catch (final IOException ex) {
      throw new RuntimeException("Unable to open repository");
    }
  }
}
