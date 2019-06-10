package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import com.google.codeu.data.Message;
import org.junit.Test;

public class ReplaceImageUrlTransformerTest {
	
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
	    MessageTransformer messageTransformer = new ReplaceImageUrlMessageTransformer();

	    runTransformTextTest(messageTransformer, "", "");
	    runTransformTextTest(messageTransformer, "Test message!", "Test message!");
	    runTransformTextTest(messageTransformer, "Test http://tineye.com/images/widgets/mona.jpg", 
	    		"Test <img src=\"http://tineye.com/images/widgets/mona.jpg\" />");
	   runTransformTextTest(
	        messageTransformer,
	        "Test https://www.gstatic.com/webp/gallery3/1.png With https and .png file",
	        "Test <img src=\"https://www.gstatic.com/webp/gallery3/1.png\" /> With https and .png file");
	      runTransformTextTest(
	        messageTransformer,
	        "Test https://www.gstatic.com/webp/gallery/4.sm.jpg a jpg file and gif file"
	        + " And http://evananthony.com/myFiles/gifs/demo_reel_2013_1280x720.gif",
	        "Test <img src=\"https://www.gstatic.com/webp/gallery/4.sm.jpg\" /> a jpg file and gif file "
	        + "And <img src=\"http://evananthony.com/myFiles/gifs/demo_reel_2013_1280x720.gif\" />");
	  }
}
