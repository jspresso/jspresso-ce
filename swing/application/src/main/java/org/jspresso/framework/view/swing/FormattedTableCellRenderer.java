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
package org.jspresso.framework.view.swing;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.html.HtmlHelper;

/**
 * A table cell renderer based on a formatter.
 *
 * @author Vincent Vandenschrick
 */
public class FormattedTableCellRenderer extends EvenOddTableCellRenderer {

  private static final long     serialVersionUID = 1994281654419286376L;
  private final IFormatter<?, String> formatter;

  /**
   * Constructs a new {@code FormattedTableCellRenderer} instance.
   *
   * @param formatter
   *          the formatter used to format object values.
   */
  public FormattedTableCellRenderer(IFormatter<?, String> formatter) {
    super();
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void setValue(Object value) {
    Object valueToSet;
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      if (formatter != null) {
        valueToSet = ((IFormatter<Object, String>) formatter)
            .format(connectorValue);
      } else {
        valueToSet = connectorValue;
      }
    } else {
      if (formatter != null) {
        valueToSet = ((IFormatter<Object, String>) formatter).format(value);
      } else {
        valueToSet = value;
      }
    }
    if (valueToSet instanceof String && !HtmlHelper.isHtml((String) valueToSet)
        && ((String) valueToSet).indexOf("\n") > 0) {
      valueToSet = HtmlHelper.toHtml(((String) valueToSet).replaceAll("\n",
          "<br>"));
    }
    super.setValue(valueToSet);
  }
}
