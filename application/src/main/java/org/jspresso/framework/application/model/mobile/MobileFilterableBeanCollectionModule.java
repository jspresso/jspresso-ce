/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractQbeAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.FilterableBeanCollectionModuleDescriptor;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
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
 * @version $LastChangedRevision$
 */
public class MobileFilterableBeanCollectionModule extends FilterableBeanCollectionModule {

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
        MobileBeanCollectionModule.MODULE_OBJECTS));
    MobileNavPageViewDescriptor moduleObjectsPageView = new MobileNavPageViewDescriptor();
    moduleObjectsPageView.setSelectionView(moduleObjectsView);
    if (getPagingAction() != null) {
      moduleObjectsPageView.setPageEndAction(new FrontendAction() {
        @Override
        public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
          try {
            context.put(AbstractQbeAction.PAGINATE, null);
            IPageable pageableModel = getModel(context);
            pageableModel.setStickyResults(pageableModel.getResults());
            if (pageableModel.getPage() != null) {
              pageableModel.setPage(pageableModel.getPage() + 1);
            } else {
              pageableModel.setPage(1);
            }
            return actionHandler.execute(getPagingAction(), context);
          } finally {
            context.remove(AbstractQbeAction.PAGINATE);
          }
        }
      });
    }
    IMobilePageViewDescriptor nextPage;
    if (getElementViewDescriptor() instanceof IMobilePageViewDescriptor) {
      nextPage = (IMobilePageViewDescriptor) getElementViewDescriptor();
    } else {
      nextPage = new MobileCompositePageViewDescriptor();
      ((MobileCompositePageViewDescriptor) nextPage).setPageSections(Collections.singletonList(
          getElementViewDescriptor()));
    }
    moduleObjectsPageView.setNextPage(nextPage);
    moduleObjectsPageView.setModelDescriptor(moduleDescriptor);

    IComponentDescriptor<IComponent> realComponentDesc = getFilterComponentDescriptor();
    MobileComponentViewDescriptor filterViewDesc = getFilterViewDescriptor();
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
    filterViewDesc.setBorderType(EBorderType.TITLED);
    filterViewDesc.setModelDescriptor(filterModelDescriptorProvider);
    if (customFilterView) {
      getQueryViewDescriptorFactory().adaptExistingViewDescriptor(filterViewDesc);
    }

    List<IMobilePageSectionViewDescriptor> sections = new ArrayList<>();
    sections.add(filterViewDesc);
    sections.add(moduleObjectsPageView);
    MobileCompositePageViewDescriptor moduleViewDescriptor = new MobileCompositePageViewDescriptor();
    moduleViewDescriptor.setInlineEditing(true);
    moduleViewDescriptor.setI18nNameKey(getName());
    moduleViewDescriptor.setPageSections(sections);

    if (getPageSize() != null && getPageSize() > 0) {
      if (moduleObjectsView != null && moduleObjectsView.getPaginationViewDescriptor() == null
          && moduleObjectsView instanceof BasicListViewDescriptor) {
        ((BasicListViewDescriptor) moduleObjectsView).setPaginationViewDescriptor(getPaginationViewDescriptor());
      }
    }
    moduleViewDescriptor.setModelDescriptor(moduleDescriptor);
    return moduleViewDescriptor;
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
    if (!(filterViewDescriptor instanceof MobileComponentViewDescriptor)) {
      throw new IllegalArgumentException(
          "Mobile filterable bean collection module views only support mobile component views as filter views "
              + "and not :" + filterViewDescriptor.getClass().getSimpleName());
    }
    super.setFilterViewDescriptor(filterViewDescriptor);
  }

  /**
   * Mobile filterable bean collection module views only support mobile component views as filter views
   * descriptors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public MobileComponentViewDescriptor getFilterViewDescriptor() {
    return (MobileComponentViewDescriptor) super.getFilterViewDescriptor();
  }
}
