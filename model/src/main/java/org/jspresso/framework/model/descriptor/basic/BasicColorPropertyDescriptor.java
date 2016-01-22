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

import org.jspresso.framework.model.descriptor.IColorPropertyDescriptor;

/**
 * Describes a property used for storing a color. Color values are stored in the
 * property as their string hexadecimal representation (<i>0xargb</i> encoded).
 * Jspresso cleanly handles color properties in views for both visually
 * displaying and editing them without any extra effort. Moreover the
 * {@code ColorHelper} helper class eases colors manipulation and helps
 * converting to/from their hexadecimal representation.
 *
 * @author Vincent Vandenschrick
 */
public class BasicColorPropertyDescriptor extends BasicScalarPropertyDescriptor
    implements IColorPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicColorPropertyDescriptor clone() {
    BasicColorPropertyDescriptor clonedDescriptor = (BasicColorPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return String.class;
  }

  /**
   * Returns {@code false}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultSortablility() {
    return false;
  }
}
