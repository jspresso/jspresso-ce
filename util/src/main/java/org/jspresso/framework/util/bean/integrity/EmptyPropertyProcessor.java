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
package org.jspresso.framework.util.bean.integrity;

/**
 * Empty implementation of a property integrity processor.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          The type of the target.
 * @param <F>
 *          The type of the property.
 */
public abstract class EmptyPropertyProcessor<E, F> implements
    IPropertyProcessor<E, F> {

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public F interceptSetter(E target, F newPropertyValue) {
    return newPropertyValue;
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void postprocessSetter(E target, F oldPropertyValue, F newPropertyValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void preprocessSetter(E target, F newPropertyValue) {
    // NO-OP
  }

}
