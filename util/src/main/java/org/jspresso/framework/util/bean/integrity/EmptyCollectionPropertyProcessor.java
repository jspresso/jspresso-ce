/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean.integrity;

import java.util.Collection;

/**
 * Empty implementation of a collection property integrity processor.
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
public class EmptyCollectionPropertyProcessor<E, F extends Collection<?>>
    extends EmptyPropertyProcessor<E, F> implements
    ICollectionPropertyProcessor<E, F> {

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void postprocessAdder(E target, F collection,
      Object addedValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void postprocessRemover(E target, F collection,
      Object removedValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void preprocessAdder(E target, F collection,
      Object addedValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void preprocessRemover(E target, F collection,
      Object removedValue) {
    // NO-OP
  }

}
