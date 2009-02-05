/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.model.entity.EntityException;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.uid.IGUIDGenerator;


/**
 * Default implementation of <code>IEntityFactory</code>. It creates standard
 * java proxies which delegate to <code>BasicEntityInvocationHandler</code>s.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
  private IComponentFactory                       inlineComponentFactory;

  /**
   * {@inheritDoc}
   */
  public <T extends IComponent> T createComponentInstance(
      Class<T> componentContract) {
    return inlineComponentFactory.createComponentInstance(componentContract);
  }

  /**
   * {@inheritDoc}
   */
  public <T extends IComponent> T createComponentInstance(
      Class<T> componentContract, Object delegate) {
    return inlineComponentFactory.createComponentInstance(componentContract,
        delegate);
  }

  /**
   * {@inheritDoc}
   */
  public <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    T createdEntity = createEntityInstance(entityContract, entityGUIDGenerator
        .generateGUID());
    for (IPropertyDescriptor propertyDescriptor : inlineComponentFactory
        .getComponentDescriptor(entityContract).getPropertyDescriptors()) {
      if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
        createdEntity
            .straightSetProperty(
                propertyDescriptor.getName(),
                entityCollectionFactory
                    .createComponentCollection(((ICollectionPropertyDescriptor<?>) propertyDescriptor)
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

  /**
   * {@inheritDoc}
   */
  public IQueryComponent createQueryComponentInstance(
      Class<? extends IComponent> componentContract) {
    return inlineComponentFactory
        .createQueryComponentInstance(componentContract);
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<?> getComponentDescriptor(
      Class<? extends IComponent> componentContract) {
    return inlineComponentFactory.getComponentDescriptor(componentContract);
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
   * Sets the entityCollectionFactory property.
   * 
   * @param entityCollectionFactory
   *            the entityCollectionFactory to set.
   */
  public void setEntityCollectionFactory(
      IComponentCollectionFactory<IComponent> entityCollectionFactory) {
    this.entityCollectionFactory = entityCollectionFactory;
  }

  /**
   * Sets the entityExtensionFactory property.
   * 
   * @param entityExtensionFactory
   *            the entityCollectionFactory to set.
   */
  public void setEntityExtensionFactory(
      IComponentExtensionFactory entityExtensionFactory) {
    entityExtensionFactory.setComponentFactory(this);
    this.entityExtensionFactory = entityExtensionFactory;
  }

  /**
   * Sets the entityGUIDGenerator.
   * 
   * @param entityGUIDGenerator
   *            the entityGUIDGenerator to set.
   */
  public void setEntityGUIDGenerator(IGUIDGenerator entityGUIDGenerator) {
    this.entityGUIDGenerator = entityGUIDGenerator;
  }

  /**
   * Sets the inlineComponentFactory.
   * 
   * @param inlineComponentFactory
   *            the inlineComponentFactory to set.
   */
  public void setInlineComponentFactory(IComponentFactory inlineComponentFactory) {
    this.inlineComponentFactory = inlineComponentFactory;
  }
  
  /**
   * Creates the entity proxy invocation handler.
   * 
   * @param entityDescriptor
   *            the entity descriptor.
   * @return the entity proxy invocation handler.
   */
  protected InvocationHandler createEntityInvocationHandler(
      IComponentDescriptor<IComponent> entityDescriptor) {
    return new BasicEntityInvocationHandler(entityDescriptor,
        inlineComponentFactory, entityCollectionFactory, accessorFactory,
        entityExtensionFactory);
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
   * Gets the entity lifecycle handler.
   * 
   * @return the entity lifecycle handler.
   */
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return null;
  }

  /**
   * Gets the inlineComponentFactory.
   * 
   * @return the inlineComponentFactory.
   */
  protected IComponentFactory getInlineComponentFactory() {
    return inlineComponentFactory;
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
  private <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id, Class[] extraInterfaces) {
    IComponentDescriptor entityDescriptor = inlineComponentFactory
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
   * {@inheritDoc}
   */
  public Collection<IComponentDescriptor<?>> getComponentDescriptors() {
    return inlineComponentFactory.getComponentDescriptors();
  }
}
