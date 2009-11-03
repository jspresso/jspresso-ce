/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.wings.components;

import java.io.IOException;

import org.jspresso.framework.gui.wings.components.plaf.ClickableHeaderTableCG;
import org.wings.SCellRendererPane;
import org.wings.SComponent;
import org.wings.SForm;
import org.wings.STable;
import org.wings.io.Device;

/**
 * A clickable header STable.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ClickableHeaderSTable extends STable {

  private static final long serialVersionUID           = 5489638938237459509L;

  private SCellRendererPane cellRendererPane;
  private int               delayedClickedHeaderColumn = -1;

  /**
   * Constructs a new <code>ClickableHeaderSTable</code> instance.
   */
  public ClickableHeaderSTable() {
    cellRendererPane = new SCellRendererPane() {

      private static final long serialVersionUID = 3159574506651887983L;

      @Override
      public void writeComponent(Device d, SComponent c, SComponent p)
          throws IOException {
        if (c != null && p instanceof STable) {
          STable renderedTable = (STable) p;
          if (renderedTable.isEditing()
              && renderedTable.getEditorComponent() == c) {
            addComponent(c);
            c.write(d);
          } else {
            super.writeComponent(d, c, p);
          }
        } else {
          super.writeComponent(d, c, p);
        }
      }
    };
    setCG(new ClickableHeaderTableCG());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SCellRendererPane getCellRendererPane() {
    return cellRendererPane;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processLowLevelEvent(String action, String[] values) {
    for (int i = 0; i < values.length; i++) {
      String value = values[i];
      if (value != null && value.length() > 0) {
        char modus = value.charAt(0);
        try {
          // editor event
          switch (modus) {
            case 'o':
              delayedClickedHeaderColumn = Integer.parseInt(value.substring(1));
              SForm.addArmedComponent(this);
              break;
            default:
              break;
          }
        } catch (NumberFormatException ex) {
          // ignored
        }
      }
    }
    super.processLowLevelEvent(action, values);
  }

  /**
   * Adds a <code>HeaderClickListener</code>.
   * 
   * @param listener
   *          the <code>HeaderClickListener</code> to add.
   */
  public void addHeaderClickListener(HeaderClickListener listener) {
    addEventListener(HeaderClickListener.class, listener);
  }

  /**
   * Removes a <code>HeaderClickListener</code>.
   * 
   * @param listener
   *          the <code>HeaderClickListener</code> to remove.
   */
  public void removeHeaderClickListener(HeaderClickListener listener) {
    removeEventListener(HeaderClickListener.class, listener);
  }

  /**
   * fires a <code>HeaderClickEvent</code>.
   * 
   * @param event
   *          the <code>HeaderClickEvent</code> to fire.
   */
  protected void fireHeaderClicked(HeaderClickEvent event) {
    // Guaranteed to return a non-null array
    Object[] listeners = getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == HeaderClickListener.class) {
        ((HeaderClickListener) listeners[i + 1]).headerClicked(event);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireFinalEvents() {
    super.fireFinalEvents();
    if (delayedClickedHeaderColumn != -1) {
      fireHeaderClicked(new HeaderClickEvent(this, delayedClickedHeaderColumn));
      delayedClickedHeaderColumn = -1;
    }
  }
}
