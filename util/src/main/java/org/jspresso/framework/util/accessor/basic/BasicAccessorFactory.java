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
package org.jspresso.framework.util.accessor.basic;

import java.util.Map;

import org.jspresso.framework.util.accessor.AbstractAccessorFactory;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * This is the default implementation of the accessor factory.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public class BasicAccessorFactory extends AbstractAccessorFactory {

  private IAccessorFactory beanAccessorFactory;
  private IAccessorFactory mapAccessorFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class<?> beanClass, Class<?> elementClass) {
    return getAccessorDelegate(beanClass, property)
        .createCollectionPropertyAccessor(property, beanClass, elementClass);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAccessor createPropertyAccessor(String property, Class<?> beanClass) {
    return getAccessorDelegate(beanClass, property).createPropertyAccessor(
        property, beanClass);
  }

  /**
   * Sets the beanAccessorFactory.
   *
   * @param beanAccessorFactory
   *          the beanAccessorFactory to set.
   */
  public void setBeanAccessorFactory(IAccessorFactory beanAccessorFactory) {
    this.beanAccessorFactory = beanAccessorFactory;
  }

  /**
   * Sets the mapAccessorFactory.
   *
   * @param mapAccessorFactory
   *          the mapAccessorFactory to set.
   */
  public void setMapAccessorFactory(IAccessorFactory mapAccessorFactory) {
    this.mapAccessorFactory = mapAccessorFactory;
  }

  private IAccessorFactory getAccessorDelegate(Class<?> beanClass,
      @SuppressWarnings("unused") String property) {
    IAccessorFactory delegate;
    if (beanClass == null || Map.class.isAssignableFrom(beanClass)) {
      delegate = mapAccessorFactory;
    } else {
      delegate = beanAccessorFactory;
    }
    return delegate;
  }

}
