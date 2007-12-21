/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.flow;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;

/**
 * Action with a static i18nalized message.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class StaticYesNoCancelAction extends YesNoAction {

  private String messageCode;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    context.put(ActionContextConstants.ACTION_PARAM, getTranslationProvider(
        context).getTranslation(messageCode, getLocale(context)));
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the messageCode.
   * 
   * @param messageCode
   *            the messageCode to set.
   */
  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }

}
