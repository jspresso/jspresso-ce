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
package org.jspresso.framework.view.ulc;

import org.jspresso.framework.util.ulc.UlcUtil;

import com.ulcjava.base.application.DefaultListCellRenderer;
import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCList;

/**
 * A default list cell renderer rendering even and odd rows background slightly
 * differently.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddListCellRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = 3450896483325205907L;

  /**
   * {@inheritDoc}
   */
  @Override
  public IRendererComponent getListCellRendererComponent(ULCList list,
      Object value, boolean isSelected, boolean hasFocus, int row) {
    IRendererComponent renderer = super.getListCellRendererComponent(list,
        value, isSelected, hasFocus, row);
    UlcUtil.alternateEvenOddBackground((ULCComponent) renderer, list,
        isSelected, row);
    return renderer;
  }
}
