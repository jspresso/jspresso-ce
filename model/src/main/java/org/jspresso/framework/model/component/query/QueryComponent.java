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
package org.jspresso.framework.model.component.query;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.collection.ObjectEqualityMap;

/**
 * The default implementation of a query component.
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
public class QueryComponent extends ObjectEqualityMap<String, Object> implements
    IQueryComponent {

  private static final long       serialVersionUID = 4271673164192796253L;

  private IComponentDescriptor<?> componentDescriptor;
  private Integer                 page;
  private Integer                 pageSize;
  private Integer                 recordCount;

  /**
   * Constructs a new <code>QueryComponent</code> instance.
   * 
   * @param componentDescriptor
   *          the query componentDescriptor.
   */
  public QueryComponent(IComponentDescriptor<?> componentDescriptor) {
    this.componentDescriptor = componentDescriptor;
    setPageSize(componentDescriptor.getPageSize());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object get(Object key) {
    IPropertyDescriptor propertyDescriptor = componentDescriptor
        .getPropertyDescriptor((String) key);
    Object actualValue = super.get(key);
    if (actualValue == null
        && propertyDescriptor instanceof IReferencePropertyDescriptor) {
      IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      if (isInlineComponentDescriptor(referencedDescriptor)) {
        QueryComponent referencedQueryComponent = new QueryComponent(
            referencedDescriptor);
        referencedQueryComponent
            .addPropertyChangeListener(new InlinedComponentTracker(
                propertyDescriptor.getName()));
        put((String) key, referencedQueryComponent);
        return referencedQueryComponent;
      }
    }
    return actualValue;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public List<? extends IComponent> getQueriedComponents() {
    return (List<? extends IComponent>) get(QUERIED_COMPONENTS);
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getQueryContract() {
    return componentDescriptor.getQueryComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  public void setQueriedComponents(List<? extends IComponent> queriedComponents) {
    put(QUERIED_COMPONENTS, queriedComponents);
  }

  private boolean isInlineComponentDescriptor(
      IComponentDescriptor<?> referencedComponentDescriptor) {
    return !IEntity.class.isAssignableFrom(referencedComponentDescriptor
        .getComponentContract())
        && !referencedComponentDescriptor.isPurelyAbstract();
  }

  private class InlinedComponentTracker implements PropertyChangeListener {

    private String componentName;

    /**
     * Constructs a new <code>InnerComponentTracker</code> instance.
     * 
     * @param componentName
     *          the name of the component to track the properties.
     */
    public InlinedComponentTracker(String componentName) {
      this.componentName = componentName;
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent evt) {
      firePropertyChange(componentName, null, evt.getSource());
      firePropertyChange(componentName + "." + evt.getPropertyName(), evt
          .getOldValue(), evt.getNewValue());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPage() {
    return page;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPage(Integer page) {
    Integer oldValue = getPage();
    boolean oldPreviousPageEnabled = isPreviousPageEnabled();
    boolean oldNextPageEnabled = isNextPageEnabled();
    this.page = page;
    firePropertyChange(PAGE, oldValue, getPage());
    firePropertyChange("previousPageEnabled", new Boolean(
        oldPreviousPageEnabled), new Boolean(isPreviousPageEnabled()));
    firePropertyChange("nextPageEnabled", new Boolean(oldNextPageEnabled),
        new Boolean(isNextPageEnabled()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPageSize(Integer pageSize) {
    Integer oldValue = getPageSize();
    Integer oldPageCount = getPageCount();
    this.pageSize = pageSize;
    firePropertyChange(PAGE_SIZE, oldValue, getPageSize());
    firePropertyChange(PAGE_COUNT, oldPageCount, getPageCount());
  }

  /**
   * {@inheritDoc}
   */
  public Integer getRecordCount() {
    return recordCount;
  }

  /**
   * Sets the recordCount.
   * 
   * @param recordCount
   *          the recordCount to set.
   */
  public void setRecordCount(Integer recordCount) {
    Integer oldValue = getRecordCount();
    Integer oldPageCount = getPageCount();
    boolean oldNextPageEnabled = isNextPageEnabled();
    this.recordCount = recordCount;
    firePropertyChange(RECORD_COUNT, oldValue, getRecordCount());
    firePropertyChange(PAGE_COUNT, oldPageCount, getPageCount());
    firePropertyChange("nextPageEnabled", new Boolean(oldNextPageEnabled),
        new Boolean(isNextPageEnabled()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPageCount() {
    if (getRecordCount() == null) {
      return null;
    }
    if (getPageSize() == null || getPageSize().intValue() <= 0) {
      return new Integer(1);
    }
    return new Integer(getRecordCount().intValue() / getPageSize().intValue()
        + 1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNextPageEnabled() {
    return getPageCount() != null && getPage() != null
        && getPage().intValue() < getPageCount().intValue() - 2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPreviousPageEnabled() {
    return getPage() != null && getPage().intValue() > 0;
  }

}
