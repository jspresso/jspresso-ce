/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity.basic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.component.IComponentCollectionFactory;
import com.d2s.framework.model.component.IComponentExtensionFactory;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorRegistry;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IScalarPropertyDescriptor;
import com.d2s.framework.model.entity.EntityException;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.security.UserPrincipal;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.uid.IGUIDGenerator;

/**
 * Default implementation of <code>IEntityFactory</code>. It creates standard
 * java proxies which delegate to <code>BasicEntityInvocationHandler</code>s.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicProxyEntityFactory implements IEntityFactory {

  private IAccessorFactory                        accessorFactory;
  private IComponentCollectionFactory<IComponent> entityCollectionFactory;
  private IComponentExtensionFactory              entityExtensionFactory;
  private IGUIDGenerator                          entityGUIDGenerator;
  private IComponentDescriptorRegistry            entityDescriptorRegistry;

  /**
   * {@inheritDoc}
   */
  public <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    T createdEntity = createEntityInstance(entityContract, entityGUIDGenerator
        .generateGUID());
    for (IPropertyDescriptor propertyDescriptor : entityDescriptorRegistry
        .getComponentDescriptor(entityContract).getPropertyDescriptors()) {
      if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
        createdEntity
            .straightSetProperty(
                propertyDescriptor.getName(),
                entityCollectionFactory
                    .createEntityCollection(((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                        .getModelType()));
      } else if (propertyDescriptor instanceof IScalarPropertyDescriptor
          && ((IScalarPropertyDescriptor) propertyDescriptor).getDefaultValue() != null) {
        createdEntity.straightSetProperty(propertyDescriptor.getName(),
            ((IScalarPropertyDescriptor) propertyDescriptor).getDefaultValue());
      }
    }
    createdEntity.onCreate(this, getPrincipal(), getEntityLifecycleHandler());
    return createdEntity;
  }

  /**
   * {@inheritDoc}
   */
  public <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id) {
    final T createdEntity = createEntityInstance(entityContract, id, null);
    createdEntity.addPropertyChangeListener(IEntity.VERSION,
        new PropertyChangeListener() {

          public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getOldValue() == null && evt.getNewValue() != null) {
              createdEntity.firePropertyChange("persistent",
                  new Boolean(false), new Boolean(true));
            } else if (evt.getOldValue() != null && evt.getNewValue() == null) {
              createdEntity.firePropertyChange("persistent", new Boolean(true),
                  new Boolean(false));
            }
          }
        });
    return createdEntity;
  }

  @SuppressWarnings("unchecked")
  private <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id, Class[] extraInterfaces) {
    IComponentDescriptor entityDescriptor = entityDescriptorRegistry
        .getComponentDescriptor(entityContract);
    if (entityDescriptor.isPurelyAbstract()) {
      throw new EntityException(entityDescriptor.getName()
          + " is purely abstract. It cannot be instanciated.");
    }
    InvocationHandler entityHandler = createEntityInvocationHandler(entityDescriptor);
    Class[] implementedClasses;
    if (extraInterfaces != null) {
      implementedClasses = new Class[extraInterfaces.length + 1];
      implementedClasses[0] = entityDescriptor.getComponentContract();
      for (int i = 0; i < extraInterfaces.length; i++) {
        implementedClasses[i + 1] = extraInterfaces[i];
      }
    } else {
      implementedClasses = new Class[1];
      implementedClasses[0] = entityDescriptor.getComponentContract();
    }
    T entity = (T) Proxy.newProxyInstance(Thread.currentThread()
        .getContextClassLoader(), implementedClasses, entityHandler);
    entity.straightSetProperty(IEntity.ID, id);
    return entity;
  }

  /**
   * Creates the entity proxy invocation handler.
   *
   * @param entityDescriptor
   *          the entity descriptor.
   * @return the entity proxy invocation handler.
   */
  protected InvocationHandler createEntityInvocationHandler(
      IComponentDescriptor<IComponent> entityDescriptor) {
    return new BasicEntityInvocationHandler(entityDescriptor,
        entityCollectionFactory, accessorFactory, entityExtensionFactory);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <T extends IQueryEntity> T createQueryEntityInstance(
      Class<T> entityContract) {
    IEntity entityDelegate = createEntityInstance(entityContract, null,
        new Class[] {IQueryEntity.class});
    QueryEntityInvocationHandler entityHandler = new QueryEntityInvocationHandler(
        entityDelegate);
    return (T) Proxy.newProxyInstance(Thread.currentThread()
        .getContextClassLoader(), entityDelegate.getClass().getInterfaces(),
        entityHandler);
  }

  /**
   * Sets the accessorFactory used by this entity factory.
   *
   * @param accessorFactory
   *          the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets the entityCollectionFactory property.
   *
   * @param entityCollectionFactory
   *          the entityCollectionFactory to set.
   */
  public void setEntityCollectionFactory(
      IComponentCollectionFactory<IComponent> entityCollectionFactory) {
    this.entityCollectionFactory = entityCollectionFactory;
  }

  /**
   * Sets the entityExtensionFactory property.
   *
   * @param entityExtensionFactory
   *          the entityCollectionFactory to set.
   */
  public void setEntityExtensionFactory(
      IComponentExtensionFactory entityExtensionFactory) {
    this.entityExtensionFactory = entityExtensionFactory;
  }

  /**
   * Sets the entityGUIDGenerator.
   *
   * @param entityGUIDGenerator
   *          the entityGUIDGenerator to set.
   */
  public void setEntityGUIDGenerator(IGUIDGenerator entityGUIDGenerator) {
    this.entityGUIDGenerator = entityGUIDGenerator;
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
   * Gets the entityCollectionFactory.
   *
   * @return the entityCollectionFactory.
   */
  protected IComponentCollectionFactory<IComponent> getEntityCollectionFactory() {
    return entityCollectionFactory;
  }

  /**
   * Gets the entityExtensionFactory.
   *
   * @return the entityExtensionFactory.
   */
  protected IComponentExtensionFactory getEntityExtensionFactory() {
    return entityExtensionFactory;
  }

  /**
   * Gets the principal using the factory.
   *
   * @return the principal using the factory.
   */
  protected UserPrincipal getPrincipal() {
    return null;
  }

  /**
   * Sets the entityDescriptorRegistry.
   *
   * @param entityDescriptorRegistry
   *          the entityDescriptorRegistry to set.
   */
  public void setEntityDescriptorRegistry(
      IComponentDescriptorRegistry entityDescriptorRegistry) {
    this.entityDescriptorRegistry = entityDescriptorRegistry;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<?> getComponentDescriptor(Class componentContract) {
    return entityDescriptorRegistry.getComponentDescriptor(componentContract);
  }

  /**
   * Gets the entity lifecycle handler.
   *
   * @return the entity lifecycle handler.
   */
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return null;
  }
}
