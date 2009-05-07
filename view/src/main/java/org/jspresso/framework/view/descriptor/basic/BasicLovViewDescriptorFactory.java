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

import java.util.List;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.descriptor.ILovViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A default implementation for lov view factories.
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
public class BasicLovViewDescriptorFactory implements ILovViewDescriptorFactory {

  private IQueryViewDescriptorFactory queryViewDescriptorFactory;
  private ActionMap                   resultViewActionMap;
  private IViewDescriptor             pagingStatusViewDescriptor;
  private IAction                     sortingAction;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IViewDescriptor createLovViewDescriptor(
      IReferencePropertyDescriptor entityRefDescriptor) {
    BasicBorderViewDescriptor lovViewDescriptor = new BasicBorderViewDescriptor();
    lovViewDescriptor
        .setNorthViewDescriptor(queryViewDescriptorFactory
            .createQueryViewDescriptor(entityRefDescriptor
                .getComponentDescriptor()));
    lovViewDescriptor
        .setCenterViewDescriptor(createResultViewDescriptor(entityRefDescriptor
            .getComponentDescriptor()));
    if (pagingStatusViewDescriptor != null) {
      BasicBorderViewDescriptor pagingViewDescriptor = new BasicBorderViewDescriptor();
      pagingViewDescriptor.setWestViewDescriptor(pagingStatusViewDescriptor);
      lovViewDescriptor.setSouthViewDescriptor(pagingViewDescriptor);
    }
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
    if (resultViewActionMap != null) {
      resultViewDescriptor.setActionMap(resultViewActionMap);
    }
    resultViewDescriptor.setSortingAction(sortingAction);

    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    resultViewDescriptor.setReadOnly(true);
    return resultViewDescriptor;
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
   * Sets the pagingStatusViewDescriptor.
   * 
   * @param pagingStatusViewDescriptor
   *          the pagingStatusViewDescriptor to set.
   */
  public void setPagingStatusViewDescriptor(
      IViewDescriptor pagingStatusViewDescriptor) {
    this.pagingStatusViewDescriptor = pagingStatusViewDescriptor;
  }

  /**
   * Sets the sortingAction.
   * 
   * @param sortingAction
   *          the sortingAction to set.
   */
  public void setSortingAction(IAction sortingAction) {
    this.sortingAction = sortingAction;
  }
}
