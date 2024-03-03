package com.watchman.metaserver.user.model;

import com.watchman.metaserver.event.model.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class User {
  @Id @NotBlank private String userName;

  // TODO: store hashed password
  @Column(nullable = false)
  @NotBlank
  private String password;

  @OneToMany(fetch = FetchType.LAZY, targetEntity = Event.class, cascade = CascadeType.ALL)
  List<Event> events;
}
