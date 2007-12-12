/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import com.d2s.framework.gui.ulc.components.server.event.ExtendedInternalFrameEvent;
import com.d2s.framework.gui.ulc.components.server.event.IExtendedInternalFrameListener;
import com.d2s.framework.gui.ulc.components.shared.ExtendedInternalFrameConstants;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC internal frame extended to take care of activation events.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCExtendedInternalFrame extends
    com.ulcjava.base.application.ULCInternalFrame {

  private static final long serialVersionUID = -4580618082523464646L;

  /**
   * Constructs a new <code>ULCExtendedInternalFrame</code> instance.
   * 
   * @param title
   *            the frame title.
   */
  public ULCExtendedInternalFrame(String title) {
    super(title);
  }

  /**
   * Adds an extended listener.
   * 
   * @param listener
   *            the listener.
   */
  public void addExtendedInternalFrameListener(
      IExtendedInternalFrameListener listener) {
    internalAddListener(
        ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT, listener);
  }

  /**
   * Removes an extended listener.
   * 
   * @param listener
   *            the listener.
   */
  public void removeExtendedInternalFrameListener(
      IExtendedInternalFrameListener listener) {
    internalRemoveListener(
        ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleEvent(int listenerType, int eventId, Anything args) {
    if (listenerType == ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT) {
      distributeToListeners(new ExtendedInternalFrameEvent(this, eventId));
    } else {
      super.handleEvent(listenerType, eventId, args);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIExtendedInternalFrame";
  }
}
