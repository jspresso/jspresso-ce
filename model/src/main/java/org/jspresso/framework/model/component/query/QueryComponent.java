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
package org.jspresso.framework.model.component.query;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
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
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 9452 $
 */
public class QueryComponent extends ObjectEqualityMap<String, Object> implements IQueryComponent {

  private static final long serialVersionUID = 1467135566962366855L;

  private final IComponentDescriptor<?> componentDescriptor;
  private final IComponentFactory       componentFactory;
  private       IComponentDescriptor<?> queryDescriptor;
  private       Map<String, ESort>      defaultOrderingProperties;
  private       Map<String, ESort>      orderingProperties;
  private       List<String>            prefetchProperties;
  private       Integer                 page;
  private       Integer                 pageSize;
  private       Integer                 recordCount;
  private       Integer                 selectedRecordCount;
  private       boolean                 distinctEnforced;
  private       List<?>                 queriedComponents;
  private       List<?>                 stickyResults;
  private       ITranslationProvider    translationProvider;
  private       Locale                  locale;
  private       IComponent              extra;

  /**
   * {@inheritDoc}
   */
  @Override
  public void setExtra(IComponent extra) {
    this.extra = extra;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponent getExtra() {
    return extra;
  }

  /**
   * Constructs a new {@code QueryComponent} instance.
   *
   * @param componentDescriptor
   *     the query componentDescriptor.
   * @param componentFactory
   *     the component factory.
   */
  public QueryComponent(IComponentDescriptor<?> componentDescriptor, IComponentFactory componentFactory) {
    this.componentDescriptor = componentDescriptor;
    this.componentFactory = componentFactory;
    this.queryDescriptor = componentFactory.getComponentDescriptor(getQueryContract());
    if (queryDescriptor == null) {
      queryDescriptor = componentDescriptor;
    }
    if (!ComparableQueryStructure.class.isAssignableFrom(queryDescriptor.getComponentContract())) {
      for (IPropertyDescriptor propertyDescriptor : getComponentDescriptor().getPropertyDescriptors()) {
        if (propertyDescriptor instanceof EnumQueryStructureDescriptor) {
          EnumQueryStructure enumQueryStructure = new EnumQueryStructure(
              (IEnumerationPropertyDescriptor) getQueryDescriptor().getPropertyDescriptor(
                  propertyDescriptor.getName()));
          put(propertyDescriptor.getName(), enumQueryStructure);
        } else if (propertyDescriptor instanceof ComparableQueryStructureDescriptor) {
          IComponentDescriptor<?> referencedDescriptor = ((ComparableQueryStructureDescriptor) propertyDescriptor)
              .getReferencedDescriptor();
          ComparableQueryStructure comparableQueryStructure = new ComparableQueryStructure(referencedDescriptor,
              getComponentFactory(), getQueryDescriptor().getPropertyDescriptor(propertyDescriptor.getName()));
          put(propertyDescriptor.getName(), comparableQueryStructure);
        }
      }
    }
    this.distinctEnforced = false;
  }

  /**
   * Assigns translation provider as well as session locale to enumeration query
   * structures.
   * <p/>
   * {@inheritDoc}
   *
   * @param aTranslationProvider
   *     the a translation provider
   * @param aLocale
   *     the a locale
   */
  @Override
  public void translate(ITranslationProvider aTranslationProvider, Locale aLocale) {
    this.translationProvider = aTranslationProvider;
    this.locale = aLocale;
    for (Object value : values()) {
      if (value instanceof EnumQueryStructure) {
        ((EnumQueryStructure) value).setTranslationProvider(aTranslationProvider);
        ((EnumQueryStructure) value).setLocale(aLocale);
      } else if (value instanceof ComparableQueryStructure) {
        ((ComparableQueryStructure) value).setTranslationProvider(aTranslationProvider);
        ((ComparableQueryStructure) value).setLocale(aLocale);
      } else if (value instanceof QueryComponent) {
        ((QueryComponent) value).translate(aTranslationProvider, aLocale);
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param key
   *     the key
   * @return the object
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object get(Object key) {
    int firstDotIndex = ((String) key).indexOf('.');
    if (firstDotIndex > 0) {
      Object nested = get(((String) key).substring(0, firstDotIndex));
      if (nested instanceof Map<?, ?>) {
        return ((Map<String, Object>) nested).get(((String) key).substring(firstDotIndex + 1));
      }
    }
    IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor((String) key);
    Object actualValue = super.get(key);
    if (actualValue == null && propertyDescriptor instanceof IReferencePropertyDescriptor<?>
        && !(propertyDescriptor instanceof ComparableQueryStructureDescriptor)
        && !(propertyDescriptor instanceof EnumQueryStructureDescriptor)) {
      IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      QueryComponent referencedQueryComponent = new QueryComponent(referencedDescriptor, getComponentFactory());
      referencedQueryComponent.translate(translationProvider, locale);
      referencedQueryComponent.addPropertyChangeListener(new InlinedComponentTracker(propertyDescriptor.getName()));
      put((String) key, referencedQueryComponent);
      return referencedQueryComponent;
    }
    return actualValue;
  }

  /**
   * {@inheritDoc}
   *
   * @param key
   *     the key
   * @param value
   *     the value
   * @return the object
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object put(String key, Object value) {
    int firstDotIndex = key.indexOf('.');
    if (firstDotIndex > 0) {
      Object nested = get(key.substring(0, firstDotIndex));
      if (nested instanceof Map<?, ?>) {
        return ((Map<String, Object>) nested).put(key.substring(firstDotIndex + 1), value);
      }
    }
    IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(key);
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      if (referencedDescriptor.isEntity()) {
        if (!(value instanceof IQueryComponent)) {
          String tsProp = referencedDescriptor.getToStringProperty();
          String acProp = referencedDescriptor.getAutoCompleteProperty();
          return buildNestedQueryComponent(key, value, tsProp, Collections.singletonList(acProp));
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
    Object refinedValue = refineValue(value, propertyDescriptor);
    return super.put(key, refinedValue);
  }

  /**
   * Build nested query component.
   *
   * @param nestedQueryComponentKey
   *     the nested query component key
   * @param incomingValue
   *     the incoming value
   * @param tsProp
   *     the toString property
   * @param extraProps
   *     the extra properties to feed
   * @return the object
   */
  public Object buildNestedQueryComponent(String nestedQueryComponentKey, Object incomingValue, String tsProp,
                                          List<String> extraProps) {
    IQueryComponent actualValue = (IQueryComponent) /* super. */get(nestedQueryComponentKey);
    if (incomingValue != null) {
      if (incomingValue instanceof IEntity) {
        List<Serializable> queryPropertyValues = extractQueryPropertyValues((IEntity) incomingValue, extraProps);
        actualValue.put(IEntity.ID, queryPropertyValues.get(0));
        actualValue.put(tsProp, queryPropertyValues.get(1));
        if (extraProps != null) {
          for (int i = 0; i < extraProps.size(); i++) {
            actualValue.put(extraProps.get(i), queryPropertyValues.get(i + 2));
          }
        }
      } else if (incomingValue instanceof Collection<?>) {
        if (((Collection<?>) incomingValue).size() == 1) {
          return put(nestedQueryComponentKey, ((Collection<?>) incomingValue).iterator().next());
        }
        Set<Serializable> idValue = new HashSet<>();
        StringBuilder tsValue = null;
        List<StringBuilder> extraPropValues = null;
        if (extraProps != null) {
          extraPropValues = new ArrayList<>(extraProps.size());
          for (int i = 0; i < extraProps.size(); i++) {
            extraPropValues.add(null);
          }
        }
        new ArrayList<>();
        for (Object element : (Collection<?>) incomingValue) {
          if (element instanceof IEntity) {
            List<Serializable> queryPropertyValues = extractQueryPropertyValues((IEntity) element, extraProps);
            idValue.add(queryPropertyValues.get(0));
            if (queryPropertyValues.get(1) instanceof CharSequence) {
              if (tsValue == null) {
                tsValue = new StringBuilder(queryPropertyValues.get(1).toString());
              } else {
                tsValue.append(IQueryComponent.DISJUNCT).append(queryPropertyValues.get(1).toString());
              }
            }
            if (extraProps != null) {
              for (int i = 0; i < extraProps.size(); i++) {
                StringBuilder extraPropValue = extraPropValues.get(i);
                if (queryPropertyValues.get(i + 2) instanceof CharSequence) {
                  if (extraPropValue == null) {
                    extraPropValue = new StringBuilder(queryPropertyValues.get(i + 2).toString());
                    extraPropValues.set(i, extraPropValue);
                  } else {
                    extraPropValue.append(IQueryComponent.DISJUNCT).append(queryPropertyValues.get(i + 2).toString());
                  }
                }
              }
            }
          }
        }
        if (idValue.isEmpty()) {
          actualValue.remove(IEntity.ID);
        } else {
          actualValue.put(IEntity.ID, idValue);
        }
        if (tsValue != null) {
          actualValue.put(tsProp, tsValue.toString());
        } else {
          actualValue.remove(tsProp);
        }
        if (extraProps != null) {
          for (int i = 0; i < extraProps.size(); i++) {
            if (extraPropValues.get(i) != null) {
              actualValue.put(extraProps.get(i), extraPropValues.get(i).toString());
            } else {
              actualValue.remove(extraProps.get(i));
            }
          }
        }
      } else if (incomingValue instanceof String) {
        if (extraProps != null) {
          for (String extraProp : extraProps) {
            actualValue.put(extraProp, incomingValue);
          }
        }
      }
    } else {
      actualValue.reset();
    }
    return actualValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
    for (Map.Entry<String, Object> entry : new HashSet<>(entrySet())) {
      if (entry.getValue() instanceof IQueryComponent) {
        ((IQueryComponent) entry.getValue()).reset();
      } else if (entry.getValue() instanceof EnumQueryStructure) {
        ((EnumQueryStructure) entry.getValue()).clear();
      } else {
        remove(entry.getKey());
      }
    }
  }

  private List<Serializable> extractQueryPropertyValues(IEntity entity, List<String> extraProps) {
    List<Serializable> values = new ArrayList<>();
    values.add(entity.getId());
    values.add(entity.toString());
    if (extraProps != null) {
      for (String extraProp : extraProps) {
        try {
          Serializable extraPropValue = getComponentFactory().getAccessorFactory().createPropertyAccessor(extraProp,
              entity.getComponentContract()).getValue(entity);
          values.add(extraPropValue);
        } catch (IllegalAccessException | NoSuchMethodException ex) {
          throw new NestedRuntimeException(ex, "Invalid property: " + extraProp);
        } catch (InvocationTargetException ex) {
          throw new NestedRuntimeException(ex.getTargetException(), "Invalid property: " + extraProp);
        }
      }
    }
    return values;
  }

  /**
   * {@inheritDoc}
   *
   * @param key
   *     the key
   * @return the object
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object remove(Object key) {
    int firstDotIndex = ((String) key).indexOf('.');
    if (firstDotIndex > 0) {
      Object nested = get(((String) key).substring(0, firstDotIndex));
      if (nested instanceof Map<?, ?>) {
        return ((Map<String, Object>) nested).remove(((String) key).substring(firstDotIndex + 1));
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
   *
   * @return the page
   */
  @Override
  public Integer getPage() {
    return page;
  }

  /**
   * {@inheritDoc}
   *
   * @return the display page index
   */
  @Override
  public Integer getDisplayPageIndex() {
    int pc = 0;
    if (getPageCount() != null) {
      pc = getPageCount();
    }
    if (pc == 0) {
      return null;
    }
    int p = 0;
    if (getPage() != null) {
      p = getPage();
    }
    return p + 1;
  }

  /**
   * {@inheritDoc}
   *
   * @param displayPageIndex
   *     the display page index
   */
  @Override
  public void setDisplayPageIndex(Integer displayPageIndex) {
    if (displayPageIndex != null) {
      setPage(displayPageIndex - 1);
    } else {
      setPage(null);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return the page count
   */
  @Override
  public Integer getPageCount() {
    if (getRecordCount() == null) {
      return null;
    } else if (getRecordCount() == UNKNOWN_COUNT) {
      return UNKNOWN_COUNT;
    }
    if (getPageSize() == null || getPageSize() <= 0) {
      return 1;
    }
    int remainder = getRecordCount() % getPageSize();
    int lastIncompletePage = 0;
    if (remainder > 0) {
      lastIncompletePage = 1;
    }
    return getRecordCount() / getPageSize() + lastIncompletePage;
  }

  /**
   * {@inheritDoc}
   *
   * @return the displayed page count
   */
  @Override
  public String getDisplayPageCount() {
    if (getPageCount() == null || getPageCount() == UNKNOWN_COUNT || getPageCount() == 0) {
      return "";
    }
    return getPageCount().toString();
  }

  /**
   * {@inheritDoc}
   *
   * @return the page size
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
   *
   * @return the queried components
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> List<T> getQueriedComponents() {
    return (List<T>) queriedComponents;
  }

  /**
   * {@inheritDoc}
   *
   * @return the query contract
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> Class<T> getQueryContract() {
    if (componentDescriptor instanceof IQueryComponentDescriptor) {
      return (Class<T>) ((IQueryComponentDescriptor) componentDescriptor).getQueriedComponentsDescriptor()
                                                                         .getComponentContract();
    }
    return (Class<T>) componentDescriptor.getComponentContract();
  }

  /**
   * {@inheritDoc}
   *
   * @return the record count
   */
  @Override
  public Integer getRecordCount() {
    return recordCount;
  }

  /**
   * {@inheritDoc}
   *
   * @return the displayed record count
   */
  @Override
  public String getDisplayRecordCount() {
    if (getRecordCount() == null || getRecordCount() == UNKNOWN_COUNT || getRecordCount() == 0) {
      return "";
    }
    return getRecordCount().toString();
  }

  /**
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isInlineComponent() {
    return !IEntity.class.isAssignableFrom(getQueryContract()) && !componentDescriptor.isPurelyAbstract();
  }

  /**
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isPageNavigationEnabled() {
    return getPageCount() != null && (getPageCount() > 1 || getPageCount() == UNKNOWN_COUNT);
  }

  /**
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isNextPageEnabled() {
    if (getPage() == null || getPageSize() == null || getResults() == null) {
      return false;
    }
    int currentPageSize = getResults().size();
    if (getStickyResults() != null) {
      currentPageSize -= getStickyResults().size();
    }
    return getPageCount() != null && ((getPageCount() == UNKNOWN_COUNT && currentPageSize >= getPageSize())
        // We are on a complete page
        || (getPageCount() > 0 && getPage() < getPageCount() - 1));
  }

  /**
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isPreviousPageEnabled() {
    return getPage() != null && getPage() > 0;
  }

  /**
   * {@inheritDoc}
   *
   * @param defaultOrderingProperties
   *     the default ordering properties
   */
  @Override
  public void setDefaultOrderingProperties(Map<String, ESort> defaultOrderingProperties) {
    this.defaultOrderingProperties = defaultOrderingProperties;
  }

  /**
   * Sets the sortingAttributes.
   *
   * @param orderingProperties
   *     the sortingAttributes to set.
   */
  @Override
  public void setOrderingProperties(Map<String, ESort> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }

  /**
   * {@inheritDoc}
   *
   * @param page
   *     the page
   */
  @Override
  public void setPage(Integer page) {
    Integer oldValue = getPage();
    Integer oldDisplayPageIndex = getDisplayPageIndex();
    boolean oldPreviousPageEnabled = isPreviousPageEnabled();
    boolean oldNextPageEnabled = isNextPageEnabled();

    int pc = 0;
    if (getPageCount() != null) {
      pc = getPageCount();
    }
    if (getPageCount() != UNKNOWN_COUNT && pc > 0) {
      if (page == null || page < 0) {
        this.page = 0;
      } else if (page >= pc) {
        this.page = pc - 1;
      } else {
        this.page = page;
      }
    } else {
      this.page = page;
    }

    firePropertyChange(PAGE, oldValue, getPage());
    firePropertyChange(DISPLAY_PAGE_INDEX, oldDisplayPageIndex, getDisplayPageIndex());
    firePropertyChange(IPageable.PREVIOUS_PAGE_ENABLED, Boolean.valueOf(oldPreviousPageEnabled), Boolean.valueOf(
        isPreviousPageEnabled()));
    firePropertyChange(IPageable.NEXT_PAGE_ENABLED, Boolean.valueOf(oldNextPageEnabled), Boolean.valueOf(
        isNextPageEnabled()));
  }

  /**
   * {@inheritDoc}
   *
   * @param pageSize
   *     the page size
   */
  @Override
  public void setPageSize(Integer pageSize) {
    Integer oldValue = getPageSize();
    Integer oldPageCount = getPageCount();
    boolean oldPageNavigationEnabled = isPageNavigationEnabled();
    this.pageSize = pageSize;
    firePropertyChange(PAGE_SIZE, oldValue, getPageSize());
    firePropertyChange(PAGE_COUNT, oldPageCount, getPageCount());
    firePropertyChange(PAGE_NAVIGATION_ENABLED, Boolean.valueOf(oldPageNavigationEnabled), Boolean.valueOf(
        isPageNavigationEnabled()));
  }

  /**
   * {@inheritDoc}
   *
   * @param queriedComponents
   *     the queried components
   */
  @Override
  public void setQueriedComponents(List<?> queriedComponents) {
    List<?> oldQueriedComponents = getQueriedComponents();
    boolean oldNextPageEnabled = isNextPageEnabled();
    this.queriedComponents = queriedComponents;
    if (queriedComponents != null && getPageCount() == UNKNOWN_COUNT && getPageSize() != null) {
      int currentPageSize = queriedComponents.size();
      if (getStickyResults() != null) {
        currentPageSize -= getStickyResults().size();
      }
      if (currentPageSize < getPageSize()) {
        setRecordCount(getPage() * getPageSize() + currentPageSize);
      }
    }
    firePropertyChange(QUERIED_COMPONENTS, oldQueriedComponents, getQueriedComponents());
    firePropertyChange(NEXT_PAGE_ENABLED, oldNextPageEnabled, isNextPageEnabled());
  }

  /**
   * Sets the recordCount.
   *
   * @param recordCount
   *     the recordCount to set.
   */
  @Override
  public void setRecordCount(Integer recordCount) {
    Integer oldValue = getRecordCount();
    String oldDisplayRecordCount = getDisplayRecordCount();
    Integer oldPageCount = getPageCount();
    String oldDisplayPageCount = getDisplayPageCount();
    Integer oldDisplayPageIndex = getDisplayPageIndex();
    boolean oldNextPageEnabled = isNextPageEnabled();
    boolean oldPageNavigationEnabled = isPageNavigationEnabled();
    this.recordCount = recordCount;
    firePropertyChange(RECORD_COUNT, oldValue, getRecordCount());
    firePropertyChange(DISPLAY_RECORD_COUNT, oldDisplayRecordCount, getDisplayRecordCount());
    firePropertyChange(PAGE_COUNT, oldPageCount, getPageCount());
    firePropertyChange(DISPLAY_PAGE_COUNT, oldDisplayPageCount, getDisplayPageCount());
    firePropertyChange(DISPLAY_PAGE_INDEX, oldDisplayPageIndex, getDisplayPageIndex());
    firePropertyChange(IPageable.NEXT_PAGE_ENABLED, Boolean.valueOf(oldNextPageEnabled), Boolean.valueOf(
        isNextPageEnabled()));
    firePropertyChange(IPageable.PAGE_NAVIGATION_ENABLED, Boolean.valueOf(oldPageNavigationEnabled), Boolean.valueOf(
        isPageNavigationEnabled()));
  }

  /**
   * Gets selected record count.
   *
   * @return the selected record count
   */
  @Override
  public Integer getSelectedRecordCount()
  {
    if (getRecordCount() == null || getRecordCount() == 0) {
      return null;
    }
    return selectedRecordCount;
  }

  /**
   * Sets selected record count.
   *
   * @param selectedRecordCount
   *     the selected record count
   */
  @Override
  public void setSelectedRecordCount(Integer selectedRecordCount) {
    Integer oldValue = getSelectedRecordCount();
    this.selectedRecordCount = selectedRecordCount;
    firePropertyChange(SELECTED_RECORD_COUNT, oldValue, getSelectedRecordCount());
  }

  private class InlinedComponentTracker implements PropertyChangeListener {

    private final String componentName;

    /**
     * Constructs a new {@code InnerComponentTracker} instance.
     *
     * @param componentName
     *     the name of the component to track the properties.
     */
    public InlinedComponentTracker(String componentName) {
      this.componentName = componentName;
    }

    /**
     * {@inheritDoc}
     *
     * @param evt
     *     the evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      firePropertyChange(componentName, null, evt.getSource());
      firePropertyChange(componentName + "." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
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
   *
   * @return the query component
   */
  @Override
  public QueryComponent clone() {
    QueryComponent clone = (QueryComponent) super.clone();
    return clone;
  }

  /**
   * Gets whether to enforce select distinct when querying.
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
   *     the distinctEnforced to set.
   */
  public void setDistinctEnforced(boolean distinctEnforced) {
    this.distinctEnforced = distinctEnforced;
  }

  /**
   * {@inheritDoc}
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "";
  }

  /**
   * {@inheritDoc}
   *
   * @param stickyResults
   *     the sticky results
   */
  @Override
  public void setStickyResults(List<?> stickyResults) {
    this.stickyResults = stickyResults;
  }

  /**
   * {@inheritDoc}
   *
   * @return the sticky results
   */
  @Override
  public List<?> getStickyResults() {
    return stickyResults;
  }

  /**
   * Hydrates a query component with a hierarchical map holding bare filter
   * values.
   *
   * @param state
   *     the hierarchical map holding bare filter values.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void hydrate(Map<String, Object> state) {
    if (state != null) {
      for (Map.Entry<String, Object> stateEntry : state.entrySet()) {
        Object value = get(stateEntry.getKey());
        if (value instanceof IQueryComponent && stateEntry.getValue() instanceof Map<?, ?>) {
          ((IQueryComponent) value).hydrate((Map<String, Object>) stateEntry.getValue());
        } else {
          put(stateEntry.getKey(), stateEntry.getValue());
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param <T>
   *     the type parameter
   * @param value
   *     the value
   * @param propertyDescriptor
   *     the property descriptor
   * @return the t
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T refineValue(T value, IPropertyDescriptor propertyDescriptor) {
    if (value instanceof String && !(propertyDescriptor instanceof ITextPropertyDescriptor)) {
      String disjunction = ((String) value).replaceAll("(\\r|\\n)+", DISJUNCT);
      if (disjunction.endsWith(DISJUNCT)) {
        disjunction = disjunction.substring(0, disjunction.length() - DISJUNCT.length());
      }
      return (T) disjunction;
    }
    return value;
  }

  /**
   * Is restricting.
   *
   * @return the boolean
   */
  @Override
  public boolean isRestricting() {
    if (isEmpty()) {
      return false;
    }
    IComponentDescriptor<?> qDesc = getQueryDescriptor();
    for (Map.Entry<String, Object> property : entrySet()) {
      IPropertyDescriptor propertyDescriptor = qDesc.getPropertyDescriptor(property.getKey());
      if (propertyDescriptor != null) {
        if (property.getValue() != null) {
          if (property.getValue() instanceof ComparableQueryStructure) {
            if (((ComparableQueryStructure) property.getValue()).isRestricting()) {
              return true;
            }
          } else if (property.getValue() instanceof EnumQueryStructure) {
            if (!((EnumQueryStructure) property.getValue()).isEmpty()) {
              return true;
            }
          } else if (property.getValue() instanceof IQueryComponent) {
            if (((IQueryComponent) property.getValue()).isRestricting()) {
              return true;
            }
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Gets results.
   *
   * @return the results
   */
  @Override
  public List<?> getResults() {
    return getQueriedComponents();
  }

  /**
   * Gets prefetch properties.
   *
   * @return the prefetch properties
   */
  @Override
  public List<String> getPrefetchProperties() {
    return prefetchProperties;
  }

  /**
   * Sets prefetch properties.
   *
   * @param prefetchProperties
   *     the prefetch properties
   */
  @Override
  public void setPrefetchProperties(List<String> prefetchProperties) {
    this.prefetchProperties = prefetchProperties;
  }
}
