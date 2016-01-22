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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Used as an adapter around table cell renderer to be able to render
 * hyperlinks.
 *
 * @author Vincent Vandenschrick
 */
class HyperlinkTableCellRenderer extends DefaultTableCellRenderer implements
    MouseListener, MouseMotionListener {

  private static final long serialVersionUID = 6431628579836509934L;

  private int               currentRow       = -1;
  private int               currentCol       = -1;

  private final TableCellRenderer delegate;
  private final Action            action;
  private final int               actionColIndex;

  /**
   * Constructs a new {@code HyperlinkTableCellRenderer} instance.
   *
   * @param delegate
   *          the delegate to use.
   * @param action
   *          the action to trigger when the hyperlink is clicked.
   * @param actionColIndex
   *          the column index for which the action should be triggered.
   */
  public HyperlinkTableCellRenderer(TableCellRenderer delegate, Action action,
      int actionColIndex) {
    this.delegate = delegate;
    this.action = action;
    this.actionColIndex = actionColIndex;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {

    Component delegateRendererComponent = delegate
        .getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
            column);
    if (delegateRendererComponent instanceof JLabel) {
      JLabel cellLabel = (JLabel) delegateRendererComponent;
      // if (currentRow == row && currentCol == column) {
      cellLabel.setText("<html><u>" + cellLabel.getText() + "</u></html>");
      // }
    }
    return delegateRendererComponent;
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    JTable table = (JTable) e.getSource();
    Point pt = e.getPoint();
    currentRow = table.rowAtPoint(pt);
    currentCol = table.columnAtPoint(pt);
    if (currentRow < 0 || currentCol < 0) {
      currentRow = -1;
      currentCol = -1;
    }
    table.repaint();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    JTable table = (JTable) e.getSource();
    currentRow = -1;
    currentCol = -1;
    table.repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    JTable table = (JTable) e.getSource();
    Point pt = e.getPoint();
    int col = table.convertColumnIndexToModel(table.columnAtPoint(pt));
    if (col == actionColIndex && action != null && action.isEnabled()) {
      ActionEvent ae = new ActionEvent(e.getSource(),
          ActionEvent.ACTION_PERFORMED, null, e.getWhen(), e.getModifiers());
      action.actionPerformed(ae);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mousePressed(MouseEvent e) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    // NO-OP
  }
}
