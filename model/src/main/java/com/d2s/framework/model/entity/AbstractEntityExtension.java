/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

/**
 * This abstract class is a helper base class for entity extensions. Developpers
 * should inherit from it and use the <code>getEntity()</code> to access the
 * extended entity instance.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <T>
 *          the parametrized entity class on which these extensions work on.
 */
public abstract class AbstractEntityExtension<T extends IEntity> implements
    IEntityExtension<T> {

  private final T extendedEntity;

  /**
   * Constructs a new <code>AbstractEntityExtension</code> instance.
   * 
   * @param entity
   *          The extended entity instance.
   */
  public AbstractEntityExtension(T entity) {
    this.extendedEntity = entity;
  }

  /**
   * {@inheritDoc}
   */
  public T getEntity() {
    return extendedEntity;
  }

}
