/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.Workspace;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicComponentDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicStringPropertyDescriptor;

/**
 * The model descriptor of workspace objects.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WorkspaceDescriptor extends BasicComponentDescriptor<Workspace> {

  /**
   * <code>WORKSPACE_DESCRIPTOR</code> is a unique reference to the model
   * descriptor of workspaces.
   */
  public static final IComponentDescriptor<Workspace> WORKSPACE_DESCRIPTOR = new WorkspaceDescriptor(
                                                                               Workspace.class
                                                                                   .getName());

  /**
   * Constructs a new <code>ModuleDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor (the actual module class name).
   */
  protected WorkspaceDescriptor(String name) {

    super(name);

    BasicCollectionDescriptor<Module> moduleListDescriptor = new BasicCollectionDescriptor<Module>();
    moduleListDescriptor.setCollectionInterface(List.class);
    moduleListDescriptor.setElementDescriptor(ModuleDescriptor.MODULE_DESCRIPTOR);

    BasicCollectionPropertyDescriptor<Module> modulesDescriptor = new BasicCollectionPropertyDescriptor<Module>();
    modulesDescriptor.setReferencedDescriptor(moduleListDescriptor);
    modulesDescriptor.setName("modules");

    BasicStringPropertyDescriptor nameDescriptor = new BasicStringPropertyDescriptor();
    nameDescriptor.setName("name");

    BasicStringPropertyDescriptor descriptionDescriptor = new BasicStringPropertyDescriptor();
    descriptionDescriptor.setName("description");

    BasicStringPropertyDescriptor i18nNameDescriptor = new BasicStringPropertyDescriptor();
    i18nNameDescriptor.setName("i18nName");

    BasicStringPropertyDescriptor i18nDescriptionDescriptor = new BasicStringPropertyDescriptor();
    i18nDescriptionDescriptor.setName("i18nDescription");

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);
    propertyDescriptors.add(i18nNameDescriptor);
    propertyDescriptors.add(i18nDescriptionDescriptor);
    propertyDescriptors.add(modulesDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }
}
