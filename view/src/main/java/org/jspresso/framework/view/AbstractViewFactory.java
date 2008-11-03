/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IConnectorSelector;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.masterdetail.IModelCascadingBinder;
import org.jspresso.framework.binding.model.IModelValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.model.descriptor.EDuration;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IGridViewDescriptor;
import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;

/**
 * Abstract base class factory for views.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class AbstractViewFactory<E, F, G> implements
    IViewFactory<E, F, G> {

  /**
   * <code>DEF_DISP_MAX_FRACTION_DIGIT</code>.
   */
  protected static final int            DEF_DISP_MAX_FRACTION_DIGIT = 2;
  /**
   * <code>DEF_DISP_MAX_VALUE</code>.
   */
  protected static final double         DEF_DISP_MAX_VALUE          = 1000;
  /**
   * <code>DEF_DISP_TEMPLATE_PERCENT</code>.
   */
  protected static final double         DEF_DISP_TEMPLATE_PERCENT   = 99;
  /**
   * <code>TEMPLATE_CHAR</code>.
   */
  protected static final char           TEMPLATE_CHAR               = 'O';
  /**
   * <code>TEMPLATE_DATE</code>.
   */
  protected static final Date           TEMPLATE_DATE               = new Date(
                                                                        27166271000L);
  /**
   * <code>TEMPLATE_DURATION</code>.
   */
  protected static final Long           TEMPLATE_DURATION           = new Long(
                                                                        EDuration.ONE_SECOND
                                                                            .getMillis()
                                                                            + EDuration.ONE_MINUTE
                                                                                .getMillis()
                                                                            + EDuration.ONE_HOUR
                                                                                .getMillis()
                                                                            + EDuration.ONE_DAY
                                                                                .getMillis()
                                                                            + EDuration.ONE_WEEK
                                                                                .getMillis());
  /**
   * <code>TEMPLATE_TIME</code>.
   */
  protected static final Date           TEMPLATE_TIME               = new Date(
                                                                        366000);

  private ITranslationProvider          translationProvider;
  private IConfigurableConnectorFactory connectorFactory;
  private IMvcBinder                    mvcBinder;
  private IActionFactory<G, E>          actionFactory;
  private IIconFactory<F>               iconFactory;
  private IModelCascadingBinder         modelCascadingBinder;

  private IDisplayableAction            binaryPropertyInfoAction;
  private IDisplayableAction            lovAction;
  private IDisplayableAction            openFileAsBinaryPropertyAction;
  private IDisplayableAction            resetPropertyAction;
  private IDisplayableAction            saveBinaryPropertyAsFileAction;

  /**
   * Creates the connector for a tree view.
   * 
   * @param viewDescriptor
   *          the tree view descriptor.
   * @param locale
   *          the locale to use.
   * @return the connector for the tree view.
   */
  protected ICompositeValueConnector createTreeViewConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale) {
    ITreeLevelDescriptor rootDescriptor = viewDescriptor
        .getRootSubtreeDescriptor();
    ICompositeValueConnector connector = null;
    if (rootDescriptor instanceof ICompositeTreeLevelDescriptor) {
      IConfigurableCollectionConnectorListProvider compositeConnector = connectorFactory
          .createConfigurableCollectionConnectorListProvider(
              ModelRefPropertyConnector.THIS_PROPERTY,
              ((ICompositeTreeLevelDescriptor) rootDescriptor)
                  .getNodeGroupDescriptor().getRenderedProperty());
      List<ICollectionConnectorProvider> subtreeConnectors = new ArrayList<ICollectionConnectorProvider>();
      if (((ICompositeTreeLevelDescriptor) rootDescriptor)
          .getChildrenDescriptors() != null) {
        for (ITreeLevelDescriptor subtreeViewDescriptor : ((ICompositeTreeLevelDescriptor) rootDescriptor)
            .getChildrenDescriptors()) {
          ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
              viewDescriptor, locale, subtreeViewDescriptor, 1);
          compositeConnector.addChildConnector(subtreeConnector);
          subtreeConnectors.add(subtreeConnector);
        }
      }
      compositeConnector.setCollectionConnectorProviders(subtreeConnectors);
      connector = compositeConnector;
    } else if (rootDescriptor instanceof ISimpleTreeLevelDescriptor) {
      IConfigurableCollectionConnectorProvider simpleConnector = connectorFactory
          .createConfigurableCollectionConnectorProvider(
              ModelRefPropertyConnector.THIS_PROPERTY,
              ((ISimpleTreeLevelDescriptor) rootDescriptor)
                  .getNodeGroupDescriptor().getRenderedProperty());
      if (((ISimpleTreeLevelDescriptor) rootDescriptor).getChildDescriptor() != null) {
        ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
            viewDescriptor, locale,
            ((ISimpleTreeLevelDescriptor) rootDescriptor).getChildDescriptor(),
            1);
        simpleConnector.addChildConnector(subtreeConnector);
        simpleConnector.setCollectionConnectorProvider(subtreeConnector);
      }
      connector = simpleConnector;
    }

    if (connector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) connector)
          .setDisplayValue(viewDescriptor.getI18nName(translationProvider,
              locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayDescription(viewDescriptor.getI18nDescription(
              translationProvider, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayIconImageUrl(viewDescriptor.getIconImageURL());
      ((AbstractCompositeValueConnector) connector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }

    if (connector instanceof IConnectorSelector) {
      ((IConnectorSelector) connector).setTracksChildrenSelection(true);
    }
    return connector;
  }

  private ICollectionConnectorProvider createNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale,
      ITreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionConnectorProvider connector = null;
    if (subtreeViewDescriptor instanceof ICompositeTreeLevelDescriptor) {
      connector = createCompositeNodeGroupConnector(viewDescriptor, locale,
          (ICompositeTreeLevelDescriptor) subtreeViewDescriptor, depth);
    } else if (subtreeViewDescriptor instanceof ISimpleTreeLevelDescriptor) {
      connector = createSimpleNodeGroupConnector(viewDescriptor, locale,
          (ISimpleTreeLevelDescriptor) subtreeViewDescriptor, depth);
    }
    if (connector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) connector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(translationProvider, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(translationProvider, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayIconImageUrl(subtreeViewDescriptor
              .getNodeGroupDescriptor().getIconImageURL());
      ((AbstractCompositeValueConnector) connector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }
    return connector;
  }

  private ICollectionConnectorProvider createCompositeNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale,
      ICompositeTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionDescriptorProvider<?> nodeGroupModelDescriptor = ((ICollectionDescriptorProvider<?>) subtreeViewDescriptor
        .getNodeGroupDescriptor().getModelDescriptor());
    IConfigurableCollectionConnectorListProvider nodeGroupPrototypeConnector = connectorFactory
        .createConfigurableCollectionConnectorListProvider(
            nodeGroupModelDescriptor.getName() + "Element",
            subtreeViewDescriptor.getNodeGroupDescriptor()
                .getRenderedProperty());
    List<ICollectionConnectorProvider> subtreeConnectors = new ArrayList<ICollectionConnectorProvider>();
    if (subtreeViewDescriptor.getChildrenDescriptors() != null
        && depth < viewDescriptor.getMaxDepth()) {
      for (ITreeLevelDescriptor childDescriptor : subtreeViewDescriptor
          .getChildrenDescriptors()) {
        ICollectionConnectorProvider childConnector = createNodeGroupConnector(
            viewDescriptor, locale, childDescriptor, depth + 1);
        nodeGroupPrototypeConnector.addChildConnector(childConnector);
        subtreeConnectors.add(childConnector);
      }
    }
    nodeGroupPrototypeConnector
        .setCollectionConnectorProviders(subtreeConnectors);
    if (nodeGroupPrototypeConnector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayIconImageUrl(subtreeViewDescriptor
              .getNodeGroupDescriptor().getIconImageURL());
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }

    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  private ICollectionConnectorProvider createSimpleNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale,
      ISimpleTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionPropertyDescriptor<?> nodeGroupModelDescriptor = (ICollectionPropertyDescriptor<?>) subtreeViewDescriptor
        .getNodeGroupDescriptor().getModelDescriptor();
    IConfigurableCollectionConnectorProvider nodeGroupPrototypeConnector = connectorFactory
        .createConfigurableCollectionConnectorProvider(nodeGroupModelDescriptor
            .getName()
            + "Element", subtreeViewDescriptor.getNodeGroupDescriptor()
            .getRenderedProperty());
    if (subtreeViewDescriptor.getChildDescriptor() != null
        && depth < viewDescriptor.getMaxDepth()) {
      ICollectionConnectorProvider childConnector = createNodeGroupConnector(
          viewDescriptor, locale, subtreeViewDescriptor.getChildDescriptor(),
          depth + 1);
      nodeGroupPrototypeConnector.addChildConnector(childConnector);
      nodeGroupPrototypeConnector
          .setCollectionConnectorProvider(childConnector);
    }
    if (nodeGroupPrototypeConnector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayIconImageUrl(subtreeViewDescriptor
              .getNodeGroupDescriptor().getIconImageURL());
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  /**
   * Creates a table column connector.
   * 
   * @param columnId
   *          the column id to create the connector for.
   * @param descriptor
   *          the component descriptor this table relies on.
   * @return the connector for the table column.
   */
  protected IValueConnector createColumnConnector(String columnId,
      IComponentDescriptor<?> descriptor) {
    IPropertyDescriptor propertyDescriptor = descriptor
        .getPropertyDescriptor(columnId);
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + columnId + " defined for "
          + descriptor.getComponentContract());
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return getConnectorFactory().createCompositeValueConnector(
          columnId,
          ((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getToStringProperty());
    }
    return getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
  }

  /**
   * Creates a card view connector.
   * 
   * @param cardView
   *          the card view to create the connector for.
   * @param actionHandler
   *          the action handler.
   * @return the card view connector.
   */
  protected IValueConnector createCardViewConnector(final IMapView<E> cardView,
      final IActionHandler actionHandler) {
    IValueConnector cardViewConnector = getConnectorFactory()
        .createValueConnector(cardView.getDescriptor().getName());
    cardViewConnector
        .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

          public void connectorValueChange(ConnectorValueChangeEvent evt) {
            Object cardModel = evt.getNewValue();
            boolean accessGranted = true;
            if (cardModel instanceof ISecurable && actionHandler != null) {
              try {
                actionHandler.checkAccess((ISecurable) cardModel);
              } catch (SecurityException se) {
                accessGranted = false;
              }
            }
            E cardsPeer = cardView.getPeer();
            if (accessGranted) {
              String cardName = ((ICardViewDescriptor) cardView.getDescriptor())
                  .getCardNameForModel(cardModel);
              if (cardName != null) {
                IView<E> childCardView = cardView.getChild(cardName);
                if (childCardView != null) {
                  showCardInPanel(cardsPeer, cardName);
                  IValueConnector childCardConnector = childCardView
                      .getConnector();
                  if (childCardConnector != null) {
                    // To handle polymorphism, especially for modules, we refine
                    // the model descriptor.
                    if (((IModelValueConnector) cardView.getConnector()
                        .getModelConnector()).getModelDescriptor().getClass()
                        .isAssignableFrom(
                            childCardView.getDescriptor().getModelDescriptor()
                                .getClass())) {
                      ((IModelValueConnector) cardView.getConnector()
                          .getModelConnector())
                          .setModelDescriptor(childCardView.getDescriptor()
                              .getModelDescriptor());
                    }
                    getMvcBinder().bind(childCardConnector,
                        cardView.getConnector().getModelConnector());
                  }
                } else {
                  showCardInPanel(cardsPeer, ICardViewDescriptor.DEFAULT_CARD);
                }
              } else {
                showCardInPanel(cardsPeer, ICardViewDescriptor.DEFAULT_CARD);
              }
            } else {
              showCardInPanel(cardsPeer, ICardViewDescriptor.SECURITY_CARD);
            }
          }
        });
    return cardViewConnector;
  }

  /**
   * Shows a card in in card layouted panel.
   * 
   * @param cardsPeer
   *          the component that holds the cards
   * @param cardName
   *          the card identifier to show.
   */
  protected abstract void showCardInPanel(E cardsPeer, String cardName);

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  /**
   * Gets the connectorFactory.
   * 
   * @return the connectorFactory.
   */
  protected IConfigurableConnectorFactory getConnectorFactory() {
    return connectorFactory;
  }

  /**
   * Gets the mvcBinder.
   * 
   * @return the mvcBinder.
   */
  protected IMvcBinder getMvcBinder() {
    return mvcBinder;
  }

  /**
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Sets the connectorFactory.
   * 
   * @param connectorFactory
   *          the connectorFactory to set.
   */
  public void setConnectorFactory(IConfigurableConnectorFactory connectorFactory) {
    this.connectorFactory = connectorFactory;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  /**
   * Gets the actionFactory.
   * 
   * @return the actionFactory.
   */
  public IActionFactory<G, E> getActionFactory() {
    return actionFactory;
  }

  /**
   * Sets the actionFactory.
   * 
   * @param actionFactory
   *          the actionFactory to set.
   */
  public void setActionFactory(IActionFactory<G, E> actionFactory) {
    this.actionFactory = actionFactory;
  }

  /**
   * Sets the binaryPropertyInfoAction.
   * 
   * @param binaryPropertyInfoAction
   *          the binaryPropertyInfoAction to set.
   */
  public void setBinaryPropertyInfoAction(
      IDisplayableAction binaryPropertyInfoAction) {
    this.binaryPropertyInfoAction = binaryPropertyInfoAction;
  }

  /**
   * Sets the openFileAsBinaryPropertyAction.
   * 
   * @param openFileAsBinaryPropertyAction
   *          the openFileAsBinaryPropertyAction to set.
   */
  public void setOpenFileAsBinaryPropertyAction(
      IDisplayableAction openFileAsBinaryPropertyAction) {
    this.openFileAsBinaryPropertyAction = openFileAsBinaryPropertyAction;
  }

  /**
   * Sets the resetPropertyAction.
   * 
   * @param resetPropertyAction
   *          the resetPropertyAction to set.
   */
  public void setResetPropertyAction(IDisplayableAction resetPropertyAction) {
    this.resetPropertyAction = resetPropertyAction;
  }

  /**
   * Sets the saveBinaryPropertyAsFileAction.
   * 
   * @param saveBinaryPropertyAsFileAction
   *          the saveBinaryPropertyAsFileAction to set.
   */
  public void setSaveBinaryPropertyAsFileAction(
      IDisplayableAction saveBinaryPropertyAsFileAction) {
    this.saveBinaryPropertyAsFileAction = saveBinaryPropertyAsFileAction;
  }

  /**
   * Creates the action list for a binary property (open from file, save as
   * file, reset, size info).
   * 
   * @param viewComponent
   *          the component these actions will be triggered from.
   * @param connector
   *          the connector these actions will be triggered from.
   * @param propertyDescriptor
   *          the binary property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the action list.
   */
  protected List<G> createBinaryActions(E viewComponent,
      IValueConnector connector, IPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    G openAction = getActionFactory().createAction(
        openFileAsBinaryPropertyAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    G saveAction = getActionFactory().createAction(
        saveBinaryPropertyAsFileAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    G resetAction = getActionFactory().createAction(resetPropertyAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    G infoAction = getActionFactory().createAction(binaryPropertyInfoAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    List<G> binaryActions = new ArrayList<G>();
    binaryActions.add(openAction);
    binaryActions.add(saveAction);
    binaryActions.add(resetAction);
    binaryActions.add(infoAction);
    return binaryActions;
  }

  /**
   * Creates the list of value action.
   * 
   * @param viewComponent
   *          the component these actions will be triggered from.
   * @param connector
   *          the connector these actions will be triggered from.
   * @param propertyDescriptor
   *          the binary property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the generic list of value action.
   */
  protected G createLovAction(E viewComponent, IValueConnector connector,
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    G action = getActionFactory().createAction(lovAction, actionHandler,
        viewComponent, propertyDescriptor, connector, locale);
    return action;
  }

  /**
   * Sets the lovAction.
   * 
   * @param lovAction
   *          the lovAction to set.
   */
  public void setLovAction(IDisplayableAction lovAction) {
    this.lovAction = lovAction;
  }

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  public IIconFactory<F> getIconFactory() {
    return iconFactory;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<F> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the modelCascadingBinder.
   * 
   * @param modelCascadingBinder
   *          the modelCascadingBinder to set.
   */
  public void setModelCascadingBinder(IModelCascadingBinder modelCascadingBinder) {
    this.modelCascadingBinder = modelCascadingBinder;
  }

  
  /**
   * Gets the modelCascadingBinder.
   * 
   * @return the modelCascadingBinder.
   */
  protected IModelCascadingBinder getModelCascadingBinder() {
    return modelCascadingBinder;
  }

  /**
   * Creates a composite view.
   * 
   * @param viewDescriptor the view descriptor.
   * @param actionHandler the action handler
   * @param locale the locale.
   * @return the composite view.
   */
  protected ICompositeView<E> createCompositeView(
      ICompositeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<E> view = null;
    if (viewDescriptor instanceof IBorderViewDescriptor) {
      view = createBorderView((IBorderViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IGridViewDescriptor) {
      view = createGridView((IGridViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ISplitViewDescriptor) {
      view = createSplitView((ISplitViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITabViewDescriptor) {
      view = createTabView((ITabViewDescriptor) viewDescriptor, actionHandler,
          locale);
    }
    if (view != null) {
      if (viewDescriptor.isCascadingModels()) {
        IView<E> masterView = view.getChildren().get(0);
        view.setConnector(masterView.getConnector());
        for (int i = 1; i < view.getChildren().size(); i++) {
          IView<E> detailView = view.getChildren().get(i);
          detailView.setParent(view);

          IValueConnector detailConnector = null;
          if (detailView.getDescriptor().getModelDescriptor() instanceof IPropertyDescriptor) {
            IConfigurableCollectionConnectorProvider wrapper = getConnectorFactory()
                .createConfigurableCollectionConnectorProvider(
                    ModelRefPropertyConnector.THIS_PROPERTY, null);
            wrapper.addChildConnector(detailView.getConnector());
            if (detailView.getConnector() instanceof ICollectionConnector) {
              wrapper
                  .setCollectionConnectorProvider((ICollectionConnector) detailView
                      .getConnector());
            }
            detailConnector = wrapper;
          } else {
            detailConnector = detailView.getConnector();
          }
          getModelCascadingBinder().bind(masterView.getConnector(), detailConnector);
          masterView = detailView;
        }
      } else {
        String connectorId;
        if (viewDescriptor.getModelDescriptor() instanceof IPropertyDescriptor) {
          connectorId = viewDescriptor.getModelDescriptor().getName();
        } else {
          connectorId = ModelRefPropertyConnector.THIS_PROPERTY;
        }
        ICompositeValueConnector connector = getConnectorFactory()
            .createCompositeValueConnector(connectorId, null);
        view.setConnector(connector);
        for (IView<E> childView : view.getChildren()) {
          childView.setParent(view);
          connector.addChildConnector(childView.getConnector());
        }
      }
    }
    return view;
  }

  /**
   * Creates a tab view.
   * 
   * @param viewDescriptor the view descriptor.
   * @param actionHandler the action handler
   * @param locale the locale.
   * @return the tab view.
   */
  protected abstract ICompositeView<E> createTabView(ITabViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a split view.
   * 
   * @param viewDescriptor the view descriptor.
   * @param actionHandler the action handler
   * @param locale the locale.
   * @return the split view.
   */
  protected abstract ICompositeView<E> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a grid view.
   * 
   * @param viewDescriptor the view descriptor.
   * @param actionHandler the action handler
   * @param locale the locale.
   * @return the grid view.
   */
  protected abstract ICompositeView<E> createGridView(IGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a border view.
   * 
   * @param viewDescriptor the view descriptor.
   * @param actionHandler the action handler
   * @param locale the locale.
   * @return the border view.
   */
  protected abstract ICompositeView<E> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);
}
