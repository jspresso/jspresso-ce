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
package org.jspresso.framework.application.frontend.command.remote;

/**
 * A command to transfer selection of a remote peer.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteSelectionCommand extends RemoteCommand {

  private static final long serialVersionUID = 8776670820353559196L;

  private int               leadingIndex;
  private int[]             selectedIndices;

  /**
   * Gets the leadingIndex.
   *
   * @return the leadingIndex.
   */
  public int getLeadingIndex() {
    return leadingIndex;
  }

  /**
   * Gets the selectedIndices.
   *
   * @return the selectedIndices.
   */
  public int[] getSelectedIndices() {
    return selectedIndices;
  }

  /**
   * Sets the leadingIndex.
   *
   * @param leadingIndex
   *          the leadingIndex to set.
   */
  public void setLeadingIndex(int leadingIndex) {
    this.leadingIndex = leadingIndex;
  }

  /**
   * Sets the selectedIndices.
   *
   * @param selectedIndices
   *          the selectedIndices to set.
   */
  public void setSelectedIndices(int... selectedIndices) {
    this.selectedIndices = selectedIndices;
  }

}
