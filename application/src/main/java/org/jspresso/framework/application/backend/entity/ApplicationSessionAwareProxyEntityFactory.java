/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.entity;

import java.lang.reflect.InvocationHandler;

import org.jspresso.framework.application.backend.component.ApplicationSessionAwareProxyComponentFactory;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.model.entity.basic.BasicProxyEntityFactory;
import org.jspresso.framework.security.UserPrincipal;


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
