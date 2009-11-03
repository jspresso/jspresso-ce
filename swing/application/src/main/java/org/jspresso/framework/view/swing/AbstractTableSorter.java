/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jspresso.framework.util.gui.IIndexMapper;

/**
 * AbstractTableSorter is the base class for TableModels sortable decorators;
 * adding sorting functionality to a supplied TableModel.
 * <p/>
 * When the tableHeader property is set, either by using the setTableHeader()
 * method or the two argument constructor, the table header may be used as a
 * complete UI for TableSorter. The default renderer of the tableHeader is
 * decorated with a renderer that indicates the sorting status of each column.
 * In addition, a mouse listener is installed with the following behavior:
 * <ul>
 * <li>Mouse-click: Clears the sorting status of all other columns and advances
 * the sorting status of that column through three values: {NOT_SORTED,
 * ASCENDING, DESCENDING} (then back to NOT_SORTED again).
 * <li>SHIFT-mouse-click: Clears the sorting status of all other columns and
 * cycles the sorting status of the column through the same three values, in the
 * opposite order: {NOT_SORTED, DESCENDING, ASCENDING}.
 * <li>CONTROL-mouse-click and CONTROL-SHIFT-mouse-click: as above except that
 * the changes to the column do not cancel the statuses of columns that are
 * already sorting - giving a way to initiate a compound sort.
 * </ul>
 * <p/>
 * This is an split from the TableSorter class.
 * 
 * @author Philip Milne
 * @author Brendon McLean
 * @author Dan van Enckevort
 * @author Parwinder Sekhon
 * @author Vincent Vandenschrick
 * @version 2.0 02/27/04
 */

public abstract class AbstractTableSorter extends AbstractTableModel implements
    IIndexMapper {

  private static final long      serialVersionUID     = 7759053241235858224L;

  /**
   * <code>ASCENDING</code>.
   */
  public static final int        ASCENDING            = 1;

  /**
   * <code>DESCENDING</code>.
   */
  public static final int        DESCENDING           = -1;

  /**
   * <code>NOT_SORTED</code>.
   */
  public static final int        NOT_SORTED           = 0;

  private static final Directive NOT_SORTED_DIRECTIVE = new Directive(-1,
                                                          NOT_SORTED);

  private Icon                   downIcon;

  private MouseListener          mouseListener;
  private List<Directive>        sortingColumns;
  private JTableHeader           tableHeader;
  private TableModel             tableModel;
  private TableModelListener     tableModelListener;
  private Icon                   upIcon;

  /**
   * Constructs a new <code>AbstractTableSorter</code> instance.
   * 
   * @param tableModel
   *          tableModel.
   * @param tableHeader
   *          tableHeader.
   */
  public AbstractTableSorter(TableModel tableModel, JTableHeader tableHeader) {
    this.sortingColumns = new ArrayList<Directive>();
    this.mouseListener = new MouseHandler();
    this.tableModelListener = createTableModelHandler();
    setTableHeader(tableHeader);
    setTableModel(tableModel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getColumnClass(int column) {
    return tableModel.getColumnClass(column);
  }

  /**
   * {@inheritDoc}
   */
  public int getColumnCount() {
    if (tableModel == null) {
      return 0;
    }
    return tableModel.getColumnCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getColumnName(int column) {
    return tableModel.getColumnName(column);
  }

  /**
   * {@inheritDoc}
   */
  public int getRowCount() {
    if (tableModel == null) {
      return 0;
    }
    return tableModel.getRowCount();
  }

  /**
   * Gets sorting status.
   * 
   * @param column
   *          column.
   * @return sorting status.
   */
  public int getSortingStatus(int column) {
    return getDirective(column).direction;
  }

  /**
   * Gets tableHeader.
   * 
   * @return tableHeader.
   */
  public JTableHeader getTableHeader() {
    return tableHeader;
  }

  /**
   * Gets tableModel.
   * 
   * @return tableModel.
   */
  public TableModel getTableModel() {
    return tableModel;
  }

  /**
   * {@inheritDoc}
   */
  public Object getValueAt(int row, int column) {
    return tableModel.getValueAt(modelIndex(row), column);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(int row, int column) {
    return tableModel.isCellEditable(modelIndex(row), column);
  }

  /**
   * is sorting ?
   * 
   * @return true if sorting
   */
  public boolean isSorting() {
    return sortingColumns.size() != 0;
  }

  /**
   * Sets the downIcon.
   * 
   * @param downIcon
   *          the downIcon to set.
   */
  public void setDownIcon(Icon downIcon) {
    this.downIcon = downIcon;
  }

  /**
   * Sets column sorting status.
   * 
   * @param column
   *          column.
   * @param status
   *          status.
   */
  public void setSortingStatus(int column, int status) {
    Directive directive = getDirective(column);
    if (directive != NOT_SORTED_DIRECTIVE) {
      sortingColumns.remove(directive);
    }
    if (status != NOT_SORTED) {
      sortingColumns.add(new Directive(column, status));
    }
    sortingStatusChanged();
  }

  /**
   * Sets tableHeader.
   * 
   * @param tableHeader
   *          tableHeader.
   */
  public void setTableHeader(JTableHeader tableHeader) {
    if (this.tableHeader != null) {
      this.tableHeader.removeMouseListener(mouseListener);
      TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
      if (defaultRenderer instanceof SortableHeaderRenderer) {
        this.tableHeader
            .setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
      }
    }
    this.tableHeader = tableHeader;
    if (this.tableHeader != null) {
      this.tableHeader.addMouseListener(mouseListener);
      this.tableHeader.setDefaultRenderer(new SortableHeaderRenderer(
          this.tableHeader.getDefaultRenderer()));
    }
  }

  /**
   * Sets tableModel.
   * 
   * @param tableModel
   *          tableModel.
   */
  public void setTableModel(TableModel tableModel) {
    if (this.tableModel != null && tableModelListener != null) {
      this.tableModel.removeTableModelListener(tableModelListener);
    }

    this.tableModel = tableModel;
    if (this.tableModel != null && tableModelListener != null) {
      this.tableModel.addTableModelListener(tableModelListener);
    }

    clearSortingState();
    fireTableStructureChanged();
  }

  /**
   * Sets the upIcon.
   * 
   * @param upIcon
   *          the upIcon to set.
   */
  public void setUpIcon(Icon upIcon) {
    this.upIcon = upIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object aValue, int row, int column) {
    tableModel.setValueAt(aValue, modelIndex(row), column);
  }

  /**
   * Gets HeaderRendererIcon.
   * 
   * @param column
   *          column.
   * @return HeaderRendererIcon
   */
  protected Icon getHeaderRendererIcon(int column) {
    Directive directive = getDirective(column);
    if (directive == NOT_SORTED_DIRECTIVE) {
      return null;
    }
    if (directive.direction == DESCENDING) {
      if (downIcon != null) {
        return downIcon;
      }
    } else {
      if (upIcon != null) {
        return upIcon;
      }
    }
    return null;
  }

  /**
   * Cancels sorting.
   */
  protected void cancelSorting() {
    sortingColumns.clear();
    // sortingStatusChanged();
  }

  /**
   * Performs any operation needed to clear some internal state when sorting has
   * changed.
   */
  protected void clearSortingState() {
    // NO-OP by default.
  }

  private Directive getDirective(int column) {
    for (int i = 0; i < sortingColumns.size(); i++) {
      Directive directive = sortingColumns.get(i);
      if (directive.column == column) {
        return directive;
      }
    }
    return NOT_SORTED_DIRECTIVE;
  }

  /**
   * Creates a table model listener to react to the underlying table model
   * change events.
   * 
   * @return the table model listener.
   */
  protected abstract TableModelListener createTableModelHandler();

  /**
   * This method is triggered whenever the user clicks changed the sorting in
   * any way.
   */
  protected abstract void sortingStatusChanged();

  /**
   * Internal class to represent a sorted column state.
   */
  protected static final class Directive {

    private int column;
    private int direction;

    /**
     * Constructs a new <code>Directive</code> instance.
     * 
     * @param column
     * @param direction
     */
    private Directive(int column, int direction) {
      this.column = column;
      this.direction = direction;
    }

    /**
     * Gets the column.
     * 
     * @return the column.
     */
    protected int getColumn() {
      return column;
    }

    /**
     * Gets the direction.
     * 
     * @return the direction.
     */
    protected int getDirection() {
      return direction;
    }
  }

  private class MouseHandler extends MouseAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      JTableHeader h = (JTableHeader) e.getSource();
      TableColumnModel columnModel = h.getColumnModel();
      int viewColumn = columnModel.getColumnIndexAtX(e.getX());
      int column = columnModel.getColumn(viewColumn).getModelIndex();
      if (column != -1) {
        int status = getSortingStatus(column);
        if (!e.isControlDown()) {
          cancelSorting();
        }
        // Cycle the sorting states through {NOT_SORTED, ASCENDING, DESCENDING}
        // or
        // {NOT_SORTED, DESCENDING, ASCENDING} depending on whether shift is
        // pressed.
        if (e.isShiftDown()) {
          status -= 1;
        } else {
          status += 1;
        }
        status = (status + 4) % 3 - 1; // signed mod, returning {-1, 0, 1}
        setSortingStatus(column, status);
      }
    }
  }

  /**
   * Retrieves the column view index from the model index.
   * 
   * @param modelColumnIndex
   *          the model column index.
   * @return the column index.
   */
  protected int convertColumnIndexToView(int modelColumnIndex) {
    if (modelColumnIndex < 0) {
      return modelColumnIndex;
    }
    TableColumnModel cm = getTableHeader().getColumnModel();
    for (int column = 0; column < getColumnCount(); column++) {
      if (cm.getColumn(column).getModelIndex() == modelColumnIndex) {
        return column;
      }
    }
    return -1;
  }

  private class SortableHeaderRenderer implements TableCellRenderer {

    private TableCellRenderer tableCellRenderer;

    /**
     * Constructs a new <code>SortableHeaderRenderer</code> instance.
     * 
     * @param tableCellRenderer
     *          the wrapped table cell renderer.
     */
    public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
      this.tableCellRenderer = tableCellRenderer;
    }

    /**
     * {@inheritDoc}
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
      Component c = tableCellRenderer.getTableCellRendererComponent(table,
          value, isSelected, hasFocus, row, column);
      if (c instanceof JLabel) {
        JLabel l = (JLabel) c;
        l.setHorizontalTextPosition(SwingConstants.LEFT);
        int modelColumn = table.convertColumnIndexToModel(column);
        l.setIcon(getHeaderRendererIcon(modelColumn));
      }
      return c;
    }
  }

  /**
   * Gets the sortingColumns.
   * 
   * @return the sortingColumns.
   */
  protected List<Directive> getSortingColumns() {
    return sortingColumns;
  }
}
