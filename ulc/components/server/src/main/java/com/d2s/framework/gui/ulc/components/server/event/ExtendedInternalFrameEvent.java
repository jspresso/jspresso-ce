/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server.event;

import com.d2s.framework.gui.ulc.components.shared.ExtendedInternalFrameConstants;
import com.ulcjava.base.application.event.IEventListener;
import com.ulcjava.base.application.event.UlcEvent;

/**
 * Event for ExtendedInternalFrame.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ExtendedInternalFrameEvent extends UlcEvent {

  private static final long serialVersionUID = 2154665669592922088L;

  private int               fId;

  /**
   * Constructs a new <code>ExtendedInternalFrameEvent</code> instance.
   * 
   * @param source
   *            the event source.
   * @param id
   *            the event id.
   */
  public ExtendedInternalFrameEvent(Object source, int id) {
    super(source);
    fId = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispatch(IEventListener listener) {
    if (listener instanceof IExtendedInternalFrameListener) {
      IExtendedInternalFrameListener extendedInternalFrameListener = (IExtendedInternalFrameListener) listener;

      if (fId == ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_ACTIVATED) {

        extendedInternalFrameListener.internalFrameActivated(this);
      } else if (fId == ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_DEACTIVATED) {

        extendedInternalFrameListener.internalFrameDeactivated(this);
      } else if (fId == ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_DEICONIFIED) {

        extendedInternalFrameListener.internalFrameDeiconified(this);
      } else if (fId == ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_ICONIFIED) {

        extendedInternalFrameListener.internalFrameIconified(this);
      } else if (fId == ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_OPENED) {

        extendedInternalFrameListener.internalFrameOpened(this);
      } else {
        throw new IllegalArgumentException("Illegal event id : " + fId);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getCategory() {
    return ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT;
  }
}
