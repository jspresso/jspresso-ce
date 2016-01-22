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
package org.jspresso.framework.application.model;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.gui.IconProvider;

/**
 * This image url provider uses a delegate provider to look up the rendering
 * image of a bean module based on its projected object.
 *
 * @author Vincent Vandenschrick
 */
public class WorkspaceIconProvider implements IconProvider {

  private IconProvider delegateProvider;
  private Dimension    defaultDimension;


  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIconForObject(Object userObject) {
    Icon icon = null;
    if (delegateProvider != null && userObject instanceof BeanModule
        && ((BeanModule) userObject).getModuleObject() != null && ((BeanModule) userObject).getIcon() == null) {
      icon = delegateProvider.getIconForObject(((BeanModule) userObject).getModuleObject());
    }
    if (userObject instanceof Module) {
      icon = ((Module) userObject).getIcon();
    }
    if (userObject instanceof Workspace) {
      icon = ((Workspace) userObject).getIcon();
    }
    if (icon != null && icon.getDimension() == null) {
      icon.setDimension(defaultDimension);
    }
    return icon;
  }

  /**
   * Sets the delegateProvider.
   *
   * @param delegateProvider
   *     the delegateProvider to set.
   */
  public void setDelegateProvider(IconProvider delegateProvider) {
    this.delegateProvider = delegateProvider;
  }

  /**
   * Sets default dimension.
   *
   * @param defaultDimension
   *     the default dimension
   */
  public void setDefaultDimension(Dimension defaultDimension) {
    this.defaultDimension = defaultDimension;
  }
}
