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
package org.jspresso.framework.util.bean.integrity;

import java.util.Collection;

/**
 * Empty implementation of a collection property integrity processor.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          The type of the target.
 * @param <F>
 *          The type of the property (a subclass of collection).
 * @param <G>
 *          The type of element managed in the collection.
 */
public class EmptyCollectionPropertyProcessor<E, F extends Collection<G>, G>
    extends EmptyPropertyProcessor<E, F> implements
    ICollectionPropertyProcessor<E, F, G> {

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void postprocessAdder(E target, F collection, G addedValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void postprocessRemover(E target, F collection, G removedValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void preprocessAdder(E target, F collection, G addedValue) {
    // NO-OP
  }

  /**
   * No-op.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void preprocessRemover(E target, F collection, G removedValue) {
    // NO-OP
  }

}
