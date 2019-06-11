package com.google.codeu.render;

import com.google.codeu.data.Message;

/** Replaces image Urls with HTML image elements. */
public class ReplaceImageUrlMessageTransformer implements MessageTransformer {

  @Override
  public Message transform(Message message) {
    String userText = message.getText();

    String regex = "(https?://\\S+\\.(png|jpg|gif))";
    String replacement = "<img src=\"$1\" />";
    String textWithImagesReplaced = userText.replaceAll(regex, replacement);

    return new Message(message.getId(), message.getUser(), textWithImagesReplaced,
        message.getTimestamp());
  }
}
