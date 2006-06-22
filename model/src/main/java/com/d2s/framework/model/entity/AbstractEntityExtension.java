/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

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

  /**
   * Computes the entity type.
   * 
   * @return The entity type.
   */
  public String getType() {
    return getEntity().getContract().getName();
  }

  /**
   * Registers a property change listener to forward property changes.
   * 
   * @param sourceBean
   *          the source bean.
   * @param sourceProperty
   *          the name of the source property.
   * @param forwardedProperty
   *          the name of the forwarded property.
   */
  protected void registerNotificationForwarding(
      IPropertyChangeCapable sourceBean, String sourceProperty,
      final String forwardedProperty) {
    sourceBean.addPropertyChangeListener(sourceProperty,
        new PropertyChangeListener() {

          public void propertyChange(@SuppressWarnings("unused")
          PropertyChangeEvent evt) {
            getEntity().firePropertyChange(forwardedProperty,
                evt.getOldValue(), evt.getNewValue());
          }
        });
  }
}
