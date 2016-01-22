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
package org.jspresso.framework.application.frontend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.module.AddBeanAsSubModuleAction;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This action can be installed on any collection view and will take the
 * selected elements in the underlying model collection and create a bean module
 * out of them.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Maxime HAMM
 * @see org.jspresso.framework.application.backend.action.module.AddBeanAsSubModuleAction
 */
@SuppressWarnings("UnusedParameters")
public class AddBeanAsSubModuleFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  private String          parentWorkspaceName;
  private String          parentModuleName;
  private IViewDescriptor childModuleProjectedViewDescriptor;
  private String          referencePath;

  /**
   * Sets the WorkspaceName where to add components to.
   *
   * @param parentWorkspaceName
   *     the parent workspace name.
   */
  public void setParentWorkspaceName(String parentWorkspaceName) {
    this.parentWorkspaceName = parentWorkspaceName;
  }

  /**
   * Gets the target workspace to add the module to.
   *
   * @param context
   *     the action context.
   * @return the target workspace to add the module to.
   */
  protected String getParentWorkspaceName(Map<String, Object> context) {
    if (parentWorkspaceName != null) {
      return parentWorkspaceName;
    }
    return getController(context).getSelectedWorkspaceName();
  }

  /**
   * Sets the parent module where to add components to.
   *
   * @param parentModuleName
   *     the parent module name.
   */
  public void setParentModuleName(String parentModuleName) {
    this.parentModuleName = parentModuleName;
  }

  /**
   * Gets the target module to add the module to.
   *
   * @param context
   *     the action context.
   * @return the target module to add the module to.
   */
  protected String getParentModuleName(Map<String, Object> context) {
    return this.parentModuleName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

    String workspaceName = getParentWorkspaceName(context);
    if (workspaceName != null) {
      context.put(AddBeanAsSubModuleAction.PARENT_WORKSPACE, getController(context).getWorkspace(workspaceName));
    }

    String moduleName = getParentModuleName(context);
    if (moduleName != null) {
      context.put(AddBeanAsSubModuleAction.PARENT_MODULE_NAME, moduleName);
    }

    IViewDescriptor projectedViewDescriptor = getChildModuleProjectedViewDescriptor(context);
    if (projectedViewDescriptor != null) {
      context.put(AddBeanAsSubModuleAction.PROJECTED_VIEW_DESCRIPTOR, projectedViewDescriptor);
    }

    setActionParameter(getComponentsToAdd(context), context);

    return super.execute(actionHandler, context);
  }

  /**
   * Gets the components to add as child modules. Defaults to the view selected
   * models.
   *
   * @param context
   *     the action context.
   * @return the components to add as child modules.
   */
  protected List<?> getComponentsToAdd(Map<String, Object> context) {
    List<?> componentsToAdd = getSelectedModels(context);
    if (getReferencePath() == null || componentsToAdd == null || componentsToAdd.isEmpty()) {
      return componentsToAdd;
    }

    try {
      IAccessorFactory accessorFactory = getBackendController(context).getAccessorFactory();
      List<Object> references = new ArrayList<>();
      for (Object component : componentsToAdd) {
        Class<?> componentContract;
        if (component instanceof IComponent) {
          componentContract = ((IComponent) component).getComponentContract();
        } else {
          componentContract = component.getClass();
        }
        IAccessor accessor = accessorFactory.createPropertyAccessor(getReferencePath(), componentContract);
        Object reference = accessor.getValue(component);
        if (reference instanceof Collection<?>) {
          references.addAll((Collection<?>) reference);
        } else {
          references.add(reference);
        }
      }
      return references;
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new NestedRuntimeException(e);
    }
  }

  /**
   * Gets the childModuleProjectedViewDescriptor.
   *
   * @param context
   *     the action context.
   * @return the childModuleProjectedViewDescriptor.
   */
  protected IViewDescriptor getChildModuleProjectedViewDescriptor(Map<String, Object> context) {
    return childModuleProjectedViewDescriptor;
  }

  /**
   * Sets the childModuleProjectedViewDescriptor.
   *
   * @param childModuleProjectedViewDescriptor
   *     the childModuleProjectedViewDescriptor to set.
   */
  public void setChildModuleProjectedViewDescriptor(IViewDescriptor childModuleProjectedViewDescriptor) {
    this.childModuleProjectedViewDescriptor = childModuleProjectedViewDescriptor;
  }

  /**
   * Gets the referencePath.
   *
   * @return the referencePath.
   */
  protected String getReferencePath() {
    return referencePath;
  }

  /**
   * Allows to configure a property path to extract the bean(s) to add from the selected models. If {@code null},
   * which is the default value, the selected models are used themselves.
   *
   * @param referencePath
   *     the referencePath to set.
   */
  public void setReferencePath(String referencePath) {
    this.referencePath = referencePath;
  }
}
