package com.watchman.metaserver.auth;

import com.watchman.metaserver.user.model.User;
import com.watchman.metaserver.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationHandlerInterceptor implements HandlerInterceptor {
  UserService userService;

  AuthorizationHandlerInterceptor(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String userName = request.getHeader("userName");
    String password = request.getHeader("password");

    if (userName == null || password == null) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("User name and password is required!");
      return false;
    }

    Optional<User> userOptional = userService.findById(userName);
    if (userOptional.isEmpty()) {
      response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
      response.getWriter().write("User is not found!");
      return false;
    }

    User user = userOptional.get();
    if (!user.getPassword().equals(password)) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("Password is not valid!");
      return false;
    }

    return true;
  }
}
