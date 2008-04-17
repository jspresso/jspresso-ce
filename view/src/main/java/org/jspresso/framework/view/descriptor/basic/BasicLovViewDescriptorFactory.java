/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.view.descriptor.ILovViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * A default implementation for lov view factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicLovViewDescriptorFactory implements ILovViewDescriptorFactory {

  private IQueryViewDescriptorFactory queryViewDescriptorFactory;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IViewDescriptor createLovViewDescriptor(
      IReferencePropertyDescriptor entityRefDescriptor) {
    BasicSplitViewDescriptor lovViewDescriptor = new BasicSplitViewDescriptor();
    lovViewDescriptor.setLeftTopViewDescriptor(queryViewDescriptorFactory
        .createQueryViewDescriptor(
            entityRefDescriptor.getComponentDescriptor()));
    lovViewDescriptor
        .setRightBottomViewDescriptor(createResultViewDescriptor(entityRefDescriptor
            .getComponentDescriptor()));
    return lovViewDescriptor;

  }

  private IViewDescriptor createResultViewDescriptor(
      IComponentDescriptor<Object> entityDescriptor) {
    BasicTableViewDescriptor resultViewDescriptor = new BasicTableViewDescriptor();

    BasicCollectionDescriptor<Object> queriedEntitiesListDescriptor = new BasicCollectionDescriptor<Object>();
    queriedEntitiesListDescriptor.setCollectionInterface(List.class);
    queriedEntitiesListDescriptor.setElementDescriptor(entityDescriptor);

    BasicCollectionPropertyDescriptor<Object> queriedEntitiesDescriptor = new BasicCollectionPropertyDescriptor<Object>();
    queriedEntitiesDescriptor
        .setReferencedDescriptor(queriedEntitiesListDescriptor);
    queriedEntitiesDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);

    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    resultViewDescriptor.setReadOnly(true);
    return resultViewDescriptor;
  }

  /**
   * Sets the queryViewDescriptorFactory.
   * 
   * @param queryViewDescriptorFactory
   *            the queryViewDescriptorFactory to set.
   */
  public void setQueryViewDescriptorFactory(
      IQueryViewDescriptorFactory queryViewDescriptorFactory) {
    this.queryViewDescriptorFactory = queryViewDescriptorFactory;
  }
}
