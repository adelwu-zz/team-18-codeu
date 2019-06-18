package com.google.codeu.servlets;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/review")
public class SentimentAnalysisServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = request.getParameter("message");

    // Get sentiment score.
    Document doc =
        Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    // Output the sentiment score as HTML.
    // We can store it in Datastore once we decide on Sentiment Analysis integration with our
    // project.
    response.setContentType("text/html;");
    response.getOutputStream().println("<h1>Review Analysis</h1>");
    response.getOutputStream().println("<p>Your review: " + message + "</p>");
    response.getOutputStream().println("<p>Your review sentiment score: " + score + "</p>");
    response.getOutputStream().println("<p><a href=\"/\">Back</a></p>");
  }
}
