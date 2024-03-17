package com.watchman.metaserver.event.dto;

import lombok.Data;

@Data
public class ApiKeyMeta {
  private String userName;
  private String apiKey;
  private long eventId;
  private long expiryTime;
}
