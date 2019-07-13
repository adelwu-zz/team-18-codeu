package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Hub;
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

@WebServlet("/hub-list")
public class HubListServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();

  }

  /** Responds with a JSON representation of all Hub data. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    List<Hub> hubs = datastore.getAllHubs();
    Gson gson = new Gson();
    String json = gson.toJson(hubs);
    response.getOutputStream().println(json);
  }
}
