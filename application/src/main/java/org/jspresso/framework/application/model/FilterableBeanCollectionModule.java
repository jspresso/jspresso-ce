/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicActionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * A bean collection module that offers a filter.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FilterableBeanCollectionModule extends BeanCollectionModule {

  private IQueryComponent              filter;
  private IComponentDescriptor<Object> filterComponentDescriptor;
  private IViewDescriptor              filterViewDescriptor;
  private PropertyChangeListener       filterComponentTracker;

  private IQueryViewDescriptorFactory  queryViewDescriptorFactory;
  private IViewDescriptor              pagingStatusViewDescriptor;
  private FrontendAction<?, ?, ?>      previousPageAction;
  private FrontendAction<?, ?, ?>      nextPageAction;

  /**
   * Constructs a new <code>FilterableBeanCollectionModule</code> instance.
   */
  public FilterableBeanCollectionModule() {
    filterComponentTracker = new FilterComponentTracker();
  }

  /**
   * Gets the filter.
   * 
   * @return the filter.
   */
  public IQueryComponent getFilter() {
    return filter;
  }

  /**
   * Gets the filterComponentDescriptor.
   * 
   * @return the filterComponentDescriptor.
   */
  public IComponentDescriptor<Object> getFilterComponentDescriptor() {
    if (filterComponentDescriptor == null) {
      return getElementComponentDescriptor();
    }
    return filterComponentDescriptor;
  }

  /**
   * Gets the filterViewDescriptor.
   * 
   * @return the filterViewDescriptor.
   */
  public IViewDescriptor getFilterViewDescriptor() {
    return filterViewDescriptor;
  }

  /**
   * Sets the filter.
   * 
   * @param filter
   *          the filter to set.
   */
  public void setFilter(IQueryComponent filter) {
    if (ObjectUtils.equals(this.filter, filter)) {
      return;
    }
    Object oldValue = getFilter();
    if (oldValue instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) oldValue)
          .removePropertyChangeListener(filterComponentTracker);
    }
    this.filter = filter;
    if (filter != null) {
      filter.setPageSize(getPageSize());
      filter.setDefaultOrderingProperties(getOrderingProperties());
    }
    if (filter instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) filter)
          .addPropertyChangeListener(filterComponentTracker);
    }
    firePropertyChange(FilterableBeanCollectionModuleDescriptor.FILTER,
        oldValue, getFilter());
  }

  /**
   * Sets the filterComponentDescriptor.
   * 
   * @param filterComponentDescriptor
   *          the filterComponentDescriptor to set.
   */
  public void setFilterComponentDescriptor(
      IComponentDescriptor<Object> filterComponentDescriptor) {
    this.filterComponentDescriptor = filterComponentDescriptor;
  }

  /**
   * Sets the filterViewDescriptor.
   * 
   * @param filterViewDescriptor
   *          the filterViewDescriptor to set.
   */
  public void setFilterViewDescriptor(IViewDescriptor filterViewDescriptor) {
    this.filterViewDescriptor = filterViewDescriptor;
  }

  private class FilterComponentTracker implements PropertyChangeListener {

    /**
     * {@inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent evt) {
      firePropertyChange(FilterableBeanCollectionModuleDescriptor.FILTER + "."
          + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
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
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    IViewDescriptor superViewDescriptor = super.getViewDescriptor();

    IComponentDescriptor<?> moduleDescriptor = (IComponentDescriptor<?>) superViewDescriptor
        .getModelDescriptor();

    IComponentDescriptor<Object> filterComponentDesc = getFilterComponentDescriptor();
    IViewDescriptor filterViewDesc = getFilterViewDescriptor();
    if (filterViewDesc == null) {
      filterViewDesc = queryViewDescriptorFactory
          .createQueryViewDescriptor(filterComponentDesc);
    }
    ((BasicViewDescriptor) filterViewDesc)
        .setModelDescriptor(moduleDescriptor
            .getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER));
    BasicBorderViewDescriptor decorator = new BasicBorderViewDescriptor();
    decorator.setNorthViewDescriptor(filterViewDesc);
    decorator.setCenterViewDescriptor(superViewDescriptor);
    if (pagingStatusViewDescriptor != null) {
      BasicBorderViewDescriptor nestingViewDescriptor = new BasicBorderViewDescriptor();
      nestingViewDescriptor
          .setModelDescriptor(moduleDescriptor
              .getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER));
      nestingViewDescriptor.setCenterViewDescriptor(pagingStatusViewDescriptor);
      nestingViewDescriptor.setBorderType(EBorderType.SIMPLE);

      if (previousPageAction != null || nextPageAction != null) {
        BasicBorderViewDescriptor pageNavigationViewDescriptor = new BasicBorderViewDescriptor();
        pageNavigationViewDescriptor
            .setCenterViewDescriptor(nestingViewDescriptor);

        if (previousPageAction != null) {
          BasicActionViewDescriptor previousActionViewDescriptor = new BasicActionViewDescriptor();
          previousActionViewDescriptor.setAction(previousPageAction);
          pageNavigationViewDescriptor
              .setWestViewDescriptor(previousActionViewDescriptor);
        }

        if (nextPageAction != null) {
          BasicActionViewDescriptor nextActionViewDescriptor = new BasicActionViewDescriptor();
          nextActionViewDescriptor.setAction(nextPageAction);
          pageNavigationViewDescriptor
              .setEastViewDescriptor(nextActionViewDescriptor);
        }
        decorator.setSouthViewDescriptor(pageNavigationViewDescriptor);
      } else {
        decorator.setSouthViewDescriptor(nestingViewDescriptor);
      }
    }
    decorator.setModelDescriptor(superViewDescriptor.getModelDescriptor());
    return decorator;
  }

  /**
   * Gets the module descriptor.
   * 
   * @return the module descriptor.
   */
  @Override
  protected BeanCollectionModuleDescriptor getDescriptor() {
    return new FilterableBeanCollectionModuleDescriptor(
        getElementComponentDescriptor(), getFilterComponentDescriptor());
  }

  /**
   * Sets the previousPageAction.
   * 
   * @param previousPageAction
   *          the previousPageAction to set.
   */
  public void setPreviousPageAction(FrontendAction<?, ?, ?> previousPageAction) {
    this.previousPageAction = previousPageAction;
  }

  /**
   * Sets the nextPageAction.
   * 
   * @param nextPageAction
   *          the nextPageAction to set.
   */
  public void setNextPageAction(FrontendAction<?, ?, ?> nextPageAction) {
    this.nextPageAction = nextPageAction;
  }
}
