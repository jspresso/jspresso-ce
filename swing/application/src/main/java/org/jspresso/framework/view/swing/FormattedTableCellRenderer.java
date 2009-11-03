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
package org.jspresso.framework.view.swing;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.format.IFormatter;


/**
 * A table cell renderer based on a formatter.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FormattedTableCellRenderer extends EvenOddTableCellRenderer {

  private static final long serialVersionUID = 1994281654419286376L;
  private IFormatter        formatter;

  /**
   * Constructs a new <code>FormattedTableCellRenderer</code> instance.
   * 
   * @param formatter
   *            the formatter used to format object values.
   */
  public FormattedTableCellRenderer(IFormatter formatter) {
    super();
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setValue(Object value) {
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      if (formatter != null) {
        super.setValue(formatter.format(connectorValue));
      } else {
        super.setValue(connectorValue);
      }
    } else {
      if (formatter != null) {
        super.setValue(formatter.format(value));
      } else {
        super.setValue(value);
      }
    }
  }
}
