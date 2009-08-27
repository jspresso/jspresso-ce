/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.wings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractListModel;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;


/**
 * This class implements a list model backed by a collection connector. As
 * expected, this list model will fire necessary events depending on connectors
 * received events.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorListModel extends AbstractListModel {

  private static final long                           serialVersionUID = -7992011455793793550L;
  private Map<Integer, IValueChangeListener> cachedListeners;
  private ICollectionConnector                        collectionConnector;

  /**
   * Constructs a new <code>CollectionConnectorListModel</code> instance.
   * 
   * @param collectionConnector
   *            the collection connector holding the values of this list model.
   */
  public CollectionConnectorListModel(ICollectionConnector collectionConnector) {
    super();
    this.collectionConnector = collectionConnector;
    bindConnector();
  }

  /**
   * Returns the connector value of the connector at <code>index</code> in the
   * child connectors collection contained in the backed collection connector.
   * <p>
   * {@inheritDoc}
   */
  public Object getElementAt(int index) {
    return collectionConnector.getChildConnector(index).getConnectorValue();
  }

  /**
   * Returns the backed collection connector size.
   * <p>
   * {@inheritDoc}
   */
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
      cellConnector
          .addValueChangeListener(getChildConnectorListener(index));
    }
  }

  private void bindConnector() {
    collectionConnector
        .addValueChangeListener(new ListConnectorListener());
    for (int index = 0; index < collectionConnector.getChildConnectorKeys()
        .size(); index++) {
      bindChildConnector(index);
    }
  }

  private IValueChangeListener getChildConnectorListener(int index) {
    if (cachedListeners == null) {
      cachedListeners = new HashMap<Integer, IValueChangeListener>();
    }
    IValueChangeListener cachedListener = cachedListeners
        .get(new Integer(index));
    if (cachedListener == null) {
      cachedListener = new CellConnectorListener(index);
      cachedListeners.put(new Integer(index), cachedListener);
    }
    return cachedListener;
  }

  private final class CellConnectorListener implements
      IValueChangeListener {

    private int index;

    private CellConnectorListener(int index) {
      this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    public void valueChange(@SuppressWarnings("unused")
    ValueChangeEvent evt) {
      fireContentsChanged(CollectionConnectorListModel.this, index, index);
    }
  }

  private class ListConnectorListener implements IValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void valueChange(final ValueChangeEvent evt) {
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
        fireIntervalAdded(CollectionConnectorListModel.this, oldCollectionSize,
            newCollectionSize);
        for (int index = oldCollectionSize; index < newCollectionSize; index++) {
          bindChildConnector(index);
        }
      } else if (newCollectionSize < oldCollectionSize) {
        fireIntervalRemoved(CollectionConnectorListModel.this,
            oldCollectionSize, newCollectionSize);
      }
    }
  }
}
