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
package org.jspresso.framework.application.frontend.action.lov.mobile;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.frontend.action.lov.AbstractLovViewDescriptorFactory;
import org.jspresso.framework.application.frontend.action.lov.ILovViewDescriptorFactory;
import org.jspresso.framework.application.frontend.action.std.mobile.AddPageAction;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.util.gui.ESelectionMode;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileBorderViewDescriptor;

/**
 * A default implementation for lov view factories.
 *
 * @author Vincent Vandenschrick
 */
public class MobileLovViewDescriptorFactory extends AbstractLovViewDescriptorFactory
    implements ILovViewDescriptorFactory {

  private BackendAction pagingAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor createLovViewDescriptor(IComponentDescriptorProvider<IComponent> entityRefDescriptor,
                                                 ESelectionMode selectionMode, IDisplayableAction okAction,
                                                 IAction propertyViewAction, IDisplayableAction propertyViewCharAction,
                                                 Map<String, Object> lovContext) {
    MobileBorderViewDescriptor lovViewDescriptor = new MobileBorderViewDescriptor();
    IComponentDescriptor<IQueryComponent> filterModelDescriptor = getQueryComponentDescriptorFactory()
        .createQueryComponentDescriptor(entityRefDescriptor);
    IViewDescriptor filterViewDescriptor = getQueryViewDescriptorFactory().createQueryViewDescriptor(
        entityRefDescriptor, filterModelDescriptor, propertyViewAction, propertyViewCharAction, lovContext);
    lovViewDescriptor.setNorthViewDescriptor(filterViewDescriptor);
    lovViewDescriptor.setModelDescriptor(filterViewDescriptor.getModelDescriptor());
    BasicViewDescriptor resultViewDescriptor = createResultViewDescriptor(entityRefDescriptor, lovContext);
    BasicCollectionViewDescriptor resultCollectionViewDescriptor = (BasicCollectionViewDescriptor)
        BasicCollectionViewDescriptor
        .extractMainCollectionView(resultViewDescriptor);
    if (selectionMode != null) {
      resultCollectionViewDescriptor.setSelectionMode(selectionMode);
    }
    if (ESelectionMode.SINGLE_SELECTION.equals(selectionMode) || ESelectionMode.SINGLE_CUMULATIVE_SELECTION.equals(
        selectionMode)) {
      resultCollectionViewDescriptor.setItemSelectionAction(okAction);
    }
    //lovContext.put(LovAction.LOV_DIALOG_ACTIONS, new ArrayList<>());
    IQueryComponent queryComponent = (IQueryComponent) lovContext.get(IQueryComponent.QUERY_COMPONENT);
    Integer pageSize = queryComponent.getPageSize();
    if (pageSize != null && pageSize > 0) {
      if (getPagingAction() != null) {
        AddPageAction<?, ?, ?> addPageAction = new AddPageAction<>();
        addPageAction.setWrappedAction(getPagingAction());
        lovViewDescriptor.setPageEndAction(addPageAction);
      }
    }
    lovViewDescriptor.setCenterViewDescriptor(resultViewDescriptor);
    return lovViewDescriptor;
  }

  /**
   * Gets paging action.
   *
   * @return the paging action
   */
  protected BackendAction getPagingAction() {
    return pagingAction;
  }

  /**
   * Sets paging action.
   *
   * @param pagingAction
   *     the paging action
   */
  public void setPagingAction(BackendAction pagingAction) {
    this.pagingAction = pagingAction;
  }
}
