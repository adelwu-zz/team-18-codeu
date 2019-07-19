package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import com.google.codeu.data.Review;
import java.util.UUID;
import org.junit.Test;

public class ReplaceImageUrlTransformerTest {

  /** Tests that a particular input */
  private void runTransformTextTest(
      ReviewTransformer reviewTransformer, String inputText, String expectedResultText) {
    Review inputReview = new Review("user1", inputText, UUID.randomUUID(), "hub", 5);

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
    ReviewTransformer reviewTransformer = new ReplaceImageUrlReviewTransformer();

    runTransformTextTest(reviewTransformer, "", "");
    runTransformTextTest(reviewTransformer, "Test review!", "Test review!");
    runTransformTextTest(
        reviewTransformer,
        "Test http://tineye.com/images/widgets/mona.jpg",
        "Test <img src=\"http://tineye.com/images/widgets/mona.jpg\" />");
    runTransformTextTest(
        reviewTransformer,
        "Test https://www.gstatic.com/webp/gallery3/1.png With https and .png file",
        "Test <img src=\"https://www.gstatic.com/webp/gallery3/1.png\" /> With https and .png file");
    runTransformTextTest(
        reviewTransformer,
        "Test https://www.gstatic.com/webp/gallery/4.sm.jpg a jpg file and gif file"
            + " And http://evananthony.com/myFiles/gifs/demo_reel_2013_1280x720.gif",
        "Test <img src=\"https://www.gstatic.com/webp/gallery/4.sm.jpg\" /> a jpg file and gif file "
            + "And <img src=\"http://evananthony.com/myFiles/gifs/demo_reel_2013_1280x720.gif\" />");
  }
}
