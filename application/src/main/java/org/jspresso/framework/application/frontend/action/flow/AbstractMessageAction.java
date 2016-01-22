/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.frontend.action.flow;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.util.html.HtmlHelper;

/**
 * This is the base class for all UI message based communication actions. This
 * type of action generally opens a modal dialog to display an informational
 * message, ask a question, and so on. It takes the message from the action
 * context parameter and supports basic HTML formatting. In order for the
 * message to be interpreted as HTML, it must be surrounded by
 * {@code &lt;HTML&gt;} tags.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class AbstractMessageAction<E, F, G> extends
    FrontendAction<E, F, G> {

  /**
   * Gets the message.
   *
   * @param context
   *          the actionContext.
   * @return the message.
   */
  protected String getMessage(Map<String, Object> context) {
    String msg = getActionParameter(context);
    if (msg == null || HtmlHelper.isHtml(msg)) {
      return msg;
    }
    return HtmlHelper
        .toHtml(HtmlHelper.emphasis(HtmlHelper.escapeForHTML(msg)));
  }

  /**
   * Allows subclasses to retrieve message arguments from the action context.
   *
   * @param context
   *          the action context.
   * @return the message arguments to be passed to the translation provider for
   *         being used with message format.
   */
  @SuppressWarnings("UnusedParameters")
  protected Object[] getMessageArgs(Map<String, Object> context) {
    return null;
  }

  /**
   * Utility method used to translate a code using the context translation
   * provider. If {@link #getMessageArgs(Map)} returns a not-null array or
   * arguments, they are passed to the translation provider to be used as message
   * format arguments.
   *
   * @param messageCode
   *          the message code to translate.
   * @param context
   *          the action context.
   * @return the translated message.
   */
  protected String translate(String messageCode, Map<String, Object> context) {
    Object[] messageArgs = getMessageArgs(context);
    String translation;
    if (messageArgs == null) {
      translation = getTranslationProvider(context).getTranslation(messageCode,
          getLocale(context));
    } else {
      translation = getTranslationProvider(context).getTranslation(messageCode,
          messageArgs, getLocale(context));
    }
    return translation;
  }

}
