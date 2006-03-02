/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.basic;

import java.util.Collections;
import java.util.List;

import com.d2s.framework.binding.ConnectorSelectionEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IConfigurableCollectionConnectorProvider;
import com.d2s.framework.binding.IConnectorSelectionListener;
import com.d2s.framework.binding.IConnectorSelector;

/**
 * A composite connector holding a reference on a collection connector to easyly
 * play a role in a master/detail relationship.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCollectionConnectorProvider extends BasicCompositeConnector
    implements IConfigurableCollectionConnectorProvider, IConnectorSelector {

  private ICollectionConnectorProvider collectionConnectorProvider;

  /**
   * Constructs a new <code>BasicCollectionConnectorProvider</code> instance.
   * 
   * @param id
   *          the connector identifier.
   */
  public BasicCollectionConnectorProvider(String id) {
    super(id);
  }

  /**
   * {@inheritDoc}
   */
  public ICollectionConnector getCollectionConnector() {
    if (collectionConnectorProvider != null) {
      return collectionConnectorProvider.getCollectionConnector();
    }
    return null;
  }

  /**
   * Returns singleton list of this.
   * <p>
   * {@inheritDoc}
   */
  public List<ICollectionConnector> getCollectionConnectors() {
    if (collectionConnectorProvider != null) {
      return Collections.singletonList(collectionConnectorProvider
          .getCollectionConnector());
    }
    return Collections.emptyList();
  }

  /**
   * {@inheritDoc}
   */
  public void setCollectionConnectorProvider(
      ICollectionConnectorProvider collectionConnectorProvider) {
    this.collectionConnectorProvider = collectionConnectorProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnectorProvider clone(String newConnectorId) {
    BasicCollectionConnectorProvider clonedConnector = (BasicCollectionConnectorProvider) super
        .clone(newConnectorId);
    if (collectionConnectorProvider != null) {
      clonedConnector.collectionConnectorProvider = (ICollectionConnectorProvider) clonedConnector
          .getChildConnector(collectionConnectorProvider.getId());
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnectorProvider clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  public void addConnectorSelectionListener(IConnectorSelectionListener listener) {
    implAddConnectorSelectionListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void fireSelectedConnectorChange(ConnectorSelectionEvent evt) {
    implFireSelectedConnectorChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  public void removeConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    implRemoveConnectorSelectionListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void setTracksChildrenSelection(boolean tracksChildrenSelection) {
    implSetTracksChildrenSelection(tracksChildrenSelection);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void boundAsView() {
    super.boundAsView();
    if (isTracksChildrenSelection()) {
      implFireSelectedConnectorChange(this);
    }
  }
}
