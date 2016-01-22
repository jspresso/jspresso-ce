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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractListModel;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * This class implements a list model backed by a collection connector. As
 * expected, this list model will fire necessary events depending on connectors
 * received events.
 *
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorListModel extends AbstractListModel<IValueConnector> {

  private static final long                  serialVersionUID = -7992011455793793550L;
  private Map<Integer, IValueChangeListener> cachedListeners;
  private final ICollectionConnector               collectionConnector;

  /**
   * Constructs a new {@code CollectionConnectorListModel} instance.
   *
   * @param collectionConnector
   *          the collection connector holding the values of this list model.
   */
  public CollectionConnectorListModel(ICollectionConnector collectionConnector) {
    super();
    this.collectionConnector = collectionConnector;
    bindConnector();
  }

  /**
   * Returns the connector at {@code index} in the child connectors
   * collection contained in the backed collection connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getElementAt(int index) {
    return collectionConnector.getChildConnector(index);
  }

  /**
   * Returns the backed collection connector size.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int getSize() {
    return collectionConnector.getChildConnectorCount();
  }

  private void bindChildConnector(int index) {
    IValueConnector cellConnector = collectionConnector
        .getChildConnector(index);
    if (cellConnector instanceof IRenderableCompositeValueConnector
        && ((IRenderableCompositeValueConnector) cellConnector)
            .getRenderingConnector() != null) {
      ((IRenderableCompositeValueConnector) cellConnector)
          .getRenderingConnector().addValueChangeListener(
              getChildConnectorListener(index));
    } else {
      cellConnector.addValueChangeListener(getChildConnectorListener(index));
    }
  }

  private void bindConnector() {
    collectionConnector.addValueChangeListener(new ListConnectorListener());
    for (int index = 0; index < collectionConnector.getChildConnectorKeys()
        .size(); index++) {
      bindChildConnector(index);
    }
  }

  private IValueChangeListener getChildConnectorListener(int index) {
    if (cachedListeners == null) {
      cachedListeners = new HashMap<>();
    }
    IValueChangeListener cachedListener = cachedListeners
        .get(index);
    if (cachedListener == null) {
      cachedListener = new CellConnectorListener(index);
      cachedListeners.put(index, cachedListener);
    }
    return cachedListener;
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

  private final class CellConnectorListener implements IValueChangeListener {

    private final int index;

    private CellConnectorListener(int index) {
      this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(final ValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          fireContentsChanged(CollectionConnectorListModel.this, index, index);
        }
      });
    }
  }

  private class ListConnectorListener implements IValueChangeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(final ValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          Collection<?> oldCollection = (Collection<?>) evt.getOldValue();
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
            fireIntervalAdded(CollectionConnectorListModel.this,
                oldCollectionSize, newCollectionSize);
            for (int index = oldCollectionSize; index < newCollectionSize; index++) {
              bindChildConnector(index);
            }
          } else if (newCollectionSize < oldCollectionSize) {
            fireIntervalRemoved(CollectionConnectorListModel.this,
                oldCollectionSize, newCollectionSize);
          }
        }
      });
    }
  }
}
