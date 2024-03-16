package com.watchman.eventserver.log.service;

import com.watchman.avro.schema.AvroEvent;
import com.watchman.avro.schema.AvroKafkaEventMessage;
import com.watchman.eventserver.log.model.Event;
import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  @Value("${kafka.event.topic.event_validation}")
  private String topic;

  EventServiceImpl(KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void publish(Event event, String apiKey) throws IOException {
    event.setTimeStamp(Instant.now().toEpochMilli());

    AvroEvent avroEvent =
        AvroEvent.newBuilder()
            .setEventId(event.getEventId())
            .setUserName(event.getUserName())
            .setTimeStamp(event.getTimeStamp())
            .setCountry(event.getCountry())
            .setBrowser(event.getBrowser())
            .setDevice(event.getDevice())
            .setPayload(event.getPayload())
            .build();

    AvroKafkaEventMessage avroKafkaEventMessage =
        AvroKafkaEventMessage.newBuilder().setApiKey(apiKey).setEvent(avroEvent).build();

    kafkaTemplate.send(topic, avroKafkaEventMessage.toByteBuffer().array());
  }
}
