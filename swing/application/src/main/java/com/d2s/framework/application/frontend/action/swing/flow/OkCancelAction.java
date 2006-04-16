/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.flow;

import java.util.Map;

import javax.swing.JOptionPane;

import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.action.IAction;
import com.d2s.framework.view.action.IActionHandler;

/**
 * Action to ask a user validation.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OkCancelAction extends AbstractMessageAction {

  private IAction okAction;
  private IAction cancelAction;

  /**
   * Displays the message using a <code>JOptionPane.OK_CANCEL_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    int selectedOption = JOptionPane.showInternalConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(getSourceComponent(context)), getMessage(),
        getName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
        getIconFactory(context).getIcon(getIconImageURL(),
            IIconFactory.LARGE_ICON_SIZE));
    if (selectedOption == JOptionPane.OK_OPTION) {
      setNextAction(okAction);
    } else {
      setNextAction(cancelAction);
    }
    super.execute(actionHandler, context);
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IAction okAction) {
    this.okAction = okAction;
  }

}
