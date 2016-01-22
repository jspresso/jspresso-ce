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
package org.jspresso.framework.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.event.ItemSelectionSupport;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.gui.IconProvider;

/**
 * This is a simple connector implementation which allows the management of child
 * connectors. It can be used for straight view connector implementation.
 *
 * @author Vincent Vandenschrick
 */

public abstract class AbstractCompositeValueConnector extends
    AbstractValueConnector implements IRenderableCompositeValueConnector {

  private Map<String, IValueConnector> childConnectors;
  private Collection<String>           childConnectorKeys;
  private String                       displayDescription;
  private Icon                         displayIcon;
  private String                       displayValue;
  private IconProvider                 iconImageURLProvider;
  private ItemSelectionSupport         itemSelectionSupport;
  private String                       renderingChildConnectorId;
  private Object                       selectedItem;
  private boolean                      trackingChildrenSelection;

  /**
   * Constructs a new {@code AbstractCompositeValueConnector}.
   *
   * @param id
   *          the connector identifier
   */
  public AbstractCompositeValueConnector(String id) {
    super(id);
    trackingChildrenSelection = false;
    initChildStructureIfNecessary();
  }

  private void initChildStructureIfNecessary() {
    if (childConnectors == null) {
      childConnectors = new THashMap<>();
      childConnectorKeys = new ArrayList<>();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean areChildrenReadable() {
    return isReadable();
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
    clonedConnector.childConnectors = null;
    clonedConnector.childConnectorKeys = null;
    clonedConnector.itemSelectionSupport = null;
    for (String connectorKey : getChildConnectorKeys()) {
      clonedConnector.addChildConnector(connectorKey,
          getChildConnector(connectorKey).clone());
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getChildConnector(String connectorKey) {
    if (childConnectors == null) {
      return null;
    }
    return childConnectors.get(connectorKey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChildConnectorCount() {
    if (childConnectorKeys == null) {
      return 0;
    }
    return childConnectorKeys.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getChildConnectorKeys() {
    if (childConnectorKeys == null) {
      return Collections.emptyList();
    }
    return new ArrayList<>(childConnectorKeys);
  }

  /**
   * Gets the displayDescription.
   *
   * @return the displayDescription.
   */
  @Override
  public String getDisplayDescription() {
    return displayDescription;
  }

  /**
   * Gets the static icon or uses the icon provider to compute it based on the
   * connector value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Icon getDisplayIcon() {
    Icon icon;
    if (iconImageURLProvider != null) {
      icon = iconImageURLProvider.getIconForObject(getConnectorValue());
    } else {
      icon = displayIcon;
    }
    return icon;
  }

  /**
   * Gets the static display value or uses the rendering connector to compute
   * the string representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getDisplayValue() {
    IValueConnector renderingConnector = getRenderingConnector();
    if (renderingConnector != null) {
      if (renderingConnector.getConnectorValue() != null) {
        return renderingConnector.getConnectorValue().toString();
      }
      return "";
    }
    // if (displayValue != null) {
    // return displayValue;
    // }
    // Object value = getConnectorValue();
    // if (value != null) {
    // return value.toString();
    // }
    return displayValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getRenderingConnector() {
    if (renderingChildConnectorId != null) {
      return getChildConnector(renderingChildConnectorId);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readabilityChange() {
    super.writabilityChange();
    for (String key : getChildConnectorKeys()) {
      getChildConnector(key).readabilityChange();
    }
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
   * Sets the static displayIcon.
   *
   * @param displayIcon
   *          the displayIcon to set.
   */
  public void setDisplayIcon(Icon displayIcon) {
    this.displayIcon = displayIcon;
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
  public void setIconImageURLProvider(IconProvider iconImageURLProvider) {
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
   * {@inheritDoc}
   */
  @Override
  public void writabilityChange() {
    super.writabilityChange();
    for (String key : getChildConnectorKeys()) {
      getChildConnector(key).writabilityChange();
    }
  }

  /**
   * Adds a new child connector to this composite. The key used as storage key
   * is the child connector id.
   *
   * @param childConnector
   *          the added connector.
   */
  @Override
  public final void addChildConnector(IValueConnector childConnector) {
    addChildConnector(childConnector.getId(), childConnector);
  }

  /**
   * Adds a new child connector using a specified storage key.
   *
   * @param storageKey
   *          the key to use to store the child connector. It may be different
   *          from its id.
   * @param childConnector
   *          the connector to be added as composite.
   */
  @Override
  public void addChildConnector(String storageKey,
      IValueConnector childConnector) {
    initChildStructureIfNecessary();
    childConnectors.put(storageKey, childConnector);
    childConnector.setParentConnector(this);
    childConnectorKeys.add(storageKey);
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement {@code IConnectorSelector}.
   *
   * @param listener
   *          the listener to add.
   */
  protected void implAddConnectorSelectionListener(
      IItemSelectionListener listener) {
    if (itemSelectionSupport == null) {
      itemSelectionSupport = new ItemSelectionSupport();
    }
    itemSelectionSupport.addItemSelectionListener(listener);
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement {@code IConnectorSelector}.
   *
   * @param selectedConnector
   *          the newly selected connector or null.
   */
  protected void implFireSelectedConnectorChange(
      IValueConnector selectedConnector) {
    implFireSelectedItemChange(new ItemSelectionEvent(this, selectedConnector));
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement {@code IItemSelectable}.
   *
   * @param evt
   *          the item selection event to propagate.
   */
  protected void implFireSelectedItemChange(ItemSelectionEvent evt) {
    selectedItem = evt.getSelectedItem();
    if (itemSelectionSupport != null && (evt.getSource() == this || isTrackingChildrenSelection())) {
      itemSelectionSupport.fireSelectedConnectorChange(evt);
    }
    IValueConnector parentConnector = getParentConnector();
    while (parentConnector != null
        && !(parentConnector instanceof IItemSelectable)) {
      parentConnector = parentConnector.getParentConnector();
    }
    if (parentConnector != null) {
      ((IItemSelectable) parentConnector).fireSelectedItemChange(evt);
    }
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement {@code IItemSelectable}.
   *
   * @return the selected item.
   */
  protected Object implGetSelectedItem() {
    return selectedItem;
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement {@code IConnectorSelector}.
   *
   * @param listener
   *          the listener to remove.
   */
  protected void implRemoveConnectorSelectionListener(
      IItemSelectionListener listener) {
    if (itemSelectionSupport != null) {
      itemSelectionSupport.removeConnectorSelectionListener(listener);
    }
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement {@code IConnectorSelector}.
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
   * {@inheritDoc}
   */
  @Override
  public void removeChildConnector(String storageKey) {
    if (childConnectors != null) {
      childConnectors.remove(storageKey);
      childConnectorKeys.remove(storageKey);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void propagateRollback() {
    Object badValue = getConnectorValue();
    super.propagateRollback();
    if (badValue == null) {
      IValueConnector renderingConnector = getRenderingConnector();
      if (renderingConnector instanceof AbstractValueConnector) {
        ((AbstractValueConnector) renderingConnector)
            .fireValueChange(new ValueChangeEvent(renderingConnector, null,
                renderingConnector.getConnectorValue()));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void recycle(IMvcBinder mvcBinder) {
    super.recycle(mvcBinder);
    // Keep displayIconImageUrl since it is set statically at creation time. So
    // it does not support recycling.
    // displayIconImageUrl = null;
    displayDescription = null;
    displayValue = null;
  }
}
