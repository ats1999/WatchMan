package com.watchman.metaserver.user.service;

import com.watchman.metaserver.user.model.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public interface UserService {
  void upsertUser(User user, @NotBlank String userName, @NotBlank String password);
  User save(User user);
  void changePassword(String userName, String newPassword);
  boolean isAdmin(String userName, String password);
  Optional<User> findById(String userName);
  List<Object> fetchApiKeyMeta(String apiKey);
}
