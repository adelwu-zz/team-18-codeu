package com.google.codeu.render;

import com.google.codeu.data.Review;

/** Rewrites a Review. */
public interface ReviewTransformer {

  /** Returns a transformed Review text. */
  public Review transform(Review review);
}
