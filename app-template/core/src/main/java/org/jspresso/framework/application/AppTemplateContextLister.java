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
package org.jspresso.framework.application;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A simple class used to dump the application template Spring elements.
 *
 * @author Vincent Vandenschrick
 */
public final class AppTemplateContextLister {

  private static final Logger LOG = LoggerFactory.getLogger(AppTemplateContextLister.class);

  private AppTemplateContextLister() {
    // UtilityClassConstructor
  }

  /**
   * Dumps the application elements along with their actual types.
   *
   * @param args main arguments
   */
  public static void main(String... args) {
    ListableBeanFactory applicationContext = new ClassPathXmlApplicationContext(
        "/org/jspresso/framework/application/commons.xml",
        "/org/jspresso/framework/application/backend/persistence/hibernate/commons-hibernate.xml",
        "/org/jspresso/framework/application/frontend/commons-frontend.xml",
        "/org/jspresso/framework/application/mock-config.xml");
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    Arrays.sort(beanNames, String.CASE_INSENSITIVE_ORDER);
    for (String beanName : beanNames) {
      Class<?> beanType = applicationContext.getType(beanName);

      outputLine(beanName, beanType);

      String[] aliases = applicationContext.getAliases(beanName);
      if (aliases != null) {
        for (String alias : aliases) {
          outputLine(alias, beanType);
        }
      }
    }
  }

  private static void outputLine(String beanName, Class<?> beanType) {
    StringBuilder line = new StringBuilder(beanName);
    if (beanType != null) {
      line.append(":").append(beanType.getName());
    }
    LOG.info(line.toString());
  }
}
