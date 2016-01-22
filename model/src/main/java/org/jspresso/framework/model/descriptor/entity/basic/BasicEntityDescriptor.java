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
package org.jspresso.framework.model.descriptor.entity.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.AbstractComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicInterfaceDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicJavaSerializablePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This descriptor key to the description of the application model. It is used
 * to describe a model entity. A Jspresso managed entity has a synthetic
 * identifier (<i>id</i>) and is versioned (<i>version</i>) to cope with
 * concurrent access conflicts through optimistic locking. It conforms to the
 * <i>Java Beans</i> standard so that its property changes can be followed by
 * the classic {@code add/removePropertyChangeListener} methods; Jspresso
 * binding architecture leverages this behaviour.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete entity type.
 */
public class BasicEntityDescriptor<E extends IEntity> extends
    AbstractComponentDescriptor<E> {

  private boolean                              purelyAbstract;
  private static IComponentDescriptor<IEntity> rootEntityDescriptor;

  /**
   * Constructs a new {@code BasicEntityDescriptor} instance.
   *
   * @param name
   *          the name of the descriptor which has to be the fully-qualified
   *          class name of its contract.
   */
  public BasicEntityDescriptor(String name) {
    super(name);
    this.purelyAbstract = false;
  }

  private static IComponentDescriptor<IEntity> createDefaultEntityDescriptor() {
    BasicInterfaceDescriptor<IEntity> defaultEntityDescriptor = new BasicInterfaceDescriptor<>(
        IEntity.class.getName());

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<>(
        3);

    BasicJavaSerializablePropertyDescriptor idPropertyDescriptor = new BasicJavaSerializablePropertyDescriptor();
    idPropertyDescriptor.setName(IEntity.ID);
    idPropertyDescriptor.setMaxLength(36);
    idPropertyDescriptor.setReadOnly(true);
    propertyDescriptors.add(idPropertyDescriptor);

    BasicIntegerPropertyDescriptor versionPropertyDescriptor = new BasicIntegerPropertyDescriptor();
    versionPropertyDescriptor.setName(IEntity.VERSION);
    versionPropertyDescriptor.setReadOnly(true);
    propertyDescriptors.add(versionPropertyDescriptor);

    BasicBooleanPropertyDescriptor persistentPropertyDescriptor = new BasicBooleanPropertyDescriptor();
    persistentPropertyDescriptor.setName(IEntity.PERSISTENT);
    persistentPropertyDescriptor.setComputed(true);
    persistentPropertyDescriptor.setReadOnly(true);
    propertyDescriptors.add(persistentPropertyDescriptor);

    defaultEntityDescriptor.setPropertyDescriptors(propertyDescriptors);

    List<String> emptyList = Collections.emptyList();
    defaultEntityDescriptor.setRenderedProperties(emptyList);
    defaultEntityDescriptor.setQueryableProperties(emptyList);

    return defaultEntityDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntity() {
    return true;
  }

  /**
   * Gets whether this entity is purely abstract.
   *
   * @return whether this entity is purely abstract.
   */
  @Override
  public boolean isPurelyAbstract() {
    return purelyAbstract;
  }

  /**
   * This property is used to indicate that the entity type described is to be
   * considered <b>abstract</b>. Jspresso will prevent any instantiation through
   * its generic actions or internal mechanisms. Trying to do so will result in a
   * low level exception and reveals a coding (assembling) error.
   * <p>
   * However, an abstract entity will have a concrete representation in the data
   * store that depends on the inheritance mapping strategy used. As of now,
   * Jspresso uses the <i>join-subclass</i> inheritance mapping strategy when
   * generating the Hibernate mapping so an abstract entity will end up as a
   * table in the data store.
   * <p>
   * An abstract entity descriptor differs from an interface descriptor mainly
   * because of its concrete representation in the data store as formerly
   * described.
   *
   * @param purelyAbstract
   *          Whether this entity is purely abstract.
   */
  public void setPurelyAbstract(boolean purelyAbstract) {
    this.purelyAbstract = purelyAbstract;
  }

  /**
   * Configures the root entity descriptor to use globally for the application.
   * Jspresso will ensure that all entity descriptors have this root descriptor
   * as ancestor.
   *
   * @param rootEntityDescriptor
   *          the root entity descriptor to use globally for the application.
   */
  public static synchronized void setRootEntityDescriptor(
      IComponentDescriptor<IEntity> rootEntityDescriptor) {
    BasicEntityDescriptor.rootEntityDescriptor = rootEntityDescriptor;
  }

  /**
   * Gets the entityDescriptor.
   *
   * @return the entityDescriptor.
   */
  public static synchronized IComponentDescriptor<IEntity> getRootEntityDescriptor() {
    if (rootEntityDescriptor == null) {
      rootEntityDescriptor = createDefaultEntityDescriptor();
    }
    return rootEntityDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAncestorDescriptors(
      List<IComponentDescriptor<?>> ancestorDescriptors) {
    List<IComponentDescriptor<?>> refinedAncestorDescriptors = new ArrayList<>();
    if (ancestorDescriptors != null) {
      refinedAncestorDescriptors.addAll(ancestorDescriptors);
    }
    IComponentDescriptor<?> rootED = getRootEntityDescriptor();
    if (!refinedAncestorDescriptors.contains(rootED)) {
      refinedAncestorDescriptors.add(rootED);
    }
    super.setAncestorDescriptors(refinedAncestorDescriptors);
  }
}
