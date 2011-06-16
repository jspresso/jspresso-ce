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
package org.jspresso.framework.model.component.query;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.collection.ObjectEqualityMap;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * The default implementation of a query component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryComponent extends ObjectEqualityMap<String, Object> implements
    IQueryComponent {

  private static final long       serialVersionUID = 4271673164192796253L;

  private IComponentDescriptor<?> componentDescriptor;
  private IComponentFactory       componentFactory;
  private Map<String, ESort>      defaultOrderingProperties;
  private Map<String, ESort>      orderingProperties;
  private Integer                 page;
  private Integer                 pageSize;
  private Integer                 recordCount;

  /**
   * Constructs a new <code>QueryComponent</code> instance.
   * 
   * @param componentDescriptor
   *          the query componentDescriptor.
   * @param componentFactory
   *          the component factory.
   */
  public QueryComponent(IComponentDescriptor<?> componentDescriptor,
      IComponentFactory componentFactory) {
    this.componentDescriptor = componentDescriptor;
    this.componentFactory = componentFactory;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object get(Object key) {
    int firstDotIndex = ((String) key).indexOf('.');
    if (firstDotIndex > 0) {
      Object nested = get(((String) key).substring(0, firstDotIndex));
      if (nested instanceof Map<?, ?>) {
        return ((Map<String, Object>) nested).get(((String) key)
            .substring(firstDotIndex + 1));
      }
    }
    IPropertyDescriptor propertyDescriptor = componentDescriptor
        .getPropertyDescriptor((String) key);
    Object actualValue = super.get(key);
    if (actualValue == null
        && propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      QueryComponent referencedQueryComponent = new QueryComponent(
          referencedDescriptor, getComponentFactory());
      if (ComparableQueryStructure.class
          .isAssignableFrom(referencedQueryComponent.getQueryContract())) {
        referencedQueryComponent.put(
            ComparableQueryStructureDescriptor.COMPARATOR,
            ComparableQueryStructureDescriptor.EQ);
      }
      referencedQueryComponent
          .addPropertyChangeListener(new InlinedComponentTracker(
              propertyDescriptor.getName()));
      put((String) key, referencedQueryComponent);
      return referencedQueryComponent;
    }
    return actualValue;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object put(String key, Object value) {
    int firstDotIndex = key.indexOf('.');
    if (firstDotIndex > 0) {
      Object nested = get(key.substring(0, firstDotIndex));
      if (nested instanceof Map<?, ?>) {
        return ((Map<String, Object>) nested).put(
            key.substring(firstDotIndex + 1), value);
      }
    }
    IPropertyDescriptor propertyDescriptor = componentDescriptor
        .getPropertyDescriptor(key);
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      if (IEntity.class.isAssignableFrom(referencedDescriptor
          .getComponentContract())) {
        if (!(value instanceof IQueryComponent)) {
          Object actualValue = /* super. */get(key);
          String tsProp = referencedDescriptor.getToStringProperty();
          String acProp = referencedDescriptor.getAutoCompleteProperty();
          if (value != null) {
            ((IQueryComponent) actualValue).put(IEntity.ID,
                ((IEntity) value).getId());
            ((IQueryComponent) actualValue).put(tsProp,
                ((IEntity) value).toString());
            if (acProp != null) {
              Object acPropVaue;
              try {
                acPropVaue = getComponentFactory()
                    .getAccessorFactory()
                    .createPropertyAccessor(acProp,
                        ((IEntity) value).getComponentContract())
                    .getValue(value);
              } catch (IllegalAccessException ex) {
                throw new NestedRuntimeException(ex, "Invalid property: "
                    + acProp);
              } catch (InvocationTargetException ex) {
                throw new NestedRuntimeException(ex.getTargetException(),
                    "Invalid property: " + acProp);
              } catch (NoSuchMethodException ex) {
                throw new NestedRuntimeException(ex, "Invalid property: "
                    + acProp);
              }
              ((IQueryComponent) actualValue).put(acProp, acPropVaue);
            }
          } else {
            ((IQueryComponent) actualValue).remove(IEntity.ID);
            ((IQueryComponent) actualValue).remove(tsProp);
            if (acProp != null) {
              ((IQueryComponent) actualValue).remove(acProp);
            }
          }
          return actualValue;
        }
      }
    }
    return super.put(key, value);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object remove(Object key) {
    int firstDotIndex = ((String) key).indexOf('.');
    if (firstDotIndex > 0) {
      Object nested = get(((String) key).substring(0, firstDotIndex));
      if (nested instanceof Map<?, ?>) {
        return ((Map<String, Object>) nested).remove(((String) key)
            .substring(firstDotIndex + 1));
      }
    }
    return super.remove(key);
  }

  /**
   * Gets the componentDescriptor.
   * 
   * @return the componentDescriptor.
   */
  @Override
  public IComponentDescriptor<?> getComponentDescriptor() {
    return componentDescriptor;
  }

  /**
   * Gets the orderingProperties.
   * 
   * @return the orderingProperties.
   */
  @Override
  public Map<String, ESort> getOrderingProperties() {
    if (orderingProperties == null || orderingProperties.isEmpty()) {
      if (defaultOrderingProperties == null) {
        return componentDescriptor.getOrderingProperties();
      }
      return defaultOrderingProperties;
    }
    return orderingProperties;
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
  public Integer getDisplayPageIndex() {
    int pc = 0;
    if (getPageCount() != null) {
      pc = getPageCount().intValue();
    }
    if (pc == 0) {
      return new Integer(0);
    }
    int p = 0;
    if (getPage() != null) {
      p = getPage().intValue();
    }
    return new Integer(p + 1);
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
    int remainder = getRecordCount().intValue() % getPageSize().intValue();
    int lastIncompletePage = 0;
    if (remainder > 0) {
      lastIncompletePage = 1;
    }
    return new Integer(getRecordCount().intValue() / getPageSize().intValue()
        + lastIncompletePage);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPageSize() {
    if (pageSize == null) {
      return componentDescriptor.getPageSize();
    }
    return pageSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends IComponent> getQueriedComponents() {
    return (List<? extends IComponent>) get(QUERIED_COMPONENTS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getQueryContract() {
    return componentDescriptor.getQueryComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getRecordCount() {
    return recordCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInlineComponent() {
    return !IEntity.class.isAssignableFrom(componentDescriptor
        .getQueryComponentContract())
        && !componentDescriptor.isPurelyAbstract();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNextPageEnabled() {
    return getPageCount() != null && getPage() != null
        && getPage().intValue() < getPageCount().intValue() - 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPreviousPageEnabled() {
    return getPage() != null && getPage().intValue() > 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDefaultOrderingProperties(
      Map<String, ESort> defaultOrderingProperties) {
    this.defaultOrderingProperties = defaultOrderingProperties;
  }

  /**
   * Sets the sortingAttributes.
   * 
   * @param orderingProperties
   *          the sortingAttributes to set.
   */
  @Override
  public void setOrderingProperties(Map<String, ESort> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPage(Integer page) {
    Integer oldValue = getPage();
    Integer oldDisplayPageIndex = getDisplayPageIndex();
    boolean oldPreviousPageEnabled = isPreviousPageEnabled();
    boolean oldNextPageEnabled = isNextPageEnabled();
    this.page = page;
    firePropertyChange(PAGE, oldValue, getPage());
    firePropertyChange(DISPLAY_PAGE_INDEX, oldDisplayPageIndex,
        getDisplayPageIndex());
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
  @Override
  public void setQueriedComponents(List<?> queriedComponents) {
    put(QUERIED_COMPONENTS, queriedComponents);
  }

  /**
   * Sets the recordCount.
   * 
   * @param recordCount
   *          the recordCount to set.
   */
  @Override
  public void setRecordCount(Integer recordCount) {
    Integer oldValue = getRecordCount();
    Integer oldPageCount = getPageCount();
    Integer oldDisplayPageIndex = getDisplayPageIndex();
    boolean oldNextPageEnabled = isNextPageEnabled();
    this.recordCount = recordCount;
    firePropertyChange(RECORD_COUNT, oldValue, getRecordCount());
    firePropertyChange(PAGE_COUNT, oldPageCount, getPageCount());
    firePropertyChange(DISPLAY_PAGE_INDEX, oldDisplayPageIndex,
        getDisplayPageIndex());
    firePropertyChange("nextPageEnabled", new Boolean(oldNextPageEnabled),
        new Boolean(isNextPageEnabled()));
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
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      firePropertyChange(componentName, null, evt.getSource());
      firePropertyChange(componentName + "." + evt.getPropertyName(),
          evt.getOldValue(), evt.getNewValue());
    }
  }

  /**
   * Gets the componentFactory.
   * 
   * @return the componentFactory.
   */
  protected IComponentFactory getComponentFactory() {
    return componentFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryComponent clone() {
    QueryComponent clone = (QueryComponent) super.clone();
    return clone;
  }
}
