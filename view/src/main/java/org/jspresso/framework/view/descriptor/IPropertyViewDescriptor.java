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
package org.jspresso.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is implemented by view descriptors which are just
 * presenting a property.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IPropertyViewDescriptor extends IViewDescriptor {

  /**
   * Gets the child properties to display in case of a complex property.
   * 
   * @return The list of displayed properties in the case of a complex property.
   */
  List<String> getRenderedChildProperties();

  /**
   * When used in a component descriptor, gets the number of columns a property
   * spans when displayed.
   * 
   * @return the spanned column count.
   */
  Integer getWidth();

  /**
   * Gets the label background.
   * 
   * @return the label background.
   */
  String getLabelBackground();

  /**
   * Gets the label font.
   * 
   * @return the label font.
   */
  String getLabelFont();

  /**
   * Gets the label foreground.
   * 
   * @return the label foreground.
   */
  String getLabelForeground();
}
