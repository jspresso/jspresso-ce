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

import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;

/**
 * Describes a boolean property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicBooleanPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IBooleanPropertyDescriptor {

  /**
   * Constructs a new {@code BasicBooleanPropertyDescriptor} instance.
   */
  public BasicBooleanPropertyDescriptor() {
    setDefaultValue(Boolean.FALSE);
    setMandatory(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicBooleanPropertyDescriptor clone() {
    BasicBooleanPropertyDescriptor clonedDescriptor = (BasicBooleanPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return Boolean.TYPE;
  }

  /**
   * Handles Boolean.TYPE cleanly.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object parseStringValue(String valueAsString) {
    return Boolean.valueOf(valueAsString);
  }
}
