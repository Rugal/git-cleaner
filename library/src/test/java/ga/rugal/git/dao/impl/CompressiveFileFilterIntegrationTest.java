package ga.rugal.git.dao.impl;

import config.DaggerGitCleaner;
import config.GitCleaner;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class CompressiveFileFilterIntegrationTest {

  private GitCleaner cleaner;

  @BeforeEach
  public void setUp() {
    this.cleaner = DaggerGitCleaner.create();
  }

  @Test
  @SneakyThrows
  public void filter() {
    final var files = this.cleaner.gitService().findLargeFile(2000, true);

    files.stream()
      .forEach(System.out::println);
  }
}
