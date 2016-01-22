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

import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.util.collection.ESort;

/**
 * This interface is implemented by descriptors of collection properties.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete collection component element type.
 */
public interface ICollectionPropertyDescriptor<E> extends
    IRelationshipEndPropertyDescriptor, ICollectionDescriptorProvider<E> {

  /**
   * Get the list of properties ordering this collection.
   *
   * @return the list of properties ordering this collection.
   */
  Map<String, ESort> getOrderingProperties();

  /**
   * Gets the descriptor of the collection referenced by this property.
   *
   * @return the referenced collection descriptor.
   */
  ICollectionDescriptor<E> getReferencedDescriptor();

  /**
   * Gets whether this collection property descriptor is a many-to-many end.
   *
   * @return true if this collection property descriptor is a many-to-many end.
   */
  boolean isManyToMany();

  /**
   * Triggers all adder post processors.
   *
   * @param component
   *          the component targeted by the adder.
   * @param collection
   *          the collection value.
   * @param addedValue
   *          the property added value.
   * @param <F>
   *          the concrete holding component type.
   * @param <G>
   *          the concrete collection component element type.
   */
  <F, G> void postprocessAdder(F component, Collection<G> collection,
      G addedValue);

  /**
   * Triggers all remover post processors.
   *
   * @param component
   *          the component targeted by the remover.
   * @param collection
   *          the collection value.
   * @param removedValue
   *          the property removed value.
   * @param <F>
   *          the concrete holding component type.
   * @param <G>
   *          the concrete collection component element type.
   */
  <F, G> void postprocessRemover(F component, Collection<G> collection,
      G removedValue);

  /**
   * Triggers all adder pre processors.
   *
   * @param component
   *          the component targeted by the adder.
   * @param collection
   *          the collection value.
   * @param addedValue
   *          the property added value.
   * @param <F>
   *          the concrete holding component type.
   * @param <G>
   *          the concrete collection component element type.
   */
  <F, G> void preprocessAdder(F component, Collection<G> collection,
      G addedValue);

  /**
   * Triggers all remover pre processors.
   *
   * @param component
   *          the component targeted by the remover.
   * @param collection
   *          the collection value.
   * @param removedValue
   *          the property removed value.
   * @param <F>
   *          the concrete holding component type.
   * @param <G>
   *          the concrete collection component element type.
   */
  <F, G> void preprocessRemover(F component, Collection<G> collection,
      G removedValue);
}
