/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.security.ulc;

/**
 * This interface is implemented by listeners on auth callback handlers.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICallbackHandlerListener {

  /**
   * Notified whenever the login process is complete from the user point of
   * view.
   */
  void callbackHandlingComplete();

}
