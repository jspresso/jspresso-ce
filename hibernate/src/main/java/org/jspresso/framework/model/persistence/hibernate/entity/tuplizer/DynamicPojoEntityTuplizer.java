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
package org.jspresso.framework.model.persistence.hibernate.entity.tuplizer;

import java.util.Iterator;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.PojoEntityTuplizer;
import org.jspresso.framework.model.persistence.hibernate.property.EntityPropertyAccessor;

/**
 * A specialized hibernate tuplizer to handle proxy entities.
 *
 * @author Vincent Vandenschrick
 */
public class DynamicPojoEntityTuplizer extends PojoEntityTuplizer {

  /**
   * Constructs a new {@code ProxyPojoEntityTuplizer} instance.
   *
   * @param entityMetamodel
   *          the entity meta model.
   * @param mappedEntity
   *          the mapped entity.
   */
  public DynamicPojoEntityTuplizer(EntityMetamodel entityMetamodel,
      PersistentClass mappedEntity) {
    super(entityMetamodel, mappedEntity);
    fixPropertyAccessors(mappedEntity);
  }

  @SuppressWarnings("unchecked")
  private void fixPropertyAccessors(PersistentClass mappedEntity) {
    Iterator<Property> properties = mappedEntity
        .getPropertyIterator();
    while (properties.hasNext()) {
      properties.next().setPropertyAccessorName(
          EntityPropertyAccessor.class.getName());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ProxyFactory buildProxyFactory(PersistentClass persistentClass,
      Getter idGetter, Setter idSetter) {
    fixPropertyAccessors(persistentClass);
    return super.buildProxyFactory(persistentClass, idGetter, idSetter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Getter buildPropertyGetter(Property mappedProperty,
      PersistentClass mappedEntity) {
    return new EntityPropertyAccessor().getGetter(
        mappedEntity.getMappedClass(), mappedProperty.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Setter buildPropertySetter(Property mappedProperty,
      PersistentClass mappedEntity) {
    return new EntityPropertyAccessor().getSetter(
        mappedEntity.getMappedClass(), mappedProperty.getName());
  }

}
