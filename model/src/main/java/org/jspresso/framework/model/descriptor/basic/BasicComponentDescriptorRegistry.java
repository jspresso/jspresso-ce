/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation based on spring application context.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentDescriptorRegistry implements
    IComponentDescriptorRegistry, ApplicationContextAware {

  private ApplicationContext  componentApplicationContext;
  private Map<String, IComponentDescriptor<?>> contractNameToComponentDescriptorMap;

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<?> getComponentDescriptor(
      Class<? extends IComponent> componentContract) {
    if (contractNameToComponentDescriptorMap == null) {
      buildContractNameIdMap();
    }
    return contractNameToComponentDescriptorMap.get(componentContract.getName());
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
    contractNameToComponentDescriptorMap = new HashMap<String, IComponentDescriptor<?>>();
    Map<String, IComponentDescriptor<?>> idToComponentDescriptors = componentApplicationContext
        .getBeansOfType(IComponentDescriptor.class);
    for (Map.Entry<String, IComponentDescriptor<?>> descriptorEntry : idToComponentDescriptors
        .entrySet()) {
      if (descriptorEntry.getValue().getComponentContract() != null) {
        contractNameToComponentDescriptorMap.put(descriptorEntry.getValue().getComponentContract()
            .getName(), descriptorEntry.getValue());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public Collection<IComponentDescriptor<?>> getComponentDescriptors() {
    if (contractNameToComponentDescriptorMap == null) {
      buildContractNameIdMap();
    }
    return contractNameToComponentDescriptorMap.values();
  }
}
