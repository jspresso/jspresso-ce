/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor.basic;

import org.jspresso.framework.model.descriptor.IPercentPropertyDescriptor;

/**
 * This is a specialization of decimal descriptor to handle percentage values.
 * The impact of using this descriptor is only on the UI level that will be
 * configured accordingly, i.e. displaying/editing properties as percentage
 * instead of their raw decimal values.
 *
 * @author Vincent Vandenschrick
 */
public class BasicPercentPropertyDescriptor extends
    BasicDecimalPropertyDescriptor implements IPercentPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPercentPropertyDescriptor clone() {
    BasicPercentPropertyDescriptor clonedDescriptor = (BasicPercentPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
