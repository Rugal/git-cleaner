package ga.rugal.git.dao.impl;

import config.Application;
import config.DaggerApplication;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CompressedFileFilterTest {

  private Application application;

  @BeforeEach
  public void setUp() {
    this.application = DaggerApplication.create();
  }

  @Test
  @SneakyThrows
  public void filter() {
    final var files = this.application.gitService().findLargeFile(2000, true);

    files.stream()
      .forEach(System.out::println);
  }
}
