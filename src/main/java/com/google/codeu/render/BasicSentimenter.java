package com.google.codeu.render;

import com.google.codeu.data.Message;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Finds review sentiment score from -1.0 to 1.0. */
public class BasicSentimenter implements ReviewSentimenter {

  @Override
  public float getSentimentScore(Message message) throws IOException {
    String userText = message.getText();

    // Get sentiment score.
    Document doc =
        Document.newBuilder().setContent(userText).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    return score;
  }
}