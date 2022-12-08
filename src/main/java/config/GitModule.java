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

  @Provides
  static Repository provideRepository() {
    try {
      final var builder = new FileRepositoryBuilder();
      return builder.setGitDir(new File("./.git")) // TODO: shall be changeable
        .readEnvironment()
        .findGitDir()
        .build();
    } catch (final IOException ex) {
      throw new RuntimeException("Unable to open repository");
    }
  }
}
