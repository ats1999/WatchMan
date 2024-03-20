package com.watchman.etl.validation.consumer;

import com.watchman.avro.schema.AvroEvent;
import com.watchman.avro.schema.AvroKafkaEventMessage;
import com.watchman.etl.config.AppConfiguration;
import com.watchman.etl.http.WatchManHttpClient;
import com.watchman.etl.worker.Producer;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.JSONObject;

public class EventValidator implements Runnable {
  AvroKafkaEventMessage kafkaEventMessage;
  Producer<AvroKafkaEventMessage, JSONObject> dbSyncProducer;
  WatchManHttpClient watchManHttpClient;
  Configuration configuration;

  EventValidator(
      AvroKafkaEventMessage kafkaEventMessage,
      Producer<AvroKafkaEventMessage, JSONObject> dbSyncProducer,
      WatchManHttpClient watchManHttpClient)
      throws ConfigurationException {
    this.kafkaEventMessage = kafkaEventMessage;
    this.dbSyncProducer = dbSyncProducer;
    this.watchManHttpClient = watchManHttpClient;
    configuration = AppConfiguration.getInstance().getConfiguration();
  }

  private Optional<Object> getApiKeyMeta(String apiKey) throws IOException {
    String endPoint =
        configuration.getString("meta_server_host")
            + "/event/api-key-meta?"
            + "apiKey"
            + "="
            + apiKey;

    return watchManHttpClient.get(endPoint, null);
  }

  boolean isValid(AvroEvent event, JSONObject apiKeyMeta) {
    long expiryTime = apiKeyMeta.getLong("expiryTime");
    long currentTime = Instant.now().toEpochMilli();

    // check if event id is valid
    return expiryTime < currentTime || event.getEventId() != apiKeyMeta.getLong("eventId");
  }

  @Override
  public void run() {
    try {
      Optional<Object> apiKeyMeta = getApiKeyMeta(kafkaEventMessage.getApiKey().toString());

      if (apiKeyMeta.isPresent()
          && !isValid(kafkaEventMessage.getEvent(), (JSONObject) apiKeyMeta.get())) {
        dbSyncProducer.publish(kafkaEventMessage, (JSONObject) apiKeyMeta.get());
      } else {
        // TODO: if event is not valid then produce it into invalid_events
        System.out.println("Event is not valid....");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
