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
import java.util.Collections;

import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.wings.components.SActionField;

/**
 * SReferenceFieldConnector connector.
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
public class SReferenceFieldConnector extends SActionFieldConnector implements
    IRenderableCompositeValueConnector {

  private IConnectorValueChangeListener renderingListener;
  private IValueConnector               renderingConnector;

  /**
   * Constructs a new <code>SActionFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected SActionField.
   */
  public SReferenceFieldConnector(String id, SActionField actionField) {
    super(id, actionField);
    renderingListener = new RenderingConnectorListener();
  }

  /**
   * {@inheritDoc}
   */
  public void addChildConnector(
      @SuppressWarnings("unused") IValueConnector childConnector) {
    throw new UnsupportedOperationException(
        "Child connectors cannot be added to action field connector");
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
  public SReferenceFieldConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SReferenceFieldConnector clone(String newConnectorId) {
    SReferenceFieldConnector clonedConnector = (SReferenceFieldConnector) super
        .clone(newConnectorId);
    if (renderingConnector != null) {
      clonedConnector.renderingConnector = renderingConnector.clone();
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    if (connectorKey.equals(renderingConnector.getId())) {
      return renderingConnector;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    if (renderingConnector != null) {
      return 1;
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    if (renderingConnector != null) {
      return Collections.singleton(renderingConnector.getId());
    }
    return null;
  }

  /**
   * Sets the renderingConnector.
   * 
   * @param renderingConnector
   *          the renderingConnector to set.
   */
  public void setRenderingConnector(IValueConnector renderingConnector) {
    if (this.renderingConnector != null) {
      this.renderingConnector
          .removeConnectorValueChangeListener(renderingListener);
    }
    this.renderingConnector = renderingConnector;
    if (this.renderingConnector != null) {
      this.renderingConnector
          .addConnectorValueChangeListener(renderingListener);
    }
  }

  private final class RenderingConnectorListener implements
      IConnectorValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(
        @SuppressWarnings("unused") ConnectorValueChangeEvent evt) {
      setConnecteeValue(getConnecteeValue());
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

  /**
   * {@inheritDoc}
   */
  public String getDisplayDescription() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public String getDisplayIconImageUrl() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public String getDisplayValue() {
    if (getRenderingConnector() != null) {
      return (String) getRenderingConnector().getConnectorValue();
    }
    return "";
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getRenderingConnector() {
    return renderingConnector;
  }
}
