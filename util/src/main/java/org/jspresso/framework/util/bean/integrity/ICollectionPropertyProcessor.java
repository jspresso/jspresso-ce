/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean.integrity;

import java.util.Collection;

/**
 * This is an interface used to identify classes responsible for providing
 * component collection property integrity processors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            The type of the target.
 * @param <F>
 *            The type of the property (a subclass of collection).
 */
public interface ICollectionPropertyProcessor<E, F extends Collection<?>>
    extends IPropertyProcessor<E, F> {

  /**
   * This method gets called whenever a value has been added to a collection
   * property on an component to which this processor is registered. This method
   * should throw an <code>IntegrityException</code> if pre-checks are not
   * valid.
   * 
   * @param target
   *            the component the processor is ran on.
   * @param collection
   *            the actual value of the collection property accessed.
   * @param addedValue
   *            the value added in the collection.
   */
  void postprocessAdder(E target, F collection, Object addedValue);

  /**
   * This method gets called whenever a value has been removed from a collection
   * property on an component to which this processor is registered. This method
   * should throw an <code>IntegrityException</code> if post-checks are not
   * valid.
   * 
   * @param target
   *            the component the processor is ran on.
   * @param collection
   *            the actual value of the collection property accessed.
   * @param removedValue
   *            the value removed from the collection.
   */
  void postprocessRemover(E target, F collection, Object removedValue);

  /**
   * This method gets called whenever a value is about to be added to a
   * collection property on an component to which this processor is registered.
   * This method should throw an <code>IntegrityException</code> if pre-checks
   * are not valid.
   * 
   * @param target
   *            the component the processor is ran on.
   * @param collection
   *            the actual value of the collection property accessed.
   * @param addedValue
   *            the value added in the collection.
   */
  void preprocessAdder(E target, F collection, Object addedValue);

  /**
   * This method gets called whenever a value is about to be removed from a
   * collection property on an component to which this processor is registered.
   * This method should throw an <code>IntegrityException</code> if pre-checks
   * are not valid.
   * 
   * @param target
   *            the component the processor is ran on.
   * @param collection
   *            the actual value of the collection property accessed.
   * @param removedValue
   *            the value removed from the collection.
   */
  void preprocessRemover(E target, F collection, Object removedValue);
}
