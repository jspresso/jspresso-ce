/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.ulc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.gui.Coordinates;

import com.ulcjava.base.application.table.AbstractTableModel;

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

  private static final long                           serialVersionUID = -3323472361980315420L;

  private Map<Coordinates, CellConnectorListener>     cachedCellListeners;

  private Map<Integer, IValueChangeListener> cachedRowListeners;

  private ICollectionConnector                        collectionConnector;
  private List<Class<?>>                              columnClasses;
  private List<String>                                columnConnectorKeys;
  private List<IFormatter>                            columnFormatters;

  private IExceptionHandler                           exceptionHandler;

  /**
   * Constructs a new <code>CollectionConnectorTableModel</code> instance.
   * 
   * @param collectionConnector
   *          the collection connector holding the values of this table model.
   * @param columnConnectorKeys
   *          the list of column connector ids.
   * @param columnClasses
   *          the column classes.
   * @param columnFormatters
   *          the columnFormatters.
   */
  public CollectionConnectorTableModel(
      ICollectionConnector collectionConnector,
      List<String> columnConnectorKeys, List<Class<?>> columnClasses,
      List<IFormatter> columnFormatters) {
    super();
    this.collectionConnector = collectionConnector;
    this.columnConnectorKeys = columnConnectorKeys;
    this.columnClasses = columnClasses;
    this.columnFormatters = columnFormatters;
    bindConnector();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnClasses != null) {
      Class<?> columnClass = columnClasses.get(columnIndex);
      if (columnClass != null) {
        return columnClass;
      }
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
    if (cellConnector instanceof ICompositeValueConnector) {
      return cellConnector.toString();
    }
    Object connectorValue = cellConnector.getConnectorValue();
    IFormatter formatter = getColumnFormatter(columnIndex);
    if (formatter != null) {
      connectorValue = formatter.format(connectorValue);
    }
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
   * Sets the exceptionHandler.
   * 
   * @param exceptionHandler
   *          the exceptionHandler to set.
   */
  public void setExceptionHandler(IExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }

  /**
   * Sets the value of the connector which backs the cell model (2nd level of
   * nesting).
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object cellValue, int rowIndex, int columnIndex) {
    IValueConnector cellConnector = getConnectorAt(rowIndex, columnIndex);
    try {
      if (cellConnector instanceof ICompositeValueConnector) {
        if (!(cellValue instanceof String)) {
          // this cellValue is the real one, not the string representation
          // comming
          // back from the client side.
          cellConnector.setConnectorValue(cellValue);
        }
      } else {
        if (cellValue instanceof String
            && getColumnFormatter(columnIndex) != null) {
          try {
            cellConnector.setConnectorValue(getColumnFormatter(columnIndex)
                .parse((String) cellValue));
          } catch (ParseException ex) {
            fireTableCellUpdated(rowIndex, columnIndex);
          }
        } else {
          if ("".equals(cellValue)) {
            cellConnector.setConnectorValue(null);
          } else {
            cellConnector.setConnectorValue(cellValue);
          }
        }
      }
    } catch (RuntimeException ex) {
      if (exceptionHandler != null) {
        fireTableCellUpdated(rowIndex, columnIndex);
        exceptionHandler.handleException(ex, null);
      } else {
        throw ex;
      }
    }
  }

  private void bindChildRowConnector(int row) {
    ICompositeValueConnector rowConnector = (ICompositeValueConnector) collectionConnector
        .getChildConnector(row);
    if (rowConnector != null) {
      rowConnector
          .addValueChangeListener(getChildRowConnectorListener(row));
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
          CellConnectorListener listener = getChildCellConnectorListener(row,
              col);
          cellConnector.addValueChangeListener(listener);
          cellConnector.addPropertyChangeListener(listener);
        }
      }
    }
  }

  private void bindConnector() {
    collectionConnector
        .addValueChangeListener(new TableConnectorListener());
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
    IValueChangeListener cachedListener = cachedRowListeners
        .get(new Integer(row));
    if (cachedListener == null) {
      cachedListener = new RowConnectorListener(row);
      cachedRowListeners.put(new Integer(row), cachedListener);
    }
    return cachedListener;
  }

  private IFormatter getColumnFormatter(int columnIndex) {
    if (columnFormatters != null) {
      return columnFormatters.get(columnIndex);
    }
    return null;
  }

  private IValueConnector getConnectorAt(int rowIndex, int columnIndex) {
    return ((ICompositeValueConnector) collectionConnector
        .getChildConnector(rowIndex)).getChildConnector(columnConnectorKeys
        .get(columnIndex));
  }

  private final class CellConnectorListener implements
      IValueChangeListener, PropertyChangeListener {

    private Coordinates cell;

    private CellConnectorListener(int row, int col) {
      cell = new Coordinates(row, col);
    }

    /**
     * {@inheritDoc}
     */
    public void valueChange(
        @SuppressWarnings("unused") ValueChangeEvent evt) {
      updateCell();
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(
        @SuppressWarnings("unused") PropertyChangeEvent evt) {
      updateCell();
    }

    private void updateCell() {
      if (cell.getX() < getRowCount()) {
        fireTableCellUpdated(cell.getX(), cell.getY());
      }
    }
  }

  private final class RowConnectorListener implements
      IValueChangeListener {

    private int row;

    private RowConnectorListener(int row) {
      this.row = row;
    }

    /**
     * {@inheritDoc}
     */
    public void valueChange(
        @SuppressWarnings("unused") ValueChangeEvent evt) {
      if (row < getRowCount()) {
        fireTableRowsUpdated(row, row);
      }
      if (collectionConnector.getSelectedIndices() != null) {
        if (Arrays.binarySearch(collectionConnector.getSelectedIndices(), row) >= 0) {
          collectionConnector.setSelectedIndices(new int[0]);
        }
      }
    }
  }

  private class TableConnectorListener implements IValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void valueChange(final ValueChangeEvent evt) {
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
  }
}
