package config;

import javax.inject.Singleton;

import ga.rugal.git.service.GitService;

import dagger.Component;

/**
 * Application assembler.
 *
 * @author Rugal Bernstein
 */
@Singleton
@Component(
  modules = {
    RepositoryModule.class
  }
)
public interface GitCleaner {

  GitService gitService();
}
