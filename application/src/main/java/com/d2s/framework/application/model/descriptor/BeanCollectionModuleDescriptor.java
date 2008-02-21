/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;

/**
 * The model descriptor for bean collection modules.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionModuleDescriptor extends ModuleDescriptor {

  /**
   * Constructs a new <code>BeanModuleDescriptor</code> instance.
   * 
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module objects.
   */
  public BeanCollectionModuleDescriptor(
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor) {
    this(BeanCollectionModule.class.getName(), moduleObjectReferencedDescriptor);
  }

  /**
   * Constructs a new <code>BeanModuleDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor (the actual module class name).
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module objects.
   */
  protected BeanCollectionModuleDescriptor(String name,
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor) {
    super(name);

    BasicCollectionDescriptor<Object> moduleObjectsListDescriptor = new BasicCollectionDescriptor<Object>();
    moduleObjectsListDescriptor.setCollectionInterface(List.class);
    moduleObjectsListDescriptor
        .setElementDescriptor(moduleObjectReferencedDescriptor);

    BasicCollectionPropertyDescriptor<Object> moduleObjectsDescriptor = new BasicCollectionPropertyDescriptor<Object>();
    moduleObjectsDescriptor
        .setReferencedDescriptor(moduleObjectsListDescriptor);
    moduleObjectsDescriptor.setName("moduleObjects");

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>(
        getPropertyDescriptors());
    propertyDescriptors.add(moduleObjectsDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }

}
