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
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Factory for list-of-value views.
 *
 * @author Vincent Vandenschrick
 */
public interface ILovViewDescriptorFactory {

  /**
   * Creates a new lov view descriptor for a component descriptor.
   *
   * @param entityRefDescriptor
   *          the entity reference descriptor.
   * @param selectionMode
   *          allows to force the result view selection mode. When
   *          {@code null}, the default selection mode is applied.
   * @param okAction
   *          the action used to select the entity in the LOV.
   * @param lovContext
   *          the action context the LOV was triggered on.
   * @return the created view descriptor.
   */
  IViewDescriptor createLovViewDescriptor(
      IComponentDescriptorProvider<IComponent> entityRefDescriptor,
      ESelectionMode selectionMode, IDisplayableAction okAction,
      Map<String, Object> lovContext);
}
