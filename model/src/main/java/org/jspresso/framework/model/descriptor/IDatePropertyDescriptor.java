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
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of date properties.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDatePropertyDescriptor extends IScalarPropertyDescriptor,
    ITimeAwarePropertyDescriptor {

  /**
   * Gets the date type. Values are among : <li> <code>DATE</code> <li>
   * <code>DATE_TIME</code>
   * 
   * @return the type of this date descriptor.
   */
  EDateType getType();

  /**
   * Wether the date display should vary depending on the client timezone.
   * 
   * @return wether the date display should vary depending on the client
   *         timezone.
   */
  boolean isTimeZoneAware();

}
