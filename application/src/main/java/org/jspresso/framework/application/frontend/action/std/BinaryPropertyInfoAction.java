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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;

/**
 * This action displays information about a binary property content. The
 * displayed information mainly consists in the content size. The action must be
 * installed on a property view and supports textual and binary properties.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class BinaryPropertyInfoAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * Displays the size of the binary property.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Integer size = 0;
    String unit = "Ko";
    byte[] content = getBinaryContent(context);
    if (content != null) {
      if (content.length < 1024) {
        size = content.length;
        unit = getTranslationProvider(context).getTranslation("bytes",
            getLocale(context));
      } else if (content.length >= 1024 && content.length < 1024 * 1024) {
        size = content.length / 1024;
        unit = getTranslationProvider(context).getTranslation("kbytes",
            getLocale(context));
      } else if (content.length >= 1024 * 1024
          && content.length < 1024 * 1024 * 1024) {
        size = content.length / (1024 * 1024);
        unit = getTranslationProvider(context).getTranslation("mbytes",
            getLocale(context));
      } else if (content.length >= 1024 * 1024 * 1024) {
        size = content.length / (1024 * 1024 * 1024);
        unit = getTranslationProvider(context).getTranslation("gbytes",
            getLocale(context));
      }
    }
    setActionParameter(
        getTranslationProvider(context).getTranslation("binary.info.message",
            new Object[] {
                size, unit
            }, getLocale(context)), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Retrieve the binary content to display the info on.
   *
   * @param context
   *          the action context.
   * @return The binary content to display the info on or null.
   */
  protected byte[] getBinaryContent(Map<String, Object> context) {
    Object connectorValue = getViewConnector(context).getConnectorValue();
    byte[] content;
    if (connectorValue instanceof String) {
      content = ((String) connectorValue).getBytes();
    } else {
      content = (byte[]) connectorValue;
    }
    return content;
  }
}
