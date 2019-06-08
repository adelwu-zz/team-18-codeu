package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.render.JSoupCleanMessageTransformer;
import com.google.codeu.render.MessageTransformer;
import com.google.codeu.render.SequentialMessageTransformer;
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
public class MessageFeedServlet extends HttpServlet {

  private Datastore datastore;
  private MessageTransformer messageTransformer;

  @Override
  public void init() {
    datastore = new Datastore();
    messageTransformer =
        new SequentialMessageTransformer(Arrays.asList(new JSoupCleanMessageTransformer()));
  }

  /** Responds with a JSON representation of Message data for all users. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    List<Message> messages = datastore.getAllMessages();
    List<Message> transformedMessages = new ArrayList<>();
    for (Message message : messages) {
      transformedMessages.add(messageTransformer.transform(message));
    }
    Gson gson = new Gson();
    String json = gson.toJson(transformedMessages);

    response.getOutputStream().println(json);
  }
}
