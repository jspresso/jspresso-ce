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
package org.jspresso.framework.model;

import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;

/**
 * This public interface should be implemented by any class being able to
 * provide a model instance. It allows for registration of
 * {@code IModelChangeListener} which can keep track of model object
 * changes.
 *
 * @author Vincent Vandenschrick
 */
public interface IModelProvider {

  /**
   * Adds a new bean listener to this model provider.
   *
   * @param listener
   *          The added listener
   */
  void addModelChangeListener(IModelChangeListener listener);

  /**
   * Gets the bean object of this provider.
   *
   * @param <T>
   *     type inference return.
   * @return The bean object
   */
  <T> T getModel();

  /**
   * Gets the bean object of this provider.
   *
   * @return The bean object
   */
  IComponentDescriptorProvider<?> getModelDescriptor();

  /**
   * Removes a bean listener from this model provider.
   *
   * @param listener
   *          The added listener
   */
  void removeModelChangeListener(IModelChangeListener listener);
}
