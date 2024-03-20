package com.watchman.etl.dbsync.config;

import com.watchman.etl.config.AppConfiguration;
import com.watchman.etl.worker.WorkerConfiguration;
import org.apache.commons.configuration2.Configuration;

public class DbSyncProducerConfiguration implements WorkerConfiguration {
  private final Configuration configuration;

  DbSyncProducerConfiguration(AppConfiguration appConfiguration) {
    configuration = appConfiguration.getConfiguration();
  }

  @Override
  public String getTopic() {
    return configuration.getString("db_sync_topic_name");
  }

  @Override
  public String getBootStrapServers() {
    return configuration.getString("kafka_bootstrap_servers");
  }

  @Override
  public String getGroupId() {
    return null;
  }

  public static WorkerConfiguration from(AppConfiguration appConfiguration) {
    return new DbSyncProducerConfiguration(appConfiguration);
  }
}
