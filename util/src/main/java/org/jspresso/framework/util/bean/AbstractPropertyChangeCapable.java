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
package org.jspresso.framework.util.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.basic.BasicAccessorFactory;
import org.jspresso.framework.util.accessor.bean.BeanAccessorFactory;
import org.jspresso.framework.util.accessor.map.MapAccessorFactory;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.lang.ICloneable;

/**
 * Abstract class to build a property change capable bean.
 *
 * @author Vincent Vandenschrick
 * @internal
 */
public abstract class AbstractPropertyChangeCapable implements IPropertyChangeCapable, ICloneable {

  private final static IAccessorFactory ACCESSOR_FACTORY;

  private transient SinglePropertyChangeSupport               propertyChangeSupport;
  private transient SingleWeakPropertyChangeSupport           weakPropertyChangeSupport;
  private transient List<PropertyChangeEvent>                 delayedEvents;
  private transient Map<String, NestedPropertyChangeListener> nestedPropertyChangeListeners;

  static {
    ACCESSOR_FACTORY = new BasicAccessorFactory();
    ((BasicAccessorFactory) ACCESSOR_FACTORY).setBeanAccessorFactory(new BeanAccessorFactory());
    ((BasicAccessorFactory) ACCESSOR_FACTORY).setMapAccessorFactory(new MapAccessorFactory());
  }

  private synchronized void initializePropertyChangeSupportIfNeeded() {
    if (propertyChangeSupport == null) {
      this.propertyChangeSupport = new SinglePropertyChangeSupport(this);
    }
  }

  private synchronized void initializeNestedPropertyChangeListenersIfNeeded() {
    if (nestedPropertyChangeListeners == null) {
      this.nestedPropertyChangeListeners = new HashMap<>();
    }
  }

  private synchronized void initializeWeakPropertyChangeSupportIfNeeded() {
    if (weakPropertyChangeSupport == null) {
      this.weakPropertyChangeSupport = new SingleWeakPropertyChangeSupport(this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    initializePropertyChangeSupportIfNeeded();
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addWeakPropertyChangeListener(PropertyChangeListener listener) {
    initializeWeakPropertyChangeSupportIfNeeded();
    weakPropertyChangeSupport.addPropertyChangeListener(listener);
  }

  private void registerNestedPropertyChangeListenerIfNeeded(String propertyName) {
    int nestedDelimiterIndex = propertyName.indexOf(IAccessor.NESTED_DELIM);
    if (nestedDelimiterIndex >= 0) {
      initializeNestedPropertyChangeListenersIfNeeded();
      String rootPropertyName = propertyName.substring(0, nestedDelimiterIndex);
      final String remainderProperty = propertyName.substring(nestedDelimiterIndex + 1);
      final AbstractPropertyChangeCapable.NestedPropertyChangeListener rootNestedPropertyListener;
      if (nestedPropertyChangeListeners.containsKey(rootPropertyName)) {
        rootNestedPropertyListener = nestedPropertyChangeListeners.get(rootPropertyName);
      } else {
        rootNestedPropertyListener = new AbstractPropertyChangeCapable.NestedPropertyChangeListener(rootPropertyName);
        nestedPropertyChangeListeners.put(rootPropertyName, rootNestedPropertyListener);
      }
      rootNestedPropertyListener.registerNestedProperty(remainderProperty);
      Object rootProperty;
      try {
        rootProperty = ACCESSOR_FACTORY.createPropertyAccessor(rootPropertyName, getClass()).getValue(this);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex, "Could not attach listener on property : " + rootPropertyName);
      }
      if (rootProperty instanceof IPropertyChangeCapable) {
        ((IPropertyChangeCapable) rootProperty).addPropertyChangeListener(remainderProperty,
            rootNestedPropertyListener);
      }
      addPropertyChangeListener(rootPropertyName, new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          Object oldValue = evt.getOldValue();
          Object newValue = evt.getNewValue();
          if (oldValue instanceof IPropertyChangeCapable) {
            ((IPropertyChangeCapable) oldValue).removePropertyChangeListener(remainderProperty,
                rootNestedPropertyListener);
          }
          if (newValue instanceof IPropertyChangeCapable) {
            ((IPropertyChangeCapable) newValue).addPropertyChangeListener(remainderProperty,
                rootNestedPropertyListener);
          }
          rootNestedPropertyListener.fireAllNestedPropertyChanges(oldValue, newValue);
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    initializePropertyChangeSupportIfNeeded();
    registerNestedPropertyChangeListenerIfNeeded(propertyName);
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addWeakPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    initializeWeakPropertyChangeSupportIfNeeded();
    registerNestedPropertyChangeListenerIfNeeded(propertyName);
    weakPropertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractPropertyChangeCapable clone() {
    try {
      AbstractPropertyChangeCapable clonedBean = (AbstractPropertyChangeCapable) super.clone();
      clonedBean.propertyChangeSupport = null;
      clonedBean.weakPropertyChangeSupport = null;
      clonedBean.nestedPropertyChangeListeners = null;
      clonedBean.delayedEvents = null;
      return clonedBean;
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport.removePropertyChangeListener(listener);
    }
    if (weakPropertyChangeSupport != null) {
      weakPropertyChangeSupport.removePropertyChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
    if (weakPropertyChangeSupport != null) {
      weakPropertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
  }

  /**
   * Performs property change firing.
   *
   * @param evt
   *     evt
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)
   */

  protected void firePropertyChange(PropertyChangeEvent evt) {
    Object oldValue = evt.getOldValue();
    Object newValue = evt.getNewValue();
    if (oldValue == null && newValue == null || oldValue != null && oldValue.equals(newValue)) {
      return;
    }
    if (delayedEvents != null) {
      delayedEvents.add(evt);
    } else {
      if (propertyChangeSupport != null) {
        propertyChangeSupport.firePropertyChange(evt);
      }
      if (weakPropertyChangeSupport != null) {
        weakPropertyChangeSupport.firePropertyChange(evt);
      }
    }
  }

  /**
   * Performs property change firing.
   *
   * @param propertyName
   *     propertyName
   * @param oldValue
   *     oldValue
   * @param newValue
   *     newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, * boolean, boolean)
   */
  protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
  }

  /**
   * Performs property change firing.
   *
   * @param propertyName
   *     propertyName
   * @param oldValue
   *     oldValue
   * @param newValue
   *     newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, * int, int)
   */
  protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
    firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
  }

  /**
   * Performs property change firing.
   *
   * @param propertyName
   *     propertyName
   * @param oldValue
   *     oldValue
   * @param newValue
   *     newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, * int, int)
   */
  protected void firePropertyChange(String propertyName, long oldValue, long newValue) {
    firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
  }

  /**
   * Performs property change firing.
   *
   * @param propertyName
   *     propertyName
   * @param oldValue
   *     oldValue
   * @param newValue
   *     newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, * java.lang.Object, java.lang.Object)
   */
  @Override
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
  }

  /**
   * Retrieves listeners.
   *
   * @return all of the {@code PropertyChangeListeners} added or an empty array
   * if no listeners have been added
   *
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  @Override
  public PropertyChangeListener[] getPropertyChangeListeners() {
    ArrayList<PropertyChangeListener> listeners = new ArrayList<>();
    if (propertyChangeSupport != null) {
      for (PropertyChangeListener pcl : propertyChangeSupport.getPropertyChangeListeners()) {
        // do not add single property change listeners
        if (!(pcl instanceof PropertyChangeListenerProxy)) {
          listeners.add(pcl);
        }
      }
    }
    if (weakPropertyChangeSupport != null) {
      listeners.addAll(Arrays.asList(weakPropertyChangeSupport.getPropertyChangeListeners()));
    }
    return listeners.toArray(new PropertyChangeListener[0]);
  }

  /**
   * Retrieves listeners.
   *
   * @param propertyName
   *     propertyName
   * @return all of the {@code PropertyChangeListeners} associated with the
   * named property. If no such listeners have been added, or if
   * {@code propertyName} is null, an empty array is returned.
   *
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(String)
   */
  @Override
  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    ArrayList<PropertyChangeListener> listeners = new ArrayList<>();
    if (propertyChangeSupport != null) {
      listeners.addAll(Arrays.asList(propertyChangeSupport.getPropertyChangeListeners(propertyName)));
    }
    if (weakPropertyChangeSupport != null) {
      listeners.addAll(Arrays.asList(weakPropertyChangeSupport.getPropertyChangeListeners(propertyName)));
    }
    return listeners.toArray(new PropertyChangeListener[0]);
  }

  /**
   * Tests whether there are listeners for the property.
   *
   * @param propertyName
   *     propertyName
   * @return true if there are one or more listeners for the given property.
   *
   * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
   */
  @Override
  public boolean hasListeners(String propertyName) {
    if (propertyChangeSupport != null && propertyChangeSupport.hasListeners(propertyName)) {
      return true;
    }
    return weakPropertyChangeSupport != null && weakPropertyChangeSupport.hasListeners(propertyName);
  }

  /**
   * Delays events propagation by buffering them. When events are unblocked,
   * they get fired in the order they were recorded. {@inheritDoc}
   */
  @Override
  public boolean blockEvents() {
    if (delayedEvents == null) {
      delayedEvents = new ArrayList<>();
      return true;
    }
    return false;
  }

  /**
   * Unblocks event propagation. All events that were buffered are fired.
   * {@inheritDoc}
   */
  @Override
  public void releaseEvents() {
    if (delayedEvents != null) {
      List<PropertyChangeEvent> delayedEventsCopy = new ArrayList<>(delayedEvents);
      delayedEvents = null;
      for (PropertyChangeEvent evt : delayedEventsCopy) {
        firePropertyChange(evt);
      }
    }
  }

  private final class NestedPropertyChangeListener implements PropertyChangeListener {

    private String      rootPropertyName;
    private Set<String> registeredNestedProperties;

    public NestedPropertyChangeListener(String rootPropertyName) {
      this.rootPropertyName = rootPropertyName;
      this.registeredNestedProperties = new LinkedHashSet<>();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      AbstractPropertyChangeCapable.this.firePropertyChange(
          rootPropertyName + IAccessor.NESTED_DELIM + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    public void registerNestedProperty(String nestedProperty) {
      registeredNestedProperties.add(nestedProperty);
    }

    public void fireAllNestedPropertyChanges(Object oldRoot, Object newRoot) {
      for (String nestedPropertyName : registeredNestedProperties) {
        Object oldNestedProperty = null;
        Object newNestedProperty = null;
        try {
          if (oldRoot != null) {
            oldNestedProperty = ACCESSOR_FACTORY.createPropertyAccessor(nestedPropertyName, oldRoot.getClass())
                                                .getValue(oldRoot);
          }
          if (newRoot != null) {
            newNestedProperty = ACCESSOR_FACTORY.createPropertyAccessor(nestedPropertyName, newRoot.getClass())
                                                .getValue(newRoot);
          }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
          throw new NestedRuntimeException(ex, "Could not attach listener on property : " + rootPropertyName);
        }
        AbstractPropertyChangeCapable.this.firePropertyChange(
            rootPropertyName + IAccessor.NESTED_DELIM + nestedPropertyName, oldNestedProperty, newNestedProperty);
      }
    }
  }
}
