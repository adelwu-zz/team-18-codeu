package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Hub;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hub")
public class HubServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UUID hubId = UUID.fromString(request.getParameter("hubId"));
    Hub hub = datastore.getHub(hubId);

    Gson gson = new Gson();
    String json = gson.toJson(hub);

    response.getWriter().println(json);
  }
}
