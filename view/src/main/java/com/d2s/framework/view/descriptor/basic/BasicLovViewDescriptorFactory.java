/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;
import com.d2s.framework.view.descriptor.ILovViewDescriptorFactory;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;
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
      IReferencePropertyDescriptor entityRefDescriptor) {
    BasicSplitViewDescriptor lovViewDescriptor = new BasicSplitViewDescriptor();
    lovViewDescriptor.setMasterDetail(true);
    lovViewDescriptor
        .setLeftTopViewDescriptor(createQueryComponentViewDescriptor(entityRefDescriptor
            .getComponentDescriptor()));
    lovViewDescriptor
        .setRightBottomViewDescriptor(createResultViewDescriptor(entityRefDescriptor
            .getComponentDescriptor()));
    return lovViewDescriptor;

  }

  private IViewDescriptor createQueryComponentViewDescriptor(
      IComponentDescriptor entityDescriptor) {
    BasicComponentViewDescriptor queryComponentViewDescriptor = new BasicComponentViewDescriptor();
    queryComponentViewDescriptor.setModelDescriptor(entityDescriptor);
    queryComponentViewDescriptor.setName("queryEntity");
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
    List<ISubViewDescriptor> queryPropertyViewDescriptors = new ArrayList<ISubViewDescriptor>();
    for (String renderedProperty : queryProperties) {
      BasicSubviewDescriptor propertyDescriptor = new BasicSubviewDescriptor();
      propertyDescriptor.setName(renderedProperty);
      queryPropertyViewDescriptors.add(propertyDescriptor);
    }
    queryComponentViewDescriptor
        .setPropertyViewDescriptors(queryPropertyViewDescriptors);

    return queryComponentViewDescriptor;
  }

  private IViewDescriptor createResultViewDescriptor(
      IComponentDescriptor entityDescriptor) {
    BasicTableViewDescriptor resultViewDescriptor = new BasicTableViewDescriptor();

    BasicCollectionDescriptor queriedEntitiesListDescriptor = new BasicCollectionDescriptor();
    queriedEntitiesListDescriptor.setCollectionInterface(List.class);
    queriedEntitiesListDescriptor.setElementDescriptor(entityDescriptor);

    BasicCollectionPropertyDescriptor queriedEntitiesDescriptor = new BasicCollectionPropertyDescriptor();
    queriedEntitiesDescriptor
        .setReferencedDescriptor(queriedEntitiesListDescriptor);
    queriedEntitiesDescriptor.setName(IQueryEntity.QUERIED_ENTITIES);

    resultViewDescriptor.setName("queriedEntities.table");
    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    return resultViewDescriptor;
  }
}
