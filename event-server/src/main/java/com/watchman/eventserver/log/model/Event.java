package com.watchman.eventserver.log.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Data;

@Data
public class Event {
  @NotBlank private String eventId;
  @NotBlank private String userName;
  private long timeStamp;
  @NotBlank private String country;
  @NotBlank private String browser;
  @NotBlank private String device;
  private @NotNull Map<CharSequence, Object> payload;
}
