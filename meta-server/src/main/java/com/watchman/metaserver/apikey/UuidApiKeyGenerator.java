package com.watchman.metaserver.apikey;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UuidApiKeyGenerator implements ApiKeyGenerator {

  @Override
  public String generateApiKey() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
