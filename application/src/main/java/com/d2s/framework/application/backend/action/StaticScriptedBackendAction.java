/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.util.scripting.ScriptMixin;

/**
 * A scripted backend action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class StaticScriptedBackendAction extends ScriptedBackendAction {

  private String scriptLanguage;
  private String script;

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
   * Sets the script language this scripted action is written in.
   * 
   * @param scriptLanguage
   *          the scripting language.
   */
  public void setScriptLanguage(String scriptLanguage) {
    this.scriptLanguage = scriptLanguage;
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
}
