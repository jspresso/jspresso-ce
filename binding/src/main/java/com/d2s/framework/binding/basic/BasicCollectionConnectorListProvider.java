/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.binding.ConnectorSelectionEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IConfigurableCollectionConnectorListProvider;
import com.d2s.framework.binding.IConnectorSelectionListener;
import com.d2s.framework.binding.IConnectorSelector;

/**
 * A composite connector holding a reference on a collection connector to easyly
 * play a role in a parent/children relationship.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCollectionConnectorListProvider extends
    BasicCompositeConnector implements
    IConfigurableCollectionConnectorListProvider, IConnectorSelector {

  private List<ICollectionConnectorProvider> collectionConnectorProviders;

  /**
   * Constructs a new <code>BasicCollectionConnectorProvider</code> instance.
   * 
   * @param id
   *          the connector identifier.
   */
  public BasicCollectionConnectorListProvider(String id) {
    super(id);
  }

  /**
   * {@inheritDoc}
   */
  public List<ICollectionConnector> getCollectionConnectors() {
    List<ICollectionConnector> collectionConnectors = new ArrayList<ICollectionConnector>();
    if (collectionConnectorProviders != null) {
      for (ICollectionConnectorProvider collectionConnectorProvider : collectionConnectorProviders) {
        collectionConnectors.add(collectionConnectorProvider
            .getCollectionConnector());
      }
    }
    return collectionConnectors;
  }

  /**
   * {@inheritDoc}
   */
  public void setCollectionConnectorProviders(
      List<ICollectionConnectorProvider> collectionConnectorProviders) {
    this.collectionConnectorProviders = collectionConnectorProviders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnectorListProvider clone(String newConnectorId) {
    BasicCollectionConnectorListProvider clonedConnector = (BasicCollectionConnectorListProvider) super
        .clone(newConnectorId);
    if (collectionConnectorProviders != null) {
      clonedConnector.collectionConnectorProviders = new ArrayList<ICollectionConnectorProvider>();
      for (ICollectionConnectorProvider collectionConnectorProvider : collectionConnectorProviders) {
        clonedConnector.collectionConnectorProviders
            .add((ICollectionConnectorProvider) clonedConnector
                .getChildConnector(collectionConnectorProvider.getId()));
      }
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnectorListProvider clone() {
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
