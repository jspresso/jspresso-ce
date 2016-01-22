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
package org.jspresso.framework.application.backend.action;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.scripting.IScript;
import org.jspresso.framework.util.scripting.IScriptHandler;

/**
 * A scripted backend action. The action takes the script to execute (an
 * {@code IScript} implementation) out of its context (using
 * {@code ActionParameter}) and delegates the actual script execution to a
 * {@code IScriptHandler} configured through the {@code scriptHandler}
 * property.
 *
 * @author Vincent Vandenschrick
 */
public class ScriptedBackendAction extends BackendAction {

  private IScriptHandler scriptHandler;

  /**
   * Executes the action script using the script handler.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    scriptHandler.execute((IScript) getActionParameter(context), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the script handler to use to perform the script execution.
   *
   * @param scriptHandler
   *          the scriptHandler to set.
   */
  public void setScriptHandler(IScriptHandler scriptHandler) {
    this.scriptHandler = scriptHandler;
  }
}
