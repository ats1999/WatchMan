package com.watchman.metaserver.event.dto;

import lombok.Data;

@Data
public class GenerateApiKeyDTO {
  private long eventId;
  private long expiryTime;
}
