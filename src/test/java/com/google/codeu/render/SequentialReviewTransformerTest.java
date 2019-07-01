package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import com.google.codeu.data.Review;
import java.util.Arrays;
import org.junit.Test;
import org.mockito.Mockito;

public class SequentialReviewTransformerTest {

  @Test
  /** Tests that when there are no delegates, the input is return untransformed. */
  public void testTransformTextNoDelegates() {
    // Constructs the class under test.
    ReviewTransformer reviewTransformer = new SequentialReviewTransformer(Arrays.asList());

    Review review0 = new Review("user0", "an input text");

    assertEquals(review0, reviewTransformer.transform(review0));
  }

  @Test
  /** Tests when there is exactly one delegate, its output is returned. */
  public void testTransformTextOneDelegate() {
    ReviewTransformer mockDelegate = Mockito.mock(ReviewTransformer.class);

    Review review0 = new Review("user0", "an input text");
    Review review1 = new Review("user1", "mock output 1");

    Mockito.when(mockDelegate.transform(review0)).thenReturn(review1);

    // Constructs the class under test.
    ReviewTransformer reviewTransformer =
        new SequentialReviewTransformer(Arrays.asList(mockDelegate));

    assertEquals(review1, reviewTransformer.transform(review0));
  }

  @Test
  /** Tests a sequence of three delegates are run correctly. */
  public void testTransformTextSequenceOfThree() {
    ReviewTransformer mockDelegate1 = Mockito.mock(ReviewTransformer.class);
    ReviewTransformer mockDelegate2 = Mockito.mock(ReviewTransformer.class);
    ReviewTransformer mockDelegate3 = Mockito.mock(ReviewTransformer.class);

    Review review0 = new Review("user0", "an input text");
    Review review1 = new Review("user1", "mock output 1");
    Review review2 = new Review("user2", "mock output 2");
    Review review3 = new Review("user3", "mock output 3");

    Mockito.when(mockDelegate1.transform(review0)).thenReturn(review1);
    Mockito.when(mockDelegate2.transform(review1)).thenReturn(review2);
    Mockito.when(mockDelegate3.transform(review2)).thenReturn(review3);

    // Constructs the class under test.
    ReviewTransformer reviewTransformer =
        new SequentialReviewTransformer(
            Arrays.asList(mockDelegate1, mockDelegate2, mockDelegate3));

    assertEquals(review3, reviewTransformer.transform(review0));
  }
}
