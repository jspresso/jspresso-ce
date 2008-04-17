/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.view.descriptor.basic.BasicNestingViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;


/**
 * The default implementation for module view descriptor factory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicModuleViewDescriptorFactory implements
    IModuleViewDescriptorFactory {

  private IQueryViewDescriptorFactory queryViewDescriptorFactory;

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
                .getPropertyDescriptor("filter"));
        BasicBorderViewDescriptor decorator = new BasicBorderViewDescriptor();
        decorator.setNorthViewDescriptor(filterViewDescriptor);
        decorator.setCenterViewDescriptor(projectedViewDescriptor);
        // decorator.setActionMap(projectedViewDescriptor.getActionMap());
        // ((BasicViewDescriptor) projectedViewDescriptor).setActionMap(null);
        projectedViewDescriptor = decorator;
      }
      BasicNestingViewDescriptor moduleViewDescriptor = new BasicNestingViewDescriptor();
      moduleViewDescriptor.setNestedViewDescriptor(projectedViewDescriptor);
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
      BasicNestingViewDescriptor moduleElementViewDescriptor = new BasicNestingViewDescriptor();
      moduleElementViewDescriptor
          .setNestedViewDescriptor(projectedViewDescriptor);
      moduleElementViewDescriptor.setModelDescriptor(moduleDescriptor);
      return moduleElementViewDescriptor;
    }
    return projectedViewDescriptor;
  }

  /**
   * Sets the queryViewDescriptorFactory.
   * 
   * @param queryViewDescriptorFactory
   *            the queryViewDescriptorFactory to set.
   */
  public void setQueryViewDescriptorFactory(
      IQueryViewDescriptorFactory queryViewDescriptorFactory) {
    this.queryViewDescriptorFactory = queryViewDescriptorFactory;
  }

}
