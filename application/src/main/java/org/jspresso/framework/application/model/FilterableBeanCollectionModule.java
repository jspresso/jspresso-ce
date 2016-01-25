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

import org.jspresso.framework.application.backend.action.BackendAction;
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
import org.jspresso.framework.view.descriptor.EBorderType;
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
public class FilterableBeanCollectionModule extends BeanCollectionModule
    implements IPageable {

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
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    IViewDescriptor superViewDescriptor = super.getViewDescriptor();
    IComponentDescriptor<?> moduleDescriptor = (IComponentDescriptor<?>) superViewDescriptor.getModelDescriptor();
    IViewDescriptor filterViewDesc = buildFilterViewDescriptor(moduleDescriptor);

    BasicBorderViewDescriptor decorator = new BasicBorderViewDescriptor();
    decorator.setNorthViewDescriptor(filterViewDesc);
    decorator.setCenterViewDescriptor(superViewDescriptor);

    BasicCollectionViewDescriptor moduleObjectsView = (BasicCollectionViewDescriptor) BasicCollectionViewDescriptor
        .extractMainCollectionView(getProjectedViewDescriptor());
    if (getPageSize() != null && getPageSize() > 0) {
      if (moduleObjectsView != null
          && moduleObjectsView.getPaginationViewDescriptor() == null) {
        moduleObjectsView.setPaginationViewDescriptor(getPaginationViewDescriptor());
      }
    }
    decorator.setModelDescriptor(superViewDescriptor.getModelDescriptor());
    return decorator;
  }

  /**
   * Build the filter view descriptor.
   * @param moduleDescriptor
   *     The module descriptor
   *
   * @return the filter view descriptor
   */
  @SuppressWarnings("unchecked")
  protected IViewDescriptor buildFilterViewDescriptor(IComponentDescriptor<?> moduleDescriptor) {

    IComponentDescriptor<IComponent> realComponentDesc = getFilterComponentDescriptor();
    IViewDescriptor filterViewDesc = getFilterViewDescriptor();
    IComponentDescriptorProvider<IQueryComponent> filterModelDescriptorProvider =
        (IComponentDescriptorProvider<IQueryComponent>) moduleDescriptor.getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER);
    boolean customFilterView = false;
    if (filterViewDesc == null) {
      filterViewDesc = getQueryViewDescriptorFactory().createQueryViewDescriptor(realComponentDesc,
          filterModelDescriptorProvider.getComponentDescriptor(), Collections.<String, Object>emptyMap());
    } else {
      customFilterView = true;
      // Deeply clean model descriptors on filter views
      cleanupFilterViewDescriptor(filterViewDesc);
    }
    if (filterViewDesc instanceof BasicViewDescriptor) {
      ((BasicViewDescriptor) filterViewDesc).setBorderType(EBorderType.TITLED);
      ((BasicViewDescriptor) filterViewDesc).setModelDescriptor(filterModelDescriptorProvider);
    }
    if (customFilterView) {
      getQueryViewDescriptorFactory().adaptExistingViewDescriptor(filterViewDesc);
    }

    BasicTabViewDescriptor tabFilterView = getFilterExtraViewDescriptor();
    if (tabFilterView == null) {
      IQueryExtraViewDescriptorFactory extraViewFactory = getQueryExtraViewDescriptorFactory();
      if (extraViewFactory != null) {
        tabFilterView = extraViewFactory.createQueryExtraViewDescriptor(
            getProjectedViewDescriptor(),
            realComponentDesc,
            filterModelDescriptorProvider.getComponentDescriptor());
      }
    }
    if (tabFilterView!=null) {
      List<IViewDescriptor> tabs = new ArrayList<>();
      for (IViewDescriptor view : tabFilterView.getChildViewDescriptors()) {
        BasicViewDescriptor v = ((BasicViewDescriptor) view).clone();
        v.setModelDescriptor(filterModelDescriptorProvider);
        tabs.add(v);
      }

      if (filterViewDesc instanceof ITabViewDescriptor) {
        for (IViewDescriptor view : ((ICompositeViewDescriptor) filterViewDesc).getChildViewDescriptors()) {
          BasicViewDescriptor v = ((BasicViewDescriptor) view).clone();
          v.setModelDescriptor(filterModelDescriptorProvider);
          tabs.add(v);
        }
      }
      else if (filterViewDesc instanceof BasicViewDescriptor) {
        ((BasicViewDescriptor) filterViewDesc).setBorderType(EBorderType.NONE);
        tabs.add(filterViewDesc);
      }

      BasicTabViewDescriptor tabView = new BasicTabViewDescriptor();
      tabView.setPermId(getPermId() + ".filter");
      tabView.setRenderingOptions(ERenderingOptions.LABEL);
      tabView.setTabs(tabs);

      filterViewDesc = tabView;
    }
    return filterViewDesc;
  }

  /**
   * Cleanup filter view descriptor.
   *
   * @param filterViewDesc the filter view desc
   */
  protected void cleanupFilterViewDescriptor(IViewDescriptor filterViewDesc) {
    if (filterViewDesc instanceof BasicViewDescriptor) {
      ((BasicViewDescriptor) filterViewDesc).setModelDescriptor(null);
    }
    if (filterViewDesc instanceof ICompositeViewDescriptor) {
      List<IViewDescriptor> children = ((ICompositeViewDescriptor) filterViewDesc)
          .getChildViewDescriptors();
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
   *          the filter to set.
   */
  public void setFilter(IQueryComponent filter) {
    if (ObjectUtils.equals(this.filter, filter)) {
      return;
    }
    IQueryComponent oldValue = getFilter();
    if (oldValue != null) {
      oldValue
          .removePropertyChangeListener(filterComponentTracker);
    }
    this.filter = filter;
    if (filter != null) {
      filter.setPageSize(getPageSize());
      filter.setDefaultOrderingProperties(getOrderingProperties());
      filter.addPropertyChangeListener(filterComponentTracker);
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
      IComponentDescriptor<IComponent> filterComponentDescriptor) {
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
   *          the filterViewDescriptor to set.
   */
  public void setFilterViewDescriptor(IViewDescriptor filterViewDescriptor) {
    this.filterViewDescriptor = filterViewDescriptor;
  }

  /**
   * This property allow to refine the filter view. If this field is not empty
   * the filter view will be replaced by a tab view containing this view and the
   * view defined bu the {@link #setFilterViewDescriptor(IViewDescriptor)} method.
   *
   * If the extra filter view or the filter view is already a tab view, then tab
   * views will be merged to a single tab view.
   *
   * @param filterExtraViewDescriptor
   *          the filterExtraViewDescriptor to set.
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
   * Sets the queryExtraViewDescriptorFactory.
   *
   * @param queryExtraViewDescriptorFactory
   *          the queryExtraViewDescriptorFactory to set.
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
     *          the target filter module.
     */
    public FilterComponentTracker(FilterableBeanCollectionModule target) {
      this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      target.firePropertyChange(FilterableBeanCollectionModuleDescriptor.FILTER
          + "." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
      if (IPageable.DISPLAY_PAGE_INDEX.equals(evt.getPropertyName())
          || IPageable.NEXT_PAGE_ENABLED.equals(evt.getPropertyName())
          || IPageable.PAGE.equals(evt.getPropertyName())
          || IPageable.PAGE_COUNT.equals(evt.getPropertyName())
          || IPageable.DISPLAY_PAGE_COUNT.equals(evt.getPropertyName())
          || IPageable.PAGE_SIZE.equals(evt.getPropertyName())
          || IPageable.PREVIOUS_PAGE_ENABLED.equals(evt.getPropertyName())
          || IPageable.RECORD_COUNT.equals(evt.getPropertyName())
          || IPageable.DISPLAY_RECORD_COUNT.equals(evt.getPropertyName())
          || IPageable.SELECTED_RECORD_COUNT.equals(evt.getPropertyName())
          || IPageable.PAGE_NAVIGATION_ENABLED.equals(evt.getPropertyName())) {
        target.firePropertyChange(evt.getPropertyName(), evt.getOldValue(),
            evt.getNewValue());
      }
    }
  }

  /**
   * Configures the sub view used to navigate between the pages.
   *
   * @param paginationViewDescriptor
   *          the paginationViewDescriptor to set.
   */
  public void setPaginationViewDescriptor(
      BasicViewDescriptor paginationViewDescriptor) {
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
   */
  @Override
  public void setSelectedRecordCount(Integer selectedRecordCount) {
    if (filter != null) {
      filter.setSelectedRecordCount(selectedRecordCount);
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
   *          the pagingAction to set.
   */
  public void setPagingAction(BackendAction pagingAction) {
    this.pagingAction = pagingAction;
  }

  /**
   * Delegates to filter. {@inheritDoc}
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
   *          the queryComponentDescriptorFactory to set.
   */
  public void setQueryComponentDescriptorFactory(
      IQueryComponentDescriptorFactory queryComponentDescriptorFactory) {
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
   * @throws IOException the iO exception
   */
  public String serializeCriteria() throws IOException {
    return QueryComponentSerializationUtil.serializeFilter(getFilter(), new LinkedHashMap<String, Serializable>());
  }


  /**
   * Deserialize filter criteria from base 64 from
   * and hydrate que query component.
   *
   * @param filterAsBase64 the serialized form of the filter criteria
   * @throws ClassNotFoundException the class not found exception
   * @throws IOException the iO exception
   */
  public void deserializeCriteria(String filterAsBase64) throws ClassNotFoundException, IOException {
    Serializable[] criteria = QueryComponentSerializationUtil.deserializeFilter(filterAsBase64);
    hydrateCriteria(getFilter(), criteria);
  }

  /**
   * Hydrate filter criteria.
   *
   * @param query the query
   * @param filters as table of
   *        - Odd indexes : parameter key
   *        - Even indexes : parameter value
   * @throws IOException the iO exception
   * @throws ClassNotFoundException the class not found exception
   */
  protected void hydrateCriteria(IQueryComponent query, Serializable... filters) throws IOException, ClassNotFoundException {

    for (int i = 0; i < filters.length; i += 2) {
      String key = (String) filters[i];
      Serializable value = filters[i + 1];

      if (value instanceof Serializable[]) {
        Serializable[] delegate = (Serializable[]) value;
        QueryComponent qc = (QueryComponent) query.get(key);

        // recurse
        if (qc!=null)
          hydrateCriteria(qc, delegate);
      }
      else if (value instanceof String && ((String) value).startsWith("[[")) {
        // This is an EnumQueryStructure serialized value
        Set<String> enumValues = new HashSet<>(Arrays.asList(((String) value).substring(2,
            ((String) value).length() - 2).split("§")));
        EnumQueryStructure eqs = (EnumQueryStructure) query.get(key);
        for (EnumValueQueryStructure evqs : eqs.getEnumerationValues()) {
          evqs.setSelected(enumValues.contains(evqs.getValue()));
        }
      }
      else {
        query.put(key, value);
      }
    }

  }
}
