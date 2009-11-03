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
package org.jspresso.framework.view.wings;

import org.jspresso.framework.util.wings.WingsUtil;
import org.wings.SComponent;
import org.wings.SDefaultListCellRenderer;


/**
 * A default list cell renderer rendering even and odd rows background slightly
 * differently.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddListCellRenderer extends SDefaultListCellRenderer {

  private static final long serialVersionUID = 2051850807889065438L;

  /**
   * {@inheritDoc}
   */
  @Override
  public SComponent getListCellRendererComponent(SComponent list, Object value,
      boolean isSelected, int index) {
    SComponent renderer = super.getListCellRendererComponent(list, value,
        isSelected, index);
    WingsUtil.alternateEvenOddBackground(renderer, list, isSelected, index);
    return renderer;
  }
}
