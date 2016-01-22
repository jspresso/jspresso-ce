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
package org.jspresso.framework.application.frontend.action.lov.mobile;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.lov.AbstractLovResultViewDescriptorFactory;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileListViewDescriptor;

/**
 * A default implementation for lov result view factories.
 *
 * @author Vincent Vandenschrick
 */
public class MobileLovResultViewDescriptorFactory extends
    AbstractLovResultViewDescriptorFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public BasicViewDescriptor createResultViewDescriptor(
      IComponentDescriptorProvider<IComponent> entityRefDescriptor,
      Map<String, Object> lovContext) {
    MobileListViewDescriptor resultViewDescriptor = new MobileListViewDescriptor();
    resultViewDescriptor.setShowArrow(false);

    ICollectionPropertyDescriptor<IComponent> queriedEntitiesDescriptor;
    queriedEntitiesDescriptor = (ICollectionPropertyDescriptor<IComponent>) getQueryComponentDescriptorFactory()
        .createQueryComponentDescriptor(entityRefDescriptor)
        .getPropertyDescriptor(IQueryComponent.QUERIED_COMPONENTS);

    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    resultViewDescriptor.setReadOnly(true);
    resultViewDescriptor.setSelectionMode(ESelectionMode.SINGLE_SELECTION);

    resultViewDescriptor.setPermId("Lov." + entityRefDescriptor.getName());
    return resultViewDescriptor;
  }
}
