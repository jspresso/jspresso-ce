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

import org.jspresso.framework.model.descriptor.IPasswordPropertyDescriptor;

/**
 * Describes a property used for password values. For obvious security reasons,
 * this type of properties will hardly be part of a persistent entity. However
 * it is useful for defining transient view models, e.g. for implementing a
 * change password action. Jspresso will automatically adapt view fields
 * accordingly, using password fields, to interact with password properties.
 *
 * @author Vincent Vandenschrick
 */
public class BasicPasswordPropertyDescriptor extends
    BasicStringPropertyDescriptor implements IPasswordPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPasswordPropertyDescriptor clone() {
    BasicPasswordPropertyDescriptor clonedDescriptor = (BasicPasswordPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
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
