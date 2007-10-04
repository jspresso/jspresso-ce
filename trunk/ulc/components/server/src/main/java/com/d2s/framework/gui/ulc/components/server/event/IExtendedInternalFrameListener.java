/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server.event;

import com.ulcjava.base.application.event.IEventListener;

/**
 * The extended internat frame listener.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IExtendedInternalFrameListener extends IEventListener {

  /**
   * Whenever the internal frame gets activated.
   * 
   * @param event
   *            the event.
   */
  void internalFrameActivated(ExtendedInternalFrameEvent event);

  /**
   * Whenever the internal frame gets deactivated.
   * 
   * @param event
   *            the event.
   */
  void internalFrameDeactivated(ExtendedInternalFrameEvent event);

  /**
   * Whenever the internal frame gets deiconified.
   * 
   * @param event
   *            the event.
   */
  void internalFrameDeiconified(ExtendedInternalFrameEvent event);

  /**
   * Whenever the internal frame gets iconified.
   * 
   * @param event
   *            the event.
   */
  void internalFrameIconified(ExtendedInternalFrameEvent event);

  /**
   * Whenever the internal frame gets opened.
   * 
   * @param event
   *            the event.
   */
  void internalFrameOpened(ExtendedInternalFrameEvent event);
}
