/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.echo;

import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.CheckBox;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Table;
import nextapp.echo2.app.table.TableCellRenderer;

import com.d2s.framework.binding.IValueConnector;

/**
 * Renders a table cell using a checkbox.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BooleanTableCellRenderer implements TableCellRenderer {

  private static final long serialVersionUID = -7769205423603311225L;

  /**
   * {@inheritDoc}
   */
  public Component getTableCellRendererComponent(@SuppressWarnings("unused")
  Table table, Object value, @SuppressWarnings("unused")
  int column, @SuppressWarnings("unused")
  int row) {
    CheckBox checkBox = new CheckBox();
    checkBox.setAlignment(new Alignment(Alignment.CENTER, Alignment.CENTER));
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      checkBox
          .setSelected((connectorValue != null && ((Boolean) connectorValue)
              .booleanValue()));
    } else {
      checkBox.setSelected((value != null && ((Boolean) value).booleanValue()));
    }
    return checkBox;
  }
}
