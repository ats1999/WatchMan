package com.watchman.metaserver.event.service;

import com.watchman.metaserver.event.model.Dimension;
import com.watchman.metaserver.event.model.DimensionType;
import com.watchman.metaserver.event.model.Event;
import com.watchman.metaserver.user.model.User;
import com.watchman.metaserver.user.repository.UserRepository;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EventServiceImpl implements EventService {
  UserRepository userRepository;

  EventServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Event createEvent(Event event, String userName) {
    validateUniqueDimension(event);
    // TODO: make sure to follow SOLID
    createDimensionsIndex(event);

    Optional<User> userOptional = userRepository.findById(userName);
    if (userOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User does not found!");
    }

    User user = userOptional.get();
    user.getEvents().add(event);
    userRepository.save(user);

    return user.getEvents().get(user.getEvents().size() - 1);
  }

  // TODO: refactor this method by using lambda and design patterns
  private void createDimensionsIndex(Event event) {
    List<Dimension> dimensions = event.getDimensions();

    // create a map of dimension type
    Map<DimensionType, List<Dimension>> dimensionTypeMap = new HashMap<>();

    for (Dimension dimension : dimensions) {
      DimensionType dimensionType = dimension.getType();
      if (!dimensionTypeMap.containsKey(dimensionType)) {
        dimensionTypeMap.put(dimensionType, new ArrayList<>());
      }

      dimensionTypeMap.get(dimensionType).add(dimension);
    }

    // add index value to each dimension, type wise
    for (Map.Entry<DimensionType, List<Dimension>> entry : dimensionTypeMap.entrySet()) {
      List<Dimension> dimensionList = entry.getValue();
      int counter = 1;

      // add index to non optional dimensions
      for (Dimension dimension : dimensionList) {
        if (!dimension.isOptional()) {
          dimension.setIdx(counter++);
        }
      }

      // add index to optional dimensions, so that we can keep optional dimensions at the end of the
      // array
      for (Dimension dimension : dimensionList) {
        if (dimension.isOptional()) {
          dimension.setIdx(counter++);
        }
      }
    }
  }

  private void validateUniqueDimension(Event event) {
    List<Dimension> dimensions = event.getDimensions();
    Set<String> dimensionNameSet = new HashSet<>();
    dimensions.forEach(dimension -> dimensionNameSet.add(dimension.getName()));
    if (dimensionNameSet.size() != dimensions.size()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dimensions are not unique!");
    }
  }
}
