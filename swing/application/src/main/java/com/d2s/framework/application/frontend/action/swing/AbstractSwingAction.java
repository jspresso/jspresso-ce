/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing;

import java.awt.Dialog;
import java.awt.Window;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.application.frontend.action.ActionWrapper;

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
public abstract class AbstractSwingAction extends
    ActionWrapper<JComponent, Icon, Action> {

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

  /**
   * Retrieves the widget which triggered the action from the action context.
   * 
   * @param context
   *          the action context.
   * @return the widget which triggered the action.
   */
  public JComponent getActionWidget(Map<String, Object> context) {
    return (JComponent) context.get(ActionContextConstants.ACTION_WIDGET);
  }

  /**
   * If the ancestor of the action widget is a dialog, dispose it.
   * 
   * @param context
   *          the action context.
   */
  protected void closeDialog(Map<String, Object> context) {
    Window actionWindow = SwingUtilities
        .windowForComponent(getActionWidget(context));
    if (actionWindow instanceof Dialog) {
      actionWindow.dispose();
    }
  }
}
