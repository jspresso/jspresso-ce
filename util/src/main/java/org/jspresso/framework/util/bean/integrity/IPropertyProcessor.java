/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean.integrity;

/**
 * This is an interface used to identify classes responsible for providing
 * component property integrity processors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            The type of the target.
 * @param <F>
 *            The type of the property.
 */
public interface IPropertyProcessor<E, F> {

  /**
   * This method gets called whenever a property has been set on an component to
   * which this processor is registered.
   * 
   * @param target
   *            the component the processor is ran on.
   * @param oldPropertyValue
   *            the old value of the property accessed.
   * @param newPropertyValue
   *            the new value of the property accessed.
   */
  void postprocessSetter(E target, F oldPropertyValue, F newPropertyValue);

  /**
   * This method gets called whenever a property has been set on an component to
   * which this processor is registered. This method may change the actual value
   * set to the bean.
   * 
   * @param target
   *            the component the processor is ran on.
   * @param newPropertyValue
   *            the new value of the property accessed.
   * @return the actual value to set on the bean (it may be unchanged of
   *         course).
   */
  F interceptSetter(E target, F newPropertyValue);

  /**
   * This method gets called whenever a property is about to be set on an
   * component to which this processor is registered. This method should throw
   * an <code>IntegrityException</code> if pre-checks are not valid.
   * 
   * @param target
   *            the component the processor is ran on.
   * @param newPropertyValue
   *            the new value of the property accessed.
   */
  void preprocessSetter(E target, F newPropertyValue);
}
