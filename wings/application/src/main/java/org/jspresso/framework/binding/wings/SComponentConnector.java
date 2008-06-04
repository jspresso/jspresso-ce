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

import java.awt.Color;

import org.jspresso.framework.binding.AbstractValueConnector;
import org.wings.SComponent;


/**
 * This abstract class serves as the base class for all SComponent connectors.
 * Subclasses can access the SComponent using the parametrized method
 * <code>getConnectedSComponent()</code> which returns the parametrized type
 * of the class.
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
 * @param <E>
 *            The actual class of the subclass of <code>SComponent</code>.
 */
public abstract class SComponentConnector<E extends SComponent> extends
    AbstractValueConnector {

  private E     connectedSComponent;

  private Color savedForeground;

  /**
   * Constructs a new <code>SComponentConnector</code> instance.
   * 
   * @param id
   *            the connector identifier.
   * @param connectedSComponent
   *            the connected SComponent.
   */
  public SComponentConnector(String id, E connectedSComponent) {
    super(id);
    this.connectedSComponent = connectedSComponent;
    bindSComponent();
    updateState();
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
  public void updateState() {
    if (isReadable()) {
      if (savedForeground != null) {
        getConnectedSComponent().setForeground(savedForeground);
      }
      savedForeground = null;
    } else if (savedForeground == null) {
      savedForeground = getConnectedSComponent().getForeground();
      getConnectedSComponent().setForeground(
          getConnectedSComponent().getBackground());
    }
  }

  /**
   * Attaches the SComponent to the connector.
   */
  protected abstract void bindSComponent();

  /**
   * This method has been overriden to take care of long-running operations not
   * to have the swing gui blocked. It uses the foxtrot library to achieve this.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected final void fireConnectorValueChange() {
    // SwingUtil.performLongOperation(new Job() {
    //
    // /**
    // * Decorates the super implementation with the foxtrot job.
    // * <p>
    // * {@inheritDoc}
    // */
    // @Override
    // public Object run() {
    // protectedFireConnectorValueChange();
    // return null;
    // }
    // });
    protectedFireConnectorValueChange();
  }

  /**
   * Gets the connectedSComponent.
   * 
   * @return the connectedSComponent.
   */
  protected E getConnectedSComponent() {
    return connectedSComponent;
  }

  private void protectedFireConnectorValueChange() {
    super.fireConnectorValueChange();
  }
}
