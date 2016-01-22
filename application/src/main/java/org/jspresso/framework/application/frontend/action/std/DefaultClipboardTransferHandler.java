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

import java.util.List;

import org.jspresso.framework.util.html.HtmlHelper;

/**
 * Default implementation of clipboard transfer handler.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultClipboardTransferHandler implements
    IClipboardTransferHandler {

  /**
   * {@inheritDoc}
   */
  @Override
  public String toPlainText(List<?> transferedComponents) {
    if (transferedComponents != null && transferedComponents.size() > 0) {
      StringBuilder buf = new StringBuilder();
      for (Object component : transferedComponents) {
        if (component != null) {
          buf.append(component.toString()).append("\n");
        }
      }
      return buf.toString();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toHtml(List<?> transferedComponents) {
    if (transferedComponents != null && transferedComponents.size() > 0) {
      StringBuilder buf = new StringBuilder();
      for (Object component : transferedComponents) {
        if (component != null) {
          buf.append(component.toString()).append("<br>");
        }
      }
      return HtmlHelper.toHtml(buf.toString());
    }
    return null;
  }

}
