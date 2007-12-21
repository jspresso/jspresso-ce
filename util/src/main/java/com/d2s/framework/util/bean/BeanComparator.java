/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;

import com.d2s.framework.util.accessor.IAccessor;

/**
 * A bean comparator which compare beans based on a list of properties using
 * their natural order.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanComparator implements Comparator<Object> {

  private static final Comparator<Object> NATURAL_COMPARATOR = new NaturalComparator();
  private List<IAccessor>                 orderingAccessors;

  /**
   * {@inheritDoc}
   */
  public int compare(Object o1, Object o2) {
    if (o1 == o2) {
      return 0;
    }
    if (o1 == null) {
      return -1;
    }
    if (o2 == null) {
      return 1;
    }
    for (IAccessor orderingAccessor : orderingAccessors) {
      Object o1Val = null;
      Object o2Val = null;
      try {
        o1Val = orderingAccessor.getValue(o1);
        o2Val = orderingAccessor.getValue(o2);
      } catch (IllegalAccessException ex) {
        throw new MissingPropertyException(ex.getMessage());
      } catch (InvocationTargetException ex) {
        throw new MissingPropertyException(ex.getMessage());
      } catch (NoSuchMethodException ex) {
        throw new MissingPropertyException(ex.getMessage());
      }
      int result = NATURAL_COMPARATOR.compare(o1Val, o2Val);
      if (result != 0) {
        return result;
      }
    }
    return 0;
  }

  /**
   * Sets the orderingAccessors.
   * 
   * @param orderingAccessors
   *            the orderingAccessors to set.
   */
  public void setOrderingAccessors(List<IAccessor> orderingAccessors) {
    this.orderingAccessors = orderingAccessors;
  }

  private static final class NaturalComparator implements Comparator<Object> {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
      if (o1 == o2) {
        return 0;
      }
      if (o1 == null) {
        return -1;
      }
      if (o2 == null) {
        return 1;
      }
      if (o1 instanceof Comparable<?>) {
        return ((Comparable<Object>) o1).compareTo(o2);
      }
      return o1.toString().compareTo(o2.toString());
    }

  }
}
