/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.IScalarPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicComponentViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicSplitViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * A default implementation for lov view factories.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 */
public class BasicLovViewFactory<E> implements ILovViewFactory<E> {

  private IViewFactory<E> viewFactory;

  /**
   * Creates a split view with a component view as the top view (the query
   * component) and a table view as the bottom component (the query result).
   * <p>
   * {@inheritDoc}
   */
  public IView<E> createLovView(IEntityDescriptor entityDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return viewFactory.createView(createLovViewDescriptor(entityDescriptor),
        actionHandler, locale);
  }

  private IViewDescriptor createLovViewDescriptor(
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
      if (propertyDescriptor instanceof IScalarPropertyDescriptor
          || propertyDescriptor instanceof IReferencePropertyDescriptor) {
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

  /**
   * Sets the viewFactory.
   * 
   * @param viewFactory
   *          the viewFactory to set.
   */
  public void setViewFactory(IViewFactory<E> viewFactory) {
    this.viewFactory = viewFactory;
  }

}
