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
package org.jspresso.framework.model.descriptor.basic;

import java.util.Map;

import gnu.trove.map.hash.THashMap;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;

/**
 * Basic implementation. Creates basic query component descriptors.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class BasicQueryComponentDescriptorFactory implements IQueryComponentDescriptorFactory {

  private final Map<IComponentDescriptorProvider<? extends IComponent>, IComponentDescriptor<IQueryComponent>> registry;
  private final Map<Class<? extends IComponent>, IComponentDescriptor<? extends IComponent>> refRegistry;

  /**
   * Instantiates a new Basic query component descriptor factory.
   */
  public BasicQueryComponentDescriptorFactory() {
    registry = new THashMap<>();
    refRegistry = new THashMap<>();
  }

  /**
   * Creates basic query component descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<IQueryComponent> createQueryComponentDescriptor(
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider) {
    IComponentDescriptorProvider<? extends IComponent> realComponentDescriptorProvider;
    if (componentDescriptorProvider.getComponentDescriptor() instanceof RefQueryComponentDescriptor<?>) {
      realComponentDescriptorProvider = ((RefQueryComponentDescriptor<?>) componentDescriptorProvider
          .getComponentDescriptor()).getQueriedComponentsDescriptor();
    } else {
      realComponentDescriptorProvider = componentDescriptorProvider;
    }
    return createOrGetQueryComponentDescriptor(realComponentDescriptorProvider);
  }

  /**
   * Create a basic query component reference descriptor.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IReferencePropertyDescriptor<IQueryComponent> createQueryComponentReferenceDescriptor(
      String referencePropertyName, IComponentDescriptorProvider<IComponent> componentDescriptorProvider) {
    BasicReferencePropertyDescriptor<IQueryComponent> referencePropertyDescriptor =
        new BasicReferencePropertyDescriptor<>();
    referencePropertyDescriptor.setName(referencePropertyName);
    referencePropertyDescriptor.setMandatory(false); // since it cannot be auto-created by the framework
    referencePropertyDescriptor.setReferencedDescriptor(createQueryComponentDescriptor(componentDescriptorProvider));
    return referencePropertyDescriptor;
  }

  private IComponentDescriptor<IQueryComponent> createOrGetQueryComponentDescriptor(
      IComponentDescriptorProvider<? extends IComponent> componentDescriptorProvider) {
    IComponentDescriptor<IQueryComponent> queryComponentDescriptor;
    synchronized (registry) {
      queryComponentDescriptor = registry.get(componentDescriptorProvider);
      if (queryComponentDescriptor == null) {
        queryComponentDescriptor = instanciateQueryComponentDescriptor(componentDescriptorProvider, refRegistry);
        registry.put(componentDescriptorProvider, queryComponentDescriptor);
      }
    }
    return queryComponentDescriptor;
  }

  /**
   * Instanciate query component descriptor.
   *
   * @param componentDescriptorProvider
   *    the provider for delegate entity descriptor.
   * @param registry
   *    the registry.
   * @return the query component descriptor
   */
  protected IComponentDescriptor<IQueryComponent> instanciateQueryComponentDescriptor(
      IComponentDescriptorProvider<? extends IComponent> componentDescriptorProvider,
      Map<Class<? extends IComponent>, IComponentDescriptor<? extends IComponent>> registry) {

    IComponentDescriptor<IQueryComponent> queryComponentDescriptor
      = new BasicQueryComponentDescriptor<IQueryComponent>(componentDescriptorProvider, registry);

    return queryComponentDescriptor;
  }
}
