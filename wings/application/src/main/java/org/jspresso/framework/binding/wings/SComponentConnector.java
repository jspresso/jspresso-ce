/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
