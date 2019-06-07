package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import com.google.codeu.data.Message;
import java.util.Arrays;
import org.junit.Test;
import org.mockito.Mockito;

public class SequentialMessageTransformerTest {

  @Test
  /** Tests that when there are no delegates, the input is return untransformed. */
  public void testTransformTextNoDelegates() {
    // Constructs the class under test.
    MessageTransformer messageTransformer = new SequentialMessageTransformer(Arrays.asList());

    Message message0 = new Message("user0", "an input text");

    assertEquals(message0, messageTransformer.transform(message0));
  }

  @Test
  /** Tests when there is exactly one delegate, its output is returned. */
  public void testTransformTextOneDelegate() {
    MessageTransformer mockDelegate = Mockito.mock(MessageTransformer.class);

    Message message0 = new Message("user0", "an input text");
    Message message1 = new Message("user1", "mock output 1");

    Mockito.when(mockDelegate.transform(message0)).thenReturn(message1);

    // Constructs the class under test.
    MessageTransformer messageTransformer =
        new SequentialMessageTransformer(Arrays.asList(mockDelegate));

    assertEquals(message1, messageTransformer.transform(message0));
  }

  @Test
  /** Tests a sequence of three delegates are run correctly. */
  public void testTransformTextSequenceOfThree() {
    MessageTransformer mockDelegate1 = Mockito.mock(MessageTransformer.class);
    MessageTransformer mockDelegate2 = Mockito.mock(MessageTransformer.class);
    MessageTransformer mockDelegate3 = Mockito.mock(MessageTransformer.class);

    Message message0 = new Message("user0", "an input text");
    Message message1 = new Message("user1", "mock output 1");
    Message message2 = new Message("user2", "mock output 2");
    Message message3 = new Message("user3", "mock output 3");

    Mockito.when(mockDelegate1.transform(message0)).thenReturn(message1);
    Mockito.when(mockDelegate2.transform(message1)).thenReturn(message2);
    Mockito.when(mockDelegate3.transform(message2)).thenReturn(message3);

    // Constructs the class under test.
    MessageTransformer messageTransformer =
        new SequentialMessageTransformer(
            Arrays.asList(mockDelegate1, mockDelegate2, mockDelegate3));

    assertEquals(message3, messageTransformer.transform(message0));
  }
}
