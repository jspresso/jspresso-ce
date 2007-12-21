/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.flow;

import com.d2s.framework.action.IAction;

/**
 * Action to ask a binary question to the user.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoAction extends AbstractFlowAction {

  private IAction noAction;
  private IAction yesAction;

  /**
   * Constructs a new <code>YesNoAction</code> instance.
   */
  protected YesNoAction() {
    super(YES_OPTION, NO_OPTION, null);
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IAction getNextAction(String selectedOption) {
    if (YES_OPTION.equals(selectedOption)) {
      return yesAction;
    }
    return noAction;
  }

}
