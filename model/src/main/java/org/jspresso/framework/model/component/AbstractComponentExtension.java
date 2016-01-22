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
package org.jspresso.framework.model.component;

import org.jspresso.framework.model.component.service.DependsOnHelper;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.IAccessorFactoryProvider;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * This abstract class is a helper base class for components extensions.
 * Developers should inherit from it and use the {@code getComponent()} to
 * access the extended component instance.
 *
 * @param <T>
 *     the parametrized component class on which these extensions work on.
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentExtension<T extends IComponent>
    implements IComponentExtension<T>, IComponentFactoryAware, IAccessorFactoryProvider {

  private       IComponentFactory componentFactory;
  private final T                 extendedComponent;

  /**
   * Constructs a new {@code AbstractComponentExtension} instance.
   *
   * @param component
   *     The extended component instance.
   */
  public AbstractComponentExtension(T component) {
    this.extendedComponent = component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T getComponent() {
    return extendedComponent;
  }

  /**
   * Sets the componentFactory.
   *
   * @param componentFactory
   *     the componentFactory to set.
   */
  @Override
  public void setComponentFactory(IComponentFactory componentFactory) {
    this.componentFactory = componentFactory;
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
   * Default implementation is empty.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void postCreate() {
    DependsOnHelper.registerDependsOnListeners(getClass(), getComponent(), this);
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  protected void registerNotificationForwarding(String sourceProperty,
                                                String forwardedProperty) {
    DependsOnHelper.registerNotificationForwarding(getComponent(), this, sourceProperty, forwardedProperty);
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceBean
   *     the source bean.
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  protected void registerNotificationForwarding(IPropertyChangeCapable sourceBean, String sourceProperty,
                                                String forwardedProperty) {
    DependsOnHelper.registerNotificationForwarding(sourceBean, this, sourceProperty, forwardedProperty);
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  protected void registerNotificationForwarding(String sourceProperty,
                                                String... forwardedProperty) {
    DependsOnHelper.registerNotificationForwarding(getComponent(), this, sourceProperty, forwardedProperty);
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceBean
   *     the source bean.
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  protected void registerNotificationForwarding(IPropertyChangeCapable sourceBean, String sourceProperty,
                                                String... forwardedProperty) {
    DependsOnHelper.registerNotificationForwarding(sourceBean, this, sourceProperty, forwardedProperty);
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  protected void registerNotificationCollectionForwarding(String sourceCollectionProperty, String sourceElementProperty,
                                                          String forwardedProperty) {
    DependsOnHelper.registerNotificationForwarding(getComponent(), this, sourceCollectionProperty, sourceElementProperty,
        forwardedProperty);
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceBean
   *     the source bean.
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  protected void registerNotificationCollectionForwarding(IPropertyChangeCapable sourceBean,
                                                          String sourceCollectionProperty, String sourceElementProperty,
                                                          String forwardedProperty) {
    DependsOnHelper.registerNotificationCollectionForwarding(sourceBean, this, sourceCollectionProperty, sourceElementProperty,
        forwardedProperty);
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  @SuppressWarnings("unchecked")
  protected void registerNotificationCollectionForwarding(String sourceCollectionProperty,
                                                          String sourceElementProperty,
                                                          String... forwardedProperty) {
    DependsOnHelper.registerNotificationCollectionForwarding(getComponent(), this, sourceCollectionProperty,
        sourceElementProperty, forwardedProperty);
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceBean
   *     the source bean.
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  @SuppressWarnings("unchecked")
  protected void registerNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                          final String sourceCollectionProperty,
                                                          final String sourceElementProperty,
                                                          final String... forwardedProperty) {
    DependsOnHelper.registerNotificationCollectionForwarding(sourceBean, this, sourceCollectionProperty,
        sourceElementProperty, forwardedProperty);
  }

  /**
   * Gets accessor factory.
   *
   * @return the accessor factory
   */
  @Override
  public IAccessorFactory getAccessorFactory() {
    IAccessorFactory accessorFactory = null;
    if (getComponentFactory() != null) {
      accessorFactory = getComponentFactory().getAccessorFactory();
    }
    return accessorFactory;
  }
}
