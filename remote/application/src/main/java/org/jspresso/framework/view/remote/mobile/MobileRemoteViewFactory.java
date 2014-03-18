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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RCardContainer;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RList;
import org.jspresso.framework.gui.remote.RTabContainer;
import org.jspresso.framework.gui.remote.RTree;
import org.jspresso.framework.gui.remote.mobile.RMobileCardPage;
import org.jspresso.framework.gui.remote.mobile.RMobileCompositePage;
import org.jspresso.framework.gui.remote.mobile.RMobileList;
import org.jspresso.framework.gui.remote.mobile.RMobileNavPage;
import org.jspresso.framework.gui.remote.mobile.RMobilePage;
import org.jspresso.framework.gui.remote.mobile.RMobilePageAware;
import org.jspresso.framework.gui.remote.mobile.RMobilePageAwareContainer;
import org.jspresso.framework.gui.remote.mobile.RMobileTree;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageAware;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageSectionViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobileViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCardPageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCompositePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileListViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileNavPageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileTreeViewDescriptor;
import org.jspresso.framework.view.remote.AbstractRemoteViewFactory;

/**
 * Factory for mobile remote views.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision: 1463 $
 */
@SuppressWarnings("UnusedParameters")
public class MobileRemoteViewFactory extends AbstractRemoteViewFactory {

  private IDisplayableAction editPageAction;
  private IDisplayableAction savePageAction;
  private IDisplayableAction cancelPageAction;

  /**
   * Checks that the view descriptor is mobile compatible.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IView<RComponent> createView(IViewDescriptor viewDescriptor, IActionHandler actionHandler, Locale locale) {
    if (viewDescriptor instanceof IMobileViewDescriptor || viewDescriptor instanceof IPropertyViewDescriptor
        || viewDescriptor instanceof ITreeViewDescriptor || viewDescriptor instanceof IListViewDescriptor
        || viewDescriptor instanceof ICardViewDescriptor || viewDescriptor instanceof IComponentViewDescriptor) {
      return super.createView(viewDescriptor, actionHandler, locale);
    }
    throw new IllegalArgumentException(
        "Mobile view factory can only handle mobile view descriptors and not : " + viewDescriptor.getClass()
                                                                                                 .getSimpleName());
  }

  /**
   * Checks that the view descriptor is mobile compatible.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createCompositeView(ICompositeViewDescriptor viewDescriptor,
                                                           IActionHandler actionHandler, Locale locale) {
    if (viewDescriptor instanceof IMobileViewDescriptor) {
      ICompositeView<RComponent> view = null;
      if (viewDescriptor instanceof IMobilePageViewDescriptor) {
        if (viewDescriptor instanceof MobileCompositePageViewDescriptor) {
          view = createMobileCompositePageView((MobileCompositePageViewDescriptor) viewDescriptor, actionHandler,
              locale);
        } else if (viewDescriptor instanceof MobileNavPageViewDescriptor) {
          view = createMobileNavPageView((MobileNavPageViewDescriptor) viewDescriptor, actionHandler, locale);
        } else if (viewDescriptor instanceof MobileCardPageViewDescriptor) {
          view = createMobileCardPageView((MobileCardPageViewDescriptor) viewDescriptor, actionHandler, locale);
        }
      } else if (viewDescriptor instanceof MobileBorderViewDescriptor) {
        view = createBorderView((MobileBorderViewDescriptor) viewDescriptor, actionHandler, locale);
      }
      if (view != null
          && view.getDescriptor() instanceof IMobilePageAware) {
        if (!(view.getPeer() instanceof RMobilePageAware)) {
          RMobilePageAwareContainer wrapper = createRMobilePageAwareContainer();
          wrapper.setContent(view.getPeer());
          view.setPeer(wrapper);
        }
        ((RMobilePageAware) view.getPeer()).setEnterAction(getActionFactory().createAction(
            ((IMobilePageAware) viewDescriptor).getEnterAction(), actionHandler, view, locale));
        ((RMobilePageAware) view.getPeer()).setBackAction(getActionFactory().createAction(
            ((IMobilePageAware) viewDescriptor).getBackAction(), actionHandler, view, locale));
        ((RMobilePageAware) view.getPeer()).setMainAction(getActionFactory().createAction(
            ((IMobilePageAware) viewDescriptor).getMainAction(), actionHandler, view, locale));
        ((RMobilePageAware) view.getPeer()).setPageEndAction(getActionFactory().createAction(
            ((IMobilePageAware) viewDescriptor).getPageEndAction(), actionHandler, view, locale));
      }
      bindCompositeView(view);
      return view;
    }
    throw new IllegalArgumentException(
        "Mobile view factory can only handle mobile view descriptors and not : " + viewDescriptor.getClass()
                                                                                                 .getSimpleName());
  }

  /**
   * Create mobile card page view.
   *
   * @param viewDescriptor
   *     the view descriptor
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   * @return the i composite view
   */
  protected ICompositeView<RComponent> createMobileCardPageView(MobileCardPageViewDescriptor viewDescriptor,
                                                                IActionHandler actionHandler, Locale locale) {
    RMobileCardPage viewComponent = createRMobileCardPage(viewDescriptor);
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();
    if (viewDescriptor.getPages() != null) {
      IView<RComponent> pagesView = createView(viewDescriptor.getPages(), actionHandler, locale);
      viewComponent.setPages((RCardContainer) pagesView.getPeer());
      childrenViews.add(pagesView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * Create mobile nav page view.
   *
   * @param viewDescriptor
   *     the view descriptor
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   * @return the i composite view
   */
  protected ICompositeView<RComponent> createMobileNavPageView(MobileNavPageViewDescriptor viewDescriptor,
                                                               IActionHandler actionHandler, Locale locale) {
    RMobileNavPage viewComponent = createRMobileNavPage(viewDescriptor);
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();
    if (viewDescriptor.getHeaderView() != null) {
      IView<RComponent> headerView = createView(viewDescriptor.getHeaderView(), actionHandler, locale);
      viewComponent.setHeaderView(headerView.getPeer());
      childrenViews.add(headerView);
    }
    if (viewDescriptor.getSelectionView() != null) {
      IView<RComponent> selectionView = createView(viewDescriptor.getSelectionView(), actionHandler, locale);
      viewComponent.setSelectionView(selectionView.getPeer());
      childrenViews.add(selectionView);
      IValueConnector selectionViewConnector = selectionView.getConnector();
      if (viewDescriptor.getNextPage() != null) {
        IView<RComponent> nextPageView = createView(viewDescriptor.getNextPage(), actionHandler, locale);
        viewComponent.setNextPage((RMobilePage) nextPageView.getPeer());
        if (selectionViewConnector != null) {
          getModelCascadingBinder().bind(selectionViewConnector, nextPageView.getConnector());
        }
        nextPageView.setParent(selectionView);
      }
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * Create mobile composite page view.
   *
   * @param viewDescriptor
   *     the view descriptor
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   * @return the i composite view
   */
  protected ICompositeView<RComponent> createMobileCompositePageView(MobileCompositePageViewDescriptor viewDescriptor,
                                                                     IActionHandler actionHandler, Locale locale) {
    RMobileCompositePage viewComponent = createRMobileCompositePage(viewDescriptor);
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();
    List<RComponent> pageSections = new ArrayList<>();
    for (IMobilePageSectionViewDescriptor pageSectionViewDescriptor : viewDescriptor.getPageSections()) {
      IView<RComponent> pageSectionView = createView(pageSectionViewDescriptor, actionHandler, locale);
      pageSections.add(pageSectionView.getPeer());
      childrenViews.add(pageSectionView);
    }
    viewComponent.setPageSections(pageSections.toArray(new RComponent[pageSections.size()]));
    if (!viewDescriptor.isInlineEditing()) {
      ICompositeView<RComponent> editorPageView = (ICompositeView<RComponent>) createView(
          viewDescriptor.cloneEditable(), actionHandler, locale);
      RMobileCompositePage editorPage = (RMobileCompositePage) editorPageView.getView().getPeer();
      RAction saveAction = getActionFactory().createAction(getSavePageAction(), actionHandler, view, locale);
      editorPage.setMainAction(saveAction);
      RAction cancelAction = getActionFactory().createAction(getCancelPageAction(), actionHandler, view, locale);
      editorPage.setBackAction(cancelAction);
      viewComponent.setEditorPage(editorPage);
      RAction editorAction = getActionFactory().createAction(getEditPageAction(), actionHandler, view, locale);
      viewComponent.setEditAction(editorAction);
      childrenViews.add(editorPageView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private RMobileCompositePage createRMobileCompositePage(MobileCompositePageViewDescriptor viewDescriptor) {
    return new RMobileCompositePage(getGuidGenerator().generateGUID());
  }

  private RMobileNavPage createRMobileNavPage(MobileNavPageViewDescriptor viewDescriptor) {
    return new RMobileNavPage(getGuidGenerator().generateGUID());
  }

  private RMobileCardPage createRMobileCardPage(MobileCardPageViewDescriptor viewDescriptor) {
    return new RMobileCardPage(getGuidGenerator().generateGUID());
  }

  private RMobilePageAwareContainer createRMobilePageAwareContainer() {
    return new RMobilePageAwareContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Completes with showArrow property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createListView(IListViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    IView<RComponent> view = super.createListView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileListViewDescriptor) {
      ((RMobileList) view.getPeer()).setShowArrow(((MobileListViewDescriptor) viewDescriptor).isShowArrow());
    }
    return view;
  }

  /**
   * Creates a mobile list.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected RList createRList(IListViewDescriptor viewDescriptor) {
    RMobileList component = new RMobileList(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Completes with showArrow property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTreeView(ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    IView<RComponent> view = super.createTreeView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileTreeViewDescriptor) {
      ((RMobileTree) view.getPeer()).setShowArrow(((MobileTreeViewDescriptor) viewDescriptor).isShowArrow());
    }
    return view;
  }

  /**
   * Creates a mobile tree.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected RTree createRTree(ITreeViewDescriptor viewDescriptor) {
    RMobileTree component = new RMobileTree(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected RComponent decorateWithPaginationView(RComponent viewPeer, RComponent paginationViewPeer) {
    // pagination through pagination view is not supported in mobile
    return viewPeer;
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTableView(ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                              Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createEvenGridView(IEvenGridViewDescriptor viewDescriptor,
                                                          IActionHandler actionHandler, Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createTabView(ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                     Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createConstrainedGridView(IConstrainedGridViewDescriptor viewDescriptor,
                                                                 IActionHandler actionHandler, Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createSplitView(ISplitViewDescriptor viewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p/>
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
   * <p/>
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
   * <p/>
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
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected void completePropertyViewsWithDynamicForegrounds(ICompositeValueConnector connector,
                                                             List<IView<RComponent>> propertyViews,
                                                             IComponentDescriptor<?> modelDescriptor) {
    // Not supported in mobile environments.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void selectChildViewIndex(RComponent viewComponent, int index) {
    if (viewComponent instanceof RTabContainer) {
      RTabContainer rTab = ((RTabContainer) viewComponent);
      if (rTab.getSelectedIndex() != index) {
        rTab.setSelectedIndex(index);

        RemoteSelectionCommand selectionCommand = new RemoteSelectionCommand();
        selectionCommand.setTargetPeerGuid(rTab.getGuid());
        selectionCommand.setLeadingIndex(index);
        getRemoteCommandHandler().registerCommand(selectionCommand);
      }
    }
  }

  /**
   * Gets edit page action.
   *
   * @return the edit page action
   */
  protected IDisplayableAction getEditPageAction() {
    return editPageAction;
  }

  /**
   * Sets edit page action.
   *
   * @param editPageAction
   *     the edit page action
   */
  public void setEditPageAction(IDisplayableAction editPageAction) {
    this.editPageAction = editPageAction;
  }

  /**
   * Gets save page action.
   *
   * @return the save page action
   */
  protected IDisplayableAction getSavePageAction() {
    return savePageAction;
  }

  /**
   * Sets save page action.
   *
   * @param savePageAction the save page action
   */
  public void setSavePageAction(IDisplayableAction savePageAction) {
    this.savePageAction = savePageAction;
  }

  /**
   * Gets cancel page action.
   *
   * @return the cancel page action
   */
  protected IDisplayableAction getCancelPageAction() {
    return cancelPageAction;
  }

  /**
   * Sets cancel page action.
   *
   * @param cancelPageAction the cancel page action
   */
  public void setCancelPageAction(IDisplayableAction cancelPageAction) {
    this.cancelPageAction = cancelPageAction;
  }
}
