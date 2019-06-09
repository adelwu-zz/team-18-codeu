package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import com.google.codeu.data.Message;
import org.junit.Test;

public class JSoupCleanMessageTransformerTest {

  /** Tests that a particular input */
  private void runTransformTextTest(
      MessageTransformer messageTransformer, String inputText, String expectedResultText) {
    Message inputMessage = new Message("user1", inputText);

    // Runs the method under test.
    Message actualResult = messageTransformer.transform(inputMessage);

    // Checks that the expect output is produced.
    assertEquals(expectedResultText, actualResult.getText());

    // Checks that nothing else changed.
    assertEquals(inputMessage.getId(), actualResult.getId());
    assertEquals(inputMessage.getUser(), actualResult.getUser());
    assertEquals(inputMessage.getTimestamp(), actualResult.getTimestamp());
  }

  @Test
  public void testTransformText() {
    MessageTransformer messageTransformer = new JSoupCleanMessageTransformer();

    runTransformTextTest(messageTransformer, "", "");
    runTransformTextTest(messageTransformer, "Test message!", "Test message!");
    runTransformTextTest(messageTransformer, "Test <b>message</b>!", "Test <b>message</b>!");
    runTransformTextTest(
        messageTransformer,
        "Test <b>message</b>! With <i>multiple</i> html tags",
        "Test <b>message</b>! With <i>multiple</i> html tags");
    runTransformTextTest(
        messageTransformer,
        "Test <b>message</b>! And <script>doSomeBadStuff();</script>?",
        "Test <b>message</b>! And ?");
  }
}
