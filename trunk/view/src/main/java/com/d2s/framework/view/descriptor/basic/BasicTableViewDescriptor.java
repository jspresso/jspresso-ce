/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;
import com.d2s.framework.view.descriptor.ITableViewDescriptor;

/**
 * Default implementation of a table view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
  }

}
