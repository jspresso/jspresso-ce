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
