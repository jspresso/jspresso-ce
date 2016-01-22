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
package org.jspresso.framework.model.descriptor.basic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;

/**
 * Default implementation based on spring application context.
 *
 * @author Vincent Vandenschrick
 */
public class BasicComponentDescriptorRegistry implements IComponentDescriptorRegistry, ApplicationContextAware {

  private final Object mutex = new Object();
  private          ApplicationContext                   componentApplicationContext;
  private volatile Map<String, IComponentDescriptor<?>> contractNameToComponentDescriptorMap;

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<?> getComponentDescriptor(Class<?> componentContract) {
    if (contractNameToComponentDescriptorMap == null) {
      synchronized (mutex) {
        if (contractNameToComponentDescriptorMap == null) {
          buildContractNameIdMap();
        }
      }
    }
    return contractNameToComponentDescriptorMap.get(componentContract.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<IComponentDescriptor<?>> getComponentDescriptors() {
    if (contractNameToComponentDescriptorMap == null) {
      synchronized (mutex) {
        if (contractNameToComponentDescriptorMap == null) {
          buildContractNameIdMap();
        }
      }
    }
    return contractNameToComponentDescriptorMap.values();
  }

  /**
   * Sets the application context holding the component descriptor bean
   * definitions.
   *
   * @param applicationContext
   *     the application context holding the component descriptor bean
   *     definitions.
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.componentApplicationContext = applicationContext;
  }

  @SuppressWarnings("rawtypes")
  private void buildContractNameIdMap() {
    Map<String, IComponentDescriptor<?>> map = new HashMap<>();
    Map<String, IComponentDescriptor> idToComponentDescriptors = componentApplicationContext.getBeansOfType(
        IComponentDescriptor.class, false, false);
    for (Map.Entry<String, IComponentDescriptor> descriptorEntry : idToComponentDescriptors.entrySet()) {
      IComponentDescriptor componentDescriptor = descriptorEntry.getValue();
      if (componentDescriptor.getComponentContract() != null) {
        map.put(componentDescriptor.getComponentContract().getName(), componentDescriptor);
        if (componentDescriptor.isTranslatable()) {
          ICollectionPropertyDescriptor<?> collectionPropertyDescriptor = (ICollectionPropertyDescriptor<?>)
              componentDescriptor
              .getPropertyDescriptor(AbstractComponentDescriptor.getComponentTranslationsDescriptorTemplate().getName());
          if (collectionPropertyDescriptor != null) {
            IComponentDescriptor<?> translationComponentDescriptor = collectionPropertyDescriptor
                .getReferencedDescriptor().getElementDescriptor();
            if (translationComponentDescriptor.getComponentContract() != null) {
              map.put(translationComponentDescriptor.getComponentContract().getName(), translationComponentDescriptor);
            }
          }
        }
      }
    }
    map.put(BasicDescriptorDescriptor.INSTANCE.getName(), BasicDescriptorDescriptor.INSTANCE);
    contractNameToComponentDescriptorMap = map;
  }
}
