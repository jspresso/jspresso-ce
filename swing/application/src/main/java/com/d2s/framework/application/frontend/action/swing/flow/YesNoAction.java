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
 * Action to ask a binary question to the user.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoAction extends AbstractMessageAction {

  private IAction yesAction;
  private IAction noAction;

  /**
   * Displays the message using a <code>JOptionPane.YES_NO_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    int selectedOption = JOptionPane.showInternalConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(getSourceComponent(context)), getMessage(),
        getName(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
        getIconFactory().getIcon(getIconImageURL(),
            IIconFactory.LARGE_ICON_SIZE));
    if (selectedOption == JOptionPane.YES_OPTION) {
      setNextAction(yesAction);
    } else {
      setNextAction(noAction);
    }
    super.execute(actionHandler, context);
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
