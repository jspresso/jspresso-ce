/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This action can be installed on any collection view and will :
 * <ol>
 * <li>take the selected elements in the underlying model collection,</li>
 * <li>create bean modules out of them, using the
 * <code>childModuleProjectedViewDescriptor</code> as projected view if it has
 * been configured,</li>
 * <li>add the created bean modules as children of the currently selected
 * module, vizualizing them in the workspace navigation tree.</li>
 * </ol>
 * Whenever there is no <code>childModuleProjectedViewDescriptor</code>
 * configured, and the currently selected module is a bean collection module,
 * the created modules projected view descriptor is taken from the bean
 * collection module (<code>elementViewDescriptor</code>).
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddBeanAsSubModuleAction extends BackendAction {

  private IViewDescriptor childModuleProjectedViewDescriptor;

  /**
   * Adds the selected objects as child modules.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<?> selectedModels = (List<?>) getActionParameter(context);
    if (selectedModels == null) {
      selectedModels = getSelectedModels(context);
    }
    IModelDescriptor modelDescriptor = getModelDescriptor(context);
    Module parentModule = getModule(context);
    List<Module> childModules = parentModule.getSubModules();
    List<Module> newSubModules = new ArrayList<Module>();

    IComponentDescriptor<?> childComponentDescriptor;
    if (modelDescriptor instanceof ICollectionDescriptorProvider<?>) {
      childComponentDescriptor = ((ICollectionDescriptorProvider<?>) modelDescriptor)
          .getCollectionDescriptor().getElementDescriptor();
    } else {
      childComponentDescriptor = ((IComponentDescriptorProvider<?>) modelDescriptor)
          .getComponentDescriptor();
    }

    Module moduleToSelect = null;
    for (Object nextSelectedModuleObject : selectedModels) {
      Module nextSubModule = createChildModule(parentModule,
          childComponentDescriptor, nextSelectedModuleObject, context);
      int nextSubModuleIndex = -1;
      if (childModules != null) {
        nextSubModuleIndex = childModules.indexOf(nextSubModule);
      }
      if (nextSubModuleIndex < 0) {
        newSubModules.add(nextSubModule);
        if (moduleToSelect == null) {
          moduleToSelect = nextSubModule;
        }
      } else if (moduleToSelect == null && childModules != null) {
        moduleToSelect = childModules.get(nextSubModuleIndex);
      }
    }
    parentModule.addSubModules(newSubModules);
    setActionParameter(moduleToSelect, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the childModuleProjectedViewDescriptor.
   * 
   * @param childModuleProjectedViewDescriptor
   *          the childModuleProjectedViewDescriptor to set.
   */
  public void setChildModuleProjectedViewDescriptor(
      IViewDescriptor childModuleProjectedViewDescriptor) {
    this.childModuleProjectedViewDescriptor = childModuleProjectedViewDescriptor;
  }

  /**
   * Creates a module to be added to the currently selected module as child.
   * 
   * @param parentModule
   *          the parent module.
   * @param childComponentDescriptor
   *          the child component descriptor.
   * @param childModuleObject
   *          the child module object to create the child module for.
   * @param context
   *          the action context.
   * @return the created child module.
   */
  @SuppressWarnings("unchecked")
  protected Module createChildModule(Module parentModule,
      IComponentDescriptor<?> childComponentDescriptor,
      Object childModuleObject, Map<String, Object> context) {
    BeanModule childModule = new BeanModule();
    IViewDescriptor projectedViewDescriptor = getChildModuleProjectedViewDescriptor(context);
    if (projectedViewDescriptor != null) {
      childModule.setProjectedViewDescriptor(projectedViewDescriptor);
    } else if (parentModule instanceof BeanCollectionModule) {
      childModule
          .setProjectedViewDescriptor(((BeanCollectionModule) parentModule)
              .getElementViewDescriptor());
    }
    childModule
        .setComponentDescriptor((IComponentDescriptor<Object>) childComponentDescriptor);
    childModule.setModuleObject(childModuleObject);
    childModule.setI18nName(String.valueOf(childModuleObject));
    return childModule;
  }

  /**
   * Gets the childModuleProjectedViewDescriptor.
   * 
   * @param context
   *          the action context.
   * @return the childModuleProjectedViewDescriptor.
   */
  protected IViewDescriptor getChildModuleProjectedViewDescriptor(
      Map<String, Object> context) {
    return childModuleProjectedViewDescriptor;
  }

}
