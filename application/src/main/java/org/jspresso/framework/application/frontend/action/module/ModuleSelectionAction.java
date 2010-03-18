/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * Can be used as startup action to select and display a module when the
 * application launches.
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

  private String workspaceName;
  private String moduleName;

  /**
   * Displays the workspace.and module
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    getController(context).displayWorkspace(workspaceName);

    Workspace ws = getController(context).getWorkspace(workspaceName);
    for (Module m : ws.getModules()) {
      if (m.getName().equals(moduleName)) {
        getController(context).displayModule(m);
        break;
      }
    }

    return super.execute(actionHandler, context);
  }

  /**
   * Sets the workspaceName.
   * 
   * @param workspaceName
   *          the workspaceName to set.
   */
  public void setWorkspaceName(String workspaceName) {
    this.workspaceName = workspaceName;
  }

  /**
   * Sets the moduleName.
   * 
   * @param moduleName
   *          the moduleName to set.
   */
  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

}
