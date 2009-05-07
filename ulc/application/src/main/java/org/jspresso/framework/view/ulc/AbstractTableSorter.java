package org.jspresso.framework.view.ulc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jspresso.framework.util.gui.IIndexMapper;

import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.event.ActionEvent;
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

  private static final long      serialVersionUID     = -1685247933285583431L;

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

  private ULCIcon                downIcon;
  private IActionListener        headerActionListener;

  private Set<ULCTableColumn>    sortedColumnsBuffer;
  private List<Directive>        sortingColumns;
  private ULCTableHeader         tableHeader;
  private ITableModel            tableModel;
  private ITableModelListener    tableModelListener;
  private ULCIcon                upIcon;

  /**
   * Constructs a new <code>TableSorter</code> instance.
   */
  private AbstractTableSorter() {
    this.sortingColumns = new ArrayList<Directive>();
    this.headerActionListener = new HeaderActionHandler();
    this.tableModelListener = createTableModelHandler();
    this.sortedColumnsBuffer = new HashSet<ULCTableColumn>();
  }

  /**
   * Constructs a new <code>AbstractTableSorter</code> instance.
   * 
   * @param tableModel
   *          tableModel.
   */
  public AbstractTableSorter(ITableModel tableModel) {
    this();
    setTableModel(tableModel);
  }

  /**
   * Constructs a new <code>AbstractTableSorter</code> instance.
   * 
   * @param tableModel
   *          tableModel.
   * @param tableHeader
   *          tableHeader.
   */
  public AbstractTableSorter(ITableModel tableModel, ULCTableHeader tableHeader) {
    this();
    setTableModel(tableModel);
    setTableHeader(tableHeader);
  }

  /**
   * Creates a table model listener to react to the underlying table model
   * change events.
   * 
   * @return the table model listener.
   */
  protected abstract ITableModelListener createTableModelHandler();

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
   * Sets the downIcon.
   * 
   * @param downIcon
   *          the downIcon to set.
   */
  public void setDownIcon(ULCIcon downIcon) {
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
   *          tableModel.
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
   *          the upIcon to set.
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

  /**
   * Gets HeaderRendererIcon.
   * 
   * @param column
   *          column.
   * @param size
   *          size.
   * @return HeaderRendererIcon
   */
  protected ULCIcon getHeaderRendererIcon(int column, int size) {
    Directive directive = getDirective(column);
    if (directive == NOT_SORTED_DIRECTIVE) {
      return null;
    }
    if (directive.direction == DESCENDING) {
      return downIcon;
    }
    return upIcon;
  }

  /**
   * Cancels sorting.
   */
  protected void cancelSorting() {
    resetHeaderRenderers();
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

  private void resetHeaderRenderers() {
    for (ULCTableColumn col : sortedColumnsBuffer) {
      col.setHeaderRenderer(null);
    }
    sortedColumnsBuffer.clear();
    tableHeader.repaint();
  }

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
    public int getColumn() {
      return column;
    }

    /**
     * Gets the direction.
     * 
     * @return the direction.
     */
    public int getDirection() {
      return direction;
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
        if (status != 0) {
          sortedColumnsBuffer.add(viewColumn);
        } else {
          sortedColumnsBuffer.remove(viewColumn);
        }
        setSortingStatus(viewColumn.getModelIndex(), status);
        if (status != 0) {
          ITableCellRenderer headerRenderer = viewColumn.getHeaderRenderer();
          if (!(headerRenderer instanceof SortableHeaderRenderer)) {
            headerRenderer = new SortableHeaderRenderer(viewColumn
                .getModelIndex());
          }
          // used to refresh the client side :(
          viewColumn.setHeaderRenderer(headerRenderer);
        } else {
          viewColumn.setHeaderRenderer(null);
        }
        tableHeader.repaint();
      }
    }
  }

  private class SortableHeaderRenderer extends DefaultTableHeaderCellRenderer {

    private static final long serialVersionUID = 8198258084747052695L;
    private int               modelColumnIndex;

    /**
     * Constructs a new <code>SortableHeaderRenderer</code> instance.
     * 
     * @param modelColumnIndex
     *          the model column index this renderer is used for.
     */
    protected SortableHeaderRenderer(int modelColumnIndex) {
      this.modelColumnIndex = modelColumnIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getTableCellRendererComponent(ULCTable table,
        Object value, boolean isSelected, boolean hasFocus, int row) {
      setHorizontalTextPosition(IDefaults.LEFT);
      setHorizontalAlignment(IDefaults.LEFT);
      setIcon(getHeaderRendererIcon(modelColumnIndex, getFont().getSize()));
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row);
    }
  }

  /**
   * Retrieves the column from the model index.
   * 
   * @param modelColumnIndex
   *          the model column index.
   * @return the column.
   */
  protected ULCTableColumn getSortedColumn(int modelColumnIndex) {
    if (modelColumnIndex < 0) {
      return null;
    }
    for (ULCTableColumn column : sortedColumnsBuffer) {
      if (column.getModelIndex() == modelColumnIndex) {
        return column;
      }
    }
    return null;
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
