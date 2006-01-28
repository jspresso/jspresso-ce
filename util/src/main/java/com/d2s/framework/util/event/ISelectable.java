/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.event;

/**
 * this interface is implemented by components supporting selection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISelectable {

  /**
   * Adds a new selection listener to this selectable.
   * 
   * @param listener
   *          the added listener.
   */
  void addSelectionChangeListener(ISelectionChangeListener listener);

  /**
   * Removes a selection listener from this selectable.
   * 
   * @param listener
   *          the removed listener.
   */
  void removeSelectionChangeListener(ISelectionChangeListener listener);

  /**
   * Sets the selected indices in this selectable.
   * 
   * @param selectedIndices
   *          the indices to set selected.
   */
  void setSelectedIndices(int[] selectedIndices);

  /**
   * Gets the selected indices in this selectable.
   * 
   * @return the indices to set selected.
   */
  int[] getSelectedIndices();
}
