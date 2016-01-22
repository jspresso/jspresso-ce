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
package org.jspresso.framework.util.gate;

import java.util.Collection;

/**
 * Accessible using gates.
 *
 * @author Vincent Vandenschrick
 */
public interface IGateAccessible {

  /**
   * Gets the collection of gates determining the readability state.
   *
   * @return the collection of gates determining the readability state.
   */
  Collection<IGate> getReadabilityGates();

  /**
   * Gets the collection of gates determining the writability state.
   *
   * @return the collection of gates determining the writability state.
   */
  Collection<IGate> getWritabilityGates();

  /**
   * Whether read-only is forced.
   *
   * @return true if read-only.
   */
  boolean isReadOnly();
}
