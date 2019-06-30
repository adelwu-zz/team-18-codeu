package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Review;
import com.google.codeu.render.JSoupCleanReviewTransformer;
import com.google.codeu.render.ReviewTransformer;
import com.google.codeu.render.SequentialReviewTransformer;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Handles fetching all messages for the public feed. */
@WebServlet("/feed")
public class ReviewFeedServlet extends HttpServlet {

  private Datastore datastore;
  private ReviewTransformer reviewTransformer;

  @Override
  public void init() {
    datastore = new Datastore();
    reviewTransformer =
        new SequentialReviewTransformer(Arrays.asList(new JSoupCleanReviewTransformer()));
  }

  /** Responds with a JSON representation of Review data for all users. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    List<Review> reviews = datastore.getAllReviews();
    List<Review> transformedReviews = new ArrayList<>();
    for (Review review : reviews) {
      transformedReviews.add(reviewTransformer.transform(review));
    }
    Gson gson = new Gson();
    String json = gson.toJson(transformedReviews);

    response.getOutputStream().println(json);
  }
}
