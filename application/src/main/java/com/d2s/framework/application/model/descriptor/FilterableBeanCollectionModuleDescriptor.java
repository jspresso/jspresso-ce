/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.application.model.FilterableBeanCollectionModule;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;

/**
 * The model descriptor for filterable bean collection modules.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FilterableBeanCollectionModuleDescriptor extends
    BeanCollectionModuleDescriptor {

  /**
   * Constructs a new <code>FilterableBeanCollectionModuleDescriptor</code> instance.
   * 
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module objects.
   */
  public FilterableBeanCollectionModuleDescriptor(
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor) {
    this(FilterableBeanCollectionModule.class.getName(),
        moduleObjectReferencedDescriptor);
  }

  /**
   * Constructs a new <code>FilterableBeanCollectionModuleDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor (the actual module class name).
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module objects.
   */
  protected FilterableBeanCollectionModuleDescriptor(String name,
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor) {
    super(name, moduleObjectReferencedDescriptor);

    BasicReferencePropertyDescriptor<Object> filterObjectDescriptor = new BasicReferencePropertyDescriptor<Object>();
    filterObjectDescriptor
        .setReferencedDescriptor(moduleObjectReferencedDescriptor);
    filterObjectDescriptor.setName("filter");

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>(
        getPropertyDescriptors());
    propertyDescriptors.add(filterObjectDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }

}
