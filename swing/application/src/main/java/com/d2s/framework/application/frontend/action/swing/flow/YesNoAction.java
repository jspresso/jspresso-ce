/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.flow;

import java.util.Map;

import javax.swing.JOptionPane;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.view.IIconFactory;

/**
 * Action to ask a binary question to the user.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoAction extends AbstractMessageAction {

  private IAction noAction;
  private IAction yesAction;

  /**
   * Displays the message using a <code>JOptionPane.YES_NO_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    int selectedOption = JOptionPane.showConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(getSourceComponent(context)),
        getMessage(context), getI18nName(getTranslationProvider(context),
            getLocale(context)), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE, getIconFactory(context).getIcon(
            getIconImageURL(), IIconFactory.LARGE_ICON_SIZE));
    if (selectedOption == JOptionPane.YES_OPTION) {
      context.put(ActionContextConstants.NEXT_ACTION, yesAction);
    } else {
      context.put(ActionContextConstants.NEXT_ACTION, noAction);
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the noAction.
   * 
   * @param noAction
   *            the noAction to set.
   */
  public void setNoAction(IAction noAction) {
    this.noAction = noAction;
  }

  /**
   * Sets the yesAction.
   * 
   * @param yesAction
   *            the yesAction to set.
   */
  public void setYesAction(IAction yesAction) {
    this.yesAction = yesAction;
  }

}
