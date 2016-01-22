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
package org.jspresso.framework.binding.swing;

import javax.swing.JTextArea;

/**
 * JTextArea connector.
 *
 * @author Vincent Vandenschrick
 */
public class JTextAreaConnector extends JTextComponentConnector<JTextArea> {

  /**
   * Constructs a new {@code JTextAreaConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param textArea
   *          the connected JTextArea.
   */
  public JTextAreaConnector(String id, JTextArea textArea) {
    super(id, textArea);
  }

}
