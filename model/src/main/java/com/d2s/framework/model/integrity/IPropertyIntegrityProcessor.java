/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.integrity;

/**
 * This is an interface used to identify classes responsible for providing
 * component property integrity processors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          The type of the target.
 * @param <F>
 *          The type of the property.
 */
public interface IPropertyIntegrityProcessor<E, F> {

  /**
   * This method gets called whenever a property is about to be set on an
   * component to which this processor is registered. This method should throw
   * an <code>IntegrityException</code> if pre-checks are not valid.
   * 
   * @param target
   *          the component the processor is ran on.
   * @param oldPropertyValue
   *          the old value of the property accessed.
   * @param newPropertyValue
   *          the new value of the property accessed.
   */
  void preprocessSetterIntegrity(E target, F oldPropertyValue,
      F newPropertyValue);

  /**
   * This method gets called whenever a property has been set on an component to
   * which this processor is registered. This method should throw an
   * <code>IntegrityException</code> if post-checks are not valid.
   * 
   * @param target
   *          the component the processor is ran on.
   * @param oldPropertyValue
   *          the old value of the property accessed.
   * @param newPropertyValue
   *          the new value of the property accessed.
   */
  void postprocessSetterIntegrity(E target, F oldPropertyValue,
      F newPropertyValue);
}
