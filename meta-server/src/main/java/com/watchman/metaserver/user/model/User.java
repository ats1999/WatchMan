package com.watchman.metaserver.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class User {
  @Id @NotBlank private String userName;

  // TODO: store hashed password
  @Column(nullable = false)
  @NotBlank
  private String password;
}
