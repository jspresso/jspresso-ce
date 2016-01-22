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
