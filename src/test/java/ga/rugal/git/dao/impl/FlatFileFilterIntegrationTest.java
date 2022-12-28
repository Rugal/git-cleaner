package ga.rugal.git.dao.impl;

import config.DaggerGitCleaner;
import config.GitCleaner;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class FlatFileFilterIntegrationTest {

  private GitCleaner application;

  @BeforeEach
  public void setUp() {
    this.application = DaggerGitCleaner.create();
  }

  @Test
  @SneakyThrows
  public void filter() {
    final var refs = this.application.gitService().findLargeFile(10000, false);

    refs.stream()
      .forEach(System.out::println);
  }
}
