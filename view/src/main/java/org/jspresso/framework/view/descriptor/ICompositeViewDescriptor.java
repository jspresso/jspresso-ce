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
 * This public interface is the super-interface of all view descriptors used as
 * containers for others.
 *
 * @author Vincent Vandenschrick
 */
public interface ICompositeViewDescriptor extends IViewDescriptor {

  /**
   * Gets the child view descriptors.
   *
   * @return the list of contained view descriptors.
   */
  List<IViewDescriptor> getChildViewDescriptors();

  /**
   * Gets whether this composite view is a master / detail view. When such a
   * master / detail composite view is built, each added child is bound to the
   * previous one in a master / detail relationship.
   *
   * @return true if the child views are in a master / detail relationship.
   */
  boolean isCascadingModels();

}
