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
package org.jspresso.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a constrained grid view descriptor.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class BasicConstrainedGridViewDescriptor extends
    BasicCompositeViewDescriptor implements IConstrainedGridViewDescriptor {

  private Map<IViewDescriptor, CellConstraints> cells;

  /**
   * {@inheritDoc}
   */
  public CellConstraints getCellConstraints(IViewDescriptor viewDescriptor) {
    if (cells != null) {
      return cells.get(viewDescriptor);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IViewDescriptor> childViewDescriptors = new ArrayList<IViewDescriptor>();
    if (cells != null) {
      for (Map.Entry<IViewDescriptor, CellConstraints> constrainedCell : cells
          .entrySet()) {
        completeChildDescriptor(constrainedCell.getKey());
        childViewDescriptors.add(constrainedCell.getKey());
      }
    }
    return childViewDescriptors;
  }

  /**
   * Sets the constrainedViews.
   * 
   * @param constrainedViews
   *          the constrainedViews to set.
   * @deprecated use setCells instead.
   */
  @Deprecated
  public void setConstrainedViews(
      Map<IViewDescriptor, CellConstraints> constrainedViews) {
    setCells(constrainedViews);
  }

  /**
   * Sets the constrainedCells.
   * 
   * @param constrainedCells
   *          the constrainedCells to set.
   * @deprecated use setCells instead.
   */
  @Deprecated
  public void setConstrainedCells(
      Map<IViewDescriptor, CellConstraints> constrainedCells) {
    setCells(constrainedCells);
  }

  /**
   * Sets the constrainedCells.
   * 
   * @param cells
   *          the cells to set.
   */
  public void setCells(Map<IViewDescriptor, CellConstraints> cells) {
    this.cells = cells;
  }
}
