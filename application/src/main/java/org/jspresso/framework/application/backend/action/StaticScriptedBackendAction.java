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
import org.jspresso.framework.util.scripting.ScriptMixin;

/**
 * A statically scripted backend action. The script and the scripting language
 * are statically configured in the action itself.
 *
 * @author Vincent Vandenschrick
 */
public class StaticScriptedBackendAction extends ScriptedBackendAction {

  private String script;
  private String scriptLanguage;

  /**
   * Executes the action script using the script handler.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ScriptMixin scriptMixin = new ScriptMixin(this);
    scriptMixin.setLanguage(scriptLanguage);
    scriptMixin.setScript(script);
    setActionParameter(scriptMixin, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the script source code.
   *
   * @param script
   *          the script source code.
   */
  public void setScript(String script) {
    this.script = script;
  }

  /**
   * Sets the script language this scripted action is written in.
   *
   * @param scriptLanguage
   *          the scripting language.
   */
  public void setScriptLanguage(String scriptLanguage) {
    this.scriptLanguage = scriptLanguage;
  }
}
