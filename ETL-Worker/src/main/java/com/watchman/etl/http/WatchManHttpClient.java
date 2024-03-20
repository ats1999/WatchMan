package com.watchman.etl.http;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;

public interface WatchManHttpClient {
  Optional<Object> get(String url, Map<String, String> headers) throws IOException;

  Optional<Object> post(String url, JSONObject requestBody, Map<String, String> headers) throws IOException;

  Optional<Object> put(String url, JSONObject requestBody, Map<String, String> headers)
      throws IOException;

  Optional<Object> delete(String url, Map<String, String> headers) throws IOException;
}
