/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing;

import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.view.action.ActionContextConstants;

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
public abstract class AbstractSwingAction extends AbstractChainedAction<JComponent, Icon> {

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
  public JComponent getSourceComponent(Map<String, Object> context) {
    return (JComponent) context.get(ActionContextConstants.SOURCE_COMPONENT);
  }
}
