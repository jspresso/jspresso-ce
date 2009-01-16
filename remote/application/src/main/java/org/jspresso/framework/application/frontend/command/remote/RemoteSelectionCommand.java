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
package org.jspresso.framework.application.frontend.command.remote;


/**
 * A command to transfer selection of a remote peer.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteSelectionCommand extends RemoteCommand {

  private int[]                  selectedIndices;
  private int                    leadingIndex;
  
  /**
   * Gets the selectedIndices.
   * 
   * @return the selectedIndices.
   */
  public int[] getSelectedIndices() {
    return selectedIndices;
  }
  
  /**
   * Sets the selectedIndices.
   * 
   * @param selectedIndices the selectedIndices to set.
   */
  public void setSelectedIndices(int[] selectedIndices) {
    this.selectedIndices = selectedIndices;
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
   * Sets the leadingIndex.
   * 
   * @param leadingIndex the leadingIndex to set.
   */
  public void setLeadingIndex(int leadingIndex) {
    this.leadingIndex = leadingIndex;
  }

}
