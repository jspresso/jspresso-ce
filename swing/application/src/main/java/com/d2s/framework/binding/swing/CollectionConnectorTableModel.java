/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IRenderableCompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.Coordinates;
import com.d2s.framework.util.exception.IExceptionHandler;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * This class implements a table model backed by a collection connector. As
 * expected, this table model will fire necessary events depending on connectors
 * received events. Its column are determined by the prototype connector which
 * serves as model for the table rows.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorTableModel extends AbstractTableModel {

  private final class CellConnectorListener implements
      IConnectorValueChangeListener, PropertyChangeListener {

    private Coordinates cell;

    private CellConnectorListener(int row, int col) {
      cell = new Coordinates(row, col);
    }

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(@SuppressWarnings("unused")
    final ConnectorValueChangeEvent evt) {
      updateCell();
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(@SuppressWarnings("unused")
    final PropertyChangeEvent evt) {
      updateCell();
    }

    private void updateCell() {
      SwingUtil.updateSwingGui(new Runnable() {

        public void run() {
          if (cell.getX() < getRowCount()) {
            fireTableCellUpdated(cell.getX(), cell.getY());
          }
        }
      });
    }
  }

  private final class RowConnectorListener implements
      IConnectorValueChangeListener {

    private int row;

    private RowConnectorListener(int row) {
      this.row = row;
    }

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(@SuppressWarnings("unused")
    final ConnectorValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

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
  private class TableConnectorListener implements IConnectorValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(final ConnectorValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        public void run() {
          Collection<?> oldCollection = null;
          if (evt.getOldValue() instanceof Collection) {
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
          if (evt.getNewValue() != null
              && !((Collection<?>) evt.getNewValue()).isEmpty()) {
            collectionConnector.setSelectedIndices(new int[] {0});
          }
        }
      });
    }
  }
  private static final long                           serialVersionUID = -3323472361980315420L;
  private Map<Coordinates, CellConnectorListener>     cachedCellListeners;
  private Map<Integer, IConnectorValueChangeListener> cachedRowListeners;

  private ICollectionConnector                        collectionConnector;

  private Map<String, Class>                          columnClassesByIds;

  private List<String>                                columnConnectorKeys;

  private IExceptionHandler                           exceptionHandler;

  /**
   * Constructs a new <code>CollectionConnectorTableModel</code> instance.
   * 
   * @param collectionConnector
   *          the collection connector holding the values of this table model.
   * @param columnConnectorKeys
   *          the list of column connector ids.
   */
  public CollectionConnectorTableModel(
      ICollectionConnector collectionConnector, List<String> columnConnectorKeys) {
    super();
    this.collectionConnector = collectionConnector;
    this.columnConnectorKeys = columnConnectorKeys;
    bindConnector();
  }

  private void bindChildRowConnector(int row) {
    ICompositeValueConnector rowConnector = (ICompositeValueConnector) collectionConnector
        .getChildConnector(row);
    rowConnector
        .addConnectorValueChangeListener(getChildRowConnectorListener(row));
    for (int col = 0; col < columnConnectorKeys.size(); col++) {
      IValueConnector cellConnector = rowConnector
          .getChildConnector(columnConnectorKeys.get(col));
      if (cellConnector instanceof IRenderableCompositeValueConnector
          && ((IRenderableCompositeValueConnector) cellConnector)
              .getRenderingConnector() != null) {
        ((IRenderableCompositeValueConnector) cellConnector)
            .getRenderingConnector().addConnectorValueChangeListener(
                getChildCellConnectorListener(row, col));
      } else {
        CellConnectorListener listener = getChildCellConnectorListener(row, col);
        cellConnector.addConnectorValueChangeListener(listener);
        cellConnector.addPropertyChangeListener(listener);
      }
    }
  }

  private void bindConnector() {
    collectionConnector
        .addConnectorValueChangeListener(new TableConnectorListener());
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

  private IConnectorValueChangeListener getChildRowConnectorListener(int row) {
    if (cachedRowListeners == null) {
      cachedRowListeners = new HashMap<Integer, IConnectorValueChangeListener>();
    }
    IConnectorValueChangeListener cachedListener = cachedRowListeners
        .get(new Integer(row));
    if (cachedListener == null) {
      cachedListener = new RowConnectorListener(row);
      cachedRowListeners.put(new Integer(row), cachedListener);
    }
    return cachedListener;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnClassesByIds != null) {
      return columnClassesByIds.get(columnConnectorKeys.get(columnIndex));
    }
    return super.getColumnClass(columnIndex);
  }

  /**
   * Returns the size of the child connectors prototype used to model the rows.
   * <p>
   * {@inheritDoc}
   */
  public int getColumnCount() {
    return columnConnectorKeys.size();
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

  /**
   * Returns the backed collection connector size.
   * <p>
   * {@inheritDoc}
   */
  public int getRowCount() {
    return collectionConnector.getChildConnectorCount();
  }

  /**
   * Returns the value of the connector which backs the cell model (2nd level of
   * nesting).
   * <p>
   * {@inheritDoc}
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    IValueConnector cellConnector = getConnectorAt(rowIndex, columnIndex);
    Object connectorValue = cellConnector.getConnectorValue();
    if (connectorValue instanceof byte[]) {
      return null;
    }
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
   * Sets the columnClassesByIds.
   * 
   * @param columnClassesByIds
   *          the columnClassesByIds to set.
   */
  public void setColumnClassesByIds(Map<String, Class> columnClassesByIds) {
    this.columnClassesByIds = columnClassesByIds;
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
}
