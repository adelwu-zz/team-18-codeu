package com.google.codeu.render;

import java.io.IOException;
import com.google.codeu.data.Message;

/** Gives a review a sentiment score. */
public interface ReviewSentimenter {

  /** Returns a review sentiment score. */
  public float getSentimentScore(Message message) throws IOException;
}
