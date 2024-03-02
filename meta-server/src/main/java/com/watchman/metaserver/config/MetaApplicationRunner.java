package com.watchman.metaserver.config;

import com.watchman.metaserver.user.model.User;
import com.watchman.metaserver.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MetaApplicationRunner implements ApplicationRunner {
  @Autowired UserRepository userRepository;

  @Value("${WatchMan.rootUser.password}")
  private String watchManUserPassword;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    createWatchManUser();
  }

  private void createWatchManUser() {
    final String watchManUserName = "WatchMan";

    User watchMan = new User();
    watchMan.setUserName(watchManUserName);
    watchMan.setPassword(watchManUserPassword);

    // save user
    userRepository.save(watchMan);
    System.out.println("WatchMan User Updated!");
  }
}
