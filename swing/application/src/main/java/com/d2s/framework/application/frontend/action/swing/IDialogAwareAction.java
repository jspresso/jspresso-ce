/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing;

import javax.swing.JDialog;

/**
 * Actions implementing this interface can get control on wether the dialog in
 * wich they are instanciated should be closed or not when the action finishes.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDialogAwareAction {

  /**
   * Gives a chance to the action to perform something with the dialog upon
   * execution.
   * 
   * @param dialog
   *          the dialog this action was executed in.
   */
  void postActionExecution(JDialog dialog);
}
