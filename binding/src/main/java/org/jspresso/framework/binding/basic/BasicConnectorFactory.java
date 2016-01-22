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

import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IFormattedValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.format.IFormatter;

/**
 * This connector factory implementation creates basic connectors.
 *
 * @author Vincent Vandenschrick
 */
public class BasicConnectorFactory implements IConfigurableConnectorFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public ICollectionConnector createCollectionConnector(String id,
      IMvcBinder binder, ICompositeValueConnector childConnectorPrototype) {
    return new BasicCollectionConnector(id, binder, childConnectorPrototype);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRenderableCompositeValueConnector createCompositeValueConnector(
      String id, String renderingConnectorId) {
    BasicCompositeConnector connector = new BasicCompositeConnector(id);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IConfigurableCollectionConnectorListProvider createConfigurableCollectionConnectorListProvider(
      String id, String renderingConnectorId) {
    BasicCollectionConnectorListProvider connector = new BasicCollectionConnectorListProvider(
        id);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IConfigurableCollectionConnectorProvider createConfigurableCollectionConnectorProvider(
      String id, String renderingConnectorId) {
    BasicCollectionConnectorProvider connector = new BasicCollectionConnectorProvider(
        id);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IFormattedValueConnector createFormattedValueConnector(String id,
      IFormatter<?, ?> formatter) {
    return new BasicFormattedValueConnector(id, formatter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createValueConnector(String id) {
    return new BasicValueConnector(id);
  }

  private void createAndAddRenderingChildConnector(
      AbstractCompositeValueConnector compositeValueConnector,
      String renderingConnectorId) {
    if (renderingConnectorId != null) {
      compositeValueConnector.addChildConnector(renderingConnectorId,
          createValueConnector(renderingConnectorId));
      compositeValueConnector
          .setRenderingChildConnectorId(renderingConnectorId);
    }
  }
}
