/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.integrity;

/**
 * Empty implementation of a property integrity processor.
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
public abstract class EmptyPropertyIntegrityProcessor<E, F> implements
    IPropertyIntegrityProcessor<E, F> {

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void preprocessSetterIntegrity(E target, F oldPropertyValue,
      F newPropertyValue) throws IntegrityException {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void postprocessSetterIntegrity(E target, F oldPropertyValue,
      F newPropertyValue) throws IntegrityException {
    // NO-OP
  }

}
