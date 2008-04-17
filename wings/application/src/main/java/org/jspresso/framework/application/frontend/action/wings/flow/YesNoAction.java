/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wings.flow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.wings.SOptionPane;


/**
 * Action to ask a binary question to the user.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoAction extends AbstractMessageAction {

  private IAction noAction;
  private IAction yesAction;

  /**
   * Displays the message using a <code>SOptionPane.YES_NO_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    SOptionPane.showYesNoDialog(getSourceComponent(context),
        getMessage(context), getI18nName(getTranslationProvider(context),
            getLocale(context)), new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            if (SOptionPane.YES_ACTION.equals(e.getActionCommand())) {
              context.put(ActionContextConstants.NEXT_ACTION, yesAction);
            } else if (SOptionPane.NO_ACTION.equals(e.getActionCommand())) {
              context.put(ActionContextConstants.NEXT_ACTION, noAction);
            }
            executeNextAction(actionHandler, context);
          }

        });
    return true;
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
