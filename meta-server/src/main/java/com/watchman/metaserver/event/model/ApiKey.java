package com.watchman.metaserver.event.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ApiKey {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String apiKey;
  private long expiryTime;
}
