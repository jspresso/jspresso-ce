/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.scripting.ScriptMixin;


/**
 * A scripted backend action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
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
  @SuppressWarnings("unchecked")
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ScriptMixin scriptMixin = new ScriptMixin(this);
    scriptMixin.setLanguage(scriptLanguage);
    scriptMixin.setScript(script);
    context.put(ActionContextConstants.ACTION_PARAM, scriptMixin);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the script source code.
   * 
   * @param script
   *            the script source code.
   */
  public void setScript(String script) {
    this.script = script;
  }

  /**
   * Sets the script language this scripted action is written in.
   * 
   * @param scriptLanguage
   *            the scripting language.
   */
  public void setScriptLanguage(String scriptLanguage) {
    this.scriptLanguage = scriptLanguage;
  }
}
