/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComponentIconImageURLProvider implements IIconImageURLProvider {

  private Collection<IComponentDescriptor<?>> componentDescriptors;
  private IComponentDescriptorRegistry        componentDescriptorRegistry;
  private Map<Class<?>, String>               cache;

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
}
