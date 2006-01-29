/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.flow;

import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;

/**
 * Base class for all message ULC actions. It just keeps a reference on the
 * message to be displayed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMessageAction extends AbstractUlcAction {

  private String message;

  /**
   * Sets the message.
   * 
   * @param message
   *          the message to set.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Gets the message.
   * 
   * @return the message.
   */
  protected String getMessage() {
    return message;
  }
}
