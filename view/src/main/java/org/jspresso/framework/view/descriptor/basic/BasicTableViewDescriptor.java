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
package org.jspresso.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;

/**
 * Default implementation of a table view descriptor.
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
public class BasicTableViewDescriptor extends BasicCollectionViewDescriptor
    implements ITableViewDescriptor {

  private List<IPropertyViewDescriptor> columnViewDescriptors;
  private List<String>                  renderedProperties;

  /**
   * Gets the renderedProperties.
   * 
   * @return the renderedProperties.
   */
  private List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      renderedProperties = ((ICollectionDescriptorProvider<?>) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor()
          .getRenderedProperties();
    }
    return renderedProperties;
  }

  /**
   * Sets the renderedProperties.
   * 
   * @param renderedProperties
   *          the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = renderedProperties;
  }

  /**
   * {@inheritDoc}
   */
  public List<IPropertyViewDescriptor> getColumnViewDescriptors() {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor());
    IComponentDescriptor<?> rowModelDescriptor = modelDescriptor
        .getCollectionDescriptor().getElementDescriptor();
    if (columnViewDescriptors == null) {
      List<String> viewRenderedProperties = getRenderedProperties();
      if (modelDescriptor instanceof ICollectionPropertyDescriptor
          && ((ICollectionPropertyDescriptor<?>) modelDescriptor)
              .getReverseRelationEnd() != null) {
        viewRenderedProperties
            .remove(((ICollectionPropertyDescriptor<?>) modelDescriptor)
                .getReverseRelationEnd().getName());
      }
      List<IPropertyViewDescriptor> defaultColumnViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
      for (String renderedProperty : viewRenderedProperties) {
        BasicPropertyViewDescriptor columnDescriptor = new BasicPropertyViewDescriptor();
        columnDescriptor.setName(renderedProperty);
        columnDescriptor.setModelDescriptor(rowModelDescriptor
            .getPropertyDescriptor(renderedProperty));
        defaultColumnViewDescriptors.add(columnDescriptor);
      }
      return defaultColumnViewDescriptors;
    }
    List<IPropertyViewDescriptor> actualPropertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
    for (IPropertyViewDescriptor propertyViewDescriptor : columnViewDescriptors) {
      actualPropertyViewDescriptors.addAll(explodeComponentReferences(
          propertyViewDescriptor, rowModelDescriptor));
    }
    return columnViewDescriptors;
  }

  private List<IPropertyViewDescriptor> explodeComponentReferences(
      IPropertyViewDescriptor propertyViewDescriptor,
      IComponentDescriptorProvider<?> componentDescriptorProvider) {
    List<IPropertyViewDescriptor> returnedList = new ArrayList<IPropertyViewDescriptor>();
    IPropertyDescriptor propertyDescriptor = componentDescriptorProvider
        .getComponentDescriptor().getPropertyDescriptor(
            propertyViewDescriptor.getName());
    if ((propertyDescriptor instanceof IReferencePropertyDescriptor<?> && !IEntity.class
        .isAssignableFrom(((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getComponentContract()))) {
      for (String nestedRenderedProperty : ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor().getRenderedProperties()) {
        BasicPropertyViewDescriptor nestedPropertyViewDescriptor = new BasicPropertyViewDescriptor();
        nestedPropertyViewDescriptor.setName(propertyDescriptor.getName() + "."
            + nestedRenderedProperty);
        nestedPropertyViewDescriptor.setModelDescriptor(propertyDescriptor);
        if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
          returnedList.addAll(explodeComponentReferences(
              nestedPropertyViewDescriptor,
              (IReferencePropertyDescriptor<?>) propertyDescriptor));
        } else {
          returnedList.add(nestedPropertyViewDescriptor);
        }
      }
    } else {
      if (propertyViewDescriptor.getModelDescriptor() == null
          && propertyViewDescriptor instanceof BasicPropertyViewDescriptor) {
        ((BasicPropertyViewDescriptor) propertyViewDescriptor)
            .setModelDescriptor(propertyDescriptor);
      }
      returnedList.add(propertyViewDescriptor);
    }
    return returnedList;
  }

  /**
   * Sets the columnViewDescriptors.
   * 
   * @param columnViewDescriptors
   *          the columnViewDescriptors to set.
   */
  public void setColumnViewDescriptors(
      List<IPropertyViewDescriptor> columnViewDescriptors) {
    this.columnViewDescriptors = columnViewDescriptors;
  }

}
