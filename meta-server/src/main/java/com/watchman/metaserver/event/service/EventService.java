package com.watchman.metaserver.event.service;

import com.watchman.metaserver.event.model.Event;

public interface EventService {
  Event createEvent(Event event,String userName);
}
