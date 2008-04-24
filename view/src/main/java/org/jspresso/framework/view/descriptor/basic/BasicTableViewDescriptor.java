/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
      IComponentDescriptor<?> rowModelDescriptor = modelDescriptor
          .getCollectionDescriptor().getElementDescriptor();
      for (ISubViewDescriptor columnViewDescriptor : getColumnViewDescriptors()) {
        columnViewDescriptor.setGrantedRoles(rowModelDescriptor
            .getPropertyDescriptor(columnViewDescriptor.getName())
            .getGrantedRoles());
      }
    }
  }

}
