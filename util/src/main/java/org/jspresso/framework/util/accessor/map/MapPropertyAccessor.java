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

import java.util.Map;

import org.jspresso.framework.util.accessor.AbstractPropertyAccessor;

/**
 * This class is the default implementation of property accessors. It relies on
 * Jakarta commons beanutils.
 *
 * @author Vincent Vandenschrick
 */
public class MapPropertyAccessor extends AbstractPropertyAccessor {

  /**
   * Constructs a map property accessor.
   *
   * @param property
   *          the property accessed.
   */
  public MapPropertyAccessor(String property) {
    super(property);
  }

  /**
   * Gets the writable.
   *
   * @return the writable.
   */
  @Override
  public boolean isWritable() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean appliesTo(Object target) {
    return target == null || (target instanceof Map<?, ?>);
  }
}
