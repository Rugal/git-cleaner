package config;

import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;

/**
 * Module regarding other application related stuffs.
 *
 * @author Rugal Bernstein
 */
@Module
public interface ApplicationModule {

  @Provides
  static Gson provideGson() {
    return new Gson();
  }
}
