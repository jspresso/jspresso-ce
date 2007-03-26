/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.event;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Helper class to ease the <code>ISelectionChangeListener</code> management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SelectionChangeSupport implements ISelectable {

  private ISelectable                   source;

  private Set<ISelectionChangeListener> listeners;
  private Set<ISelectionChangeListener> inhibitedListeners;

  private int[]                         oldSelectedIndices;
  private int[]                         selectedIndices;
  private int                           leadingIndex;

  /**
   * Constructs a new support.
   * 
   * @param source
   *          The selectable to which this support is attached. It will serve as
   *          <code>source</code> of fired <code>SelectionChangeEvent</code>s
   *          if no other is provided.
   */
  public SelectionChangeSupport(ISelectable source) {
    if (source == null) {
      throw new NullPointerException();
    }
    this.source = source;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void addSelectionChangeListener(
      ISelectionChangeListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new LinkedHashSet<ISelectionChangeListener>();
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void removeSelectionChangeListener(
      ISelectionChangeListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setSelectedIndices(int[] selectedIndices) {
    int leadingInd = -1;
    if (selectedIndices != null && selectedIndices.length > 0) {
      leadingInd = selectedIndices[selectedIndices.length - 1];
    }
    setSelectedIndices(selectedIndices, leadingInd);
  }

  /**
   * {@inheritDoc}
   */
  public void setSelectedIndices(int[] selectedInds, int leadingInd) {
    this.oldSelectedIndices = this.selectedIndices;
    this.selectedIndices = selectedInds;
    this.leadingIndex = leadingInd;
    fireSelectionChange();
  }

  /**
   * {@inheritDoc}
   */
  public int[] getSelectedIndices() {
    return selectedIndices;
  }

  /**
   * Fires a new <code>SelectionChangeEvent</code> built with
   * <code>source</code> as source and parameters as old and new values.
   */
  public void fireSelectionChange() {
    SelectionChangeEvent evt = new SelectionChangeEvent(source,
        oldSelectedIndices, selectedIndices, leadingIndex);
    fireSelectionChange(evt);
  }

  /**
   * Propagates the <code>SelectionChangeEvent</code> as is (i.e. whithout
   * modifying its source) to the listeners.
   * 
   * @param evt
   *          the propagated <code>BeanChangeEvent</code>
   */
  public void fireSelectionChange(SelectionChangeEvent evt) {
    if (listeners != null) {
      int[] oldSelection = evt.getOldSelection();
      int[] newSelection = evt.getNewSelection();
      if (oldSelection == null && newSelection == null) {
        return;
      }
      if (oldSelection != null && newSelection != null
          && Arrays.equals(oldSelection, newSelection)) {
        return;
      }
      for (ISelectionChangeListener listener : listeners) {
        if (inhibitedListeners == null
            || !inhibitedListeners.contains(listener)) {
          listener.selectionChange(evt);
        }
      }
    }
  }

  /**
   * Registers a listener to be excluded (generally temporarily) from the
   * notification process without being removed from the actual listeners
   * collection.
   * 
   * @param listener
   *          the excluded listener.
   */
  public void addInhibitedListener(ISelectionChangeListener listener) {
    if (inhibitedListeners == null && listener != null) {
      inhibitedListeners = new HashSet<ISelectionChangeListener>();
    }
    inhibitedListeners.add(listener);
  }

  /**
   * Registers a listener to be re-included to the notification process without
   * being re-added to the actual listeners collection.
   * 
   * @param listener
   *          the previously excluded listener.
   */
  public void removeInhibitedListener(ISelectionChangeListener listener) {
    if (inhibitedListeners == null || listener == null) {
      return;
    }
    inhibitedListeners.remove(listener);
  }
}
