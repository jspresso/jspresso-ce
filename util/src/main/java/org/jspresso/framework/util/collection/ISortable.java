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
package org.jspresso.framework.util.collection;

import java.util.Map;

/**
 * Defines the contract for sortable bean collections.
 *
 * @author Vincent Vandenschrick
 */
public interface ISortable {

  /**
   * Gets the ordered list of sorting attributes along with their individual
   * sorting direction.
   *
   * @return the ordered list of sorting attributes along with their individual
   *         sorting direction.
   */
  Map<String, ESort> getOrderingProperties();

  /**
   * Sets the ordered list of sorting attributes along with their individual
   * sorting direction.
   *
   * @param orderingProperties
   *          the ordered list of sorting attributes along with their individual
   *          sorting direction.
   */
  void setOrderingProperties(Map<String, ESort> orderingProperties);
}
