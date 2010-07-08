/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;

/**
 * The model descriptor for bean collection modules.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionModuleDescriptor extends ModuleDescriptor {

  /**
   * Constructs a new <code>BeanModuleDescriptor</code> instance.
   * 
   * @param moduleObjectReferencedDescriptor
   *          the component descriptor of the module objects.
   */
  public BeanCollectionModuleDescriptor(
      IComponentDescriptor<? extends Object> moduleObjectReferencedDescriptor) {
    this(BeanCollectionModule.class.getName(), moduleObjectReferencedDescriptor);
  }

  /**
   * Constructs a new <code>BeanModuleDescriptor</code> instance.
   * 
   * @param name
   *          the name of the descriptor (the actual module class name).
   * @param moduleObjectReferencedDescriptor
   *          the component descriptor of the module objects.
   */
  protected BeanCollectionModuleDescriptor(String name,
      IComponentDescriptor<? extends Object> moduleObjectReferencedDescriptor) {
    super(name);

    BasicListDescriptor<Object> moduleObjectsListDescriptor = new BasicListDescriptor<Object>();
    moduleObjectsListDescriptor
        .setElementDescriptor((IComponentDescriptor<Object>) moduleObjectReferencedDescriptor);

    BasicCollectionPropertyDescriptor<Object> moduleObjectsDescriptor = new BasicCollectionPropertyDescriptor<Object>();
    moduleObjectsDescriptor
        .setReferencedDescriptor(moduleObjectsListDescriptor);
    moduleObjectsDescriptor.setName(BeanCollectionModule.MODULE_OBJECTS);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>(
        getPropertyDescriptors());
    propertyDescriptors.add(moduleObjectsDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }

}
