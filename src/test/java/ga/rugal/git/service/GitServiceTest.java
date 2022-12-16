package ga.rugal.git.service;

import config.Application;
import config.DaggerApplication;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
public class GitServiceTest {

  private Application application;

  @BeforeEach
  public void setUp() {
    this.application = DaggerApplication.create();
  }

  @Disabled
  @Test
  @SneakyThrows
  public void filter() {
    final var refs = this.application.gitService().filter(10000);
    for (var ref : refs) {
      ref.getCommits().stream()
        .forEach(a -> LOG.info("[{}] [{}] [{}]", ref.getName(), a.getId(), a.getFiles()));
    }
  }

  @Test
  @SneakyThrows
  public void test() {
    this.application.gitService().test();
  }
}
