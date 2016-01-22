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

import org.jspresso.framework.application.frontend.action.std.mobile.AddPageAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageSectionViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCompositePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileNavPageViewDescriptor;

/**
 * This is a specialized type of filterable bean collection module that provides a filter (
 * an instance of {@code IQueryComponent} ). This type of module, coupled
 * with a generic, built-in, action map is perfectly suited for CRUD-like
 * operations.
 *
 * @author Vincent Vandenschrick
 */
public class MobileFilterableBeanCollectionModule extends FilterableBeanCollectionModule {

  private IDisplayableAction queryModuleFilterAction;

  /**
   * {@inheritDoc}
   *
   * @return the view descriptor
   */
  @SuppressWarnings("unchecked")
  @Override
  public IViewDescriptor getViewDescriptor() {
    IListViewDescriptor moduleObjectsView = getProjectedViewDescriptor();
    BeanCollectionModuleDescriptor moduleDescriptor = getDescriptor();
    ((BasicViewDescriptor) moduleObjectsView).setModelDescriptor(moduleDescriptor.getPropertyDescriptor(
        BeanCollectionModule.MODULE_OBJECTS));
    MobileNavPageViewDescriptor modulePageView = new MobileNavPageViewDescriptor();
    modulePageView.setSelectionViewDescriptor(moduleObjectsView);
    if (getPagingAction() != null) {
      AddPageAction<?, ?, ?> addPageAction = new AddPageAction<>();
      addPageAction.setWrappedAction(getPagingAction());
      modulePageView.setPageEndAction(addPageAction);
    }
    IMobilePageViewDescriptor nextPage;
    if (getElementViewDescriptor() instanceof IMobilePageViewDescriptor) {
      nextPage = (IMobilePageViewDescriptor) getElementViewDescriptor();
    } else {
      nextPage = new MobileCompositePageViewDescriptor();
      ((MobileCompositePageViewDescriptor) nextPage).setPageSectionDescriptors(Collections.singletonList(
          getElementViewDescriptor()));
    }
    modulePageView.setNextPageViewDescriptor(nextPage);
    modulePageView.setModelDescriptor(moduleDescriptor);

    IComponentDescriptor<IComponent> realComponentDesc = getFilterComponentDescriptor();
    IMobilePageSectionViewDescriptor filterViewDesc = (IMobilePageSectionViewDescriptor) getFilterViewDescriptor();
    IComponentDescriptorProvider<IQueryComponent> filterModelDescriptorProvider =
        (IComponentDescriptorProvider<IQueryComponent>) moduleDescriptor
        .getPropertyDescriptor(FilterableBeanCollectionModuleDescriptor.FILTER);
    boolean customFilterView = false;
    if (filterViewDesc == null) {
      filterViewDesc = (MobileComponentViewDescriptor) getQueryViewDescriptorFactory().
          createQueryViewDescriptor(realComponentDesc, filterModelDescriptorProvider.getComponentDescriptor(),
              Collections.<String, Object>emptyMap());
    } else {
      customFilterView = true;
      // Deeply clean model descriptors on filter views
      cleanupFilterViewDescriptor(filterViewDesc);
    }
    if (filterViewDesc instanceof MobileCompositePageViewDescriptor) {
      for (IMobilePageSectionViewDescriptor sectionViewDescriptor : ((MobileCompositePageViewDescriptor) filterViewDesc)
          .getPageSectionDescriptors()) {
        if (sectionViewDescriptor instanceof BasicViewDescriptor) {
          ((BasicViewDescriptor) sectionViewDescriptor).setModelDescriptor(filterModelDescriptorProvider);
        }
      }
    } else if (filterViewDesc instanceof BasicViewDescriptor) {
      ((BasicViewDescriptor) filterViewDesc).setBorderType(EBorderType.TITLED);
      ((BasicViewDescriptor) filterViewDesc).setModelDescriptor(filterModelDescriptorProvider);
    }
    if (customFilterView) {
      getQueryViewDescriptorFactory().adaptExistingViewDescriptor(filterViewDesc);
    }

    if (getPageSize() != null && getPageSize() > 0) {
      if (moduleObjectsView.getPaginationViewDescriptor() == null
          && moduleObjectsView instanceof BasicListViewDescriptor) {
        ((BasicListViewDescriptor) moduleObjectsView).setPaginationViewDescriptor(getPaginationViewDescriptor());
      }
    }
    if (filterViewDesc instanceof MobileCompositePageViewDescriptor) {
      modulePageView.setHeaderSectionDescriptors(((MobileCompositePageViewDescriptor) filterViewDesc).getPageSectionDescriptors());
    } else {
      modulePageView.setHeaderSectionDescriptors(Arrays.asList(filterViewDesc));
    }
    modulePageView.setMainAction(getQueryModuleFilterAction());
    modulePageView.setI18nName(getI18nName());
    modulePageView.setI18nDescription(getI18nDescription());
    return modulePageView;
  }

  /**
   * Mobile filter bean collection module views only support list views as projected view
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setProjectedViewDescriptor(IViewDescriptor projectedViewDescriptor) {
    if (!(projectedViewDescriptor instanceof IListViewDescriptor)) {
      throw new IllegalArgumentException(
          "Mobile filter bean collection modules only support list collection views and not :" + projectedViewDescriptor
              .getClass().getSimpleName());
    }
    super.setProjectedViewDescriptor(projectedViewDescriptor);
  }

  /**
   * Mobile filter bean collection module views only support list views as projected view
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IListViewDescriptor getProjectedViewDescriptor() {
    return (IListViewDescriptor) super.getProjectedViewDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MobileFilterableBeanCollectionModule clone() {
    MobileFilterableBeanCollectionModule clone = (MobileFilterableBeanCollectionModule) super.clone();
    return clone;
  }

  /**
   * Mobile filterable bean collection module views only support page views as element views
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IMobilePageSectionViewDescriptor getElementViewDescriptor() {
    IMobilePageSectionViewDescriptor elementViewDescriptor = (IMobilePageSectionViewDescriptor) super
        .getElementViewDescriptor();
    if (elementViewDescriptor == null) {
      elementViewDescriptor = new MobileComponentViewDescriptor();
      ((BasicViewDescriptor) elementViewDescriptor).setModelDescriptor(getElementComponentDescriptor());
      setElementViewDescriptor(elementViewDescriptor);
    }
    return elementViewDescriptor;
  }

  /**
   * Mobile filterable bean collection module views only support page views as element views
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setElementViewDescriptor(IViewDescriptor elementViewDescriptor) {
    if (!(elementViewDescriptor instanceof IMobilePageSectionViewDescriptor)) {
      throw new IllegalArgumentException(
          "Mobile filterable bean collection module views only support page views as element views and not :"
              + elementViewDescriptor.getClass().getSimpleName());
    }
    super.setElementViewDescriptor(elementViewDescriptor);
  }

  /**
   * Mobile filterable bean collection module views only support mobile component views as filter views
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setFilterViewDescriptor(IViewDescriptor filterViewDescriptor) {
    if (!(filterViewDescriptor instanceof MobileComponentViewDescriptor
        || filterViewDescriptor instanceof MobileCompositePageViewDescriptor)) {
      throw new IllegalArgumentException(
          "Mobile filterable bean collection module views only support mobile component views or mobile composite "
              + "pages as filter views and not :" + filterViewDescriptor.getClass().getSimpleName());
    }
    super.setFilterViewDescriptor(filterViewDescriptor);
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setPaginationViewDescriptor(BasicViewDescriptor paginationViewDescriptor) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Gets query module filter action.
   *
   * @return the query module filter action
   */
  public IDisplayableAction getQueryModuleFilterAction() {
    return queryModuleFilterAction;
  }

  /**
   * Sets query module filter action.
   *
   * @param queryModuleFilterAction the query module filter action
   */
  public void setQueryModuleFilterAction(IDisplayableAction queryModuleFilterAction) {
    this.queryModuleFilterAction = queryModuleFilterAction;
  }
}
