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
    GitModule.class
  }
)
public interface Application {

  GitService gitService();
}
