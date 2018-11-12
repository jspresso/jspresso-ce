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
package org.jspresso.framework.application.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.application.backend.action.AbstractQueryComponentsAction;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.action.IQueryComponentRefiner;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.component.query.QueryComponentSerializationUtil;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.util.gui.EBorderType;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IQueryExtraViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTabViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * This is a specialized type of bean collection module that provides a filter (
 * an instance of {@code IQueryComponent} ). This type of module, coupled
 * with a generic, built-in, action map is perfectly suited for CRUD-like
 * operations.
 *
 * @author Vincent Vandenschrick
 */
public class FilterableBeanCollectionModule extends BeanCollectionModule implements IPageable {

  private IQueryComponent                  filter;
  private IComponentDescriptor<IComponent> filterComponentDescriptor;
  private PropertyChangeListener           filterComponentTracker;
  private IViewDescriptor                  filterViewDescriptor;
  private BasicTabViewDescriptor           filterExtraViewDescriptor;
  private Map<String, ESort>               orderingProperties;
  private Integer                          pageSize;
  private IQueryComponentDescriptorFactory queryComponentDescriptorFactory;
  private IQueryViewDescriptorFactory      queryViewDescriptorFactory;
  private IQueryExtraViewDescriptorFactory queryExtraViewDescriptorFactory;
  private IViewDescriptor                  paginationViewDescriptor;
  private BackendAction                    pagingAction;
  private IViewDescriptor                  cachedViewDescriptor;
  private Boolean                          findOnType;
  private Boolean                          findOnSet;
  private IQueryComponentRefiner           queryComponentRefiner;
  private Object                           criteriaRefiner;
  private Object                           criteriaFactory;

  /**
   * Gets the queryViewDescriptorFactory.
   *
   * @return the queryViewDescriptorFactory.
   */
  protected IQueryViewDescriptorFactory getQueryViewDescriptorFactory() {
    return queryViewDescriptorFactory;
  }

  /**
   * Constructs a new {@code FilterableBeanCollectionModule} instance.
   */
  public FilterableBeanCollectionModule() {
    super();
    filterComponentTracker = new FilterComponentTracker(this);
  }

  /**
   * Gest the queryExtraViewDescriptorFactory.
   *
   * @return the queryExtraViewDescriptorFactory.
   */
  public IQueryExtraViewDescriptorFactory getQueryExtraViewDescriptorFactory() {
    return queryExtraViewDescriptorFactory;
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
  public IComponentDescriptor<IComponent> getFilterComponentDescriptor() {
    if (filterComponentDescriptor == null) {
      return (IComponentDescriptor<IComponent>) getElementComponentDescriptor();
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
   * Gets the filterExtraViewDescriptor.
   *
   * @return the filterExtraViewDescriptor.
   */
  protected BasicTabViewDescriptor getFilterExtraViewDescriptor() {
    return filterExtraViewDescriptor;
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
   *
   * @return the view descriptor
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    if (cachedViewDescriptor == null) {
      IViewDescriptor superViewDescriptor = super.getViewDescriptor();
      IComponentDescriptor<?> moduleDescriptor = (IComponentDescriptor<?>) superViewDescriptor.getModelDescriptor();
      IViewDescriptor filterViewDesc = buildFilterViewDescriptor(moduleDescriptor);

      BasicBorderViewDescriptor decorator = new BasicBorderViewDescriptor();
      decorator.setNorthViewDescriptor(filterViewDesc);
      decorator.setCenterViewDescriptor(superViewDescriptor);

      BasicCollectionViewDescriptor moduleObjectsView = (BasicCollectionViewDescriptor) BasicCollectionViewDescriptor
          .extractMainCollectionView(getProjectedViewDescriptor());
      if (moduleObjectsView != null) {
        if (moduleObjectsView.getName() == null) {
          moduleObjectsView.setName(getName());
        }
        if (getPageSize() != null && getPageSize() > 0) {
          if (moduleObjectsView.getPaginationViewDescriptor() == null) {
            moduleObjectsView.setPaginationViewDescriptor(getPaginationViewDescriptor());
          }
        }
      }
      decorator.setModelDescriptor(superViewDescriptor.getModelDescriptor());
      cachedViewDescriptor = decorator;
    }
    return cachedViewDescriptor;
  }

  /**
   * Build the filter view descriptor.
   *
   * @param moduleDescriptor
   *     The module descriptor
   * @return the filter view descriptor
   */
  @SuppressWarnings("unchecked")
  protected IViewDescriptor buildFilterViewDescriptor(IComponentDescriptor<?> moduleDescriptor) {

    IComponentDescriptor<IComponent> realComponentDesc = getFilterComponentDescriptor();
    IViewDescriptor filterView = getFilterViewDescriptor();
    IComponentDescriptorProvider<IQueryComponent> filterModelDescriptorProvider =
        (IComponentDescriptorProvider<IQueryComponent>) moduleDescriptor
        .getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER);
    boolean customFilterView = false;
    if (filterView == null) {
      boolean fos = getFindOnSet();
      boolean fot = getFindOnType();
      IDisplayableAction charAction = null;
      if (fot) {
        if (pagingAction != null) {
          if (pagingAction instanceof IDisplayableAction) {
            charAction = (IDisplayableAction) pagingAction;
          } else {
            charAction = new FrontendAction<>();
            ((FrontendAction) charAction).setWrappedAction(pagingAction);
          }
        }
      }
      filterView = getQueryViewDescriptorFactory().createQueryViewDescriptor(realComponentDesc,
          filterModelDescriptorProvider.getComponentDescriptor(), fos ? pagingAction : null, fot ? charAction : null,
          Collections.<String, Object>emptyMap());
    } else {
      customFilterView = true;
      // Deeply clean model descriptors on filter views
      cleanupFilterViewDescriptor(filterView);
    }
    if (filterView instanceof BasicViewDescriptor) {
      ((BasicViewDescriptor) filterView).setBorderType(EBorderType.TITLED);
      ((BasicViewDescriptor) filterView).setModelDescriptor(filterModelDescriptorProvider);
    }
    if (customFilterView) {
      getQueryViewDescriptorFactory().adaptExistingViewDescriptor(filterView);
    }

    BasicTabViewDescriptor tabFilterView = getFilterExtraViewDescriptor();
    if (tabFilterView == null) {
      IQueryExtraViewDescriptorFactory extraViewFactory = getQueryExtraViewDescriptorFactory();
      if (extraViewFactory != null) {
        tabFilterView = extraViewFactory.createQueryExtraViewDescriptor(getProjectedViewDescriptor(), realComponentDesc,
            filterModelDescriptorProvider.getComponentDescriptor());
      }
    }
    if (tabFilterView == null && filterView instanceof BasicTabViewDescriptor) {
      // In that case, the filter view must be reworked for the model descriptor to be set on each tabs
      tabFilterView = new BasicTabViewDescriptor();
      tabFilterView.setTabs(new ArrayList<IViewDescriptor>());
    }
    if (tabFilterView != null) {
      List<IViewDescriptor> tabs = new ArrayList<>();
      for (IViewDescriptor tab : tabFilterView.getChildViewDescriptors()) {
        if (tab instanceof BasicViewDescriptor) {
          ((BasicViewDescriptor) tab).clone();
          ((BasicViewDescriptor) tab).setModelDescriptor(filterModelDescriptorProvider);
          tabs.add(tab);
        }
      }

      if (filterView instanceof ITabViewDescriptor) {
        for (IViewDescriptor tab : ((ICompositeViewDescriptor) filterView).getChildViewDescriptors()) {
          if (tab instanceof BasicViewDescriptor) {
            tab = ((BasicViewDescriptor) tab).clone();
            ((BasicViewDescriptor) tab).setModelDescriptor(filterModelDescriptorProvider);
            tabs.add(tab);
          }
        }
      } else if (filterView instanceof BasicViewDescriptor) {
        ((BasicViewDescriptor) filterView).setBorderType(EBorderType.NONE);
        tabs.add(filterView);
      }

      BasicTabViewDescriptor newTabFilterView;
      if (tabFilterView != null) {
        // Clone it to copy all properties (border, background, etc.)
        newTabFilterView = (BasicTabViewDescriptor) tabFilterView.clone();
      } else {
        newTabFilterView = new BasicTabViewDescriptor();
      }
      newTabFilterView.setPermId(getPermId() + ".filter");
      newTabFilterView.setRenderingOptions(ERenderingOptions.LABEL);
      newTabFilterView.setTabs(tabs);

      filterView = newTabFilterView;
    }
    return filterView;
  }

  /**
   * Cleanup filter view descriptor.
   *
   * @param filterViewDesc
   *     the filter view desc
   */
  protected void cleanupFilterViewDescriptor(IViewDescriptor filterViewDesc) {
    if (filterViewDesc instanceof BasicViewDescriptor) {
      ((BasicViewDescriptor) filterViewDesc).setModelDescriptor(null);
    }
    if (filterViewDesc instanceof ICompositeViewDescriptor) {
      List<IViewDescriptor> children = ((ICompositeViewDescriptor) filterViewDesc).getChildViewDescriptors();
      if (children != null) {
        for (IViewDescriptor childViewDesc : children) {
          cleanupFilterViewDescriptor(childViewDesc);
        }
      }
    }
  }

  /**
   * Assigns the filter to this module instance. It is by default assigned by
   * the module startup action (see {@code InitModuleFilterAction}). So if
   * you ever want to change the default implementation of the filter, you have
   * to write and install you own custom startup action or explicitly inject a
   * specific instance.
   *
   * @param filter
   *     the filter to set.
   */
  public void setFilter(IQueryComponent filter) {
    if (ObjectUtils.equals(this.filter, filter)) {
      return;
    }
    Integer oldDisplayPageIndex = getDisplayPageIndex();
    String oldDisplayPageCount = getDisplayPageCount();
    String oldDisplayRecordCount = getDisplayRecordCount();
    boolean oldPageNavigationEnabled = isPageNavigationEnabled();
    boolean oldNextPageEnabled = isNextPageEnabled();
    boolean oldPreviousPageEnabled = isPreviousPageEnabled();
    IQueryComponent oldValue = getFilter();
    if (oldValue != null) {
      oldValue.removePropertyChangeListener(filterComponentTracker);
    }
    this.filter = filter;
    if (filter != null) {
      if (queryComponentRefiner != null) {
        filter.put(AbstractQueryComponentsAction.COMPONENT_REFINER, queryComponentRefiner);
      }
      if (criteriaFactory != null) {
        filter.put(AbstractQueryComponentsAction.CRITERIA_FACTORY, criteriaFactory);
      }
      if (criteriaRefiner != null) {
        filter.put(AbstractQueryComponentsAction.CRITERIA_REFINER, criteriaRefiner);
      }
      filter.setPageSize(getPageSize());
      filter.setDefaultOrderingProperties(getOrderingProperties());
      filter.addPropertyChangeListener(filterComponentTracker);
      firePropertyChange(IPageable.DISPLAY_PAGE_INDEX, oldDisplayPageIndex, getDisplayPageIndex());
      firePropertyChange(IPageable.DISPLAY_PAGE_COUNT, oldDisplayPageCount, getDisplayPageCount());
      firePropertyChange(IPageable.DISPLAY_RECORD_COUNT, oldDisplayRecordCount, getDisplayRecordCount());
      firePropertyChange(IPageable.PAGE_NAVIGATION_ENABLED, oldPageNavigationEnabled, isPageNavigationEnabled());
      firePropertyChange(IPageable.NEXT_PAGE_ENABLED, oldNextPageEnabled, isNextPageEnabled());
      firePropertyChange(IPageable.PREVIOUS_PAGE_ENABLED, oldPreviousPageEnabled, isPreviousPageEnabled());
    }
    firePropertyChange(FilterableBeanCollectionModuleDescriptor.FILTER, oldValue, getFilter());
  }

  /**
   * This property allows to configure a custom filter model descriptor. If not
   * set, which is the default value, the filter model is built out of the
   * element component descriptor (QBE filter model).
   *
   * @param filterComponentDescriptor
   *     the filterComponentDescriptor to set.
   */
  public void setFilterComponentDescriptor(IComponentDescriptor<IComponent> filterComponentDescriptor) {
    this.filterComponentDescriptor = filterComponentDescriptor;
  }

  /**
   * This property allows to refine the default filter view to re-arrange the
   * filter fields. Custom filter view descriptors assigned here must not be
   * assigned a model descriptor since they will be at runtime. This is because
   * the filter component descriptor must be reworked - to adapt comparable
   * field structures for instance.
   *
   * @param filterViewDescriptor
   *     the filterViewDescriptor to set.
   */
  public void setFilterViewDescriptor(IViewDescriptor filterViewDescriptor) {
    this.filterViewDescriptor = filterViewDescriptor;
  }

  /**
   * This property allow to refine the filter view. If this field is not empty
   * the filter view will be replaced by a tab view containing this view and the
   * view defined bu the {@link #setFilterViewDescriptor(IViewDescriptor)} method.
   * <p>
   * If the extra filter view or the filter view is already a tab view, then tab
   * views will be merged to a single tab view.
   *
   * @param filterExtraViewDescriptor
   *     the filterExtraViewDescriptor to set.
   */
  public void setFilterExtraViewDescriptor(BasicTabViewDescriptor filterExtraViewDescriptor) {
    this.filterExtraViewDescriptor = filterExtraViewDescriptor;
  }

  /**
   * Configures a custom map of ordering properties for the result set. If not
   * set, which is the default, the elements ordering properties is used.
   * <p>
   * This property consist of a {@code Map} whose entries are composed with
   * :
   * <ul>
   * <li>the property name as key</li>
   * <li>the sort order for this property as value. This is either a value of
   * the {@code ESort} enum (<i>ASCENDING</i> or <i>DESCENDING</i>) or its
   * equivalent string representation.</li>
   * </ul>
   * Ordering properties are considered following their order in the map
   * iterator.
   *
   * @param orderingProperties
   *     the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ESort> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }

  /**
   * Configures a custom page size for the result set. If not set, which is the
   * default, the elements default page size is used.
   *
   * @param pageSize
   *     the pageSize to set.
   */
  @Override
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Whenever setting findOnSet to {@code true}, the module
   * trigger the query each time a field is set in the filter view. This
   * brings continuous autocomplete feature on the filter module.
   *
   * @param findOnSet
   *     the find on set
   */
  public void setFindOnSet(Boolean findOnSet) {
    this.findOnSet = findOnSet;
  }

  /**
   * Gets find on set.
   *
   * @return the find on set
   */
  protected boolean getFindOnSet() {
    if (findOnSet != null) {
      return findOnSet;
    }
    Boolean autoQueryEnabled = getElementComponentDescriptor().getAutoQueryEnabled();
    if (autoQueryEnabled != null) {
      return autoQueryEnabled;
    }
    Integer pageSize = getPageSize();
    return pageSize != null && pageSize > 0;
  }

  /**
   * Whenever setting findOnType to {@code true}, the module will
   * trigger the query each time a field is typed-in in the module filter view. This
   * brings continuous autocomplete feature on the filter module.
   *
   * @param findOnType
   *     the find on type
   */
  public void setFindOnType(Boolean findOnType) {
    this.findOnType = findOnType;
  }

  /**
   * Gets find on type.
   *
   * @return the find on type
   */
  protected boolean getFindOnType() {
    if (findOnType != null) {
      return findOnType;
    }
    Boolean autoQueryEnabled = getElementComponentDescriptor().getAutoQueryEnabled();
    if (autoQueryEnabled != null) {
      return autoQueryEnabled;
    }
    Integer pageSize = getPageSize();
    return pageSize != null && pageSize > 0;
  }

  /**
   * Sets the queryViewDescriptorFactory.
   *
   * @param queryViewDescriptorFactory
   *     the queryViewDescriptorFactory to set.
   */
  public void setQueryViewDescriptorFactory(IQueryViewDescriptorFactory queryViewDescriptorFactory) {
    this.queryViewDescriptorFactory = queryViewDescriptorFactory;
  }

  /**
   * Sets the queryExtraViewDescriptorFactory.
   *
   * @param queryExtraViewDescriptorFactory
   *     the queryExtraViewDescriptorFactory to set.
   */
  public void setQueryExtraViewDescriptorFactory(IQueryExtraViewDescriptorFactory queryExtraViewDescriptorFactory) {
    this.queryExtraViewDescriptorFactory = queryExtraViewDescriptorFactory;
  }

  /**
   * Gets the module descriptor.
   *
   * @return the module descriptor.
   */
  @Override
  protected BeanCollectionModuleDescriptor getDescriptor() {
    return new FilterableBeanCollectionModuleDescriptor(getElementComponentDescriptor(),
        getQueryComponentDescriptorFactory().createQueryComponentDescriptor(getFilterComponentDescriptor()));
  }

  /**
   * Gets the query component descriptor factory used to create the filter model
   * descriptor.
   *
   * @return the query component descriptor factory used to create the filter model descriptor.
   */
  protected IQueryComponentDescriptorFactory getQueryComponentDescriptorFactory() {
    return queryComponentDescriptorFactory;
  }

  private static class FilterComponentTracker implements PropertyChangeListener {

    private final FilterableBeanCollectionModule target;

    /**
     * Constructs a new
     * {@code FilterableBeanCollectionModule.FilterComponentTracker}
     * instance.
     *
     * @param target
     *     the target filter module.
     */
    public FilterComponentTracker(FilterableBeanCollectionModule target) {
      this.target = target;
    }

    /**
     * {@inheritDoc}
     *
     * @param evt
     *     the evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      target.firePropertyChange(FilterableBeanCollectionModuleDescriptor.FILTER + "." + evt.getPropertyName(),
          evt.getOldValue(), evt.getNewValue());
      if (IPageable.DISPLAY_PAGE_INDEX.equals(evt.getPropertyName()) || IPageable.NEXT_PAGE_ENABLED.equals(
          evt.getPropertyName()) || IPageable.PAGE.equals(evt.getPropertyName()) || IPageable.PAGE_COUNT.equals(
          evt.getPropertyName()) || IPageable.DISPLAY_PAGE_COUNT.equals(evt.getPropertyName()) || IPageable.PAGE_SIZE
          .equals(evt.getPropertyName()) || IPageable.PREVIOUS_PAGE_ENABLED.equals(evt.getPropertyName())
          || IPageable.RECORD_COUNT.equals(evt.getPropertyName()) || IPageable.DISPLAY_RECORD_COUNT.equals(
          evt.getPropertyName()) || IPageable.SELECTED_RECORD_COUNT.equals(evt.getPropertyName())
          || IPageable.PAGE_NAVIGATION_ENABLED.equals(evt.getPropertyName())) {
        target.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
      }
    }
  }

  /**
   * Configures the sub view used to navigate between the pages.
   *
   * @param paginationViewDescriptor
   *     the paginationViewDescriptor to set.
   */
  public void setPaginationViewDescriptor(BasicViewDescriptor paginationViewDescriptor) {
    this.paginationViewDescriptor = paginationViewDescriptor;
  }

  /**
   * Gets pagination view descriptor.
   *
   * @return the pagination view descriptor
   */
  protected IViewDescriptor getPaginationViewDescriptor() {
    return paginationViewDescriptor;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the display page index
   */
  @Override
  public Integer getDisplayPageIndex() {
    if (filter != null) {
      return filter.getDisplayPageIndex();
    }
    return null;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @param displayPageIndex
   *     the display page index
   */
  @Override
  public void setDisplayPageIndex(Integer displayPageIndex) {
    if (filter != null) {
      filter.setDisplayPageIndex(displayPageIndex);
    }
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the page
   */
  @Override
  public Integer getPage() {
    if (filter != null) {
      return filter.getPage();
    }
    return null;
  }

  /**
   * Delegates to filter.
   * <p/>
   * {@inheritDoc}
   *
   * @return the page count
   */
  @Override
  public Integer getPageCount() {
    if (filter != null) {
      return filter.getPageCount();
    }
    return null;
  }

  /**
   * Delegates to filter.
   * <p/>
   * {@inheritDoc}
   *
   * @return the display page count
   */
  @Override
  public String getDisplayPageCount() {
    if (filter != null) {
      return filter.getDisplayPageCount();
    }
    return null;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the record count
   */
  @Override
  public Integer getRecordCount() {
    if (filter != null) {
      return filter.getRecordCount();
    }
    return null;
  }

  /**
   * Delegates to filter.
   * <p/>
   * {@inheritDoc}
   *
   * @return the display record count
   */
  @Override
  public String getDisplayRecordCount() {
    if (filter != null) {
      return filter.getDisplayRecordCount();
    }
    return null;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the selected record count
   */
  @Override
  public Integer getSelectedRecordCount() {
    if (filter != null) {
      return filter.getSelectedRecordCount();
    }
    return null;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isNextPageEnabled() {
    if (filter != null) {
      return filter.isNextPageEnabled();
    }
    return false;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isPageNavigationEnabled() {
    if (filter != null) {
      return filter.isPageNavigationEnabled();
    }
    return false;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isPreviousPageEnabled() {
    if (filter != null) {
      return filter.isPreviousPageEnabled();
    }
    return false;
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @param page
   *     the page
   */
  @Override
  public void setPage(Integer page) {
    if (filter != null) {
      filter.setPage(page);
    }
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @param recordCount
   *     the record count
   */
  @Override
  public void setRecordCount(Integer recordCount) {
    if (filter != null) {
      filter.setRecordCount(recordCount);
    }
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @param selectedRecordCount
   *     the selected record count
   */
  @Override
  public void setSelectedRecordCount(Integer selectedRecordCount) {
    if (filter != null) {
      filter.setSelectedRecordCount(selectedRecordCount);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return the filterable bean collection module
   */
  @Override
  public FilterableBeanCollectionModule clone() {
    FilterableBeanCollectionModule clone = (FilterableBeanCollectionModule) super.clone();
    clone.filterComponentTracker = new FilterComponentTracker(clone);
    if (filter != null) {
      clone.setFilter(filter.clone());
    }
    return clone;
  }

  /**
   * Gets the pagingAction.
   *
   * @return the pagingAction.
   */
  public BackendAction getPagingAction() {
    return pagingAction;
  }

  /**
   * Sets the pagingAction.
   *
   * @param pagingAction
   *     the pagingAction to set.
   */
  public void setPagingAction(BackendAction pagingAction) {
    this.pagingAction = pagingAction;
  }

  /**
   * Delegates to filter. {@inheritDoc}
   *
   * @param stickyResults
   *     the sticky results
   */
  @Override
  public void setStickyResults(List<?> stickyResults) {
    if (filter != null) {
      filter.setStickyResults(stickyResults);
    }
  }

  /**
   * Delegates to filter.
   * <p>
   * {@inheritDoc}
   *
   * @return the sticky results
   */
  @Override
  public List<?> getStickyResults() {
    if (filter != null) {
      return filter.getStickyResults();
    }
    return null;
  }

  /**
   * Sets the queryComponentDescriptorFactory.
   *
   * @param queryComponentDescriptorFactory
   *     the queryComponentDescriptorFactory to set.
   */
  public void setQueryComponentDescriptorFactory(IQueryComponentDescriptorFactory queryComponentDescriptorFactory) {
    this.queryComponentDescriptorFactory = queryComponentDescriptorFactory;
  }

  /**
   * Gets results.
   *
   * @return the results
   */
  @Override
  public List<?> getResults() {
    return getModuleObjects();
  }

  /**
   * Serialize filter criteria.
   *
   * @return the serialized form of the filter criteria component
   *
   * @throws IOException
   *     the iO exception
   */
  public String serializeCriteria() throws IOException {
    return QueryComponentSerializationUtil.serializeFilter(getFilter(), new LinkedHashMap<String, Serializable>());
  }


  /**
   * Deserialize filter criteria from base 64 from
   * and hydrate que query component.
   *
   * @param filterAsBase64
   *     the serialized form of the filter criteria
   * @throws ClassNotFoundException
   *     the class not found exception
   * @throws IOException
   *     the iO exception
   */
  public void deserializeCriteria(String filterAsBase64) throws ClassNotFoundException, IOException {
    Serializable[] criteria = QueryComponentSerializationUtil.deserializeFilter(filterAsBase64);
    hydrateCriteria(getFilter(), criteria);
  }

  /**
   * Hydrate filter criteria.
   *
   * @param query
   *     the query
   * @param filters
   *     as table of     - Odd indexes : parameter key     - Even indexes : parameter value
   * @throws IOException
   *     the iO exception
   * @throws ClassNotFoundException
   *     the class not found exception
   */
  protected void hydrateCriteria(IQueryComponent query, Serializable... filters)
      throws IOException, ClassNotFoundException {

    for (int i = 0; i < filters.length; i += 2) {
      String key = (String) filters[i];
      Serializable value = filters[i + 1];

      if (value instanceof Serializable[]) {
        Serializable[] delegate = (Serializable[]) value;
        QueryComponent qc = (QueryComponent) query.get(key);

        // recurse
        if (qc != null) {
          hydrateCriteria(qc, delegate);
        }
      } else if (value instanceof String && ((String) value).startsWith("[[")) {
        // This is an EnumQueryStructure serialized value
        Set<String> enumValues = new HashSet<>(
            Arrays.asList(((String) value).substring(2, ((String) value).length() - 2).split("§")));
        EnumQueryStructure eqs = (EnumQueryStructure) query.get(key);
        for (EnumValueQueryStructure evqs : eqs.getEnumerationValues()) {
          evqs.setSelected(
              enumValues.contains(evqs.getValue() == null ? String.valueOf((Object) null) : evqs.getValue()));
        }
      } else {
        query.put(key, value);
      }
    }
  }

  /**
   * Sets query component refiner.
   *
   * @param queryComponentRefiner
   *     the query component refiner
   */
  public void setQueryComponentRefiner(IQueryComponentRefiner queryComponentRefiner) {
    this.queryComponentRefiner = queryComponentRefiner;
  }

  /**
   * Sets criteria factory. Depending on the persistence layer used, it should be an instance of :
   * <ul>
   * <li>org.jspresso.framework.model.persistence.hibernate.criterion.ICriteriaFactory</li>
   * <li>org.jspresso.framework.model.persistence.mongo.criterion.IQueryFactory</li>
   * </ul>
   *
   * @param criteriaFactory
   *     the criteria factory
   */
  public void setCriteriaFactory(Object criteriaFactory) {
    this.criteriaFactory = criteriaFactory;
  }

  /**
   * Sets criteria refiner. Depending on the persistence layer used, it should be an instance of :
   * <ul>
   * <li>org.jspresso.framework.application.backend.action.persistence.hibernate.ICriteriaRefiner</li>
   * <li>org.jspresso.framework.application.backend.action.persistence.mongo.IQueryRefiner</li>
   * </ul>
   *
   * @param criteriaRefiner
   *     the criteria refiner
   */
  public void setCriteriaRefiner(Object criteriaRefiner) {
    this.criteriaRefiner = criteriaRefiner;
  }

  /**
   * Gets query component refiner.
   *
   * @return the query component refiner
   */
  public IQueryComponentRefiner getQueryComponentRefiner() {
    return queryComponentRefiner;
  }

  /**
   * Gets criteria refiner.
   *
   * @return the criteria refiner
   */
  public Object getCriteriaRefiner() {
    return criteriaRefiner;
  }

  /**
   * Gets criteria factory.
   *
   * @return the criteria factory
   */
  public Object getCriteriaFactory() {
    return criteriaFactory;
  }
}
