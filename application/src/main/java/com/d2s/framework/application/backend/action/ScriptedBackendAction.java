/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import com.d2s.framework.util.scripting.IScriptHandler;
import com.d2s.framework.util.scripting.ScriptMixin;
import com.d2s.framework.view.action.IActionHandler;

/**
 * A scripted backend action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ScriptedBackendAction extends AbstractBackendAction {

  private IScriptHandler scriptHandler;
  private ScriptMixin    scriptMixin;

  /**
   * Constructs a new <code>ScriptedBackendAction</code> instance.
   */
  public ScriptedBackendAction() {
    scriptMixin = new ScriptMixin(this);
  }

  /**
   * Executes the action script using the script handler.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    scriptHandler.execute(scriptMixin);
  }

  /**
   * Sets the scriptHandler.
   * 
   * @param scriptHandler
   *          the scriptHandler to set.
   */
  public void setScriptHandler(IScriptHandler scriptHandler) {
    this.scriptHandler = scriptHandler;
  }

  /**
   * Sets the script language this scripted action is written in.
   * 
   * @param scriptLanguage
   *          the scripting language.
   */
  public void setScriptLanguage(String scriptLanguage) {
    scriptMixin.setLanguage(scriptLanguage);
  }

  /**
   * Sets the script source code.
   * 
   * @param script
   *          the script source code.
   */
  public void setScript(String script) {
    scriptMixin.setScript(script);
  }
}
