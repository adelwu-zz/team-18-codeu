package com.google.codeu.servlets;

import java.io.IOException;

import com.google.codeu.data.Datastore;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.HashSet;
/**
 * Handles fetching all users for the community page.
 */
@WebServlet("/user-list")
public class UserListServlet extends HttpServlet {
  private Datastore datastore;

  @Override
  public void init() {
     datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
      Set<String> users = datastore.getUsers();
      System.out.println(users);
  }
}
