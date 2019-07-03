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
    reviewEntity.setProperty("hub", review.getHub());
    reviewEntity.setProperty("rating", review.getRating());

    datastore.put(reviewEntity);
  }

  /**
   * Gets Reviews posted by a specific user.
   *
   * @return a list of Reviews posted by the user, or empty list if user has never posted a
   *     Review. List is sorted by time descending.
   */
  public List<Review> getReviews(String user) {
    return reviewGet(user);
  }

  public List<Review> getAllReviews() {
    return reviewGet(null);
  }

  /**
   * Repeated code from getReviews and getAllReviews.
   *
   * @return a list of Reviews posted by the user, or empty list if user has never posted a
   *     Review. List is sorted by time descending.
   */
  public List<Review> reviewGet(String user) {
    List<Review> reviews = new ArrayList<>();

    Query query;
    if (user == null) {
      query = new Query("Review").addSort("timestamp", SortDirection.DESCENDING);
    } else {
      query =
          new Query("Review")
              .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
              .addSort("timestamp", SortDirection.DESCENDING);
    }

    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String entityUser = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        String hub = (String) entity.getProperty("hub");
        int rating = (int) (long) entity.getProperty("rating");

        Review review = new Review(id, entityUser, text, timestamp, hub, rating);
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
}
