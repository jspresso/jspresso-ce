/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
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
public final class ModuleDescriptor extends BasicComponentDescriptor<SubModule> {

  /**
   * <code>MODULE_DESCRIPTOR</code> is a unique reference to the model
   * descriptor of modules.
   */
  public static final IComponentDescriptor<SubModule> MODULE_DESCRIPTOR = new ModuleDescriptor();

  /**
   * Constructs a new <code>ModuleDescriptor</code> instance.
   */
  private ModuleDescriptor() {

    super(Module.class.getName());

    BasicReferencePropertyDescriptor<SubModule> parentDescriptor = new BasicReferencePropertyDescriptor<SubModule>();
    parentDescriptor.setName("parent");
    parentDescriptor.setReferencedDescriptor(this);

    BasicCollectionDescriptor<SubModule> moduleListDescriptor = new BasicCollectionDescriptor<SubModule>();
    moduleListDescriptor
        .setCollectionInterface(List.class);
    moduleListDescriptor.setElementDescriptor(this);

    BasicCollectionPropertyDescriptor<SubModule> subModulesDescriptor = new BasicCollectionPropertyDescriptor<SubModule>();
    subModulesDescriptor.setReferencedDescriptor(moduleListDescriptor);
    subModulesDescriptor.setName("subModules");

    BasicObjectPropertyDescriptor projectedObjectDescriptor = new BasicObjectPropertyDescriptor();
    projectedObjectDescriptor.setName("projectedObject");

    BasicCollectionPropertyDescriptor<Object> projectedObjectsDescriptor = new BasicCollectionPropertyDescriptor<Object>();

    BasicStringPropertyDescriptor nameDescriptor = new BasicStringPropertyDescriptor();
    nameDescriptor.setName("name");

    BasicStringPropertyDescriptor descriptionDescriptor = new BasicStringPropertyDescriptor();
    descriptionDescriptor.setName("description");

    BasicStringPropertyDescriptor i18nNameDescriptor = new BasicStringPropertyDescriptor();
    i18nNameDescriptor.setName("i18nName");

    BasicStringPropertyDescriptor i18nDescriptionDescriptor = new BasicStringPropertyDescriptor();
    i18nDescriptionDescriptor.setName("i18nDescription");

    parentDescriptor.setReverseRelationEnd(subModulesDescriptor);
    subModulesDescriptor.setReverseRelationEnd(parentDescriptor);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(projectedObjectDescriptor);
    propertyDescriptors.add(projectedObjectsDescriptor);
    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);
    propertyDescriptors.add(i18nNameDescriptor);
    propertyDescriptors.add(i18nDescriptionDescriptor);
    propertyDescriptors.add(parentDescriptor);
    propertyDescriptors.add(subModulesDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }
}
