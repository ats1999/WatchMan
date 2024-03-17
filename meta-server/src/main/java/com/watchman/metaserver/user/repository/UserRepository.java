package com.watchman.metaserver.user.repository;

import com.watchman.metaserver.user.model.User;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
  @Query(
      """
    SELECT u.userName AS user_name,
           e.eventId AS event_id,
           apikey.apiKey,
           apikey.expiryTime
    FROM User u
    JOIN u.events e
    JOIN e.apiKeys apikey
    WHERE apikey.apiKey = :apiKey
    """)
  List<Object> fetchApiKeyMeta(@Param("apiKey") String apiKey);
}
