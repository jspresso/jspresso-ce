/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.collection.IPageable;

/**
 * An implementation used for query components.
 * 
 * @internal
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete type of components.
 */
public class BasicQueryComponentDescriptor<E> extends
    RefQueryComponentDescriptor<IQueryComponent> {

  /**
   * Constructs a new {@code BasicQueryComponentDescriptor} instance.
   *
   * @param componentDescriptorProvider
   *          the provider for delegate entity descriptor.
   */
  public BasicQueryComponentDescriptor(
      IComponentDescriptorProvider<? extends IComponent> componentDescriptorProvider) {
    this(componentDescriptorProvider,
        new THashMap<Class<? extends IComponent>, IComponentDescriptor<? extends IComponent>>());
  }

  /**
   * Constructs a new {@code BasicQueryComponentDescriptor} instance.
   *
   * @param componentDescriptorProvider           the provider for delegate entity descriptor.
   * @param registry the registry
   */
  public BasicQueryComponentDescriptor(
      IComponentDescriptorProvider<? extends IComponent> componentDescriptorProvider,
      Map<Class<? extends IComponent>, IComponentDescriptor<? extends IComponent>> registry) {
    super(
        componentDescriptorProvider,
        IQueryComponent.class, registry);
    finishConfiguration();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Collection<IPropertyDescriptor> getExtraPropertyDescriptors() {
    Collection<IPropertyDescriptor> extraPropertyDescriptors = new ArrayList<>();
    BasicListDescriptor<IComponent> queriedEntitiesCollectionDescriptor = new BasicListDescriptor<>();
    queriedEntitiesCollectionDescriptor
        .setElementDescriptor(getQueriedComponentsDescriptor());
    queriedEntitiesCollectionDescriptor
        .setName(IQueryComponent.QUERIED_COMPONENTS);
    queriedEntitiesCollectionDescriptor
        .setDescription("queriedEntities.description");
    BasicCollectionPropertyDescriptor<IComponent> qCPDescriptor = new BasicCollectionPropertyDescriptor<>();
    qCPDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);
    qCPDescriptor.setReferencedDescriptor(queriedEntitiesCollectionDescriptor);

    extraPropertyDescriptors.add(qCPDescriptor);

    BasicIntegerPropertyDescriptor pagePropertyDescriptor = new BasicIntegerPropertyDescriptor();
    pagePropertyDescriptor.setName(IPageable.PAGE);
    pagePropertyDescriptor.setReadOnly(true);
    extraPropertyDescriptors.add(pagePropertyDescriptor);

    BasicIntegerPropertyDescriptor displayPageIndexPropertyDescriptor = new BasicIntegerPropertyDescriptor();
    displayPageIndexPropertyDescriptor.setName(IPageable.DISPLAY_PAGE_INDEX);
    extraPropertyDescriptors.add(displayPageIndexPropertyDescriptor);

    BasicIntegerPropertyDescriptor pageSizePropertyDescriptor = new BasicIntegerPropertyDescriptor();
    pageSizePropertyDescriptor.setName(IPageable.PAGE_SIZE);
    pageSizePropertyDescriptor.setReadOnly(true);
    extraPropertyDescriptors.add(pageSizePropertyDescriptor);

    BasicIntegerPropertyDescriptor pageCountPropertyDescriptor = new BasicIntegerPropertyDescriptor();
    pageCountPropertyDescriptor.setName(IPageable.PAGE_COUNT);
    pageCountPropertyDescriptor.setReadOnly(true);
    extraPropertyDescriptors.add(pageCountPropertyDescriptor);

    BasicIntegerPropertyDescriptor recordCountPropertyDescriptor = new BasicIntegerPropertyDescriptor();
    recordCountPropertyDescriptor.setName(IPageable.RECORD_COUNT);
    recordCountPropertyDescriptor.setReadOnly(true);
    extraPropertyDescriptors.add(recordCountPropertyDescriptor);

    BasicIntegerPropertyDescriptor selectedRecordCountPropertyDescriptor = new BasicIntegerPropertyDescriptor();
    selectedRecordCountPropertyDescriptor.setName(IPageable.SELECTED_RECORD_COUNT);
    selectedRecordCountPropertyDescriptor.setReadOnly(true);
    extraPropertyDescriptors.add(selectedRecordCountPropertyDescriptor);

    return extraPropertyDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPermId() {
    return super.getPermId() + ".filter";
  }
}
