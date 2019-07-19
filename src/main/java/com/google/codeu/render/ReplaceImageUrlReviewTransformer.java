package com.google.codeu.render;

import com.google.codeu.data.Review;

/** Replaces image Urls with HTML image elements. */
public class ReplaceImageUrlReviewTransformer implements ReviewTransformer {

  @Override
  public Review transform(Review review) {
    String userText = review.getText();

    String regex = "(https?://\\S+\\.(png|jpg|gif))";
    String replacement = "<img src=\"$1\" />";
    String textWithImagesReplaced = userText.replaceAll(regex, replacement);

    return new Review(
        review.getId(), review.getUser(), textWithImagesReplaced, review.getTimestamp(), review.getHubId(), review.getHub(),
        review.getRating());
  }
}
