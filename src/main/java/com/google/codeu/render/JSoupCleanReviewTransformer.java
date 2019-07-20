package com.google.codeu.render;

import com.google.codeu.data.Review;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

/** Removes undesirable HTML elements from a review text. */
public class JSoupCleanReviewTransformer implements ReviewTransformer {

  @Override
  public Review transform(Review review) {
    String text =
        Jsoup.clean(
            review.getText(), "", Whitelist.basic(), new OutputSettings().prettyPrint(false));
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
