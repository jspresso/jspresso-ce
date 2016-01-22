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

/**
 * An interface to handle various string flavors of a component collection in
 * order to transfer them to the clipboard.
 *
 * @author Vincent Vandenschrick
 */
public interface IClipboardTransferHandler {

  /**
   * Creates a plain text representation of the component collection.
   *
   * @param transferredComponents
   *          the component collection to transform.
   * @return the plain text representation.
   */
  String toPlainText(List<?> transferredComponents);

  /**
   * Creates an HTML representation of the component collection.
   *
   * @param transferredComponents
   *          the component collection to transform.
   * @return the HTML representation.
   */
  String toHtml(List<?> transferredComponents);
}
