package com.google.codeu.render;

import com.google.codeu.data.Review;
import java.util.List;

/**
 * Chains together multiple MessageTransformer implementations.
 *
 * <p>This is an example of the Composite Pattern https://en.wikipedia.org/wiki/Composite_pattern
 */
public class SequentialReviewTransformer implements ReviewTransformer {

  private List<ReviewTransformer> delegates;

  public SequentialReviewTransformer(List<ReviewTransformer> delegates) {
    this.delegates = delegates;
  }

  @Override
  public Review transform(Review review) {
    for (ReviewTransformer delegate : delegates) {
      review = delegate.transform(review);
    }
    return review;
  }
}
