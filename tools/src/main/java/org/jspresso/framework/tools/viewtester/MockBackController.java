/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.tools.viewtester;

import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.model.entity.IEntity;

/**
 * A mock backend controller.
 * 
 * @version $LastChangedRevision$
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
  public void reload(@SuppressWarnings("unused") IEntity entity) {
    // NO-OP
  }
}
