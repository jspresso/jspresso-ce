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
package org.jspresso.framework.gui.remote;

import java.beans.PropertyChangeListener;

import org.jspresso.framework.util.bean.SinglePropertyChangeSupport;

/**
 * A container with tabbed children views.
 *
 * @author Vincent Vandenschrick
 */
public class RTabContainer extends RContainer {

  private static final long           serialVersionUID = 8976562094649779477L;

  private RComponent[]                tabs;
  private int                         selectedIndex;
  private SinglePropertyChangeSupport propertyChangeSupport;

  /**
   * Constructs a new {@code RTabContainer} instance.
   *
   * @param guid
   *          the guid
   */
  public RTabContainer(String guid) {
    super(guid);
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
  }

  /**
   * Constructs a new {@code RTabContainer} instance. Only used for
   * serialization support.
   */
  public RTabContainer() {
    // For serialization support
  }

  /**
   * Gets the tabs.
   *
   * @return the tabs.
   */
  public RComponent[] getTabs() {
    return tabs;
  }

  /**
   * Sets the tabs.
   *
   * @param tabs
   *          the tabs to set.
   */
  public void setTabs(RComponent... tabs) {
    this.tabs = tabs;
  }

  /**
   * Gets the selectedIndex.
   *
   * @return the selectedIndex.
   */
  public int getSelectedIndex() {
    return selectedIndex;
  }

  /**
   * Sets the selectedIndex.
   *
   * @param selectedIndex
   *          the selectedIndex to set.
   */
  public void setSelectedIndex(int selectedIndex) {
    int oldSelectedIndex = this.selectedIndex;
    this.selectedIndex = selectedIndex;
    propertyChangeSupport.firePropertyChange("selectedIndex", oldSelectedIndex,
        selectedIndex);
  }

  /**
   * Delegates to support.
   * @param listener the listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Delegates to support.
   * @param listener the listener.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Delegates to support.
   * @param propertyName the property name.
   * @param listener the listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Delegates to support.
   * @param propertyName the property name.
   * @param listener the listener.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

}
