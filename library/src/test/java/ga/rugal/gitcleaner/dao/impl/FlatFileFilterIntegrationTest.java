package ga.rugal.gitcleaner.dao.impl;

import config.DaggerGitCleaner;
import config.GitCleaner;
import config.RepositoryModule;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class FlatFileFilterIntegrationTest {

  private GitCleaner cleaner;

  @BeforeEach
  public void setUp() {
    this.cleaner = DaggerGitCleaner.builder()
      .repositoryModule(new RepositoryModule("../.git"))
      .build();
  }

  @Test
  @SneakyThrows
  public void filter() {
    final var refs = this.cleaner.gitService().findLargeFile(10000, false);

    refs.stream()
      .forEach(f -> LOG.debug(f.toString()));
  }
}
