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
package org.jspresso.framework.model.entity.basic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.basic.AbstractComponentFactory;
import org.jspresso.framework.model.component.basic.BasicComponentInvocationHandler;
import org.jspresso.framework.model.component.basic.BasicDelegatingComponentInvocationHandler;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptorFactory;
import org.jspresso.framework.model.entity.EntityException;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.uid.IGUIDGenerator;

/**
 * Default implementation of {@code IEntityFactory}. It creates standard java
 * proxies which delegate to {@code BasicEntityInvocationHandler}s.
 *
 * @author Vincent Vandenschrick
 */
public class BasicProxyEntityFactory extends AbstractComponentFactory implements IEntityFactory {

  private IComponentCollectionFactory      componentCollectionFactory;
  private IComponentExtensionFactory       componentExtensionFactory;
  private IComponentDescriptorRegistry     componentDescriptorRegistry;
  private IQueryComponentDescriptorFactory queryComponentDescriptorFactory;
  private IGUIDGenerator<?>                entityGUIDGenerator;

  /**
   * {@inheritDoc}
   */
  @Override
  public final <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    return createEntityInstance(entityContract, entityGUIDGenerator.generateGUID(), true);
  }

  /**
   * Performs necessary post instantiation initialization.
   *
   * @param entity
   *          the instantiated entity.
   */
  protected void initializeEntity(IEntity entity) {
    initializeComponent(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final <T extends IEntity> T createEntityInstance(Class<T> entityContract, Serializable id) {
    return createEntityInstance(entityContract, id, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final <T extends IEntity> T createEntityInstance(Class<T> entityContract, Serializable id,
      boolean performInitialization) {
    final T createdEntity = createEntityInstance(entityContract, id, (Class<?>[]) null);
    createdEntity.addPropertyChangeListener(IEntity.VERSION, new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getOldValue() == null && evt.getNewValue() != null) {
          createdEntity.firePropertyChange(IEntity.PERSISTENT, false, true);
        } else if (evt.getOldValue() != null && evt.getNewValue() == null) {
          createdEntity.firePropertyChange(IEntity.PERSISTENT, true, false);
        }
      }
    });
    if (performInitialization) {
      initializeEntity(createdEntity);
    }
    return createdEntity;
  }

  /**
   * Sets the entityGUIDGenerator.
   *
   * @param entityGUIDGenerator
   *          the entityGUIDGenerator to set.
   */
  public void setEntityGUIDGenerator(IGUIDGenerator<?> entityGUIDGenerator) {
    this.entityGUIDGenerator = entityGUIDGenerator;
  }

  /**
   * Creates the entity proxy invocation handler.
   *
   * @param entityDescriptor
   *          the entity descriptor.
   * @return the entity proxy invocation handler.
   */
  protected InvocationHandler createEntityInvocationHandler(IComponentDescriptor<IEntity> entityDescriptor) {
    return new BasicEntityInvocationHandler(entityDescriptor, this, componentCollectionFactory, getAccessorFactory(),
        componentExtensionFactory);
  }

  /**
   * Gets the entity lifecycle handler.
   *
   * @return the entity lifecycle handler.
   */
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T extends IEntity> T createEntityInstance(Class<T> entityContract, Serializable id,
      Class<?>... extraInterfaces) {
    T entity;
    if (entityContract.isInterface()) {
      IComponentDescriptor<IEntity> entityDescriptor = (IComponentDescriptor<IEntity>) getComponentDescriptor(entityContract);
      if (entityDescriptor.isPurelyAbstract()) {
        throw new EntityException(entityDescriptor.getName() + " is purely abstract. It cannot be instantiated.");
      }
      InvocationHandler entityHandler = createEntityInvocationHandler(entityDescriptor);
      Class<?>[] implementedClasses;
      if (extraInterfaces != null) {
        implementedClasses = new Class<?>[extraInterfaces.length + 2];
        implementedClasses[0] = entityDescriptor.getComponentContract();
        implementedClasses[1] = ILifecycleCapable.class;
        System.arraycopy(extraInterfaces, 0, implementedClasses, 2, extraInterfaces.length);
      } else {
        implementedClasses = new Class<?>[2];
        implementedClasses[0] = entityDescriptor.getComponentContract();
        implementedClasses[1] = ILifecycleCapable.class;
      }
      entity = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), implementedClasses,
          entityHandler);
    } else {
      try {
        entity = entityContract.newInstance();
      } catch (InstantiationException | IllegalAccessException ex) {
        throw new EntityException(ex, "Could not instantiate entity " + entityContract.getName());
      }
    }
    entity.straightSetProperty(IEntity.ID, id);
    return entity;
  }

  /**
   * Gets the entityGUIDGenerator.
   *
   * @return the entityGUIDGenerator.
   */
  protected IGUIDGenerator<?> getEntityGUIDGenerator() {
    return entityGUIDGenerator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends IComponent> T createComponentInstance(Class<T> componentContract) {
    return createComponentInstance(componentContract, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends IComponent> T createComponentInstance(Class<T> componentContract, Object delegate) {
    T createdComponent = createComponentInstance(componentContract, delegate, (Class<?>[]) null);
    initializeComponent(createdComponent);
    return createdComponent;
  }

  /**
   * Performs necessary post instantiation initialization.
   *
   * @param component
   *          the instantiated component.
   */
  protected void initializeComponent(IComponent component) {
    IComponentDescriptor<?> componentDescriptor = getComponentDescriptor(component.getComponentContract());
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor.getPropertyDescriptors()) {
      if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
        component.straightSetProperty(propertyDescriptor.getName(),
            componentCollectionFactory.createComponentCollection(propertyDescriptor.getModelType()));
      } else if (propertyDescriptor instanceof IScalarPropertyDescriptor) {
        Object defaultValue = ((IScalarPropertyDescriptor) propertyDescriptor).getDefaultValue();
        if (defaultValue != null) {
          defaultValue = propertyDescriptor.interceptSetter(component, defaultValue);
          propertyDescriptor.preprocessSetter(component, defaultValue);
          component.straightSetProperty(propertyDescriptor.getName(),
              ((IScalarPropertyDescriptor) propertyDescriptor).getDefaultValue());
        }
      }
    }
    if (component instanceof ILifecycleCapable) {
      ((ILifecycleCapable) component).onCreate(this, getPrincipal(), getEntityLifecycleHandler());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public IQueryComponent createQueryComponentInstance(Class<? extends IComponent> componentContract) {
    return new QueryComponent(getQueryComponentDescriptorFactory().createQueryComponentDescriptor(
        (IComponentDescriptor<IComponent>) getComponentDescriptor(componentContract)), this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<?> getComponentDescriptor(Class<?> componentContract) {
    return componentDescriptorRegistry.getComponentDescriptor(componentContract);
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
  public void setComponentCollectionFactory(IComponentCollectionFactory componentCollectionFactory) {
    this.componentCollectionFactory = componentCollectionFactory;
  }

  /**
   * Sets the componentDescriptorRegistry.
   *
   * @param componentDescriptorRegistry
   *          the componentDescriptorRegistry to set.
   */
  public void setComponentDescriptorRegistry(IComponentDescriptorRegistry componentDescriptorRegistry) {
    this.componentDescriptorRegistry = componentDescriptorRegistry;
  }

  /**
   * Sets the componentExtensionFactory property.
   *
   * @param componentExtensionFactory
   *          the componentCollectionFactory to set.
   */
  public void setComponentExtensionFactory(IComponentExtensionFactory componentExtensionFactory) {
    this.componentExtensionFactory = componentExtensionFactory;
  }

  /**
   * Creates the component proxy invocation handler.
   *
   * @param componentDescriptor
   *          the component descriptor.
   * @return the component proxy invocation handler.
   */
  protected InvocationHandler createComponentInvocationHandler(IComponentDescriptor<IComponent> componentDescriptor) {
    return new BasicComponentInvocationHandler(componentDescriptor, this, componentCollectionFactory,
        getAccessorFactory(), componentExtensionFactory);
  }

  /**
   * Gets the componentCollectionFactory.
   *
   * @return the componentCollectionFactory.
   */
  protected IComponentCollectionFactory getComponentCollectionFactory() {
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
  private <T extends IComponent> T createComponentInstance(Class<T> componentContract, Object delegate,
      Class<?>... extraInterfaces) {
    if (IEntity.class.isAssignableFrom(componentContract)) {
      throw new IllegalArgumentException(componentContract.getName()
          + " is an entity contract. You should use createEntityInstance instead.");
    }
    IComponentDescriptor<IComponent> componentDescriptor = (IComponentDescriptor<IComponent>) componentDescriptorRegistry
        .getComponentDescriptor(componentContract);
    InvocationHandler componentHandler;
    if (delegate != null) {
      componentHandler = createDelegatingComponentInvocationHandler(componentDescriptor, delegate);
    } else {
      componentHandler = createComponentInvocationHandler(componentDescriptor);
    }
    Class<?>[] implementedClasses;
    if (extraInterfaces != null) {
      implementedClasses = new Class<?>[extraInterfaces.length + 2];
      implementedClasses[0] = componentDescriptor.getComponentContract();
      implementedClasses[1] = ILifecycleCapable.class;
      System.arraycopy(extraInterfaces, 0, implementedClasses, 2, extraInterfaces.length);
    } else {
      implementedClasses = new Class<?>[2];
      implementedClasses[0] = componentDescriptor.getComponentContract();
      implementedClasses[1] = ILifecycleCapable.class;
    }
    T component = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), implementedClasses,
        componentHandler);
    return component;
  }

  private InvocationHandler createDelegatingComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor, Object delegate) {
    return new BasicDelegatingComponentInvocationHandler(delegate, this, componentDescriptor,
        componentCollectionFactory, getAccessorFactory(), componentExtensionFactory);
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
  public void setQueryComponentDescriptorFactory(IQueryComponentDescriptorFactory queryComponentDescriptorFactory) {
    this.queryComponentDescriptorFactory = queryComponentDescriptorFactory;
  }
}
