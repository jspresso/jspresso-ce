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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.jspresso.framework.binding.swing.CollectionConnectorTableModel;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * A default table cell renderer rendering even and odd rows background slightly
 * differently.
 *
 * @author Vincent Vandenschrick
 */
public class EvenOddTableCellRenderer extends DefaultTableCellRenderer
    implements DynamicStyleRenderer {

  private static final long serialVersionUID = -635326662239616998L;

  private Color             backgroundBase;
  private Font              customFont;
  private Font              dynamicFont;
  private String            toolTipProperty;
  private String            backgroundProperty;
  private String            foregroundProperty;
  private String            fontProperty;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (row >= 0) {
      Color actualBackground = table.getBackground();
      if (backgroundBase != null) {
        actualBackground = backgroundBase;
      }
      setVerticalAlignment(TOP);
      super.setBackground(SwingUtil.computeEvenOddBackground(actualBackground,
          isSelected, row));
      FontMetrics fm = getFontMetrics(getFont());
      CollectionConnectorTableModel tm = getActualTableModel(table);
      if (table.convertColumnIndexToModel(column) == 0) {
        setToolTipText(tm.getRowToolTip(row));
      } else if (getToolTipProperty() != null) {
        setToolTipText(tm.getCellToolTip(row, getToolTipProperty()));
      } else {
        if (HtmlHelper.isHtml(getText())
            || fm.stringWidth(getText()) > table.getColumnModel()
                .getColumn(column).getWidth()) {
          setToolTipText(getText());
        } else {
          setToolTipText(null);
        }
      }
      if (getBackgroundProperty() != null) {
        setBackground(DefaultSwingViewFactory.createColor(tm.getCellBackground(
            row, getBackgroundProperty())));
      } else {
        setBackground(DefaultSwingViewFactory.createColor(tm
            .getRowBackground(row)));
      }
      if (getForegroundProperty() != null) {
        setForeground(DefaultSwingViewFactory.createColor(tm.getCellForeground(
            row, getForegroundProperty())));
      } else {
        setForeground(DefaultSwingViewFactory.createColor(tm
            .getRowForeground(row)));
      }
      if (getFontProperty() != null) {
        setDynamicFont(DefaultSwingViewFactory.createFont(
            tm.getCellFont(row, getFontProperty()), getFont()));
      } else {
        if (tm.getRowFont(row) != null) {
          setDynamicFont(DefaultSwingViewFactory.createFont(tm.getRowFont(row),
              getFont()));
        } else {
          setDynamicFont(null);
        }
      }
    }
    Component c = super.getTableCellRendererComponent(table, value, isSelected,
        hasFocus, row, column);
    Font font = getDynamicFont();
    if (font == null) {
      font = getCustomFont();
    }
    if (font != null) {
      // to override default font mgt of JTable
      c.setFont(font);
    }
    Dimension ps = c.getPreferredSize();
    if (row >= 0 && ps.height > table.getRowHeight(row)) {
      table.setRowHeight(row, ps.height);
    }
    return c;
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
  public void setBackground(Color c) {
    backgroundBase = c;
    super.setBackground(c);
  }

  /**
   * Gets the customFont.
   *
   * @return the customFont.
   */
  protected Font getCustomFont() {
    return customFont;
  }

  /**
   * Sets the customFont.
   *
   * @param customFont
   *          the customFont to set.
   */
  public void setCustomFont(Font customFont) {
    this.customFont = customFont;
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

  /**
   * Gets the dynamicFont.
   *
   * @return the dynamicFont.
   */
  protected Font getDynamicFont() {
    return dynamicFont;
  }

  /**
   * Sets the dynamicFont.
   *
   * @param dynamicFont
   *          the dynamicFont to set.
   */
  public void setDynamicFont(Font dynamicFont) {
    this.dynamicFont = dynamicFont;
  }
}
