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
package org.jspresso.framework.binding;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jspresso.framework.util.IIconImageURLProvider;

/**
 * This is a simple connector implementation whic allows the management of child
 * connectors. It can be used for straight view connector implementation.
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

public abstract class AbstractCompositeValueConnector extends
    AbstractValueConnector implements IRenderableCompositeValueConnector {

  private Map<String, IValueConnector> childConnectors;
  private ConnectorSelectionSupport    connectorSelectionSupport;
  private String                       displayDescription;
  private String                       displayIconImageUrl;
  private String                       displayValue;
  private IIconImageURLProvider        iconImageURLProvider;
  private String                       renderingChildConnectorId;
  private boolean                      trackingChildrenSelection;

  /**
   * Constructs a new <code>AbstractCompositeValueConnector</code>.
   * 
   * @param id
   *          the connector identifier
   */
  public AbstractCompositeValueConnector(String id) {
    super(id);
    connectorSelectionSupport = new ConnectorSelectionSupport();
    trackingChildrenSelection = false;
    childConnectors = new LinkedHashMap<String, IValueConnector>();
  }

  /**
   * Adds a new child connector.
   * 
   * @param connector
   *          the connector to be added as composite.
   */
  public void addChildConnector(IValueConnector connector) {
    addChildConnector(connector.getId(), connector);
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenReadable() {
    return isReadable();
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenWritable() {
    return isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractCompositeValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractCompositeValueConnector clone(String newConnectorId) {
    AbstractCompositeValueConnector clonedConnector = (AbstractCompositeValueConnector) super
        .clone(newConnectorId);
    clonedConnector.childConnectors = new LinkedHashMap<String, IValueConnector>();
    clonedConnector.connectorSelectionSupport = new ConnectorSelectionSupport();
    for (String connectorKey : getChildConnectorKeys()) {
      clonedConnector
          .addChildConnector(getChildConnector(connectorKey).clone());
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    return childConnectors.get(connectorKey);
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    return getChildConnectorKeys().size();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    return childConnectors.keySet();
  }

  /**
   * Gets the displayDescription.
   * 
   * @return the displayDescription.
   */
  public String getDisplayDescription() {
    return displayDescription;
  }

  /**
   * Gets the static icon image url or uses the icon image url provider to
   * compute it based on the connector value.
   * <p>
   * {@inheritDoc}
   */
  public String getDisplayIconImageUrl() {
    String iconImageUrl = null;
    if (iconImageURLProvider != null) {
      iconImageUrl = iconImageURLProvider
          .getIconImageURLForObject(getConnectorValue());
    }
    if (iconImageUrl == null) {
      iconImageUrl = displayIconImageUrl;
    }
    return iconImageUrl;
  }

  /**
   * Gets the static display value or uses the rendering connector to compute
   * the string representation.
   * <p>
   * {@inheritDoc}
   */
  public String getDisplayValue() {
    IValueConnector renderingConnector = getRenderingConnector();
    if (renderingConnector != null) {
      if (renderingConnector.getConnectorValue() != null) {
        return renderingConnector.getConnectorValue().toString();
      }
      return "";
    }
    if (displayValue != null) {
      return displayValue;
    }
    Object value = getConnectorValue();
    if (value != null) {
      return value.toString();
    }
    return "";
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getRenderingConnector() {
    if (renderingChildConnectorId != null) {
      return getChildConnector(renderingChildConnectorId);
    }
    return null;
  }

  /**
   * Sets the displayDescription.
   * 
   * @param displayDescription
   *          the displayDescription to set.
   */
  public void setDisplayDescription(String displayDescription) {
    this.displayDescription = displayDescription;
  }

  /**
   * Sets the static displayIconImageUrl.
   * 
   * @param displayIconImageUrl
   *          the displayIconImageUrl to set.
   */
  public void setDisplayIconImageUrl(String displayIconImageUrl) {
    this.displayIconImageUrl = displayIconImageUrl;
  }

  /**
   * Sets the static displayValue.
   * 
   * @param displayValue
   *          the displayValue to set.
   */
  public void setDisplayValue(String displayValue) {
    this.displayValue = displayValue;
  }

  /**
   * Sets the iconImageURLProvider.
   * 
   * @param iconImageURLProvider
   *          the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Sets the renderingChildConnectorId.
   * 
   * @param renderingChildConnectorId
   *          the renderingChildConnectorId to set.
   */
  public void setRenderingChildConnectorId(String renderingChildConnectorId) {
    this.renderingChildConnectorId = renderingChildConnectorId;
  }

  /**
   * Uses the value to compute the string representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getDisplayValue();
  }

  /**
   * Adds a new child connector using a specified storage key.
   * 
   * @param storageKey
   *          the key to use to store the child connector. It may be different
   *          from its id.
   * @param connector
   *          the connector to be added as composite.
   */
  protected void addChildConnector(String storageKey, IValueConnector connector) {
    childConnectors.put(storageKey, connector);
    connector.setParentConnector(this);
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param listener
   *          the listener to add.
   */
  protected void implAddConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    connectorSelectionSupport.addConnectorSelectionListener(listener);
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param evt
   *          the connector selection event to propagate.
   */
  protected void implFireSelectedConnectorChange(ConnectorSelectionEvent evt) {
    if (evt.getSource() == this || trackingChildrenSelection) {
      connectorSelectionSupport.fireSelectedConnectorChange(evt);
    }
    IValueConnector parentConnector = getParentConnector();
    while (parentConnector != null
        && !(parentConnector instanceof IConnectorSelector)) {
      parentConnector = parentConnector.getParentConnector();
    }
    if (parentConnector != null) {
      ((IConnectorSelector) parentConnector).fireSelectedConnectorChange(evt);
    }
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param selectedConnector
   *          the newly selected connector or null.
   */
  protected void implFireSelectedConnectorChange(
      IValueConnector selectedConnector) {
    implFireSelectedConnectorChange(new ConnectorSelectionEvent(this,
        selectedConnector));
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param listener
   *          the listener to remove.
   */
  protected void implRemoveConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    connectorSelectionSupport.removeConnectorSelectionListener(listener);
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param tracksChildren
   *          the trackingChildrenSelection to set.
   */
  protected void implSetTracksChildrenSelection(boolean tracksChildren) {
    this.trackingChildrenSelection = tracksChildren;
  }

  /**
   * Gets the trackingChildrenSelection.
   * 
   * @return the trackingChildrenSelection.
   */
  protected boolean isTrackingChildrenSelection() {
    return trackingChildrenSelection;
  }

  /**
   * Removes a child connector.
   * 
   * @param connector
   *          the connector to be removed.
   */
  protected void removeChildConnector(IValueConnector connector) {
    IValueConnector removedConnector = childConnectors
        .remove(connector.getId());
    if (removedConnector != null) {
      removedConnector.setParentConnector(null);
    }
  }

}
