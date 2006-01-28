/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.scripting;

import java.util.Map;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

/**
 * This is the default implementation of the script handler interface. It relies
 * on Jakarta's "Bean Scripting Framework".
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultScriptHandler implements IScriptHandler {

  /**
   * Constructs a new <code>DefaultScriptHandler</code> instance.
   */
  public DefaultScriptHandler() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public Object evaluate(IScript scriptable) {
    BSFManager enginesManager = new BSFManager();
    if (scriptable.getContext() != null) {
      for (Map.Entry<String, Object> contextEntry : scriptable.getContext()
          .entrySet()) {
        enginesManager.registerBean(contextEntry.getKey(), contextEntry
            .getValue());
      }
    }
    try {
      return enginesManager.eval(scriptable.getLanguage(), null, 0, 0,
          scriptable.getScript());
    } catch (BSFException ex) {
      throw new ScriptException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void execute(IScript scriptable) {
    BSFManager enginesManager = new BSFManager();
    if (scriptable.getContext() != null) {
      for (Map.Entry<String, Object> contextEntry : scriptable.getContext()
          .entrySet()) {
        enginesManager.registerBean(contextEntry.getKey(), contextEntry
            .getValue());
      }
    }
    try {
      enginesManager.exec(scriptable.getLanguage(), null, 0, 0, scriptable
          .getScript());
    } catch (BSFException ex) {
      throw new ScriptException(ex);
    }
  }

}
