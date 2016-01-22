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
 * This public interface is implemented by view descriptors which are used to
 * display a model in a form way. A form view will typically display a
 * subset of a bean simple properties. For instance, this might be implemented
 * by a swing JPanel containing a set of arranged label/widget pairs.
 *
 * @author Vincent Vandenschrick
 */
public interface IComponentViewDescriptor extends IScrollableViewDescriptor {

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
   * @return the relative position of the labels ({@code ABOVE} or
   *         {@code ASIDE}).
   */
  ELabelPosition getLabelsPosition();

  /**
   * Gets the property view descriptors.
   *
   * @return the property view descriptors.
   */
  List<IPropertyViewDescriptor> getPropertyViewDescriptors();

  /**
   * Gets the property view descriptors.
   *
   * @param explodeComponentReferences explode component references ?
   * @return the property view descriptors.
   */
  List<IPropertyViewDescriptor> getPropertyViewDescriptors(boolean explodeComponentReferences);

  /**
   * Gets label horizontal position.
   *
   * @return the label horizontal position
   */
  EHorizontalPosition getLabelsHorizontalPosition();

  /**
   * Gets label font.
   *
   * @return the label font
   */
  String getLabelFont();

  /**
   * Gets value font.
   *
   * @return the value font
   */
  String getValueFont();

  /**
   * Should the form width fill avalable width.
   *
   * @return the boolean
   */
  boolean isWidthResizeable();
}
