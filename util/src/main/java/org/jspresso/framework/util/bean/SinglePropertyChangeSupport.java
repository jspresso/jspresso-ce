/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This property change support prevents from adding twice the same property
 * change listener.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SinglePropertyChangeSupport extends PropertyChangeSupport {

  private static final long serialVersionUID = -3547472625502417905L;

  /**
   * Constructs a new <code>SinglePropertyChangeSupport</code> instance.
   * 
   * @param sourceBean
   *            the source bean.
   */
  public SinglePropertyChangeSupport(Object sourceBean) {
    super(sourceBean);
  }

  /**
   * Checks listener uniqueness before performing.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public synchronized void addPropertyChangeListener(
      PropertyChangeListener listener) {
    if (checkUniqueness(null, listener)) {
      super.addPropertyChangeListener(listener);
    }
  }

  /**
   * Checks listener uniqueness before performing.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public synchronized void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (checkUniqueness(propertyName, listener)) {
      super.addPropertyChangeListener(propertyName, listener);
    }
  }

  private boolean checkUniqueness(String propertyName,
      PropertyChangeListener listener) {
    PropertyChangeListener[] containedListeners;
    if (propertyName == null) {
      containedListeners = getPropertyChangeListeners();
    } else {
      containedListeners = getPropertyChangeListeners(propertyName);
    }
    for (int i = 0; i < containedListeners.length; i++) {
      if (containedListeners[i] == listener) {
        return false;
      }
    }
    return true;
  }
}
