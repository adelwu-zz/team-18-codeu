package com.google.codeu.render;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import com.google.codeu.data.Message;
import org.junit.Test;

public class BasicSentimenterTest {
  /**
   * Tests that a particular input True means score is positive or > 0. False means score is
   * negative.
   *
   * @throws IOException
   */
  private void runReviewSentimenterTest(
      ReviewSentimenter reviewSentimenter, String inputText, boolean expectedSign)
      throws IOException {
    Message inputMessage = new Message("user1", inputText);

    // Runs the method under test.
    float score = reviewSentimenter.getSentimentScore(inputMessage);

    boolean actualSign = score >= 0 ? true : false;

    boolean result = (expectedSign && actualSign || !expectedSign && !actualSign) ? true : false;

    // Checks that the expect output is produced.
    assert (result);

    // Checks that nothing else changed.
    assertEquals(inputMessage.getId(), inputMessage.getId());
    assertEquals(inputMessage.getUser(), inputMessage.getUser());
    assertEquals(inputMessage.getTimestamp(), inputMessage.getTimestamp());
  }

  @Test
  public void testReviewSentimenter() throws IOException {
    ReviewSentimenter reviewSentimenter = new BasicSentimenter();

    runReviewSentimenterTest(reviewSentimenter, "I hate the world", false);
    runReviewSentimenterTest(reviewSentimenter, "I love the world", true);

    runReviewSentimenterTest(reviewSentimenter, "The food was terrible!", false);
    runReviewSentimenterTest(reviewSentimenter, "The food was awesome!", true);
  }
}
