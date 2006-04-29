/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * This class serves as base class for swing actions. It provides accessors on
 * commonly used artifacts.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractUlcAction extends AbstractChainedAction<ULCComponent, ULCIcon, IAction> {

  /**
   * Retrieves the widget this action was triggered from. It may serve to
   * determine the root window or dialog for instance. It uses a well-known
   * action context key which is :
   * <li> <code>ActionContextConstants.SOURCE_COMPONENT</code>.
   * 
   * @param context
   *          the action context.
   * @return the source widget this action was triggered from.
   */
  public ULCComponent getSourceComponent(Map<String, Object> context) {
    return (ULCComponent) context.get(ActionContextConstants.SOURCE_COMPONENT);
  }
}
