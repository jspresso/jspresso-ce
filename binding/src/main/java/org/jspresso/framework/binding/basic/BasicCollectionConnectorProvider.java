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
package org.jspresso.framework.binding.basic;

import java.util.Collections;
import java.util.List;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;

/**
 * A composite connector holding a reference on a collection connector to easyly
 * play a role in a master/detail relationship.
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
public class BasicCollectionConnectorProvider extends BasicCompositeConnector
    implements IConfigurableCollectionConnectorProvider, IItemSelectable {

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
  public BasicCollectionConnectorProvider clone() {
    return clone(getId());
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
  public void fireSelectedItemChange(ItemSelectionEvent evt) {
    implFireSelectedItemChange(evt);
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
  public void removeItemSelectionListener(IItemSelectionListener listener) {
    implRemoveConnectorSelectionListener(listener);
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
  public void setTracksChildrenSelection(boolean tracksChildrenSelection) {
    implSetTracksChildrenSelection(tracksChildrenSelection);
  }
}
