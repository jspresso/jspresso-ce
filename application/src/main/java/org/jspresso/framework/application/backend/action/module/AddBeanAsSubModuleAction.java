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

import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
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

  /**
   * <code>PARENT_WORKSPACE</code> is &quot;PARENT_WORKSPACE&quot;.
   */
  public static final String PARENT_WORKSPACE   = "PARENT_WORKSPACE";
  /**
   * <code>PARENT_MODULE_NAME</code> is &quot;PARENT_MODULE_NAME&quot;.
   */
  public static final String PARENT_MODULE_NAME = "PARENT_MODULE_NAME";

  private IViewDescriptor    childModuleProjectedViewDescriptor;

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

      preSelectModuleObject(nextSelectedModuleObject, context);

      Module parentModule = findDestinationModule(nextSelectedModuleObject,
          context);
      List<Module> childModules = parentModule.getSubModules();
      Module newSubModule = null;

      Module nextSubModule = createChildModule(parentModule,
          childComponentDescriptor, nextSelectedModuleObject, context);
      int nextSubModuleIndex = -1;
      if (childModules != null) {
        nextSubModuleIndex = childModules.indexOf(nextSubModule);
      }
      if (nextSubModuleIndex < 0) {
        newSubModule = nextSubModule;
        if (moduleToSelect == null) {
          moduleToSelect = nextSubModule;
        }
      } else if (moduleToSelect == null && childModules != null) {
        moduleToSelect = childModules.get(nextSubModuleIndex);
      }

      if (newSubModule != null) {
        parentModule.addSubModule(newSubModule);
      }

      postSelectModuleObject(nextSelectedModuleObject, context);
    }

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

  /**
   * Gets the parent workspace if found in context using key
   * {@link AddBeanAsSubModuleAction#PARENT_WORKSPACE}.
   * 
   * @param context
   *          the action context.
   * @return the target workspace name.
   */
  protected Workspace getParentWorkspace(Map<String, Object> context) {
    return (Workspace) context.get(PARENT_WORKSPACE);
  }

  /**
   * Gets parent module name if found in context using key
   * {@link AddBeanAsSubModuleAction#PARENT_MODULE_NAME}.
   * 
   * @param context
   *          the action context.
   * @return the target parent module name.
   */
  public static String getParentModuleName(Map<String, Object> context) {
    return (String) context.get(PARENT_MODULE_NAME);
  }

  /**
   * Finds the parent module where to add the selected component.
   * 
   * @param component
   *          the component to add as module object.
   * @param context
   *          the action context.
   * @return the parent module to add the new module to.
   */
  protected Module findDestinationModule(Object component,
      Map<String, Object> context) {
    Workspace workspace = getParentWorkspace(context);
    if (workspace == null) {
      return getCurrentModule(context);
    }
    String moduleName = getParentModuleName(context);
    for (Module m : workspace.getModules()) {
      Module m2 = findModule(m, moduleName, null);
      if (m2 != null) {
        return m2;
      }
    }
    return null;
  }

  /**
   * Notifies that a module object is going to been selected. Does nothing by
   * default. Override if needed.
   * 
   * @param moduleObject
   *          the module object that will been selected.
   * @param context
   *          the action context.
   */
  protected void preSelectModuleObject(Object moduleObject,
      Map<String, Object> context) {
    // Empty implementation by default.
  }

  /**
   * Notifies that a module object has been selected. Does nothing by default.
   * Override if needed.
   * 
   * @param moduleObject
   *          the module object that has been selected.
   * @param context
   *          the action context.
   */
  protected void postSelectModuleObject(Object moduleObject,
      Map<String, Object> context) {
    // Empty implementation by default.
  }

  /**
   * Finds the existing module for a module object.
   * 
   * @param rootModule
   *          the root module to start the search from.
   * @param moduleObject
   *          the module object.
   * @param context
   *          the action context.
   * @return the stack of modules containing the found module.
   */
  public Stack<Module> findCurrentModule(Module rootModule,
      Object moduleObject, Map<String, Object> context) {
    Stack<Module> stack = new Stack<Module>();
    findCurrentModule(rootModule, moduleObject, stack);
    return stack;
  }

  private Module findCurrentModule(Module rootModule,
      Object nextSelectedModuleObject, Stack<Module> stack) {
    stack.push(rootModule);
    if (rootModule instanceof BeanModule
        && nextSelectedModuleObject.equals(((BeanModule) rootModule)
            .getModuleObject())) {
      return rootModule;
    }
    if (rootModule.getSubModules() != null) {
      for (Module sub : rootModule.getSubModules()) {
        Module parentModule = findCurrentModule(sub, nextSelectedModuleObject,
            stack);
        if (parentModule != null) {
          return parentModule;
        }
      }
    }
    stack.pop();
    return null;
  }

  /**
   * Finds the existing module for a module object or name.
   * 
   * @param rootModule
   *          the root module to start the search from.
   * @param moduleObject
   *          the module object.
   * @param moduleName
   *          the module name.
   * @return the stack of modules containing the found module.
   */
  protected Module findModule(Module rootModule, String moduleName,
      Object moduleObject) {
    if (moduleName != null && moduleName.equals(rootModule.getName())) {
      return rootModule;
    }
    if (moduleObject != null && (rootModule instanceof BeanModule)
        && moduleObject.equals(((BeanModule) rootModule).getModuleObject())) {
      return rootModule;
    }
    if (rootModule.getSubModules() != null) {
      for (Module sub : rootModule.getSubModules()) {
        Module parentModule = findModule(sub, moduleName, moduleObject);
        if (parentModule != null) {
          return parentModule;
        }
      }
    }
    return null;
  }

}
