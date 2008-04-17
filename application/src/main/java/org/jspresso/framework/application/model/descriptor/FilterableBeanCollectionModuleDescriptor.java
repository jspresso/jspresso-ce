/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;


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
   * Constructs a new <code>FilterableBeanCollectionModuleDescriptor</code>
   * instance.
   * 
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module objects.
   * @param moduleFilterReferencedDescriptor
   *            the component descriptor for the filter object.
   */
  public FilterableBeanCollectionModuleDescriptor(
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor,
      IComponentDescriptor<Object> moduleFilterReferencedDescriptor) {
    this(FilterableBeanCollectionModule.class.getName(),
        moduleObjectReferencedDescriptor, moduleFilterReferencedDescriptor);
  }

  /**
   * Constructs a new <code>FilterableBeanCollectionModuleDescriptor</code>
   * instance.
   * 
   * @param name
   *            the name of the descriptor (the actual module class name).
   * @param moduleObjectReferencedDescriptor
   *            the component descriptor of the module objects.
   * @param moduleFilterReferencedDescriptor
   *            the component descriptor for the filter object.
   */
  protected FilterableBeanCollectionModuleDescriptor(String name,
      IComponentDescriptor<Object> moduleObjectReferencedDescriptor,
      IComponentDescriptor<Object> moduleFilterReferencedDescriptor) {
    super(name, moduleObjectReferencedDescriptor);

    BasicReferencePropertyDescriptor<IQueryComponent> filterDescriptor = new BasicReferencePropertyDescriptor<IQueryComponent>();
    filterDescriptor.setReferencedDescriptor(new BasicQueryComponentDescriptor(
        moduleFilterReferencedDescriptor));
    filterDescriptor.setName("filter");

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>(
        getPropertyDescriptors());
    propertyDescriptors.add(filterDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }

}
