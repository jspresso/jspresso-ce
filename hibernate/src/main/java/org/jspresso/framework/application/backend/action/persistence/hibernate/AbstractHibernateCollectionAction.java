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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.Map;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;

/**
 * Base class for backend Hibernate actions acting on collections. This class is
 * just used to refine certain protected methods return types.
 *
 * @author Vincent Vandenschrick
 *
 * @deprecated directly use AbstractCollectionAction and perform appropriate casts.
 */
@Deprecated
public abstract class AbstractHibernateCollectionAction extends
    AbstractHibernateAction {

  /**
   * refined to return a collection connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICollectionConnector getModelConnector(Map<String, Object> context) {
    return (ICollectionConnector) super.getModelConnector(context);
  }

  /**
   * Refined to return a collection descriptor.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICollectionDescriptorProvider<?> getModelDescriptor(
      Map<String, Object> context) {
    return (ICollectionDescriptorProvider<?>) super.getModelDescriptor(context);
  }
}
