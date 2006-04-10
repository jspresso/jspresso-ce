/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.model.descriptor.module;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.application.model.module.Module;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicComponentDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicObjectPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicStringPropertyDescriptor;

/**
 * The model descriptor of module objects.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModuleDescriptor extends BasicComponentDescriptor {

  /**
   * Constructs a new <code>ModuleDescriptor</code> instance.
   */
  public ModuleDescriptor() {

    super(Module.class.getName());

    BasicReferencePropertyDescriptor parentDescriptor = new BasicReferencePropertyDescriptor();
    parentDescriptor.setName("parent");
    parentDescriptor.setReferencedDescriptor(this);

    BasicCollectionDescriptor moduleListDescriptor = new BasicCollectionDescriptor();
    moduleListDescriptor.setCollectionInterface(List.class);
    moduleListDescriptor.setElementDescriptor(this);

    BasicCollectionPropertyDescriptor subModulesDescriptor = new BasicCollectionPropertyDescriptor();
    subModulesDescriptor.setReferencedDescriptor(moduleListDescriptor);
    subModulesDescriptor.setName("subModules");

    BasicObjectPropertyDescriptor projectedObjectDescriptor = new BasicObjectPropertyDescriptor();
    projectedObjectDescriptor.setName("projectedObject");

    BasicCollectionPropertyDescriptor projectedObjectsDescriptor = new BasicCollectionPropertyDescriptor();
    projectedObjectsDescriptor.setName("projectedObjects");

    BasicStringPropertyDescriptor nameDescriptor = new BasicStringPropertyDescriptor();
    nameDescriptor.setName("name");

    BasicStringPropertyDescriptor descriptionDescriptor = new BasicStringPropertyDescriptor();
    descriptionDescriptor.setName("description");

    parentDescriptor.setReverseRelationEnd(subModulesDescriptor);
    subModulesDescriptor.setReverseRelationEnd(parentDescriptor);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(projectedObjectDescriptor);
    propertyDescriptors.add(projectedObjectsDescriptor);
    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);
    propertyDescriptors.add(parentDescriptor);
    propertyDescriptors.add(subModulesDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }
}
