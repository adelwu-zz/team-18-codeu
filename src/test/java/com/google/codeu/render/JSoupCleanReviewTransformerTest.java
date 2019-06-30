package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import com.google.codeu.data.Review;
import org.junit.Test;

public class JSoupCleanReviewTransformerTest { 

  /** Tests that a particular input */
  private void runTransformTextTest(
      ReviewTransformer reviewTransformer, String inputText, String expectedResultText) {
    Review inputReview = new Review("user1", inputText);

    // Runs the method under test.
    Review actualResult = reviewTransformer.transform(inputReview);

    // Checks that the expect output is produced.
    assertEquals(expectedResultText, actualResult.getText());

    // Checks that nothing else changed.
    assertEquals(inputReview.getId(), actualResult.getId());
    assertEquals(inputReview.getUser(), actualResult.getUser());
    assertEquals(inputReview.getTimestamp(), actualResult.getTimestamp());
  }

  @Test
  public void testTransformText() {
    ReviewTransformer reviewTransformer = new JSoupCleanReviewTransformer();

    runTransformTextTest(reviewTransformer, "", "");
    runTransformTextTest(reviewTransformer, "Test review!", "Test review!");
    runTransformTextTest(reviewTransformer, "Test <b>review</b>!", "Test <b>review</b>!");
    runTransformTextTest(
        reviewTransformer,
        "Test <b>review</b>! With <i>multiple</i> html tags",
        "Test <b>review</b>! With <i>multiple</i> html tags");
    runTransformTextTest(
        reviewTransformer,
        "Test <b>review</b>! And <script>doSomeBadStuff();</script>?",
        "Test <b>review</b>! And ?");
  }
}
