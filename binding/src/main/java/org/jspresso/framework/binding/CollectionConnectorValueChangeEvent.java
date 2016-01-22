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
package org.jspresso.framework.binding;

import java.util.List;

import org.jspresso.framework.util.event.ValueChangeEvent;

/**
 * This class is a specific connector value change event for collection
 * connectors. It keeps track of the children connectors that were potentially
 * removed during the notification process.
 *
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorValueChangeEvent extends ValueChangeEvent {

  private static final long     serialVersionUID = 6547764843701088585L;
  private final List<IValueConnector> removedChildrenConnectors;

  /**
   * Constructs a new {@code CollectionConnectorValueChangeEvent} instance.
   *
   * @param source
   *          the connector that initiated the event.
   * @param oldValue
   *          the old value of the connector.
   * @param newValue
   *          the new value of the connector.
   * @param removedChildrenConnectors
   *          the children connectors that have just been removed.
   */
  public CollectionConnectorValueChangeEvent(IValueConnector source,
      Object oldValue, Object newValue,
      List<IValueConnector> removedChildrenConnectors) {
    super(source, oldValue, newValue);
    this.removedChildrenConnectors = removedChildrenConnectors;
  }

  /**
   * Gets the removedChildrenConnectors.
   *
   * @return the removedChildrenConnectors.
   */
  public List<IValueConnector> getRemovedChildrenConnectors() {
    return removedChildrenConnectors;
  }

  /**
   * The event needs firing if any connector removal need to be notified even if
   * oldValue and newValue are equal.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean needsFiring() {
    return super.needsFiring()
        || (removedChildrenConnectors != null && !removedChildrenConnectors
            .isEmpty());
  }
}
