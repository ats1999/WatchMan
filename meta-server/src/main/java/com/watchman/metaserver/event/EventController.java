package com.watchman.metaserver.event;

import com.watchman.metaserver.event.dto.GenerateApiKeyDTO;
import com.watchman.metaserver.event.model.Event;
import com.watchman.metaserver.event.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {
  EventService eventService;

  EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping
  public Event createEvent(
      @Valid @RequestBody Event event, @NotBlank @RequestHeader String userName) {
    return eventService.createEvent(event, userName);
  }

  @PostMapping("/generate-api-key")
  public String generateApiKey(
      @Valid @RequestBody GenerateApiKeyDTO generateApiKeyDTO,
      @NotBlank @RequestHeader String userName) {
    return eventService.generateApiKey(
        userName, generateApiKeyDTO.getEventId(), generateApiKeyDTO.getExpiryTime());
  }
}