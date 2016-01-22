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
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.view.descriptor.basic.BasicTabViewDescriptor;

/**
 * Factory for query component extra view descriptor.
 *
 * @author Maxime Hamm
 */
public interface IQueryExtraViewDescriptorFactory {

  /**
   * Creates a new extra query component view descriptor.
   * @param projectedViewDescriptor
   *     the projected view descriptor.
   * @param componentDescriptorProvider
   *     the component descriptor provider.
   * @param queryComponentDescriptor
   *     the actual query component descriptor that will be used as model.
   * @return the created view descriptor.
   */
  BasicTabViewDescriptor createQueryExtraViewDescriptor(
      IViewDescriptor projectedViewDescriptor,
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
      IComponentDescriptor<? extends IQueryComponent> queryComponentDescriptor);

}
