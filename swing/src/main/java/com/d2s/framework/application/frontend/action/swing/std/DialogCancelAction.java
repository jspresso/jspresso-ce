/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.std;

import javax.swing.JDialog;

import com.d2s.framework.application.frontend.action.std.CancelAction;
import com.d2s.framework.application.frontend.action.swing.IDialogAwareAction;

/**
 * A standard cancel action. Since it is a chained action, it can be chained
 * with another action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DialogCancelAction extends CancelAction implements
    IDialogAwareAction {

  /**
   * {@inheritDoc}
   */
  public void postActionExecution(JDialog dialog) {
    dialog.dispose();
  }
}
