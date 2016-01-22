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
package org.jspresso.framework.application.backend.action.module;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractQbeAction;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.backend.action.IQueryComponentRefiner;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCollectionViewDescriptor;

/**
 * Initialize a module filter with a brand new query component and resets the
 * module objects collection.
 *
 * @author Vincent Vandenschrick
 */
public class InitModuleFilterAction extends BackendAction {

  private CreateQueryComponentAction createQueryComponentAction;
  private IQueryComponentRefiner     queryComponentRefiner;

  /**
   * Initializes the module filter and resets the bean collection.
   * <p/>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final IActionHandler actionHandler, final Map<String, Object> context) {
    final FilterableBeanCollectionModule filterableBeanCollectionModule = (FilterableBeanCollectionModule) getModule(
        context);
    context.put(CreateQueryComponentAction.COMPONENT_REF_DESCRIPTOR,
        ((IComponentDescriptor<?>) filterableBeanCollectionModule.getViewDescriptor().getModelDescriptor())
            .getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER));
    final IQueryComponent queryComponent;
    if (createQueryComponentAction != null) {
      actionHandler.execute(createQueryComponentAction, context);
      queryComponent = (IQueryComponent) context.get(IQueryComponent.QUERY_COMPONENT);
    } else {
      queryComponent = getEntityFactory(context).createQueryComponentInstance(
          (Class<? extends IComponent>) filterableBeanCollectionModule.getElementComponentDescriptor()
                                                                      .getComponentContract());
    }
    ICollectionViewDescriptor moduleObjectsViewDescriptor = BasicCollectionViewDescriptor.extractMainCollectionView(
        filterableBeanCollectionModule.getProjectedViewDescriptor());
    if (moduleObjectsViewDescriptor instanceof ITableViewDescriptor) {
      List<String> prefetchProperties = new ArrayList<>();
      for (IPropertyViewDescriptor columnDescriptor : ((ITableViewDescriptor) moduleObjectsViewDescriptor)
          .getColumnViewDescriptors()) {
        IPropertyDescriptor columnPropertyDescriptor = (IPropertyDescriptor) columnDescriptor.getModelDescriptor();
        if (columnPropertyDescriptor.isComputed()
            || columnPropertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
          prefetchProperties.add(columnPropertyDescriptor.getName());
        }
      }
      queryComponent.setPrefetchProperties(prefetchProperties);
    }
    if (queryComponentRefiner != null) {
      queryComponentRefiner.refineQueryComponent(queryComponent, context);
    }
    IQueryComponent previousFilter = filterableBeanCollectionModule.getFilter();
    if (previousFilter != null) {
      queryComponent.setOrderingProperties(previousFilter.getOrderingProperties());
    }
    filterableBeanCollectionModule.setFilter(queryComponent);
    filterableBeanCollectionModule.setModuleObjects(null);

    if (filterableBeanCollectionModule.getPagingAction() != null) {
      PropertyChangeListener paginationListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if (evt.getOldValue() != null && evt.getNewValue() != null) {
            try {
              context.put(AbstractQbeAction.PAGINATE, null);
              actionHandler.execute(filterableBeanCollectionModule.getPagingAction(), context);
            } finally {
              context.remove(AbstractQbeAction.PAGINATE);
            }
          }
        }
      };
      queryComponent.addPropertyChangeListener(IPageable.PAGE, paginationListener);
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the createQueryComponentAction.
   *
   * @param createQueryComponentAction
   *     the createQueryComponentAction to set.
   */
  public void setCreateQueryComponentAction(CreateQueryComponentAction createQueryComponentAction) {
    this.createQueryComponentAction = createQueryComponentAction;
  }

  /**
   * Sets the queryComponentRefiner.
   *
   * @param queryComponentRefiner
   *     the queryComponentRefiner to set.
   */
  public void setQueryComponentRefiner(IQueryComponentRefiner queryComponentRefiner) {
    this.queryComponentRefiner = queryComponentRefiner;
  }
}
