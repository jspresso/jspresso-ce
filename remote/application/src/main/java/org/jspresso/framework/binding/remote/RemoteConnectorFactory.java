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

import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.uid.IGUIDGenerator;

/**
 * This connector factory implementation creates remote connector server peers.
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
public class RemoteConnectorFactory implements IConfigurableConnectorFactory {
  
  private IGUIDGenerator guidGenerator;

  /**
   * {@inheritDoc}
   */
  public ICollectionConnector createCollectionConnector(String id,
      IMvcBinder binder, ICompositeValueConnector childConnectorPrototype) {
    return new RemoteCollectionConnector(id, binder, childConnectorPrototype, guidGenerator);
  }

  /**
   * {@inheritDoc}
   */
  public IRenderableCompositeValueConnector createCompositeValueConnector(
      String id, String renderingConnectorId) {
    RemoteCompositeConnector connector = new RemoteCompositeConnector(id, guidGenerator);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IConfigurableCollectionConnectorListProvider createConfigurableCollectionConnectorListProvider(
      String id, String renderingConnectorId) {
    RemoteCollectionConnectorListProvider connector = new RemoteCollectionConnectorListProvider(
        id, guidGenerator);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IConfigurableCollectionConnectorProvider createConfigurableCollectionConnectorProvider(
      String id, String renderingConnectorId) {
    RemoteCollectionConnectorProvider connector = new RemoteCollectionConnectorProvider(
        id, guidGenerator);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector createValueConnector(String id) {
    return new RemoteValueConnector(id, guidGenerator);
  }

  private void createAndAddRenderingChildConnector(
      AbstractCompositeValueConnector compositeValueConnector,
      String renderingConnectorId) {
    if (renderingConnectorId != null) {
      compositeValueConnector
          .addChildConnector(createValueConnector(renderingConnectorId));
      compositeValueConnector
          .setRenderingChildConnectorId(renderingConnectorId);
    }
  }

  
  /**
   * Sets the guidGenerator.
   * 
   * @param guidGenerator the guidGenerator to set.
   */
  public void setGuidGenerator(IGUIDGenerator guidGenerator) {
    this.guidGenerator = guidGenerator;
  }
}
