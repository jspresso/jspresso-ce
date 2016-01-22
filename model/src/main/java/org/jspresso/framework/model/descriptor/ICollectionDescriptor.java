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
package org.jspresso.framework.model.descriptor;

import java.util.Map;

import org.jspresso.framework.util.collection.ESort;

/**
 * This interface is implemented by descriptors of collections.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete component element type.
 */
public interface ICollectionDescriptor<E> extends
    ICollectionDescriptorProvider<E> {

  /**
   * Gets the {@code Collection} sub-interface implemented by the described
   * collection property (i.e {@code java.util.Set},
   * {@code java.util.List}, ...).
   *
   * @return the collection interface.
   */
  Class<?> getCollectionInterface();

  /**
   * Gets the component descriptor of the elements contained in this collection.
   *
   * @return The collection's component descriptor.
   */
  IComponentDescriptor<? extends E> getElementDescriptor();

  /**
   * Get the list of properties ordering this collection.
   *
   * @return the list of properties ordering this collection.
   */
  Map<String, ESort> getOrderingProperties();

  /**
   * Is null element allowed.
   *
   * @return the boolean
   */
  boolean isNullElementAllowed();
}
