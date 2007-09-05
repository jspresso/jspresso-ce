/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.Collection;
import java.util.List;

/**
 * This interface is implemented by descriptors of collection properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete collection component element type.
 */
public interface ICollectionPropertyDescriptor<E> extends
    IRelationshipEndPropertyDescriptor, ICollectionDescriptorProvider {

  /**
   * Narrowed to a collection type.
   * 
   * @return the type of the model.
   */
  Class<? extends Collection> getModelType();

  /**
   * Get the list of properties ordering this collection.
   * 
   * @return the list of properties ordering this collection.
   */
  List<String> getOrderingProperties();

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
   * Triggers all adder postprocessors.
   * 
   * @param component
   *          the component targetted by the adder.
   * @param collection
   *          the collection value.
   * @param addedValue
   *          the property added value.
   */
  void postprocessAdder(Object component, Collection collection,
      Object addedValue);

  /**
   * Triggers all remer postprocessors.
   * 
   * @param component
   *          the component targetted by the remer.
   * @param collection
   *          the collection value.
   * @param removedValue
   *          the property removed value.
   */
  void postprocessRemover(Object component, Collection collection,
      Object removedValue);

  /**
   * Triggers all adder preprocessors.
   * 
   * @param component
   *          the component targetted by the adder.
   * @param collection
   *          the collection value.
   * @param addedValue
   *          the property added value.
   */
  void preprocessAdder(Object component, Collection collection,
      Object addedValue);

  /**
   * Triggers all remer preprocessors.
   * 
   * @param component
   *          the component targetted by the remer.
   * @param collection
   *          the collection value.
   * @param removedValue
   *          the property removed value.
   */
  void preprocessRemover(Object component, Collection collection,
      Object removedValue);
}
