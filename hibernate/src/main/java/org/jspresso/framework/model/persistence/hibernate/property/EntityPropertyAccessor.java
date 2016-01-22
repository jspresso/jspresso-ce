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
package org.jspresso.framework.model.persistence.hibernate.property;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.util.bean.PropertyHelper;

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
 * {@code PropertyChangeEvent} in case the actual property value changed.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("rawtypes")
public class EntityPropertyAccessor implements PropertyAccessor {

  /**
   * {@inheritDoc}
   */
  @Override
  public Getter getGetter(Class theClass, String propertyName) {
    return new EntityPropertyGetter(theClass, propertyName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Setter getSetter(Class theClass, String propertyName) {
    return new EntityPropertySetter(theClass, propertyName);
  }

  /**
   * Implements the getter strategy on the entity proxy implementation to be
   * used by hibernate.
   *
     * @author Vincent Vandenschrick
   */
  private static final class EntityPropertyGetter implements Getter {

    private static final long serialVersionUID = -7896937881971754040L;
    private final Class<?>          propertyClass;
    private final String            propertyName;
    private final Method            getter;

    /**
     * Constructs a new {@code EntityPropertyGetter} instance.
     *
     * @param theClass
     *          The class of the entity.
     * @param propertyName
     *          the name of the property to access.
     */
    public EntityPropertyGetter(Class<?> theClass, String propertyName) {
      this.propertyName = PropertyHelper
          .fromJavaBeanPropertyName(propertyName);
      this.propertyClass = PropertyHelper.getPropertyType(theClass,
          propertyName);
      this.getter = PropertyHelper
          .getPropertyDescriptor(theClass, propertyName).getReadMethod();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(Object target) {
      return ((IComponent) target).straightGetProperty(propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getForInsert(Object target, Map mergeMap,
        SessionImplementor session) {
      return get(target);
    }

    /**
     * Actually returns the getter method.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Method getMethod() {
      return getter;
    }

    /**
     * Actually returns getter method name.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getMethodName() {
      if (getter != null) {
        return getter.getName();
      }
      return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getReturnType() {
      return propertyClass;
    }

    /**
     * Actually returns the getter method. {@inheritDoc}
     */
    @Override
    public Member getMember() {
      return getter;
    }

  }

  /**
   * Implements the setter strategy on the entity proxy implementation to be
   * used by hibernate.
   *
     * @author Vincent Vandenschrick
   */
  private static final class EntityPropertySetter implements Setter {

    private static final long serialVersionUID = 1836686220358025728L;
    private final String            propertyName;
    private final Method            setter;

    /**
     * Constructs a new {@code EntityPropertySetter} instance.
     *
     * @param theClass
     *          The class of the entity.
     * @param propertyName
     *          the name of the property to access.
     */
    public EntityPropertySetter(Class<?> theClass, String propertyName) {
      this.propertyName = PropertyHelper
          .fromJavaBeanPropertyName(propertyName);
      this.setter = PropertyHelper
          .getPropertyDescriptor(theClass, propertyName).getWriteMethod();
    }

    /**
     * Actually returns the setter.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Method getMethod() {
      return setter;
    }

    /**
     * Actually returns the setter name.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getMethodName() {
      if (setter != null) {
        return setter.getName();
      }
      return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(Object target, Object value,
        SessionFactoryImplementor factory) {
      ((IComponent) target).straightSetProperty(propertyName, value);
    }

  }
}
