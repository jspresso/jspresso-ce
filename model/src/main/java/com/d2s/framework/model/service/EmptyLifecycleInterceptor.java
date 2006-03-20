/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.service;

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
  public void onCreate(@SuppressWarnings("unused")
  E entity) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void onPersist(@SuppressWarnings("unused")
  E entity) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void onUpdate(@SuppressWarnings("unused")
  E entity) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void onDelete(@SuppressWarnings("unused")
  E entity) {
    // NO-OP
  }

}
