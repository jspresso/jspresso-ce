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

import org.jspresso.framework.util.IIconImageURLProvider;

/**
 * This public interface is implemented by any tree view descriptor.
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
public interface ITreeViewDescriptor extends IViewDescriptor {

  /**
   * Given a user object, this method qives the ability to the tree view
   * descriptor to return the url of an image used to render the user object.
   * This method may return null.
   * 
   * @param userObject
   *          the user object to render.
   * @return the url of the image to use for the renderer or null.
   */
  String getIconImageURLForUserObject(Object userObject);

  /**
   * Gets the iconImageURLProvider.
   * 
   * @return the iconImageURLProvider.
   */
  IIconImageURLProvider getIconImageURLProvider();

  /**
   * It gets the maximum depth of the tree structure whichis mandatory in case
   * of a recursive one.
   * 
   * @return the maximum tree structure depth.
   */
  int getMaxDepth();

  /**
   * Gets the root tree level descriptor of this tree view.
   * 
   * @return the root tree level descriptor of this tree view.
   */
  ITreeLevelDescriptor getRootSubtreeDescriptor();

}
