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

import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;

/**
 * The model descriptor of module objects.
 *
 * @author Vincent Vandenschrick
 * @internal
 */
public class ModuleDescriptor extends BasicComponentDescriptor<Module> {

  /**
   * {@code MODULE_DESCRIPTOR} is a unique reference to the model
   * descriptor of modules.
   */
  public static final IComponentDescriptor<Module> MODULE_DESCRIPTOR = new ModuleDescriptor(
                                                                         Module.class
                                                                             .getName());

  /**
   * Constructs a new {@code ModuleDescriptor} instance.
   *
   * @param name
   *          the name of the descriptor (the actual module class name).
   */
  protected ModuleDescriptor(String name) {

    super(name);

    BasicReferencePropertyDescriptor<Module> parentDescriptor = new BasicReferencePropertyDescriptor<>();
    parentDescriptor.setName(Module.PARENT);
    parentDescriptor.setReferencedDescriptor(this);

    BasicListDescriptor<Module> moduleListDescriptor = new BasicListDescriptor<>();
    moduleListDescriptor.setElementDescriptor(this);

    BasicCollectionPropertyDescriptor<Module> subModulesDescriptor = new BasicCollectionPropertyDescriptor<>();
    subModulesDescriptor.setReferencedDescriptor(moduleListDescriptor);
    subModulesDescriptor.setName(Module.SUB_MODULES);

    BasicStringPropertyDescriptor nameDescriptor = new BasicStringPropertyDescriptor();
    nameDescriptor.setName(Module.NAME);

    BasicStringPropertyDescriptor descriptionDescriptor = new BasicStringPropertyDescriptor();
    descriptionDescriptor.setName(Module.DESCRIPTION);

    BasicStringPropertyDescriptor i18nNameDescriptor = new BasicStringPropertyDescriptor();
    i18nNameDescriptor.setName(Module.I18N_NAME);

    BasicStringPropertyDescriptor i18nDescriptionDescriptor = new BasicStringPropertyDescriptor();
    i18nDescriptionDescriptor.setName(Module.I18N_DESCRIPTION);

    parentDescriptor.setReverseRelationEnd(subModulesDescriptor);
    subModulesDescriptor.setReverseRelationEnd(parentDescriptor);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<>();
    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);
    propertyDescriptors.add(i18nNameDescriptor);
    propertyDescriptors.add(i18nDescriptionDescriptor);
    propertyDescriptors.add(parentDescriptor);
    propertyDescriptors.add(subModulesDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }
}
