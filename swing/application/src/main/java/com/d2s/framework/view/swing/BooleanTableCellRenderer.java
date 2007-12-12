/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing;

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

import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * Renders a table cell using a checkbox.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BooleanTableCellRenderer extends JCheckBox implements
    TableCellRenderer {

  private static final Border NO_FOCUS_BORDER  = new EmptyBorder(1, 1, 1, 1);
  private static final long   serialVersionUID = 5944792695339009139L;
  private Color               unselectedBackground;

  private Color               unselectedForeground;

  /**
   * Constructs a new <code>BooleanTableCellRenderer</code> instance.
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
  public void firePropertyChange(@SuppressWarnings("unused")
  String propertyName, @SuppressWarnings("unused")
  boolean oldValue, @SuppressWarnings("unused")
  boolean newValue) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, @SuppressWarnings("unused")
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
    SwingUtil.alternateEvenOddBackground(this, table, isSelected, row);
    return this;
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
  public void repaint(@SuppressWarnings("unused")
  long tm, @SuppressWarnings("unused")
  int x, @SuppressWarnings("unused")
  int y, @SuppressWarnings("unused")
  int width, @SuppressWarnings("unused")
  int height) {
    // NO-OP
  }

  /**
   * Overridden for performance reasons.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void repaint(@SuppressWarnings("unused")
  Rectangle r) {
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
  protected void firePropertyChange(@SuppressWarnings("unused")
  String propertyName, @SuppressWarnings("unused")
  Object oldValue, @SuppressWarnings("unused")
  Object newValue) {
    // NO-OP
  }

}
