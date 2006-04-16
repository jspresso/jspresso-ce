/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.flow;

import java.util.Map;

import javax.swing.JOptionPane;

import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.action.IActionHandler;

/**
 * Action to present a message to the user.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class InfoAction extends AbstractMessageAction {

  /**
   * Displays the message using a <code>JOptionPane.INFORMATION_MESSAGE</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    JOptionPane.showInternalMessageDialog(SwingUtil
        .getWindowOrInternalFrame(getSourceComponent(context)), getMessage(),
        getName(), JOptionPane.INFORMATION_MESSAGE, getIconFactory(context).getIcon(
            getIconImageURL(), IIconFactory.LARGE_ICON_SIZE));
    super.execute(actionHandler, context);
  }
}
