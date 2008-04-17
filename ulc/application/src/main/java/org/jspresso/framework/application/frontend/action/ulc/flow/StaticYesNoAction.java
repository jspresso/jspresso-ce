/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.ulc.flow;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;


/**
 * Action with a static i18nalized message.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class StaticYesNoAction extends YesNoAction {

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
