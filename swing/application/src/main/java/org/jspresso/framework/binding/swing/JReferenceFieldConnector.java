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
import java.util.Collections;

import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.swing.components.JActionField;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.gui.Icon;

/**
 * JReferenceFieldConnector connector.
 *
 * @author Vincent Vandenschrick
 */
public class JReferenceFieldConnector extends JActionFieldConnector implements
    IRenderableCompositeValueConnector {

  private IValueConnector      renderingConnector;
  private final IValueChangeListener renderingListener;

  /**
   * Constructs a new {@code JActionFieldConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected JActionField.
   */
  public JReferenceFieldConnector(String id, JActionField actionField) {
    super(id, actionField);
    renderingListener = new RenderingConnectorListener();
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
   * Unsupported operation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void addChildConnector(String storageKey,
      IValueConnector childConnector) {
    throw new UnsupportedOperationException(
        "Child connectors cannot be added to action field connector");
  }

  /**
   * Unsupported operation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void removeChildConnector(String storageKey) {
    throw new UnsupportedOperationException(
        "Child connectors cannot be removed to action field connector");
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
  public boolean areChildrenWritable() {
    return isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JReferenceFieldConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JReferenceFieldConnector clone(String newConnectorId) {
    JReferenceFieldConnector clonedConnector = (JReferenceFieldConnector) super
        .clone(newConnectorId);
    if (renderingConnector != null) {
      clonedConnector.renderingConnector = renderingConnector.clone();
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getChildConnector(String connectorKey) {
    if (connectorKey.equals(renderingConnector.getId())) {
      return renderingConnector;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChildConnectorCount() {
    if (renderingConnector != null) {
      return 1;
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getChildConnectorKeys() {
    if (renderingConnector != null) {
      return Collections.singleton(renderingConnector.getId());
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayDescription() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getDisplayIcon() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayValue() {
    if (getRenderingConnector() != null) {
      return getRenderingConnector().getConnectorValue();
    }
    return "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getRenderingConnector() {
    return renderingConnector;
  }

  /**
   * Sets the renderingConnector.
   *
   * @param renderingConnector
   *          the renderingConnector to set.
   */
  public void setRenderingConnector(IValueConnector renderingConnector) {
    if (this.renderingConnector != null) {
      this.renderingConnector.removeValueChangeListener(renderingListener);
    }
    this.renderingConnector = renderingConnector;
    if (this.renderingConnector != null) {
      this.renderingConnector.addValueChangeListener(renderingListener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getActionText() {
    if (renderingConnector != null) {
      if (renderingConnector.getConnectorValue() == null) {
        return "";
      }
      return renderingConnector.getConnectorValue().toString();
    }
    return super.getActionText();
  }

  private final class RenderingConnectorListener implements
      IValueChangeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(ValueChangeEvent evt) {
      protectedSetConnecteeValue(getConnecteeValue());
    }
  }
}
