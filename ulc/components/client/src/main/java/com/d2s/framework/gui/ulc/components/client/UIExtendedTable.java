/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.awt.event.FocusListener;

import javax.swing.JTable;

import com.ulcjava.base.client.UITable;
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
public class UIExtendedTable extends UITable {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(@SuppressWarnings("unused")
  Anything args) {
    JTable table = new UiJTable();
    table.setSurrendersFocusOnKeystroke(true);
    return table;
  }

  private class UiJTable extends com.ulcjava.base.client.UITable.UiJTable {

    private static final long serialVersionUID = -2086620865057853061L;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addFocusListener(FocusListener l) {
      if (l.getClass().getName().startsWith("com.ulcjava.base.client.UITable")) {
        return;
      }
      super.addFocusListener(l);
    }
  }

}
