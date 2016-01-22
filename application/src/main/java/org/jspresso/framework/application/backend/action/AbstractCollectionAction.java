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
package org.jspresso.framework.application.backend.action;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;

/**
 * Base class for backend actions acting on collection models. This class is
 * just used to refine certain protected methods return types.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCollectionAction extends BackendAction {

  private int[] viewPath;

  /**
   * refined to return a collection connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICollectionConnector getModelConnector(Map<String, Object> context) {
    IValueConnector connector = super.getModelConnector(getViewPath(), context);
    // for handling table editing connectors...
    while (connector != null && !(connector instanceof ICollectionConnector)) {
      connector = connector.getParentConnector();
    }
    return (ICollectionConnector) connector;
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

  /**
   * Gets the selected objects from the backend collection connector and the
   * selected indices.
   *
   * @param context
   *          the action context.
   * @return the list of selected objects.
   * @deprecated use getSelectedModels instead.
   */
  @Deprecated
  protected List<?> getSelectedObjects(Map<String, Object> context) {
    return getSelectedModels(getViewPath(), context);
  }

  /**
   * Get view path.
   *
   * @return the int [ ]
   */
  protected int[] getViewPath() {
    return viewPath;
  }

  /**
   * Sets view path.
   *
   * @param viewPath the view path
   */
  public void setViewPath(int... viewPath) {
    this.viewPath = viewPath;
  }
}
