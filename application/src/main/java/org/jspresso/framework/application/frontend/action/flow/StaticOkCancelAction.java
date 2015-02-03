/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.flow;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;

/**
 * This action pops-up an Ok - Cancel confirmation option. The message, instead
 * of being extracted out of the context, is parametrized statically into the
 * action through its internationalization key.
 * 
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class StaticOkCancelAction<E, F, G> extends OkCancelAction<E, F, G> {

  private String messageCode;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    setActionParameter(translate(messageCode, context), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the i18n key used to translate the message that is to be
   * displayed to the user. When the action executes, the statically configured
   * message is first translated, then placed into the action context to be
   * popped-up.
   * 
   * @param messageCode
   *          the messageCode to set.
   */
  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }

}
