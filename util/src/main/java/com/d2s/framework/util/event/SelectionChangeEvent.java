/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.event;

import java.util.Arrays;
import java.util.EventObject;

/**
 * This kind of events gets delivered whenever a selectable detects a change in
 * its selection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SelectionChangeEvent extends EventObject {

  private static final long serialVersionUID = -3165899293226869795L;

  private int[]             newSelection;
  private int[]             oldSelection;
  private int               leadingIndex;

  /**
   * Constructs a new <code>SelectionChangeEvent</code>.
   * 
   * @param source
   *          The object that initiated the event.
   * @param oldSelection
   *          The old selection of the source.
   * @param newSelection
   *          The new selection of the source.
   * @param leadingIndex
   *          the leadingIndex selecton index.
   */
  public SelectionChangeEvent(ISelectable source, int[] oldSelection,
      int[] newSelection, int leadingIndex) {
    super(source);
    this.newSelection = newSelection;
    this.oldSelection = oldSelection;
    if (this.newSelection != null) {
      Arrays.sort(this.newSelection);
    }
    if (this.oldSelection != null) {
      Arrays.sort(this.oldSelection);
    }
    this.leadingIndex = leadingIndex;
  }

  /**
   * Gets the new selection. The indices array is ordered.
   * 
   * @return the new selection.
   */
  public int[] getNewSelection() {
    return newSelection;
  }

  /**
   * Gets the old selection. The indices array is ordered.
   * 
   * @return the old selection.
   */
  public int[] getOldSelection() {
    return oldSelection;
  }

  /**
   * Narrows return type.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ISelectable getSource() {
    return (ISelectable) source;
  }

  /**
   * Gets the leadingIndex.
   * 
   * @return the leadingIndex.
   */
  public int getLeadingIndex() {
    return leadingIndex;
  }
}
