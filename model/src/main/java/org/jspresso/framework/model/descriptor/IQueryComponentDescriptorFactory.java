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
package org.jspresso.framework.model.descriptor;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;

/**
 * Interface contract for factories creating query component descriptors.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public interface IQueryComponentDescriptorFactory {

  /**
   * Creates a query component descriptor.
   *
   * @param componentDescriptorProvider
   *     the source for the component descriptor.
   * @return the created query component descriptor.
   */
  IComponentDescriptor<IQueryComponent> createQueryComponentDescriptor(
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider);

  /**
   * Create query component reference descriptor.
   *
   * @param referencePropertyName the reference property name
   * @param componentDescriptorProvider the component descriptor provider
   * @return the reference property descriptor that is usable in model as a filter reference.
   */
  IReferencePropertyDescriptor<IQueryComponent> createQueryComponentReferenceDescriptor(
      String referencePropertyName, IComponentDescriptorProvider<IComponent> componentDescriptorProvider);
}
