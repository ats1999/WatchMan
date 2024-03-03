package com.watchman.metaserver.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Dimension {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotBlank private String name;
  private int idx;
  private boolean optional;

  @Enumerated(EnumType.ORDINAL)
  private DimensionType type;
}
