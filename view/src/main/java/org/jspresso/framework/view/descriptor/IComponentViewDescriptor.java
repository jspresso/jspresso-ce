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

import java.util.List;

/**
 * This public interface is implemented by view descriptors which are used to
 * display a model in a formular way. A form view will typically display a
 * subset of a bean simple properties. For instance, this might be implemented
 * by a swing JPanel containing a set of arranged label/widget pairs.
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
public interface IComponentViewDescriptor extends IViewDescriptor {

  /**
   * Gets the number of properties displayed in a row. This is actually a
   * maximum value since a property might span multiple columns.
   * 
   * @return the number of properties displayed in a row of this view.
   */
  int getColumnCount();

  /**
   * Gets the position of the labels naming the displayed properties.
   * 
   * @return the relative position of the labels (<code>ABOVE</code> or
   *         <code>ASIDE</code>).
   */
  ELabelPosition getLabelsPosition();

  /**
   * Gets the property view descriptors.
   * 
   * @return the property view descriptors.
   */
  List<ISubViewDescriptor> getPropertyViewDescriptors();

  /**
   * Gets the number of columns a property spans when displayed.
   * 
   * @param propertyName
   *          the name of the property.
   * @return the spanned column count.
   */
  int getPropertyWidth(String propertyName);

  /**
   * Gets the child properties to display in case of a complex property.
   * 
   * @param propertyName
   *          the name of the property.
   * @return The list of displayed properties in the case of a complex property.
   */
  List<String> getRenderedChildProperties(String propertyName);
}
