/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import com.d2s.framework.binding.IValueConnector;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCCheckBox;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.border.ULCAbstractBorder;
import com.ulcjava.base.application.border.ULCEmptyBorder;
import com.ulcjava.base.application.table.ITableCellRenderer;
import com.ulcjava.base.application.util.Color;
import com.ulcjava.base.shared.IDefaults;

/**
 * Renders a table cell using a checkbox.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BooleanTableCellRenderer extends ULCCheckBox implements
    ITableCellRenderer {

  private static final ULCAbstractBorder NO_FOCUS_BORDER  = new ULCEmptyBorder(
                                                              1, 1, 1, 1);
  private static final long              serialVersionUID = 5944792695339009139L;
  private Color                          unselectedBackground;

  private Color                          unselectedForeground;

  /**
   * Constructs a new <code>BooleanTableCellRenderer</code> instance.
   */
  public BooleanTableCellRenderer() {
    super();
    setHorizontalAlignment(IDefaults.CENTER);
    setBorderPainted(true);
  }

  /**
   * {@inheritDoc}
   */
  public IRendererComponent getTableCellRendererComponent(ULCTable table,
      Object value, boolean isSelected, @SuppressWarnings("unused")
      boolean hasFocus, @SuppressWarnings("unused")
      int row) {
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
    if (hasFocus) {
      ULCAbstractBorder border = null;
      if (isSelected) {
        border = ClientContext
            .getBorder("Table.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = ClientContext.getBorder("Table.focusCellHighlightBorder");
      }
      setBorder(border);
      if (!isSelected) {
        Color col;
        col = ClientContext.getColor("Table.focusCellForeground");
        if (col != null) {
          super.setForeground(col);
        }
        col = ClientContext.getColor("Table.focusCellBackground");
        if (col != null) {
          super.setBackground(col);
        }
      }
    } else {
      setBorder(NO_FOCUS_BORDER);
    }
    return this;
  }

  /**
   * Overrides <code>ULCComponent.setBackground</code> to assign the
   * unselected-background color to the specified color.
   * 
   * @param c
   *          set the background color to this value
   */
  @Override
  public void setBackground(Color c) {
    super.setBackground(c);
    unselectedBackground = c;
  }

  /**
   * Overrides <code>ULCComponent.setForeground</code> to assign the
   * unselected-foreground color to the specified color.
   * 
   * @param c
   *          set the foreground color to this value
   */
  @Override
  public void setForeground(Color c) {
    super.setForeground(c);
    unselectedForeground = c;
  }
}
