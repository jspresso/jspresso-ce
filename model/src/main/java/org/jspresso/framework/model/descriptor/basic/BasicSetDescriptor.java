/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.util.Set;

/**
 * This descriptor is equivalent to a <code>BasicCollectionDescriptor</code>
 * with its <code>collectionInterface</code> property set to
 * <code>java.util.Set</code>. Using this descriptor prevents mesing up with
 * technical implementation details.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete collection component element type.
 */
public class BasicSetDescriptor<E> extends BasicCollectionDescriptor<E> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getCollectionInterface() {
    return Set.class;
  }
}
