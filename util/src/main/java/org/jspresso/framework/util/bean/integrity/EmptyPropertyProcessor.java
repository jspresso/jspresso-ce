/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean.integrity;

/**
 * Empty implementation of a property integrity processor.
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
public abstract class EmptyPropertyProcessor<E, F> implements
    IPropertyProcessor<E, F> {

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void postprocessSetter(E target, F oldPropertyValue, F newPropertyValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public F interceptSetter(E target, F newPropertyValue) {
    return newPropertyValue;
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void preprocessSetter(E target, F newPropertyValue) {
    // NO-OP
  }

}
