package com.google.codeu.render;

import com.google.codeu.data.Message;
import java.util.List;

/**
 * Chains together multiple MessageTransformer implementations.
 *
 * <p>This is an example of the Composite Pattern https://en.wikipedia.org/wiki/Composite_pattern
 */
public class SequentialMessageTransformer implements MessageTransformer {

  private List<MessageTransformer> delegates;

  public SequentialMessageTransformer(List<MessageTransformer> delegates) {
    this.delegates = delegates;
  }

  @Override
  public Message transform(Message message) {
    for (MessageTransformer delegate : delegates) {
      message = delegate.transform(message);
    }
    return message;
  }
}
