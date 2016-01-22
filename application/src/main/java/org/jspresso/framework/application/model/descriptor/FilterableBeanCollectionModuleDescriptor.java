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
package org.jspresso.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.PageableDescriptor;

/**
 * The model descriptor for filterable bean collection modules.
 *
 * @author Vincent Vandenschrick
 */
public class FilterableBeanCollectionModuleDescriptor extends
    BeanCollectionModuleDescriptor {

  /**
   * {@code FILTER} is the "filter" constant.
   */
  public static final String FILTER = "filter";

  /**
   * Constructs a new {@code FilterableBeanCollectionModuleDescriptor}
   * instance.
   *
   * @param moduleObjectReferencedDescriptor
   *          the component descriptor of the module objects.
   * @param moduleFilterReferencedDescriptor
   *          the component descriptor for the filter object.
   */
  public FilterableBeanCollectionModuleDescriptor(
      IComponentDescriptor<?> moduleObjectReferencedDescriptor,
      IComponentDescriptor<IQueryComponent> moduleFilterReferencedDescriptor) {
    this(FilterableBeanCollectionModule.class.getName(),
        moduleObjectReferencedDescriptor, moduleFilterReferencedDescriptor);
  }

  /**
   * Constructs a new {@code FilterableBeanCollectionModuleDescriptor}
   * instance.
   *
   * @param name
   *          the name of the descriptor (the actual module class name).
   * @param moduleObjectReferencedDescriptor
   *          the component descriptor of the module objects.
   * @param moduleFilterReferencedDescriptor
   *          the component descriptor for the filter object.
   */
  protected FilterableBeanCollectionModuleDescriptor(String name,
      IComponentDescriptor<?> moduleObjectReferencedDescriptor,
      IComponentDescriptor<IQueryComponent> moduleFilterReferencedDescriptor) {
    super(name, moduleObjectReferencedDescriptor);

    BasicReferencePropertyDescriptor<IQueryComponent> filterDescriptor = new BasicReferencePropertyDescriptor<>();
    filterDescriptor.setReferencedDescriptor(moduleFilterReferencedDescriptor);
    filterDescriptor.setName(FILTER);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<>(getPropertyDescriptors());
    propertyDescriptors.add(filterDescriptor);

    setPropertyDescriptors(propertyDescriptors);

    List<IComponentDescriptor<?>> ancestorDescriptors = new ArrayList<>();
    ancestorDescriptors.add(PageableDescriptor.INSTANCE);
    setAncestorDescriptors(ancestorDescriptors);
  }

}
