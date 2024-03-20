package com.watchman.etl.dbsync;

import com.watchman.avro.schema.AvroKafkaEventMessage;
import com.watchman.etl.executor.TaskExecutor;
import com.watchman.etl.worker.WorkerConfiguration;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;

public class DbSyncProducer
    implements com.watchman.etl.worker.Producer<AvroKafkaEventMessage, JSONObject>, Runnable {
  private final WorkerConfiguration workerConfiguration;
  private Producer<String, byte[]> kafkaProducer;
  private static volatile DbSyncProducer dbSyncProducer;

  public DbSyncProducer(
      WorkerConfiguration workerConfiguration) {
    System.out.println("DbSyncProducer started...");
    this.workerConfiguration = workerConfiguration;
  }

  private Properties getConsumerProperties() {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, workerConfiguration.getBootStrapServers());
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
    return props;
  }

  @Override
  public void publish(AvroKafkaEventMessage avroKafkaEventMessage, JSONObject apiKeyMetaData)
      throws IOException {
    String topicName = workerConfiguration.getTopic();
    byte[] message = avroKafkaEventMessage.toByteBuffer().array();
    ProducerRecord<String, byte[]> record = new ProducerRecord<>(topicName, message);
    kafkaProducer.send(record);
  }

  @Override
  public void run() {
    Properties props = getConsumerProperties();
    kafkaProducer = new KafkaProducer<>(props);
  }

  public static com.watchman.etl.worker.Producer<AvroKafkaEventMessage, JSONObject> from(
      WorkerConfiguration workerConfiguration) {
    if (dbSyncProducer == null) {
      synchronized (DbSyncProducer.class) {
        if (dbSyncProducer == null) {
          dbSyncProducer = new DbSyncProducer(workerConfiguration);
        }
      }
    }

    return dbSyncProducer;
  }
}
