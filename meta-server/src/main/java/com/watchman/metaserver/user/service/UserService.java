package com.watchman.metaserver.user.service;

import com.watchman.metaserver.user.model.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public interface UserService {
  void upsertUser(User user, @NotBlank String userName, @NotBlank String password);
  void changePassword(String userName, String newPassword);
  boolean isAdmin(String userName, String password);
  Optional<User> findById(String userName);
}
