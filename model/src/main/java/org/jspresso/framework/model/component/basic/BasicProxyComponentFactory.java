/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.component.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptorFactory;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Default implementation of <code>IComponentFactory</code>. It creates standard
 * java proxies which delegate to <code>BasicComponentInvocationHandler</code>s.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicProxyComponentFactory extends AbstractComponentFactory {

  private IComponentCollectionFactory<IComponent> componentCollectionFactory;
  private IComponentDescriptorRegistry            componentDescriptorRegistry;
  private IComponentExtensionFactory              componentExtensionFactory;
  private IQueryComponentDescriptorFactory        queryComponentDescriptorFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends IComponent> T createComponentInstance(
      Class<T> componentContract) {
    return createComponentInstance(componentContract, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends IComponent> T createComponentInstance(
      Class<T> componentContract, Object delegate) {
    T createdComponent = createComponentInstance(componentContract, delegate,
        null);
    for (IPropertyDescriptor propertyDescriptor : getComponentDescriptor(
        componentContract).getPropertyDescriptors()) {
      if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
        createdComponent
            .straightSetProperty(
                propertyDescriptor.getName(),
                componentCollectionFactory
                    .createComponentCollection(((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                        .getModelType()));
      } else if (propertyDescriptor instanceof IScalarPropertyDescriptor
          && ((IScalarPropertyDescriptor) propertyDescriptor).getDefaultValue() != null) {
        createdComponent.straightSetProperty(propertyDescriptor.getName(),
            ((IScalarPropertyDescriptor) propertyDescriptor).getDefaultValue());
      }
    }
    if (createdComponent instanceof ILifecycleCapable) {
      ((ILifecycleCapable) createdComponent).onCreate(null, getPrincipal(),
          null);
    }
    return createdComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public IQueryComponent createQueryComponentInstance(
      Class<? extends IComponent> componentContract) {
    return new QueryComponent(
        getQueryComponentDescriptorFactory()
            .createQueryComponentDescriptor(
                (IComponentDescriptor<IEntity>) getComponentDescriptor(componentContract)),
        this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<?> getComponentDescriptor(
      Class<?> componentContract) {
    return componentDescriptorRegistry
        .getComponentDescriptor(componentContract);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<IComponentDescriptor<?>> getComponentDescriptors() {
    return componentDescriptorRegistry.getComponentDescriptors();
  }

  /**
   * Sets the componentCollectionFactory property.
   * 
   * @param componentCollectionFactory
   *          the componentCollectionFactory to set.
   */
  public void setComponentCollectionFactory(
      IComponentCollectionFactory<IComponent> componentCollectionFactory) {
    this.componentCollectionFactory = componentCollectionFactory;
  }

  /**
   * Sets the componentDescriptorRegistry.
   * 
   * @param componentDescriptorRegistry
   *          the componentDescriptorRegistry to set.
   */
  public void setComponentDescriptorRegistry(
      IComponentDescriptorRegistry componentDescriptorRegistry) {
    this.componentDescriptorRegistry = componentDescriptorRegistry;
  }

  /**
   * Sets the componentExtensionFactory property.
   * 
   * @param componentExtensionFactory
   *          the componentCollectionFactory to set.
   */
  public void setComponentExtensionFactory(
      IComponentExtensionFactory componentExtensionFactory) {
    this.componentExtensionFactory = componentExtensionFactory;
  }

  /**
   * Creates the component proxy invocation handler.
   * 
   * @param componentDescriptor
   *          the component descriptor.
   * @return the component proxy invocation handler.
   */
  protected InvocationHandler createComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor) {
    return new BasicComponentInvocationHandler(componentDescriptor, this,
        componentCollectionFactory, getAccessorFactory(),
        componentExtensionFactory);
  }

  /**
   * Gets the componentCollectionFactory.
   * 
   * @return the componentCollectionFactory.
   */
  protected IComponentCollectionFactory<IComponent> getComponentCollectionFactory() {
    return componentCollectionFactory;
  }

  /**
   * Gets the componentExtensionFactory.
   * 
   * @return the componentExtensionFactory.
   */
  protected IComponentExtensionFactory getComponentExtensionFactory() {
    return componentExtensionFactory;
  }

  /**
   * Gets the principal using the factory.
   * 
   * @return the principal using the factory.
   */
  protected UserPrincipal getPrincipal() {
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T extends IComponent> T createComponentInstance(
      Class<T> componentContract, Object delegate, Class<?>[] extraInterfaces) {
    IComponentDescriptor<IComponent> componentDescriptor = (IComponentDescriptor<IComponent>) componentDescriptorRegistry
        .getComponentDescriptor(componentContract);
    InvocationHandler componentHandler;
    if (delegate != null) {
      componentHandler = createDelegatingComponentInvocationHandler(
          componentDescriptor, delegate);
    } else {
      componentHandler = createComponentInvocationHandler(componentDescriptor);
    }
    Class<?>[] implementedClasses;
    if (extraInterfaces != null) {
      implementedClasses = new Class[extraInterfaces.length + 2];
      implementedClasses[0] = componentDescriptor.getComponentContract();
      implementedClasses[1] = ILifecycleCapable.class;
      for (int i = 0; i < extraInterfaces.length; i++) {
        implementedClasses[i + 2] = extraInterfaces[i];
      }
    } else {
      implementedClasses = new Class[2];
      implementedClasses[0] = componentDescriptor.getComponentContract();
      implementedClasses[1] = ILifecycleCapable.class;
    }
    T component = (T) Proxy.newProxyInstance(Thread.currentThread()
        .getContextClassLoader(), implementedClasses, componentHandler);
    return component;
  }

  private InvocationHandler createDelegatingComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor, Object delegate) {
    return new BasicDelegatingComponentInvocationHandler(delegate, this,
        componentDescriptor, componentCollectionFactory, getAccessorFactory(),
        componentExtensionFactory);
  }

  /**
   * Gets the queryComponentDescriptorFactory.
   * 
   * @return the queryComponentDescriptorFactory.
   */
  protected IQueryComponentDescriptorFactory getQueryComponentDescriptorFactory() {
    if (queryComponentDescriptorFactory == null) {
      queryComponentDescriptorFactory = new BasicQueryComponentDescriptorFactory();
    }
    return queryComponentDescriptorFactory;
  }

  /**
   * Sets the queryComponentDescriptorFactory.
   * 
   * @param queryComponentDescriptorFactory
   *          the queryComponentDescriptorFactory to set.
   */
  public void setQueryComponentDescriptorFactory(
      IQueryComponentDescriptorFactory queryComponentDescriptorFactory) {
    this.queryComponentDescriptorFactory = queryComponentDescriptorFactory;
  }
}
