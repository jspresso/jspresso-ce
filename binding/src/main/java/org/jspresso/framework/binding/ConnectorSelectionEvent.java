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

import java.util.EventObject;

/**
 * An event notifying a connector selection change. It contains the connector at
 * the source of the event (generally a collection connector) and the newly
 * selected connector (or null if none).
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
public class ConnectorSelectionEvent extends EventObject {

  private static final long serialVersionUID = -5022556820638206657L;

  /**
   * New selected connector.
   */
  private IValueConnector   selectedConnector;

  /**
   * Constructs a new <code>ConnectorSelectionEvent</code>.
   * 
   * @param source
   *            the connector that initiated the event.
   * @param selectedConnector
   *            the new connector.
   */
  public ConnectorSelectionEvent(IValueConnector source,
      IValueConnector selectedConnector) {
    super(source);
    this.selectedConnector = selectedConnector;
  }

  /**
   * Gets the selectedConnector.
   * 
   * @return the selectedConnector.
   */
  public IValueConnector getSelectedConnector() {
    return selectedConnector;
  }

  /**
   * Narrows return type.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getSource() {
    return (IValueConnector) source;
  }
}
