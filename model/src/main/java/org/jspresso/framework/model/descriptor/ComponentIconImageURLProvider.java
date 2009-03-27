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
package org.jspresso.framework.model.descriptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.util.IIconImageURLProvider;


/**
 * This class uses a collection of component descriptors to be able to determine
 * the rendering image of a component based on its contract. It basically
 * iterates over the descriptor collection and returns the image url of the
 * first compatible descriptor.
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
public class ComponentIconImageURLProvider implements IIconImageURLProvider {

  private Map<Class<?>, String>               cache;
  private IComponentDescriptorRegistry        componentDescriptorRegistry;
  private Collection<IComponentDescriptor<?>> componentDescriptors;

  /**
   * Constructs a new <code>ComponentIconImageURLProvider</code> instance.
   */
  protected ComponentIconImageURLProvider() {
    cache = new HashMap<Class<?>, String>();
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURLForObject(Object userObject) {
    if (userObject == null) {
      return null;
    }
    Class<?> modelClass = userObject.getClass();
    if (cache.containsKey(modelClass)) {
      return cache.get(modelClass);
    }
    if (componentDescriptors == null) {
      componentDescriptors = componentDescriptorRegistry
          .getComponentDescriptors();
    }
    String iconImageURL = computeIconImageURL(modelClass);
    cache.put(modelClass, iconImageURL);
    return iconImageURL;
  }

  /**
   * Sets the componentDescriptorRegistry.
   * 
   * @param componentDescriptorRegistry
   *            the componentDescriptorRegistry to set.
   */
  public void setComponentDescriptorRegistry(
      IComponentDescriptorRegistry componentDescriptorRegistry) {
    this.componentDescriptorRegistry = componentDescriptorRegistry;
  }

  /**
   * Sets the componentDescriptors.
   * 
   * @param componentDescriptors
   *            the componentDescriptors to set.
   */
  public void setComponentDescriptors(
      Collection<IComponentDescriptor<?>> componentDescriptors) {
    this.componentDescriptors = componentDescriptors;
  }

  private String computeIconImageURL(Class<?> modelClass) {
    String iconImageURL = null;
    for (IComponentDescriptor<?> componentDescriptor : componentDescriptors) {
      if (modelClass.equals(componentDescriptor.getComponentContract())
          && componentDescriptor.getIconImageURL() != null) {
        iconImageURL = componentDescriptor.getIconImageURL();
      }
    }
    if (iconImageURL == null) {
      Class<?>[] superInterfaces = modelClass.getInterfaces();
      for (int i = superInterfaces.length - 1; i >= 0 && iconImageURL == null; i--) {
        iconImageURL = computeIconImageURL(superInterfaces[i]);
      }
      if (iconImageURL == null && modelClass.getSuperclass() != null) {
        iconImageURL = computeIconImageURL(modelClass.getSuperclass());
      }
    }
    return iconImageURL;
  }
}
