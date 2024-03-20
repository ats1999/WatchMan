package com.watchman.etl.http;

import com.watchman.etl.config.AppConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class SimpleWatchManHttpClientFactory {
  public static WatchManHttpClient createHttpClient() throws ConfigurationException {
    Configuration configuration = AppConfiguration.getInstance().getConfiguration();
    String userName = configuration.getString("meta_server_user_name");
    String password = configuration.getString("meta_server_user_password");
    return new SimpleWatchManHttpClient(userName, password);
  }
}
