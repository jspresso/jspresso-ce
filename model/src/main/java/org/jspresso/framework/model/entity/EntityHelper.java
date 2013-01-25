/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.entity;

import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;

/**
 * Helper class for entities utility methods.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class EntityHelper {

  /**
   * Constructs a new <code>EntityHelper</code> instance.
   */
  private EntityHelper() {
    // Helper cponstructor.
  }

  /**
   * Determines if a reference property descriptor references an inline
   * component.
   * 
   * @param propertyDescriptor
   *          the reference property descriptor to test.
   * @return <code>true</code> if the reference property descriptor references
   *         an inline component.
   */
  public static boolean isInlineComponentReference(
      IReferencePropertyDescriptor<?> propertyDescriptor) {
    return !propertyDescriptor.getReferencedDescriptor().isEntity()
        && !propertyDescriptor.getReferencedDescriptor().isPurelyAbstract();
  }
}
