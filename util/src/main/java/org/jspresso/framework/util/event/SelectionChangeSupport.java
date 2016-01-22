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
package org.jspresso.framework.util.event;

import java.util.Arrays;
import java.util.Set;

import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;

/**
 * Helper class to ease the {@code ISelectionChangeListener} management.
 *
 * @author Vincent Vandenschrick
 */
public class SelectionChangeSupport implements ISelectable {

  private Set<ISelectionChangeListener> inhibitedListeners;

  private int                           leadingIndex;
  private Set<ISelectionChangeListener> listeners;

  private int[]                         oldSelectedIndices;
  private int[]                         selectedIndices;
  private ISelectable                   source;

  /**
   * Constructs a new support.
   *
   * @param source
   *          The selectable to which this support is attached. It will serve as
   *          {@code source} of fired {@code SelectionChangeEvent}s if
   *          no other is provided.
   */
  public SelectionChangeSupport(ISelectable source) {
    if (source == null) {
      throw new NullPointerException();
    }
    this.source = source;
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
      inhibitedListeners = new TLinkedHashSet<>(1);
    }
    if (inhibitedListeners != null) {
      inhibitedListeners.add(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addSelectionChangeListener(
      ISelectionChangeListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new THashSet<>(1);
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Fires a new {@code SelectionChangeEvent} built with
   * {@code source} as source and parameters as old and new values.
   */
  public void fireSelectionChange() {
    SelectionChangeEvent evt = new SelectionChangeEvent(source,
        oldSelectedIndices, selectedIndices, leadingIndex);
    fireSelectionChange(evt);
  }

  /**
   * Propagates the {@code SelectionChangeEvent} as is (i.e. without
   * modifying its source) to the listeners.
   *
   * @param evt
   *          the propagated {@code BeanChangeEvent}
   */
  public void fireSelectionChange(SelectionChangeEvent evt) {
    if (listeners != null) {
      int[] oldSelection = evt.getOldSelection();
      if (oldSelection != null && oldSelection.length == 0) {
        oldSelection = null;
      }
      int[] newSelection = evt.getNewSelection();
      if (newSelection != null && newSelection.length == 0) {
        newSelection = null;
      }
      if (oldSelection == null && newSelection == null) {
        return;
      }
      if (oldSelection != null && newSelection != null
          && Arrays.equals(oldSelection, newSelection)) {
        return;
      }
      for (ISelectionChangeListener listener : listeners.toArray(new ISelectionChangeListener[listeners.size()])) {
        if (inhibitedListeners == null
            || !inhibitedListeners.contains(listener)) {
          listener.selectionChange(evt);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int[] getSelectedIndices() {
    return selectedIndices;
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
    if (inhibitedListeners.size() == 0) {
      inhibitedListeners = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void removeSelectionChangeListener(
      ISelectionChangeListener listener) {
    if (listeners == null || listener == null) {
      return;
    }
    listeners.remove(listener);
    if (listeners.size() == 0) {
      listeners = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedIndices(int... selectedIndices) {
    int leadingInd = -1;
    if (selectedIndices != null && selectedIndices.length > 0) {
      leadingInd = selectedIndices[selectedIndices.length - 1];
    }
    setSelectedIndices(selectedIndices, leadingInd);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedIndices(int[] selectedInds, int leadingInd) {
    this.oldSelectedIndices = this.selectedIndices;
    this.selectedIndices = selectedInds;
    this.leadingIndex = leadingInd;
    fireSelectionChange();
  }
}
