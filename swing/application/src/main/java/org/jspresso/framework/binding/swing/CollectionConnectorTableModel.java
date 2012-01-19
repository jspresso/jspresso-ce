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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

  private static final long      serialVersionUID = -3323472361980315420L;

  private TableConnectorListener tableListener;
  private RowConnectorListener   rowListener;
  private CellConnectorListener  cellListener;
  private ICollectionConnector   collectionConnector;
  private List<Class<?>>         columnClasses;
  private List<String>           columnConnectorKeys;

  private IExceptionHandler      exceptionHandler;

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
    this.tableListener = new TableConnectorListener();
    this.rowListener = new RowConnectorListener();
    this.cellListener = new CellConnectorListener();
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

  private Coordinates computeCoordinates(IValueConnector connector) {
    IValueConnector cellConnector = connector;
    ICompositeValueConnector rowConnector = cellConnector.getParentConnector();
    while (rowConnector != null
        && rowConnector.getParentConnector() != collectionConnector) {
      cellConnector = rowConnector;
      rowConnector = rowConnector.getParentConnector();
    }
    int col = -1;
    int row = -1;
    if (rowConnector != null) {
      row = computeRow(rowConnector);
      for (String key : columnConnectorKeys) {
        if (rowConnector.getChildConnector(key) == cellConnector) {
          break;
        }
        col++;
      }
    }
    return new Coordinates(row, col);
  }

  private int computeRow(IValueConnector connector) {
    for (int i = 0; i < collectionConnector.getChildConnectorCount(); i++) {
      if (collectionConnector.getChildConnector(i) == connector) {
        return i;
      }
    }
    return -1;
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
    if (!rowConnector.getValueChangeListeners().contains(rowListener)) {
      rowConnector.addValueChangeListener(rowListener);
      for (int col = 0; col < columnConnectorKeys.size(); col++) {
        IValueConnector cellConnector = rowConnector
            .getChildConnector(columnConnectorKeys.get(col));
        if (cellConnector instanceof IRenderableCompositeValueConnector
            && ((IRenderableCompositeValueConnector) cellConnector)
                .getRenderingConnector() != null) {
          ((IRenderableCompositeValueConnector) cellConnector)
              .getRenderingConnector().addValueChangeListener(cellListener);
        } else {
          cellConnector.addValueChangeListener(cellListener);
          cellConnector.addPropertyChangeListener(cellListener);
        }
      }
    }
  }

  private void bindConnector() {
    collectionConnector.addValueChangeListener(tableListener);
    for (int row = 0; row < collectionConnector.getChildConnectorCount(); row++) {
      bindChildRowConnector(row);
    }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      updateCell(computeCoordinates((IValueConnector) evt.getSource()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(ValueChangeEvent evt) {
      updateCell(computeCoordinates((IValueConnector) evt.getSource()));
    }

    private void updateCell(final Coordinates cell) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          if (cell.getX() >= 0 && cell.getY() >= 0
              && cell.getX() < getRowCount()) {
            fireTableCellUpdated(cell.getX(), cell.getY());
          }
        }
      });
    }
  }

  private final class RowConnectorListener implements IValueChangeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(ValueChangeEvent evt) {
      updateRow(computeRow((IValueConnector) evt.getSource()));
    }

    private void updateRow(final int row) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          if (row >= 0 && row < getRowCount()) {
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

  /**
   * Gets the value to display as row toolTip.
   * 
   * @param rowIndex
   *          the row index to compute the toolTip for.
   * @return the row toolTip or null.
   */
  public String getRowToolTip(int rowIndex) {
    return collectionConnector.getChildConnector(rowIndex).toString();
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
          List<?> oldCollection = new ArrayList<Object>();
          if (evt.getOldValue() instanceof Collection<?>) {
            oldCollection = new ArrayList<Object>((Collection<?>) evt
                .getOldValue());
          }
          List<?> newCollection = new ArrayList<Object>();
          if (evt.getNewValue() instanceof Collection<?>) {
            newCollection = new ArrayList<Object>((Collection<?>) evt
                .getNewValue());
          }

          int oldCollectionSize = oldCollection.size();
          int newCollectionSize = newCollection.size();

          for (int i = 0; i < newCollectionSize; i++) {
            Object element = newCollection.get(i);
            if (oldCollectionSize > i) {
              if (oldCollection.get(i) != element) {
                fireTableRowsUpdated(i, i);
              }
            } else {
              fireTableRowsInserted(i, i);
            }
            bindChildRowConnector(i);
          }
          if (newCollectionSize < oldCollectionSize) {
            fireTableRowsDeleted(newCollectionSize, oldCollectionSize - 1);
          }
        }
      });
    }
  }
}
