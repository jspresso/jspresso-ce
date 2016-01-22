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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This is a simple utility bean to get a reference on the bean factory which
 * loaded it.
 *
 * @author Vincent Vandenschrick
 */
public class ThisApplicationContextFactoryBean implements
    ApplicationContextAware, FactoryBean<ApplicationContext> {

  private ApplicationContext applicationContext;

  /**
   * Returns the application context.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ApplicationContext getObject() throws Exception {
    return applicationContext;
  }

  /**
   * Returns the application context class.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Class<ApplicationContext> getObjectType() {
    return ApplicationContext.class;
  }

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
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
