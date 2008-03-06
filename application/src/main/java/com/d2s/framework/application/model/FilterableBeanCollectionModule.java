/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model;

import com.d2s.framework.util.lang.ObjectUtils;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A bean collection module that offers a filter.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FilterableBeanCollectionModule extends BeanCollectionModule {

  private Object                       filter;
  private IComponentDescriptor<Object> filterComponentDescriptor;
  private IViewDescriptor              filterViewDescriptor;

  /**
   * Gets the filter.
   * 
   * @return the filter.
   */
  public Object getFilter() {
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
   *            the filter to set.
   */
  public void setFilter(Object filter) {
    if (ObjectUtils.equals(this.filter, filter)) {
      return;
    }
    Object oldValue = getFilter();
    this.filter = filter;
    firePropertyChange("filter", oldValue, getFilter());
  }

  /**
   * Sets the filterComponentDescriptor.
   * 
   * @param filterComponentDescriptor
   *            the filterComponentDescriptor to set.
   */
  public void setFilterComponentDescriptor(
      IComponentDescriptor<Object> filterComponentDescriptor) {
    this.filterComponentDescriptor = filterComponentDescriptor;
  }

  /**
   * Sets the filterViewDescriptor.
   * 
   * @param filterViewDescriptor
   *            the filterViewDescriptor to set.
   */
  public void setFilterViewDescriptor(IViewDescriptor filterViewDescriptor) {
    this.filterViewDescriptor = filterViewDescriptor;
  }
}
