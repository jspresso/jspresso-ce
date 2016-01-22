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
package org.jspresso.framework.model.component;

import java.util.Collection;

/**
 * This interface is implemented by factories of component collections.
 *
 * @author Vincent Vandenschrick
 */
public interface IComponentCollectionFactory {

  /**
   * Given a collection interface (i.e. {@code Set},{@code List}, ...)
   * this method creates a concrete implementation.
   *
   * @param <E>
   *     type inference return.
   * @param collectionInterface
   *          the interface which must be implemented by the created collection.
   * @return an empty instance of a concrete collection.
   */
  <E> Collection<E> createComponentCollection(Class<?> collectionInterface);
}
