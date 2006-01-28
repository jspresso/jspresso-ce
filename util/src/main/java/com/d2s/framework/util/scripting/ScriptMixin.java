/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.scripting;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class helping to bring scripting capability in an object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ScriptMixin implements IScript {

  private String              script;
  private String              language;
  private Map<String, Object> scriptContext;
  private Map<String, Object> executionResult;
  private Object              scriptedObject;

  /**
   * Constructs a new <code>ScriptMixin</code> instance.
   * 
   * @param scriptedObject
   *          the object this mixin is introduced in.
   */
  public ScriptMixin(Object scriptedObject) {
    this.scriptedObject = scriptedObject;
  }

  /**
   * Returns a context containing :
   * <li> any value from the context set through the setContext method.
   * <li> SCRIPT : the mixin itself.
   * <li> SCRIPTED_OBJECT : the object the mixin is introduced in.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> getContext() {
    if (scriptContext == null) {
      scriptContext = new HashMap<String, Object>();
    }
    scriptContext.put(IScript.SCRIPT, this);
    scriptContext.put(IScript.SCRIPTED_OBJECT, scriptedObject);
    return scriptContext;
  }

  /**
   * {@inheritDoc}
   */
  public void setContext(Map<String, Object> context) {
    this.scriptContext = context;
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
   * Sets the language.
   * 
   * @param language
   *          the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Sets the script.
   * 
   * @param script
   *          the script to set.
   */
  public void setScript(String script) {
    this.script = script;
  }

  /**
   * Gets the executionResult.
   * 
   * @return the executionResult.
   */
  public Map<String, Object> getExecutionResult() {
    return executionResult;
  }

  /**
   * Sets the executionResult.
   * 
   * @param executionResult
   *          the executionResult to set.
   */
  public void setExecutionResult(Map<String, Object> executionResult) {
    this.executionResult = executionResult;
  }
}
