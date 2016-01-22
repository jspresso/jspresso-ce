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
package org.jspresso.framework.util.spring;

import org.springframework.beans.factory.FactoryBean;

/**
 * This is a simple utility bean to get a named null bean.
 *
 * @author Vincent Vandenschrick
 */
public class NullFactoryBean implements FactoryBean<Object> {

  /**
   * Returns true.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleton() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject() throws Exception {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<Object> getObjectType() {
    return Object.class;
  }
}
