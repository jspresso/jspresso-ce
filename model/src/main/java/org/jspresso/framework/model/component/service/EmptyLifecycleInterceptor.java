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
package org.jspresso.framework.model.component.service;

import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Empty interceptor for component lifecycle. It is designed to be subclassed
 * and used as an component service.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the class of the intercepted entity.
 */
public abstract class EmptyLifecycleInterceptor<E> implements
    ILifecycleInterceptor<E>, IComponentService {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onCreate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onDelete(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onLoad(E component) {
    // Empty implementation.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onClone(E component, E sourceComponent) {
    // Empty implementation.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onPersist(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onUpdate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

}
