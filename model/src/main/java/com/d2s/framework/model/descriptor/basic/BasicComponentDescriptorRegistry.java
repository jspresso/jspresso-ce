/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorRegistry;

/**
 * Default implementation based on spring application context.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentDescriptorRegistry implements
    IComponentDescriptorRegistry, ApplicationContextAware {

  private ApplicationContext  componentApplicationContext;
  private Map<String, String> contractNameIdMap;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IComponentDescriptor<?> getComponentDescriptor(
      Class<? extends IComponent> componentContract) {
    if (contractNameIdMap == null) {
      buildContractNameIdMap();
    }
    return (IComponentDescriptor<IComponent>) componentApplicationContext
        .getBean(contractNameIdMap.get(componentContract.getName()));
  }

  /**
   * Sets the application context holding the component descriptor bean
   * definitions.
   * 
   * @param applicationContext
   *            the application context holding the component descriptor bean
   *            definitions.
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.componentApplicationContext = applicationContext;
  }

  @SuppressWarnings("unchecked")
  private void buildContractNameIdMap() {
    contractNameIdMap = new HashMap<String, String>();
    Map<String, IComponentDescriptor<?>> idToComponentDescriptors = componentApplicationContext
        .getBeansOfType(IComponentDescriptor.class);
    for (Map.Entry<String, IComponentDescriptor<?>> descriptorEntry : idToComponentDescriptors
        .entrySet()) {
      if (descriptorEntry.getValue().getComponentContract() != null) {
        contractNameIdMap.put(descriptorEntry.getValue().getComponentContract()
            .getName(), descriptorEntry.getKey());
      }
    }
  }
}
