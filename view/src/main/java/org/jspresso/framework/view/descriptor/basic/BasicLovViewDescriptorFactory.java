/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ILovViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A default implementation for lov view factories.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicLovViewDescriptorFactory implements ILovViewDescriptorFactory {

  private IQueryViewDescriptorFactory queryViewDescriptorFactory;
  private ActionMap                   resultViewActionMap;
  private IDisplayableAction          sortingAction;
  private IViewDescriptor             paginationViewDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor createLovViewDescriptor(
      IComponentDescriptorProvider<IEntity> entityRefDescriptor,
      IDisplayableAction okAction) {
    BasicBorderViewDescriptor lovViewDescriptor = new BasicBorderViewDescriptor();
    IViewDescriptor filterViewDescriptor = queryViewDescriptorFactory
        .createQueryViewDescriptor(entityRefDescriptor);
    lovViewDescriptor.setNorthViewDescriptor(filterViewDescriptor);
    lovViewDescriptor.setModelDescriptor(filterViewDescriptor
        .getModelDescriptor());
    BasicCollectionViewDescriptor resultViewDescriptor = createResultViewDescriptor(entityRefDescriptor);
    if (resultViewDescriptor instanceof BasicTableViewDescriptor) {
      ((BasicTableViewDescriptor) resultViewDescriptor)
          .setSortingAction(sortingAction);
    }
    resultViewDescriptor.setRowAction(okAction);
    if (entityRefDescriptor.getComponentDescriptor().getPageSize() != null
        && entityRefDescriptor.getComponentDescriptor().getPageSize()
            .intValue() >= 0) {
      if (paginationViewDescriptor != null) {
        resultViewDescriptor
            .setPaginationViewDescriptor(paginationViewDescriptor);
      }
    }
    if (resultViewActionMap != null) {
      resultViewDescriptor.setActionMap(resultViewActionMap);
    }
    lovViewDescriptor.setCenterViewDescriptor(resultViewDescriptor);
    return lovViewDescriptor;

  }

  /**
   * Sets the queryViewDescriptorFactory.
   * 
   * @param queryViewDescriptorFactory
   *          the queryViewDescriptorFactory to set.
   */
  public void setQueryViewDescriptorFactory(
      IQueryViewDescriptorFactory queryViewDescriptorFactory) {
    this.queryViewDescriptorFactory = queryViewDescriptorFactory;
  }

  /**
   * Sets the resultViewActionMap.
   * 
   * @param resultViewActionMap
   *          the resultViewActionMap to set.
   */
  public void setResultViewActionMap(ActionMap resultViewActionMap) {
    this.resultViewActionMap = resultViewActionMap;
  }

  /**
   * Sets the sortingAction.
   * 
   * @param sortingAction
   *          the sortingAction to set.
   */
  public void setSortingAction(IDisplayableAction sortingAction) {
    this.sortingAction = sortingAction;
  }

  /**
   * Creates a result collection view.
   * 
   * @param entityRefDescriptor
   *          the entity reference descriptor.
   * @return a result collection view.
   */
  protected BasicCollectionViewDescriptor createResultViewDescriptor(
      IComponentDescriptorProvider<IEntity> entityRefDescriptor) {
    BasicTableViewDescriptor resultViewDescriptor = new BasicTableViewDescriptor();

    BasicListDescriptor<IEntity> queriedEntitiesListDescriptor = new BasicListDescriptor<IEntity>();
    queriedEntitiesListDescriptor.setElementDescriptor(entityRefDescriptor
        .getComponentDescriptor());

    BasicCollectionPropertyDescriptor<IEntity> queriedEntitiesDescriptor = new BasicCollectionPropertyDescriptor<IEntity>();
    queriedEntitiesDescriptor
        .setReferencedDescriptor(queriedEntitiesListDescriptor);
    queriedEntitiesDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);

    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    resultViewDescriptor.setReadOnly(true);
    resultViewDescriptor.setRenderedProperties(entityRefDescriptor
        .getRenderedProperties());
    resultViewDescriptor.setSelectionMode(ESelectionMode.SINGLE_SELECTION);

    resultViewDescriptor.setPermId("Lov." + entityRefDescriptor.getName());
    return resultViewDescriptor;
  }

  /**
   * Sets the paginationViewDescriptor.
   * 
   * @param paginationViewDescriptor
   *          the paginationViewDescriptor to set.
   */
  public void setPaginationViewDescriptor(
      BasicViewDescriptor paginationViewDescriptor) {
    this.paginationViewDescriptor = paginationViewDescriptor;
  }
}
