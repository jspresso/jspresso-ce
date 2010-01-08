/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.flow;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;

/**
 * Info action with a static i18nalized message.
 * 
 * @version $LastChangedRevision: 2097 $
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class StaticInfoAction<E, F, G> extends InfoAction<E, F, G> {

  private String messageCode;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    setActionParameter(getTranslationProvider(context).getTranslation(
        messageCode, getLocale(context)), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the messageCode.
   * 
   * @param messageCode
   *          the messageCode to set.
   */
  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }

}
