/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.launch.ulc;

import java.io.File;

import com.ulcjava.base.client.IMessageService;
import com.ulcjava.base.client.UISession;

/**
 * A message handler that alows to test for the existence of a file.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FileExists implements IMessageService {

  /**
   * <code>MESSAGE_ROOT</code>.
   */
  private static final String MESSAGE_ROOT = "fileExists";

  /**
   * <code>SEPARATOR</code>.
   */
  private static final String SEPARATOR    = "::";

  /**
   * {@inheritDoc}
   */
  public void handleMessage(String msg) {
    if (msg.startsWith(MESSAGE_ROOT)) {
      String[] messageParts = msg.split(SEPARATOR);
      if (messageParts.length != 2) {
        throw new IllegalArgumentException("usage: " + MESSAGE_ROOT + " <file>");
      }

      String pathname = messageParts[1];
      File file = new File(pathname);
      UISession.currentSession().sendMessage(Boolean.toString(file.exists()));
    }
  }
}
