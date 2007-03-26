/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.util.descriptor.IDescriptor;

/**
 * Basic implementation of an action descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class BasicDescriptorDescriptor {

  private BasicDescriptorDescriptor() {
    super();
    // Helper constructor
  }

  /**
   * The descriptor of the component descriptor.
   */
  public static final IComponentDescriptor<IDescriptor> INSTANCE = createInstance();

  private static IComponentDescriptor<IDescriptor> createInstance() {
    BasicComponentDescriptor<IDescriptor> instance = new BasicComponentDescriptor<IDescriptor>(
        IDescriptor.class.getName());

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();

    BasicPropertyDescriptor nameDescriptor = new BasicStringPropertyDescriptor();
    nameDescriptor.setName(IDescriptor.NAME);
    BasicPropertyDescriptor descriptionDescriptor = new BasicStringPropertyDescriptor();
    descriptionDescriptor.setName(IDescriptor.DESCRIPTION);

    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);

    instance.setPropertyDescriptors(propertyDescriptors);

    return instance;
  }
}
