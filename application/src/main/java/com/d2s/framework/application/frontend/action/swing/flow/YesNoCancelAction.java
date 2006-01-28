/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.flow;

import java.util.Map;

import javax.swing.JOptionPane;

import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.view.IIconFactory;

/**
 * Action to ask a binary question to the user with a cancel option.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoCancelAction extends AbstractMessageAction {

  private IAction yesAction;
  private IAction noAction;
  private IAction cancelAction;

  /**
   * Displays the message using a <code>JOptionPane.YES_NO_CANCEL_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    int selectedOption = JOptionPane.showInternalConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(getSourceComponent()), getMessage(),
        getName(), JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, getIconFactory().getIcon(
            getIconImageURL(), IIconFactory.LARGE_ICON_SIZE));
    if (selectedOption == JOptionPane.CANCEL_OPTION) {
      setNextAction(cancelAction);
    } else if (selectedOption == JOptionPane.YES_OPTION) {
      setNextAction(yesAction);
    } else {
      setNextAction(noAction);
    }
    return super.execute(actionHandler);
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
   * Sets the noAction.
   * 
   * @param noAction
   *          the noAction to set.
   */
  public void setNoAction(IAction noAction) {
    this.noAction = noAction;
  }

  /**
   * Sets the yesAction.
   * 
   * @param yesAction
   *          the yesAction to set.
   */
  public void setYesAction(IAction yesAction) {
    this.yesAction = yesAction;
  }
}
