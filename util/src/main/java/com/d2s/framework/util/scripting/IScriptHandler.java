/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.scripting;

import java.util.Map;

/**
 * The interface implemented by script executors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IScriptHandler {

  /**
   * Evaluates a scripted expression.
   * 
   * @param script
   *            the script to evaluate.
   * @param context
   *            the action context.
   * @return the evaluation result.
   */
  Object evaluate(IScript script, Map<String, Object> context);

  /**
   * Executes a script.
   * 
   * @param script
   *            the script to execute.
   * @param context
   *            the action context.
   */
  void execute(IScript script, Map<String, Object> context);
}
