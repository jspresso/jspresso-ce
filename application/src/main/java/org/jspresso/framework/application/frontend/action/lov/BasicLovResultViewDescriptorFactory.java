/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.application.action.AbstractActionContextAware;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.basic.BasicCollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * A default implementation for lov result view factories.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicLovResultViewDescriptorFactory extends
    AbstractActionContextAware implements ILovResultViewDescriptorFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionViewDescriptor createResultViewDescriptor(
      IComponentDescriptorProvider<IComponent> entityRefDescriptor,
      Map<String, Object> lovContext) {
    BasicTableViewDescriptor resultViewDescriptor = new BasicTableViewDescriptor();

    BasicListDescriptor<IComponent> queriedEntitiesListDescriptor = new BasicListDescriptor<IComponent>();
    IComponentDescriptor<? extends IComponent> resultListCompDesc = entityRefDescriptor
        .getComponentDescriptor();
    if (resultListCompDesc instanceof IQueryComponentDescriptor) {
      resultListCompDesc = ((IQueryComponentDescriptor) resultListCompDesc)
          .getQueriedComponentsDescriptor();
    }
    queriedEntitiesListDescriptor.setElementDescriptor(resultListCompDesc);

    BasicCollectionPropertyDescriptor<IComponent> queriedEntitiesDescriptor = new BasicCollectionPropertyDescriptor<IComponent>();
    queriedEntitiesDescriptor
        .setReferencedDescriptor(queriedEntitiesListDescriptor);
    queriedEntitiesDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);

    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    resultViewDescriptor.setReadOnly(true);
    resultViewDescriptor.setRenderedProperties(entityRefDescriptor
        .getRenderedProperties());
    if (getModel(lovContext) instanceof IQueryComponent) {
      // We are on a filter view that suppports multi selection
      resultViewDescriptor
          .setSelectionMode(ESelectionMode.MULTIPLE_INTERVAL_CUMULATIVE_SELECTION);
    } else {
      resultViewDescriptor.setSelectionMode(ESelectionMode.SINGLE_SELECTION);
    }

    resultViewDescriptor.setPermId("Lov." + entityRefDescriptor.getName());
    return resultViewDescriptor;
  }

}
