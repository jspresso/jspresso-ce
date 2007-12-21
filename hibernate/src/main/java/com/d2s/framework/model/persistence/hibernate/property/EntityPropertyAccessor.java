/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate.property;

import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.util.bean.PropertyHelper;

/**
 * This class the contract defined by hibernate to enable entity properties to
 * be accessed. Since entities are generically implemented by a proxy, the
 * strategy used by hibernate can't be "field" based. We could have used the
 * "property strategy, but each and every time hibernate would have mutated the
 * properties to refresh them based on the backing store, the business rules and
 * integrity rules would have been fired. It is none-sense in terms of
 * performance and even in terms of functionality since we know that the values
 * held in the backing store are considered to have been checked formerly.
 * Considering the previous points, this implementation uses a intermediate
 * strategy which update the property values without firing anything but a
 * <code>PropertyChangeEvent</code> in case the actual property value changed.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityPropertyAccessor implements PropertyAccessor {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Getter getGetter(Class theClass, String propertyName) {
    return new EntityPropertyGetter(theClass, propertyName);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({"unused", "unchecked" })
  public Setter getSetter(Class theClass, String propertyName) {
    return new EntityPropertySetter(propertyName);
  }

  /**
   * Implements the getter strategy on the entity proxy implementation to be
   * used by hibernate.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  private static final class EntityPropertyGetter implements Getter {

    private static final long serialVersionUID = -7896937881971754040L;
    private Class<?>          propertyClass;
    private String            propertyName;

    /**
     * Constructs a new <code>EntityPropertyGetter</code> instance.
     * 
     * @param theClass
     *            The class of the property.
     * @param propertyName
     *            the name of the property to access.
     */
    public EntityPropertyGetter(Class<?> theClass, String propertyName) {
      this.propertyName = propertyName;
      this.propertyClass = PropertyHelper.getPropertyType(theClass,
          propertyName);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(Object target) {
      return ((IComponent) target).straightGetProperty(propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unused", "unchecked" })
    public Object getForInsert(Object target, Map mergeMap,
        SessionImplementor session) {
      return get(target);
    }

    /**
     * Actually returns null.
     * <p>
     * {@inheritDoc}
     */
    public Method getMethod() {
      return null;
    }

    /**
     * Actually returns null.
     * <p>
     * {@inheritDoc}
     */
    public String getMethodName() {
      return null;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getReturnType() {
      return propertyClass;
    }

  }

  /**
   * Implements the setter strategy on the entity proxy implementation to be
   * used by hibernate.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  private static final class EntityPropertySetter implements Setter {

    private static final long serialVersionUID = 1836686220358025728L;
    private String            propertyName;

    /**
     * Constructs a new <code>EntityPropertySetter</code> instance.
     * 
     * @param propertyName
     *            the name of the property to access.
     */
    public EntityPropertySetter(String propertyName) {
      this.propertyName = propertyName;
    }

    /**
     * Actually returns null.
     * <p>
     * {@inheritDoc}
     */
    public Method getMethod() {
      return null;
    }

    /**
     * Actually returns null.
     * <p>
     * {@inheritDoc}
     */
    public String getMethodName() {
      return null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    public void set(Object target, Object value,
        SessionFactoryImplementor factory) {
      ((IComponent) target).straightSetProperty(propertyName, value);
    }

  }
}
