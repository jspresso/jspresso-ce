/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A simple class used to record bean property changes. Original values of
 * changed properties are also kept.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanPropertyChangeRecorder implements PropertyChangeListener {

  private Map<IPropertyChangeCapable, Map<String, Object>> changedPropertiesMap;
  private boolean                                          enabled;
  private PropertyChangeListener                           interceptor;

  /**
   * Constructs a new <code>BeanPropertyChangeRecorder</code> instance.
   */
  public BeanPropertyChangeRecorder() {
    this(null);
  }

  /**
   * Constructs a new <code>BeanPropertyChangeRecorder</code> instance.
   * 
   * @param interceptor
   *          an optional property change listener which will be notified of
   *          changed properties.
   */
  public BeanPropertyChangeRecorder(PropertyChangeListener interceptor) {
    changedPropertiesMap = new WeakHashMap<IPropertyChangeCapable, Map<String, Object>>();
    enabled = true;
    this.interceptor = interceptor;
  }

  /**
   * Gets the changed properties which have been recorded since this recorder
   * have started on the specified bean.
   * 
   * @param bean
   *          the bean to get the changed properties of.
   * @return the set of changed properties on the bean.
   */
  public Map<String, Object> getChangedProperties(IPropertyChangeCapable bean) {
    return changedPropertiesMap.get(bean);
  }

  /**
   * Gets the enabled.
   * 
   * @return the enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * {@inheritDoc}
   */
  public void propertyChange(PropertyChangeEvent evt) {
    // don't store nested property changes
    if (enabled && evt.getPropertyName().indexOf('.') < 0) {
      Map<String, Object> changedProperties = changedPropertiesMap.get(evt
          .getSource());
      if (!changedProperties.containsKey(evt.getPropertyName())) {
        changedProperties.put(evt.getPropertyName(), evt.getOldValue());
      }
      if (interceptor != null) {
        interceptor.propertyChange(evt);
      }
    }
  }

  /**
   * Starts the property change recording on a bean.
   * 
   * @param bean
   *          the bean to record the property changes of.
   * @param initialChangedProperties
   *          the set of initial changed properties.
   */
  public void register(IPropertyChangeCapable bean,
      Map<String, Object> initialChangedProperties) {
    if (initialChangedProperties != null) {
      changedPropertiesMap.put(bean, new HashMap<String, Object>(
          initialChangedProperties));
    } else {
      changedPropertiesMap.put(bean, new HashMap<String, Object>());
    }
    bean.addPropertyChangeListener(this);
  }

  /**
   * Resets the changed properties recording on the bean specified to a
   * specified set of changed properties.
   * 
   * @param bean
   *          the bean to reset the recording of.
   * @param changedProperties
   *          a new set of changed properties.
   */
  public void resetChangedProperties(IPropertyChangeCapable bean,
      Map<String, Object> changedProperties) {
    Map<String, Object> recordedChangedProperties = changedPropertiesMap
        .get(bean);
    if (recordedChangedProperties != null) {
      recordedChangedProperties.clear();
      if (changedProperties != null) {
        recordedChangedProperties.putAll(changedProperties);
      }
    }
  }

  /**
   * Sets the enabled.
   * 
   * @param enabled
   *          the enabled to set.
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Stops the recording of property changes on a bean.
   * 
   * @param bean
   *          the bean to stop the property changes recording on.
   */
  public void unregister(IPropertyChangeCapable bean) {
    changedPropertiesMap.remove(bean);
  }

  /**
   * Gets the set of beans registered in this recorder.
   * 
   * @return the set of beans registered in this recorder.
   */
  public Set<IPropertyChangeCapable> getRegistered() {
    return changedPropertiesMap.keySet();
  }
}
