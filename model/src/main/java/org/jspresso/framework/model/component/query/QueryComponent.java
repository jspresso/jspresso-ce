/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryComponent extends ObjectEqualityMap<String, Object> implements
    IQueryComponent {

  private final IComponentDescriptor<?> componentDescriptor;
  private final IComponentFactory       componentFactory;
  private       IComponentDescriptor<?> queryDescriptor;
  private       Map<String, ESort>      defaultOrderingProperties;
  private       Map<String, ESort>      orderingProperties;
  private       Integer                 page;
  private       Integer                 pageSize;
  private       Integer                 recordCount;
  private       Integer                 selectedRecordCount;
  private       boolean                 distinctEnforced;
  private       List<?>                 queriedComponents;
  private       List<?>                 stickyResults;
  private       ITranslationProvider    translationProvider;
  private       Locale                  locale;

  /**
   * Constructs a new {@code QueryComponent} instance.
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
      for (IPropertyDescriptor propertyDescriptor : getComponentDescriptor()
          .getPropertyDescriptors()) {
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
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void translate(ITranslationProvider aTranslationProvider, Locale aLocale) {
    for (Object value : values()) {
      this.translationProvider = aTranslationProvider;
      this.locale = aLocale;
      if (value instanceof EnumQueryStructure) {
        ((EnumQueryStructure) value)
            .setTranslationProvider(aTranslationProvider);
        ((EnumQueryStructure) value).setLocale(aLocale);
      } else if (value instanceof ComparableQueryStructure) {
        ((ComparableQueryStructure) value)
            .setTranslationProvider(aTranslationProvider);
        ((ComparableQueryStructure) value).setLocale(aLocale);
      } else if (value instanceof QueryComponent) {
        ((QueryComponent) value)
            .translate(aTranslationProvider, aLocale);
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
        && !(propertyDescriptor instanceof ComparableQueryStructureDescriptor)
        && !(propertyDescriptor instanceof EnumQueryStructureDescriptor)) {
      IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      QueryComponent referencedQueryComponent = new QueryComponent(referencedDescriptor,
            getComponentFactory());
      referencedQueryComponent.translate(translationProvider, locale);
      referencedQueryComponent
          .addPropertyChangeListener(new InlinedComponentTracker(propertyDescriptor.getName()));
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
      if (referencedDescriptor.isEntity()) {
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
              StringBuilder idValue = null;
              StringBuilder tsValue = null;
              StringBuilder acValue = null;
              for (Object element : (Collection<?>) value) {
                if (element instanceof IEntity) {
                  Serializable[] queryPropertyValues = extractQueryPropertyValues(
                      (IEntity) element, acProp);
                  if (queryPropertyValues[0] instanceof CharSequence) {
                    if (idValue == null) {
                      idValue = new StringBuilder(
                          queryPropertyValues[0].toString());
                    } else {
                      idValue.append(IQueryComponent.DISJUNCT).append(
                          queryPropertyValues[0].toString());
                    }
                  }
                  if (queryPropertyValues[1] instanceof CharSequence) {
                    if (tsValue == null) {
                      tsValue = new StringBuilder(
                          queryPropertyValues[1].toString());
                    } else {
                      tsValue.append(IQueryComponent.DISJUNCT).append(
                          queryPropertyValues[1].toString());
                    }
                  }
                  if (acProp != null
                      && queryPropertyValues[2] instanceof CharSequence) {
                    if (acValue == null) {
                      acValue = new StringBuilder(
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
    Object refinedValue = refineValue(value, propertyDescriptor);
    return super.put(key, refinedValue);
  }

  private Serializable[] extractQueryPropertyValues(IEntity entity,
      String acProp) {
    String acPropValue = null;
    if (acProp != null) {
      try {
        acPropValue = getComponentFactory().getAccessorFactory()
            .createPropertyAccessor(acProp, entity.getComponentContract())
            .getValue(entity);
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex, "Invalid property: " + acProp);
      } catch (InvocationTargetException ex) {
        throw new NestedRuntimeException(ex.getTargetException(),
            "Invalid property: " + acProp);
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
      pc = getPageCount();
    }
    if (pc == 0) {
      return 0;
    }
    int p = 0;
    if (getPage() != null) {
      p = getPage();
    }
    return p + 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDisplayPageIndex(Integer displayPageIndex) {
    if (displayPageIndex != null) {
      setPage(Integer.valueOf(displayPageIndex - 1));
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
    if (getPageSize() == null || getPageSize() <= 0) {
      return 1;
    }
    int remainder = getRecordCount() % getPageSize();
    int lastIncompletePage = 0;
    if (remainder > 0) {
      lastIncompletePage = 1;
    }
    return getRecordCount()
        / getPageSize() + lastIncompletePage;
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
  public List<?> getQueriedComponents() {
    return queriedComponents;
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
    return getPageCount() != null && getPageCount() > 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNextPageEnabled() {
    return getPageCount() != null && getPage() != null
        && getPage() < getPageCount() - 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPreviousPageEnabled() {
    return getPage() != null && getPage() > 0;
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
      pc = getPageCount();
    }
    if (pc > 0) {
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
    firePropertyChange(DISPLAY_PAGE_INDEX, oldDisplayPageIndex,
        getDisplayPageIndex());
    firePropertyChange(IPageable.PREVIOUS_PAGE_ENABLED,
        Boolean.valueOf(oldPreviousPageEnabled),
        Boolean.valueOf(isPreviousPageEnabled()));
    firePropertyChange(IPageable.NEXT_PAGE_ENABLED,
        Boolean.valueOf(oldNextPageEnabled),
        Boolean.valueOf(isNextPageEnabled()));
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
    firePropertyChange(PAGE_NAVIGATION_ENABLED,
        Boolean.valueOf(oldPageNavigationEnabled),
        Boolean.valueOf(isPageNavigationEnabled()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setQueriedComponents(List<?> queriedComponents) {
    List<?> oldQueriedComponents = getQueriedComponents();
    this.queriedComponents = queriedComponents;
    firePropertyChange(QUERIED_COMPONENTS, oldQueriedComponents,
        getQueriedComponents());
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
    firePropertyChange(IPageable.NEXT_PAGE_ENABLED,
        Boolean.valueOf(oldNextPageEnabled),
        Boolean.valueOf(isNextPageEnabled()));
    firePropertyChange(IPageable.PAGE_NAVIGATION_ENABLED,
        Boolean.valueOf(oldPageNavigationEnabled),
        Boolean.valueOf(isPageNavigationEnabled()));
  }

  /**
   * Gets selected record count.
   *
   * @return the selected record count
   */
  @Override
  public Integer getSelectedRecordCount() {
    return selectedRecordCount;
  }

  /**
   * Sets selected record count.
   *
   * @param selectedRecordCount the selected record count
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
    this.stickyResults = stickyResults;
  }

  /**
   * {@inheritDoc}
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
   *          the hierarchical map holding bare filter values.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void hydrate(Map<String, Object> state) {
    if (state != null) {
      for (Map.Entry<String, Object> stateEntry : state.entrySet()) {
        Object value = get(stateEntry.getKey());
        if (value instanceof IQueryComponent
            && stateEntry.getValue() instanceof Map<?, ?>) {
          ((IQueryComponent) value).hydrate((Map<String, Object>) stateEntry
              .getValue());
        } else {
          put(stateEntry.getKey(), stateEntry.getValue());
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T refineValue(T value, IPropertyDescriptor propertyDescriptor) {
    if (value instanceof String
        && !(propertyDescriptor instanceof ITextPropertyDescriptor)) {
      String disjunction = ((String) value).replaceAll("(\\r|\\n)+", DISJUNCT);
      if (disjunction.endsWith(DISJUNCT)) {
        disjunction = disjunction.substring(0,
            disjunction.length() - DISJUNCT.length());
      }
      return (T) disjunction;
    }
    return value;
  }

  @Override
  public boolean isRestricting() {
    if (isEmpty()) {
      return false;
    }
    IComponentDescriptor<?> qDesc = getQueryDescriptor();
    for (Map.Entry<String, Object> property : entrySet()) {
      IPropertyDescriptor propertyDescriptor = qDesc
          .getPropertyDescriptor(property.getKey());
      if (propertyDescriptor != null) {
        if (property.getValue() != null) {
          if (property.getValue() instanceof ComparableQueryStructure) {
            if (((ComparableQueryStructure) property.getValue())
                .isRestricting()) {
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
}
