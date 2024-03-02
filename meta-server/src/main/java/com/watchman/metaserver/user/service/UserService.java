package com.watchman.metaserver.user.service;

import com.watchman.metaserver.user.model.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public interface UserService {
  void createUser(User user, @NotBlank String userName, @NotBlank String password);
  boolean isAdmin(String userName, String password);
  Optional<User> findById(String userName);
}
