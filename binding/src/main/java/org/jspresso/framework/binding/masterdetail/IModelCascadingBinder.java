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
package org.jspresso.framework.binding.masterdetail;

import org.jspresso.framework.binding.IValueConnector;

/**
 * This is the interface which has to be implemented by classes which bind two
 * connectors in a master / detail relationship where the master connector is
 * determined by the selection in a {@code ISelectableConnector}.
 *
 * @author Vincent Vandenschrick
 */
public interface IModelCascadingBinder {

  /**
   * Binds two connectors altogether.
   *
   * @param masterConnector
   *          The master collection connector.
   * @param detailConnector
   *          The detail connector tracking the master selectable selection.
   */
  void bind(IValueConnector masterConnector, IValueConnector detailConnector);
}
