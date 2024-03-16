package com.watchman.eventserver.log;

import com.watchman.eventserver.log.model.Event;
import com.watchman.eventserver.log.service.EventService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/event")
public class EventController {
  EventService eventService;

  EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping("/log")
  public String log(@Valid @RequestBody Event event, @RequestHeader String apiKey) throws IOException {
    eventService.publish(event, apiKey);
    return "Event Published...";
  }
}
