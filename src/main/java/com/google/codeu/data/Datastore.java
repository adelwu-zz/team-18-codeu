/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Review in Datastore. */
  public void storeReview(Review review) {
    Entity reviewEntity = new Entity("Review", review.getId().toString());
    reviewEntity.setProperty("user", review.getUser());
    reviewEntity.setProperty("text", review.getText());
    reviewEntity.setProperty("timestamp", review.getTimestamp());
    reviewEntity.setProperty("hubId", review.getHubId().toString());
    reviewEntity.setProperty("rating", review.getRating());
    datastore.put(reviewEntity);
  }

  public List<Review> getAllReviews() {
    return getReviews(null, null);
  }

  /**
   * Gets Reviews posted by a specific user, or for a specific hub, or all reviews if both paramters
   * are null.
   *
   * @return a list of Reviews, sorted by time descending.
   */
  public List<Review> getReviews(String user, UUID hubId) {
    List<Review> reviews = new ArrayList<>();

    Query query = new Query("Review");
    if (user != null) {
      query.setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user));
    } else if (hubId != null) {
      query.setFilter(new Query.FilterPredicate("hubId", FilterOperator.EQUAL, hubId.toString()));
    }
    query.addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String entityUser = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        UUID entityHubId = UUID.fromString((String) entity.getProperty("hubId"));
        int rating = (int) (long) entity.getProperty("rating");

        String hubName = getHub(entityHubId).getName();

        Review review = new Review(id, entityUser, text, timestamp, entityHubId, hubName, rating);
        reviews.add(review);
      } catch (Exception e) {
        System.err.println("Error reading review.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return reviews;
  }

  public Set<String> getUsers() {
    Set<String> users = new HashSet<>();
    Query query = new Query("Review");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      users.add((String) entity.getProperty("user"));
    }
    return users;
  }

  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    datastore.put(userEntity);
  }

  /** Returns the User owned by the email address, or null if no matching User was found. */
  public User getUser(String email) {
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      return null;
    }

    String aboutMe = (String) userEntity.getProperty("aboutMe");
    User user = new User(email, aboutMe);

    return user;
  }

  public void storeHub(Hub hub) {
    Entity hubEntity = new Entity("Hub", hub.getId().toString());
    hubEntity.setProperty("name", hub.getName());
    hubEntity.setProperty("address", hub.getAddress());
    hubEntity.setProperty("gpsLat", hub.getLat());
    hubEntity.setProperty("gpsLong", hub.getLong());
    datastore.put(hubEntity);
  }

  public Hub getHub(UUID hubId) {
    List<Hub> hubs = getAllHubs();
    for (Hub hub : hubs) {
      if (hub.getId().equals(hubId)) {
        return hub;
      }
    }
    return null;
  }

  public List<Hub> getAllHubs() {
    List<Hub> hubs = new ArrayList<>();
    Query query = new Query("Hub");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String name = (String) entity.getProperty("name");
        Double latitude = (Double) entity.getProperty("gpsLat");
        Double longitude = (Double) entity.getProperty("gpsLong");
        String address = (String) entity.getProperty("address");
        Hub hub = new Hub(id, name, address, latitude, longitude);
        hubs.add(hub);
      } catch (Exception e) {
        System.err.println("Error reading Hub");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return hubs;
  }
}
