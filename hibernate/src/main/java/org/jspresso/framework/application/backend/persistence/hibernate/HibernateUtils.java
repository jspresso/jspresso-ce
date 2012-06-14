/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for Hibernate.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class HibernateUtils {

  private static final Logger LOG = LoggerFactory.getLogger(HibernateUtils.class);

  /**
   * Constructs a new <code>HibernateUtils</code> instance.
   */
  private HibernateUtils() {
    // Helper constructor
  }

  /**
   * Retrieves a component contract without initializing an entity.
   * 
   * @param component
   *          the componennt to rerieve the contract for.
   * @return the component contract.
   */
  @SuppressWarnings("unchecked")
  public static <E extends IComponent> Class<? extends E> getComponentContract(E component) {
    if (!Hibernate.isInitialized(component)) {
      if (component instanceof HibernateProxy) {
        try {
          return (Class<? extends E>) Class.forName(((HibernateProxy) component).getHibernateLazyInitializer()
              .getEntityName());
        } catch (ClassNotFoundException ex) {
          LOG.warn("Can not retrieve entity class {} without initializing entity.", ((HibernateProxy) component)
              .getHibernateLazyInitializer().getEntityName());
        }
      }
    }
    return (Class<? extends E>) component.getComponentContract();
  }

  /**
   * Test Object equality potentially unwrapping Hibernate proxies.
   * 
   * @param e1
   *          the 1st entity to test.
   * @param e2
   *          the 2nd entity to test.
   * @return true if they both refer to the same object reference.
   */
  public static boolean objectEquals(IEntity e1, IEntity e2) {
    IEntity actualE1 = e1;
    IEntity actualE2 = e2;

    if (actualE1 instanceof HibernateProxy) {
      actualE1 = (IEntity) ((HibernateProxy) actualE1).getHibernateLazyInitializer().getImplementation();
    }
    if (actualE2 instanceof HibernateProxy) {
      actualE2 = (IEntity) ((HibernateProxy) actualE2).getHibernateLazyInitializer().getImplementation();
    }
    return actualE1 == actualE2;
  }

}
