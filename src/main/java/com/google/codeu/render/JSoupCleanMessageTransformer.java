package com.google.codeu.render;

import com.google.codeu.data.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

/** Removes undesirable HTML elements from a message text. */
public class JSoupCleanMessageTransformer implements MessageTransformer {

  @Override
  public Message transform(Message message) {
    String text =
        Jsoup.clean(
            message.getText(), "", Whitelist.basic(), new OutputSettings().prettyPrint(false));
    return new Message(message.getId(), message.getUser(), text, message.getTimestamp());
  }
}
