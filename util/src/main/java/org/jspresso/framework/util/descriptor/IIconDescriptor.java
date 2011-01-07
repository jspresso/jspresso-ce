/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.descriptor;

/**
 * This interface is implemented by anything which is graphically identifiable
 * by an icon.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IIconDescriptor extends IDescriptor {

  /**
   * Gets the URL of the image used by the icon. For Swing view factory a
   * special kind of URL is supported in the form of
   * <code>classpath:directory/image.ext</code> to be able to load images as
   * classpath resource streams.
   * 
   * @return the image URL.
   */
  String getIconImageURL();

}
