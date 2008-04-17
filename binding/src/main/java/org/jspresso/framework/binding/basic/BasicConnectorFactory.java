/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.basic;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;

/**
 * This connector factory implementation creates basic connectors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicConnectorFactory implements IConfigurableConnectorFactory {

  /**
   * {@inheritDoc}
   */
  public ICollectionConnector createCollectionConnector(String id,
      IMvcBinder binder, ICompositeValueConnector childConnectorPrototype) {
    return new BasicCollectionConnector(id, binder, childConnectorPrototype);
  }

  /**
   * {@inheritDoc}
   */
  public IRenderableCompositeValueConnector createCompositeValueConnector(
      String id, String renderingConnectorId) {
    BasicCompositeConnector connector = new BasicCompositeConnector(id);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
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
  public IValueConnector createValueConnector(String id) {
    return new BasicValueConnector(id);
  }

  private void createAndAddRenderingChildConnector(
      BasicCompositeConnector compositeValueConnector,
      String renderingConnectorId) {
    if (renderingConnectorId != null) {
      compositeValueConnector
          .addChildConnector(createValueConnector(renderingConnectorId));
      compositeValueConnector
          .setRenderingChildConnectorId(renderingConnectorId);
    }
  }
}
