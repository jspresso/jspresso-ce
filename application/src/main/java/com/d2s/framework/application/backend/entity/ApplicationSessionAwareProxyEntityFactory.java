/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.entity;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.basic.BasicProxyEntityFactory;

/**
 * Proxy entity factory aware of an application session to deal with uniqueness
 * of entity instances across the JVM.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
  public IEntity createEntityInstance(Class entityContract) {
    IEntity newEntity = super.createEntityInstance(entityContract);
    applicationSession.registerEntity(newEntity);
    return newEntity;
  }

  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession
   *          the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    this.applicationSession = applicationSession;
  }
}
