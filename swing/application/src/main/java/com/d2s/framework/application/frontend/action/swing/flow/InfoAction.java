/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.flow;

import java.util.Map;

import javax.swing.JOptionPane;

import org.jspresso.framework.view.IIconFactory;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * Action to present a message to the user.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    JOptionPane.showMessageDialog(SwingUtil
        .getWindowOrInternalFrame(getSourceComponent(context)),
        getMessage(context), getI18nName(getTranslationProvider(context),
            getLocale(context)), JOptionPane.INFORMATION_MESSAGE,
        getIconFactory(context).getIcon(getIconImageURL(),
            IIconFactory.LARGE_ICON_SIZE));
    return super.execute(actionHandler, context);
  }
}
