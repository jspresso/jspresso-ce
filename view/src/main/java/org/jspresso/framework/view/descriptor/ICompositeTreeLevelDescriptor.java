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

import java.util.List;

/**
 * This interface is implemented by descriptors of composite tree levels.
 * composite tree levels are tree levels where children are organised into sub
 * groups.
 *
 * @author Vincent Vandenschrick
 */
public interface ICompositeTreeLevelDescriptor extends ITreeLevelDescriptor {

  /**
   * Gets the subtree descriptor from its name.
   *
   * @param name
   *          the name of the child tree level descriptor.
   * @return the subtree descriptor.
   */
  ITreeLevelDescriptor getChildDescriptor(String name);

  /**
   * Gets the subtree descriptors.
   *
   * @return the subtree descriptors.
   */
  List<ITreeLevelDescriptor> getChildrenDescriptors();
}
