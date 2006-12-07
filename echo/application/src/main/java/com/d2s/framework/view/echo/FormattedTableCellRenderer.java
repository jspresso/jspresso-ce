/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.echo;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Table;

import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.format.IFormatter;

/**
 * A table cell renderer based on a formatter.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
   *          the formatter used to format object values.
   */
  public FormattedTableCellRenderer(IFormatter formatter) {
    super();
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellRendererComponent(Table table, Object value,
      int column, int row) {
    Label renderer = (Label) super.getTableCellRendererComponent(table, value,
        column, row);
    if (renderer != null) {
      if (value instanceof IValueConnector) {
        Object connectorValue = ((IValueConnector) value).getConnectorValue();
        if (formatter != null) {
          renderer.setText(formatter.format(connectorValue));
        }
      } else {
        if (formatter != null) {
          renderer.setText(formatter.format(value));
        }
      }
    }
    return renderer;
  }
}
