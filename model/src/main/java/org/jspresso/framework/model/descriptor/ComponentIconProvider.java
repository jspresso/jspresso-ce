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
package org.jspresso.framework.model.descriptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.gui.IconProvider;

/**
 * This class uses a collection of component descriptors to be able to determine
 * the rendering image of a component based on its contract. It basically
 * iterates over the descriptor collection and returns the image url of the
 * first compatible descriptor.
 *
 * @author Vincent Vandenschrick
 */
public class ComponentIconProvider implements IconProvider {

  private final Map<Class<?>, Icon>           cache;
  private IComponentDescriptorRegistry        componentDescriptorRegistry;
  private Collection<IComponentDescriptor<?>> componentDescriptors;
  private Dimension                           defaultDimension;

  /**
   * Constructs a new {@code ComponentIconImageURLProvider} instance.
   */
  protected ComponentIconProvider() {
    cache = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIconForObject(Object userObject) {
    if (userObject == null) {
      return null;
    }
    Class<?> modelClass;
    if (userObject instanceof IComponent) {
      modelClass = ((IComponent) userObject).getComponentContract();
    } else {
      modelClass = userObject.getClass();
    }
    if (cache.containsKey(modelClass)) {
      return cache.get(modelClass);
    }
    if (componentDescriptors == null) {
      componentDescriptors = componentDescriptorRegistry
          .getComponentDescriptors();
    }
    Icon icon = computeIcon(modelClass);
    cache.put(modelClass, icon);
    return icon;
  }

  /**
   * Sets the componentDescriptorRegistry.
   *
   * @param componentDescriptorRegistry
   *          the componentDescriptorRegistry to set.
   */
  public void setComponentDescriptorRegistry(
      IComponentDescriptorRegistry componentDescriptorRegistry) {
    this.componentDescriptorRegistry = componentDescriptorRegistry;
  }

  /**
   * Sets the componentDescriptors.
   *
   * @param componentDescriptors
   *          the componentDescriptors to set.
   */
  public void setComponentDescriptors(
      Collection<IComponentDescriptor<?>> componentDescriptors) {
    this.componentDescriptors = componentDescriptors;
  }

  /**
   * Sets default dimension.
   *
   * @param defaultDimension the default dimension
   */
  public void setDefaultDimension(Dimension defaultDimension) {
    this.defaultDimension = defaultDimension;
  }

  private Icon computeIcon(Class<?> modelClass) {
    Icon icon = null;
    for (IComponentDescriptor<?> componentDescriptor : componentDescriptors) {
      if (modelClass.equals(componentDescriptor.getComponentContract())
          && componentDescriptor.getIcon() != null) {
        icon = componentDescriptor.getIcon();
      }
    }
    if (icon == null) {
      Class<?>[] superInterfaces = modelClass.getInterfaces();
      for (int i = superInterfaces.length - 1; i >= 0 && icon == null; i--) {
        icon = computeIcon(superInterfaces[i]);
      }
      if (icon == null && modelClass.getSuperclass() != null) {
        icon = computeIcon(modelClass.getSuperclass());
      }
    }
    if (icon != null && icon.getDimension() == null) {
      icon.setDimension(defaultDimension);
    }
    return icon;
  }
}
