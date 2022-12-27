package config;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Module regarding Git.
 *
 * @author Rugal Bernstein
 */
@Module
public interface GitModule {

  /**
   * Provide Git repository object.
   *
   * @return Git repository object
   * @throws RuntimeException Unable to read from file system
   */
  @Provides
  static Repository provideRepository() {
    try {
      // TODO: shall be changeable
      return FileRepositoryBuilder.create(new File(".git"));
    } catch (final IOException ex) {
      throw new RuntimeException("Unable to open repository");
    }
  }
}
