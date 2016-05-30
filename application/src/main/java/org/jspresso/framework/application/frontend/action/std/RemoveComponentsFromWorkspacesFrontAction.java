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
package org.jspresso.framework.application.frontend.action.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;

/**
 * Remove selected models from workspaces front action.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 */
public class RemoveComponentsFromWorkspacesFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Collection<?> selectedModels = getSelectedModels(context);
    if (selectedModels != null) {
      removeFromWorkspaces(selectedModels, context);
    }
    return super.execute(actionHandler, context);
  }

  private void removeFromWorkspaces(Collection<?> componentsToDelete, Map<String, Object> context) {
    IFrontendController<E, F, G> controller = getController(context);
    for (String wsName : controller.getWorkspaceNames()) {
      Workspace workspace = controller.getWorkspace(wsName);
      if (workspace != null) {
        for (Module module : workspace.getModules()) {
          Collection<Module> subModulesToRemove = cleanComponentsFromModules(module, componentsToDelete);
          removeSubModules(module, subModulesToRemove);
        }
      }
    }
  }

  private Collection<Module> cleanComponentsFromModules(Module module, Collection<?> componentsToDelete) {
    if (module instanceof BeanCollectionModule) {
      List<?> moduleObjects = ((BeanCollectionModule) module).getModuleObjects();
      if (moduleObjects != null) {
        for (Object componentToDelete : componentsToDelete) {
          if (moduleObjects.contains(componentToDelete)) {
            ((BeanCollectionModule) module).removeFromModuleObjects(componentToDelete);
          }
        }
      }
    }
    Collection<Module> modulesToRemove = new ArrayList<>();
    List<Module> subModules = module.getSubModules();
    if (subModules != null) {
      for (Module subModule : subModules) {
        if (subModule instanceof BeanModule && componentsToDelete.contains(
            ((BeanModule) subModule).getModuleObject())) {
          ((BeanModule) subModule).setModuleObject(null);
          modulesToRemove.add(subModule);
        }
        Collection<Module> grandSubModulesToRemove = cleanComponentsFromModules(subModule, componentsToDelete);
        removeSubModules(subModule, grandSubModulesToRemove);
      }
    }
    return modulesToRemove;
  }

  private void removeSubModules(Module module, Collection<Module> subModulesToRemove) {
    if (!subModulesToRemove.isEmpty()) {
      module.removeSubModules(subModulesToRemove);
      for (Module grandSubModuleToRemove : subModulesToRemove) {
        ((BeanModule) grandSubModuleToRemove).setModuleObject(null);
      }
    }
  }
}
