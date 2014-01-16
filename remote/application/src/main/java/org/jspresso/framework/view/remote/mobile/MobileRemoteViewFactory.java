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
package org.jspresso.framework.view.remote.mobile;

import java.util.List;
import java.util.Locale;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.remote.AbstractRemoteViewFactory;

/**
 * Factory for mobile remote views.
 * 
 * @version $LastChangedRevision: 1463 $
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public class MobileRemoteViewFactory extends AbstractRemoteViewFactory {

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected RComponent decorateWithPaginationView(RComponent viewPeer, RComponent paginationViewPeer) {
    // pagination through pagination view is not supported in mobile
    return viewPeer;
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTreeView(ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createListView(IListViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTableView(ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                              Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createEvenGridView(IEvenGridViewDescriptor viewDescriptor,
                                                          IActionHandler actionHandler, Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createConstrainedGridView(IConstrainedGridViewDescriptor viewDescriptor,
                                                                 IActionHandler actionHandler, Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createSplitView(ISplitViewDescriptor viewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createBorderView(IBorderViewDescriptor viewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void completePropertyViewsWithDynamicToolTips(ICompositeValueConnector connector,
                                                          List<IView<RComponent>> propertyViews,
                                                          IComponentDescriptor<?> modelDescriptor) {
    // Not supported in mobile environments.
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void completePropertyViewsWithDynamicBackgrounds(ICompositeValueConnector connector,
                                                             List<IView<RComponent>> propertyViews,
                                                             IComponentDescriptor<?> modelDescriptor) {
    // Not supported in mobile environments.
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void completePropertyViewsWithDynamicFonts(ICompositeValueConnector connector,
                                                       List<IView<RComponent>> propertyViews,
                                                       IComponentDescriptor<?> modelDescriptor) {
    // Not supported in mobile environments.
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void completePropertyViewsWithDynamicForegrounds(ICompositeValueConnector connector,
                                                             List<IView<RComponent>> propertyViews,
                                                             IComponentDescriptor<?> modelDescriptor) {
    // Not supported in mobile environments.
  }

}
