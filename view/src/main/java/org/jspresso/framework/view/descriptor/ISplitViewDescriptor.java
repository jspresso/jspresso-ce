/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

/**
 * This public interface is implemented by a composite view descriptor which
 * organizes 2 view descriptors in a splitted manner. The described view can
 * typically be implemented using a swing JSplitPane.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
public interface ISplitViewDescriptor extends ICompositeViewDescriptor {

  /**
   * <code>HORIZONTAL</code> location constant.
   */
  int HORIZONTAL = 1;

  /**
   * <code>VERTICAL</code> location constant.
   */
  int VERTICAL   = 2;

  /**
   * Gets the left / top sub view descriptor of this split composite view
   * descriptor.
   * 
   * @return the left / top sub view descriptor.
   */
  IViewDescriptor getLeftTopViewDescriptor();

  /**
   * Gets the orientation of the split described view.
   * 
   * @return the split orientation. The admissible values are :
   *         <li>VERTICAL
   *         <li>HORIZONTAL
   */
  int getOrientation();

  /**
   * Gets the right / bottom sub view descriptor of this split composite view
   * descriptor.
   * 
   * @return the right / bottom sub view descriptor.
   */
  IViewDescriptor getRightBottomViewDescriptor();
}
