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
package org.jspresso.framework.util.accessor.map;

import org.jspresso.framework.util.accessor.AbstractAccessorFactory;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * This is the map implementation of the accessor factory.
 *
 * @author Vincent Vandenschrick
 */
public class MapAccessorFactory extends AbstractAccessorFactory {

  /**
   * Creates a new {@code MapCollectionAccessor} on the collection
   * property.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class<?> beanClass, Class<?> elementClass) {
    return new MapCollectionAccessor(property);
  }

  /**
   * Creates a new {@code MapPropertyAccessor} on the property.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IAccessor createPropertyAccessor(String property, Class<?> beanClass) {
    return new MapPropertyAccessor(property);
  }
}
