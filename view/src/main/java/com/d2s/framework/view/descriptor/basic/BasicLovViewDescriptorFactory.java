/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;
import com.d2s.framework.view.descriptor.ILovViewDescriptorFactory;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A default implementation for lov view factories.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicLovViewDescriptorFactory implements ILovViewDescriptorFactory {

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor createLovViewDescriptor(
      IEntityDescriptor entityDescriptor) {
    BasicSplitViewDescriptor lovViewDescriptor = new BasicSplitViewDescriptor();
    lovViewDescriptor.setMasterDetail(true);
    lovViewDescriptor
        .setLeftTopViewDescriptor(createQueryComponentViewDescriptor(entityDescriptor));
    lovViewDescriptor
        .setRightBottomViewDescriptor(createResultViewDescriptor(entityDescriptor));
    return lovViewDescriptor;

  }

  private IViewDescriptor createQueryComponentViewDescriptor(
      IEntityDescriptor entityDescriptor) {
    BasicComponentViewDescriptor queryComponentViewDescriptor = new BasicComponentViewDescriptor();
    queryComponentViewDescriptor.setModelDescriptor(entityDescriptor);
    queryComponentViewDescriptor.setName("queryEntity");
    queryComponentViewDescriptor.setDescription("queryEntityView");
    queryComponentViewDescriptor
        .setLabelsPosition(IComponentViewDescriptor.ASIDE);
    queryComponentViewDescriptor.setColumnCount(2);

    List<String> queryProperties = new ArrayList<String>();
    for (String queryProperty : entityDescriptor.getQueryableProperties()) {
      IPropertyDescriptor propertyDescriptor = entityDescriptor
          .getPropertyDescriptor(queryProperty);
      if (propertyDescriptor.isQueryable()) {
        queryProperties.add(queryProperty);
      }
    }
    queryComponentViewDescriptor.setRenderedProperties(queryProperties);

    return queryComponentViewDescriptor;
  }

  private IViewDescriptor createResultViewDescriptor(
      IEntityDescriptor entityDescriptor) {
    BasicTableViewDescriptor resultViewDescriptor = new BasicTableViewDescriptor();

    BasicCollectionDescriptor queriedEntitiesListDescriptor = new BasicCollectionDescriptor();
    queriedEntitiesListDescriptor.setCollectionInterface(List.class);
    queriedEntitiesListDescriptor.setElementDescriptor(entityDescriptor);

    BasicCollectionPropertyDescriptor queriedEntitiesDescriptor = new BasicCollectionPropertyDescriptor();
    queriedEntitiesDescriptor
        .setReferencedDescriptor(queriedEntitiesListDescriptor);
    queriedEntitiesDescriptor.setName("queriedEntities");

    resultViewDescriptor.setName("queriedEntities.table");
    resultViewDescriptor.setDescription("queriedEntitiesView");
    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    return resultViewDescriptor;
  }
}
