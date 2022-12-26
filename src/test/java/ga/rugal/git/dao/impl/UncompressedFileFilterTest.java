package ga.rugal.git.dao.impl;

import config.Application;
import config.DaggerApplication;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncompressedFileFilterTest {

  private Application application;

  @BeforeEach
  public void setUp() {
    this.application = DaggerApplication.create();
  }

  @Test
  @SneakyThrows
  public void filter() {
    final var refs = this.application.gitService().findLargeFile(10000, false);

    refs.stream()
      .forEach(System.out::println);
  }
}
