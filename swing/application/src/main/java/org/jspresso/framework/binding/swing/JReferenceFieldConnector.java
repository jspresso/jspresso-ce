/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

/**
 * JReferenceFieldConnector connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JReferenceFieldConnector extends JActionFieldConnector implements
    IRenderableCompositeValueConnector {

  private IValueConnector      renderingConnector;
  private IValueChangeListener renderingListener;

  /**
   * Constructs a new <code>JActionFieldConnector</code> instance.
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
    public void valueChange(@SuppressWarnings("unused") ValueChangeEvent evt) {
      protectedSetConnecteeValue(getConnecteeValue());
    }
  }
}
