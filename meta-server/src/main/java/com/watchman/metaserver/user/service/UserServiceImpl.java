package com.watchman.metaserver.user.service;

import com.watchman.metaserver.user.model.User;
import com.watchman.metaserver.user.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {
  UserRepository userRepository;

  UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void upsertUser(User user, @NotBlank String userName, @NotBlank String password) {
    if (!isAdmin(userName, password)) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "You don't have permission to create users!");
    }

    userRepository.save(user);
  }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
  public void changePassword(String userName, String newPassword) {
    User user = new User();
    user.setUserName(userName);
    user.setPassword(newPassword);
    userRepository.save(user);
  }

  @Override
  public boolean isAdmin(String userName, String password) {
    // TODO: move WatchMan to constant
    Optional<User> fetchedUser = userRepository.findById("WatchMan");
    return fetchedUser.isPresent()
        && fetchedUser.get().getUserName().equals(userName)
        && fetchedUser.get().getPassword().equals(password);
  }

  @Override
  public Optional<User> findById(String userName) {
    return userRepository.findById(userName);
  }

    @Override
    public List<Object> fetchApiKeyMeta(String apiKey) {
        return userRepository.fetchApiKeyMeta(apiKey);
    }
}
