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
package org.jspresso.framework.application.backend.entity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.component.ControllerAwareComponentInvocationHandler;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.model.entity.basic.BasicProxyEntityFactory;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.bean.MissingPropertyException;

/**
 * Proxy entity factory aware of a backend controller to deal with uniqueness of
 * entity instances across the JVM.
 *
 * @author Vincent Vandenschrick
 */
public class ControllerAwareProxyEntityFactory extends BasicProxyEntityFactory {

  private static final Logger LOG                     = LoggerFactory.getLogger(
      ControllerAwareProxyEntityFactory.class);
  private static final String SESSION_PROPERTY_PREFIX = "session.";

  /**
   * If a unit of work is active, it will register the new entity in the UOW
   * before performing any initialization.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void initializeEntity(IEntity entity) {
    getBackendController().registerEntity(entity);
    super.initializeEntity(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected InvocationHandler createEntityInvocationHandler(IComponentDescriptor<IEntity> entityDescriptor) {
    return new ControllerAwareEntityInvocationHandler(entityDescriptor, this, getComponentCollectionFactory(),
        getAccessorFactory(), getComponentExtensionFactory());
  }

  /**
   * Gets the backendController.
   *
   * @return the backendController.
   */
  protected IBackendController getBackendController() {
    return BackendControllerHolder.getCurrentBackendController();
  }

  /**
   * Returns the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return getBackendController();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected UserPrincipal getPrincipal() {
    return getBackendController().getApplicationSession().getPrincipal();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected InvocationHandler createComponentInvocationHandler(IComponentDescriptor<IComponent> componentDescriptor) {
    return new ControllerAwareComponentInvocationHandler(componentDescriptor, this, getComponentCollectionFactory(),
        getAccessorFactory(), getComponentExtensionFactory());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IQueryComponent createQueryComponentInstance(Class<? extends IComponent> componentContract) {
    IQueryComponent queryComponent = super.createQueryComponentInstance(componentContract);
    queryComponent.translate(getBackendController(), getBackendController().getLocale());
    return queryComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object extractInitValue(Object masterComponent, Object initializedAttributeValue)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Object initValue;
    if (initializedAttributeValue instanceof String
        && ((String) initializedAttributeValue)
        .startsWith(SESSION_PROPERTY_PREFIX)) {
      String sessionProperty = ((String) initializedAttributeValue)
          .substring(SESSION_PROPERTY_PREFIX.length());
      IApplicationSession applicationSession = getBackendController().getApplicationSession();
      if (applicationSession != null) {
        Class<?> sessionContract = applicationSession.getClass();
        try {
          IAccessor sessionAccessor = getAccessorFactory()
              .createPropertyAccessor(sessionProperty, sessionContract);
          initValue = sessionAccessor.getValue(applicationSession);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Session contract : " + sessionContract.getName());
            LOG.debug("Init value computed from session : " + initValue);
          }
        } catch (MissingPropertyException ex) {
          // the value in the initialization mapping is not a session
          // value. Handle it as null.
          initValue = null;
          if (LOG.isDebugEnabled()) {
            LOG.debug(
                "Init value '{}' not found on application session. Assigning null.",
                sessionProperty);
          }
        }
      } else {
        initValue = null;
        if (LOG.isDebugEnabled()) {
          LOG.debug("Application session is null. Assigning null.");
        }
      }
    } else {
      initValue = super.extractInitValue(masterComponent, initializedAttributeValue);
    }
    return initValue;
  }
}
