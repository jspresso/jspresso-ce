/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.flow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import org.wings.SOptionPane;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;

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

  private IAction cancelAction;
  private IAction noAction;
  private IAction yesAction;

  /**
   * Displays the message using a <code>SOptionPane.YES_NO_CANCEL_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    SOptionPane.showConfirmDialog(getSourceComponent(context),
        getMessage(context), getI18nName(getTranslationProvider(context),
            getLocale(context)), SOptionPane.YES_NO_CANCEL_OPTION,
        new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            if (SOptionPane.YES_ACTION.equals(e.getActionCommand())) {
              context.put(ActionContextConstants.NEXT_ACTION, yesAction);
            } else if (SOptionPane.NO_ACTION.equals(e.getActionCommand())) {
              context.put(ActionContextConstants.NEXT_ACTION, noAction);
            } else if (SOptionPane.CANCEL_ACTION.equals(e.getActionCommand())) {
              context.put(ActionContextConstants.NEXT_ACTION, cancelAction);
            }
            executeNextAction(actionHandler, context);
          }

        }, null);
    return true;
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
