/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.tools.viewtester;

import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.model.component.IComponent;
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
  public void performPendingOperations() {
    // NO-OP
  }

  /**
   * Returns componentOrProxy.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IComponent unwrapProxy(IComponent componentOrProxy) {
    return componentOrProxy;
  }

  /**
   * NO-OP.
   * <p>
   * {@inheritDoc}
   */
  public void reload(@SuppressWarnings("unused") IEntity entity) {
    // NO-OP
  }
}
