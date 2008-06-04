/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor.basic;

import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;


/**
 * Default implementation of a collection descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete collection component element type.
 */
public class BasicCollectionDescriptor<E> extends DefaultDescriptor implements
    ICollectionDescriptor<E> {

  private Class<?>                collectionInterface;
  private IComponentDescriptor<E> elementDescriptor;

  /**
   * {@inheritDoc}
   */
  public ICollectionDescriptor<E> getCollectionDescriptor() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getCollectionInterface() {
    return collectionInterface;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<E> getElementDescriptor() {
    return elementDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return getCollectionInterface();
  }

  /**
   * Sets the collectionInterface.
   * 
   * @param collectionInterface
   *            the collectionInterface to set.
   */
  public void setCollectionInterface(Class<?> collectionInterface) {
    this.collectionInterface = collectionInterface;
  }

  /**
   * Sets the elementDescriptor.
   * 
   * @param elementDescriptor
   *            the elementDescriptor to set.
   */
  public void setElementDescriptor(IComponentDescriptor<E> elementDescriptor) {
    this.elementDescriptor = elementDescriptor;
  }
}
