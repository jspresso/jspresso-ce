/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.scripting;

/**
 * Interface implemented by ny scriptable object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IScript {

  /**
   * <code>SCRIPTED_OBJECT</code> is the script context key of the scripted
   * object (the object the script is working on).
   */
  String SCRIPTED_OBJECT = "SCRIPTED_OBJECT";

  /**
   * <code>CONTEXT</code> is the script execution context map.
   */
  String CONTEXT         = "CONTEXT";

  /**
   * Gets the script code.
   * 
   * @return the script code.
   */
  String getScript();

  /**
   * Gets the scripted object.
   * 
   * @return the scripted object.
   */
  Object getScriptedObject();

  /**
   * Gets the scripting language used.
   * 
   * @return the scripting language used.
   */
  String getLanguage();
}
