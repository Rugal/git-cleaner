package ga.rugal.gitcleaner.dao.impl;

import config.DaggerGitCleaner;
import config.GitCleaner;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class FlatFileFilterIntegrationTest {

  private GitCleaner cleaner;

  @BeforeEach
  public void setUp() {
    this.cleaner = DaggerGitCleaner.create();
  }

  @Test
  @SneakyThrows
  public void filter() {
    final var refs = this.cleaner.gitService().findLargeFile(10000, false);

    refs.stream()
      .forEach(System.out::println);
  }
}
