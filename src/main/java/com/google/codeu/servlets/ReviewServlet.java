/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Review;
import com.google.codeu.render.JSoupCleanReviewTransformer;
import com.google.codeu.render.ReplaceImageUrlReviewTransformer;
import com.google.codeu.render.ReviewTransformer;
import com.google.codeu.render.SentimentReviewTransformer;
import com.google.codeu.render.SequentialReviewTransformer;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving {@link Review} instances. */
@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {

  private Datastore datastore;
  private ReviewTransformer reviewTransformer;

  @Override
  public void init() {
    datastore = new Datastore();
    reviewTransformer =
        new SequentialReviewTransformer(
            Arrays.asList(
                new JSoupCleanReviewTransformer(),
                new ReplaceImageUrlReviewTransformer(),
                new SentimentReviewTransformer()));
  }

  /**
   * Responds with a JSON representation of {@link Review} data for a specific user or hub. Responds
   * with an empty array if no user or hub is provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    String user = request.getParameter("user");
    String hubId = request.getParameter("hubId");

    List<Review> reviews;
    if (user != null && !user.isEmpty()) {
      reviews = datastore.getReviews(user, null);
    } else if (hubId != null && !hubId.isEmpty()) {
      reviews = datastore.getReviews(null, UUID.fromString(hubId));
    } else {
      // Request is invalid, return empty array
      response.getWriter().println("[]");
      return;
    }

    List<Review> transformedReviews = new ArrayList<>();
    for (Review review : reviews) {
      transformedReviews.add(reviewTransformer.transform(review));
    }
    Gson gson = new Gson();
    String json = gson.toJson(transformedReviews);

    response.getWriter().println(json);
  }

  /** Stores a new {@link Review}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String user = userService.getCurrentUser().getEmail();
    String text = Jsoup.clean(request.getParameter("text"), Whitelist.relaxed());
    UUID hubId = UUID.fromString(request.getParameter("hubId"));
    System.out.print(request.getParameter("rating"));
    int rating = Integer.parseInt(request.getParameter("rating"));

    Review review = new Review(user, text, hubId, "", rating);
    datastore.storeReview(review);

    response.sendRedirect("/user-page.html?user=" + user);
  }
}
