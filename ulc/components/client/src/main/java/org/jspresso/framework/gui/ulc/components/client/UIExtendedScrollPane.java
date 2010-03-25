/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.gui.ulc.components.client;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ulcjava.base.client.UIScrollPane;
import com.ulcjava.base.shared.internal.Anything;

/**
 * This subclass implements some default behaviors which are not yet
 * configurable using ULC.
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
