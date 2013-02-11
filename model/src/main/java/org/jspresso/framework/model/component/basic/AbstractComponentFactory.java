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
package org.jspresso.framework.model.component.basic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.PersistentCollection;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.BeanComparator;
import org.jspresso.framework.util.bean.MissingPropertyException;
import org.jspresso.framework.util.collection.ESort;

/**
 * Base class for component factories.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentFactory implements IComponentFactory {

  private IAccessorFactory accessorFactory;

  /**
   * Gets the accessorFactory.
   * 
   * @return the accessorFactory.
   */
  @Override
  public IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  /**
   * Sets the accessorFactory used by this entity factory.
   * 
   * @param accessorFactory
   *          the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sortCollectionProperty(IComponent component, String propertyName) {
    ICollectionPropertyDescriptor<?> propertyDescriptor = (ICollectionPropertyDescriptor<?>) getComponentDescriptor(
        component.getComponentContract()).getPropertyDescriptor(propertyName);
    Map<String, ESort> orderingProperties = propertyDescriptor
        .getOrderingProperties();
    if (orderingProperties != null && !orderingProperties.isEmpty()) {
    Collection<Object> propertyValue = (Collection<Object>) component
        .straightGetProperty(propertyName);
    boolean wasClean = false;
    if (propertyValue instanceof PersistentCollection
        && !((PersistentCollection) propertyValue).isDirty()) {
      wasClean = true;
    }
    if (propertyValue != null
        && !propertyValue.isEmpty()
        && !List.class.isAssignableFrom(propertyDescriptor
            .getCollectionDescriptor().getCollectionInterface())) {
        List<IAccessor> orderingAccessors = new ArrayList<IAccessor>();
        List<ESort> orderingDirections = new ArrayList<ESort>();
        Class<?> collectionElementContract = propertyDescriptor
            .getCollectionDescriptor().getElementDescriptor()
            .getComponentContract();
        for (Map.Entry<String, ESort> orderingProperty : orderingProperties
            .entrySet()) {
          orderingAccessors.add(accessorFactory.createPropertyAccessor(
              orderingProperty.getKey(), collectionElementContract));
          orderingDirections.add(orderingProperty.getValue());
        }

        List<Object> collectionOrigin = new ArrayList<Object>(propertyValue);
        List<ComparableProperties> listToSort = new ArrayList<ComparableProperties>();
        for (Object sourceObject : propertyValue) {
          listToSort.add(new ComparableProperties(sourceObject,
              orderingAccessors));
        }
        Collections.sort(listToSort, new ComparablePropertiesComparator(
            orderingDirections));
        List<Object> collectionCopy = new ArrayList<Object>();
        for (ComparableProperties comparableProperties : listToSort) {
          collectionCopy.add(comparableProperties.getSourceObject());
        }
        if (!collectionCopy.equals(collectionOrigin)) {
          Collection<Object> collectionProperty = propertyValue;
          collectionProperty.clear();
          collectionProperty.addAll(collectionCopy);
          if (wasClean) {
            ((PersistentCollection) collectionProperty).clearDirty();
          }
        }
      }
    }
  }

  private static class ComparableProperties {

    private Object[] valuesToCompare;
    private Object   sourceObject;

    /**
     * Constructs a new <code>ComparableProperties</code> instance.
     * 
     * @param sourceObject
     *          the source object to compare properties.
     * @param orderingAccessors
     *          the accessors to extract properties to compare.
     */
    public ComparableProperties(Object sourceObject,
        List<IAccessor> orderingAccessors) {
      this.sourceObject = sourceObject;
      if (sourceObject != null) {
        valuesToCompare = new Object[orderingAccessors.size()];
        for (int i = 0; i < valuesToCompare.length; i++) {
          try {
            valuesToCompare[i] = orderingAccessors.get(i)
                .getValue(sourceObject);
          } catch (IllegalAccessException ex) {
            throw new MissingPropertyException(ex.getMessage());
          } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof RuntimeException) {
              throw (RuntimeException) ex.getCause();
            }
            throw new RuntimeException(ex.getCause());
          } catch (NoSuchMethodException ex) {
            throw new MissingPropertyException(ex.getMessage());
          }
        }
      }
    }

    /**
     * Gets the sourceObject.
     * 
     * @return the sourceObject.
     */
    public Object getSourceObject() {
      return sourceObject;
    }

    /**
     * Gets the valuesToCompare.
     * 
     * @return the valuesToCompare.
     */
    public Object[] getValuesToCompare() {
      return valuesToCompare;
    }
  }

  private static class ComparablePropertiesComparator implements
      Comparator<ComparableProperties> {

    private List<ESort> orderingDirections;

    /**
     * Constructs a new <code>ComparablePropertiesComparator</code> instance.
     * 
     * @param orderingDirections
     *          the ordering directions.
     */
    public ComparablePropertiesComparator(List<ESort> orderingDirections) {
      this.orderingDirections = orderingDirections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(ComparableProperties o1, ComparableProperties o2) {
      if (o1 == o2) {
        return 0;
      }
      if (o1 == null) {
        return -1;
      }
      if (o2 == null) {
        return 1;
      }
      for (int i = 0; i < o1.getValuesToCompare().length; i++) {
        ESort direction = orderingDirections.get(i);
        Object o1Val = o1.getValuesToCompare()[i];
        Object o2Val = o2.getValuesToCompare()[i];
        int result = BeanComparator.NATURAL_COMPARATOR.compare(o1Val, o2Val);
        if (result != 0) {
          if (direction == ESort.DESCENDING) {
            result *= (-1);
          }
          return result;
        }
      }
      return 0;
    }
  }
}
