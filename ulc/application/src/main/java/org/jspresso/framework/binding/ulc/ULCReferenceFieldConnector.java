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
package org.jspresso.framework.binding.ulc;

import java.util.Collection;
import java.util.Collections;

import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.ulc.components.server.ULCActionField;


/**
 * ULCReferenceFieldConnector connector.
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
public class ULCReferenceFieldConnector extends ULCActionFieldConnector
    implements ICompositeValueConnector {

  private IConnectorValueChangeListener toStringListener;
  private IValueConnector               toStringPropertyConnector;

  /**
   * Constructs a new <code>ULCReferenceFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param actionField
   *            the connected ULCActionField.
   */
  public ULCReferenceFieldConnector(String id, ULCActionField actionField) {
    super(id, actionField);
    toStringListener = new ToStringConnectorListener();
  }

  /**
   * {@inheritDoc}
   */
  public void addChildConnector(@SuppressWarnings("unused")
  IValueConnector childConnector) {
    throw new UnsupportedOperationException(
        "Child connectors cannot be added to action field connector");
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenReadable() {
    return isReadable();
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenWritable() {
    return isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ULCReferenceFieldConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ULCReferenceFieldConnector clone(String newConnectorId) {
    ULCReferenceFieldConnector clonedConnector = (ULCReferenceFieldConnector) super
        .clone(newConnectorId);
    if (toStringPropertyConnector != null) {
      clonedConnector.toStringPropertyConnector = toStringPropertyConnector
          .clone();
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    if (connectorKey.equals(toStringPropertyConnector.getId())) {
      return toStringPropertyConnector;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    if (toStringPropertyConnector != null) {
      return 1;
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    if (toStringPropertyConnector != null) {
      return Collections.singleton(toStringPropertyConnector.getId());
    }
    return null;
  }

  /**
   * Sets the toStringPropertyConnector.
   * 
   * @param toStringPropertyConnector
   *            the toStringPropertyConnector to set.
   */
  public void setToStringPropertyConnector(
      IValueConnector toStringPropertyConnector) {
    if (this.toStringPropertyConnector != null) {
      this.toStringPropertyConnector
          .removeConnectorValueChangeListener(toStringListener);
    }
    this.toStringPropertyConnector = toStringPropertyConnector;
    if (this.toStringPropertyConnector != null) {
      this.toStringPropertyConnector
          .addConnectorValueChangeListener(toStringListener);
    }
  }

  private final class ToStringConnectorListener implements
      IConnectorValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(@SuppressWarnings("unused")
    ConnectorValueChangeEvent evt) {
      setConnecteeValue(getConnecteeValue());
    }

  }
}
