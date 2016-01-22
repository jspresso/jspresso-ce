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
package org.jspresso.framework.model.descriptor;

import java.util.Map;

/**
 * This interface is implemented by descriptors of reference properties.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete component type.
 */
public interface IReferencePropertyDescriptor<E> extends
    IRelationshipEndPropertyDescriptor, IComponentDescriptorProvider<E> {

  /**
   * Gets the initialization map for a LOV query entity. Initialization mapping
   * can hold either a references to property names of the owning entity
   * (master) or constant values. For instance, a mapping holding ('propA',
   * 'propB') will indicate that the LOV query entity 'propA' property should be
   * initialized with the 'propB' property of its owning entity. Whenever
   * 'propB' is not a valid property name, it will be treated as a constant
   * value and 'propA' property will be initialized accordingly.
   *
   * @return the initialisation mapping.
   */
  Map<String, Object> getInitializationMapping();

  /**
   * When a list of value is triggered, this is the default page size of the
   * result list.
   *
   * @return the default page size of the result list.
   */
  Integer getPageSize();

  /**
   * Gets the descriptor of the component referenced by this property.
   *
   * @return the referenced component descriptor
   */
  IComponentDescriptor<? extends E> getReferencedDescriptor();

  /**
   * Gets whether this reference property descriptor is a one-to-one end.
   *
   * @return true if this reference property descriptor is a one-to-one end.
   */
  boolean isOneToOne();

}
