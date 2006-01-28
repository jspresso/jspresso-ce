/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.scripting;

/**
 * The interface implemented by script executors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IScriptHandler {

  /**
   * Executes a script.
   * 
   * @param script
   *          the script to execute.
   */
  void execute(IScript script);

  /**
   * Evaluates a scripted expression.
   * 
   * @param script
   *          the script to evaluate.
   * @return the evaluation result.
   */
  Object evaluate(IScript script);
}
