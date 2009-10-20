/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation based on spring application context.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class BasicComponentDescriptorRegistry implements
    IComponentDescriptorRegistry, ApplicationContextAware {

  private ApplicationContext  componentApplicationContext;
  private Map<String, IComponentDescriptor<?>> contractNameToComponentDescriptorMap;

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<?> getComponentDescriptor(
      Class<?> componentContract) {
    if (contractNameToComponentDescriptorMap == null) {
      buildContractNameIdMap();
    }
    return contractNameToComponentDescriptorMap.get(componentContract.getName());
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
}
