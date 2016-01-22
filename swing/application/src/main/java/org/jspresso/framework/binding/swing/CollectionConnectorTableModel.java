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
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorTableModel extends AbstractTableModel {

  private static final long      serialVersionUID = -3323472361980315420L;

  private final TableConnectorListener tableListener;
  private final RowConnectorListener   rowListener;
  private final CellConnectorListener  cellListener;
  private final ICollectionConnector   collectionConnector;
  private final List<Class<?>>         columnClasses;
  private final List<String>           columnConnectorKeys;

  private String                 rowBackgroundProperty;
  private String                 rowForegroundProperty;
  private String                 rowFontProperty;

  private IExceptionHandler      exceptionHandler;

  /**
   * Constructs a new {@code CollectionConnectorTableModel} instance.
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
      if (exceptionHandler != null
          && exceptionHandler.handleException(ex, null)) {
        return;
      }
      throw ex;
    }
  }

  private void bindChildRowConnector(int row) {
    ICompositeValueConnector rowConnector = (ICompositeValueConnector) collectionConnector
        .getChildConnector(row);
    if (rowConnector != null && !rowConnector.getValueChangeListeners().contains(rowListener)) {
      rowConnector.addValueChangeListener(rowListener);
      for (String columnConnectorKey : columnConnectorKeys) {
        IValueConnector cellConnector = rowConnector
            .getChildConnector(columnConnectorKey);
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
          resetRowSelection(row);
        }
      });
    }
  }

  private void resetRowSelection(int row) {
    if (collectionConnector.getSelectedIndices() != null) {
      if (Arrays.binarySearch(collectionConnector.getSelectedIndices(), row) >= 0) {
        collectionConnector.setSelectedIndices();
      }
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

  /**
   * Gets the value to display as cell toolTip.
   *
   * @param rowIndex
   *          the row index to compute the toolTip for.
   * @param toolTipProperty
   *          the property used to compute the tooltip.
   * @return the cell toolTip or null.
   */
  public String getCellToolTip(int rowIndex, String toolTipProperty) {
    IValueConnector toolTipConnector = ((ICompositeValueConnector) collectionConnector
        .getChildConnector(rowIndex)).getChildConnector(toolTipProperty);
    if (toolTipConnector != null
        && toolTipConnector.getConnectorValue() != null) {
      return toolTipConnector.getConnectorValue().toString();
    }
    return null;
  }

  /**
   * Gets the value to display as cell background.
   *
   * @param rowIndex
   *          the row index to compute the background for.
   * @param backgroundProperty
   *          the property used to compute the background.
   * @return the cell background or null.
   */
  public String getCellBackground(int rowIndex, String backgroundProperty) {
    IValueConnector backgroundConnector = ((ICompositeValueConnector) collectionConnector
        .getChildConnector(rowIndex)).getChildConnector(backgroundProperty);
    if (backgroundConnector != null
        && backgroundConnector.getConnectorValue() != null) {
      return backgroundConnector.getConnectorValue().toString();
    }
    return null;
  }

  /**
   * Gets the value to display as cell foreground.
   *
   * @param rowIndex
   *          the row index to compute the foreground for.
   * @param foregroundProperty
   *          the property used to compute the foreground.
   * @return the cell foreground or null.
   */
  public String getCellForeground(int rowIndex, String foregroundProperty) {
    IValueConnector foregroundConnector = ((ICompositeValueConnector) collectionConnector
        .getChildConnector(rowIndex)).getChildConnector(foregroundProperty);
    if (foregroundConnector != null
        && foregroundConnector.getConnectorValue() != null) {
      return foregroundConnector.getConnectorValue().toString();
    }
    return null;
  }

  /**
   * Gets the value to display as cell font.
   *
   * @param rowIndex
   *          the row index to compute the font for.
   * @param fontProperty
   *          the property used to compute the font.
   * @return the cell font or null.
   */
  public String getCellFont(int rowIndex, String fontProperty) {
    IValueConnector fontConnector = ((ICompositeValueConnector) collectionConnector
        .getChildConnector(rowIndex)).getChildConnector(fontProperty);
    if (fontConnector != null && fontConnector.getConnectorValue() != null) {
      return fontConnector.getConnectorValue().toString();
    }
    return null;
  }

  /**
   * Gets the value to display as row background.
   *
   * @param rowIndex
   *          the row index to compute the background for.
   * @return the row background or null.
   */
  public String getRowBackground(int rowIndex) {
    if (getRowBackgroundProperty() != null) {
      IValueConnector backgroundConnector = ((ICompositeValueConnector) collectionConnector
          .getChildConnector(rowIndex))
          .getChildConnector(getRowBackgroundProperty());
      if (backgroundConnector != null
          && backgroundConnector.getConnectorValue() != null) {
        return backgroundConnector.getConnectorValue().toString();
      }
    }
    return null;
  }

  /**
   * Gets the value to display as row foreground.
   *
   * @param rowIndex
   *          the row index to compute the foreground for.
   * @return the row foreground or null.
   */
  public String getRowForeground(int rowIndex) {
    if (getRowForegroundProperty() != null) {
      IValueConnector foregroundConnector = ((ICompositeValueConnector) collectionConnector
          .getChildConnector(rowIndex))
          .getChildConnector(getRowForegroundProperty());
      if (foregroundConnector != null
          && foregroundConnector.getConnectorValue() != null) {
        return foregroundConnector.getConnectorValue().toString();
      }
    }
    return null;
  }

  /**
   * Gets the value to display as row font.
   *
   * @param rowIndex
   *          the row index to compute the font for.
   * @return the row font or null.
   */
  public String getRowFont(int rowIndex) {
    if (getRowFontProperty() != null) {
      IValueConnector fontConnector = ((ICompositeValueConnector) collectionConnector
          .getChildConnector(rowIndex)).getChildConnector(getRowFontProperty());
      if (fontConnector != null && fontConnector.getConnectorValue() != null) {
        return fontConnector.getConnectorValue().toString();
      }
    }
    return null;
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
                resetRowSelection(i);
              }
            } else {
              fireTableRowsInserted(i, i);
              resetRowSelection(i);
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

  /**
   * Gets the rowBackgroundProperty.
   *
   * @return the rowBackgroundProperty.
   */
  protected String getRowBackgroundProperty() {
    return rowBackgroundProperty;
  }

  /**
   * Sets the rowBackgroundProperty.
   *
   * @param rowBackgroundProperty
   *          the rowBackgroundProperty to set.
   */
  public void setRowBackgroundProperty(String rowBackgroundProperty) {
    this.rowBackgroundProperty = rowBackgroundProperty;
  }

  /**
   * Gets the rowForegroundProperty.
   *
   * @return the rowForegroundProperty.
   */
  protected String getRowForegroundProperty() {
    return rowForegroundProperty;
  }

  /**
   * Sets the rowForegroundProperty.
   *
   * @param rowForegroundProperty
   *          the rowForegroundProperty to set.
   */
  public void setRowForegroundProperty(String rowForegroundProperty) {
    this.rowForegroundProperty = rowForegroundProperty;
  }

  /**
   * Gets the rowFontProperty.
   *
   * @return the rowFontProperty.
   */
  protected String getRowFontProperty() {
    return rowFontProperty;
  }

  /**
   * Sets the rowFontProperty.
   *
   * @param rowFontProperty
   *          the rowFontProperty to set.
   */
  public void setRowFontProperty(String rowFontProperty) {
    this.rowFontProperty = rowFontProperty;
  }
}
