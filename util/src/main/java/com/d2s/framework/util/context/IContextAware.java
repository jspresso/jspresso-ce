/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.context;

import java.util.Map;

/**
 * This interface is implemented by all classes which need to be aware of a
 * context. This context is basically a <code>Map</code> of arbitrary values.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IContextAware {

  /**
   * Gets the current context.
   * 
   * @return the context key/value pairs.
   */
  Map<String, Object> getContext();

  /**
   * Sets the current context.
   * 
   * @param context
   *            the context to set.
   */
  void setContext(Map<String, Object> context);
}
