/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.wings;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.format.IFormatter;
import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.STable;


/**
 * A table cell renderer based on a formatter.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
  public SComponent getTableCellRendererComponent(STable table, Object value,
      boolean isSelected, int row, int column) {
    SLabel renderer = (SLabel) super.getTableCellRendererComponent(table,
        value, isSelected, row, column);
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      if (formatter != null) {
        renderer.setText(formatter.format(connectorValue));
      } else {
        if (connectorValue != null) {
          renderer.setText(connectorValue.toString());
        } else {
          renderer.setText(null);
        }
      }
    } else {
      if (formatter != null) {
        renderer.setText(formatter.format(value));
      } else {
        if (value != null) {
          renderer.setText(value.toString());
        } else {
          renderer.setText(null);
        }
      }
    }
    return renderer;
  }
}
