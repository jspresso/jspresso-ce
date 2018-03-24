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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.view.descriptor.IRepeaterViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This descriptor is used to implement a repeater view. A repeater view displays a
 * collection of components, each one in an arbitrary view that is repeated as necessary.
 * Repeater view supports selection by clicking one of the sections, so it can be used in a master-detail view.
 * It also supports row action by double-clicking one of the sections.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractRepeaterViewDescriptor extends BasicCollectionViewDescriptor
    implements IRepeaterViewDescriptor {

  private IViewDescriptor repeatedViewDescriptor;

  /**
   * Gets element view descriptor.
   *
   * @return the element view descriptor
   */
  @Override
  public IViewDescriptor getRepeatedViewDescriptor() {
    if (repeatedViewDescriptor instanceof BasicViewDescriptor && repeatedViewDescriptor.getModelDescriptor() == null
        && getModelDescriptor() instanceof ICollectionDescriptorProvider<?>) {
      ((BasicViewDescriptor) repeatedViewDescriptor).setModelDescriptor(
          ((ICollectionDescriptorProvider) getModelDescriptor()).getCollectionDescriptor().getElementDescriptor());
    }
    return repeatedViewDescriptor;
  }

  /**
   * Configures the view to be repeated as many times as necessary to match the element count in the model collection.
   *
   * @param repeatedViewDescriptor
   *     the element view descriptor
   */
  public void setRepeatedViewDescriptor(IViewDescriptor repeatedViewDescriptor) {
    this.repeatedViewDescriptor = repeatedViewDescriptor;
  }

  /**
   * Returns {@code true}.
   * <p>
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isVerticallyScrollable() {
    return true;
  }

  /**
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isScrollable() {
    return isVerticallyScrollable() || isHorizontallyScrollable();
  }

  /**
   * Returns {@code false}.
   * <p>
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isHorizontallyScrollable() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized AbstractRepeaterViewDescriptor cloneReadOnly() {
    if (readOnlyClone == null) {
      readOnlyClone = clone();
      if (getRepeatedViewDescriptor() != null) {
        ((AbstractRepeaterViewDescriptor) readOnlyClone).setRepeatedViewDescriptor(
            (IViewDescriptor) getRepeatedViewDescriptor().cloneReadOnly());
      }
    }
    return (AbstractRepeaterViewDescriptor) readOnlyClone;
  }
}
