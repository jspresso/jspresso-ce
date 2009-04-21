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
package org.jspresso.framework.application.view.descriptor.basic;

import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.BeanModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import org.jspresso.framework.application.view.descriptor.IModuleViewDescriptorFactory;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * The default implementation for module view descriptor factory.
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
 */
public class BasicModuleViewDescriptorFactory implements
    IModuleViewDescriptorFactory {

  private IQueryViewDescriptorFactory queryViewDescriptorFactory;
  private IViewDescriptor             pagingStatusViewDescriptor;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IViewDescriptor createProjectedViewDescriptor(Module module) {
    IViewDescriptor projectedViewDescriptor = module
        .getProjectedViewDescriptor();
    if (module instanceof BeanCollectionModule) {
      IComponentDescriptor<Object> componentDescriptor = ((BeanCollectionModule) module)
          .getElementComponentDescriptor();
      if (componentDescriptor == null
          && projectedViewDescriptor instanceof ICollectionViewDescriptor
          && projectedViewDescriptor.getModelDescriptor() != null) {
        componentDescriptor = ((ICollectionDescriptorProvider) projectedViewDescriptor
            .getModelDescriptor()).getCollectionDescriptor()
            .getElementDescriptor();
      }
      BeanCollectionModuleDescriptor moduleDescriptor;
      if (module instanceof FilterableBeanCollectionModule) {
        moduleDescriptor = new FilterableBeanCollectionModuleDescriptor(
            componentDescriptor, ((FilterableBeanCollectionModule) module)
                .getFilterComponentDescriptor());
      } else {
        moduleDescriptor = new BeanCollectionModuleDescriptor(
            componentDescriptor);
      }
      ((BasicViewDescriptor) projectedViewDescriptor)
          .setModelDescriptor(moduleDescriptor
              .getPropertyDescriptor("moduleObjects"));
      if (module instanceof FilterableBeanCollectionModule) {
        IComponentDescriptor<Object> filterComponentDescriptor = ((FilterableBeanCollectionModule) module)
            .getFilterComponentDescriptor();
        IViewDescriptor filterViewDescriptor = ((FilterableBeanCollectionModule) module)
            .getFilterViewDescriptor();
        if (filterViewDescriptor == null) {
          filterViewDescriptor = queryViewDescriptorFactory
              .createQueryViewDescriptor(filterComponentDescriptor);
        }
        ((BasicViewDescriptor) filterViewDescriptor)
            .setModelDescriptor(moduleDescriptor
                .getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER));
        BasicBorderViewDescriptor decorator = new BasicBorderViewDescriptor();
        decorator.setNorthViewDescriptor(filterViewDescriptor);
        decorator.setCenterViewDescriptor(projectedViewDescriptor);
        // decorator.setActionMap(projectedViewDescriptor.getActionMap());
        // ((BasicViewDescriptor) projectedViewDescriptor).setActionMap(null);
        if (pagingStatusViewDescriptor != null) {
          BasicBorderViewDescriptor nestingViewDescriptor = new BasicBorderViewDescriptor();
          nestingViewDescriptor
              .setModelDescriptor(moduleDescriptor
                  .getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER));
          nestingViewDescriptor
              .setWestViewDescriptor(pagingStatusViewDescriptor);
          decorator.setSouthViewDescriptor(nestingViewDescriptor);
        }
        projectedViewDescriptor = decorator;
      }
      BasicBorderViewDescriptor moduleViewDescriptor = new BasicBorderViewDescriptor();
      moduleViewDescriptor.setCenterViewDescriptor(projectedViewDescriptor);
      moduleViewDescriptor.setModelDescriptor(moduleDescriptor);
      return moduleViewDescriptor;
    } else if (module instanceof BeanModule) {
      IComponentDescriptor<Object> componentDescriptor = ((BeanModule) module)
          .getComponentDescriptor();
      if (componentDescriptor == null
          && projectedViewDescriptor.getModelDescriptor() instanceof IComponentDescriptor) {
        componentDescriptor = (IComponentDescriptor<Object>) projectedViewDescriptor
            .getModelDescriptor();
      }
      BeanModuleDescriptor moduleDescriptor = new BeanModuleDescriptor(
          componentDescriptor);
      ((BasicViewDescriptor) projectedViewDescriptor)
          .setModelDescriptor(moduleDescriptor
              .getPropertyDescriptor("moduleObject"));
      BasicBorderViewDescriptor moduleElementViewDescriptor = new BasicBorderViewDescriptor();
      moduleElementViewDescriptor
          .setCenterViewDescriptor(projectedViewDescriptor);
      moduleElementViewDescriptor.setModelDescriptor(moduleDescriptor);
      return moduleElementViewDescriptor;
    }
    return projectedViewDescriptor;
  }

  /**
   * Sets the queryViewDescriptorFactory.
   * 
   * @param queryViewDescriptorFactory
   *          the queryViewDescriptorFactory to set.
   */
  public void setQueryViewDescriptorFactory(
      IQueryViewDescriptorFactory queryViewDescriptorFactory) {
    this.queryViewDescriptorFactory = queryViewDescriptorFactory;
  }

  /**
   * Sets the pagingStatusViewDescriptor.
   * 
   * @param pagingStatusViewDescriptor
   *          the pagingStatusViewDescriptor to set.
   */
  public void setPagingStatusViewDescriptor(
      IViewDescriptor pagingStatusViewDescriptor) {
    this.pagingStatusViewDescriptor = pagingStatusViewDescriptor;
  }

}
