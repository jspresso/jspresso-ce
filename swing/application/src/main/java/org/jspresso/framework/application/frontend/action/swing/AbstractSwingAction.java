/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.swing;

import java.awt.Dialog;
import java.awt.Window;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.frontend.action.ActionWrapper;
import org.jspresso.framework.util.swing.SwingUtil;


/**
 * This class serves as base class for swing actions. It provides accessors on
 * commonly used artifacts.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractSwingAction extends
    ActionWrapper<JComponent, Icon, Action> {

  /**
   * Retrieves the widget which triggered the action from the action context.
   * 
   * @param context
   *            the action context.
   * @return the widget which triggered the action.
   */
  public JComponent getActionWidget(Map<String, Object> context) {
    return (JComponent) context.get(ActionContextConstants.ACTION_WIDGET);
  }

  /**
   * Retrieves the widget this action was triggered from. It may serve to
   * determine the root window or dialog for instance. It uses a well-known
   * action context key which is :
   * <li> <code>ActionContextConstants.SOURCE_COMPONENT</code>.
   * 
   * @param context
   *            the action context.
   * @return the source widget this action was triggered from.
   */
  public JComponent getSourceComponent(Map<String, Object> context) {
    return (JComponent) context.get(ActionContextConstants.SOURCE_COMPONENT);
  }

  /**
   * If the ancestor of the action widget is a dialog, dispose it.
   * 
   * @param context
   *            the action context.
   */
  protected void closeDialog(Map<String, Object> context) {
    Window actionWindow = SwingUtil.getVisibleWindow(getActionWidget(context));
    if (actionWindow instanceof Dialog) {
      actionWindow.dispose();
    }
  }
}
