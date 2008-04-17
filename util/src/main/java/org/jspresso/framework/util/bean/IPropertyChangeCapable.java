/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean;

import java.beans.PropertyChangeListener;

/**
 * This class needs to be implemented by java beans which are capable of
 * registering a <code>PropertyChangeListener</code>. See also java beans
 * specifications.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @see java.beans.PropertyChangeSupport
 * @author Vincent Vandenschrick
 */
public interface IPropertyChangeCapable {

  /**
   * <code>UNKNOWN</code> value of property. This value may be passed as
   * <code>newValue</code> in <code>PropertyChangeEvents</code>.
   */
  Object UNKNOWN = new Object();

  /**
   * Adds a new <code>PropertyChangeListener</code>.
   * 
   * @param listener
   *            The added listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(
   *      PropertyChangeListener)
   */
  void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Adds a new <code>PropertyChangeListener</code> on a specific property.
   * 
   * @param propertyName
   *            The listened property.
   * @param listener
   *            The added listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String,
   *      PropertyChangeListener)
   */
  void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

  /**
   * Removes a new <code>PropertyChangeListener</code>.
   * 
   * @param listener
   *            The removed listener.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(
   *      PropertyChangeListener)
   */
  void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Removes a <code>PropertyChangeListener</code> on a specific property.
   * 
   * @param propertyName
   *            The listened property.
   * @param listener
   *            The removed listener.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(String,
   *      PropertyChangeListener)
   */
  void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

}
