/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.awt.Color;

import javax.swing.JComponent;

import com.d2s.framework.binding.AbstractValueConnector;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * This abstract class serves as the base class for all JComponent connectors.
 * Subclasses can access the JComponent using the parametrized method
 * <code>getConnectedJComponent()</code> which returns the parametrized type
 * of the class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            The actual class of the subclass of <code>JComponent</code>.
 */
public abstract class JComponentConnector<E extends JComponent> extends
    AbstractValueConnector {

  private E     connectedJComponent;

  private Color savedForeground;

  /**
   * Constructs a new <code>JComponentConnector</code> instance.
   * 
   * @param id
   *            the connector identifier.
   * @param connectedJComponent
   *            the connected JComponent.
   */
  public JComponentConnector(String id, E connectedJComponent) {
    super(id);
    this.connectedJComponent = connectedJComponent;
    bindJComponent();
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
   * This implementation takes care of having the peer component modifications
   * ran on the Swing event dispatch thread. It actually delegates the connectee
   * modification to the <code>protectedUpdateState</code> method.
   * 
   * @see #protectedUpdateState()
   *      <p>
   *      {@inheritDoc}
   */
  @Override
  public final void updateState() {
    SwingUtil.updateSwingGui(new Runnable() {

      /**
       * {@inheritDoc}
       */
      public void run() {
        protectedUpdateState();
      }
    });
  }

  /**
   * Attaches the JComponent to the connector.
   */
  protected abstract void bindJComponent();

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
   * Gets the connectedJComponent.
   * 
   * @return the connectedJComponent.
   */
  protected E getConnectedJComponent() {
    return connectedJComponent;
  }

  /**
   * Implementation of connectee modifications which normally would have been
   * coded in the <code>setConnecteeValue</code> should go here to preserve
   * the connector modification to be handled in the event dispatch thread.
   * 
   * @param aValue
   *            the connectee value to set.
   */
  protected abstract void protectedSetConnecteeValue(Object aValue);

  /**
   * Implementation of connectee state modifications which normally would have
   * been coded in the <code>updateState</code> method should go here to
   * preserve the connector modification to be handled in the event dispatch
   * thread.
   */
  protected void protectedUpdateState() {
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
   * This implementation takes care of having the peer component modifications
   * ran on the Swing event dispatch thread. It actually delegates the connectee
   * modification to the <code>protectedSetConnecteeValue</code> method.
   * <p>
   * {@inheritDoc}
   * 
   * @see #protectedSetConnecteeValue(Object)
   */
  @Override
  protected final void setConnecteeValue(final Object aValue) {
    SwingUtil.updateSwingGui(new Runnable() {

      public void run() {
        protectedSetConnecteeValue(aValue);
      }
    });
  }

  private void protectedFireConnectorValueChange() {
    super.fireConnectorValueChange();
  }
}
