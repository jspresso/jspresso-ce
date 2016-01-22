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

import java.util.Collections;

import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageSectionViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCompositePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileNavPageViewDescriptor;

/**
 * This type of module keeps a reference on a beans collection. There is no
 * assumption made on whether these beans are actually persistent entities or any
 * other type of java beans.
 * <p/>
 * Simple bean collection modules must have their collection of referenced beans
 * initialized somehow. There is no standard built-in action to do so, since it
 * is highly dependent on what's needed. So it's rather common to have the
 * module content initialized through a startup action depending on the session
 * state.
 *
 * @author Vincent Vandenschrick
 */
public class MobileBeanCollectionModule extends BeanCollectionModule {

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    IListViewDescriptor moduleObjectsView = getProjectedViewDescriptor();
    BeanCollectionModuleDescriptor moduleDescriptor = getDescriptor();
    ((BasicViewDescriptor) moduleObjectsView).setModelDescriptor(moduleDescriptor.getPropertyDescriptor(
        MobileBeanCollectionModule.MODULE_OBJECTS));
    MobileNavPageViewDescriptor moduleViewDescriptor = new MobileNavPageViewDescriptor();
    moduleViewDescriptor.setSelectionViewDescriptor(moduleObjectsView);
    IMobilePageViewDescriptor nextPage;
    if (getElementViewDescriptor() instanceof IMobilePageViewDescriptor) {
      nextPage = (IMobilePageViewDescriptor) getElementViewDescriptor();
    } else {
      nextPage = new MobileCompositePageViewDescriptor();
      ((MobileCompositePageViewDescriptor) nextPage).setPageSectionDescriptors(Collections.singletonList(
          getElementViewDescriptor()));
    }
    moduleViewDescriptor.setNextPageViewDescriptor(nextPage);
    moduleViewDescriptor.setModelDescriptor(moduleDescriptor);
    moduleViewDescriptor.setI18nName(getI18nName());
    moduleViewDescriptor.setI18nDescription(getI18nDescription());
    return moduleViewDescriptor;
  }

  /**
   * Mobile bean collection module views only support list views as projected view
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setProjectedViewDescriptor(IViewDescriptor projectedViewDescriptor) {
    if (!(projectedViewDescriptor instanceof IListViewDescriptor)) {
      throw new IllegalArgumentException(
          "Mobile bean collection modules only support list collection views and not :" + projectedViewDescriptor
              .getClass().getSimpleName());
    }
    super.setProjectedViewDescriptor(projectedViewDescriptor);
  }

  /**
   * Mobile bean collection module views only support list views as projected view
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IListViewDescriptor getProjectedViewDescriptor() {
    return (IListViewDescriptor) super.getProjectedViewDescriptor();
  }

  /**
   * Mobile bean collection module views only support page views as element views
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IMobilePageSectionViewDescriptor getElementViewDescriptor() {
    return (IMobilePageViewDescriptor) super.getElementViewDescriptor();
  }

  /**
   * Mobile bean collection module views only support page views as element views
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setElementViewDescriptor(IViewDescriptor elementViewDescriptor) {
    if (!(elementViewDescriptor instanceof IMobilePageSectionViewDescriptor)) {
      throw new IllegalArgumentException(
          "Mobile bean collection module views only support page views as element views and not :"
              + elementViewDescriptor.getClass().getSimpleName());
    }
    super.setElementViewDescriptor(elementViewDescriptor);
  }


}
