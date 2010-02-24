/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * Base class for all message swing actions. It just keeps a reference on the
 * message to be displayed.
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
    return HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper
        .escapeForHTML((String) getActionParameter(context))));
  }
}
