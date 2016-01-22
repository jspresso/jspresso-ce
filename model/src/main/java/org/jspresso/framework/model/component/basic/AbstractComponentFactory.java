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
package org.jspresso.framework.model.component.basic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.spi.PersistentCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.BeanComparator;
import org.jspresso.framework.util.bean.MissingPropertyException;
import org.jspresso.framework.util.collection.ESort;

/**
 * Base class for component factories.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentFactory implements IComponentFactory {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentFactory.class);

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
  @SuppressWarnings("unchecked")
  @Override
  public void sortCollectionProperty(IComponent component, String propertyName) {
    ICollectionPropertyDescriptor<?> propertyDescriptor = (ICollectionPropertyDescriptor<?>) getComponentDescriptor(
        component.getComponentContract()).getPropertyDescriptor(propertyName);
    if(propertyDescriptor != null) {
      Map<String, ESort> orderingProperties = propertyDescriptor.getOrderingProperties();
      if (orderingProperties != null && !orderingProperties.isEmpty()) {
        Collection<Object> propertyValue = (Collection<Object>) component.straightGetProperty(propertyName);
        boolean wasClean = false;
        if (propertyValue instanceof PersistentCollection && !((PersistentCollection) propertyValue).isDirty()) {
          wasClean = true;
        }
        if (propertyValue != null && !propertyValue.isEmpty() && !List.class.isAssignableFrom(
            propertyDescriptor.getCollectionDescriptor().getCollectionInterface())) {
          List<IAccessor> orderingAccessors = new ArrayList<>();
          List<ESort> orderingDirections = new ArrayList<>();
          Class<?> collectionElementContract = propertyDescriptor.getCollectionDescriptor().getElementDescriptor()
                                                                 .getComponentContract();
          for (Map.Entry<String, ESort> orderingProperty : orderingProperties.entrySet()) {
            orderingAccessors.add(accessorFactory.createPropertyAccessor(orderingProperty.getKey(),
                collectionElementContract));
            orderingDirections.add(orderingProperty.getValue());
          }

          List<Object> collectionOrigin = new ArrayList<>(propertyValue);
          List<ComparableProperties> listToSort = new ArrayList<>();
          for (Object sourceObject : propertyValue) {
            listToSort.add(new ComparableProperties(sourceObject,
                orderingAccessors));
          }
          Collections.sort(listToSort, new ComparablePropertiesComparator(
              orderingDirections));
          List<Object> collectionCopy = new ArrayList<>();
          for (ComparableProperties comparableProperties : listToSort) {
            collectionCopy.add(comparableProperties.getSourceObject());
          }
          if (!collectionCopy.equals(collectionOrigin)) {
            propertyValue.clear();
            propertyValue.addAll(collectionCopy);
            if (wasClean) {
              ((PersistentCollection) propertyValue).clearDirty();
            }
          }
        }
      }
    }
  }

  /**
   * Apply initialization mapping.
   *
   * @param component the component
   * @param componentDescriptor the component descriptor
   * @param masterComponent the master component
   * @param initializationMapping the initialization mapping
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public void applyInitializationMapping(Object component, IComponentDescriptor<?> componentDescriptor,
                                          Object masterComponent, Map<String, Object> initializationMapping) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("initializationMapping : " + initializationMapping);
    }
    for (Map.Entry<String, Object> initializedAttribute : initializationMapping
        .entrySet()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("initializing property : " + initializedAttribute.getKey()
            + " from " + initializedAttribute.getKey());
      }
      IAccessor accessor = getAccessorFactory().createPropertyAccessor(initializedAttribute.getKey(),
          componentDescriptor.getComponentContract());
      try {
        Object initValue;
        initValue = extractInitValue(masterComponent, initializedAttribute.getValue());
        if (initValue != null) {
          if (initValue instanceof String
              && (((String) initValue).endsWith("null") || ((String) initValue)
              .endsWith(IQueryComponent.NULL_VAL))) {
            if (((String) initValue).startsWith(IQueryComponent.NOT_VAL)) {
              initValue = IQueryComponent.NULL_VAL + IQueryComponent.NULL_VAL;
              if (LOG.isDebugEnabled()) {
                LOG.debug("Init value set to not null");
              }
            } else {
              initValue = IQueryComponent.NULL_VAL;
              if (LOG.isDebugEnabled()) {
                LOG.debug("Init value set to null");
              }
            }
          } else {
            IPropertyDescriptor initializedPropertyDescriptor = componentDescriptor
                .getPropertyDescriptor(initializedAttribute.getKey());

            if (initializedPropertyDescriptor != null) {
              Class<?> expectedType = initializedPropertyDescriptor
                  .getModelType();
              Class<?> initValueType = initValue.getClass();
              if (!IQueryComponent.class.isAssignableFrom(initValueType)
                  && !expectedType.isAssignableFrom(initValueType)) {
                if (Boolean.TYPE.equals(expectedType)) {
                  expectedType = Boolean.class;
                }
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Init value needs to be refined to match expected type : "
                      + expectedType.getName());
                }
                try {
                  initValue = expectedType.getConstructor(new Class<?>[] {
                      String.class
                  }).newInstance(initValue.toString());
                  // Whenever an exception occurs, just try to set it
                  // normally
                  // though.
                  if (LOG.isDebugEnabled()) {
                    LOG.debug("Refined init value : " + initValue);
                  }
                } catch (IllegalArgumentException | InstantiationException | SecurityException ex) {
                  // throw new NestedRuntimeException(ex,
                  // "Invalid initialization mapping for property "
                  // + initializedAttribute.getKey());
                }
              }
            }
          }
          // } else {
          // initValue = IQueryComponent.NULL_VAL;
          // if (LOG.isDebugEnabled()) {
          // LOG.debug("Init value set to null");
          // }
        }
        accessor.setValue(component, initValue);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Init value assigned.");
        }
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      }
    }
  }

  /**
   * Extract init value.
   *
   * @param masterComponent the master component
   * @param initializedAttributeValue the initialized attribute value
   * @return the object
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   * @throws NoSuchMethodException the no such method exception
   */
  protected Object extractInitValue(Object masterComponent, Object initializedAttributeValue)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Object initValue;
    if (masterComponent != null
        && initializedAttributeValue instanceof String) {
      Class<?> masterComponentContract;
      if (masterComponent instanceof IComponent) {
        masterComponentContract = ((IComponent) masterComponent)
            .getComponentContract();
      } else if (masterComponent instanceof IQueryComponent) {
        masterComponentContract = ((IQueryComponent) masterComponent)
            .getQueryContract();
      } else {
        masterComponentContract = masterComponent.getClass();
      }
      try {
        IAccessor masterAccessor = getAccessorFactory()
            .createPropertyAccessor((String) initializedAttributeValue, masterComponentContract);
        initValue = masterAccessor.getValue(masterComponent);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Master component contract : "
              + masterComponentContract.getName());
          LOG.debug("Init value computed from master component : "
              + initValue);
        }
      } catch (MissingPropertyException ex) {
        // the value in the initialization mapping is not a property.
        // Handle it as a constant value.
        initValue = initializedAttributeValue;
        if (LOG.isDebugEnabled()) {
          LOG.debug("Init value computed from static value : "
              + initValue);
        }
      }
    } else {
      initValue = initializedAttributeValue;
      if (LOG.isDebugEnabled()) {
        LOG.debug("Init value computed from static value : " + initValue);
      }
    }
    return initValue;
  }

  private static class ComparableProperties {

    private Object[] valuesToCompare;
    private final Object   sourceObject;

    /**
     * Constructs a new {@code ComparableProperties} instance.
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
          } catch (IllegalAccessException | NoSuchMethodException ex) {
            throw new MissingPropertyException(ex.getMessage());
          } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof RuntimeException) {
              throw (RuntimeException) ex.getCause();
            }
            throw new RuntimeException(ex.getCause());
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

    private final List<ESort> orderingDirections;

    /**
     * Constructs a new {@code ComparablePropertiesComparator} instance.
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
