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
package org.jspresso.framework.util.scripting;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * This is the default implementation of the script handler interface. It relies
 * on Jakarta's "Bean Scripting Framework".
 *
 * @author Vincent Vandenschrick
 */
public class DefaultScriptHandler implements IScriptHandler {

  /**
   * Constructs a new {@code DefaultScriptHandler} instance.
   */
  public DefaultScriptHandler() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object evaluate(IScript scriptable, Map<String, Object> context) {
    ScriptEngineManager enginesManager = new ScriptEngineManager();
    ScriptEngine engine = enginesManager.getEngineByName(scriptable.getLanguage());
    if (context != null) {
      engine.put(IScript.CONTEXT, context);
      engine.put(IScript.SCRIPTED_OBJECT, scriptable.getScriptedObject());
    }
    try {
      return engine.eval(scriptable.getScript());
    } catch (javax.script.ScriptException ex) {
      throw new ScriptException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(IScript scriptable, Map<String, Object> context) {
    evaluate(scriptable, context);
  }

}
