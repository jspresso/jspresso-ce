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
import java.util.EventObject;

/**
 * This kind of events gets delivered whenever a selectable detects a change in
 * its selection.
 *
 * @author Vincent Vandenschrick
 */
public class SelectionChangeEvent extends EventObject {

  private static final long serialVersionUID = -3165899293226869795L;

  private final int               leadingIndex;
  private final int[]             newSelection;
  private final int[]             oldSelection;

  /**
   * Constructs a new {@code SelectionChangeEvent}.
   *
   * @param source
   *          The object that initiated the event.
   * @param oldSelection
   *          The old selection of the source.
   * @param newSelection
   *          The new selection of the source.
   * @param leadingIndex
   *          the leadingIndex selection index.
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
   * Gets the leadingIndex.
   *
   * @return the leadingIndex.
   */
  public int getLeadingIndex() {
    return leadingIndex;
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
}
