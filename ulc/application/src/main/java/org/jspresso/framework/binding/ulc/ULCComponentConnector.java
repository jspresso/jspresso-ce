/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.ulc;

import org.jspresso.framework.binding.AbstractValueConnector;

import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.util.Color;

/**
 * This abstract class serves as the base class for all ULCComponent connectors.
 * Subclasses can access the ULCComponent using the parametrized method
 * <code>getConnectedULCComponent()</code> which returns the parametrized type
 * of the class.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            The actual class of the subclass of <code>ULCComponent</code>.
 */
public abstract class ULCComponentConnector<E extends ULCComponent> extends
    AbstractValueConnector {

  private E     connectedULCComponent;

  private Color savedForeground;

  /**
   * Constructs a new <code>ULCComponentConnector</code> instance.
   * 
   * @param id
   *            the connector identifier.
   * @param connectedULCComponent
   *            the connected ULCComponent.
   */
  public ULCComponentConnector(String id, E connectedULCComponent) {
    super(id);
    this.connectedULCComponent = connectedULCComponent;
    bindULCComponent();
    readabilityChange();
    writabilityChange();
  }

  /**
   * Turn read-only if not bound.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isWritable() {
    return (getModelConnector() != null) && super.isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readabilityChange() {
    super.readabilityChange();
    if (isReadable()) {
      if (savedForeground != null) {
        getConnectedULCComponent().setForeground(savedForeground);
      }
      savedForeground = null;
    } else if (savedForeground == null) {
      savedForeground = getConnectedULCComponent().getForeground();
      getConnectedULCComponent().setForeground(
          getConnectedULCComponent().getBackground());
    }
  }

  /**
   * Attaches the ULCComponent to the connector.
   */
  protected abstract void bindULCComponent();

  /**
   * Gets the connectedULCComponent.
   * 
   * @return the connectedULCComponent.
   */
  protected E getConnectedULCComponent() {
    return connectedULCComponent;
  }
}
