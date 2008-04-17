/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;


/**
 * The model descriptor for bean modules.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanModuleDescriptor extends ModuleDescriptor {

  /**
   * Constructs a new <code>BeanModuleDescriptor</code> instance.
   * 
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module object.
   */
  public BeanModuleDescriptor(
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor) {
    this(BeanModule.class.getName(), moduleObjectReferencedDescriptor);
  }

  /**
   * Constructs a new <code>BeanModuleDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor (the actual module class name).
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module object.
   */
  protected BeanModuleDescriptor(String name,
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor) {
    super(name);

    BasicReferencePropertyDescriptor<Object> moduleObjectDescriptor = new BasicReferencePropertyDescriptor<Object>();
    moduleObjectDescriptor
        .setReferencedDescriptor(moduleObjectReferencedDescriptor);
    moduleObjectDescriptor.setName("moduleObject");

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>(
        getPropertyDescriptors());
    propertyDescriptors.add(moduleObjectDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }

}
