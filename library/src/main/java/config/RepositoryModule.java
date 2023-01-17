package config;

import java.io.File;
import java.io.IOException;

import ga.rugal.gitcleaner.exception.RepositoryNotFoundRuntimeException;
import ga.rugal.gitcleaner.exception.RepositoryUnreadableRuntimeException;

import dagger.Module;
import dagger.Provides;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
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
@Slf4j
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
    final var file = new File(this.gitFolder);
    try {
      LOG.debug("Try to open git folder [{}]", file.getAbsolutePath());
      final var repository = new FileRepositoryBuilder()
        .setMustExist(true)
        .setGitDir(file)
        .build();
      LOG.debug("Get repository for git folder [{}]", file.getAbsolutePath());
      return repository;
    } catch (final RepositoryNotFoundException ex) {
      LOG.error("Repository not found [{}]", file.getAbsolutePath());
      throw new RepositoryNotFoundRuntimeException(file.getAbsolutePath());
    } catch (final IOException ex) {
      LOG.error("Repository found but unable to read configuration [{}]", file.getAbsolutePath());
      throw new RepositoryUnreadableRuntimeException(file.getAbsolutePath());
    }
  }
}
