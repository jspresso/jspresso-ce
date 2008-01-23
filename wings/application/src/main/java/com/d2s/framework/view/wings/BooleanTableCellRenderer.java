/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.wings;

import java.awt.Color;

import org.wings.SCheckBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.STable;
import org.wings.table.STableCellRenderer;

import com.d2s.framework.binding.IValueConnector;

/**
 * Renders a table cell using a checkbox.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BooleanTableCellRenderer extends SCheckBox implements
    STableCellRenderer {

  private static final long serialVersionUID = 5944792695339009139L;
  private Color             unselectedBackground;
  private Color             unselectedForeground;

  /**
   * Constructs a new <code>BooleanTableCellRenderer</code> instance.
   */
  public BooleanTableCellRenderer() {
    super();
    setHorizontalAlignment(SConstants.CENTER);
  }

  /**
   * {@inheritDoc}
   */
  public SComponent getTableCellRendererComponent(STable table, Object value,
      boolean isSelected, @SuppressWarnings("unused")
      int row, @SuppressWarnings("unused")
      int column) {
    if (isSelected) {
      super.setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      if (unselectedForeground != null) {
        super.setForeground(unselectedForeground);
      } else {
        super.setForeground(table.getForeground());
      }
      if (unselectedBackground != null) {
        super.setBackground(unselectedBackground);
      } else {
        super.setBackground(table.getBackground());
      }
    }
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      setSelected((connectorValue != null && ((Boolean) connectorValue)
          .booleanValue()));
    } else {
      setSelected((value != null && ((Boolean) value).booleanValue()));
    }
    return this;
  }

  /**
   * Overrides <code>JComponent.setBackground</code> to assign the
   * unselected-background color to the specified color.
   * 
   * @param c
   *            set the background color to this value
   */
  @Override
  public void setBackground(Color c) {
    super.setBackground(c);
    unselectedBackground = c;
  }

  /**
   * Overrides <code>JComponent.setForeground</code> to assign the
   * unselected-foreground color to the specified color.
   * 
   * @param c
   *            set the foreground color to this value
   */
  @Override
  public void setForeground(Color c) {
    super.setForeground(c);
    unselectedForeground = c;
  }

  /**
   * Notification from the <code>UIManager</code> that the look and feel [L&F]
   * has changed. Replaces the current UI object with the latest version from
   * the <code>UIManager</code>.
   */
  @Override
  public void updateCG() {
    super.updateCG();
    setForeground(null);
    setBackground(null);
  }
}
