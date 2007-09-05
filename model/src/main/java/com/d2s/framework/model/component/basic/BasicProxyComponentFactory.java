/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.component.IComponentCollectionFactory;
import com.d2s.framework.model.component.IComponentExtensionFactory;
import com.d2s.framework.model.component.IComponentFactory;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorRegistry;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.security.UserPrincipal;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * Default implementation of <code>IComponentFactory</code>. It creates
 * standard java proxies which delegate to
 * <code>BasicComponentInvocationHandler</code>s.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicProxyComponentFactory implements IComponentFactory {

  private IAccessorFactory                        accessorFactory;
  private IComponentCollectionFactory<IComponent> componentCollectionFactory;
  private IComponentDescriptorRegistry            componentDescriptorRegistry;
  private IComponentExtensionFactory              componentExtensionFactory;

  /**
   * {@inheritDoc}
   */
  public <T extends IComponent> T createComponentInstance(
      Class<T> entityContract, Object delegate) {
    final T createdComponent = createEntityInstance(entityContract, delegate,
        null);
    createdComponent
        .onCreate(null, getPrincipal(), getEntityLifecycleHandler());
    return createdComponent;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<?> getComponentDescriptor(
      Class<? extends IComponent> componentContract) {
    return componentDescriptorRegistry
        .getComponentDescriptor(componentContract);
  }

  /**
   * Sets the accessorFactory used by this entity factory.
   * 
   * @param accessorFactory
   *            the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets the componentCollectionFactory property.
   * 
   * @param componentCollectionFactory
   *            the componentCollectionFactory to set.
   */
  public void setComponentCollectionFactory(
      IComponentCollectionFactory<IComponent> componentCollectionFactory) {
    this.componentCollectionFactory = componentCollectionFactory;
  }

  /**
   * Sets the componentDescriptorRegistry.
   * 
   * @param componentDescriptorRegistry
   *            the componentDescriptorRegistry to set.
   */
  public void setComponentDescriptorRegistry(
      IComponentDescriptorRegistry componentDescriptorRegistry) {
    this.componentDescriptorRegistry = componentDescriptorRegistry;
  }

  /**
   * Sets the componentExtensionFactory property.
   * 
   * @param componentExtensionFactory
   *            the componentCollectionFactory to set.
   */
  public void setComponentExtensionFactory(
      IComponentExtensionFactory componentExtensionFactory) {
    this.componentExtensionFactory = componentExtensionFactory;
  }

  /**
   * Gets the accessorFactory.
   * 
   * @return the accessorFactory.
   */
  protected IAccessorFactory getAccessorFactory() {
    return accessorFactory;
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
   * Gets the entity lifecycle handler.
   * 
   * @return the entity lifecycle handler.
   */
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return null;
  }

  /**
   * Gets the principal using the factory.
   * 
   * @return the principal using the factory.
   */
  protected UserPrincipal getPrincipal() {
    return null;
  }

  private InvocationHandler createComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor, Object delegate) {
    return new BasicComponentInvocationHandler(delegate, this,
        componentDescriptor, componentCollectionFactory, accessorFactory,
        componentExtensionFactory);
  }

  @SuppressWarnings("unchecked")
  private <T extends IComponent> T createEntityInstance(
      Class<T> componentContract, Object delegate, Class[] extraInterfaces) {
    IComponentDescriptor componentDescriptor = componentDescriptorRegistry
        .getComponentDescriptor(componentContract);
    InvocationHandler componentHandler = createComponentInvocationHandler(
        componentDescriptor, delegate);
    Class[] implementedClasses;
    if (extraInterfaces != null) {
      implementedClasses = new Class[extraInterfaces.length + 1];
      implementedClasses[0] = componentDescriptor.getComponentContract();
      for (int i = 0; i < extraInterfaces.length; i++) {
        implementedClasses[i + 1] = extraInterfaces[i];
      }
    } else {
      implementedClasses = new Class[1];
      implementedClasses[0] = componentDescriptor.getComponentContract();
    }
    T component = (T) Proxy.newProxyInstance(Thread.currentThread()
        .getContextClassLoader(), implementedClasses, componentHandler);
    return component;
  }
}
