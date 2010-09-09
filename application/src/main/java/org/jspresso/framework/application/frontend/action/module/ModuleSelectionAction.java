/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;

/**
 * Displays a module, and the corresponding workspace if necessary based on
 * their names. It can be used as startup action to select and display a module
 * when the application launches.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ModuleSelectionAction<E, F, G> extends FrontendAction<E, F, G> {

  private String moduleName;
  private String workspaceName;

  /**
   * Displays the workspace and module.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    String wsName = getWorkspaceName(context);
    String moName = getModuleName(context);

    Workspace ws = getController(context).getWorkspace(wsName);
    for (Module m : ws.getModules()) {
      if (moName.equals(m.getName())) {
        getController(context).displayModule(wsName, m);
        break;
      }
    }

    return super.execute(actionHandler, context);
  }

  /**
   * Configures the name (untranslated) of the module to be displayed.
   * 
   * @param moduleName
   *          the moduleName to set.
   */
  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  /**
   * Configures the name (untranslated) of the workspace to be displayed.
   * 
   * @param workspaceName
   *          the workspaceName to set.
   */
  public void setWorkspaceName(String workspaceName) {
    this.workspaceName = workspaceName;
  }

  /**
   * Gets the moduleName.
   * 
   * @param context
   *          the action context.
   * @return the moduleName.
   */
  protected String getModuleName(Map<String, Object> context) {
    return moduleName;
  }

  /**
   * Gets the workspaceName.
   * 
   * @param context
   *          the action context.
   * @return the workspaceName.
   */
  protected String getWorkspaceName(Map<String, Object> context) {
    return workspaceName;
  }

}
