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

import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;

/**
 * Factory for query component view descriptor.
 *
 * @author Vincent Vandenschrick
 */
public interface IQueryViewDescriptorFactory {

  /**
   * Creates a new query component view descriptor.
   *
   * @param componentDescriptorProvider           the component descriptor provider to create query descriptor for.
   * @param queryComponentDescriptor           the actual query component descriptor that will be used as model.
   * @param actionContext the action context
   * @return the created view descriptor.
   */
  IViewDescriptor createQueryViewDescriptor(
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
      IComponentDescriptor<? extends IQueryComponent> queryComponentDescriptor, @SuppressWarnings(
      "UnusedParameters") Map<String, Object> actionContext);

  /**
   * Performs necessary adaptations to an existing view descriptor in order to
   * make it support query component model.
   *
   * @param viewDescriptor           the view descriptor to adapt.
   * @return the adapted view descriptor (same instance as the parameter)
   */
  <V extends IViewDescriptor> V adaptExistingViewDescriptor(V viewDescriptor);
}
