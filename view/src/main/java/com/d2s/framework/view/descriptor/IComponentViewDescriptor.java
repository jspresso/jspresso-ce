/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is implemented by view descriptors which are used to
 * display a model in a formular way. A form view will typically display a
 * subset of a bean simple properties. For instance, this might be implemented
 * by a swing JPanel containing a set of arranged label/widget pairs.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponentViewDescriptor extends IViewDescriptor {

  /**
   * <code>ABOVE</code> location constant.
   */
  int ABOVE = 1;

  /**
   * <code>ASIDE</code> location constant.
   */
  int ASIDE = 2;

  /**
   * Gets the position of the labels naming the displayed properties.
   *
   * @return the relative position of the labels (<code>ABOVE</code> or
   *         <code>ASIDE</code>).
   */
  int getLabelsPosition();

  /**
   * Gets the number of properties displayed in a row. This is actually a
   * maximum value since a property might span multiple columns.
   *
   * @return the number of properties displayed in a row of this view.
   */
  int getColumnCount();

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

  /**
   * Gets the property view descriptors.
   *
   * @return the property view descriptors.
   */
  List<ISubViewDescriptor> getPropertyViewDescriptors();
}
