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
    AbstractValueConnector implements IRenderableCompositeValueConnector,
    IConnectorMapProvider {

  private IConnectorMap             childConnectors;
  private ChildConnectorSupport     childConnectorSupport;
  private ConnectorSelectionSupport connectorSelectionSupport;
  private String                    renderingChildConnectorId;
  private boolean                   trackingChildrenSelection;
  private String                    displayValue;
  private String                    displayDescription;
  private String                    displayIconImageUrl;
  private IIconImageURLProvider     iconImageURLProvider;

  /**
   * Constructs a new <code>AbstractCompositeValueConnector</code>.
   * 
   * @param id
   *          the connector identifier
   */
  public AbstractCompositeValueConnector(String id) {
    super(id);
    childConnectorSupport = new ChildConnectorSupport(this);
    connectorSelectionSupport = new ConnectorSelectionSupport();
    trackingChildrenSelection = false;
    childConnectors = new ConnectorMap(this);
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
    clonedConnector.childConnectors = new ConnectorMap(clonedConnector);
    clonedConnector.childConnectorSupport = new ChildConnectorSupport(
        clonedConnector);
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
    return childConnectorSupport.getChildConnector(connectorKey);
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
    return childConnectorSupport.getChildConnectorKeys();
  }

  /**
   * Returns the connector map of the connectors contained in this connector.
   * This method should return null if no connector are child in this connector.
   * 
   * @return the connector map of the child connectors
   */
  public IConnectorMap getConnectorMap() {
    return childConnectors;
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
    Object value = getConnectorValue();
    if (value != null) {
      return value.toString();
    }
    if (displayValue != null) {
      return displayValue;
    }
    return "";
  }

  /**
   * Gets the static icon image url or uses the icon image url provider to
   * compute it based on the connector value.
   * <p>
   * {@inheritDoc}
   */
  public String getDisplayIconImageUrl() {
    if (displayIconImageUrl != null) {
      return displayIconImageUrl;
    }
    if (iconImageURLProvider != null) {
      return iconImageURLProvider.getIconImageURLForObject(getConnectorValue());
    }
    return null;
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
    getConnectorMap().addConnector(storageKey, connector);
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
    getConnectorMap().removeConnector(connector.getId());
    connector.setParentConnector(null);
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
   * Sets the static displayIconImageUrl.
   * 
   * @param displayIconImageUrl
   *          the displayIconImageUrl to set.
   */
  public void setDisplayIconImageUrl(String displayIconImageUrl) {
    this.displayIconImageUrl = displayIconImageUrl;
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
   * Sets the displayDescription.
   * 
   * @param displayDescription the displayDescription to set.
   */
  public void setDisplayDescription(String displayDescription) {
    this.displayDescription = displayDescription;
  }

  /**
   * Gets the displayDescription.
   * 
   * @return the displayDescription.
   */
  public String getDisplayDescription() {
    return displayDescription;
  }

}
