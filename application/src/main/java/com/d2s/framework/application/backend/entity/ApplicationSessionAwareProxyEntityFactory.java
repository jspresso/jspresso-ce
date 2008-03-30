/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.entity;

import java.lang.reflect.InvocationHandler;

import com.d2s.framework.application.backend.component.ApplicationSessionAwareProxyComponentFactory;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.model.entity.basic.BasicProxyEntityFactory;
import com.d2s.framework.security.UserPrincipal;

/**
 * Proxy entity factory aware of an application session to deal with uniqueness
 * of entity instances across the JVM.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ApplicationSessionAwareProxyEntityFactory extends
    BasicProxyEntityFactory {

  private IApplicationSession applicationSession;

  /**
   * Takes care of registering the newly created bean in the application
   * session.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    T newEntity = super.createEntityInstance(entityContract);
    applicationSession.registerEntity(newEntity, true);
    return newEntity;
  }

  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession
   *            the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    this.applicationSession = applicationSession;
    if (getInlineComponentFactory() instanceof ApplicationSessionAwareProxyComponentFactory) {
      ((ApplicationSessionAwareProxyComponentFactory) getInlineComponentFactory())
          .setApplicationSession(applicationSession);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected InvocationHandler createEntityInvocationHandler(
      IComponentDescriptor<IComponent> entityDescriptor) {
    return new ApplicationSessionAwareEntityInvocationHandler(entityDescriptor,
        getInlineComponentFactory(), getEntityCollectionFactory(),
        getAccessorFactory(), getEntityExtensionFactory(), applicationSession);
  }

  /**
   * Returns the application session.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected UserPrincipal getPrincipal() {
    return applicationSession.getPrincipal();
  }
}
