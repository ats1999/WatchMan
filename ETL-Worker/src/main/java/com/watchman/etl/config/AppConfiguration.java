package com.watchman.etl.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class AppConfiguration {
  private Configuration configuration;
  private static volatile AppConfiguration appConfiguration;

  private AppConfiguration() throws ConfigurationException {
    loadConfig();
  }

  private void loadConfig() throws ConfigurationException {
    configuration = new Configurations().properties("application.properties");
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public static AppConfiguration getInstance() throws ConfigurationException {
    if (appConfiguration == null) {
      synchronized (AppConfiguration.class) {
        if (appConfiguration == null) {
          appConfiguration = new AppConfiguration();
        }
      }
    }

    return appConfiguration;
  }
}
