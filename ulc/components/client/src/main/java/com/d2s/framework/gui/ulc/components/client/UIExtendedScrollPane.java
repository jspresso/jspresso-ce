/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ulcjava.base.client.UIScrollPane;
import com.ulcjava.base.shared.internal.Anything;

/**
 * This subclass implements some default behaviors which are not yet
 * configurable using ULC.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIExtendedScrollPane extends UIScrollPane {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(@SuppressWarnings("unused")
  Anything args) {
    JScrollPane scrollPane = new UiJScrollPane();
    return scrollPane;
  }

  private class UiJScrollPane extends com.ulcjava.base.client.UiJScrollPane {

    private static final long serialVersionUID = 7399016313250306389L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCorner(String key, Component corner) {
      if (key == ScrollPaneConstants.LOWER_TRAILING_CORNER) {
        super.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, corner);
      } else if (key == ScrollPaneConstants.LOWER_LEADING_CORNER) {
        super.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, corner);
      } else if (key == ScrollPaneConstants.UPPER_TRAILING_CORNER) {
        super.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, corner);
      } else if (key == ScrollPaneConstants.UPPER_LEADING_CORNER) {
        super.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, corner);
      } else {
        super.setCorner(key, corner);
      }
    }
  }

}
