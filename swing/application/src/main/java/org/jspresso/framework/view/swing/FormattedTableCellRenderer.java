/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.swing;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.format.IFormatter;


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
