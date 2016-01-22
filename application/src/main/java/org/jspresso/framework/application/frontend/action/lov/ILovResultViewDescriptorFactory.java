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
package org.jspresso.framework.application.frontend.action.lov;

import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * Factory interface for LOV result view factories.
 *
 * @author Vincent Vandenschrick
 */
public interface ILovResultViewDescriptorFactory {

  /**
   * Creates a new LOV result view.
   *
   * @param entityRefDescriptor
   *          the provider of the row descriptor.
   * @param lovContext
   *          the LOV action context.
   * @return the created result view descriptor.
   */
  BasicViewDescriptor createResultViewDescriptor(
      IComponentDescriptorProvider<IComponent> entityRefDescriptor,
      Map<String, Object> lovContext);
}
