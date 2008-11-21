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
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.view.descriptor.EAxis;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of an even grid view descriptor.
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
public class BasicEvenGridViewDescriptor extends BasicCompositeViewDescriptor
    implements IEvenGridViewDescriptor {

  private List<IViewDescriptor> childViewDescriptors;
  private EAxis                 drivingDimension = EAxis.ROW;
  private int                   drivingDimensionCellCount;

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    return childViewDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public EAxis getDrivingDimension() {
    return drivingDimension;
  }

  /**
   * {@inheritDoc}
   */
  public int getDrivingDimensionCellCount() {
    return drivingDimensionCellCount;
  }

  /**
   * Sets the viewDescriptors.
   * 
   * @param viewDescriptors
   *          the viewDescriptors to set.
   */
  public void setChildViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    this.childViewDescriptors = viewDescriptors;
  }

  /**
   * Sets the drivingDimension.
   * 
   * @param drivingDimension
   *          the drivingDimension to set.
   */
  public void setDrivingDimension(EAxis drivingDimension) {
    this.drivingDimension = drivingDimension;
  }

  /**
   * Sets the drivingDimensionCellCount.
   * 
   * @param drivingDimensionCellCount
   *          the drivingDimensionCellCount to set.
   */
  public void setDrivingDimensionCellCount(int drivingDimensionCellCount) {
    this.drivingDimensionCellCount = drivingDimensionCellCount;
  }

}
