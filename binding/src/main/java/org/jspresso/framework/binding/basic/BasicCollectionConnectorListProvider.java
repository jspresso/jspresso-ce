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
package org.jspresso.framework.binding.basic;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;

/**
 * A composite connector holding a reference on a collection connector to easily
 * play a role in a parent/children relationship.
 *
 * @author Vincent Vandenschrick
 */
public class BasicCollectionConnectorListProvider extends
    BasicCompositeConnector implements
    IConfigurableCollectionConnectorListProvider, IItemSelectable {

  private List<ICollectionConnectorProvider> collectionConnectorProviders;

  /**
   * Constructs a new {@code BasicCollectionConnectorProvider} instance.
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
  @Override
  public void addItemSelectionListener(IItemSelectionListener listener) {
    implAddConnectorSelectionListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void boundAsView() {
    super.boundAsView();
    if (isTrackingChildrenSelection()) {
      implFireSelectedConnectorChange(this);
    }
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
  @Override
  public BasicCollectionConnectorListProvider clone(String newConnectorId) {
    BasicCollectionConnectorListProvider clonedConnector = (BasicCollectionConnectorListProvider) super
        .clone(newConnectorId);
    if (collectionConnectorProviders != null) {
      clonedConnector.collectionConnectorProviders = new ArrayList<>();
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
  public void fireSelectedItemChange(ItemSelectionEvent evt) {
    implFireSelectedItemChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ICollectionConnector> getCollectionConnectors() {
    List<ICollectionConnector> collectionConnectors = new ArrayList<>();
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
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getSelectedItem() {
    return (T) implGetSelectedItem();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeItemSelectionListener(IItemSelectionListener listener) {
    implRemoveConnectorSelectionListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCollectionConnectorProviders(
      List<ICollectionConnectorProvider> collectionConnectorProviders) {
    this.collectionConnectorProviders = collectionConnectorProviders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTracksChildrenSelection(boolean tracksChildrenSelection) {
    implSetTracksChildrenSelection(tracksChildrenSelection);
  }
}
