package com.google.codeu.analysis;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;

public class SentimentAnalyzer {

  private LanguageServiceClient languageService;

  private static SentimentAnalyzer instance = null;

  /** Returns singleton instance. */
  public static SentimentAnalyzer getInstance() {
    if (instance == null) {
      instance = new SentimentAnalyzer();
    }
    return instance;
  }

  private SentimentAnalyzer() {
    try {
      languageService = LanguageServiceClient.create();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Returns a score between -1.0 and 1.0 reflecting the sentiment of the text. */
  public float score(String text) {
    Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    return sentiment.getScore();
  }
}
