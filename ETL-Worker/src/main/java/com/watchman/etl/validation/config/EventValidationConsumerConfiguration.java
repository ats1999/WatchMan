package com.watchman.etl.validation.config;

import com.watchman.etl.config.AppConfiguration;
import com.watchman.etl.worker.WorkerConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class EventValidationConsumerConfiguration implements WorkerConfiguration {
  private final Configuration configuration;

  private EventValidationConsumerConfiguration() throws ConfigurationException {
    configuration = AppConfiguration.getInstance().getConfiguration();
  }

  public String getTopic() {
    return configuration.getString("event_validation_topic_name");
  }

  public String getBootStrapServers() {
    return configuration.getString("kafka_bootstrap_servers");
  }

  public String getGroupId() {
    return configuration.getString("event_validation_consumer_group_id");
  }

  public static EventValidationConsumerConfiguration getInstance() throws ConfigurationException {
    return new EventValidationConsumerConfiguration();
  }
}
