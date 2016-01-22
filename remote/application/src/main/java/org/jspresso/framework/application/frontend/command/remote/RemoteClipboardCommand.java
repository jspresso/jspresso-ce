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
package org.jspresso.framework.application.frontend.command.remote;

/**
 * Sends some text content to assign to the system clipboard.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteClipboardCommand extends RemoteCommand {

  private static final long serialVersionUID = -8830398246333440149L;

  private String            plainContent;
  private String            htmlContent;

  /**
   * Gets the plainContent.
   *
   * @return the plainContent.
   */
  public String getPlainContent() {
    return plainContent;
  }

  /**
   * Sets the plainContent.
   *
   * @param plainContent
   *          the plainContent to set.
   */
  public void setPlainContent(String plainContent) {
    this.plainContent = plainContent;
  }

  /**
   * Gets the htmlContent.
   *
   * @return the htmlContent.
   */
  public String getHtmlContent() {
    return htmlContent;
  }

  /**
   * Sets the htmlContent.
   *
   * @param htmlContent
   *          the htmlContent to set.
   */
  public void setHtmlContent(String htmlContent) {
    this.htmlContent = htmlContent;
  }

}
