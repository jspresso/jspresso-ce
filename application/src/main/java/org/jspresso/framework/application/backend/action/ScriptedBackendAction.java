/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.scripting.IScript;
import org.jspresso.framework.util.scripting.IScriptHandler;


/**
 * A scripted backend action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ScriptedBackendAction extends AbstractBackendAction {

  private IScriptHandler scriptHandler;

  /**
   * Executes the action script using the script handler.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    scriptHandler.execute((IScript) context
        .get(ActionContextConstants.ACTION_PARAM), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the scriptHandler.
   * 
   * @param scriptHandler
   *            the scriptHandler to set.
   */
  public void setScriptHandler(IScriptHandler scriptHandler) {
    this.scriptHandler = scriptHandler;
  }
}
