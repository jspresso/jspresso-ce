/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.scripting;

/**
 * Utility class helping to bring scripting capability in an object.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ScriptMixin implements IScript {

  private String language;
  private String script;
  private Object scriptedObject;

  /**
   * Constructs a new <code>ScriptMixin</code> instance.
   * 
   * @param scriptedObject
   *            the object this mixin is introduced in.
   */
  public ScriptMixin(Object scriptedObject) {
    this.scriptedObject = scriptedObject;
  }

  /**
   * Gets the language.
   * 
   * @return the language.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Gets the script.
   * 
   * @return the script.
   */
  public String getScript() {
    return script;
  }

  /**
   * Gets the scriptedObject.
   * 
   * @return the scriptedObject.
   */
  public Object getScriptedObject() {
    return scriptedObject;
  }

  /**
   * Sets the language.
   * 
   * @param language
   *            the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Sets the script.
   * 
   * @param script
   *            the script to set.
   */
  public void setScript(String script) {
    this.script = script;
  }
}
