package com.watchman.etl.validation.consumer;

import com.watchman.avro.schema.AvroKafkaEventMessage;
import com.watchman.etl.executor.TaskExecutor;
import com.watchman.etl.http.SimpleWatchManHttpClientFactory;
import com.watchman.etl.http.WatchManHttpClient;
import com.watchman.etl.worker.Producer;
import com.watchman.etl.worker.WorkerConfiguration;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONObject;

public class EventValidationConsumer implements Runnable {
  private final TaskExecutor<Runnable, Future<?>> taskExecutor;
  private final WorkerConfiguration workerConfiguration;
  private final Producer<AvroKafkaEventMessage, JSONObject> producer;

  public EventValidationConsumer(
      TaskExecutor<Runnable, Future<?>> taskExecutor,
      WorkerConfiguration workerConfiguration,
      Producer<AvroKafkaEventMessage, JSONObject> producer) {
    System.out.println("EventValidationConsumer started...");
    this.taskExecutor = taskExecutor;
    this.workerConfiguration = workerConfiguration;
    this.producer = producer;
  }

  private Properties getConsumerProperties() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, workerConfiguration.getBootStrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, workerConfiguration.getGroupId());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    return props;
  }

  @Override
  public void run() {
    Properties props = getConsumerProperties();
    try (KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props)) {
      consumer.subscribe(Collections.singletonList(workerConfiguration.getTopic()));

      while (true) {
        ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(1000));

        for (ConsumerRecord<String, byte[]> record : records) {
          // deserialize message
          AvroKafkaEventMessage kafkaEventMessage =
              AvroKafkaEventMessage.fromByteBuffer(ByteBuffer.wrap(record.value()));

          // submit message for validation
          WatchManHttpClient watchManHttpClient =
              SimpleWatchManHttpClientFactory.createHttpClient();
          EventValidator eventValidator =
              new EventValidator(kafkaEventMessage, producer, watchManHttpClient);

          taskExecutor.submit(eventValidator);
        }
      }
    } catch (IOException | ConfigurationException e) {
      throw new RuntimeException(e);
    }
  }
}
