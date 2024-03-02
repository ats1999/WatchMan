package com.watchman.metaserver.user;

import com.watchman.metaserver.user.model.UpdatePasswordDTO;
import com.watchman.metaserver.user.model.User;
import com.watchman.metaserver.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
  UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public String createUser(
      @Valid @RequestBody User user,
      @NotBlank @RequestHeader String userName,
      @NotBlank @RequestHeader String password) {
    userService.createUser(user, userName, password);
    return user.getUserName();
  }

  @PostMapping("/update-password")
  public String updatePassword(
      @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
      @NotBlank @RequestHeader String userName,
      @NotBlank @RequestHeader String password) {
    return "";
  }
}
