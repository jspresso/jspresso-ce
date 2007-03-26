/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component.service;

import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.security.UserPrincipal;

/**
 * Empty interceptor for component lifecycle. It is designed to be subclassed and
 * used as an component service.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the class of the intercepted entity.
 */
public abstract class EmptyLifecycleInterceptor<E> implements
    ILifecycleInterceptor<E>, IComponentService {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public boolean onCreate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public boolean onPersist(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public boolean onUpdate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public boolean onDelete(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    return false;
  }

}
