package com.watchman.eventserver.log.service;

import com.watchman.eventserver.log.model.Event;

import java.io.IOException;

public interface EventService {
  void publish(Event event,String apiKey) throws IOException;
}
