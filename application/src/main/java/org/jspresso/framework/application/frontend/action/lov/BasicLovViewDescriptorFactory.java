/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.lov;

import java.util.Map;

import org.jspresso.framework.application.action.AbstractActionContextAware;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * A default implementation for lov view factories.
 *
 * @author Vincent Vandenschrick
 */
public class BasicLovViewDescriptorFactory extends AbstractLovViewDescriptorFactory
    implements ILovViewDescriptorFactory {

  private ActionMap                        resultViewActionMap;
  private IDisplayableAction               sortingAction;
  private IViewDescriptor                  paginationViewDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor createLovViewDescriptor(
      IComponentDescriptorProvider<IComponent> entityRefDescriptor,
      ESelectionMode selectionMode, IDisplayableAction okAction,
      Map<String, Object> lovContext) {
    BasicBorderViewDescriptor lovViewDescriptor = new BasicBorderViewDescriptor();
    IComponentDescriptor<IQueryComponent> filterModelDescriptor = getQueryComponentDescriptorFactory()
        .createQueryComponentDescriptor(entityRefDescriptor);
    IViewDescriptor filterViewDescriptor = getQueryViewDescriptorFactory()
        .createQueryViewDescriptor(entityRefDescriptor, filterModelDescriptor, lovContext);
    lovViewDescriptor.setNorthViewDescriptor(filterViewDescriptor);
    lovViewDescriptor.setModelDescriptor(filterViewDescriptor
        .getModelDescriptor());
    BasicViewDescriptor resultViewDescriptor = createResultViewDescriptor(
        entityRefDescriptor, lovContext);
    BasicCollectionViewDescriptor resultCollectionViewDescriptor = (BasicCollectionViewDescriptor) BasicCollectionViewDescriptor
        .extractMainCollectionView(resultViewDescriptor);
    resultCollectionViewDescriptor.setSelectionMode(selectionMode);
    if (resultCollectionViewDescriptor instanceof BasicTableViewDescriptor) {
      ((BasicTableViewDescriptor) resultCollectionViewDescriptor)
          .setSortingAction(sortingAction);
    }
    resultCollectionViewDescriptor.setRowAction(okAction);
    IQueryComponent queryComponent = (IQueryComponent) lovContext
        .get(IQueryComponent.QUERY_COMPONENT);
    Integer pageSize = queryComponent.getPageSize();
    if (pageSize != null && pageSize > 0) {
      if (paginationViewDescriptor != null) {
        resultCollectionViewDescriptor
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
