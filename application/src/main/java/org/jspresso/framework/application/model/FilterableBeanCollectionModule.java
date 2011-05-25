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
package org.jspresso.framework.application.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptorFactory;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * This is a specialized type of bean collection module that provides a filter (
 * an instance of <code>IQueryComponent</code> ). This type of module, coupled
 * with a generic, built-in, action map is perfectly suited for CRUD-like
 * operations.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FilterableBeanCollectionModule extends BeanCollectionModule
    implements IPageable {

  private IQueryComponent               filter;
  private IComponentDescriptor<IEntity> filterComponentDescriptor;
  private PropertyChangeListener        filterComponentTracker;
  private IViewDescriptor               filterViewDescriptor;

  private Map<String, ESort>            orderingProperties;
  private Integer                       pageSize;
  private IQueryViewDescriptorFactory   queryViewDescriptorFactory;
  private IViewDescriptor               paginationViewDescriptor;

  /**
   * Constructs a new <code>FilterableBeanCollectionModule</code> instance.
   */
  public FilterableBeanCollectionModule() {
    filterComponentTracker = new FilterComponentTracker(this);
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
  @SuppressWarnings("unchecked")
  public IComponentDescriptor<IEntity> getFilterComponentDescriptor() {
    if (filterComponentDescriptor == null) {
      return (IComponentDescriptor<IEntity>) getElementComponentDescriptor();
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
   * Gets the orderingProperties.
   * 
   * @return the orderingProperties.
   */
  public Map<String, ESort> getOrderingProperties() {
    if (orderingProperties == null) {
      return getElementComponentDescriptor().getOrderingProperties();
    }
    return orderingProperties;
  }

  /**
   * Gets the pageSize.
   * 
   * @return the pageSize.
   */
  @Override
  public Integer getPageSize() {
    if (pageSize == null) {
      return getElementComponentDescriptor().getPageSize();
    }
    return pageSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    IViewDescriptor superViewDescriptor = super.getViewDescriptor();

    IComponentDescriptor<?> moduleDescriptor = (IComponentDescriptor<?>) superViewDescriptor
        .getModelDescriptor();

    IComponentDescriptor<IEntity> filterComponentDesc = getFilterComponentDescriptor();
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

    IViewDescriptor moduleObjectsView = getProjectedViewDescriptor();
    if (getPageSize() != null && getPageSize().intValue() >= 0) {
      BasicCollectionViewDescriptor pageableView = null;
      if (moduleObjectsView instanceof BasicCollectionViewDescriptor) {
        pageableView = (BasicCollectionViewDescriptor) moduleObjectsView;
      } else if (moduleObjectsView instanceof ICompositeViewDescriptor
          && ((ICompositeViewDescriptor) moduleObjectsView)
              .getChildViewDescriptors() != null
          && ((ICompositeViewDescriptor) moduleObjectsView)
              .getChildViewDescriptors().size() > 0
          && ((ICompositeViewDescriptor) moduleObjectsView)
              .getChildViewDescriptors().get(0) instanceof BasicCollectionViewDescriptor) {
        pageableView = (BasicCollectionViewDescriptor) ((ICompositeViewDescriptor) moduleObjectsView)
            .getChildViewDescriptors().get(0);
      }
      if (pageableView != null
          && pageableView.getPaginationViewDescriptor() == null) {
        pageableView.setPaginationViewDescriptor(paginationViewDescriptor);
      }
    }
    decorator.setModelDescriptor(superViewDescriptor.getModelDescriptor());
    return decorator;
  }

  /**
   * Assigns the filter to this module instance. It is by default assigned by
   * the module startup action (see <code>InitModuleFilterAction</code>). So if
   * you ever want to change the default implementation of the filter, you have
   * to write and install you own custom startup action or explicitely inject a
   * specific instance.
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
   * This property allows to configure a custom filter model descriptor. If not
   * set, which is the default value, the filter model is built out of the
   * element component descriptor (QBE filter model).
   * 
   * @param filterComponentDescriptor
   *          the filterComponentDescriptor to set.
   */
  public void setFilterComponentDescriptor(
      IComponentDescriptor<IEntity> filterComponentDescriptor) {
    this.filterComponentDescriptor = filterComponentDescriptor;
  }

  /**
   * This property allows to refine the default filer view to re-arange the
   * filter fields. Custom filter view descriptors assigned here must not be
   * assigned a model descriptor since they will be at runtime. This is because
   * the filter component descriptor must be reworked - to adapt comparable
   * field structures for instance.
   * 
   * @param filterViewDescriptor
   *          the filterViewDescriptor to set.
   */
  public void setFilterViewDescriptor(IViewDescriptor filterViewDescriptor) {
    this.filterViewDescriptor = filterViewDescriptor;
  }

  /**
   * Configures a custom map of ordering properties for the result set. If not
   * set, which is the default, the elements ordering properties is used.
   * <p>
   * This property consist of a <code>Map</code> whose entries are composed with
   * :
   * <ul>
   * <li>the property name as key</li>
   * <li>the sort order for this property as value. This is either a value of
   * the <code>ESort</code> enum (<i>ASCENDING</i> or <i>DESCENDING</i>) or its
   * equivalent string representation.</li>
   * </ul>
   * Ordering properties are considered following their order in the map
   * iterator.
   * 
   * @param orderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ESort> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }

  /**
   * Configures a custom page size for the result set. If not set, which is the
   * default, the elements default page size is used.
   * 
   * @param pageSize
   *          the pageSize to set.
   */
  @Override
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
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
   * Gets the module descriptor.
   * 
   * @return the module descriptor.
   */
  @Override
  protected BeanCollectionModuleDescriptor getDescriptor() {
    return new FilterableBeanCollectionModuleDescriptor(
        getElementComponentDescriptor(), getQueryComponentDescriptorFactory()
            .createQueryComponentDescriptor(getFilterComponentDescriptor()));
  }

  /**
   * Gets the query component descriptor factory used to create the filter model
   * descriptor.
   * 
   * @return the query component descriptor factory used to create the filter
   *         model descriptor.
   */
  protected IQueryComponentDescriptorFactory getQueryComponentDescriptorFactory() {
    if (queryViewDescriptorFactory instanceof IQueryComponentDescriptorFactory) {
      return (IQueryComponentDescriptorFactory) queryViewDescriptorFactory;
    }
    return new BasicQueryComponentDescriptorFactory();
  }

  private static class FilterComponentTracker implements PropertyChangeListener {
    
    private FilterableBeanCollectionModule target;

    
    /**
     * Constructs a new <code>FilterableBeanCollectionModule.FilterComponentTracker</code> instance.
     * @param target the target filter module.
     * 
     */
    public FilterComponentTracker(FilterableBeanCollectionModule target) {
      this.target = target;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      target.firePropertyChange(FilterableBeanCollectionModuleDescriptor.FILTER + "."
          + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
      if (IPageable.DISPLAY_PAGE_INDEX.equals(evt.getPropertyName())
          || IPageable.NEXT_PAGE_ENABLED.equals(evt.getPropertyName())
          || IPageable.PAGE.equals(evt.getPropertyName())
          || IPageable.PAGE_COUNT.equals(evt.getPropertyName())
          || IPageable.PAGE_SIZE.equals(evt.getPropertyName())
          || IPageable.PREVIOUS_PAGE_ENABLED.equals(evt.getPropertyName())
          || IPageable.RECORD_COUNT.equals(evt.getPropertyName())) {
        target.firePropertyChange(evt.getPropertyName(), evt.getOldValue(),
            evt.getNewValue());
      }
    }
  }

  /**
   * Configures the sub view used to navigate beetween the pages.
   * 
   * @param paginationViewDescriptor
   *          the paginationViewDescriptor to set.
   */
  public void setPaginationViewDescriptor(
      BasicViewDescriptor paginationViewDescriptor) {
    this.paginationViewDescriptor = paginationViewDescriptor;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public Integer getDisplayPageIndex() {
    if (filter != null) {
      return filter.getDisplayPageIndex();
    }
    return null;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public Integer getPage() {
    if (filter != null) {
      return filter.getPage();
    }
    return null;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public Integer getPageCount() {
    if (filter != null) {
      return filter.getPageCount();
    }
    return null;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public Integer getRecordCount() {
    if (filter != null) {
      return filter.getRecordCount();
    }
    return null;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public boolean isNextPageEnabled() {
    if (filter != null) {
      return filter.isNextPageEnabled();
    }
    return false;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public boolean isPreviousPageEnabled() {
    if (filter != null) {
      return filter.isPreviousPageEnabled();
    }
    return false;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public void setPage(Integer page) {
    if (filter != null) {
      filter.setPage(page);
    }
  }

  /**
   * Delegates to filter. {@inheritDoc}
   */
  @Override
  public void setRecordCount(Integer recordCount) {
    if (filter != null) {
      filter.setRecordCount(recordCount);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FilterableBeanCollectionModule clone() {
    FilterableBeanCollectionModule clone = (FilterableBeanCollectionModule) super
        .clone();
    clone.filterComponentTracker = new FilterComponentTracker(clone);
    if (filter != null) {
      clone.setFilter(filter.clone());
    }
    return clone;
  }
}
