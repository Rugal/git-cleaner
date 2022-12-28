package ga.rugal.git.dao.impl;

import config.DaggerGitCleaner;
import config.GitCleaner;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CompressiveFileFilterIntegrationTest {

  private GitCleaner application;

  @BeforeEach
  public void setUp() {
    this.application = DaggerGitCleaner.create();
  }

  @Test
  @SneakyThrows
  public void filter() {
    final var files = this.application.gitService().findLargeFile(2000, true);

    files.stream()
      .forEach(System.out::println);
  }
}
