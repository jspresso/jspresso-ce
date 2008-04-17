/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;


/**
 * Implementation of enumeration based on types of components registered.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TypeEnumerationPropertyDescriptor extends
    BasicEnumerationPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public TypeEnumerationPropertyDescriptor clone() {
    TypeEnumerationPropertyDescriptor clonedDescriptor = (TypeEnumerationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * Sets the componentDescriptorList property.
   * 
   * @param componentDescriptorList
   *            the componentDescriptorList to set.
   */
  public void setComponentDescriptors(
      List<IComponentDescriptor<?>> componentDescriptorList) {
    Map<String, String> componentDescriptorImages = new LinkedHashMap<String, String>();
    for (IComponentDescriptor<?> componentDescriptor : componentDescriptorList) {
      componentDescriptorImages.put(componentDescriptor.getName(),
          componentDescriptor.getIconImageURL());
    }
    setValuesAndIconImageUrls(componentDescriptorImages);
  }
}
