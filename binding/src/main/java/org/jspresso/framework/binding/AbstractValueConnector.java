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
package org.jspresso.framework.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.security.ISecurityHandlerAware;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.event.ValueChangeSupport;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.gate.GateHelper;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.lang.IModelAware;
import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * This abstract class holds some default implementation for a value connector.
 * All the default value connectors inherit from this default behaviour. It
 * implements the Connector value listener management through the use of the
 * <code>ValueChangeSupport</code> helper. It can virtually adapt to any peer
 * connectee since the way the value is retrieved by from the connectee is left
 * to the implementor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public abstract class AbstractValueConnector extends AbstractConnector
    implements IValueConnector {

  private IExceptionHandler        exceptionHandler;
  private boolean                  locallyReadable;
  private boolean                  locallyWritable;

  private IValueConnector          modelConnector;
  private IModelDescriptor         modelDescriptor;
  private PropertyChangeListener   modelReadabilityListener;
  private PropertyChangeListener   modelWritabilityListener;

  private Object                   oldConnectorValue;
  private boolean                  oldReadability;

  private boolean                  oldWritability;
  private ICompositeValueConnector parentConnector;

  private Collection<IGate>        readabilityGates;
  private PropertyChangeListener   readabilityGatesListener;

  private ISecurityHandler         securityHandler;

  private ValueChangeSupport       valueChangeSupport;
  private Collection<IGate>        writabilityGates;

  private PropertyChangeListener   writabilityGatesListener;

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
    valueChangeSupport = new ValueChangeSupport(this);
    locallyReadable = true;
    locallyWritable = true;
    oldReadability = isReadable();
    oldWritability = isWritable();
  }

  /**
   * {@inheritDoc}
   */
  public void addReadabilityGate(IGate gate) {
    if (gate instanceof ISecurityHandlerAware) {
      ((ISecurityHandlerAware) gate).setSecurityHandler(getSecurityHandler());
    }
    if (readabilityGates == null) {
      readabilityGates = new HashSet<IGate>(4);
    }
    readabilityGates.add(gate);
    gate.addPropertyChangeListener(IGate.OPEN_PROPERTY,
        getReadabilityGatesListener());
    readabilityChange();
  }

  /**
   * {@inheritDoc}
   */
  public void resetReadabilityGates() {
    readabilityGates = null;
    readabilityChange();
  }

  /**
   * {@inheritDoc}
   */
  public void addValueChangeListener(IValueChangeListener listener) {
    if (listener != null) {
      valueChangeSupport.addValueChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void addWritabilityGate(IGate gate) {
    if (gate instanceof ISecurityHandlerAware) {
      ((ISecurityHandlerAware) gate).setSecurityHandler(getSecurityHandler());
    }
    if (writabilityGates == null) {
      writabilityGates = new HashSet<IGate>(4);
    }
    writabilityGates.add(gate);
    gate.addPropertyChangeListener(IGate.OPEN_PROPERTY,
        getWritabilityGatesListener());
    writabilityChange();
  }

  /**
   * {@inheritDoc}
   */
  public void resetWritabilityGates() {
    writabilityGates = null;
    writabilityChange();
  }

  /**
   * Empty implementation.
   * <p>
   * {@inheritDoc}
   */
  public void boundAsModel() {
    // Empty implementation
  }

  /**
   * Binds model gates.
   * <p>
   * {@inheritDoc}
   */
  public void boundAsView() {
    bindModelGates(getWritabilityGates());
    bindModelGates(getReadabilityGates());
  }

  /**
   * {@inheritDoc}
   */
  public void cleanBindings() {
    for (IValueChangeListener listener : valueChangeSupport.getListeners()) {
      removeValueChangeListener(listener);
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
    clonedConnector.valueChangeSupport = new ValueChangeSupport(clonedConnector);
    clonedConnector.parentConnector = null;
    clonedConnector.modelConnector = null;
    clonedConnector.readabilityGates = null;
    clonedConnector.writabilityGates = null;
    clonedConnector.modelReadabilityListener = null;
    clonedConnector.modelWritabilityListener = null;
    clonedConnector.readabilityGatesListener = null;
    clonedConnector.writabilityGatesListener = null;
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
    clonedConnector.oldReadability = clonedConnector.isReadable();
    clonedConnector.oldWritability = clonedConnector.isWritable();
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
          return ((Comparable<Object>) getConnectorValue()).compareTo(another
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
   * Gets the modelConnector.
   * 
   * @return the modelConnector.
   */
  public IValueConnector getModelConnector() {
    return modelConnector;
  }

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  public IModelDescriptor getModelDescriptor() {
    if (modelDescriptor != null) {
      return modelDescriptor;
    } else if (getModelConnector() != null) {
      return getModelConnector().getModelDescriptor();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public IModelProvider getModelProvider() {
    if (getModelConnector() != null) {
      return getModelConnector().getModelProvider();
    }
    return null;
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
   * Gets the readability gates listener.
   * 
   * @return the readability gates listener.
   */
  public PropertyChangeListener getReadabilityGatesListener() {
    if (readabilityGatesListener == null) {
      readabilityGatesListener = new PropertyChangeListener() {

        public void propertyChange(
            @SuppressWarnings("unused") PropertyChangeEvent evt) {
          readabilityChange();
        }
      };
    }
    return readabilityGatesListener;
  }

  /**
   * Gets the writability gates listener.
   * 
   * @return the writability gates listener.
   */
  public PropertyChangeListener getWritabilityGatesListener() {
    if (writabilityGatesListener == null) {
      writabilityGatesListener = new PropertyChangeListener() {

        public void propertyChange(
            @SuppressWarnings("unused") PropertyChangeEvent evt) {
          writabilityChange();
        }
      };
    }
    return writabilityGatesListener;
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
    return isLocallyReadable();
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
    return isLocallyWritable();
  }

  /**
   * Called whenever readability may have changed.
   */
  public void readabilityChange() {
    boolean readable = isReadable();
    firePropertyChange(READABLE_PROPERTY, oldReadability, readable);
    oldReadability = readable;
  }

  /**
   * {@inheritDoc}
   */
  public void removeReadabilityGate(IGate gate) {
    if (gate instanceof ISecurityHandlerAware) {
      ((ISecurityHandlerAware) gate).setSecurityHandler(null);
    }
    if (readabilityGates == null) {
      return;
    }
    readabilityGates.remove(gate);
    gate.removePropertyChangeListener(IGate.OPEN_PROPERTY,
        getReadabilityGatesListener());
    readabilityChange();
  }

  /**
   * {@inheritDoc}
   */
  public void removeValueChangeListener(IValueChangeListener listener) {
    if (listener != null) {
      valueChangeSupport.removeValueChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void removeWritabilityGate(IGate gate) {
    if (gate instanceof ISecurityHandlerAware) {
      ((ISecurityHandlerAware) gate).setSecurityHandler(null);
    }
    if (writabilityGates == null) {
      return;
    }
    writabilityGates.remove(gate);
    gate.removePropertyChangeListener(IGate.OPEN_PROPERTY,
        getWritabilityGatesListener());
    writabilityChange();
  }

  /**
   * {@inheritDoc}
   */
  public void setConnectorValue(Object aValue) {
    if (aValue instanceof Number) {
      if (getModelDescriptor() != null) {
        Class<?> expectedType = ((IPropertyDescriptor) getModelDescriptor())
            .getModelType();
        if (expectedType.isAssignableFrom(aValue.getClass())) {
          setConnecteeValue(aValue);
        } else {
          if (Boolean.TYPE.equals(expectedType)) {
            expectedType = Boolean.class;
          }
          try {
            Object adaptedValue = expectedType.getConstructor(new Class<?>[] {
              String.class
            }).newInstance(new Object[] {
              aValue.toString()
            });
            setConnecteeValue(adaptedValue);
          } catch (IllegalArgumentException ex) {
            throw new ConnectorBindingException(ex);
          } catch (SecurityException ex) {
            throw new ConnectorBindingException(ex);
          } catch (InstantiationException ex) {
            throw new ConnectorBindingException(ex);
          } catch (IllegalAccessException ex) {
            throw new ConnectorBindingException(ex);
          } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof RuntimeException) {
              throw (RuntimeException) ex.getCause();
            }
            throw new ConnectorBindingException(ex.getCause());
          } catch (NoSuchMethodException ex) {
            throw new ConnectorBindingException(ex);
          }
        }
      } else {
        setConnecteeValue(aValue);
      }
    } else {
      setConnecteeValue(aValue);
    }
    fireConnectorValueChange();
  }

  /**
   * {@inheritDoc}
   */
  public void setExceptionHandler(IExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }

  /**
   * Gets the exception handler.
   * 
   * @return the exception handler.
   */
  protected IExceptionHandler getExceptionHandler() {
    return exceptionHandler;
  }

  /**
   * {@inheritDoc}
   */
  public void setLocallyReadable(boolean locallyReadable) {
    this.locallyReadable = locallyReadable;
    readabilityChange();
  }

  /**
   * {@inheritDoc}
   */
  public void setLocallyWritable(boolean locallyWritable) {
    this.locallyWritable = locallyWritable;
    writabilityChange();
  }

  /**
   * Sets the modelConnector.
   * 
   * @param modelConnector
   *          the modelConnector to set.
   */
  public void setModelConnector(IValueConnector modelConnector) {
    IValueConnector oldModelConnector = getModelConnector();
    if (oldModelConnector != null) {
      oldModelConnector.removeValueChangeListener(this);
      removeValueChangeListener(oldModelConnector);
      if (modelReadabilityListener != null) {
        oldModelConnector.removePropertyChangeListener(
            IValueConnector.READABLE_PROPERTY, modelReadabilityListener);
      }
      if (modelWritabilityListener != null) {
        oldModelConnector.removePropertyChangeListener(
            IValueConnector.WRITABLE_PROPERTY, modelWritabilityListener);
      }
    }
    this.modelConnector = modelConnector;
    if (getModelConnector() != null) {
      // manually triggers a connector value change event
      valueChange(new ValueChangeEvent(getModelConnector(),
          getConnectorValue(), getModelConnector().getConnectorValue()));
      getModelConnector().addValueChangeListener(this);
      addValueChangeListener(getModelConnector());
      if (modelReadabilityListener == null) {
        modelReadabilityListener = new PropertyChangeListener() {

          public void propertyChange(
              @SuppressWarnings("unused") PropertyChangeEvent evt) {
            readabilityChange();
          }
        };
      }
      if (modelWritabilityListener == null) {
        modelWritabilityListener = new PropertyChangeListener() {

          public void propertyChange(
              @SuppressWarnings("unused") PropertyChangeEvent evt) {
            writabilityChange();
          }
        };
      }
      getModelConnector().addPropertyChangeListener(
          IValueConnector.READABLE_PROPERTY, modelReadabilityListener);
      getModelConnector().addPropertyChangeListener(
          IValueConnector.WRITABLE_PROPERTY, modelWritabilityListener);
    } else {
      setConnectorValue(null);
    }
    readabilityChange();
    writabilityChange();
    firePropertyChange("modelConnector", oldModelConnector, getModelConnector());
  }

  /**
   * Sets the modelDescriptor.
   * 
   * @param modelDescriptor
   *          the modelDescriptor to set.
   */
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
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
   * Configures accessibility gates with the security handler.
   * 
   * @param securityHandler
   *          the security handler responsible for managing authorizations.
   */
  public void setSecurityHandler(ISecurityHandler securityHandler) {
    this.securityHandler = securityHandler;
    if (getReadabilityGates() != null) {
      for (IGate gate : getReadabilityGates()) {
        if (gate instanceof ISecurityHandlerAware) {
          ((ISecurityHandlerAware) gate).setSecurityHandler(securityHandler);
        }
      }
    }
    if (getWritabilityGates() != null) {
      for (IGate gate : getWritabilityGates()) {
        if (gate instanceof ISecurityHandlerAware) {
          ((ISecurityHandlerAware) gate).setSecurityHandler(securityHandler);
        }
      }
    }
  }

  /**
   * This connector is notified of a change in a bound connector. It forwards by
   * default the change to its own connectee.
   * <p>
   * {@inheritDoc}
   */
  public void valueChange(ValueChangeEvent evt) {
    // we must prevent the event to return back to the sender.
    valueChangeSupport.addInhibitedListener((IValueConnector) evt.getSource());
    try {
      setConnectorValue(evt.getNewValue());
      // a model connector should not be updated by the view connector when
      // being assigned as model.
      if (evt.getSource() != getModelConnector()) {
        Object potentiallyChangedValue = getConnectorValue();
        if (!ObjectUtils.equals(potentiallyChangedValue, evt.getNewValue())) {
          // the source connector must be notified because settting this
          // connector
          // value resulted in a value changed (a string to uppercase for
          // instance).
          ((IValueConnector) evt.getSource())
              .setConnectorValue(potentiallyChangedValue);
        }
      }
    } finally {
      valueChangeSupport.removeInhibitedListener((IValueConnector) evt
          .getSource());
    }
  }

  /**
   * Called whenever writability may have changed.
   */
  public void writabilityChange() {
    boolean writable = isWritable();
    firePropertyChange(WRITABLE_PROPERTY, oldWritability, writable);
    oldWritability = writable;
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
   * Gives a chance to the connector to create a specific subclass of connector
   * value change event.
   * 
   * @param oldValue
   *          the old connector value.
   * @param newValue
   *          the new connector value.
   * @return the created change event.
   */
  protected ValueChangeEvent createChangeEvent(Object oldValue, Object newValue) {
    return new ValueChangeEvent(this, oldValue, newValue);
  }

  /**
   * Notifies its listeners about a change in the connector's value.
   */
  protected void fireConnectorValueChange() {
    boolean propagatedCorrectly = true;
    try {
      fireValueChange(createChangeEvent(oldConnectorValue, getConnecteeValue()));
    } catch (RuntimeException ex) {
      propagatedCorrectly = false;
      try {
        propagateRollback();
      } catch (Exception ex2) {
        // ignore. Nothing can be done about it.
      }
      handleException(ex);
    }
    if (propagatedCorrectly) {
      // the change propagated correctly. Save the value propagated as the old
      // value of the connector.
      oldConnectorValue = computeOldConnectorValue(getConnecteeValue());
    }
  }

  /**
   * Whenever an exception occurs when setting a connector value, some
   * notifications have to be fired to notify listeneres.
   */
  protected void propagateRollback() {
    Object badValue = getConnectorValue();
    setConnecteeValue(oldConnectorValue);
    // propagate the reverse change...
    fireValueChange(createChangeEvent(badValue, getConnecteeValue()));
  }

  /**
   * Fires a value change, delegating to the value change support.
   * 
   * @param changeEvent
   *          the change event to propagate.
   */
  protected void fireValueChange(ValueChangeEvent changeEvent) {
    valueChangeSupport.fireValueChange(changeEvent);
  }

  /**
   * Handles a runtime exception, trying to delegeta it to the exception handler
   * if any.
   * 
   * @param ex
   *          the runtime exception.
   */
  protected void handleException(RuntimeException ex) {
    if (getExceptionHandler() != null) {
      getExceptionHandler().handleException(ex, null);
    } else {
      throw ex;
    }
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
   * Gets the locallyReadable.
   * 
   * @return the locallyReadable.
   */
  protected boolean isLocallyReadable() {
    return locallyReadable && GateHelper.areGatesOpen(getReadabilityGates());
  }

  /**
   * Gets the locallyWritable.
   * 
   * @return the locallyWritable.
   */
  protected boolean isLocallyWritable() {
    return locallyWritable && GateHelper.areGatesOpen(getWritabilityGates());
  }

  /**
   * Sets the value to the peer connectee.
   * 
   * @param connecteeValue
   *          the connectee value to set
   */
  protected abstract void setConnecteeValue(Object connecteeValue);

  private void bindModelGates(Collection<IGate> gates) {
    if (gates != null) {
      IValueConnector connectorToListenTo;
      if (getModelDescriptor() instanceof IPropertyDescriptor) {
        // to avoid misbinding on reference property connectors
        connectorToListenTo = getComponentConnector(getParentConnector());
      } else {
        connectorToListenTo = getComponentConnector(this);
      }
      if (connectorToListenTo != null) {
        for (IGate gate : gates) {
          if (gate instanceof IModelAware) {
            ((IModelAware) gate).setModel(connectorToListenTo
                .getConnectorValue());
            connectorToListenTo
                .addValueChangeListener(new InnerGateModelListener(
                    (IModelAware) gate));
          }
        }
      }
    }
  }

  private IValueConnector getComponentConnector(IValueConnector connector) {
    if (connector == null) {
      return null;
    }
    /* connector.getModelDescriptor() instanceof IComponentDescriptorProvider<?> */
    if (connector instanceof ICompositeValueConnector
        && !(connector instanceof ICollectionConnector)) {
      return connector;
    }
    return getComponentConnector(connector.getParentConnector());
  }

  private static class InnerGateModelListener implements IValueChangeListener {

    private IModelAware gate;

    /**
     * Constructs a new <code>InnerGateModelListener</code> instance.
     * 
     * @param gate
     *          the model gate.
     */
    public InnerGateModelListener(IModelAware gate) {
      this.gate = gate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      InnerGateModelListener other = (InnerGateModelListener) obj;
      if (gate == null) {
        if (other.gate != null) {
          return false;
        }
      } else if (!gate.equals(other.gate)) {
        return false;
      }
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result;
      if (gate != null) {
        result += gate.hashCode();
      }
      return result;
    }

    /**
     * {@inheritDoc}
     */
    public void valueChange(ValueChangeEvent evt) {
      gate.setModel(evt.getNewValue());
    }

  }

  /**
   * Gets the securityHandler.
   * 
   * @return the securityHandler.
   */
  protected ISecurityHandler getSecurityHandler() {
    return securityHandler;
  }
}
