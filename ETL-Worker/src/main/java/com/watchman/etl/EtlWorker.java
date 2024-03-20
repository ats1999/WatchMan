package com.watchman.etl;

import com.watchman.avro.schema.AvroKafkaEventMessage;
import com.watchman.etl.config.AppConfiguration;
import com.watchman.etl.dbsync.DbSyncProducer;
import com.watchman.etl.dbsync.config.DbSyncProducerConfiguration;
import com.watchman.etl.executor.TaskExecutor;
import com.watchman.etl.executor.ThreadTaskExecutor;
import com.watchman.etl.validation.config.EventValidationConsumerConfiguration;
import com.watchman.etl.validation.consumer.EventValidationConsumer;
import com.watchman.etl.worker.Producer;
import com.watchman.etl.worker.WorkerConfiguration;
import java.util.concurrent.Future;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.JSONObject;

public class EtlWorker {
  public static void main(String[] args) throws ConfigurationException {
    TaskExecutor<Runnable, Future<?>> threadWorker = ThreadTaskExecutor.getInstance();

    WorkerConfiguration dbSyncWorkerConfiguration =
        DbSyncProducerConfiguration.from(AppConfiguration.getInstance());
    Producer<AvroKafkaEventMessage, JSONObject> dbSyncProducer =
        DbSyncProducer.from(dbSyncWorkerConfiguration, threadWorker);

    // event validation consumer
    WorkerConfiguration eventValidationConsumerConfiguration =
        EventValidationConsumerConfiguration.getInstance();
    EventValidationConsumer eventValidationConsumer =
        new EventValidationConsumer(
            threadWorker, eventValidationConsumerConfiguration,dbSyncProducer);

    new Thread(eventValidationConsumer).start();
      new Thread((Runnable) dbSyncProducer).start();

  }
}
