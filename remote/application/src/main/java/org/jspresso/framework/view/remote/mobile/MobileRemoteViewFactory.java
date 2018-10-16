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
package org.jspresso.framework.view.remote.mobile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.mobile.NearElementAction;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionComponent;
import org.jspresso.framework.gui.remote.RBorderContainer;
import org.jspresso.framework.gui.remote.RCardContainer;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.REvenGridContainer;
import org.jspresso.framework.gui.remote.RForm;
import org.jspresso.framework.gui.remote.RImageComponent;
import org.jspresso.framework.gui.remote.RList;
import org.jspresso.framework.gui.remote.RMap;
import org.jspresso.framework.gui.remote.RRepeater;
import org.jspresso.framework.gui.remote.RTabContainer;
import org.jspresso.framework.gui.remote.RTree;
import org.jspresso.framework.gui.remote.mobile.RImageCanvas;
import org.jspresso.framework.gui.remote.mobile.RImagePicker;
import org.jspresso.framework.gui.remote.mobile.RMobileActionComponent;
import org.jspresso.framework.gui.remote.mobile.RMobileBorderContainer;
import org.jspresso.framework.gui.remote.mobile.RMobileCardContainer;
import org.jspresso.framework.gui.remote.mobile.RMobileCardPage;
import org.jspresso.framework.gui.remote.mobile.RMobileCompositePage;
import org.jspresso.framework.gui.remote.mobile.RMobileEvenGridContainer;
import org.jspresso.framework.gui.remote.mobile.RMobileForm;
import org.jspresso.framework.gui.remote.mobile.RMobileImageComponent;
import org.jspresso.framework.gui.remote.mobile.RMobileList;
import org.jspresso.framework.gui.remote.mobile.RMobileMap;
import org.jspresso.framework.gui.remote.mobile.RMobileNavPage;
import org.jspresso.framework.gui.remote.mobile.RMobilePage;
import org.jspresso.framework.gui.remote.mobile.RMobilePageAware;
import org.jspresso.framework.gui.remote.mobile.RMobilePageAwareContainer;
import org.jspresso.framework.gui.remote.mobile.RMobileRepeater;
import org.jspresso.framework.gui.remote.mobile.RMobileTabContainer;
import org.jspresso.framework.gui.remote.mobile.RMobileTree;
import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IImageBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.server.remote.RemotePeerRegistryServlet;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IMapViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IRepeaterViewDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCardViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.AbstractMobilePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageAware;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobileViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCardPageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCardViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileCompositePageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileListViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileMapViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileNavPageViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileRepeaterViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileTabViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileTreeViewDescriptor;
import org.jspresso.framework.view.remote.AbstractRemoteViewFactory;

/**
 * Factory for mobile remote views.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public class MobileRemoteViewFactory extends AbstractRemoteViewFactory {

  private IDisplayableAction editPageAction;
  private IDisplayableAction savePageAction;
  private IDisplayableAction cancelPageAction;
  private IDisplayableAction nextElementAction;
  private IDisplayableAction previousElementAction;

  /**
   * Checks that the view descriptor is mobile compatible.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IView<RComponent> createView(IViewDescriptor viewDescriptor, IActionHandler actionHandler, Locale locale) {
    if (viewDescriptor instanceof IMobileViewDescriptor || viewDescriptor instanceof IPropertyViewDescriptor
        || viewDescriptor instanceof ICardViewDescriptor) {
      IView<RComponent> view = super.createView(viewDescriptor, actionHandler, locale);
      RComponent viewPeer = view.getPeer();
      if (viewDescriptor instanceof AbstractMobilePageViewDescriptor && viewPeer instanceof RMobilePage) {
        if (((AbstractMobilePageViewDescriptor) viewDescriptor).getI18nName() != null) {
          viewPeer.setLabel(((AbstractMobilePageViewDescriptor) viewDescriptor).getI18nName());
        }
        if (((AbstractMobilePageViewDescriptor) viewDescriptor).getI18nDescription() != null) {
          viewPeer.setToolTip(((AbstractMobilePageViewDescriptor) viewDescriptor).getI18nDescription());
        }
      }
      decorateWithPageAwareContainer(view, viewDescriptor, actionHandler, locale);
      return view;
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
                                                           final IActionHandler actionHandler, Locale locale) {
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
        if (view != null) {
          ((RMobilePage) view.getPeer()).setPosition(((IMobilePageViewDescriptor) viewDescriptor).getPosition().name());
        }
      } else if (viewDescriptor instanceof MobileBorderViewDescriptor) {
        view = createBorderView((MobileBorderViewDescriptor) viewDescriptor, actionHandler, locale);
      } else if (viewDescriptor instanceof MobileEvenGridViewDescriptor) {
        view = createEvenGridView((MobileEvenGridViewDescriptor) viewDescriptor, actionHandler, locale);
      } else if (viewDescriptor instanceof MobileTabViewDescriptor) {
        view = createTabView((MobileTabViewDescriptor) viewDescriptor, actionHandler, locale);
      }
      if (view.getConnector() == null) {
        bindCompositeView(view);
      }
      return view;
    }
    throw new IllegalArgumentException(
        "Mobile view factory can only handle mobile view descriptors and not : " + viewDescriptor.getClass()
                                                                                                 .getSimpleName());
  }

  private void decorateWithPageAwareContainer(IView<RComponent> view, IViewDescriptor viewDescriptor,
                                              IActionHandler actionHandler, Locale locale) {
    if (view != null && view.getDescriptor() instanceof IMobilePageAware) {
      if (!(view.getPeer() instanceof RMobilePageAware)) {
        RMobilePageAwareContainer wrapper = createRMobilePageAwareContainer();
        wrapper.setContent(view.getPeer());
        view.setPeer(wrapper);
      }
      if (((IMobilePageAware) viewDescriptor).getEnterAction() != null) {
        ((RMobilePageAware) view.getPeer()).setEnterAction(getActionFactory()
            .createAction(((IMobilePageAware) viewDescriptor).getEnterAction(), actionHandler, view, locale));
      }
      if (((IMobilePageAware) viewDescriptor).getBackAction() != null) {
        ((RMobilePageAware) view.getPeer()).setBackAction(getActionFactory()
            .createAction(((IMobilePageAware) viewDescriptor).getBackAction(), actionHandler, view, locale));
      }
      if (((IMobilePageAware) viewDescriptor).getMainAction() != null) {
        ((RMobilePageAware) view.getPeer()).setMainAction(getActionFactory()
            .createAction(((IMobilePageAware) viewDescriptor).getMainAction(), actionHandler, view, locale));
      }
      if (((IMobilePageAware) viewDescriptor).getPageEndAction() != null) {
        ((RMobilePageAware) view.getPeer()).setPageEndAction(getActionFactory()
            .createAction(((IMobilePageAware) viewDescriptor).getPageEndAction(), actionHandler, view, locale));
      }
      if (((IMobilePageAware) viewDescriptor).getSwipeLeftAction() != null) {
        ((RMobilePageAware) view.getPeer()).setSwipeLeftAction(getActionFactory()
            .createAction(((IMobilePageAware) viewDescriptor).getSwipeLeftAction(), actionHandler, view, locale));
      }
      if (((IMobilePageAware) viewDescriptor).getSwipeRightAction() != null) {
        ((RMobilePageAware) view.getPeer()).setSwipeRightAction(getActionFactory()
            .createAction(((IMobilePageAware) viewDescriptor).getSwipeRightAction(), actionHandler, view, locale));
      }
      if (((IMobilePageAware) viewDescriptor).getI18nHeader() != null) {
        ((RMobilePageAware) view.getPeer()).setHeaderText(((IMobilePageAware) viewDescriptor).getI18nHeader());
      }
    }
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
    if (viewDescriptor.getPagesCardViewDescriptor() != null) {
      IView<RComponent> pagesView = createView(viewDescriptor.getPagesCardViewDescriptor(), actionHandler, locale);
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
    MobileNavPageViewDescriptor editorPageDescriptor = null;
    IViewDescriptor selectionViewDescriptor = viewDescriptor.getSelectionViewDescriptor();
    if (selectionViewDescriptor != null) {
      if (!viewDescriptor.isReadOnly()) {
        ActionMap collectionBasedActionMap = filterActionMap(selectionViewDescriptor.getActionMap(), true,
            actionHandler);
        if (collectionBasedActionMap != null) {
          editorPageDescriptor = viewDescriptor.getEditorPage();
          selectionViewDescriptor = ((BasicViewDescriptor) selectionViewDescriptor).clone();
          ActionMap notCollectionBasedActionMap = filterActionMap(selectionViewDescriptor.getActionMap(), false,
              actionHandler);
          ((BasicViewDescriptor) selectionViewDescriptor).setActionMap(notCollectionBasedActionMap);
        }
      }
    }
    RMobileNavPage viewComponent = createRMobileNavPage(viewDescriptor);
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();
    List<RComponent> headerSections = new ArrayList<>();
    if (viewDescriptor.getHeaderSectionsDescriptors() != null) {
      for (IMobileViewDescriptor hsd : viewDescriptor.getHeaderSectionsDescriptors()) {
        try {
          actionHandler.pushToSecurityContext(hsd);
          if (actionHandler.isAccessGranted(hsd) && isAllowedForClientType(hsd, actionHandler)) {
            IView<RComponent> headerSection = createView(hsd, actionHandler, locale);
            headerSections.add(headerSection.getPeer());
            childrenViews.add(headerSection);
          }
        } finally {
          actionHandler.restoreLastSecurityContextSnapshot();
        }
      }
    }
    viewComponent.setHeaderSections(headerSections.toArray(new RComponent[headerSections.size()]));
    if (selectionViewDescriptor != null) {
      IView<RComponent> selectionView = createView(selectionViewDescriptor, actionHandler, locale);
      if (selectionViewDescriptor.getName() == null) {
        // If not overriden, Navigation page title will be taken from the nav page title. see #514
        selectionView.getPeer().setLabel(null);
      }
      viewComponent.setSelectionView(selectionView.getPeer());
      childrenViews.add(selectionView);
      IValueConnector selectionViewConnector = selectionView.getConnector();
      IMobilePageViewDescriptor nextPageViewDescriptor = viewDescriptor.getNextPageViewDescriptor();
      if (nextPageViewDescriptor != null) {
        IView<RComponent> nextPageView = createView(nextPageViewDescriptor, actionHandler, locale);
        RMobilePage nextPage = (RMobilePage) nextPageView.getPeer();
        viewComponent.setNextPage(nextPage);
        if (selectionViewConnector != null) {
          IValueConnector detailConnector = nextPageView.getConnector();
          if (nextPageViewDescriptor.getModelDescriptor() instanceof IReferencePropertyDescriptor<?>) {
            ICompositeValueConnector wrapperConnector = getConnectorFactory().createCompositeValueConnector(
                ModelRefPropertyConnector.THIS_PROPERTY, null);
            wrapperConnector.addChildConnector(nextPageView.getConnector());
            detailConnector = wrapperConnector;
          }
          getModelCascadingBinder().bind(selectionViewConnector, detailConnector);
        }
        if (selectionViewConnector instanceof ICollectionConnector) {
          Map<String, Object> staticContext = new HashMap<>();
          staticContext.put(NearElementAction.NAVIGATION_CONNECTOR_KEY, selectionViewConnector);
          staticContext.put(NearElementAction.FETCH_ACTION_KEY, viewDescriptor.getPageEndAction());

          RAction swipeLeftAction = getActionFactory().createAction(getNextElementAction(), actionHandler, view,
              locale);
          swipeLeftAction.putValue(IAction.STATIC_CONTEXT_KEY, staticContext);
          nextPage.setSwipeLeftAction(swipeLeftAction);

          RAction swipeRightAction = getActionFactory().createAction(getPreviousElementAction(), actionHandler, view,
              locale);
          swipeRightAction.putValue(IAction.STATIC_CONTEXT_KEY, staticContext);
          nextPage.setSwipeRightAction(swipeRightAction);
        }
        nextPageView.setParent(selectionView);
      } else {
        if (selectionView.getPeer() instanceof RMobileTree) {
          ((RMobileTree) selectionView.getPeer()).setShowArrow(false);
        } else if (selectionView.getPeer() instanceof RMobileList) {
          ((RMobileList) selectionView.getPeer()).setShowArrow(false);
        }
      }
    }
    if (editorPageDescriptor != null) {
      ICompositeView<RComponent> editorPageView = (ICompositeView<RComponent>) createView(editorPageDescriptor,
          actionHandler, locale);
      RMobileNavPage editorPage = (RMobileNavPage) editorPageView.getView().getPeer();
      viewComponent.setEditorPage(editorPage);
      childrenViews.add(editorPageView);
    }
    view.setChildren(childrenViews);
    // Anticipate connector creation in order to have editor action correctly reacting to model change. see #391
    bindCompositeView(view);
    if (viewComponent.getEditorPage() != null) {
      final RAction editAction = getActionFactory().createAction(getEditPageAction(), actionHandler, view, locale);
      viewComponent.setEditAction(editAction);
      if (viewComponent.getMainAction() == null) {
        viewComponent.setMainAction(editAction);
      }
      editAction.setEnabled(view.getConnector().isWritable());
      view.getConnector().addPropertyChangeListener(IValueConnector.WRITABLE_PROPERTY, new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          getActionFactory().setActionEnabled(editAction, (Boolean) evt.getNewValue());
        }
      });
    }
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
    MobileCompositePageViewDescriptor filteredViewDescriptor = viewDescriptor.filterForReading();
    RMobileCompositePage viewComponent = createRMobileCompositePage(filteredViewDescriptor);
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, filteredViewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();
    List<RComponent> pageSections = new ArrayList<>();
    for (IMobileViewDescriptor pageSectionViewDescriptor : filteredViewDescriptor.getPageSectionDescriptors()) {
      try {
        actionHandler.pushToSecurityContext(pageSectionViewDescriptor);
        if (actionHandler.isAccessGranted(pageSectionViewDescriptor) && isAllowedForClientType(
            pageSectionViewDescriptor, actionHandler)) {
          IView<RComponent> pageSectionView = createView(pageSectionViewDescriptor, actionHandler, locale);
          RComponent peer = pageSectionView.getPeer();
          if (peer instanceof RMobileCompositePage
              && pageSectionViewDescriptor instanceof MobileCompositePageViewDescriptor
              && !pageSectionViewDescriptor.isReadOnly()) {
            if (((MobileCompositePageViewDescriptor) pageSectionViewDescriptor).isInlineEditing()) {
              if (((MobileCompositePageViewDescriptor) pageSectionViewDescriptor).getMainAction() == null) {
                ((RMobileCompositePage) peer).setMainAction(null);
              }
              if (((MobileCompositePageViewDescriptor) pageSectionViewDescriptor).getBackAction() == null) {
                ((RMobileCompositePage) peer).setBackAction(null);
              }
            } else {
              if (((MobileCompositePageViewDescriptor) pageSectionViewDescriptor).getMainAction() == null) {
                ((RMobileCompositePage) peer).getEditorPage().setMainAction(null);
              }
              if (((MobileCompositePageViewDescriptor) pageSectionViewDescriptor).getBackAction() == null) {
                ((RMobileCompositePage) peer).getEditorPage().setBackAction(null);
              }
            }
          }
          pageSections.add(peer);
          childrenViews.add(pageSectionView);
        }
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
    viewComponent.setPageSections(pageSections.toArray(new RComponent[pageSections.size()]));
    if (!viewDescriptor.isReadOnly()) {
      RAction saveAction = getActionFactory().createAction(getSavePageAction(), actionHandler, view, locale);
      RAction cancelAction = getActionFactory().createAction(getCancelPageAction(), actionHandler, view, locale);
      if (viewDescriptor.isInlineEditing()) {
        if (viewComponent.getMainAction() == null) {
          viewComponent.setMainAction(saveAction);
        }
        viewComponent.setBackAction(cancelAction);
      } else {
        MobileCompositePageViewDescriptor filteredEditorPage = viewDescriptor.getEditorPage();
        ICompositeView<RComponent> editorPageView = (ICompositeView<RComponent>) createView(filteredEditorPage,
            actionHandler, locale);
        RMobileCompositePage editorPage = (RMobileCompositePage) editorPageView.getView().getPeer();
        editorPage.setMainAction(saveAction);
        editorPage.setBackAction(cancelAction);
        viewComponent.setEditorPage(editorPage);
        childrenViews.add(editorPageView);
      }
    }
    view.setChildren(childrenViews);
    // Anticipate connector creation in order to have editor action correctly reacting to model change. see #391
    bindCompositeView(view);
    if (viewComponent.getEditorPage() != null) {
      final RAction editAction = getActionFactory().createAction(getEditPageAction(), actionHandler, view, locale);
      viewComponent.setEditAction(editAction);
      if (viewComponent.getMainAction() == null) {
        viewComponent.setMainAction(editAction);
      }
      editAction.setEnabled(view.getConnector().isWritable());
      view.getConnector().addPropertyChangeListener(IValueConnector.WRITABLE_PROPERTY, new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          getActionFactory().setActionEnabled(editAction, (Boolean) evt.getNewValue());
        }
      });
    }
    for (IView<RComponent> child : view.getChildren()) {
      IComponentDescriptor<?> componentDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor
          .getModelDescriptor()).getComponentDescriptor();
      if (child.getPeer() instanceof RMobileNavPage) {
        IPropertyDescriptor selectionModelDescriptor = (IPropertyDescriptor) ((MobileNavPageViewDescriptor) child
            .getDescriptor()).getSelectionViewDescriptor().getModelDescriptor();
        completePageWithDynamicLabels((ICompositeValueConnector) view.getConnector(), child, componentDescriptor,
            selectionModelDescriptor);
      } else if (child.getPeer() instanceof RMobileCompositePage) {
        IModelDescriptor sectionModelDescriptor = child.getDescriptor().getModelDescriptor();
        if (sectionModelDescriptor instanceof IPropertyDescriptor) {
          completePageWithDynamicLabels((ICompositeValueConnector) view.getConnector(), child, componentDescriptor,
              (IPropertyDescriptor) sectionModelDescriptor);
        } else {
          completePageWithDynamicLabels((ICompositeValueConnector) view.getConnector(), child, componentDescriptor,
              null);
        }
      }
    }
    return view;
  }

  private RMobileCompositePage createRMobileCompositePage(MobileCompositePageViewDescriptor viewDescriptor) {
    RMobileCompositePage mobileCompositePage = new RMobileCompositePage(getGuidGenerator().generateGUID());
    return mobileCompositePage;
  }

  private RMobileNavPage createRMobileNavPage(MobileNavPageViewDescriptor viewDescriptor) {
    RMobileNavPage mobileNavPage = new RMobileNavPage(getGuidGenerator().generateGUID());
    return mobileNavPage;
  }

  private RMobileCardPage createRMobileCardPage(MobileCardPageViewDescriptor viewDescriptor) {
    RMobileCardPage mobileCardPage = new RMobileCardPage(getGuidGenerator().generateGUID());
    return mobileCardPage;
  }

  private RMobilePageAwareContainer createRMobilePageAwareContainer() {
    RMobilePageAwareContainer mobilePageAwareContainer = new RMobilePageAwareContainer(
        getGuidGenerator().generateGUID());
    return mobilePageAwareContainer;
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
      ((RMobileList) view.getPeer()).setPosition(((MobileListViewDescriptor) viewDescriptor).getPosition().name());
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createRepeaterView(IRepeaterViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                 Locale locale) {
    IView<RComponent> view = super.createRepeaterView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileRepeaterViewDescriptor) {
      ((RMobileRepeater) view.getPeer()).setPosition(
          ((MobileRepeaterViewDescriptor) viewDescriptor).getPosition().name());
    }
    return view;
  }

  /**
   * Completes with horizontal position property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createComponentView(IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                  Locale locale) {
    IView<RComponent> view = super.createComponentView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileComponentViewDescriptor) {
      ((RMobileForm) view.getPeer()).setPosition(((MobileComponentViewDescriptor) viewDescriptor).getPosition().name());
    }
    return view;
  }

  /**
   * Completes with horizontal position property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createMapView(IMapViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                            Locale locale) {
    IView<RComponent> view = super.createMapView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileMapViewDescriptor) {
      ((RMobileMap) view.getPeer()).setPosition(((MobileMapViewDescriptor) viewDescriptor).getPosition().name());
      ((RMobileMap) view.getPeer()).setInline(((MobileMapViewDescriptor) viewDescriptor).isInline());
    }
    return view;
  }

  /**
   * Completes with horizontal position property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createBorderView(IBorderViewDescriptor viewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    ICompositeView<RComponent> borderView = super.createBorderView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileBorderViewDescriptor) {
      ((RMobileBorderContainer) borderView.getPeer()).setPosition(
          ((MobileBorderViewDescriptor) viewDescriptor).getPosition().name());
    }
    return borderView;
  }

  /**
   * Completes with horizontal position property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createEvenGridView(IEvenGridViewDescriptor viewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    ICompositeView<RComponent> evenGridView = super.createEvenGridView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileEvenGridViewDescriptor) {
      ((RMobileEvenGridContainer) evenGridView.getPeer()).setPosition(
          ((MobileEvenGridViewDescriptor) viewDescriptor).getPosition().name());
    }
    return evenGridView;
  }

  /**
   * Completes with horizontal position property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createCardView(ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    IView<RComponent> cardView = super.createCardView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileCardViewDescriptor) {
      ((RMobileCardContainer) cardView.getPeer()).setPosition(
          ((MobileCardViewDescriptor) viewDescriptor).getPosition().name());
    }
    return cardView;
  }

  /**
   * Creates a mobile list.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected RList createRList(IListViewDescriptor viewDescriptor) {
    RMobileList mobileList = new RMobileList(getGuidGenerator().generateGUID());
    return mobileList;
  }

  /**
   * Creates a remote button component.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RActionComponent createRActionComponent(IActionViewDescriptor viewDescriptor) {
    RMobileActionComponent component = new RMobileActionComponent(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a mobile repeater.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected RRepeater createRRepeater(IRepeaterViewDescriptor viewDescriptor) {
    RMobileRepeater mobileRepeater = new RMobileRepeater(getGuidGenerator().generateGUID());
    return mobileRepeater;
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
      ((RMobileTree) view.getPeer()).setPosition(((MobileTreeViewDescriptor) viewDescriptor).getPosition().name());
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
    RMobileTree mobileTree = new RMobileTree(getGuidGenerator().generateGUID());
    return mobileTree;
  }

  /**
   * Creates a mobile tab view.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected RTabContainer createRTabContainer(ITabViewDescriptor viewDescriptor) {
    RMobileTabContainer mobileTabContainer = new RMobileTabContainer(getGuidGenerator().generateGUID());
    return mobileTabContainer;
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
   * Completes with carouselMode and horizontal position properties.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createTabView(ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                     Locale locale) {
    ICompositeView<RComponent> view = super.createTabView(viewDescriptor, actionHandler, locale);
    if (viewDescriptor instanceof MobileTabViewDescriptor) {
      ((RMobileTabContainer) view.getPeer()).setCarouselMode(
          ((MobileTabViewDescriptor) viewDescriptor).isCarouselMode());
      ((RMobileTabContainer) view.getPeer()).setPosition(
          ((MobileTabViewDescriptor) viewDescriptor).getPosition().name());
    }
    return view;
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
   * Complete page with dynamic tool tip and label.
   *
   * @param connector
   *     the connector
   * @param pageView
   *     the property view
   * @param modelDescriptor
   *     the model descriptor
   * @param propertyDescriptor
   *     the property descriptor
   */
  protected void completePageWithDynamicLabels(ICompositeValueConnector connector, IView<RComponent> pageView,
                                               IComponentDescriptor<?> modelDescriptor,
                                               IPropertyDescriptor propertyDescriptor) {
    IViewDescriptor viewDescriptor = pageView.getDescriptor();

    // Compute dynamic label
    String dynamicLabelProperty = computeDynamicLabelPropertyName(viewDescriptor, modelDescriptor, propertyDescriptor);
    // Dynamic label
    if (dynamicLabelProperty != null) {
      IValueConnector labelConnector = connector.getChildConnector(dynamicLabelProperty);
      if (labelConnector == null) {
        labelConnector = getConnectorFactory().createValueConnector(dynamicLabelProperty);
        connector.addChildConnector(dynamicLabelProperty, labelConnector);
      }
      if (labelConnector instanceof IRemoteStateOwner) {
        pageView.getPeer().setLabelState(((IRemoteStateOwner) labelConnector).getState());
      }
    }

    // Compute dynamic tooltip
    String dynamicToolTipProperty = computeDynamicToolTipPropertyName(viewDescriptor, modelDescriptor,
        propertyDescriptor);
    // Dynamic tooltip
    if (dynamicToolTipProperty != null) {
      IValueConnector tooltipConnector = connector.getChildConnector(dynamicToolTipProperty);
      if (tooltipConnector == null) {
        tooltipConnector = getConnectorFactory().createValueConnector(dynamicToolTipProperty);
        connector.addChildConnector(dynamicToolTipProperty, tooltipConnector);
      }
      if (tooltipConnector instanceof IRemoteStateOwner) {
        pageView.getPeer().setToolTipState(((IRemoteStateOwner) tooltipConnector).getState());
      }
    }
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected void completeViewWithDynamicToolTip(RComponent viewComponent, IViewDescriptor viewDescriptor,
                                                IComponentDescriptor<?> componentDescriptor,
                                                ICompositeValueConnector connectorToComplete) {
    // Not supported in mobile environments.
  }

  /**
   * Complete view with dynamic background.
   *
   * @param viewComponent
   *     the view component
   * @param viewDescriptor
   *     the view descriptor
   * @param componentDescriptor
   *     the component descriptor
   * @param connectorToComplete
   *     the connector to complete
   */
  protected void completeViewWithDynamicBackground(RComponent viewComponent, IViewDescriptor viewDescriptor,
                                                   IComponentDescriptor<?> componentDescriptor,
                                                   ICompositeValueConnector connectorToComplete) {
    // Not supported in mobile environments.
  }

  /**
   * Complete view with dynamic foreground.
   *
   * @param viewComponent
   *     the view component
   * @param viewDescriptor
   *     the view descriptor
   * @param componentDescriptor
   *     the component descriptor
   * @param connectorToComplete
   *     the connector to complete
   */
  protected void completeViewWithDynamicForeground(RComponent viewComponent, IViewDescriptor viewDescriptor,
                                                   IComponentDescriptor<?> componentDescriptor,
                                                   ICompositeValueConnector connectorToComplete) {
    // Not supported in mobile environments.
  }

  /**
   * Complete view with dynamic font.
   *
   * @param viewComponent
   *     the view component
   * @param viewDescriptor
   *     the view descriptor
   * @param componentDescriptor
   *     the component descriptor
   * @param connectorToComplete
   *     the connector to complete
   */
  protected void completeViewWithDynamicFont(RComponent viewComponent, IViewDescriptor viewDescriptor,
                                             IComponentDescriptor<?> componentDescriptor,
                                             ICompositeValueConnector connectorToComplete) {
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

  @Override
  protected IView<RComponent> createImagePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> imagePropertyView = super.createImagePropertyView(propertyViewDescriptor, actionHandler, locale);
    if (imagePropertyView.getPeer() instanceof RMobileImageComponent) {
      ((RMobileImageComponent) imagePropertyView.getPeer()).setSubmitUrl(
          RemotePeerRegistryServlet.computeUploadUrl(((IRemotePeer) imagePropertyView.getConnector()).getGuid()));
      Integer scaledWidth = ((IImageViewDescriptor) propertyViewDescriptor).getScaledWidth();
      Integer scaledHeight = ((IImageViewDescriptor) propertyViewDescriptor).getScaledHeight();
      if (scaledWidth == null) {
        if (scaledHeight == null) {
          scaledWidth = 300;
        } else {
          scaledWidth = scaledHeight;
        }
      }
      if (scaledHeight == null) {
        scaledHeight = scaledWidth;
      }
      ((RMobileImageComponent) imagePropertyView.getPeer()).setImageSize(new Dimension(scaledWidth, scaledHeight));
      ((RMobileImageComponent) imagePropertyView.getPeer()).setFormatName(
          ((IImageBinaryPropertyDescriptor) propertyViewDescriptor.getModelDescriptor()).getFormatName());
    }
    return imagePropertyView;
  }

  /**
   * Will create an image picker if the component is not read-only.
   * {@inheritDoc}
   */
  @Override
  protected RImageComponent createRImageComponent(IPropertyViewDescriptor viewDescriptor) {
    final IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) viewDescriptor.getModelDescriptor();
    if (viewDescriptor.isReadOnly() || !(propertyDescriptor instanceof IBinaryPropertyDescriptor)) {
      return super.createRImageComponent(viewDescriptor);
    } else {
      if (viewDescriptor instanceof IImageViewDescriptor && ((IImageViewDescriptor) viewDescriptor).isDrawable()) {
        return new RImageCanvas(getGuidGenerator().generateGUID());
      }
      return new RImagePicker(getGuidGenerator().generateGUID());
    }
  }

  /**
   * Creates a remote mobile form.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  @Override
  protected RForm createRForm(IComponentViewDescriptor viewDescriptor) {
    RMobileForm mobileForm = new RMobileForm(getGuidGenerator().generateGUID());
    return mobileForm;
  }

  /**
   * Creates a remote mobile map.
   *
   * @param viewDescriptor
   *     the map view descriptor.
   * @return the created remote component.
   */
  @Override
  protected RMap createRMap(IMapViewDescriptor viewDescriptor) {
    RMobileMap mobileMap = new RMobileMap(getGuidGenerator().generateGUID());
    return mobileMap;
  }

  /**
   * Creates a mobile remote border container.
   *
   * @param viewDescriptor
   *     the border view descriptor.
   * @return the created remote component.
   */
  @Override
  protected RBorderContainer createRBorderContainer(IBorderViewDescriptor viewDescriptor) {
    RMobileBorderContainer mobileBorderContainer = new RMobileBorderContainer(getGuidGenerator().generateGUID());
    return mobileBorderContainer;
  }

  /**
   * Creates a mobile remote even grid container.
   *
   * @param viewDescriptor
   *     the even grid view descriptor.
   * @return the created remote component.
   */
  @Override
  protected REvenGridContainer createREvenGridContainer(IEvenGridViewDescriptor viewDescriptor) {
    RMobileEvenGridContainer mobileEvenGridContainer = new RMobileEvenGridContainer(getGuidGenerator().generateGUID());
    return mobileEvenGridContainer;
  }

  /**
   * Creates a mobile remote card container.
   *
   * @param viewDescriptor
   *     the card view descriptor.
   * @return the created remote component.
   */
  @Override
  protected RCardContainer createRCardContainer(ICardViewDescriptor viewDescriptor) {
    if (viewDescriptor instanceof BasicCardViewDescriptor) {
      RMobileCardContainer mobileCardContainer = new RMobileCardContainer(getGuidGenerator().generateGUID());
      return mobileCardContainer;
    }
    return super.createRCardContainer(viewDescriptor);
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
   * @param savePageAction
   *     the save page action
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
   * @param cancelPageAction
   *     the cancel page action
   */
  public void setCancelPageAction(IDisplayableAction cancelPageAction) {
    this.cancelPageAction = cancelPageAction;
  }

  /**
   * Gets next element action.
   *
   * @return the next element action
   */
  protected IDisplayableAction getNextElementAction() {
    return nextElementAction;
  }

  /**
   * Sets next element action.
   *
   * @param nextElementAction
   *     the next element action
   */
  public void setNextElementAction(IDisplayableAction nextElementAction) {
    this.nextElementAction = nextElementAction;
  }

  /**
   * Gets previous element action.
   *
   * @return the previous element action
   */
  protected IDisplayableAction getPreviousElementAction() {
    return previousElementAction;
  }

  /**
   * Sets previous element action.
   *
   * @param previousElementAction
   *     the previous element action
   */
  public void setPreviousElementAction(IDisplayableAction previousElementAction) {
    this.previousElementAction = previousElementAction;
  }

  /**
   * Filter action map keeping only collectionBased or not collectionBased actions depending on the
   * keepCollectionBased parameter.
   *
   * @param actionMap
   *     the action map
   * @param keepCollectionBased
   *     the keep collection based
   * @param securityHandler
   *     the security handler
   * @return the action map
   */
  protected ActionMap filterActionMap(ActionMap actionMap, boolean keepCollectionBased,
                                      ISecurityHandler securityHandler) {
    ActionMap filteredActionMap = null;
    if (actionMap != null) {
      List<ActionList> filteredActionLists = null;
      for (ActionList actionList : actionMap.getActionLists(securityHandler)) {
        List<IDisplayableAction> filteredActions = null;
        for (IDisplayableAction action : actionList.getActions()) {
          if ((keepCollectionBased && action.isCollectionBased()) || (!keepCollectionBased && !action
              .isCollectionBased())) {
            if (filteredActions == null) {
              filteredActions = new ArrayList<>();
            }
            filteredActions.add(action);
          }
          if (filteredActions != null) {
            ActionList filteredActionList = actionList.clone();
            filteredActionList.setActions(filteredActions);
            if (filteredActionLists == null) {
              filteredActionLists = new ArrayList<>();
              filteredActionLists.add(filteredActionList);
            }
          }
        }
      }
      if (filteredActionLists != null) {
        filteredActionMap = actionMap.clone();
        filteredActionMap.setActionLists(filteredActionLists);
      }
    }
    return filteredActionMap;
  }
}
