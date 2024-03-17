package com.watchman.metaserver.event;

import com.watchman.metaserver.event.dto.ApiKeyDTO;
import com.watchman.metaserver.event.dto.ApiKeyMeta;
import com.watchman.metaserver.event.dto.GenerateApiKeyDTO;
import com.watchman.metaserver.event.model.Event;
import com.watchman.metaserver.event.service.EventService;
import com.watchman.metaserver.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
  EventService eventService;
  UserService userService;

  EventController(EventService eventService, UserService userService) {
    this.eventService = eventService;
    this.userService = userService;
  }

  @PostMapping
  public Event createEvent(
      @Valid @RequestBody Event event, @NotBlank @RequestHeader String userName) {
    return eventService.createEvent(event, userName);
  }

  @PostMapping("/generate-api-key")
  public ApiKeyDTO generateApiKey(
      @Valid @RequestBody GenerateApiKeyDTO generateApiKeyDTO,
      @NotBlank @RequestHeader String userName) {

    String apiKey =
        eventService.generateApiKey(
            userName, generateApiKeyDTO.getEventId(), generateApiKeyDTO.getExpiryTime());

    return new ApiKeyDTO(apiKey);
  }

  @GetMapping("/api-key-meta")
  public ApiKeyMeta getApiKeyMeta(@RequestParam String apiKey) {
    List<Object> apiKeyMetaList = userService.fetchApiKeyMeta(apiKey);
    if(apiKeyMetaList.isEmpty()){
        return null;
    }

    Object[] keys = (Object[]) apiKeyMetaList.get(0);

    ApiKeyMeta apiKeyMeta = new ApiKeyMeta();
    apiKeyMeta.setUserName((String) keys[0]);
    apiKeyMeta.setEventId((long) keys[1]);
    apiKeyMeta.setApiKey((String) keys[2]);
    apiKeyMeta.setExpiryTime((long) keys[3]);

    return apiKeyMeta;
  }
}
