/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.tools.viewtester;

import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * A mock backend controller.
 * 
 * @author Vincent Vandenschrick
 */
public class MockBackController extends AbstractBackendController {

  // a mock implementation.

  /**
   * NO-OP.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void performPendingOperations() {
    // NO-OP
  }

  @Override
  public void initializePropertyIfNeeded(IComponent componentOrEntity, String propertyName) {
    // NO-OP
  }

  @Override
  public boolean isInitialized(Object objectOrProxy) {
    return false;
  }

  /**
   * Returns componentOrProxy.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object unwrapProxy(Object componentOrProxy) {
    return componentOrProxy;
  }

  /**
   * NO-OP.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void reload(IEntity entity) {
    // NO-OP
  }
}
