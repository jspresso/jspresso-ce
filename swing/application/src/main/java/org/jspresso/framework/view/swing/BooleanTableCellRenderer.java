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

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.swing.CollectionConnectorTableModel;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * Renders a table cell using a checkbox.
 *
 * @author Vincent Vandenschrick
 */
public class BooleanTableCellRenderer extends JCheckBox implements
    TableCellRenderer, DynamicStyleRenderer {

  private static final long   serialVersionUID = 5944792695339009139L;
  private static final Border NO_FOCUS_BORDER  = new EmptyBorder(1, 1, 1, 1);

  private Color               unselectedForeground;
  private Color               unselectedBackground;

  private String              toolTipProperty;
  private String              backgroundProperty;
  private String              foregroundProperty;
  private String              fontProperty;

  /**
   * Constructs a new {@code BooleanTableCellRenderer} instance.
   */
  public BooleanTableCellRenderer() {
    super();
    setHorizontalAlignment(SwingConstants.CENTER);
    setBorderPainted(true);
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void firePropertyChange(String propertyName, boolean oldValue,
      boolean newValue) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      super.setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      if (unselectedForeground != null) {
        super.setForeground(unselectedForeground);
      } else {
        super.setForeground(table.getForeground());
      }
      Color actualBackground = table.getBackground();
      if (unselectedBackground != null) {
        actualBackground = unselectedBackground;
      }
      super.setBackground(SwingUtil.computeEvenOddBackground(actualBackground, false, row));
    }
    setValue(value);
    if (hasFocus) {
      Border border = null;
      if (isSelected) {
        border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("Table.focusCellHighlightBorder");
      }
      setBorder(border);
      if (!isSelected && table.isCellEditable(row, column)) {
        Color col;
        col = UIManager.getColor("Table.focusCellForeground");
        if (col != null) {
          super.setForeground(col);
        }
        col = UIManager.getColor("Table.focusCellBackground");
        if (col != null) {
          super.setBackground(col);
        }
      }
    } else {
      setBorder(NO_FOCUS_BORDER);
    }
    if (row >= 0) {
      CollectionConnectorTableModel tm = getActualTableModel(table);
      if (table.convertColumnIndexToModel(column) == 0) {
        setToolTipText(tm.getRowToolTip(row));
      } else if (getToolTipProperty() != null) {
        setToolTipText(tm.getCellToolTip(row, getToolTipProperty()));
      }
      if (getBackgroundProperty() != null) {
        setBackground(DefaultSwingViewFactory.createColor(tm.getCellBackground(
            row, getBackgroundProperty())));
      } else if (tm.getRowBackground(row) != null) {
        setBackground(DefaultSwingViewFactory.createColor(tm
            .getRowBackground(row)));
      }
      if (getForegroundProperty() != null) {
        setForeground(DefaultSwingViewFactory.createColor(tm.getCellForeground(
            row, getForegroundProperty())));
      } else if (tm.getRowBackground(row) != null) {
        setForeground(DefaultSwingViewFactory.createColor(tm
            .getRowForeground(row)));
      }
    }
    return this;
  }

  /**
   * Sets the renderer value.
   *
   * @param value
   *          the model value.
   */
  protected void setValue(Object value) {
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      setSelected((connectorValue != null && (Boolean) connectorValue));
    } else {
      setSelected((value != null && (Boolean) value));
    }
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void invalidate() {
    // NO-OP
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isOpaque() {
    Color back = getBackground();
    Component p = getParent();
    if (p != null) {
      p = p.getParent();
    }
    // p should now be the JTable.
    boolean colorMatch = (back != null) && (p != null)
        && back.equals(p.getBackground()) && p.isOpaque();
    return !colorMatch && super.isOpaque();
  }

  /*
   * The following methods are overridden for performance reasons (see
   * DefaultTableCellRenderer comments for a reference).
   */

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void repaint() {
    // NO-OP
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void repaint(long tm, int x, int y, int width, int height) {
    // NO-OP
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void repaint(Rectangle r) {
    // NO-OP
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void revalidate() {
    // NO-OP
  }

  /**
   * Notification from the {@code UIManager} that the look and feel [L&F]
   * has changed. Replaces the current UI object with the latest version from
   * the {@code UIManager}.
   */
  @Override
  public void updateUI() {
    super.updateUI();
    setForeground(null);
    setBackground(null);
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void validate() {
    // NO-OP
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    // NO-OP
  }

  private CollectionConnectorTableModel getActualTableModel(JTable table) {
    TableModel tm = table.getModel();
    if (tm instanceof AbstractTableSorter) {
      tm = ((AbstractTableSorter) tm).getTableModel();
    }
    return (CollectionConnectorTableModel) tm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForeground(Color fg) {
    super.setForeground(fg);
    unselectedForeground = fg;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBackground(Color bg) {
    super.setBackground(bg);
    unselectedBackground = bg;
  }

  /**
   * Gets the toolTipProperty.
   *
   * @return the toolTipProperty.
   */
  protected String getToolTipProperty() {
    return toolTipProperty;
  }

  /**
   * Sets the toolTipProperty.
   *
   * @param toolTipProperty
   *          the toolTipProperty to set.
   */
  @Override
  public void setToolTipProperty(String toolTipProperty) {
    this.toolTipProperty = toolTipProperty;
  }

  /**
   * Gets the backgroundProperty.
   *
   * @return the backgroundProperty.
   */
  protected String getBackgroundProperty() {
    return backgroundProperty;
  }

  /**
   * Sets the backgroundProperty.
   *
   * @param backgroundProperty
   *          the backgroundProperty to set.
   */
  @Override
  public void setBackgroundProperty(String backgroundProperty) {
    this.backgroundProperty = backgroundProperty;
  }

  /**
   * Gets the foregroundProperty.
   *
   * @return the foregroundProperty.
   */
  protected String getForegroundProperty() {
    return foregroundProperty;
  }

  /**
   * Sets the foregroundProperty.
   *
   * @param foregroundProperty
   *          the foregroundProperty to set.
   */
  @Override
  public void setForegroundProperty(String foregroundProperty) {
    this.foregroundProperty = foregroundProperty;
  }

  /**
   * Gets the fontProperty.
   *
   * @return the fontProperty.
   */
  protected String getFontProperty() {
    return fontProperty;
  }

  /**
   * Sets the fontProperty.
   *
   * @param fontProperty
   *          the fontProperty to set.
   */
  @Override
  public void setFontProperty(String fontProperty) {
    this.fontProperty = fontProperty;
  }
}
