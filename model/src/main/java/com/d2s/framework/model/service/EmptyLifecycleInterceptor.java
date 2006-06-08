/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.service;

import java.security.Principal;

import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.security.UserPrincipal;

/**
 * Empty interceptor for entity lifecycle. It is designed to be subclassed and
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
  public boolean onCreate(E entity, IEntityFactory entityFactory,
      UserPrincipal principal) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public boolean onPersist(E entity, IEntityFactory entityFactory,
      Principal principal) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public boolean onUpdate(E entity, IEntityFactory entityFactory,
      Principal principal) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public boolean onDelete(E entity, IEntityFactory entityFactory,
      Principal principal) {
    return false;
  }

}
