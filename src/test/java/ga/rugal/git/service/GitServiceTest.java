package ga.rugal.git.service;

import config.Application;
import config.DaggerApplication;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GitServiceTest {

  private Application application;

  @BeforeEach
  public void setUp() {
    this.application = DaggerApplication.create();
  }

  @Test
  @SneakyThrows
  public void filter() {
    this.application.gitService().filter(3000);
  }
}
