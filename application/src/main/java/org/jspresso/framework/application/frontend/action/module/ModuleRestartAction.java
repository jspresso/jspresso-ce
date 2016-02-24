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
package org.jspresso.framework.application.frontend.action.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;

/**
 * This action is used to restart a module. It cleans its children and executes
 * its startup action.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ModuleRestartAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * Gets the current module and restarts it. {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Module module = getModule(context);
    boolean startupResult = restartModule(module, actionHandler, context);
    return startupResult && super.execute(actionHandler, context);
  }

  /**
   * Restart a given module and its sticky children.
   *
   * @param module the module
   * @param actionHandler the action handler
   * @param context the context
   * @return the boolean
   */
  protected boolean restartModule(Module module, IActionHandler actionHandler, Map<String, Object> context) {
    List<Module> subModules = module.getSubModules();
    if (subModules != null) {
      Collection<Module> modulesToRemove = new ArrayList<>();
      for (Module subModule : subModules) {
        if (module.isSubModuleSticky(subModule)) {
          restartModule(subModule, actionHandler, context);
        } else {
          cleanSubModules(subModule);
          modulesToRemove.add(subModule);
        }
      }
      removeModulesFromParent(module, modulesToRemove);
    }
    boolean startupResult = true;
    if (module.getStartupAction() != null) {
      startupResult = actionHandler.execute(module.getStartupAction(), context);
    }
    return startupResult;
  }

  private Collection<Module> cleanSubModules(Module module) {
    Collection<Module> modulesToRemove = new ArrayList<>();
    List<Module> subModules = module.getSubModules();
    if (subModules != null) {
      for (Module subModule : subModules) {
        modulesToRemove.add(subModule);
        Collection<Module> grandSubModulesToRemove = cleanSubModules(subModule);
        if (!grandSubModulesToRemove.isEmpty()) {
          removeModulesFromParent(subModule, grandSubModulesToRemove);
        }
      }
    }
    return modulesToRemove;
  }

  private void removeModulesFromParent(Module parentModule, Collection<Module> subModulesToRemove) {
    parentModule.removeSubModules(subModulesToRemove);
    for (Module module : subModulesToRemove) {
      if (module instanceof BeanModule) {
        ((BeanModule) module).setModuleObject(null);
      } else if (module instanceof BeanCollectionModule) {
        ((BeanCollectionModule) module).setModuleObjects(null);
      }
    }
  }
}
