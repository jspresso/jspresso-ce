/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.util.IGate;

/**
 * This abstract class holds some default implementation for a value connector.
 * All the default value connectors inherit from this default behaviour. It
 * implements the Connector value listener management through the use of the
 * <code>ConnectorValueChangeSupport</code> helper. It can virtually adapt to
 * any peer connectee since the way the value is retrieved by from the connectee
 * is left to the implementor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public abstract class AbstractValueConnector extends AbstractConnector
    implements IValueConnector {

  private Object                      oldConnectorValue;
  private ConnectorValueChangeSupport valueChangeSupport;
  private ICompositeValueConnector    parentConnector;

  private IValueConnector             modelConnector;

  private Collection<IGate>           readabilityGates;
  private Collection<IGate>           writabilityGates;

  private boolean                     locallyReadable;
  private boolean                     locallyWritable;

  /**
   * Constructs a new AbstractValueConnector using an identifier. In case of a
   * bean connector, this identifier must be the bean property the connector
   * connects.
   * 
   * @param id
   *          The connector identifier.
   */
  public AbstractValueConnector(String id) {
    super(id);
    valueChangeSupport = new ConnectorValueChangeSupport(this);
    locallyReadable = true;
    locallyWritable = true;
  }

  /**
   * Retrieves the value from the peer connectee.
   * 
   * @return the connectee value.
   */
  protected abstract Object getConnecteeValue();

  /**
   * Sets the value to the peer connectee.
   * 
   * @param connecteeValue
   *          the connectee value to set
   */
  protected abstract void setConnecteeValue(Object connecteeValue);

  /**
   * {@inheritDoc}
   */
  public Object getConnectorValue() {
    return getConnecteeValue();
  }

  /**
   * {@inheritDoc}
   */
  public void setConnectorValue(Object aValue) {
    setConnecteeValue(aValue);
    fireConnectorValueChange();
  }

  /**
   * @return Returns the oldConnectorValue.
   */
  protected Object getOldConnectorValue() {
    return oldConnectorValue;
  }

  /**
   * {@inheritDoc}
   */
  public void addConnectorValueChangeListener(
      IConnectorValueChangeListener listener) {
    if (listener != null) {
      valueChangeSupport.addConnectorValueChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void removeConnectorValueChangeListener(
      IConnectorValueChangeListener listener) {
    if (listener != null) {
      valueChangeSupport.removeConnectorValueChangeListener(listener);
    }
  }

  /**
   * This connector is notified of a change in a bound connector. It forwards by
   * default the change to its own connectee.
   * <p>
   * {@inheritDoc}
   */
  public void connectorValueChange(ConnectorValueChangeEvent evt) {
    // we must prevent the event to return back to the sender.
    valueChangeSupport.addInhibitedListener(evt.getSource());
    try {
      setConnectorValue(evt.getNewValue());
    } finally {
      valueChangeSupport.removeInhibitedListener(evt.getSource());
    }
  }

  /**
   * Notifies its listeners about a change in the connector's value.
   */
  protected void fireConnectorValueChange() {
    valueChangeSupport.fireConnectorValueChange(createChangeEvent(
        oldConnectorValue, getConnecteeValue()));
    // the change propagated correctly. Save the value propagated as the old
    // value of the connector.
    oldConnectorValue = computeOldConnectorValue(getConnecteeValue());
  }

  /**
   * Gives a chance to the connector to create a specific subclass of connector
   * value change event.
   * 
   * @param oldValue
   *          the old connector value.
   * @param newValue
   *          the new connector value.
   * @return the created change event.
   */
  protected ConnectorValueChangeEvent createChangeEvent(Object oldValue,
      Object newValue) {
    return new ConnectorValueChangeEvent(this, oldValue, newValue);
  }

  /**
   * When overriden . This method is called whenever a connector needs some
   * extra computation to save its old value. By default, the method returns the
   * parameter itself. For instance, this method is overriden in collection
   * connectors, where the underlying collection does not change but its content
   * changes. Simply keeping a reference on the underlying collection would not
   * be of any help since it never changes, preventing the notification to
   * happen. In that case, a clone of the model collection can be built to keep
   * track of the collection content change.
   * 
   * @param connectorValue
   *          the value to take a snapshot of.
   * @return the value to keep a reference on as the
   *         <code>oldConnectorValue</code>.
   */
  protected Object computeOldConnectorValue(Object connectorValue) {
    return connectorValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractValueConnector clone(String newConnectorId) {
    AbstractValueConnector clonedConnector = (AbstractValueConnector) super
        .clone(newConnectorId);
    clonedConnector.oldConnectorValue = null;
    clonedConnector.valueChangeSupport = new ConnectorValueChangeSupport(
        clonedConnector);
    clonedConnector.parentConnector = null;
    clonedConnector.modelConnector = null;
    clonedConnector.readabilityGates = null;
    clonedConnector.writabilityGates = null;
    clonedConnector.locallyReadable = locallyReadable;
    clonedConnector.locallyWritable = locallyWritable;
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  public void boundAsModel(
      IConnectorValueChangeListener modelConnectorListener,
      PropertyChangeListener readChangeListener,
      PropertyChangeListener writeChangeListener) {
    modelConnectorListener.connectorValueChange(new ConnectorValueChangeEvent(
        this, new Object(), getConnectorValue()));
    readChangeListener.propertyChange(new PropertyChangeEvent(this,
        READABLE_PROPERTY, null, new Boolean(isReadable())));
    writeChangeListener.propertyChange(new PropertyChangeEvent(this,
        WRITABLE_PROPERTY, null, new Boolean(isWritable())));
  }

  /**
   * {@inheritDoc}
   */
  public void boundAsView() {
    // Empty implementation
  }

  /**
   * Gets the readable.
   * 
   * @return the readable.
   */
  public boolean isReadable() {
    if (getParentConnector() != null && !getParentConnector().isReadable()) {
      return false;
    }
    return locallyReadable && areGatesOpen(readabilityGates);
  }

  /**
   * Gets the writable.
   * 
   * @return the writable.
   */
  public boolean isWritable() {
    if (getParentConnector() != null && !getParentConnector().areChildrenWritable()) {
      return false;
    }
    return locallyWritable && areGatesOpen(writabilityGates);
  }

  /**
   * {@inheritDoc}
   */
  public void setLocallyReadable(boolean locallyReadable) {
    // use isReadable() instead of readable parameter because there may be extra
    // computing rules.
    boolean oldReadable = isReadable();
    this.locallyReadable = locallyReadable;
    firePropertyChange(READABLE_PROPERTY, oldReadable, isReadable());
    updateState();
  }

  /**
   * {@inheritDoc}
   */
  public void setLocallyWritable(boolean locallyWritable) {
    // use isWritable() instead of writable parameter because there may be extra
    // computing rules.
    boolean oldWritable = isWritable();
    this.locallyWritable = locallyWritable;
    firePropertyChange(WRITABLE_PROPERTY, oldWritable, isWritable());
    updateState();
  }

  /**
   * Gets the parentConnector.
   * 
   * @return the parentConnector.
   */
  public ICompositeValueConnector getParentConnector() {
    return parentConnector;
  }

  /**
   * Sets the parentConnector.
   * 
   * @param parentConnector
   *          the parentConnector to set.
   */
  public void setParentConnector(ICompositeValueConnector parentConnector) {
    this.parentConnector = parentConnector;
  }

  /**
   * {@inheritDoc}
   */
  public void addReadabilityGate(IGate gate) {
    if (readabilityGates == null) {
      readabilityGates = new HashSet<IGate>(4);
    }
    readabilityGates.add(gate);
    updateState();
  }

  /**
   * {@inheritDoc}
   */
  public void addWritabilityGate(IGate gate) {
    if (writabilityGates == null) {
      writabilityGates = new HashSet<IGate>(4);
    }
    writabilityGates.add(gate);
    updateState();
  }

  /**
   * {@inheritDoc}
   */
  public void removeReadabilityGate(IGate gate) {
    if (readabilityGates == null) {
      return;
    }
    readabilityGates.remove(gate);
    updateState();
  }

  /**
   * {@inheritDoc}
   */
  public void removeWritabilityGate(IGate gate) {
    if (writabilityGates == null) {
      return;
    }
    writabilityGates.remove(gate);
    updateState();
  }

  /**
   * Gets the modelConnector.
   * 
   * @return the modelConnector.
   */
  public IValueConnector getModelConnector() {
    return modelConnector;
  }

  /**
   * Sets the modelConnector.
   * 
   * @param modelConnector
   *          the modelConnector to set.
   */
  public void setModelConnector(IValueConnector modelConnector) {
    if (getModelConnector() != null) {
      getModelConnector().removeConnectorValueChangeListener(this);
      removeConnectorValueChangeListener(getModelConnector());
      ModelConnectorPropertyChangeListener readGate = new ModelConnectorPropertyChangeListener(
          this);
      ModelConnectorPropertyChangeListener writeGate = new ModelConnectorPropertyChangeListener(
          this);
      getModelConnector().removePropertyChangeListener(
          IValueConnector.READABLE_PROPERTY, readGate);
      getModelConnector().removePropertyChangeListener(
          IValueConnector.WRITABLE_PROPERTY, writeGate);
      removeReadabilityGate(readGate);
      removeWritabilityGate(writeGate);
    }
    IValueConnector oldModelConnector = this.modelConnector;
    this.modelConnector = modelConnector;
    if (getModelConnector() != null) {
      getModelConnector().addConnectorValueChangeListener(this);
      addConnectorValueChangeListener(getModelConnector());
      ModelConnectorPropertyChangeListener readGate = new ModelConnectorPropertyChangeListener(
          this);
      ModelConnectorPropertyChangeListener writeGate = new ModelConnectorPropertyChangeListener(
          this);
      getModelConnector().addPropertyChangeListener(
          IValueConnector.READABLE_PROPERTY, readGate);
      getModelConnector().addPropertyChangeListener(
          IValueConnector.WRITABLE_PROPERTY, writeGate);
      addReadabilityGate(readGate);
      addWritabilityGate(writeGate);
      getModelConnector().boundAsModel(this, readGate, writeGate);
      connectorModelChange(oldModelConnector, getModelConnector());
    } else {
      setConnectorValue(null);
      updateState();
    }
  }

  /**
   * Gives a chance to the connector to react on a model connector change.
   * 
   * @param oldModelConnector
   *          the old model connector.
   * @param newModelConnector
   *          the new model connector.
   */
  @SuppressWarnings("unused")
  protected void connectorModelChange(IValueConnector oldModelConnector,
      IValueConnector newModelConnector) {
    // Default empty impl.
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public int compareTo(IValueConnector another) {
    if (getConnectorValue() != null) {
      if (another.getConnectorValue() != null) {
        if (getConnectorValue() instanceof Comparable) {
          if (getConnectorValue() instanceof String) {
            return ((String) getConnectorValue())
                .compareToIgnoreCase((String) another.getConnectorValue());
          }
          return ((Comparable) getConnectorValue()).compareTo(another
              .getConnectorValue());
        }
        return getConnectorValue().toString().compareToIgnoreCase(
            another.getConnectorValue().toString());
      }
    } else if (another.getConnectorValue() != null) {
      return -1;
    }
    return 0;
  }

  private class ModelConnectorPropertyChangeListener implements
      PropertyChangeListener, IGate {

    private IValueConnector viewConnector;
    private boolean         open;

    /**
     * Constructs a new <code>ConnectorReadabilityChangeListener</code>
     * instance.
     * 
     * @param viewConnector
     *          the view connector
     */
    public ModelConnectorPropertyChangeListener(IValueConnector viewConnector) {
      this.viewConnector = viewConnector;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isOpen() {
      return open;
    }

    /**
     * Sets the gate open.
     * 
     * @param open
     *          true if open.
     */
    private void setOpen(boolean open) {
      this.open = open;
    }

    /**
     * Returns the view connector.
     * 
     * @return the view connector.
     */
    protected IValueConnector getViewConnector() {
      return viewConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof ModelConnectorPropertyChangeListener)) {
        return false;
      }
      if (this == obj) {
        return true;
      }
      ModelConnectorPropertyChangeListener rhs = (ModelConnectorPropertyChangeListener) obj;
      return new EqualsBuilder().append(viewConnector, rhs.viewConnector)
          .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 13).append(viewConnector).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent evt) {
      setOpen(((Boolean) evt.getNewValue()).booleanValue());
      getViewConnector().updateState();
    }
  }
}
