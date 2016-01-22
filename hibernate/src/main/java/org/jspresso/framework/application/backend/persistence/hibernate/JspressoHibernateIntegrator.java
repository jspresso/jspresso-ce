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
package org.jspresso.framework.application.backend.persistence.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * A new Hibernate 4 integrator used to register the default Jspresso post-load
 * listener.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoHibernateIntegrator implements Integrator {

  private final PostLoadEventListener postLoadListener = new LifecyclePostLoadEventListener();

  /**
   * Appends the {@link LifecyclePostLoadEventListener}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void integrate(Configuration configuration,
      SessionFactoryImplementor sessionFactory,
      SessionFactoryServiceRegistry serviceRegistry) {
    serviceRegistry.getService(EventListenerRegistry.class)
        .getEventListenerGroup(EventType.POST_LOAD)
        .appendListener(postLoadListener);
  }

  /**
   * Appends the {@link LifecyclePostLoadEventListener}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void integrate(MetadataImplementor metadata,
      SessionFactoryImplementor sessionFactory,
      SessionFactoryServiceRegistry serviceRegistry) {
    serviceRegistry.getService(EventListenerRegistry.class)
        .getEventListenerGroup(EventType.POST_LOAD)
        .appendListener(postLoadListener);
  }

  /**
   * Do nothing.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void disintegrate(SessionFactoryImplementor sessionFactory,
      SessionFactoryServiceRegistry serviceRegistry) {
    // Cannot un-register listener
    // serviceRegistry.getService(EventListenerRegistry.class)
    // .getEventListenerGroup(EventType.POST_LOAD)
    // .removeListener(postLoadListener);
  }

}
