/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wings.flow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JOptionPane;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.wings.SOptionPane;


/**
 * Action to ask a user validation.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OkCancelAction extends AbstractMessageAction {

  private IAction cancelAction;
  private IAction okAction;

  /**
   * Displays the message using a <code>SOptionPane.OK_CANCEL_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    SOptionPane.showConfirmDialog(getSourceComponent(context),
        getMessage(context), getI18nName(getTranslationProvider(context),
            getLocale(context)), JOptionPane.WARNING_MESSAGE,
        new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            if (SOptionPane.OK_ACTION.equals(e.getActionCommand())) {
              context.put(ActionContextConstants.NEXT_ACTION, okAction);
            } else if (SOptionPane.CANCEL_ACTION.equals(e.getActionCommand())) {
              context.put(ActionContextConstants.NEXT_ACTION, cancelAction);
            }
            executeNextAction(actionHandler, context);
          }

        });
    return true;
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *            the cancelAction to set.
   */
  public void setCancelAction(IAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *            the okAction to set.
   */
  public void setOkAction(IAction okAction) {
    this.okAction = okAction;
  }

}
