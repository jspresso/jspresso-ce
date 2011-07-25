/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.gui.Coordinates;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * This class implements a table model backed by a collection connector. As
 * expected, this table model will fire necessary events depending on connectors
 * received events. Its column are determined by the prototype connector which
 * serves as model for the table rows.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorTableModel extends AbstractTableModel {

  private static final long                       serialVersionUID = -3323472361980315420L;

  private Map<Coordinates, CellConnectorListener> cachedCellListeners;
  private Map<Integer, IValueChangeListener>      cachedRowListeners;
  private ICollectionConnector                    collectionConnector;
  private List<Class<?>>                          columnClasses;
  private List<String>                            columnConnectorKeys;

  private IExceptionHandler                       exceptionHandler;

  /**
   * Constructs a new <code>CollectionConnectorTableModel</code> instance.
   * 
   * @param collectionConnector
   *          the collection connector holding the values of this table model.
   * @param columnConnectorKeys
   *          the list of column connector ids.
   * @param columnClasses
   *          the column classes.
   */
  public CollectionConnectorTableModel(
      ICollectionConnector collectionConnector,
      List<String> columnConnectorKeys, List<Class<?>> columnClasses) {
    super();
    this.collectionConnector = collectionConnector;
    this.columnConnectorKeys = columnConnectorKeys;
    this.columnClasses = columnClasses;
    bindConnector();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnClasses != null) {
      return columnClasses.get(columnIndex);
    }
    return super.getColumnClass(columnIndex);
  }

  /**
   * Returns the size of the child connectors prototype used to model the rows.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int getColumnCount() {
    return columnConnectorKeys.size();
  }

  /**
   * Returns the backed collection connector size.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int getRowCount() {
    return collectionConnector.getChildConnectorCount();
  }

  /**
   * Returns the value of the connector which backs the cell model (2nd level of
   * nesting).
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    IValueConnector cellConnector = getConnectorAt(rowIndex, columnIndex);
    if (cellConnector instanceof ICompositeValueConnector) {
      return cellConnector;
    }
    Object connectorValue = cellConnector.getConnectorValue();
    // if (connectorValue instanceof byte[]) {
    // return null;
    // }
    return connectorValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return collectionConnector.isWritable()
        && collectionConnector.getChildConnector(rowIndex).isWritable()
        && getConnectorAt(rowIndex, columnIndex).isWritable();
  }

  /**
   * Sets the exceptionHandler.
   * 
   * @param exceptionHandler
   *          the exceptionHandler to set.
   */
  public void setExceptionHandler(IExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }

  /**
   * Returns the value of the connector which backs the cell model (2nd level of
   * nesting).
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object cellValue, int rowIndex, int columnIndex) {
    try {
      getConnectorAt(rowIndex, columnIndex).setConnectorValue(cellValue);
    } catch (RuntimeException ex) {
      if (exceptionHandler != null) {
        exceptionHandler.handleException(ex, null);
      } else {
        throw ex;
      }
    }
  }

  private void bindChildRowConnector(int row) {
    ICompositeValueConnector rowConnector = (ICompositeValueConnector) collectionConnector
        .getChildConnector(row);
    rowConnector.addValueChangeListener(getChildRowConnectorListener(row));
    for (int col = 0; col < columnConnectorKeys.size(); col++) {
      IValueConnector cellConnector = rowConnector
          .getChildConnector(columnConnectorKeys.get(col));
      if (cellConnector instanceof IRenderableCompositeValueConnector
          && ((IRenderableCompositeValueConnector) cellConnector)
              .getRenderingConnector() != null) {
        ((IRenderableCompositeValueConnector) cellConnector)
            .getRenderingConnector().addValueChangeListener(
                getChildCellConnectorListener(row, col));
      } else {
        CellConnectorListener listener = getChildCellConnectorListener(row, col);
        cellConnector.addValueChangeListener(listener);
        cellConnector.addPropertyChangeListener(listener);
      }
    }
  }

  private void bindConnector() {
    collectionConnector.addValueChangeListener(new TableConnectorListener());
    for (int row = 0; row < collectionConnector.getChildConnectorCount(); row++) {
      bindChildRowConnector(row);
    }
  }

  private CellConnectorListener getChildCellConnectorListener(int row, int col) {
    if (cachedCellListeners == null) {
      cachedCellListeners = new HashMap<Coordinates, CellConnectorListener>();
    }
    CellConnectorListener cachedListener = cachedCellListeners
        .get(new Coordinates(row, col));
    if (cachedListener == null) {
      cachedListener = new CellConnectorListener(row, col);
      cachedCellListeners.put(new Coordinates(row, col), cachedListener);
    }
    return cachedListener;
  }

  private IValueChangeListener getChildRowConnectorListener(int row) {
    if (cachedRowListeners == null) {
      cachedRowListeners = new HashMap<Integer, IValueChangeListener>();
    }
    IValueChangeListener cachedListener = cachedRowListeners.get(new Integer(
        row));
    if (cachedListener == null) {
      cachedListener = new RowConnectorListener(row);
      cachedRowListeners.put(new Integer(row), cachedListener);
    }
    return cachedListener;
  }

  /**
   * Gets the value connector backing the table cell.
   * 
   * @param rowIndex
   *          the row index of the cell.
   * @param columnIndex
   *          the column index of the cell.
   * @return the value connector behind the cell.
   */
  private IValueConnector getConnectorAt(int rowIndex, int columnIndex) {
    return ((ICompositeValueConnector) collectionConnector
        .getChildConnector(rowIndex)).getChildConnector(columnConnectorKeys
        .get(columnIndex));
  }

  private final class CellConnectorListener implements IValueChangeListener,
      PropertyChangeListener {

    private Coordinates cell;

    private CellConnectorListener(int row, int col) {
      cell = new Coordinates(row, col);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(
        @SuppressWarnings("unused") final PropertyChangeEvent evt) {
      updateCell();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(
        @SuppressWarnings("unused") final ValueChangeEvent evt) {
      updateCell();
    }

    private void updateCell() {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          if (cell.getX() < getRowCount()) {
            fireTableCellUpdated(cell.getX(), cell.getY());
          }
        }
      });
    }
  }

  private final class RowConnectorListener implements IValueChangeListener {

    private int row;

    private RowConnectorListener(int row) {
      this.row = row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(
        @SuppressWarnings("unused") final ValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          if (row < getRowCount()) {
            fireTableRowsUpdated(row, row);
          }
          if (collectionConnector.getSelectedIndices() != null) {
            if (Arrays.binarySearch(collectionConnector.getSelectedIndices(),
                row) >= 0) {
              collectionConnector.setSelectedIndices(new int[0]);
            }
          }
        }
      });
    }
  }

  private class TableConnectorListener implements IValueChangeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(final ValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          Collection<?> oldCollection = null;
          if (evt.getOldValue() instanceof Collection<?>) {
            oldCollection = (Collection<?>) evt.getOldValue();
          }
          Collection<?> newCollection = (Collection<?>) evt.getNewValue();
          int oldCollectionSize = 0;
          int newCollectionSize = 0;
          if (oldCollection != null) {
            oldCollectionSize = oldCollection.size();
          }
          if (newCollection != null) {
            newCollectionSize = newCollection.size();
          }
          if (newCollectionSize > oldCollectionSize) {
            fireTableRowsInserted(oldCollectionSize, newCollectionSize - 1);
            for (int row = oldCollectionSize; row < newCollectionSize; row++) {
              bindChildRowConnector(row);
            }
          } else if (newCollectionSize < oldCollectionSize) {
            fireTableRowsDeleted(newCollectionSize, oldCollectionSize - 1);
          }
        }
      });
    }
  }
}
