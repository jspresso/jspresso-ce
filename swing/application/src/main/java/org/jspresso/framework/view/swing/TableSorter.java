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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 * TableSorter is a decorator for TableModels; adding sorting functionality to a
 * supplied TableModel. TableSorter does not store or copy the data in its
 * TableModel; instead it maintains a map from the row indexes of the view to
 * the row indexes of the model. As requests are made of the sorter (like
 * getValueAt(row, col)) they are passed to the underlying model after the row
 * numbers have been translated via the internal mapping array. This way, the
 * TableSorter appears to hold another copy of the table with the rows in a
 * different order.
 * <p/>
 * TableSorter registers itself as a listener to the underlying model, just as
 * the JTable itself would. Events received from the model are examined,
 * sometimes manipulated (typically widened), and then passed on to the
 * TableSorter's listeners (typically the JTable). If a change to the model has
 * invalidated the order of TableSorter's rows, a note of this is made and the
 * sorter will resort the rows the next time a value is requested.
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
 * This is a long overdue rewrite of a class of the same name that first
 * appeared in the swing table demos in 1997.
 *
 * @author Philip Milne
 * @author Brendon McLean
 * @author Dan van Enckevort
 * @author Parwinder Sekhon
 * @version 2.0 02/27/04
 */

public class TableSorter extends AbstractTableSorter {

  /**
   * {@code COMPARABLE_COMPARATOR}.
   */
  public static final Comparator<Object> COMPARABLE_COMPARATOR = new Comparator<Object>() {

    @Override
    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
      return ((Comparable<Object>) o1).compareTo(o2);
    }
  };

  /**
   * {@code LEXICAL_COMPARATOR}.
   */
  public static final Comparator<Object> LEXICAL_COMPARATOR = new Comparator<Object>() {

    @Override
    public int compare(Object o1, Object o2) {
      return o1.toString().compareTo(o2.toString());
    }
  };

  private static final long serialVersionUID = -5437879837063286581L;

  private final Map<Class<?>, Comparator<?>> columnComparators = new HashMap<>();

  private int[] modelToView;
  private Row[] viewToModel;

  /**
   * Constructs a new {@code TableSorter} instance.
   *
   * @param tableModel
   *          tableModel.
   * @param tableHeader
   *          tableHeader.
   */
  public TableSorter(TableModel tableModel, JTableHeader tableHeader) {
    super(tableModel, tableHeader);
  }

  /**
   * modelIndex.
   *
   * @param viewIndex
   *          viewIndex.
   * @return modelIndex.
   */
  @Override
  public int modelIndex(int viewIndex) {
    return getViewToModel()[viewIndex].modelIndex;
  }

  /**
   * Sets ColumnComparator.
   *
   * @param type
   *          type.
   * @param comparator
   *          comparator.
   */
  public void setColumnComparator(Class<?> type, Comparator<?> comparator) {
    if (comparator == null) {
      columnComparators.remove(type);
    } else {
      columnComparators.put(type, comparator);
    }
  }

  /**
   * viewIndex.
   *
   * @param modelIndex
   *          modelIndex
   * @return viewIndex
   */
  @Override
  public int viewIndex(int modelIndex) {
    return getModelToView()[modelIndex];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void clearSortingState() {
    viewToModel = null;
    modelToView = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected TableModelListener createTableModelHandler() {
    return new TableModelHandler();
  }

  /**
   * Gets Comparator.
   *
   * @param column
   *          column.
   * @return Comparator.
   */
  protected Comparator<?> getComparator(int column) {
    Class<?> columnType = getTableModel().getColumnClass(column);
    Comparator<?> comparator = columnComparators.get(columnType);
    if (comparator != null) {
      return comparator;
    }
    if (Comparable.class.isAssignableFrom(columnType)) {
      return COMPARABLE_COMPARATOR;
    }
    return LEXICAL_COMPARATOR;
  }

  // Helper classes

  /**
   * {@inheritDoc}
   */
  @Override
  protected void sortingStatusChanged() {
    clearSortingState();
    fireTableDataChanged();
    if (getTableHeader() != null) {
      getTableHeader().repaint();
    }
  }

  private int[] getModelToView() {
    if (modelToView == null) {
      int n = getViewToModel().length;
      modelToView = new int[n];
      for (int i = 0; i < n; i++) {
        modelToView[modelIndex(i)] = i;
      }
    }
    return modelToView;
  }

  private Row[] getViewToModel() {
    if (viewToModel == null) {
      int tableModelRowCount = getTableModel().getRowCount();
      viewToModel = new Row[tableModelRowCount];
      for (int row = 0; row < tableModelRowCount; row++) {
        viewToModel[row] = new Row(row);
      }

      if (isSorting()) {
        Arrays.sort(viewToModel);
      }
    }
    return viewToModel;
  }

  private class Row implements Comparable<Object> {

    private final int modelIndex;

    /**
     * Constructs a new {@code Row} instance.
     *
     * @param index the index.
     */
    public Row(int index) {
      this.modelIndex = index;
    }

    /**
     * compareTo.
     *
     * @param o the object to compare to.
     * @return comparison.
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(Object o) {
      int row1 = modelIndex;
      int row2 = ((Row) o).modelIndex;

      for (Directive directive : getSortingColumns()) {
        int column = directive.getColumn();
        Object o1 = getTableModel().getValueAt(row1, column);
        Object o2 = getTableModel().getValueAt(row2, column);

        int comparison;
        // Define null less than everything, except null.
        if (o1 == null && o2 == null) {
          comparison = 0;
        } else if (o1 == null) {
          comparison = -1;
        } else if (o2 == null) {
          comparison = 1;
        } else {
          comparison = ((Comparator<Object>) getComparator(column)).compare(o1,
              o2);
        }
        if (comparison != 0) {
          if (directive.getDirection() == DESCENDING) {
            return -comparison;
          }
          return comparison;
        }
      }
      return 0;
    }
  }

  private class TableModelHandler implements TableModelListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableChanged(TableModelEvent e) {
      // If we're not sorting by anything, just pass the event along.
      if (!isSorting()) {
        clearSortingState();
        fireTableChanged(e);
        return;
      }

      // If the table structure has changed, cancel the sorting; the
      // sorting columns may have been either moved or deleted from
      // the model.
      if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
        cancelSorting();
        fireTableChanged(e);
        return;
      }

      // We can map a cell event through to the view without widening
      // when the following conditions apply:
      //
      // a) all the changes are on one row (e.getFirstRow() == e.getLastRow())
      // and,
      // b) all the changes are in one column (column !=
      // TableModelEvent.ALL_COLUMNS) and,
      // c) we are not sorting on that column (getSortingStatus(column) ==
      // NOT_SORTED) and,
      // d) a reverse lookup will not trigger a sort (modelToView != null)
      //
      // Note: INSERT and DELETE events fail this test as they have column ==
      // ALL_COLUMNS.
      //
      // The last check, for (modelToView != null) is to see if modelToView
      // is already allocated. If we don't do this check; sorting can become
      // a performance bottleneck for applications where cells
      // change rapidly in different parts of the table. If cells
      // change alternately in the sorting column and then outside of
      // it this class can end up re-sorting on alternate cell updates -
      // which can be a performance problem for large tables. The last
      // clause avoids this problem.
      int column = e.getColumn();
      if (e.getFirstRow() == e.getLastRow()
          && column != TableModelEvent.ALL_COLUMNS
          && getSortingStatus(column) == NOT_SORTED && modelToView != null) {
        int viewIndex = getModelToView()[e.getFirstRow()];
        fireTableChanged(new TableModelEvent(TableSorter.this, viewIndex,
            viewIndex, column, e.getType()));
        return;
      }

      // Something has happened to the data that may have invalidated the row
      // order.
      clearSortingState();
      fireTableDataChanged();
    }
  }
}
