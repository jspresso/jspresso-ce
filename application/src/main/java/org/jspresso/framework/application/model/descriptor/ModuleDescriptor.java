/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;


/**
 * The model descriptor of module objects.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModuleDescriptor extends BasicComponentDescriptor<Module> {

  /**
   * <code>MODULE_DESCRIPTOR</code> is a unique reference to the model
   * descriptor of modules.
   */
  public static final IComponentDescriptor<Module> MODULE_DESCRIPTOR = new ModuleDescriptor(
                                                                         Module.class
                                                                             .getName());

  /**
   * Constructs a new <code>ModuleDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor (the actual module class name).
   */
  protected ModuleDescriptor(String name) {

    super(name);

    BasicReferencePropertyDescriptor<Module> parentDescriptor = new BasicReferencePropertyDescriptor<Module>();
    parentDescriptor.setName("parent");
    parentDescriptor.setReferencedDescriptor(this);

    BasicCollectionDescriptor<Module> moduleListDescriptor = new BasicCollectionDescriptor<Module>();
    moduleListDescriptor.setCollectionInterface(List.class);
    moduleListDescriptor.setElementDescriptor(this);

    BasicCollectionPropertyDescriptor<Module> subModulesDescriptor = new BasicCollectionPropertyDescriptor<Module>();
    subModulesDescriptor.setReferencedDescriptor(moduleListDescriptor);
    subModulesDescriptor.setName("subModules");

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
    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);
    propertyDescriptors.add(i18nNameDescriptor);
    propertyDescriptors.add(i18nDescriptionDescriptor);
    propertyDescriptors.add(parentDescriptor);
    propertyDescriptors.add(subModulesDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }
}
