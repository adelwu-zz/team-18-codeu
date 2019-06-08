package com.google.codeu.render;

import com.google.codeu.data.Message;

/** Rewrites a Message. */
public interface MessageTransformer {

  /** Returns a transformed message text. */
  public Message transform(Message message);
}
