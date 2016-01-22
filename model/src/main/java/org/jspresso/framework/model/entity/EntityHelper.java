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
package org.jspresso.framework.model.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;

/**
 * Helper class for entities utility methods.
 *
 * @author Vincent Vandenschrick
 */
public final class EntityHelper {

  /**
   * Constructs a new {@code EntityHelper} instance.
   */
  private EntityHelper() {
    // Helper constructor.
  }

  /**
   * Determines if a reference property descriptor references an inline
   * component.
   *
   * @param propertyDescriptor
   *          the reference property descriptor to test.
   * @return {@code true} if the reference property descriptor references
   *         an inline component.
   */
  public static boolean isInlineComponentReference(
      IComponentDescriptorProvider<?> propertyDescriptor) {
    return !propertyDescriptor.getComponentDescriptor().isEntity()
        && !propertyDescriptor.getComponentDescriptor().isPurelyAbstract();
  }

  /**
   * Gets entity sub contracts.
   *
   * @param entityContract
   *     the entity contract
   * @return the entity sub contracts
   */
  @SuppressWarnings("unchecked")
  public static Collection<Class<IEntity>> getEntitySubContracts(Class<IEntity> entityContract) {
    Collection<Class<IEntity>> entitySubContracts = new HashSet<>();
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false) {
      @Override
      protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        // Allow to return superclasses
        return beanDefinition.getMetadata().isIndependent();
      }
    };
    provider.addIncludeFilter(new AssignableTypeFilter(entityContract));
    Set<BeanDefinition> components = provider.findCandidateComponents(entityContract.getPackage().getName().replace('.',
        '/'));
    for (BeanDefinition component : components) {
      try {
        Class<IEntity> entitySubContract = (Class<IEntity>) Class.forName(component.getBeanClassName());
        if (entitySubContract != entityContract) {
          entitySubContracts.add(entitySubContract);
        }
      } catch (ClassNotFoundException e) {
        // Ignore
      }
    }
    return entitySubContracts;
  }
}
