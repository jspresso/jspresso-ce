/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import javax.swing.JTextPane;

/**
 * JTextPane connector.
 * 
 * @version $LastChangedRevision: 2097 $
 * @author Vincent Vandenschrick
 */
public class JTextPaneConnector extends JTextComponentConnector<JTextPane> {

  /**
   * Constructs a new <code>JTextPane</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param textPane
   *          the connected JTextPane.
   */
  public JTextPaneConnector(String id, JTextPane textPane) {
    super(id, textPane);
  }

}
