/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
