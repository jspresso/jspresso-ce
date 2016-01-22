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
package org.jspresso.framework.application.startup.development;

import javax.servlet.ServletContextEvent;

import org.jspresso.framework.application.startup.AbstractBeanFactoryAwareContextListener;
import org.springframework.beans.factory.BeanFactory;

/**
 * A simple listener to hook in webapp startup and persist sample data.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTestDataContextListener extends
    AbstractBeanFactoryAwareContextListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void contextInitialized(BeanFactory beanFactory,
      ServletContextEvent event) {
    if (beanFactory != null) {
      persistTestData(beanFactory);
    }
  }

  /**
   * Triggers the test data creation.
   *
   * @param beanFactory
   *          the bean factory to use.
   */
  public abstract void persistTestData(BeanFactory beanFactory);

}
