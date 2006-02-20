/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.basic;

import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConfigurableCollectionConnectorListProvider;
import com.d2s.framework.binding.IConfigurableCollectionConnectorProvider;
import com.d2s.framework.binding.IConfigurableConnectorFactory;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.binding.IRenderableCompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;

/**
 * This connector factory implementation creates basic connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicConnectorFactory implements IConfigurableConnectorFactory {

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
  public ICollectionConnector createCollectionConnector(String id,
      IMvcBinder binder, ICompositeValueConnector childConnectorPrototype) {
    return new BasicCollectionConnector(id, binder, childConnectorPrototype);
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
