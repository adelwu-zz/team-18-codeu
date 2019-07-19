package com.google.codeu.render;

import com.google.codeu.analysis.SentimentAnalyzer;
import com.google.codeu.data.Review;

/** Replaces image Urls with HTML image elements. */
public class SentimentReviewTransformer implements ReviewTransformer {

  private SentimentAnalyzer sentimentAnalyzer;

  public SentimentReviewTransformer() {
    sentimentAnalyzer = SentimentAnalyzer.getInstance();
  }

  @Override
  public Review transform(Review review) {
    String text = review.getText();
    text +=
        "\n\n<p style=\"color: grey\">"
            + "Sentiment score: "
            + sentimentAnalyzer.score(text)
            + "</p>";

    return new Review(
        review.getId(),
        review.getUser(),
        text,
        review.getTimestamp(),
        review.getHubId(),
        review.getHub(),
        review.getRating());
  }
}
