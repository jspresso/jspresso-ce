/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.FilterableBeanCollectionModule;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import com.d2s.framework.application.model.descriptor.BeanModuleDescriptor;
import com.d2s.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import com.d2s.framework.application.view.descriptor.IModuleViewDescriptorFactory;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.view.descriptor.ICollectionViewDescriptor;
import com.d2s.framework.view.descriptor.IQueryViewDescriptorFactory;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicNestingViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicViewDescriptor;

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
