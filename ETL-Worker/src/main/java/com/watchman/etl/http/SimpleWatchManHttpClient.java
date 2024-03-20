package com.watchman.etl.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import org.json.JSONObject;

public class SimpleWatchManHttpClient implements WatchManHttpClient {
  private final String userName;
  private final String password;

  SimpleWatchManHttpClient(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  @Override
  public Optional<Object> get(String url, Map<String, String> headers) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("GET");
    setHeaders(connection, headers);

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      return readResponse(connection);
    }
    throw new IOException("GET request failed with response code: " + responseCode);
  }

  @Override
  public Optional<Object> post(String url, JSONObject requestBody, Map<String, String> headers)
      throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("POST");
    setHeaders(connection, headers);

    connection.setDoOutput(true);
    try (OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
      writer.write(requestBody.toString());
      writer.flush();
    }

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      return readResponse(connection);
    }
    throw new IOException("POST request failed with response code: " + responseCode);
  }

  @Override
  public Optional<Object> put(String url, JSONObject requestBody, Map<String, String> headers)
      throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("PUT");
    setHeaders(connection, headers);

    connection.setDoOutput(true);
    try (OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
      writer.write(requestBody.toString());
      writer.flush();
    }

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      return readResponse(connection);
    }
    throw new IOException("PUT request failed with response code: " + responseCode);
  }

  @Override
  public Optional<Object> delete(String url, Map<String, String> headers) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("DELETE");
    setHeaders(connection, headers);

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      return readResponse(connection);
    }
    throw new IOException("DELETE request failed with response code: " + responseCode);
  }

  private void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
    if (headers != null) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        connection.setRequestProperty(entry.getKey(), entry.getValue());
      }
    }

    connection.setRequestProperty("userName", this.userName);
    connection.setRequestProperty("password", this.password);
  }

  private Optional<Object> readResponse(HttpURLConnection connection) throws IOException {
    StringBuilder response = new StringBuilder();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
    }
    if (response.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(new JSONObject(response.toString()));
  }
}
