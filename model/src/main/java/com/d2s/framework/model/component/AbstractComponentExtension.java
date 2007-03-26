/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This abstract class is a helper base class for components extensions.
 * Developpers should inherit from it and use the <code>getComponent()</code>
 * to access the extended component instance.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <T>
 *          the parametrized component class on which these extensions work on.
 */
public abstract class AbstractComponentExtension<T extends IComponent>
    implements IComponentExtension<T>, IComponentFactoryAware {

  private final T           extendedComponent;
  private IComponentFactory componentFactory;

  /**
   * Constructs a new <code>AbstractComponentExtension</code> instance.
   *
   * @param component
   *          The extended component instance.
   */
  public AbstractComponentExtension(T component) {
    this.extendedComponent = component;
  }

  /**
   * {@inheritDoc}
   */
  public T getComponent() {
    return extendedComponent;
  }

  /**
   * Computes the entity type.
   *
   * @return The entity type.
   */
  public String getType() {
    return getComponent().getContract().getName();
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
            getComponent().firePropertyChange(forwardedProperty,
                evt.getOldValue(), evt.getNewValue());
          }
        });
  }

  /**
   * Gets the componentFactory.
   *
   * @return the componentFactory.
   */
  protected IComponentFactory getComponentFactory() {
    return componentFactory;
  }

  /**
   * Sets the componentFactory.
   *
   * @param componentFactory
   *          the componentFactory to set.
   */
  public void setComponentFactory(IComponentFactory componentFactory) {
    this.componentFactory = componentFactory;
  }
}
