/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.model;

import org.jspresso.framework.util.gui.IIconImageURLProvider;

/**
 * This image url provider uses a delegate provider to look up the rendering
 * image of a bean module based on its projected object.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WorkspaceIconImageURLProvider implements IIconImageURLProvider {

  private IIconImageURLProvider delegateProvider;

  /**
   * {@inheritDoc}
   */
  public String getIconImageURLForObject(Object userObject) {
    if (delegateProvider != null && userObject instanceof BeanModule
        && ((BeanModule) userObject).getModuleObject() != null
        && ((BeanModule) userObject).getIconImageURL() == null) {
      return delegateProvider
          .getIconImageURLForObject(((BeanModule) userObject).getModuleObject());
    } else if (userObject instanceof Module) {
      return ((Module) userObject).getIconImageURL();
    } else if (userObject instanceof Workspace) {
      return ((Workspace) userObject).getIconImageURL();
    }
    return null;
  }

  /**
   * Sets the delegateProvider.
   * 
   * @param delegateProvider
   *            the delegateProvider to set.
   */
  public void setDelegateProvider(IIconImageURLProvider delegateProvider) {
    this.delegateProvider = delegateProvider;
  }

}
