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
package org.jspresso.framework.binding.remote;

import java.text.ParseException;

import org.jspresso.framework.binding.ConnectorBindingException;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.uid.IGUIDGenerator;

/**
 * The server peer of a remote value connector that formats its value back and
 * forth as strings.
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
public class RemoteFormattedValueConnector extends RemoteValueConnector {

  private IFormatter formatter;

  /**
   * Constructs a new <code>RemoteFormattedValueConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param guidGenerator
   *          the guid generator.
   * @param formatter
   *          the format used to parse and format connector value object.
   */
  public RemoteFormattedValueConnector(String id, IGUIDGenerator guidGenerator,
      IFormatter formatter) {
    super(id, guidGenerator);
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteFormattedValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteFormattedValueConnector clone(String newConnectorId) {
    RemoteFormattedValueConnector clonedConnector = (RemoteFormattedValueConnector) super
        .clone(newConnectorId);
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteValueState getState() {
    RemoteValueState state = super.getState();
    state.setValue(getConnectorValueAsString());
    return state;
  }

  /**
   * Sets the connector value as a string representation.
   * 
   * @param valueAsString
   *          the connector value as a string representation.
   */
  public void setConnectorValueAsString(String valueAsString) {
    try {
      setConnectorValue(getFormatter().parse(valueAsString));
    } catch (ParseException ex) {
      throw new ConnectorBindingException(ex);
    }
  }

  /**
   * Gets the connector value as a string representation.
   * 
   * @return the connector value as a string representation.
   */
  public String getConnectorValueAsString() {
    return getFormatter().format(getConnectorValue());
  }

  /**
   * Gets the formatter.
   * 
   * @return the formatter.
   */
  private IFormatter getFormatter() {
    return formatter;
  }
}
