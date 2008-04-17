/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.swing.flow;

import java.util.Map;

import javax.swing.JOptionPane;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.IIconFactory;


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
   * Displays the message using a <code>JOptionPane.OK_CANCEL_OPTION</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    int selectedOption = JOptionPane.showConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(getSourceComponent(context)),
        getMessage(context), getI18nName(getTranslationProvider(context),
            getLocale(context)), JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.WARNING_MESSAGE, getIconFactory(context).getIcon(
            getIconImageURL(), IIconFactory.LARGE_ICON_SIZE));
    if (selectedOption == JOptionPane.OK_OPTION) {
      context.put(ActionContextConstants.NEXT_ACTION, okAction);
    } else {
      context.put(ActionContextConstants.NEXT_ACTION, cancelAction);
    }
    return super.execute(actionHandler, context);
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
