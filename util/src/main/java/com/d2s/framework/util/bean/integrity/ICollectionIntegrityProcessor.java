/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean.integrity;

import java.util.Collection;

/**
 * This is an interface used to identify classes responsible for providing
 * component collection property integrity processors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICollectionIntegrityProcessor extends
    IPropertyIntegrityProcessor {

  /**
   * This method gets called whenever a value has been added to a collection
   * property on an component to which this processor is registered. This method
   * should throw an <code>IntegrityException</code> if pre-checks are not
   * valid.
   * 
   * @param target
   *          the component the processor is ran on.
   * @param collection
   *          the actual value of the collection property accessed.
   * @param addedValue
   *          the value added in the collection.
   */
  void postprocessAdderIntegrity(Object target, Collection collection,
      Object addedValue);

  /**
   * This method gets called whenever a value has been removed from a collection
   * property on an component to which this processor is registered. This method
   * should throw an <code>IntegrityException</code> if post-checks are not
   * valid.
   * 
   * @param target
   *          the component the processor is ran on.
   * @param collection
   *          the actual value of the collection property accessed.
   * @param removedValue
   *          the value removed from the collection.
   */
  void postprocessRemoverIntegrity(Object target, Collection collection,
      Object removedValue);

  /**
   * This method gets called whenever a value is about to be added to a
   * collection property on an component to which this processor is registered.
   * This method should throw an <code>IntegrityException</code> if pre-checks
   * are not valid.
   * 
   * @param target
   *          the component the processor is ran on.
   * @param collection
   *          the actual value of the collection property accessed.
   * @param addedValue
   *          the value added in the collection.
   */
  void preprocessAdderIntegrity(Object target, Collection collection,
      Object addedValue);

  /**
   * This method gets called whenever a value is about to be removed from a
   * collection property on an component to which this processor is registered.
   * This method should throw an <code>IntegrityException</code> if pre-checks
   * are not valid.
   * 
   * @param target
   *          the component the processor is ran on.
   * @param collection
   *          the actual value of the collection property accessed.
   * @param removedValue
   *          the value removed from the collection.
   */
  void preprocessRemoverIntegrity(Object target, Collection collection,
      Object removedValue);
}
