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
package org.jspresso.framework.binding.basic;

import java.util.Collection;

import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;

/**
 * This is a simple connector which itself holds the connector's value and which
 * allows child connectors. This connector is useful for building complex
 * technical view models (e.g. TableModel where each row model would be an
 * instance of {@code BasicCompositeConnector}).
 *
 * @author Vincent Vandenschrick
 */
public class BasicCompositeConnector extends AbstractCompositeValueConnector {

  private Object connecteeValue;

  /**
   * Constructs a new instance of BasicCompositeConnector.
   *
   * @param id
   *          the connector identifier
   */
  public BasicCompositeConnector(String id) {
    super(id);
  }

  /**
   * Overrides to allow for duplicate Ids. When 2 connectors with the same id
   * are added, instead of replacing the old one, the new one is added with '#x'
   * appended to its id.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void addChildConnector(String storageKey, IValueConnector connector) {
    Collection<String> childrenKeys = getChildConnectorKeys();
    int n = 1;
    String dedupKey = storageKey;
    while (childrenKeys.contains(dedupKey)) {
      dedupKey = getRankedKey(storageKey, n);
      n++;
    }
    super.addChildConnector(dedupKey, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean areChildrenWritable() {
    if (getParentConnector() != null
        && !getParentConnector().areChildrenWritable()) {
      return false;
    }
    // overridden to remove model connector writability condition.
    // if (getModelConnector() != null && !getModelConnector().isWritable()) {
    // return false;
    // }
    return isLocallyWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCompositeConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCompositeConnector clone(String newConnectorId) {
    BasicCompositeConnector clonedConnector = (BasicCompositeConnector) super
        .clone(newConnectorId);
    clonedConnector.connecteeValue = null;
    return clonedConnector;
  }

  /**
   * Gets the self-hosted value.
   *
   * @return the self-hosted value.
   */
  @Override
  protected Object getConnecteeValue() {
    return connecteeValue;
  }

  /**
   * Sets the self-hosted value.
   *
   * @param connecteeValue
   *          the value to host
   */
  @Override
  protected void setConnecteeValue(Object connecteeValue) {
    this.connecteeValue = connecteeValue;
  }

  private String getRankedKey(String base, int rank) {
    return base + "#" + rank;
  }
}
