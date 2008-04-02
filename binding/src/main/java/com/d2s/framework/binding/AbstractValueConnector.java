/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.util.exception.IExceptionHandler;
import com.d2s.framework.util.gate.GateHelper;
import com.d2s.framework.util.gate.IGate;
import com.d2s.framework.util.lang.ObjectUtils;

/**
 * This abstract class holds some default implementation for a value connector.
 * All the default value connectors inherit from this default behaviour. It
 * implements the Connector value listener management through the use of the
 * <code>ConnectorValueChangeSupport</code> helper. It can virtually adapt to
 * any peer connectee since the way the value is retrieved by from the connectee
 * is left to the implementor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public abstract class AbstractValueConnector extends AbstractConnector
    implements IValueConnector {

  private IExceptionHandler                    exceptionHandler;
  private PropertyChangeListener               gatesListener;
  private boolean                              locallyReadable;

  private boolean                              locallyWritable;

  private IValueConnector                      modelConnector;
  private Object                               oldConnectorValue;

  private ICompositeValueConnector             parentConnector;
  private Collection<IGate>                    readabilityGates;

  private ModelConnectorPropertyChangeListener readableListener;
  private ConnectorValueChangeSupport          valueChangeSupport;

  private Collection<IGate>                    writabilityGates;

  private ModelConnectorPropertyChangeListener writableListener;

  /**
   * Constructs a new AbstractValueConnector using an identifier. In case of a
   * bean connector, this identifier must be the bean property the connector
   * connects.
   * 
   * @param id
   *            The connector identifier.
   */
  public AbstractValueConnector(String id) {
    super(id);
    valueChangeSupport = new ConnectorValueChangeSupport(this);
    locallyReadable = true;
    locallyWritable = true;
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
  public void addReadabilityGate(IGate gate) {
    if (readabilityGates == null) {
      readabilityGates = new HashSet<IGate>(4);
    }
    readabilityGates.add(gate);
    gate.addPropertyChangeListener(IGate.OPEN_PROPERTY, getGatesListener());
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
    gate.addPropertyChangeListener(IGate.OPEN_PROPERTY, getGatesListener());
    updateState();
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
   * {@inheritDoc}
   */
  public void cleanBindings() {
    for (IConnectorValueChangeListener listener : valueChangeSupport
        .getListeners()) {
      removeConnectorValueChangeListener(listener);
    }
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
    if (readabilityGates != null) {
      for (IGate gate : readabilityGates) {
        clonedConnector.addReadabilityGate(gate.clone());
      }
    }
    if (writabilityGates != null) {
      for (IGate gate : writabilityGates) {
        clonedConnector.addWritabilityGate(gate.clone());
      }
    }
    return clonedConnector;
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
      // a model connector should not be updated by the view connector when being assigned as model.
      if (evt.getSource() != getModelConnector()) {
        Object potentiallyChangedValue = getConnectorValue();
        if (!ObjectUtils.equals(potentiallyChangedValue, evt.getNewValue())) {
          // the source connector must be notified because settting this
          // connector
          // value resulted in a value changed (a string to uppercase for
          // instance).
          evt.getSource().setConnectorValue(potentiallyChangedValue);
        }
      }
    } finally {
      valueChangeSupport.removeInhibitedListener(evt.getSource());
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getConnectorPath() {
    List<String> connectorPath = new ArrayList<String>();
    IValueConnector c = this;
    while (c != null) {
      connectorPath.add(c.getId());
      c = c.getParentConnector();
    }
    Collections.reverse(connectorPath);
    StringBuffer path = new StringBuffer();
    for (String id : connectorPath) {
      path.append("/");
      path.append(id);
    }
    return path.toString();
  }

  /**
   * {@inheritDoc}
   */
  public Object getConnectorValue() {
    return getConnecteeValue();
  }

  /**
   * Gets the gatesListener.
   * 
   * @return the gatesListener.
   */
  public PropertyChangeListener getGatesListener() {
    if (gatesListener == null) {
      gatesListener = new PropertyChangeListener() {

        public void propertyChange(@SuppressWarnings("unused")
        PropertyChangeEvent evt) {
          updateState();
        }
      };
    }
    return gatesListener;
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
   * Gets the parentConnector.
   * 
   * @return the parentConnector.
   */
  public ICompositeValueConnector getParentConnector() {
    return parentConnector;
  }

  /**
   * Gets the readable.
   * 
   * @return the readable.
   */
  public boolean isReadable() {
    if (getParentConnector() != null
        && !getParentConnector().areChildrenReadable()) {
      return false;
    }
    if (getModelConnector() != null && !getModelConnector().isReadable()) {
      return false;
    }
    return locallyReadable && GateHelper.areGatesOpen(readabilityGates);
  }

  /**
   * Gets the writable.
   * 
   * @return the writable.
   */
  public boolean isWritable() {
    if (getParentConnector() != null
        && !getParentConnector().areChildrenWritable()) {
      return false;
    }
    if (getModelConnector() != null && !getModelConnector().isWritable()) {
      return false;
    }
    return locallyWritable && GateHelper.areGatesOpen(writabilityGates);
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
   * {@inheritDoc}
   */
  public void removeReadabilityGate(IGate gate) {
    if (readabilityGates == null) {
      return;
    }
    readabilityGates.remove(gate);
    gate.removePropertyChangeListener(IGate.OPEN_PROPERTY, getGatesListener());
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
    gate.removePropertyChangeListener(IGate.OPEN_PROPERTY, getGatesListener());
    updateState();
  }

  /**
   * {@inheritDoc}
   */
  public void setConnectorValue(Object aValue) {
    setConnecteeValue(aValue);
    fireConnectorValueChange();
  }

  /**
   * {@inheritDoc}
   */
  public void setExceptionHandler(IExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
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
   * Sets the modelConnector.
   * 
   * @param modelConnector
   *            the modelConnector to set.
   */
  public void setModelConnector(IValueConnector modelConnector) {
    if (getModelConnector() != null) {
      getModelConnector().removeConnectorValueChangeListener(this);
      removeConnectorValueChangeListener(getModelConnector());
      if (readableListener != null) {
        getModelConnector().removePropertyChangeListener(
            IValueConnector.READABLE_PROPERTY, readableListener);
      }
      if (writableListener != null) {
        getModelConnector().removePropertyChangeListener(
            IValueConnector.WRITABLE_PROPERTY, writableListener);
      }
    }
    IValueConnector oldModelConnector = this.modelConnector;
    this.modelConnector = modelConnector;
    if (getModelConnector() != null) {
      getModelConnector().addConnectorValueChangeListener(this);
      addConnectorValueChangeListener(getModelConnector());
      if (readableListener == null) {
        readableListener = new ModelConnectorPropertyChangeListener(this);
      }
      if (writableListener == null) {
        writableListener = new ModelConnectorPropertyChangeListener(this);
      }
      getModelConnector().addPropertyChangeListener(
          IValueConnector.READABLE_PROPERTY, readableListener);
      getModelConnector().addPropertyChangeListener(
          IValueConnector.WRITABLE_PROPERTY, writableListener);
      getModelConnector()
          .boundAsModel(this, readableListener, writableListener);
    } else {
      setConnectorValue(null);
    }
    connectorModelChange(oldModelConnector, getModelConnector());
  }

  /**
   * Sets the parentConnector.
   * 
   * @param parentConnector
   *            the parentConnector to set.
   */
  public void setParentConnector(ICompositeValueConnector parentConnector) {
    this.parentConnector = parentConnector;
  }

  /**
   * Default implementation does nothing.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    boolean readable = isReadable();
    firePropertyChange(READABLE_PROPERTY, !readable, readable);
    boolean writable = isWritable();
    firePropertyChange(WRITABLE_PROPERTY, !writable, writable);
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
   *            the value to take a snapshot of.
   * @return the value to keep a reference on as the
   *         <code>oldConnectorValue</code>.
   */
  protected Object computeOldConnectorValue(Object connectorValue) {
    return connectorValue;
  }

  /**
   * Gives a chance to the connector to react on a model connector change.
   * 
   * @param oldModelConnector
   *            the old model connector.
   * @param newModelConnector
   *            the new model connector.
   */
  @SuppressWarnings("unused")
  protected void connectorModelChange(IValueConnector oldModelConnector,
      IValueConnector newModelConnector) {
    updateState();
  }

  /**
   * Gives a chance to the connector to create a specific subclass of connector
   * value change event.
   * 
   * @param oldValue
   *            the old connector value.
   * @param newValue
   *            the new connector value.
   * @return the created change event.
   */
  protected ConnectorValueChangeEvent createChangeEvent(Object oldValue,
      Object newValue) {
    return new ConnectorValueChangeEvent(this, oldValue, newValue);
  }

  /**
   * Notifies its listeners about a change in the connector's value.
   */
  protected void fireConnectorValueChange() {
    try {
      valueChangeSupport.fireConnectorValueChange(createChangeEvent(
          oldConnectorValue, getConnecteeValue()));
    } catch (RuntimeException ex) {
      if (exceptionHandler != null) {
        exceptionHandler.handleException(ex, null);
      } else {
        throw ex;
      }
      try {
        setConnecteeValue(oldConnectorValue);
      } catch (Exception ex2) {
        // ignore. Nothing can be done about it.
      }
    }
    // the change propagated correctly. Save the value propagated as the old
    // value of the connector.
    oldConnectorValue = computeOldConnectorValue(getConnecteeValue());
  }

  /**
   * Retrieves the value from the peer connectee.
   * 
   * @return the connectee value.
   */
  protected abstract Object getConnecteeValue();

  /**
   * @return Returns the oldConnectorValue.
   */
  protected Object getOldConnectorValue() {
    return oldConnectorValue;
  }

  /**
   * Gets the readabilityGates.
   * 
   * @return the readabilityGates.
   */
  protected Collection<IGate> getReadabilityGates() {
    return readabilityGates;
  }

  /**
   * Gets the writabilityGates.
   * 
   * @return the writabilityGates.
   */
  protected Collection<IGate> getWritabilityGates() {
    return writabilityGates;
  }

  /**
   * Sets the value to the peer connectee.
   * 
   * @param connecteeValue
   *            the connectee value to set
   */
  protected abstract void setConnecteeValue(Object connecteeValue);

  private class ModelConnectorPropertyChangeListener implements
      PropertyChangeListener {

    private IValueConnector viewConnector;

    /**
     * Constructs a new <code>ConnectorReadabilityChangeListener</code>
     * instance.
     * 
     * @param viewConnector
     *            the view connector
     */
    public ModelConnectorPropertyChangeListener(IValueConnector viewConnector) {
      this.viewConnector = viewConnector;
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
    public void propertyChange(@SuppressWarnings("unused")
    PropertyChangeEvent evt) {
      viewConnector.updateState();
    }
  }
}
