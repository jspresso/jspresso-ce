/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.flow;

import com.d2s.framework.view.action.IAction;

/**
 * Action to ask a binary question to the user with a cancel option.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoCancelAction extends AbstractFlowAction {

  private IAction yesAction;
  private IAction noAction;
  private IAction cancelAction;

  /**
   * Constructs a new <code>YesNoAction</code> instance.
   */
  protected YesNoCancelAction() {
    super(YES_OPTION, NO_OPTION, CANCEL_OPTION);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IAction getNextAction(String selectedOption) {
    if (YES_OPTION.equals(selectedOption)) {
      return yesAction;
    } else if (NO_OPTION.equals(selectedOption)) {
      return noAction;
    }
    return cancelAction;
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
