/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model;

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

  private Object          filter;
  private IViewDescriptor filterViewDescriptor;

  /**
   * Gets the filter.
   * 
   * @return the filter.
   */
  public Object getFilter() {
    return filter;
  }

  /**
   * Sets the filter.
   * 
   * @param filter
   *            the filter to set.
   */
  public void setFilter(Object filter) {
    this.filter = filter;
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
   * Sets the filterViewDescriptor.
   * 
   * @param filterViewDescriptor
   *            the filterViewDescriptor to set.
   */
  public void setFilterViewDescriptor(IViewDescriptor filterViewDescriptor) {
    this.filterViewDescriptor = filterViewDescriptor;
  }
}
