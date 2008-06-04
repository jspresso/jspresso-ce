/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding;

/**
 * This interface is implemented by any structure capable of selecting a
 * connector (a view collection connector is such a structure since it keeps
 * track of its children selection).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnectorSelector {

  /**
   * Adds a connector selection listener to this connector selector.
   * 
   * @param listener
   *            the listener to add.
   */
  void addConnectorSelectionListener(IConnectorSelectionListener listener);

  /**
   * Triggers notification of a connector selection event. This method has to be
   * made public to cope with notification of the children selection events.
   * 
   * @param evt
   *            the event to be propagated.
   */
  void fireSelectedConnectorChange(ConnectorSelectionEvent evt);

  /**
   * Removes a connector selection listener to this connector selector.
   * 
   * @param listener
   *            the listener to remove.
   */
  void removeConnectorSelectionListener(IConnectorSelectionListener listener);

  /**
   * Setes whether this connector selector should only forward its selection (a
   * table connector for instance) or also its children selections (a tree
   * connector for instance).
   * 
   * @param tracksChildrenSelection
   *            true if the connector selector should also forward its children
   *            selections.
   */
  void setTracksChildrenSelection(boolean tracksChildrenSelection);
}
