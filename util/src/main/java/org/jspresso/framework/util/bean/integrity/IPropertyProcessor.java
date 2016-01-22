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
 * This is an interface used to identify classes responsible for providing
 * component property integrity processors.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          The type of the target.
 * @param <F>
 *          The type of the property.
 */
@SuppressWarnings({"EmptyMethod", "UnusedParameters"})
public interface IPropertyProcessor<E, F> {

  /**
   * This method gets called whenever a property has been set on an component to
   * which this processor is registered. This method may change the actual value
   * set to the bean.
   *
   * @param target
   *          the component the processor is ran on.
   * @param newPropertyValue
   *          the new value of the property accessed.
   * @return the actual value to set on the bean (it may be unchanged of
   *         course).
   */
  F interceptSetter(E target, F newPropertyValue);

  /**
   * This method gets called whenever a property has been set on an component to
   * which this processor is registered.
   *
   * @param target
   *          the component the processor is ran on.
   * @param oldPropertyValue
   *          the old value of the property accessed.
   * @param newPropertyValue
   *          the new value of the property accessed.
   */
  void postprocessSetter(E target, F oldPropertyValue, F newPropertyValue);

  /**
   * This method gets called whenever a property is about to be set on an
   * component to which this processor is registered. This method should throw
   * an {@code IntegrityException} if pre-checks are not valid.
   *
   * @param target
   *          the component the processor is ran on.
   * @param newPropertyValue
   *          the new value of the property accessed.
   */
  void preprocessSetter(E target, F newPropertyValue);
}
