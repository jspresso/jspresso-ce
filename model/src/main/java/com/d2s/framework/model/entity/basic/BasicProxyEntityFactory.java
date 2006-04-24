/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity.basic;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.EntityException;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityCollectionFactory;
import com.d2s.framework.model.entity.IEntityExtensionFactory;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.util.bean.IAccessorFactory;
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
public class BasicProxyEntityFactory implements IEntityFactory,
    ApplicationContextAware {

  private IAccessorFactory         accessorFactory;
  private IEntityCollectionFactory entityCollectionFactory;
  private IEntityExtensionFactory  entityExtensionFactory;
  private IGUIDGenerator           entityGUIDGenerator;
  private ApplicationContext       entityApplicationContext;

  /**
   * {@inheritDoc}
   */
  public <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    T createdEntity = createEntityInstance(entityContract, entityGUIDGenerator
        .generateGUID());
    for (IPropertyDescriptor propertyDescriptor : getEntityDescriptor(
        entityContract).getPropertyDescriptors()) {
      if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
        createdEntity
            .straightSetProperty(
                propertyDescriptor.getName(),
                entityCollectionFactory
                    .createEntityCollection(((ICollectionPropertyDescriptor) propertyDescriptor)
                        .getPropertyClass()));
      }
    }
    createdEntity.onCreate(this);
    return createdEntity;
  }

  /**
   * {@inheritDoc}
   */
  public <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id) {
    return createEntityInstance(entityContract, id, null);
  }

  @SuppressWarnings("unchecked")
  private <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id, Class[] extraInterfaces) {
    IEntityDescriptor entityDescriptor = getEntityDescriptor(entityContract);
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
    T entity = (T) Proxy.newProxyInstance(IEntity.class.getClassLoader(),
        implementedClasses, entityHandler);
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
      IEntityDescriptor entityDescriptor) {
    return new BasicEntityInvocationHandler(entityDescriptor,
        entityCollectionFactory, accessorFactory, entityExtensionFactory);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <T extends IQueryEntity> T createQueryEntityInstance(Class<T> entityContract) {
    IEntity entityDelegate = createEntityInstance(entityContract, null,
        new Class[] {IQueryEntity.class});
    QueryEntityInvocationHandler entityHandler = new QueryEntityInvocationHandler(
        entityDelegate);
    return (T) Proxy.newProxyInstance(IQueryEntity.class
        .getClassLoader(), entityDelegate.getClass().getInterfaces(),
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
      IEntityCollectionFactory entityCollectionFactory) {
    this.entityCollectionFactory = entityCollectionFactory;
  }

  /**
   * Sets the entityExtensionFactory property.
   * 
   * @param entityExtensionFactory
   *          the entityCollectionFactory to set.
   */
  public void setEntityExtensionFactory(
      IEntityExtensionFactory entityExtensionFactory) {
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
   * Sets the application context holding the entity descriptor bean
   * definitions.
   * 
   * @param entityApplicationContext
   *          the application context holding the entity descriptor bean
   *          definitions.
   */
  public void setEntityApplicationContext(
      ApplicationContext entityApplicationContext) {
    this.entityApplicationContext = entityApplicationContext;
  }

  IEntityDescriptor getEntityDescriptor(Class entityContract) {
    return (IEntityDescriptor) entityApplicationContext.getBean(entityContract
        .getName());
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
  protected IEntityCollectionFactory getEntityCollectionFactory() {
    return entityCollectionFactory;
  }

  /**
   * Gets the entityExtensionFactory.
   * 
   * @return the entityExtensionFactory.
   */
  protected IEntityExtensionFactory getEntityExtensionFactory() {
    return entityExtensionFactory;
  }

  /**
   * {@inheritDoc}
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    setEntityApplicationContext(applicationContext);
  }
}
