package org.jspresso.framework.view.ulc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.util.gui.IIndexMapper;

import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.TableModelEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;
import com.ulcjava.base.application.event.serializable.ITableModelListener;
import com.ulcjava.base.application.table.AbstractTableModel;
import com.ulcjava.base.application.table.DefaultTableHeaderCellRenderer;
import com.ulcjava.base.application.table.ITableCellRenderer;
import com.ulcjava.base.application.table.ITableModel;
import com.ulcjava.base.application.table.ULCTableColumn;
import com.ulcjava.base.application.table.ULCTableHeader;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.shared.IDefaults;

/**
 * TableSorter is a decorator for TableModels; adding sorting functionality to a
 * supplied TableModel. TableSorter does not store or copy the data in its
 * TableModel; instead it maintains a map from the row indexes of the view to
 * the row indexes of the model. As requests are made of the sorter (like
 * getValueAt(row, col)) they are passed to the underlying model after the row
 * numbers have been translated via the internal mapping array. This way, the
 * TableSorter appears to hold another copy of the table with the rows in a
 * different order. <p/> TableSorter registers itself as a listener to the
 * underlying model, just as the JTable itself would. Events received from the
 * model are examined, sometimes manipulated (typically widened), and then
 * passed on to the TableSorter's listeners (typically the JTable). If a change
 * to the model has invalidated the order of TableSorter's rows, a note of this
 * is made and the sorter will resort the rows the next time a value is
 * requested. <p/> When the tableHeader property is set, either by using the
 * setTableHeader() method or the two argument constructor, the table header may
 * be used as a complete UI for TableSorter. The default renderer of the
 * tableHeader is decorated with a renderer that indicates the sorting status of
 * each column. In addition, a mouse listener is installed with the following
 * behavior:
 * <ul>
 * <li> Mouse-click: Clears the sorting status of all other columns and advances
 * the sorting status of that column through three values: {NOT_SORTED,
 * ASCENDING, DESCENDING} (then back to NOT_SORTED again).
 * <li> SHIFT-mouse-click: Clears the sorting status of all other columns and
 * cycles the sorting status of the column through the same three values, in the
 * opposite order: {NOT_SORTED, DESCENDING, ASCENDING}.
 * <li> CONTROL-mouse-click and CONTROL-SHIFT-mouse-click: as above except that
 * the changes to the column do not cancel the statuses of columns that are
 * already sorting - giving a way to initiate a compound sort.
 * </ul>
 * <p/> This is a long overdue rewrite of a class of the same name that first
 * appeared in the swing table demos in 1997.
 * 
 * @author Philip Milne
 * @author Brendon McLean
 * @author Dan van Enckevort
 * @author Parwinder Sekhon
 * @version 2.0 02/27/04
 */

public class TableSorter extends AbstractTableModel implements IIndexMapper {

  /**
   * <code>ASCENDING</code>.
   */
  public static final int                   ASCENDING             = 1;

  /**
   * <code>COMPARABLE_COMPARATOR</code>.
   */
  public static final Comparator<Object>    COMPARABLE_COMPARATOR = new Comparator<Object>() {

                                                                    @SuppressWarnings("unchecked")
                                                                    public int compare(
                                                                        Object o1,
                                                                        Object o2) {
                                                                      return ((Comparable<Object>) o1)
                                                                          .compareTo(o2);
                                                                    }
                                                                  };

  /**
   * <code>DESCENDING</code>.
   */
  public static final int                   DESCENDING            = -1;

  /**
   * <code>LEXICAL_COMPARATOR</code>.
   */
  public static final Comparator<Object>    LEXICAL_COMPARATOR    = new Comparator<Object>() {

                                                                    public int compare(
                                                                        Object o1,
                                                                        Object o2) {
                                                                      return o1
                                                                          .toString()
                                                                          .compareTo(
                                                                              o2
                                                                                  .toString());
                                                                    }
                                                                  };

  /**
   * <code>NOT_SORTED</code>.
   */
  public static final int                   NOT_SORTED            = 0;

  private static final Directive            NOT_SORTED_DIRECTIVE  = new Directive(
                                                                      -1,
                                                                      NOT_SORTED);

  private static final long                 serialVersionUID      = -5437879837063286581L;
  private Map<Class<?>, Comparator<Object>> columnComparators     = new HashMap<Class<?>, Comparator<Object>>();

  private ULCIcon                           downIcon;
  private IActionListener                   headerActionListener;

  private int[]                             modelToView;
  private Set<ULCTableColumn>               sortedColumnsBuffer;
  private List<Directive>                   sortingColumns        = new ArrayList<Directive>();
  private ULCTableHeader                    tableHeader;
  private ITableModel                       tableModel;
  private ITableModelListener               tableModelListener;
  private ULCIcon                           upIcon;

  private Row[]                             viewToModel;

  /**
   * Constructs a new <code>TableSorter</code> instance.
   */
  public TableSorter() {
    this.headerActionListener = new HeaderActionHandler();
    this.tableModelListener = new TableModelHandler();
    this.sortedColumnsBuffer = new HashSet<ULCTableColumn>();
  }

  /**
   * Constructs a new <code>TableSorter</code> instance.
   * 
   * @param tableModel
   *            tableModel.
   */
  public TableSorter(ITableModel tableModel) {
    this();
    setTableModel(tableModel);
  }

  /**
   * Constructs a new <code>TableSorter</code> instance.
   * 
   * @param tableModel
   *            tableModel.
   * @param tableHeader
   *            tableHeader.
   */
  public TableSorter(ITableModel tableModel, ULCTableHeader tableHeader) {
    this();
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
   *            column.
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
  public ULCTableHeader getTableHeader() {
    return tableHeader;
  }

  /**
   * Gets tableModel.
   * 
   * @return tableModel.
   */
  public ITableModel getTableModel() {
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
   * modelIndex.
   * 
   * @param viewIndex
   *            viewIndex.
   * @return modelIndex.
   */
  public int modelIndex(int viewIndex) {
    return getViewToModel()[viewIndex].modelIndex;
  }

  /**
   * Sets ColumnComparator.
   * 
   * @param type
   *            type.
   * @param comparator
   *            comparator.
   */
  @SuppressWarnings("unchecked")
  public void setColumnComparator(Class type, Comparator comparator) {
    if (comparator == null) {
      columnComparators.remove(type);
    } else {
      columnComparators.put(type, comparator);
    }
  }

  /**
   * Sets the downIcon.
   * 
   * @param downIcon
   *            the downIcon to set.
   */
  public void setDownIcon(ULCIcon downIcon) {
    this.downIcon = downIcon;
  }

  /**
   * Sets column sorting status.
   * 
   * @param column
   *            column.
   * @param status
   *            status.
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
   *            tableHeader.
   */
  public void setTableHeader(ULCTableHeader tableHeader) {
    if (this.tableHeader != null) {
      this.tableHeader.removeActionListener(headerActionListener);
    }
    this.tableHeader = tableHeader;
    if (this.tableHeader != null) {
      this.tableHeader.addActionListener(headerActionListener);
    }
  }

  /**
   * Sets tableModel.
   * 
   * @param tableModel
   *            tableModel.
   */
  public void setTableModel(ITableModel tableModel) {
    if (this.tableModel != null) {
      this.tableModel.removeTableModelListener(tableModelListener);
    }

    this.tableModel = tableModel;
    if (this.tableModel != null) {
      this.tableModel.addTableModelListener(tableModelListener);
    }

    clearSortingState();
    fireTableStructureChanged();
  }

  /**
   * Sets the upIcon.
   * 
   * @param upIcon
   *            the upIcon to set.
   */
  public void setUpIcon(ULCIcon upIcon) {
    this.upIcon = upIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object aValue, int row, int column) {
    tableModel.setValueAt(aValue, modelIndex(row), column);
  }

  // TableModel interface methods

  /**
   * viewIndex.
   * 
   * @param modelIndex
   *            modelIndex
   * @return viewIndex
   */
  public int viewIndex(int modelIndex) {
    return getModelToView()[modelIndex];
  }

  /**
   * Gets Comparator.
   * 
   * @param column
   *            column.
   * @return Comparator.
   */
  protected Comparator<Object> getComparator(int column) {
    Class<?> columnType = tableModel.getColumnClass(column);
    Comparator<Object> comparator = columnComparators.get(columnType);
    if (comparator != null) {
      return comparator;
    }
    if (Comparable.class.isAssignableFrom(columnType)) {
      return COMPARABLE_COMPARATOR;
    }
    return LEXICAL_COMPARATOR;
  }

  /**
   * Gets HeaderRendererIcon.
   * 
   * @param column
   *            column.
   * @param size
   *            size.
   * @return HeaderRendererIcon
   */
  protected ULCIcon getHeaderRendererIcon(int column,
      int size) {
    Directive directive = getDirective(column);
    if (directive == NOT_SORTED_DIRECTIVE) {
      return null;
    }
    if (directive.direction == DESCENDING) {
      return downIcon;
    }
    return upIcon;
  }

  private void cancelSorting() {
    resetHeaderRenderers();
    sortingColumns.clear();
    sortingStatusChanged();
  }

  private void clearSortingState() {
    viewToModel = null;
    modelToView = null;
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
      int tableModelRowCount = tableModel.getRowCount();
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

  // Helper classes

  private void resetHeaderRenderers() {
    for (ULCTableColumn col : sortedColumnsBuffer) {
      col.setHeaderRenderer(null);
    }
    sortedColumnsBuffer.clear();
    tableHeader.repaint();
  }

  private void sortingStatusChanged() {
    clearSortingState();
    fireTableDataChanged();
    // if (tableHeader != null) {
    // tableHeader.repaint();
    // }
  }

  private static final class Directive {

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
  }

  private class HeaderActionHandler implements IActionListener {

    private static final long serialVersionUID = 2722236995259713328L;

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
      ULCTableColumn viewColumn = (ULCTableColumn) e.getSource();
      if (viewColumn != null) {
        int status = getSortingStatus(viewColumn.getModelIndex());

        if ((e.getModifiers() & ActionEvent.CTRL_MASK) == 0) {
          cancelSorting();
        }

        // Cycle the sorting states through {NOT_SORTED, ASCENDING, DESCENDING}
        // or
        // {NOT_SORTED, DESCENDING, ASCENDING} depending on whether shift is
        // pressed.

        if (((e.getModifiers() & ActionEvent.SHIFT_MASK) != 0)) {
          status -= 1;
        } else {
          status += 1;
        }

        status = (status + 4) % 3 - 1; // signed mod, returning {-1, 0, 1}
        setSortingStatus(viewColumn.getModelIndex(), status);
        if (status != 0) {
          ITableCellRenderer headerRenderer = viewColumn.getHeaderRenderer();
          if (!(headerRenderer instanceof SortableHeaderRenderer)) {
            headerRenderer = new SortableHeaderRenderer(viewColumn
                .getModelIndex());
          }
          // used to refresh the client side :(
          viewColumn.setHeaderRenderer(headerRenderer);
          sortedColumnsBuffer.add(viewColumn);
        } else {
          viewColumn.setHeaderRenderer(null);
          sortedColumnsBuffer.remove(viewColumn);
        }
        tableHeader.repaint();
      }
    }
  }

  private class Row implements Comparable<Object> {

    private int modelIndex;

    /**
     * Constructs a new <code>Row</code> instance.
     * 
     * @param index
     */
    public Row(int index) {
      this.modelIndex = index;
    }

    /**
     * compareTo.
     * 
     * @param o
     * @return comparison.
     */
    @SuppressWarnings("unchecked")
    public int compareTo(Object o) {
      int row1 = modelIndex;
      int row2 = ((Row) o).modelIndex;

      for (Iterator it = sortingColumns.iterator(); it.hasNext();) {
        Directive directive = (Directive) it.next();
        int column = directive.column;
        Object o1 = tableModel.getValueAt(row1, column);
        Object o2 = tableModel.getValueAt(row2, column);

        int comparison = 0;
        // Define null less than everything, except null.
        if (o1 == null && o2 == null) {
          comparison = 0;
        } else if (o1 == null) {
          comparison = -1;
        } else if (o2 == null) {
          comparison = 1;
        } else {
          comparison = getComparator(column).compare(o1, o2);
        }
        if (comparison != 0) {
          if (directive.direction == DESCENDING) {
            return -comparison;
          }
          return comparison;
        }
      }
      return 0;
    }
  }

  private class SortableHeaderRenderer extends DefaultTableHeaderCellRenderer {

    private static final long serialVersionUID = 8198258084747052695L;
    private int               column;

    /**
     * Constructs a new <code>SortableHeaderRenderer</code> instance.
     * 
     * @param column
     *            the column this renderer is used for.
     */
    protected SortableHeaderRenderer(int column) {
      this.column = column;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getTableCellRendererComponent(ULCTable table,
        Object value, boolean isSelected, boolean hasFocus, int row) {
      setHorizontalTextPosition(IDefaults.LEFT);
      setHorizontalAlignment(IDefaults.LEFT);
      int modelColumn = table.convertColumnIndexToModel(column);
      setIcon(getHeaderRendererIcon(modelColumn, getFont().getSize()));
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row);
    }
  }

  private class TableModelHandler implements ITableModelListener {

    private static final long serialVersionUID = 447926266162838842L;

    /**
     * {@inheritDoc}
     */
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
      return;
    }
  }
}
