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
import org.jspresso.framework.view.descriptor.ISubViewDescriptor;
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

  private List<ISubViewDescriptor> columnViewDescriptors;

  /**
   * {@inheritDoc}
   */
  public List<ISubViewDescriptor> getColumnViewDescriptors() {
    if (columnViewDescriptors == null) {
      ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor());
      IComponentDescriptor<?> rowModelDescriptor = modelDescriptor
          .getCollectionDescriptor().getElementDescriptor();
      List<String> modelRenderedProperties = rowModelDescriptor
          .getRenderedProperties();
      if (modelDescriptor instanceof ICollectionPropertyDescriptor
          && ((ICollectionPropertyDescriptor<?>) modelDescriptor)
              .getReverseRelationEnd() != null) {
        modelRenderedProperties
            .remove(((ICollectionPropertyDescriptor<?>) modelDescriptor)
                .getReverseRelationEnd().getName());
      }
      List<ISubViewDescriptor> defaultColumnViewDescriptors = new ArrayList<ISubViewDescriptor>();
      for (String renderedProperty : modelRenderedProperties) {
        BasicSubviewDescriptor columnDescriptor = new BasicSubviewDescriptor();
        columnDescriptor.setName(renderedProperty);
        columnDescriptor.setGrantedRoles(rowModelDescriptor
            .getPropertyDescriptor(renderedProperty).getGrantedRoles());
        defaultColumnViewDescriptors.add(columnDescriptor);
      }
      return defaultColumnViewDescriptors;
    }
    return columnViewDescriptors;
  }

  /**
   * Sets the columnViewDescriptors.
   * 
   * @param columnViewDescriptors
   *            the columnViewDescriptors to set.
   */
  public void setColumnViewDescriptors(
      List<ISubViewDescriptor> columnViewDescriptors) {
    this.columnViewDescriptors = columnViewDescriptors;
    if (columnViewDescriptors != null) {
      ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor());
      if (modelDescriptor != null) {
        IComponentDescriptor<?> rowModelDescriptor = modelDescriptor
            .getCollectionDescriptor().getElementDescriptor();
        for (ISubViewDescriptor columnViewDescriptor : columnViewDescriptors) {
          columnViewDescriptor.setGrantedRoles(rowModelDescriptor
              .getPropertyDescriptor(columnViewDescriptor.getName())
              .getGrantedRoles());
        }
      }
    }
  }

}
