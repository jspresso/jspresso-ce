/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IRenderableCompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.ulcjava.base.application.AbstractListModel;

/**
 * This class implements a list model backed by a collection connector. As
 * expected, this list model will fire necessary events depending on connectors
 * received events.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorListModel extends AbstractListModel {

  private static final long                           serialVersionUID = -7992011455793793550L;
  private ICollectionConnector                        collectionConnector;
  private Map<Integer, IConnectorValueChangeListener> cachedListeners;

  /**
   * Constructs a new <code>CollectionConnectorListModel</code> instance.
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
   * Returns the backed collection connector size.
   * <p>
   * {@inheritDoc}
   */
  public int getSize() {
    return collectionConnector.getChildConnectorCount();
  }

  /**
   * Returns the connector value of the connector at <code>index</code> in the
   * child connectors collection contained in the backed collection connector.
   * <p>
   * {@inheritDoc}
   */
  public Object getElementAt(int index) {
    IValueConnector cellConnector = collectionConnector
        .getChildConnector(index);
    if (cellConnector instanceof ICompositeValueConnector) {
      return cellConnector.toString();
    }
    return cellConnector.getConnectorValue();
  }

  private void bindConnector() {
    collectionConnector
        .addConnectorValueChangeListener(new ListConnectorListener());
    for (int index = 0; index < collectionConnector.getChildConnectorKeys()
        .size(); index++) {
      bindChildConnector(index);
    }
  }

  private void bindChildConnector(int index) {
    IValueConnector cellConnector = collectionConnector
        .getChildConnector(index);
    if (cellConnector instanceof IRenderableCompositeValueConnector
        && ((IRenderableCompositeValueConnector) cellConnector)
            .getRenderingConnector() != null) {
      ((IRenderableCompositeValueConnector) cellConnector)
          .getRenderingConnector().addConnectorValueChangeListener(
              getChildConnectorListener(index));
    } else {
      cellConnector
          .addConnectorValueChangeListener(getChildConnectorListener(index));
    }
  }

  private IConnectorValueChangeListener getChildConnectorListener(int index) {
    if (cachedListeners == null) {
      cachedListeners = new HashMap<Integer, IConnectorValueChangeListener>();
    }
    IConnectorValueChangeListener cachedListener = cachedListeners
        .get(new Integer(index));
    if (cachedListener == null) {
      cachedListener = new CellConnectorListener(index);
      cachedListeners.put(new Integer(index), cachedListener);
    }
    return cachedListener;
  }

  private class ListConnectorListener implements IConnectorValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(final ConnectorValueChangeEvent evt) {
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

  private final class CellConnectorListener implements
      IConnectorValueChangeListener {

    private int index;

    private CellConnectorListener(int index) {
      this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(@SuppressWarnings("unused")
    ConnectorValueChangeEvent evt) {
      fireContentsChanged(CollectionConnectorListModel.this, index, index);
    }
  }
}
