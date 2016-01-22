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

import java.io.Serializable;

import org.jspresso.framework.model.descriptor.IJavaSerializablePropertyDescriptor;

/**
 * Describes a property used to store any java {@code Serializable} object.
 * The property value is serialized/de-serialized to/from the data store. The
 * operation is completely transparent to the developer, i.e. the developer
 * never plays with the serialized form.
 *
 * @author Vincent Vandenschrick
 */
public class BasicJavaSerializablePropertyDescriptor extends
    BasicBinaryPropertyDescriptor implements
    IJavaSerializablePropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicJavaSerializablePropertyDescriptor clone() {
    BasicJavaSerializablePropertyDescriptor clonedDescriptor = (BasicJavaSerializablePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return Serializable.class;
  }
}
