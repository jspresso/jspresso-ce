/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.flow;

import com.d2s.framework.action.IAction;

/**
 * Action to ask a user validation.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OkCancelAction extends AbstractFlowAction {

  private IAction cancelAction;
  private IAction okAction;

  /**
   * Constructs a new <code>OkCancelAction</code> instance.
   */
  protected OkCancelAction() {
    super(OK_OPTION, CANCEL_OPTION, null);
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IAction getNextAction(String selectedOption) {
    if (OK_OPTION.equals(selectedOption)) {
      return okAction;
    }
    return cancelAction;
  }

}
