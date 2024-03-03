package com.watchman.metaserver.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long eventId;

  @NotBlank private String eventName;
  // TODO: add sampling support
  // TODO: add data deletion policy support
  // TODO: add time zone support
  @OneToMany(targetEntity = Dimension.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @NotEmpty
  private List<Dimension> dimensions;

  @OneToMany(targetEntity = ApiKey.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<ApiKey> apiKeys;
}
