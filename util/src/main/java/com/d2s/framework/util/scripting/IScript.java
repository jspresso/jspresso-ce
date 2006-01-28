/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.scripting;

import java.util.Map;

import com.d2s.framework.util.context.IContextAware;

/**
 * Interface implemented by ny scriptable object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IScript extends IContextAware {

  /**
   * <code>SCRIPTED_OBJECT</code> is the script context key of the scripted
   * object (the object the script is working on).
   */
  String SCRIPTED_OBJECT = "SCRIPTED_OBJECT";

  /**
   * <code>SCRIPT</code> is the script context key of the script object
   * itself. It is generally used to set the script execution result.
   */
  String SCRIPT          = "SCRIPT";

  /**
   * Gets the script code.
   * 
   * @return the script code.
   */
  String getScript();

  /**
   * Gets the scripting language used.
   * 
   * @return the scripting language used.
   */
  String getLanguage();

  /**
   * Gets the script execution result.
   * 
   * @return the script execution result.
   */
  Map<String, Object> getExecutionResult();

  /**
   * Sets the script execution result.
   * 
   * @param executionResult
   *          the script execution result.
   */
  void setExecutionResult(Map<String, Object> executionResult);
}
