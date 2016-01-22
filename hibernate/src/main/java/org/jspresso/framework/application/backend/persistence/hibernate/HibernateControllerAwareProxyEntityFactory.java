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

import java.util.Collection;

import org.jspresso.framework.application.backend.entity.ControllerAwareProxyEntityFactory;
import org.jspresso.framework.model.component.IComponent;

/**
 * An Hibernate aware entity factory that ensures that all sorted PersistentSets
 * are backed by LinkedHashSets.
 *
 * @author Vincent Vandenschrick
 */
public class HibernateControllerAwareProxyEntityFactory extends
    ControllerAwareProxyEntityFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void sortCollectionProperty(IComponent component, String propertyName) {
    Collection<Object> propertyValue = (Collection<Object>) component
        .straightGetProperty(propertyName);
    HibernateHelper.ensureInnerLinkedHashSet(propertyValue);
    super.sortCollectionProperty(component, propertyName);
  }
}
