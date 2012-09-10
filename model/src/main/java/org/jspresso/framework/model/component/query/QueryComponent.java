/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.AbstractEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.descriptor.query.EnumQueryStructureDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.util.collection.ObjectEqualityMap;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * The default implementation of a query component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryComponent extends ObjectEqualityMap<String, Object> implements
    IQueryComponent {

  private IComponentDescriptor<?> componentDescriptor;
  private IComponentDescriptor<?> queryDescriptor;
  private IComponentFactory       componentFactory;
  private Map<String, ESort>      defaultOrderingProperties;
  private Map<String, ESort>      orderingProperties;
  private Integer                 page;
  private Integer                 pageSize;
  private Integer                 recordCount;
  private boolean                 distinctEnforced;

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
    this.queryDescriptor = componentFactory
        .getComponentDescriptor(getQueryContract());
    if (queryDescriptor == null) {
      queryDescriptor = componentDescriptor;
    }
    if (!ComparableQueryStructure.class.isAssignableFrom(queryDescriptor
        .getComponentContract())) {
      for (IPropertyDescriptor propertyDescriptor : queryDescriptor
          .getPropertyDescriptors()) {
        if (propertyDescriptor instanceof AbstractEnumerationPropertyDescriptor
            && ((AbstractEnumerationPropertyDescriptor) propertyDescriptor)
                .isQueryMultiselect()) {
          EnumQueryStructure enumQueryStructure = new EnumQueryStructure(
              (IEnumerationPropertyDescriptor) propertyDescriptor);
          put(propertyDescriptor.getName(), enumQueryStructure);
        }
      }
    }
    this.distinctEnforced = false;
  }

  /**
   * Assigns translation provider as well as session locale to enumeration query
   * structures.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void translate(ITranslationProvider translationProvider, Locale locale) {
    for (Object value : values()) {
      if (value instanceof EnumQueryStructure) {
        ((EnumQueryStructure) value)
            .setTranslationProvider(translationProvider);
        ((EnumQueryStructure) value).setLocale(locale);
      }
    }
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
        && propertyDescriptor instanceof IReferencePropertyDescriptor<?>
        && !(propertyDescriptor instanceof EnumQueryStructureDescriptor)) {
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
          IQueryComponent actualValue = (IQueryComponent) /* super. */get(key);
          String tsProp = referencedDescriptor.getToStringProperty();
          String acProp = referencedDescriptor.getAutoCompleteProperty();
          if (value != null) {
            if (value instanceof IEntity) {
              Serializable[] queryPropertyValues = extractQueryPropertyValues(
                  (IEntity) value, acProp);
              actualValue.put(IEntity.ID, queryPropertyValues[0]);
              actualValue.put(tsProp, queryPropertyValues[1]);
              if (acProp != null) {
                actualValue.put(acProp, queryPropertyValues[2]);
              }
            } else if (value instanceof Collection<?>) {
              if (((Collection<?>) value).size() == 1) {
                return put(key, ((Collection<?>) value).iterator().next());
              }
              StringBuffer idValue = null;
              StringBuffer tsValue = null;
              StringBuffer acValue = null;
              for (Object element : (Collection<?>) value) {
                if (element instanceof IEntity) {
                  Serializable[] queryPropertyValues = extractQueryPropertyValues(
                      (IEntity) element, acProp);
                  if (queryPropertyValues[0] instanceof CharSequence) {
                    if (idValue == null) {
                      idValue = new StringBuffer(
                          queryPropertyValues[0].toString());
                    } else {
                      idValue.append(IQueryComponent.DISJUNCT).append(
                          queryPropertyValues[0].toString());
                    }
                  }
                  if (queryPropertyValues[1] instanceof CharSequence) {
                    if (tsValue == null) {
                      tsValue = new StringBuffer(
                          queryPropertyValues[1].toString());
                    } else {
                      tsValue.append(IQueryComponent.DISJUNCT).append(
                          queryPropertyValues[1].toString());
                    }
                  }
                  if (acProp != null
                      && queryPropertyValues[2] instanceof CharSequence) {
                    if (acValue == null) {
                      acValue = new StringBuffer(
                          queryPropertyValues[2].toString());
                    } else {
                      acValue.append(IQueryComponent.DISJUNCT).append(
                          queryPropertyValues[2].toString());
                    }
                  }
                }
              }
              if (idValue != null) {
                actualValue.put(IEntity.ID, idValue.toString());
              } else {
                actualValue.remove(IEntity.ID);
              }
              if (tsValue != null) {
                actualValue.put(tsProp, tsValue.toString());
              } else {
                actualValue.remove(tsProp);
              }
              if (acProp != null) {
                if (acValue != null) {
                  actualValue.put(acProp, acValue.toString());
                } else {
                  actualValue.remove(acProp);
                }
              }
            } else if (value instanceof String) {
              if (acProp != null) {
                actualValue.put(acProp, value);
              }
            }
          } else {
            actualValue.remove(IEntity.ID);
            actualValue.remove(tsProp);
            if (acProp != null) {
              actualValue.remove(acProp);
            }
          }
          return actualValue;
        }
      } else if (propertyDescriptor instanceof EnumQueryStructureDescriptor) {
        Object actualValue = /* super. */get(key);
        if (actualValue instanceof EnumQueryStructure) {
          if (value == null) {
            ((EnumQueryStructure) actualValue).clear();
          }
          // Never nullify an EnumQueryStructure
          return null;
        } else if (!(value instanceof EnumQueryStructure)) {
          return null;
        }
      }
    }
    return super.put(key, value);
  }

  private Serializable[] extractQueryPropertyValues(IEntity entity,
      String acProp) {
    String acPropValue = null;
    if (acProp != null) {
      try {
        acPropValue = (String) getComponentFactory().getAccessorFactory()
            .createPropertyAccessor(acProp, entity.getComponentContract())
            .getValue(entity);
      } catch (IllegalAccessException ex) {
        throw new NestedRuntimeException(ex, "Invalid property: " + acProp);
      } catch (InvocationTargetException ex) {
        throw new NestedRuntimeException(ex.getTargetException(),
            "Invalid property: " + acProp);
      } catch (NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex, "Invalid property: " + acProp);
      }
    }
    return new Serializable[] {
        entity.getId(), entity.toString(), acPropValue
    };
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
   * Gets the queryComponentDescriptor.
   * 
   * @return the componentDescriptor.
   */
  @Override
  public IComponentDescriptor<?> getQueryDescriptor() {
    return queryDescriptor;
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
  public void setDisplayPageIndex(Integer displayPageIndex) {
    if (displayPageIndex != null) {
      setPage(new Integer(displayPageIndex.intValue() - 1));
    } else {
      setPage(null);
    }
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
    if (componentDescriptor instanceof IQueryComponentDescriptor) {
      return ((IQueryComponentDescriptor) componentDescriptor)
          .getQueriedComponentsDescriptor().getComponentContract();
    }
    return componentDescriptor.getComponentContract();
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
    return !IEntity.class.isAssignableFrom(getQueryContract())
        && !componentDescriptor.isPurelyAbstract();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPageNavigationEnabled() {
    return getPageCount() != null && getPageCount().intValue() > 1;
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

    int pc = 0;
    if (getPageCount() != null) {
      pc = getPageCount().intValue();
    }
    if (pc > 0) {
      if (page == null || page.intValue() < 0) {
        this.page = new Integer(0);
      } else if (page.intValue() >= pc) {
        this.page = new Integer(pc - 1);
      } else {
        this.page = page;
      }
    } else {
      this.page = page;
    }

    firePropertyChange(PAGE, oldValue, getPage());
    firePropertyChange(DISPLAY_PAGE_INDEX, oldDisplayPageIndex,
        getDisplayPageIndex());
    firePropertyChange(IPageable.PREVIOUS_PAGE_ENABLED, new Boolean(
        oldPreviousPageEnabled), new Boolean(isPreviousPageEnabled()));
    firePropertyChange(IPageable.NEXT_PAGE_ENABLED, new Boolean(
        oldNextPageEnabled), new Boolean(isNextPageEnabled()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPageSize(Integer pageSize) {
    Integer oldValue = getPageSize();
    Integer oldPageCount = getPageCount();
    boolean oldPageNavigationEnabled = isPageNavigationEnabled();
    this.pageSize = pageSize;
    firePropertyChange(PAGE_SIZE, oldValue, getPageSize());
    firePropertyChange(PAGE_COUNT, oldPageCount, getPageCount());
    firePropertyChange(PAGE_NAVIGATION_ENABLED, new Boolean(
        oldPageNavigationEnabled), new Boolean(isPageNavigationEnabled()));
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
    boolean oldPageNavigationEnabled = isPageNavigationEnabled();
    this.recordCount = recordCount;
    firePropertyChange(RECORD_COUNT, oldValue, getRecordCount());
    firePropertyChange(PAGE_COUNT, oldPageCount, getPageCount());
    firePropertyChange(DISPLAY_PAGE_INDEX, oldDisplayPageIndex,
        getDisplayPageIndex());
    firePropertyChange(IPageable.NEXT_PAGE_ENABLED, new Boolean(
        oldNextPageEnabled), new Boolean(isNextPageEnabled()));
    firePropertyChange(IPageable.PAGE_NAVIGATION_ENABLED, new Boolean(
        oldPageNavigationEnabled), new Boolean(isPageNavigationEnabled()));
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

  /**
   * Gets wether to enforce select distinct when querying.
   * 
   * @return the distinctEnforced.
   */
  @Override
  public boolean isDistinctEnforced() {
    return distinctEnforced;
  }

  /**
   * Sets the distinctEnforced.
   * 
   * @param distinctEnforced
   *          the distinctEnforced to set.
   */
  public void setDistinctEnforced(boolean distinctEnforced) {
    this.distinctEnforced = distinctEnforced;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStickyResults(List<?> stickyResults) {
    put(STICKY_RESULTS, stickyResults);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<?> getStickyResults() {
    return (List<Object>) get(STICKY_RESULTS);
  }
}
