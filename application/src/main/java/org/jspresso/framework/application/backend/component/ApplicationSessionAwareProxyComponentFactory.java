/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.component;

import java.lang.reflect.InvocationHandler;

import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.basic.BasicProxyComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
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
public class ApplicationSessionAwareProxyComponentFactory extends
    BasicProxyComponentFactory {

  private IApplicationSession applicationSession;

  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession
   *            the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    this.applicationSession = applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected InvocationHandler createComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor) {
    return new ApplicationSessionAwareComponentInvocationHandler(
        componentDescriptor, this, getComponentCollectionFactory(),
        getAccessorFactory(), getComponentExtensionFactory(),
        applicationSession);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected UserPrincipal getPrincipal() {
    return applicationSession.getPrincipal();
  }
}
