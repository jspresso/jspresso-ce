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
package org.jspresso.framework.application.backend.entity;

import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.component.ControllerAwareComponentInvocationHandler;
import org.jspresso.framework.application.backend.session.IApplicationSessionAware;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtension;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.basic.BasicComponentInvocationHandler;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.model.entity.IEntityLifecycleHandlerAware;
import org.jspresso.framework.model.entity.basic.BasicEntityInvocationHandler;
import org.jspresso.framework.security.ISubjectAware;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This entity invocation handler handles initialization of lazy loaded
 * properties like collections an entity references, delegating the
 * initialization job to the backend controller.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerAwareEntityInvocationHandler extends
    BasicEntityInvocationHandler {

  private static final String DETACHED_ENTITIES_PROPERTY_NAME = "detachedEntities";

  private static final long   serialVersionUID                = 3663517052427878204L;

  private IBackendController  backendController;

  private Set<IEntity>        detachedEntities;

  /**
   * Constructs a new <code>ControllerAwareEntityInvocationHandler</code>
   * instance.
   * 
   * @param entityDescriptor
   *          The descriptor of the proxy entity.
   * @param inlineComponentFactory
   *          the factory used to create inline components.
   * @param collectionFactory
   *          The factory used to create empty entity collections from
   *          collection getters.
   * @param accessorFactory
   *          The factory used to access proxy properties.
   * @param extensionFactory
   *          The factory used to create entity extensions based on their
   *          classes.
   * @param backendController
   *          the current backend controller.
   */
  protected ControllerAwareEntityInvocationHandler(
      IComponentDescriptor<IEntity> entityDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory,
      IBackendController backendController) {
    super(entityDescriptor, inlineComponentFactory, collectionFactory,
        accessorFactory, extensionFactory);
    this.backendController = backendController;
  }

  /**
   * Sets the JAAS subject to subject aware extensions. Sets the backend
   * controller to entity lifecycle handler aware extensions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void configureExtension(IComponentExtension<IComponent> extension) {
    super.configureExtension(extension);
    if (getBackendController() != null) {
      if (extension instanceof ISubjectAware) {
        ((ISubjectAware) extension).setSubject(getBackendController()
            .getApplicationSession().getSubject());
      }
      if (extension instanceof IApplicationSessionAware) {
        ((IApplicationSessionAware) extension)
            .setApplicationSession(getBackendController()
                .getApplicationSession());
      }
    }
    if (extension instanceof IEntityLifecycleHandlerAware) {
      ((IEntityLifecycleHandlerAware) extension)
          .setEntityLifecycleHandler(getBackendController());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void entityDetached(IEntity child) {
    if (detachedEntities == null) {
      detachedEntities = new LinkedHashSet<IEntity>();
    }
    detachedEntities.add(child);
  }

  /**
   * Gets the backendController.
   * 
   * @return the backendController.
   */
  protected IBackendController getBackendController() {
    return backendController;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor<? extends IComponent> propertyDescriptor) {
    getBackendController().initializePropertyIfNeeded((IEntity) proxy,
        propertyDescriptor.getName());
    return super.getCollectionProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getReferenceProperty(Object proxy,
      IReferencePropertyDescriptor<IComponent> propertyDescriptor) {

    getBackendController().initializePropertyIfNeeded((IEntity) proxy,
        propertyDescriptor.getName());
    return super.getReferenceProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isInitialized(Object objectOrProxy) {
    return getBackendController().isInitialized(objectOrProxy);
  }

  /**
   * Registers detached entities for update.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void onUpdate(IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    if (detachedEntities != null) {
      for (IEntity detachedEntity : detachedEntities) {
        if (detachedEntity.isPersistent()
            && !entityLifecycleHandler
                .isEntityRegisteredForDeletion(detachedEntity)
            && !entityLifecycleHandler
                .isEntityRegisteredForUpdate(detachedEntity)) {
          entityLifecycleHandler.registerForUpdate(detachedEntity);
        }
      }
    }
    detachedEntities = null;
    super.onUpdate(entityFactory, principal, entityLifecycleHandler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void storeReferenceProperty(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      Object oldPropertyValue, Object newPropertyValue) {
    if (newPropertyValue != null
        && isInlineComponentReference(propertyDescriptor)) {
      if (Proxy.isProxyClass(newPropertyValue.getClass())
          && Proxy.getInvocationHandler(newPropertyValue) instanceof BasicComponentInvocationHandler
          && !(Proxy.getInvocationHandler(newPropertyValue) instanceof ControllerAwareComponentInvocationHandler)) {
        IComponent sessionAwareComponent = getInlineComponentFactory()
            .createComponentInstance(
                ((IComponent) newPropertyValue).getComponentContract());
        sessionAwareComponent
            .straightSetProperties(((IComponent) newPropertyValue)
                .straightGetProperties());
        super.storeReferenceProperty(propertyDescriptor, oldPropertyValue,
            sessionAwareComponent);
      } else {
        super.storeReferenceProperty(propertyDescriptor, oldPropertyValue,
            newPropertyValue);
      }
    } else {
      super.storeReferenceProperty(propertyDescriptor, oldPropertyValue,
          newPropertyValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Map<String, Object> straightGetProperties(Object proxy) {
    Map<String, Object> superMap = super.straightGetProperties(proxy);
    superMap.put(DETACHED_ENTITIES_PROPERTY_NAME, detachedEntities);
    return superMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object straightGetProperty(Object proxy, String propertyName) {
    if (DETACHED_ENTITIES_PROPERTY_NAME.equals(propertyName)) {
      return detachedEntities;
    }
    return super.straightGetProperty(proxy, propertyName);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void straightSetProperty(Object proxy, String propertyName,
      Object newPropertyValue) {
    if (DETACHED_ENTITIES_PROPERTY_NAME.equals(propertyName)) {
      detachedEntities = (Set<IEntity>) newPropertyValue;
    } else {
      super.straightSetProperty(proxy, propertyName, newPropertyValue);
    }
  }
}
