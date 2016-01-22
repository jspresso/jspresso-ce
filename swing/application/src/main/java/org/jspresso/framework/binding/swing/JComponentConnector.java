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

import java.awt.Color;

import javax.swing.JComponent;

import org.jspresso.framework.binding.AbstractValueConnector;
import org.jspresso.framework.binding.ConnectorInputException;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * This abstract class serves as the base class for all JComponent connectors.
 * Subclasses can access the JComponent using the parametrized method
 * {@code getConnectedJComponent()} which returns the parametrized type of
 * the class.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          The actual class of the subclass of {@code JComponent}.
 */
public abstract class JComponentConnector<E extends JComponent> extends
    AbstractValueConnector {

  private final E     connectedJComponent;

  private Color savedForeground;

  /**
   * Constructs a new {@code JComponentConnector} instance.
   *
   * @param id
   *          the connector identifier.
   * @param connectedJComponent
   *          the connected JComponent.
   */
  public JComponentConnector(String id, E connectedJComponent) {
    super(id);
    this.connectedJComponent = connectedJComponent;
    bindJComponent();
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
   * This implementation takes care of having the peer component modifications
   * ran on the Swing event dispatch thread. It actually delegates the connectee
   * modification to the {@code protectedReadabilityChange} method.
   *
   * @see #protectedReadabilityChange()
   */
  @Override
  public final void readabilityChange() {
    super.readabilityChange();
    SwingUtil.updateSwingGui(new Runnable() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void run() {
        protectedReadabilityChange();
      }
    });
  }

  /**
   * This implementation takes care of having the peer component modifications
   * ran on the Swing event dispatch thread. It actually delegates the connectee
   * modification to the {@code protectedWritabilityChange} method.
   *
   * @see #protectedWritabilityChange()
   */
  @Override
  public final void writabilityChange() {
    super.writabilityChange();
    SwingUtil.updateSwingGui(new Runnable() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void run() {
        protectedWritabilityChange();
      }
    });
  }

  /**
   * Attaches the JComponent to the connector.
   */
  protected abstract void bindJComponent();

  /**
   * This method has been overridden to take care of long-running operations not
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
    try {
      protectedFireConnectorValueChange();
    } catch (ConnectorInputException ex) {
      // It's an input error. ignore
    } catch (RuntimeException ex) {
      handleException(ex);
    }
  }

  /**
   * Gets the connectedJComponent.
   *
   * @return the connectedJComponent.
   */
  protected E getConnectedJComponent() {
    return connectedJComponent;
  }

  /**
   * This method can be overridden by subclasses in lieu of
   * {@code fireConnectorValueChange} that has been made final to take care
   * of the swing EDT.
   */
  protected void protectedFireConnectorValueChange() {
    super.fireConnectorValueChange();
  }

  /**
   * Implementation of connectee readability state modifications which normally
   * would have been coded in the {@code readabilityChange} method should
   * go here to preserve the connector modification to be handled in the event
   * dispatch thread.
   */
  protected void protectedReadabilityChange() {
    if (isReadable()) {
      if (savedForeground != null) {
        getConnectedJComponent().setForeground(savedForeground);
      }
      savedForeground = null;
    } else if (savedForeground == null) {
      savedForeground = getConnectedJComponent().getForeground();
      getConnectedJComponent().setForeground(
          getConnectedJComponent().getBackground());
    }
  }

  /**
   * Implementation of connectee modifications which normally would have been
   * coded in the {@code setConnecteeValue} should go here to preserve the
   * connector modification to be handled in the event dispatch thread.
   *
   * @param aValue
   *          the connectee value to set.
   */
  protected abstract void protectedSetConnecteeValue(Object aValue);

  /**
   * Implementation of connectee writability state modifications which normally
   * would have been coded in the {@code writabilityChange} method should
   * go here to preserve the connector modification to be handled in the event
   * dispatch thread.
   */
  protected void protectedWritabilityChange() {
    // Empty implementation.
  }

  /**
   * This implementation takes care of having the peer component modifications
   * ran on the Swing event dispatch thread. It actually delegates the connectee
   * modification to the {@code protectedSetConnecteeValue} method.
   * <p>
   * {@inheritDoc}
   *
   * @see #protectedSetConnecteeValue(Object)
   */
  @Override
  protected final void setConnecteeValue(final Object aValue) {
    SwingUtil.updateSwingGui(new Runnable() {

      @Override
      public void run() {
        protectedSetConnecteeValue(aValue);
      }
    });
  }
}
