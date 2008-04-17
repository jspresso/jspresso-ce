/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.ulc.flow;

import org.jspresso.framework.action.IAction;

/**
 * Action to present a message to the user.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class InfoAction extends AbstractFlowAction {

  /**
   * Constructs a new <code>InfoAction</code> instance.
   */
  protected InfoAction() {
    super(OK_OPTION, null, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IAction getNextAction(@SuppressWarnings("unused")
  String selectedOption) {
    return null;
  }

}
