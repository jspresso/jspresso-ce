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
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.automation.IPermIdSource;

/**
 * This interface is implemented by descriptors of a subtree.
 *
 * @author Vincent Vandenschrick
 */
public interface ITreeLevelDescriptor extends ISecurable, IPermIdSource {

  /**
   * Gets the list descriptor describing the collection of nodes composing the
   * tree level this subtree starts at.
   *
   * @return the list view descriptor describing the node collection this
   *         subtree is headed by.
   */
  IListViewDescriptor getNodeGroupDescriptor();
}
