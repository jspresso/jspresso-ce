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
package org.jspresso.framework.application.model.mobile;

import java.util.Arrays;
import java.util.Collections;

import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.descriptor.BeanModuleDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.AbstractMobilePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobileViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCompositePageViewDescriptor;

/**
 * This type of module keeps a reference on a single bean. There is no
 * assumption made on whether this bean is actually a persistent entity or any
 * other type of java bean.
 * <p/>
 * Bean modules must have their referenced bean initialized somehow. So it's
 * rather common to have the module content initialized through a startup action
 * depending on the session state or dynamically constructed by a standard
 * action like {@code AddBeanAsSubModuleAction}.
 * <p/>
 * This type of module is definitely the one that offers maximum flexibility to
 * handle arbitrary models.
 *
 * @author Vincent Vandenschrick
 */
public class MobileBeanModule extends BeanModule {

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractMobilePageViewDescriptor getProjectedViewDescriptor() {
    AbstractMobilePageViewDescriptor projectedViewDescriptor = (AbstractMobilePageViewDescriptor) super
        .getProjectedViewDescriptor();
    IComponentDescriptor<?> componentDescriptor = getComponentDescriptor();
    if (componentDescriptor != null) {
      if (projectedViewDescriptor == null) {
        MobileComponentViewDescriptor beanViewDescriptor = new MobileComponentViewDescriptor();
        beanViewDescriptor.setModelDescriptor(componentDescriptor);
        beanViewDescriptor.setBorderType(EBorderType.TITLED);
        beanViewDescriptor.setName(componentDescriptor.getName());
        projectedViewDescriptor = new MobileCompositePageViewDescriptor();
        ((MobileCompositePageViewDescriptor) projectedViewDescriptor).setPageSectionDescriptors(
            Collections.singletonList((IMobileViewDescriptor) beanViewDescriptor));
        setProjectedViewDescriptor(projectedViewDescriptor);
      }
      if (projectedViewDescriptor.getModelDescriptor() == null) {
        projectedViewDescriptor.setModelDescriptor(componentDescriptor);
      }
    }
    projectedViewDescriptor.setI18nName(getI18nName());
    projectedViewDescriptor.setI18nDescription(getI18nDescription());
    return projectedViewDescriptor;
  }

  /**
   * Returns the projectedViewDescriptor nested in a "moduleObject" property
   * view.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    AbstractMobilePageViewDescriptor projectedViewDescriptor = getProjectedViewDescriptor();
    if (projectedViewDescriptor != null) {
      BeanModuleDescriptor beanModuleDescriptor = getDescriptor();
      MobileBorderViewDescriptor wrapperDescriptor = new MobileBorderViewDescriptor();
      wrapperDescriptor.setModelDescriptor(beanModuleDescriptor.getPropertyDescriptor(MODULE_OBJECT));
      wrapperDescriptor.setCenterViewDescriptor(projectedViewDescriptor);
      MobileBorderViewDescriptor viewDescriptor = new MobileBorderViewDescriptor();
      viewDescriptor.setModelDescriptor(beanModuleDescriptor);
      viewDescriptor.setCenterViewDescriptor(wrapperDescriptor);
      viewDescriptor.setI18nHeader(getI18nHeaderDescription());
      return viewDescriptor;
    }
    return null;
  }

  /**
   * Mobile bean module only support page views as projected views
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setProjectedViewDescriptor(IViewDescriptor elementViewDescriptor) {
    if (!(elementViewDescriptor instanceof AbstractMobilePageViewDescriptor)) {
      throw new IllegalArgumentException(
          "Mobile bean module views only support page views as element views and not : " + elementViewDescriptor
              .getClass().getSimpleName());
    }
    super.setProjectedViewDescriptor(elementViewDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MobileBeanModule clone() {
    MobileBeanModule clone = (MobileBeanModule) super.clone();
    return clone;
  }

  /**
   * Create default projected view descriptor.
   *
   * @return the view descriptor
   */
  protected IViewDescriptor createDefaultProjectedViewDescriptor() {
    IComponentDescriptor<?> componentDescriptor = getComponentDescriptor();

    MobileComponentViewDescriptor componentViewDescriptor = new MobileComponentViewDescriptor();
    componentViewDescriptor.setModelDescriptor(componentDescriptor);
    componentViewDescriptor.setName(componentDescriptor.getName());

    MobileCompositePageViewDescriptor defaultProjectedViewDescriptor = new MobileCompositePageViewDescriptor();
    defaultProjectedViewDescriptor.setInlineEditing(true);
    defaultProjectedViewDescriptor.setPageSectionDescriptors(
        Arrays.<IMobileViewDescriptor>asList(componentViewDescriptor));
    return defaultProjectedViewDescriptor;
  }
}
