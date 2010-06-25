/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.basic;

import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptor;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A default implementation for query view descriptor factory.
 * 
 * @version $LastChangedRevision: 1091 $
 * @author Vincent Vandenschrick
 */
public class BasicQueryViewDescriptorFactory implements
    IQueryViewDescriptorFactory {

  private boolean horizontallyResizable;

  /**
   * Constructs a new <code>BasicQueryViewDescriptorFactory</code> instance.
   */
  public BasicQueryViewDescriptorFactory() {
    horizontallyResizable = true;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor createQueryViewDescriptor(
      IComponentDescriptor<Object> queryComponentDescriptor) {
    BasicQueryComponentDescriptor actualModelDescriptor = new BasicQueryComponentDescriptor(
        queryComponentDescriptor);
    BasicComponentViewDescriptor queryComponentViewDescriptor = new BasicComponentViewDescriptor();
    Map<String, Object> propertyWidths = new HashMap<String, Object>();
    for (String queriableProperty : queryComponentDescriptor
        .getQueryableProperties()) {
      // To preserve col spans for query structures.
      propertyWidths.put(queriableProperty, new Integer(3));
    }
    queryComponentViewDescriptor.setPropertyWidths(propertyWidths);
    queryComponentViewDescriptor.setColumnCount(6);

    IViewDescriptor queryViewDescriptor;
    if (horizontallyResizable) {
      queryComponentViewDescriptor.setModelDescriptor(actualModelDescriptor);
      queryComponentViewDescriptor.setBorderType(EBorderType.TITLED);
      queryViewDescriptor = queryComponentViewDescriptor;
    } else {
      BasicBorderViewDescriptor borderViewDescriptor = new BasicBorderViewDescriptor();
      borderViewDescriptor.setWestViewDescriptor(queryComponentViewDescriptor);

      borderViewDescriptor.setModelDescriptor(actualModelDescriptor);
      borderViewDescriptor.setBorderType(EBorderType.TITLED);
      queryViewDescriptor = borderViewDescriptor;
    }

    return queryViewDescriptor;
  }

  /**
   * Sets the horizontallyResizable.
   * 
   * @param horizontallyResizable
   *          the horizontallyResizable to set.
   */
  public void setHorizontallyResizable(boolean horizontallyResizable) {
    this.horizontallyResizable = horizontallyResizable;
  }
}
