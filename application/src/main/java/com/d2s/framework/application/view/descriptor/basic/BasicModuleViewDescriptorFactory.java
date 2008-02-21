/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

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
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicComponentViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicNestingViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicPropertyViewDescriptor;
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
        IViewDescriptor filterViewDescriptor = ((FilterableBeanCollectionModule) module)
            .getFilterViewDescriptor();
        if (filterViewDescriptor == null) {
          filterViewDescriptor = new BasicComponentViewDescriptor();
          ((BasicComponentViewDescriptor) filterViewDescriptor)
              .setLabelsPosition(IComponentViewDescriptor.ABOVE);
          List<ISubViewDescriptor> propertyViewDescriptors = new ArrayList<ISubViewDescriptor>();
          for (String qPropertyName : ((FilterableBeanCollectionModule) module)
              .getFilterComponentDescriptor().getQueryableProperties()) {
            BasicPropertyViewDescriptor qPropertyViewDescriptor = new BasicPropertyViewDescriptor();
            qPropertyViewDescriptor.setName(qPropertyName);
            propertyViewDescriptors.add(qPropertyViewDescriptor);
          }
          ((BasicComponentViewDescriptor) filterViewDescriptor)
              .setPropertyViewDescriptors(propertyViewDescriptors);
          ((BasicComponentViewDescriptor) filterViewDescriptor)
              .setColumnCount(propertyViewDescriptors.size());
        }
        ((BasicViewDescriptor) filterViewDescriptor)
            .setModelDescriptor(moduleDescriptor
                .getPropertyDescriptor("filter"));
        BasicBorderViewDescriptor decorator = new BasicBorderViewDescriptor();
        decorator.setNorthViewDescriptor(filterViewDescriptor);
        decorator.setCenterViewDescriptor(projectedViewDescriptor);
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

}
