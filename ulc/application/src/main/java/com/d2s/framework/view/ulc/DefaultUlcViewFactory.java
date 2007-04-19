/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.view.descriptor.basic.ModuleCardViewDescriptor;
import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConfigurableCollectionConnectorListProvider;
import com.d2s.framework.binding.IConfigurableCollectionConnectorProvider;
import com.d2s.framework.binding.IConfigurableConnectorFactory;
import com.d2s.framework.binding.IConnectorSelector;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.basic.BasicValueConnector;
import com.d2s.framework.binding.masterdetail.IMasterDetailBinder;
import com.d2s.framework.binding.model.ModelRefPropertyConnector;
import com.d2s.framework.binding.ulc.CollectionConnectorListModel;
import com.d2s.framework.binding.ulc.CollectionConnectorTableModel;
import com.d2s.framework.binding.ulc.ConnectorHierarchyTreeModel;
import com.d2s.framework.binding.ulc.ConnectorTreeHelper;
import com.d2s.framework.binding.ulc.IListSelectionModelBinder;
import com.d2s.framework.binding.ulc.ITreeSelectionModelBinder;
import com.d2s.framework.binding.ulc.ULCActionFieldConnector;
import com.d2s.framework.binding.ulc.ULCComboBoxConnector;
import com.d2s.framework.binding.ulc.ULCDateFieldConnector;
import com.d2s.framework.binding.ulc.ULCImageConnector;
import com.d2s.framework.binding.ulc.ULCJEditTextAreaConnector;
import com.d2s.framework.binding.ulc.ULCPasswordFieldConnector;
import com.d2s.framework.binding.ulc.ULCReferenceFieldConnector;
import com.d2s.framework.binding.ulc.ULCTextAreaConnector;
import com.d2s.framework.binding.ulc.ULCTextFieldConnector;
import com.d2s.framework.binding.ulc.ULCToggleButtonConnector;
import com.d2s.framework.gui.ulc.components.server.ITreePathPopupFactory;
import com.d2s.framework.gui.ulc.components.server.ULCActionField;
import com.d2s.framework.gui.ulc.components.server.ULCDateField;
import com.d2s.framework.gui.ulc.components.server.ULCDurationDataType;
import com.d2s.framework.gui.ulc.components.server.ULCDurationDataTypeFactory;
import com.d2s.framework.gui.ulc.components.server.ULCExtendedButton;
import com.d2s.framework.gui.ulc.components.server.ULCExtendedTable;
import com.d2s.framework.gui.ulc.components.server.ULCExtendedTree;
import com.d2s.framework.gui.ulc.components.server.ULCJEditTextArea;
import com.d2s.framework.gui.ulc.components.server.ULCOnFocusSelectTextField;
import com.d2s.framework.gui.ulc.components.server.ULCTranslationDataTypeFactory;
import com.d2s.framework.model.descriptor.IBinaryPropertyDescriptor;
import com.d2s.framework.model.descriptor.IBooleanPropertyDescriptor;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;
import com.d2s.framework.model.descriptor.IDatePropertyDescriptor;
import com.d2s.framework.model.descriptor.IDecimalPropertyDescriptor;
import com.d2s.framework.model.descriptor.IDurationPropertyDescriptor;
import com.d2s.framework.model.descriptor.IEnumerationPropertyDescriptor;
import com.d2s.framework.model.descriptor.IIntegerPropertyDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.INumberPropertyDescriptor;
import com.d2s.framework.model.descriptor.IPasswordPropertyDescriptor;
import com.d2s.framework.model.descriptor.IPercentPropertyDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import com.d2s.framework.model.descriptor.ISourceCodePropertyDescriptor;
import com.d2s.framework.model.descriptor.IStringPropertyDescriptor;
import com.d2s.framework.model.descriptor.ITextPropertyDescriptor;
import com.d2s.framework.model.descriptor.ITimePropertyDescriptor;
import com.d2s.framework.security.ISecurable;
import com.d2s.framework.util.format.DurationFormatter;
import com.d2s.framework.util.format.FormatAdapter;
import com.d2s.framework.util.format.IFormatter;
import com.d2s.framework.util.gate.IGate;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.util.ulc.UlcUtil;
import com.d2s.framework.view.BasicCompositeView;
import com.d2s.framework.view.BasicMapView;
import com.d2s.framework.view.BasicView;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.ICompositeView;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IMapView;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.ViewException;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.IBorderViewDescriptor;
import com.d2s.framework.view.descriptor.ICardViewDescriptor;
import com.d2s.framework.view.descriptor.ICollectionViewDescriptor;
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;
import com.d2s.framework.view.descriptor.ICompositeTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ICompositeViewDescriptor;
import com.d2s.framework.view.descriptor.IConstrainedGridViewDescriptor;
import com.d2s.framework.view.descriptor.IEvenGridViewDescriptor;
import com.d2s.framework.view.descriptor.IGridViewDescriptor;
import com.d2s.framework.view.descriptor.IImageViewDescriptor;
import com.d2s.framework.view.descriptor.IListViewDescriptor;
import com.d2s.framework.view.descriptor.INestingViewDescriptor;
import com.d2s.framework.view.descriptor.IPropertyViewDescriptor;
import com.d2s.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ISplitViewDescriptor;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;
import com.d2s.framework.view.descriptor.ITabViewDescriptor;
import com.d2s.framework.view.descriptor.ITableViewDescriptor;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ITreeViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.TreeDescriptorHelper;
import com.d2s.framework.view.descriptor.ViewConstraints;
import com.d2s.framework.view.descriptor.basic.BasicListViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicSubviewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicTableViewDescriptor;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.DefaultComboBoxCellRenderer;
import com.ulcjava.base.application.GridBagConstraints;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCAbstractButton;
import com.ulcjava.base.application.ULCBorderLayoutPane;
import com.ulcjava.base.application.ULCButton;
import com.ulcjava.base.application.ULCCardPane;
import com.ulcjava.base.application.ULCCheckBox;
import com.ulcjava.base.application.ULCComboBox;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCGridBagLayoutPane;
import com.ulcjava.base.application.ULCGridLayoutPane;
import com.ulcjava.base.application.ULCLabel;
import com.ulcjava.base.application.ULCList;
import com.ulcjava.base.application.ULCMenuItem;
import com.ulcjava.base.application.ULCPasswordField;
import com.ulcjava.base.application.ULCPopupMenu;
import com.ulcjava.base.application.ULCScrollPane;
import com.ulcjava.base.application.ULCSplitPane;
import com.ulcjava.base.application.ULCTabbedPane;
import com.ulcjava.base.application.ULCTableTree;
import com.ulcjava.base.application.ULCTextArea;
import com.ulcjava.base.application.ULCTextField;
import com.ulcjava.base.application.ULCToolBar;
import com.ulcjava.base.application.ULCTree;
import com.ulcjava.base.application.datatype.ULCDateDataType;
import com.ulcjava.base.application.datatype.ULCNumberDataType;
import com.ulcjava.base.application.datatype.ULCPercentDataType;
import com.ulcjava.base.application.table.DefaultTableHeaderCellRenderer;
import com.ulcjava.base.application.table.ITableCellRenderer;
import com.ulcjava.base.application.table.ULCTableColumn;
import com.ulcjava.base.application.tree.DefaultTreeCellRenderer;
import com.ulcjava.base.application.tree.TreePath;
import com.ulcjava.base.application.tree.ULCTreeSelectionModel;
import com.ulcjava.base.application.util.Color;
import com.ulcjava.base.application.util.Dimension;
import com.ulcjava.base.application.util.Font;
import com.ulcjava.base.application.util.Insets;
import com.ulcjava.base.application.util.KeyStroke;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.shared.IDefaults;
import com.ulcjava.base.shared.IUlcEventConstants;

/**
 * Factory for ULC views.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultUlcViewFactory implements
    IViewFactory<ULCComponent, ULCIcon, IAction> {

  private IConfigurableConnectorFactory         connectorFactory;
  private IMvcBinder                            mvcBinder;
  private IMasterDetailBinder                   masterDetailBinder;
  private IListSelectionModelBinder             listSelectionModelBinder;
  private ITreeSelectionModelBinder             treeSelectionModelBinder;
  private ITranslationProvider                  translationProvider;
  private int                                   maxCharacterLength          = 30;
  private int                                   maxColumnCharacterLength    = 15;
  private static final char                     TEMPLATE_CHAR               = 'O';
  private static final Date                     TEMPLATE_DATE               = new Date(
                                                                                3661 * 1000);
  private static final Date                     TEMPLATE_TIME               = new Date(
                                                                                3661 * 1000);
  private static final Long                     TEMPLATE_DURATION           = new Long(
                                                                                IDurationPropertyDescriptor.ONE_SECOND
                                                                                    + IDurationPropertyDescriptor.ONE_MINUTE
                                                                                    + IDurationPropertyDescriptor.ONE_HOUR
                                                                                    + IDurationPropertyDescriptor.ONE_DAY
                                                                                    + IDurationPropertyDescriptor.ONE_WEEK);
  private static final double                   DEF_DISP_MAX_VALUE          = 1000;
  private static final int                      DEF_DISP_MAX_FRACTION_DIGIT = 2;
  private static final double                   DEF_DISP_TEMPLATE_PERCENT   = 99;

  private static final Dimension                MINIMUM_AREA_SIZE           = new Dimension(
                                                                                100,
                                                                                100);
  private IIconFactory<ULCIcon>                 iconFactory;
  private IActionFactory<IAction, ULCComponent> actionFactory;
  private IDisplayableAction                    lovAction;
  private IDisplayableAction                    openFileAsBinaryPropertyAction;
  private IDisplayableAction                    saveBinaryPropertyAsFileAction;
  private IDisplayableAction                    resetPropertyAction;
  private IDisplayableAction                    binaryPropertyInfoAction;

  private ULCTranslationDataTypeFactory         translationDataTypeFactory  = new ULCTranslationDataTypeFactory();
  private ULCDurationDataTypeFactory            durationDataTypeFactory     = new ULCDurationDataTypeFactory();

  /**
   * {@inheritDoc}
   */
  public IView<ULCComponent> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<ULCComponent> view = null;
    if (viewDescriptor instanceof IComponentViewDescriptor) {
      view = createComponentView((IComponentViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof INestingViewDescriptor) {
      view = createNestingView((INestingViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IImageViewDescriptor) {
      view = createImageView((IImageViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IPropertyViewDescriptor) {
      view = createPropertyView((IPropertyViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICollectionViewDescriptor) {
      view = createCollectionView((ICollectionViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICompositeViewDescriptor) {
      view = createCompositeView((ICompositeViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICardViewDescriptor) {
      view = createCardView((ICardViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITreeViewDescriptor) {
      view = createTreeView((ITreeViewDescriptor) viewDescriptor,
          actionHandler, locale);
    }
    if (viewDescriptor.getDescription() != null) {
      view.getPeer().setToolTipText(
          viewDescriptor.getI18nDescription(getTranslationProvider(), locale)
              + TOOLTIP_ELLIPSIS);
    }
    try {
      actionHandler.checkAccess(viewDescriptor);
      if (viewDescriptor.getForeground() != null) {
        view.getPeer().setForeground(
            createUlcColor(viewDescriptor.getForeground()));
      }
      if (viewDescriptor.getBackground() != null) {
        view.getPeer().setBackground(
            createUlcColor(viewDescriptor.getBackground()));
      }
      if (viewDescriptor.getFont() != null) {
        view.getPeer().setFont(createUlcFont(viewDescriptor.getFont()));
      }
      if (viewDescriptor.isReadOnly()) {
        view.getConnector().setLocallyWritable(false);
      }
      if (viewDescriptor.getActions() != null) {
        ULCToolBar toolBar = createULCToolBar();
        for (Iterator<Map.Entry<String, List<IDisplayableAction>>> iter = viewDescriptor
            .getActions().entrySet().iterator(); iter.hasNext();) {
          Map.Entry<String, List<IDisplayableAction>> nextActionSet = iter
              .next();
          for (IDisplayableAction action : nextActionSet.getValue()) {
            IAction ulcAction = actionFactory.createAction(action,
                actionHandler, view, locale);
            ULCButton actionButton = createULCButton();
            actionButton.setAction(ulcAction);

            if (action.getAcceleratorAsString() != null) {
              KeyStroke ks = KeyStroke.getKeyStroke(action
                  .getAcceleratorAsString());
              view.getPeer().registerKeyboardAction(ulcAction, ks,
                  ULCComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
              String acceleratorString = java.awt.event.KeyEvent
                  .getKeyModifiersText(ks.getModifiers())
                  + "-" + java.awt.event.KeyEvent.getKeyText(ks.getKeyCode());
              actionButton.setToolTipText("<HTML>"
                  + actionButton.getToolTipText()
                  + " <FONT SIZE=\"-2\" COLOR=\"#993366\">" + acceleratorString
                  + "</FONT></HTML>");
            }

            actionButton.setText("");
            toolBar.add(actionButton);
          }
          if (iter.hasNext()) {
            toolBar.addSeparator();
          }
        }

        ULCBorderLayoutPane viewPanel = createBorderLayoutPane();
        viewPanel.add(toolBar, ULCBorderLayoutPane.NORTH);
        viewPanel.add(view.getPeer(), ULCBorderLayoutPane.CENTER);
        view.setPeer(viewPanel);
      }
      decorateWithBorder(view, locale);
    } catch (SecurityException ex) {
      view.setPeer(createSecurityPanel());
    }
    return view;
  }

  private Color createUlcColor(java.awt.Color color) {
    return new Color(color.getRed(), color.getGreen(), color.getBlue(), color
        .getAlpha());
  }

  private Font createUlcFont(java.awt.Font font) {
    return new Font(font.getName(), font.getStyle(), font.getSize());
  }

  /**
   * Decorates the created view with the apropriate border.
   *
   * @param view
   *          the view to descorate.
   * @param locale
   *          the locale to use.
   */
  protected void decorateWithBorder(IView<ULCComponent> view, Locale locale) {
    switch (view.getDescriptor().getBorderType()) {
      case IViewDescriptor.SIMPLE:
        view.getPeer().setBorder(BorderFactory.createEtchedBorder());
        break;
      case IViewDescriptor.TITLED:
        view.getPeer().setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), view.getDescriptor()
                    .getI18nName(getTranslationProvider(), locale)));
        break;
      default:
        break;
    }
  }

  // ///////////////// //
  // Composite Section //
  // ///////////////// //

  private ICompositeView<ULCComponent> createCompositeView(
      ICompositeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<ULCComponent> view = null;
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
    if (viewDescriptor.isMasterDetail()) {
      IView<ULCComponent> masterView = view.getChildren().get(0);
      view.setConnector(masterView.getConnector());
      for (int i = 1; i < view.getChildren().size(); i++) {
        IView<ULCComponent> detailView = view.getChildren().get(i);
        detailView.setParent(view);
        IValueConnector detailConnector = null;
        if (detailView.getConnector() instanceof ICollectionConnector) {
          IConfigurableCollectionConnectorProvider wrapper = connectorFactory
              .createConfigurableCollectionConnectorProvider(
                  ModelRefPropertyConnector.THIS_PROPERTY, null);
          wrapper.addChildConnector(detailView.getConnector());
          wrapper
              .setCollectionConnectorProvider((ICollectionConnector) detailView
                  .getConnector());
          detailConnector = wrapper;
        } else {
          detailConnector = detailView.getConnector();
        }
        masterDetailBinder.bind(masterView.getConnector(), detailConnector);
        masterView = detailView;
      }
    } else {
      ICompositeValueConnector connector = connectorFactory
          .createCompositeValueConnector(
              ModelRefPropertyConnector.THIS_PROPERTY, null);
      view.setConnector(connector);
      for (IView<ULCComponent> childView : view.getChildren()) {
        childView.setParent(view);
        connector.addChildConnector(childView.getConnector());
      }
    }
    return view;
  }

  private ICompositeView<ULCComponent> createGridView(
      IGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<ULCComponent> view = null;
    if (viewDescriptor instanceof IEvenGridViewDescriptor) {
      view = createEvenGridView((IEvenGridViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IConstrainedGridViewDescriptor) {
      view = createConstrainedGridView(
          (IConstrainedGridViewDescriptor) viewDescriptor, actionHandler,
          locale);
    }
    return view;
  }

  private ICompositeView<ULCComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCTabbedPane viewComponent = createULCTabbedPane();
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<ULCComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      ULCIcon childIcon = iconFactory.getIcon(childViewDescriptor
          .getIconImageURL(), IIconFactory.SMALL_ICON_SIZE);
      if (childViewDescriptor.getDescription() != null) {
        viewComponent.addTab(childViewDescriptor.getI18nName(
            getTranslationProvider(), locale), childIcon, childView.getPeer(),
            childViewDescriptor.getI18nDescription(getTranslationProvider(),
                locale));
      } else {
        viewComponent.addTab(childViewDescriptor.getI18nName(
            getTranslationProvider(), locale), childIcon, childView.getPeer());
      }
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private IMapView<ULCComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCCardPane viewComponent = createCardPane();
    BasicMapView<ULCComponent> view = constructMapView(viewComponent,
        viewDescriptor);
    Map<String, IView<ULCComponent>> childrenViews = new HashMap<String, IView<ULCComponent>>();

    viewComponent.add(createBorderLayoutPane(),
        ICardViewDescriptor.DEFAULT_CARD);
    viewComponent.add(createSecurityPanel(), ICardViewDescriptor.SECURITY_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<ULCComponent> childView = createView(
          childViewDescriptor.getValue(), actionHandler, locale);
      viewComponent.addCard(childViewDescriptor.getKey(), childView.getPeer());
      childrenViews.put(childViewDescriptor.getKey(), childView);
    }
    view.setChildren(childrenViews);
    view.setConnector(createCardViewConnector(view, actionHandler));
    return view;
  }

  private IValueConnector createCardViewConnector(
      final IMapView<ULCComponent> cardView, final IActionHandler actionHandler) {
    IValueConnector cardViewConnector = connectorFactory
        .createValueConnector(cardView.getDescriptor().getName());
    cardViewConnector
        .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

          public void connectorValueChange(ConnectorValueChangeEvent evt) {
            Object cardModel = evt.getNewValue();
            boolean accessGranted = true;
            if (cardModel instanceof ISecurable) {
              try {
                actionHandler.checkAccess((ISecurable) cardModel);
              } catch (SecurityException se) {
                accessGranted = false;
              }
            }
            ULCCardPane cardPanel = (ULCCardPane) cardView.getPeer();
            if (accessGranted) {
              String cardName = ((ICardViewDescriptor) cardView.getDescriptor())
                  .getCardNameForModel(cardModel);
              if (cardName != null) {
                IView<ULCComponent> childCardView = cardView.getChild(cardName);
                if (childCardView != null) {
                  cardPanel.setSelectedName(cardName);
                  IValueConnector childCardConnector = childCardView
                      .getConnector();
                  if (cardView.getDescriptor() instanceof ModuleCardViewDescriptor) {
                    if (childCardView.getDescriptor() instanceof ICollectionViewDescriptor) {
                      if (cardModel != null
                          && cardModel instanceof BeanCollectionModule) {
                        childCardConnector.getModelConnector()
                            .setConnectorValue(
                                ((BeanCollectionModule) cardModel)
                                    .getModuleObjects());
                      } else {
                        childCardConnector.getModelConnector()
                            .setConnectorValue(cardModel);
                      }
                    } else {
                      if (cardModel != null && cardModel instanceof BeanModule) {
                        childCardConnector.getModelConnector()
                            .setConnectorValue(
                                ((BeanModule) cardModel).getModuleObject());
                      } else {
                        childCardConnector.getModelConnector()
                            .setConnectorValue(cardModel);
                      }
                    }
                  } else {
                    if (childCardConnector != null) {
                      mvcBinder.bind(childCardConnector, cardView
                          .getConnector().getModelConnector());
                    }
                  }
                } else {
                  cardPanel.setSelectedName(ICardViewDescriptor.DEFAULT_CARD);
                }
              } else {
                cardPanel.setSelectedName(ICardViewDescriptor.DEFAULT_CARD);
              }
            } else {
              cardPanel.setSelectedName(ICardViewDescriptor.SECURITY_CARD);
            }
          }
        });
    return cardViewConnector;
  }

  private ICompositeView<ULCComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCSplitPane viewComponent = createULCSplitPane();
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    switch (viewDescriptor.getOrientation()) {
      case ISplitViewDescriptor.HORIZONTAL:
        viewComponent.setOrientation(ULCSplitPane.HORIZONTAL_SPLIT);
        break;
      case ISplitViewDescriptor.VERTICAL:
        viewComponent.setOrientation(ULCSplitPane.VERTICAL_SPLIT);
        break;
      default:
        break;
    }

    if (viewDescriptor.getLeftTopViewDescriptor() != null) {
      IView<ULCComponent> leftTopView = createView(viewDescriptor
          .getLeftTopViewDescriptor(), actionHandler, locale);
      viewComponent.setLeftComponent(leftTopView.getPeer());
      childrenViews.add(leftTopView);
    }
    if (viewDescriptor.getRightBottomViewDescriptor() != null) {
      IView<ULCComponent> rightBottomView = createView(viewDescriptor
          .getRightBottomViewDescriptor(), actionHandler, locale);
      viewComponent.setRightComponent(rightBottomView.getPeer());
      childrenViews.add(rightBottomView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private ICompositeView<ULCComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCGridLayoutPane viewComponent = createGridLayoutPane();
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    switch (viewDescriptor.getDrivingDimension()) {
      case IEvenGridViewDescriptor.ROW:
        viewComponent.setColumns(viewDescriptor.getDrivingDimensionCellCount());
        viewComponent.setRows(0);
        break;
      case IEvenGridViewDescriptor.COLUMN:
        viewComponent.setRows(viewDescriptor.getDrivingDimensionCellCount());
        viewComponent.setColumns(0);
        break;
      default:
        break;
    }
    viewComponent.setHgap(5);
    viewComponent.setVgap(5);

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<ULCComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      viewComponent.add(childView.getPeer());
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private ICompositeView<ULCComponent> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCGridBagLayoutPane viewComponent = createGridBagLayoutPane();
    viewComponent.setMinimumSize(MINIMUM_AREA_SIZE);
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<ULCComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      viewComponent.add(childView.getPeer(),
          createGridBagConstraints(viewDescriptor
              .getViewConstraints(childViewDescriptor)));
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private GridBagConstraints createGridBagConstraints(
      ViewConstraints viewConstraints) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.setGridX(viewConstraints.getColumn());
    constraints.setGridY(viewConstraints.getRow());
    constraints.setGridWidth(viewConstraints.getWidth());
    constraints.setGridHeight(viewConstraints.getHeight());
    if (viewConstraints.isWidthResizable()) {
      constraints.setWeightX(1.0D);
      if (viewConstraints.isHeightResizable()) {
        constraints.setFill(GridBagConstraints.BOTH);
      } else {
        constraints.setFill(GridBagConstraints.HORIZONTAL);
      }
    }
    if (viewConstraints.isHeightResizable()) {
      constraints.setWeightY(1.0D);
      if (viewConstraints.isWidthResizable()) {
        constraints.setFill(GridBagConstraints.BOTH);
      } else {
        constraints.setFill(GridBagConstraints.VERTICAL);
      }
    }
    return constraints;
  }

  private ICompositeView<ULCComponent> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCBorderLayoutPane viewComponent = createBorderLayoutPane();
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<ULCComponent> eastView = createView(viewDescriptor
          .getEastViewDescriptor(), actionHandler, locale);
      viewComponent.add(eastView.getPeer(), ULCBorderLayoutPane.EAST);
      childrenViews.add(eastView);
    }
    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<ULCComponent> northView = createView(viewDescriptor
          .getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.add(northView.getPeer(), ULCBorderLayoutPane.NORTH);
      childrenViews.add(northView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<ULCComponent> centerView = createView(viewDescriptor
          .getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.add(centerView.getPeer(), ULCBorderLayoutPane.CENTER);
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<ULCComponent> westView = createView(viewDescriptor
          .getWestViewDescriptor(), actionHandler, locale);
      viewComponent.add(westView.getPeer(), ULCBorderLayoutPane.WEST);
      childrenViews.add(westView);
    }
    if (viewDescriptor.getSouthViewDescriptor() != null) {
      IView<ULCComponent> southView = createView(viewDescriptor
          .getSouthViewDescriptor(), actionHandler, locale);
      viewComponent.add(southView.getPeer(), ULCBorderLayoutPane.SOUTH);
      childrenViews.add(southView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  // //////////// //
  // Tree Section //
  // //////////// //
  private IView<ULCComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor, @SuppressWarnings("unused")
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {

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
              viewDescriptor, subtreeViewDescriptor, 0);
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
            viewDescriptor, ((ISimpleTreeLevelDescriptor) rootDescriptor)
                .getChildDescriptor(), 0);
        simpleConnector.addChildConnector(subtreeConnector);
        simpleConnector.setCollectionConnectorProvider(subtreeConnector);
      }
      connector = simpleConnector;
    }

    if (connector instanceof IConnectorSelector) {
      ((IConnectorSelector) connector).setTracksChildrenSelection(true);
    }

    ULCExtendedTree viewComponent = createULCTree();
    ConnectorHierarchyTreeModel treeModel = new ConnectorHierarchyTreeModel(
        connector, viewComponent);

    // TODO tabletree mgt
    // ULCTableTree viewComponent = createULCTableTree();
    // ConnectorHierarchyTableTreeModel treeModel = new
    // ConnectorHierarchyTableTreeModel(
    // connector, viewComponent);
    // List<String> columnIds = new ArrayList<String>();
    // columnIds.add("text");
    // columnIds.add("text");
    // treeModel.setColumnConnectorKeys(columnIds);
    // Map<String, Class> columnClassesByIds = new HashMap<String, Class>();
    // columnClassesByIds.put("text", String.class);
    // treeModel.setColumnClassesByIds(columnClassesByIds);

    viewComponent.getSelectionModel().setSelectionMode(
        ULCTreeSelectionModel.SINGLE_TREE_SELECTION);
    viewComponent.setModel(treeModel);
    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer(viewDescriptor,
        locale));
    treeSelectionModelBinder.bindSelectionModel(connector, viewComponent);
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent.setPopupFactory(new TreeNodePopupFactory(viewComponent, view,
        actionHandler, locale));
    return view;
  }

  private ICollectionConnectorProvider createNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
      ITreeLevelDescriptor subtreeViewDescriptor, int depth) {
    if (subtreeViewDescriptor instanceof ICompositeTreeLevelDescriptor) {
      return createCompositeNodeGroupConnector(viewDescriptor,
          (ICompositeTreeLevelDescriptor) subtreeViewDescriptor, depth);
    } else if (subtreeViewDescriptor instanceof ISimpleTreeLevelDescriptor) {
      return createSimpleNodeGroupConnector(viewDescriptor,
          (ISimpleTreeLevelDescriptor) subtreeViewDescriptor, depth);
    }
    return null;
  }

  private ICollectionConnectorProvider createCompositeNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
      ICompositeTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionDescriptorProvider nodeGroupModelDescriptor = ((ICollectionDescriptorProvider) subtreeViewDescriptor
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
            viewDescriptor, childDescriptor, depth + 1);
        nodeGroupPrototypeConnector.addChildConnector(childConnector);
        subtreeConnectors.add(childConnector);
      }
    }
    nodeGroupPrototypeConnector
        .setCollectionConnectorProviders(subtreeConnectors);
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    nodeGroupCollectionConnector.setAllowLazyChildrenLoading(true);
    return nodeGroupCollectionConnector;
  }

  private ICollectionConnectorProvider createSimpleNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
      ISimpleTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionPropertyDescriptor nodeGroupModelDescriptor = (ICollectionPropertyDescriptor) subtreeViewDescriptor
        .getNodeGroupDescriptor().getModelDescriptor();
    IConfigurableCollectionConnectorProvider nodeGroupPrototypeConnector = connectorFactory
        .createConfigurableCollectionConnectorProvider(nodeGroupModelDescriptor
            .getName()
            + "Element", subtreeViewDescriptor.getNodeGroupDescriptor()
            .getRenderedProperty());
    if (subtreeViewDescriptor.getChildDescriptor() != null
        && depth < viewDescriptor.getMaxDepth()) {
      ICollectionConnectorProvider childConnector = createNodeGroupConnector(
          viewDescriptor, subtreeViewDescriptor.getChildDescriptor(), depth + 1);
      nodeGroupPrototypeConnector.addChildConnector(childConnector);
      nodeGroupPrototypeConnector
          .setCollectionConnectorProvider(childConnector);
    }
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    nodeGroupCollectionConnector.setAllowLazyChildrenLoading(true);
    return nodeGroupCollectionConnector;
  }

  private final class ConnectorTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long   serialVersionUID = -5153268751092971328L;
    private ITreeViewDescriptor viewDescriptor;

    @SuppressWarnings("unused")
    private Locale              locale;

    /**
     * Constructs a new <code>ConnectorTreeCellRenderer</code> instance.
     *
     * @param viewDescriptor
     *          the tree view descriptor used by the tree view.
     * @param locale
     */
    public ConnectorTreeCellRenderer(ITreeViewDescriptor viewDescriptor,
        Locale locale) {
      this.viewDescriptor = viewDescriptor;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getTreeCellRendererComponent(
        com.ulcjava.base.application.ULCTree tree, Object value, boolean sel,
        boolean expanded, boolean leaf, boolean nodeHasFocus) {
      IRendererComponent renderer = super.getTreeCellRendererComponent(tree,
          value, sel, expanded, leaf, nodeHasFocus);
      if (value instanceof IValueConnector) {
        ULCIcon nodeIcon = null;
        IValueConnector rootConnector = (IValueConnector) tree.getModel()
            .getRoot();
        nodeIcon = iconFactory.getIcon(viewDescriptor
            .getIconImageURLForUserObject(((IValueConnector) value)
                .getConnectorValue()), IIconFactory.SMALL_ICON_SIZE);
        if (nodeIcon == null) {
          if (value == rootConnector) {
            nodeIcon = iconFactory.getIcon(viewDescriptor.getIconImageURL(),
                IIconFactory.SMALL_ICON_SIZE);
          } else {
            TreePath path = ConnectorTreeHelper.getTreePathForConnector(
                rootConnector, (IValueConnector) value);
            if (path != null) {
              nodeIcon = iconFactory.getIcon(TreeDescriptorHelper
                  .getSubtreeDescriptorFromPath(
                      viewDescriptor.getRootSubtreeDescriptor(),
                      getDescriptorPathFromConnectorTreePath(path))
                  .getNodeGroupDescriptor().getIconImageURL(),
                  IIconFactory.SMALL_ICON_SIZE);
            }
          }
        }
        ((ULCLabel) renderer).setIcon(nodeIcon);

        // The following is useless in ULC since the renderer value is not
        // forwarded to the client.

        // String labelText = null;
        // String toolTipText = null;
        // if (value instanceof ICollectionConnector) {
        // IListViewDescriptor nodeGroupDescriptor = TreeDescriptorHelper
        // .getSubtreeDescriptorFromPath(
        // viewDescriptor.getRootSubtreeDescriptor(),
        // getDescriptorPathFromConnectorTreePath(ConnectorTreeHelper
        // .getTreePathForConnector((IValueConnector) tree
        // .getModel().getRoot(), (IValueConnector) value)))
        // .getNodeGroupDescriptor();
        // String labelKey = nodeGroupDescriptor.getName();
        // if (labelKey == null) {
        // labelKey = nodeGroupDescriptor.getModelDescriptor().getName();
        // }
        // labelText = translationProvider.getTranslation(labelKey, locale);
        // if (nodeGroupDescriptor.getDescription() != null) {
        // toolTipText = translationProvider.getTranslation(
        // nodeGroupDescriptor.getDescription(), locale) + TOOLTIP_ELLIPSIS;
        // }
        // } else {
        // setDataType(null);
        // if (((IValueConnector) value).getConnectorValue() != null) {
        // labelText = ((IValueConnector) value).getConnectorValue()
        // .toString();
        // } else {
        // labelText = "";
        // }
        // }
        // ((ULCLabel) renderer).setText(labelText);
        // ((ULCLabel) renderer).setToolTipText(toolTipText);
      }
      return renderer;
    }
  }

  private List<String> getDescriptorPathFromConnectorTreePath(
      TreePath connectorTreePath) {
    List<String> descriptorPath = new ArrayList<String>();
    if (connectorTreePath != null) {
      Object[] connectors = connectorTreePath.getPath();
      for (Object connector : connectors) {
        if (connector instanceof ICollectionConnectorProvider) {
          descriptorPath.add(((IValueConnector) connector).getId());
        }
      }
    }
    return descriptorPath;
  }

  // ////////////////// //
  // Collection Section //
  // ////////////////// //

  private IView<ULCComponent> createCollectionView(
      ICollectionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<ULCComponent> view = null;
    if (viewDescriptor instanceof IListViewDescriptor) {
      view = createListView((IListViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITableViewDescriptor) {
      view = createTableView((ITableViewDescriptor) viewDescriptor,
          actionHandler, locale);
    }
    return view;
  }

  private IView<ULCComponent> createListView(
      IListViewDescriptor viewDescriptor, @SuppressWarnings("unused")
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    ICollectionDescriptorProvider modelDescriptor = (ICollectionDescriptorProvider) viewDescriptor
        .getModelDescriptor();
    ICompositeValueConnector rowConnectorPrototype = connectorFactory
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    ICollectionConnector connector = connectorFactory
        .createCollectionConnector(modelDescriptor.getName(), mvcBinder,
            rowConnectorPrototype);
    ULCList viewComponent = createULCList();
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createColumnConnector(viewDescriptor
          .getRenderedProperty(), modelDescriptor.getCollectionDescriptor()
          .getElementDescriptor());
      rowConnectorPrototype.addChildConnector(cellConnector);
    }
    viewComponent.setCellRenderer(new EvenOddListCellRenderer());

    CollectionConnectorListModel listModel = new CollectionConnectorListModel(
        connector);
    viewComponent.setModel(listModel);
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), null);
    return view;
  }

  private IView<ULCComponent> createTableView(
      ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICollectionDescriptorProvider modelDescriptor = ((ICollectionDescriptorProvider) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = connectorFactory
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            modelDescriptor.getCollectionDescriptor().getElementDescriptor()
                .getToStringProperty());
    ICollectionConnector connector = connectorFactory
        .createCollectionConnector(modelDescriptor.getName(), mvcBinder,
            rowConnectorPrototype);
    ULCExtendedTable viewComponent = createULCTable();
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    ULCLabel iconLabel = createULCLabel();
    iconLabel.setIcon(iconFactory.getIcon(modelDescriptor
        .getCollectionDescriptor().getElementDescriptor().getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));
    iconLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setCorner(ULCScrollPane.UPPER_RIGHT_CORNER, iconLabel);
    scrollPane
        .setVerticalScrollBarPolicy(ULCScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent
        .setAutoResizeMode(com.ulcjava.base.application.ULCTable.AUTO_RESIZE_OFF);

    Map<String, Class> columnClassesByIds = new HashMap<String, Class>();
    List<String> columnConnectorKeys = new ArrayList<String>();

    for (ISubViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String columnId = columnViewDescriptor.getName();
      IValueConnector columnConnector = createColumnConnector(columnId,
          modelDescriptor.getCollectionDescriptor().getElementDescriptor());
      rowConnectorPrototype.addChildConnector(columnConnector);
      IPropertyDescriptor columnModelDescriptor = modelDescriptor
          .getCollectionDescriptor().getElementDescriptor()
          .getPropertyDescriptor(columnId);
      if (columnModelDescriptor instanceof IReferencePropertyDescriptor) {
        columnClassesByIds.put(columnId, String.class);
      } else if (columnModelDescriptor instanceof IBooleanPropertyDescriptor) {
        columnClassesByIds.put(columnId, Boolean.class);
      } else {
        columnClassesByIds.put(columnId, columnModelDescriptor.getModelType());
      }
      columnConnectorKeys.add(columnId);
      if (columnViewDescriptor.getReadabilityGates() != null) {
        for (IGate gate : columnViewDescriptor.getReadabilityGates()) {
          columnConnector.addReadabilityGate(gate.clone());
        }
      }
      if (columnViewDescriptor.getWritabilityGates() != null) {
        for (IGate gate : columnViewDescriptor.getWritabilityGates()) {
          columnConnector.addWritabilityGate(gate.clone());
        }
      }
      columnConnector.setLocallyWritable(!columnViewDescriptor.isReadOnly());
    }
    CollectionConnectorTableModel tableModel = new CollectionConnectorTableModel(
        connector, columnConnectorKeys);
    tableModel.setExceptionHandler(actionHandler);
    tableModel.setColumnClassesByIds(columnClassesByIds);

    TableSorter sorterDecorator = new TableSorter(tableModel, viewComponent
        .getTableHeader());
    java.awt.Dimension iconSize = new java.awt.Dimension(viewComponent
        .getTableHeader().getFont().getSize(), viewComponent.getTableHeader()
        .getFont().getSize());
    sorterDecorator.setUpIcon(iconFactory.getIcon(
        "classpath:images/1uparrow-48x48.png", iconSize));
    sorterDecorator.setDownIcon(iconFactory.getIcon(
        "classpath:images/1downarrow-48x48.png", iconSize));
    sorterDecorator.setColumnComparator(String.class,
        String.CASE_INSENSITIVE_ORDER);
    viewComponent.setModel(sorterDecorator);
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), sorterDecorator);

    int maxColumnSize = computePixelWidth(viewComponent,
        maxColumnCharacterLength);

    for (int i = 0; i < viewDescriptor.getColumnViewDescriptors().size(); i++) {
      ULCTableColumn column = viewComponent.getColumnModel().getColumn(i);
      column.setHeaderRenderer(new DefaultTableHeaderCellRenderer());
      String propertyName = viewDescriptor.getColumnViewDescriptors().get(i)
          .getName();
      column.setIdentifier(propertyName);
      IPropertyDescriptor propertyDescriptor = modelDescriptor
          .getCollectionDescriptor().getElementDescriptor()
          .getPropertyDescriptor(propertyName);
      StringBuffer columnName = new StringBuffer(propertyDescriptor
          .getI18nName(getTranslationProvider(), locale));
      if (propertyDescriptor.isMandatory()) {
        columnName.append("*");
      }
      column.setHeaderValue(columnName.toString());
      IView<ULCComponent> editorView = createPropertyView(propertyDescriptor,
          null, actionHandler, locale);
      if (editorView.getPeer() instanceof ULCActionField) {
        ULCActionField actionField = (ULCActionField) editorView.getPeer();
        actionField.setActions(Collections.singletonList(actionField
            .getActions().get(0)));
      }
      if (editorView.getConnector().getParentConnector() == null) {
        editorView.getConnector().setParentConnector(connector);
      }
      if (editorView.getPeer() instanceof IEditorComponent) {
        UlcViewCellEditorAdapter editor = new UlcViewCellEditorAdapter(
            editorView);
        column.setCellEditor(editor);
      }
      ITableCellRenderer cellRenderer = createTableCellRenderer(column
          .getModelIndex(), propertyDescriptor, locale);
      if (cellRenderer != null) {
        column.setCellRenderer(cellRenderer);
      } else {
        column.setCellRenderer(new EvenOddTableCellRenderer(column
            .getModelIndex()));
      }
      int minHeaderWidth = computePixelWidth(viewComponent, columnName.length());
      if (propertyDescriptor instanceof IBooleanPropertyDescriptor
          || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
        column.setPreferredWidth(Math.max(computePixelWidth(viewComponent, 2),
            minHeaderWidth));
        if (editorView.getPeer() instanceof ULCAbstractButton) {
          ((ULCAbstractButton) editorView.getPeer())
              .setHorizontalAlignment(IDefaults.CENTER);
        } else if (editorView.getPeer() instanceof ULCLabel) {
          ((ULCLabel) editorView.getPeer())
              .setHorizontalAlignment(IDefaults.CENTER);
        }
      } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
        column.setPreferredWidth(Math.max(computePixelWidth(viewComponent,
            getEnumerationTemplateValue(
                (IEnumerationPropertyDescriptor) propertyDescriptor, locale)
                .length()), minHeaderWidth));
      } else {
        column.setPreferredWidth(Math.max(
            Math.min(computePixelWidth(viewComponent, getFormatLength(
                createFormatter(propertyDescriptor, locale),
                getTemplateValue(propertyDescriptor))), maxColumnSize),
            minHeaderWidth));
      }
    }
    viewComponent.setComponentPopupMenu(createPopupMenu(viewComponent, view,
        actionHandler, locale));
    return view;
  }

  private Map<String, String> computeTranslationMapping(
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    Map<String, String> translationMapping = new HashMap<String, String>();
    for (String enumerationValue : propertyDescriptor.getEnumerationValues()) {
      translationMapping.put(enumerationValue, translationProvider
          .getTranslation(computeEnumerationKey(propertyDescriptor
              .getEnumerationName(), enumerationValue), locale));
    }
    return translationMapping;
  }

  private ITableCellRenderer createTableCellRenderer(int column,
      IPropertyDescriptor propertyDescriptor, Locale locale) {
    ITableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      cellRenderer = createBooleanTableCellRenderer(
          (IBooleanPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      cellRenderer = createDateTableCellRenderer(column,
          (IDatePropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      cellRenderer = createTimeTableCellRenderer(column,
          (ITimePropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      cellRenderer = createDurationTableCellRenderer(column,
          (IDurationPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
      cellRenderer = createEnumerationTableCellRenderer(column,
          (IEnumerationPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof INumberPropertyDescriptor) {
      cellRenderer = createNumberTableCellRenderer(column,
          (INumberPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      cellRenderer = createRelationshipEndTableCellRenderer(
          (IRelationshipEndPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      cellRenderer = createStringTableCellRenderer(column,
          (IStringPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private ITableCellRenderer createBooleanTableCellRenderer(
      @SuppressWarnings("unused")
      IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return new BooleanTableCellRenderer();
  }

  private ITableCellRenderer createDateTableCellRenderer(int column,
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createDateDataType(
        propertyDescriptor, locale,
        createDateFormat(propertyDescriptor, locale)));
  }

  private ITableCellRenderer createTimeTableCellRenderer(int column,
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createTimeDataType(
        propertyDescriptor, locale,
        createTimeFormat(propertyDescriptor, locale)));
  }

  private ITableCellRenderer createDurationTableCellRenderer(int column,
      IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createDurationDataType(
        propertyDescriptor, locale, createDurationFormatter(propertyDescriptor,
            locale)));
  }

  private ITableCellRenderer createEnumerationTableCellRenderer(int column,
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new TranslatedEnumerationTableCellRenderer(column,
        propertyDescriptor, locale);
  }

  private ITableCellRenderer createNumberTableCellRenderer(int column,
      INumberPropertyDescriptor propertyDescriptor, Locale locale) {
    ITableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      cellRenderer = createIntegerTableCellRenderer(column,
          (IIntegerPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      cellRenderer = createDecimalTableCellRenderer(column,
          (IDecimalPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private ITableCellRenderer createIntegerTableCellRenderer(int column,
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createIntegerDataType(
        propertyDescriptor, locale, createIntegerFormat(propertyDescriptor,
            locale)));
  }

  private ITableCellRenderer createDecimalTableCellRenderer(int column,
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentTableCellRenderer(column,
          (IPercentPropertyDescriptor) propertyDescriptor, locale);
    }
    return new FormattedTableCellRenderer(column, createDecimalDataType(
        propertyDescriptor, locale, createDecimalFormat(propertyDescriptor,
            locale)));
  }

  private ITableCellRenderer createPercentTableCellRenderer(int column,
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createPercentDataType(
        propertyDescriptor, locale, createPercentFormat(propertyDescriptor,
            locale)));
  }

  private ITableCellRenderer createRelationshipEndTableCellRenderer(
      IRelationshipEndPropertyDescriptor propertyDescriptor, Locale locale) {
    ITableCellRenderer cellRenderer = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      cellRenderer = createReferenceTableCellRenderer(
          (IReferencePropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      cellRenderer = createCollectionTableCellRenderer(
          (ICollectionPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private ITableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused")
      IReferencePropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

  private ITableCellRenderer createCollectionTableCellRenderer(
      @SuppressWarnings("unused")
      ICollectionPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

  private ITableCellRenderer createStringTableCellRenderer(int column,
      @SuppressWarnings("unused")
      IStringPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      Locale locale) {
    return new FormattedTableCellRenderer(column, null);
  }

  private final class TranslatedEnumerationTableCellRenderer extends
      EvenOddTableCellRenderer {

    private static final long              serialVersionUID = -4500472602998482756L;
    private IEnumerationPropertyDescriptor propertyDescriptor;
    private Locale                         locale;

    /**
     * Constructs a new <code>TranslatedEnumerationTableCellRenderer</code>
     * instance.
     *
     * @param column
     *          the column this renderer is attached to.
     * @param propertyDescriptor
     *          the property descriptor from which the enumeration name is
     *          taken. The prefix used to lookup translation keys in the form
     *          keyPrefix.value is the propertyDescriptor enumeration name.
     * @param locale
     *          the locale to lookup the translation.
     */
    public TranslatedEnumerationTableCellRenderer(int column,
        IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
      super(column);
      this.propertyDescriptor = propertyDescriptor;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getTableCellRendererComponent(
        com.ulcjava.base.application.ULCTable table, Object value,
        boolean isSelected, boolean hasFocus, int row) {
      if (propertyDescriptor.isTranslated()) {
        setDataType(translationDataTypeFactory.getTranslationDataType(
            propertyDescriptor.getEnumerationName(), locale,
            computeTranslationMapping(propertyDescriptor, locale)));
      }
      setIcon(iconFactory.getIcon(propertyDescriptor.getIconImageURL(String
          .valueOf(value)), IIconFactory.TINY_ICON_SIZE));
      UlcUtil.alternateEvenOddBackground(this, table, isSelected, row);
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row);
    }
  }

  private IValueConnector createColumnConnector(String columnId,
      IComponentDescriptor descriptor) {
    IPropertyDescriptor propertyDescriptor = descriptor
        .getPropertyDescriptor(columnId);
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + columnId + " defined for "
          + descriptor.getComponentContract());
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return connectorFactory.createCompositeValueConnector(columnId,
          ((IReferencePropertyDescriptor) propertyDescriptor)
              .getReferencedDescriptor().getToStringProperty());
    }
    return connectorFactory.createValueConnector(propertyDescriptor.getName());
  }

  // ///////////// //
  // Image Section //
  // ///////////// //

  private IView<ULCComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {
    ULCLabel imageLabel = createULCLabel();
    imageLabel.setHorizontalAlignment(IDefaults.CENTER);
    ULCImageConnector connector = new ULCImageConnector(viewDescriptor
        .getModelDescriptor().getName(), imageLabel);
    connector.setExceptionHandler(actionHandler);
    ULCBorderLayoutPane viewComponent = createBorderLayoutPane();
    IView<ULCComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(imageLabel);
    viewComponent.add(scrollPane, ULCBorderLayoutPane.CENTER);
    return view;
  }

  // /////////////// //
  // Nesting Section //
  // /////////////// //

  private IView<ULCComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = connectorFactory
        .createCompositeValueConnector(viewDescriptor.getModelDescriptor()
            .getName(), null);
    ULCBorderLayoutPane viewComponent = createBorderLayoutPane();
    IView<ULCComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    IView<ULCComponent> nestedView = createView(viewDescriptor
        .getNestedViewDescriptor(), actionHandler, locale);
    connector.addChildConnector(nestedView.getConnector());
    viewComponent.add(nestedView.getPeer(), ULCBorderLayoutPane.CENTER);
    return view;
  }

  // ///////////////// //
  // Component Section //
  // ///////////////// //

  private String getConnectorIdForComponentView(
      IComponentViewDescriptor viewDescriptor) {
    if (viewDescriptor.getModelDescriptor() instanceof IComponentDescriptor) {
      return ModelRefPropertyConnector.THIS_PROPERTY;
    }
    return viewDescriptor.getModelDescriptor().getName();
  }

  private IView<ULCComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = connectorFactory
        .createCompositeValueConnector(
            getConnectorIdForComponentView(viewDescriptor), null);
    ULCGridBagLayoutPane viewComponent = createGridBagLayoutPane();
    IView<ULCComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    int currentX = 0;
    int currentY = 0;

    boolean isSpaceFilled = false;

    for (ISubViewDescriptor propertyViewDescriptor : viewDescriptor
        .getPropertyViewDescriptors()) {
      String propertyName = propertyViewDescriptor.getName();
      IPropertyDescriptor propertyDescriptor = ((IComponentDescriptorProvider) viewDescriptor
          .getModelDescriptor()).getComponentDescriptor()
          .getPropertyDescriptor(propertyName);
      if (propertyDescriptor == null) {
        throw new ViewException("Property descriptor [" + propertyName
            + "] does not exist for model descriptor "
            + viewDescriptor.getModelDescriptor().getName() + ".");
      }
      IView<ULCComponent> propertyView = createPropertyView(propertyDescriptor,
          viewDescriptor.getRenderedChildProperties(propertyName),
          actionHandler, locale);
      propertyView.setParent(view);
      connector.addChildConnector(propertyView.getConnector());
      if (propertyViewDescriptor.getReadabilityGates() != null) {
        for (IGate gate : propertyViewDescriptor.getReadabilityGates()) {
          propertyView.getConnector().addReadabilityGate(gate.clone());
        }
      }
      if (propertyViewDescriptor.getWritabilityGates() != null) {
        for (IGate gate : propertyViewDescriptor.getWritabilityGates()) {
          propertyView.getConnector().addWritabilityGate(gate.clone());
        }
      }
      propertyView.getConnector().setLocallyWritable(
          !propertyViewDescriptor.isReadOnly());
      ULCLabel propertyLabel = createPropertyLabel(propertyDescriptor,
          propertyView.getPeer(), locale);

      int propertyWidth = viewDescriptor.getPropertyWidth(propertyName);
      if (propertyWidth > viewDescriptor.getColumnCount()) {
        propertyWidth = viewDescriptor.getColumnCount();
      }
      if (currentX + propertyWidth > viewDescriptor.getColumnCount()) {
        currentX = 0;
        currentY++;
      }

      // label positionning
      GridBagConstraints constraints = new GridBagConstraints();
      switch (viewDescriptor.getLabelsPosition()) {
        case IComponentViewDescriptor.ASIDE:
          constraints.setInsets(new Insets(5, 5, 5, 5));
          if (propertyView.getPeer() instanceof ULCTextArea
              || propertyView.getPeer() instanceof ULCList
              || propertyView.getPeer() instanceof ULCScrollPane
              || propertyView.getPeer() instanceof ULCExtendedTable
              || propertyView.getPeer() instanceof ULCJEditTextArea) {
            constraints.setAnchor(GridBagConstraints.NORTHEAST);
          } else {
            constraints.setAnchor(GridBagConstraints.EAST);
          }
          constraints.setGridX(currentX * 2);
          constraints.setGridY(currentY);
          break;
        case IComponentViewDescriptor.ABOVE:
          constraints.setInsets(new Insets(5, 5, 0, 5));
          constraints.setAnchor(GridBagConstraints.SOUTHWEST);
          constraints.setGridX(currentX);
          constraints.setGridY(currentY * 2);
          constraints.setGridWidth(propertyWidth);
          break;
        default:
          break;
      }
      viewComponent.add(propertyLabel, constraints);

      // component positionning
      switch (viewDescriptor.getLabelsPosition()) {
        case IComponentViewDescriptor.ASIDE:
          constraints.setGridX(constraints.getGridX() + 1);
          constraints.setInsets(new Insets(5, 0, 5, 5));
          constraints.setGridWidth(propertyWidth * 2 - 1);
          break;
        case IComponentViewDescriptor.ABOVE:
          constraints.setGridY(constraints.getGridY() + 1);
          constraints.setInsets(new Insets(0, 5, 0, 5));
          constraints.setGridWidth(propertyWidth);
          break;
        default:
          break;
      }

      constraints.setAnchor(GridBagConstraints.WEST);
      if (propertyView.getPeer().getPreferredSize() != null) {
        constraints.setWeightX(propertyView.getPeer().getPreferredSize()
            .getWidth());
      }
      if (propertyView.getPeer() instanceof ULCTextArea
          || propertyView.getPeer() instanceof ULCList
          || propertyView.getPeer() instanceof ULCScrollPane
          || propertyView.getPeer() instanceof ULCExtendedTable
          || propertyView.getPeer() instanceof ULCJEditTextArea) {
        constraints.setWeightY(1.0);
        constraints.setFill(GridBagConstraints.BOTH);
        isSpaceFilled = true;
      } else {
        constraints.setFill(GridBagConstraints.NONE);
      }
      viewComponent.add(propertyView.getPeer(), constraints);

      currentX += propertyWidth;
    }
    if (!isSpaceFilled) {
      ULCBorderLayoutPane filler = createBorderLayoutPane();
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.setGridX(0);
      constraints.setWeightX(1.0);
      constraints.setWeightY(1.0);
      constraints.setFill(GridBagConstraints.BOTH);
      switch (viewDescriptor.getLabelsPosition()) {
        case IComponentViewDescriptor.ASIDE:
          constraints.setGridY(currentY + 1);
          constraints.setGridWidth(viewDescriptor.getColumnCount() * 2);
          break;
        case IComponentViewDescriptor.ABOVE:
          constraints.setGridY((currentY + 1) * 2);
          constraints.setGridWidth(viewDescriptor.getColumnCount());
          break;
        default:
          break;
      }
      viewComponent.add(filler, constraints);
    }
    return view;
  }

  private ULCLabel createPropertyLabel(IPropertyDescriptor propertyDescriptor,
      ULCComponent propertyComponent, Locale locale) {
    ULCLabel propertyLabel = createULCLabel();
    StringBuffer labelText = new StringBuffer(propertyDescriptor.getI18nName(
        getTranslationProvider(), locale));
    if (propertyDescriptor.isMandatory()) {
      labelText.append("*");
      propertyLabel.setForeground(Color.red);
    }
    propertyLabel.setText(labelText.toString());
    propertyLabel.setLabelFor(propertyComponent);

    return propertyLabel;
  }

  private IView<ULCComponent> createPropertyView(
      IPropertyViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<ULCComponent> view = createPropertyView(
        (IPropertyDescriptor) viewDescriptor.getModelDescriptor(),
        viewDescriptor.getRenderedChildProperties(), actionHandler, locale);
    return constructView(view.getPeer(), viewDescriptor, view.getConnector());
  }

  private IView<ULCComponent> createPropertyView(
      IPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<ULCComponent> view = null;
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      view = createBooleanPropertyView(
          (IBooleanPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      view = createDatePropertyView(
          (IDatePropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      view = createTimePropertyView(
          (ITimePropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      view = createDurationPropertyView(
          (IDurationPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
      view = createEnumerationPropertyView(
          (IEnumerationPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof INumberPropertyDescriptor) {
      view = createNumberPropertyView(
          (INumberPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      view = createRelationshipEndPropertyView(
          (IRelationshipEndPropertyDescriptor) propertyDescriptor,
          renderedChildProperties, actionHandler, locale);
    } else if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      view = createStringPropertyView(
          (IStringPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IBinaryPropertyDescriptor) {
      view = createBinaryPropertyView(
          (IBinaryPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    if (propertyDescriptor.getDescription() != null) {
      view.getPeer().setToolTipText(
          propertyDescriptor.getI18nDescription(getTranslationProvider(),
              locale)
              + TOOLTIP_ELLIPSIS);
    }
    return view;
  }

  private IView<ULCComponent> createRelationshipEndPropertyView(
      IRelationshipEndPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<ULCComponent> view = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      view = createReferencePropertyView(
          (IReferencePropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      view = createCollectionPropertyView(
          (ICollectionPropertyDescriptor) propertyDescriptor,
          renderedChildProperties, actionHandler, locale);
    }
    return view;
  }

  private IView<ULCComponent> createNumberPropertyView(
      INumberPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      IActionHandler actionHandler, Locale locale) {
    IView<ULCComponent> view = null;
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      view = createIntegerPropertyView(
          (IIntegerPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      view = createDecimalPropertyView(
          (IDecimalPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    return view;
  }

  private IView<ULCComponent> createDatePropertyView(
      IDatePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {

    SimpleDateFormat format = createDateFormat(propertyDescriptor, locale);

    ULCDateField viewComponent = createULCDateField(format.toLocalizedPattern());
    ULCDateFieldConnector connector = new ULCDateFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getDateTemplateValue(propertyDescriptor), ClientContext
            .getScreenResolution() / 10);
    return constructView(viewComponent, null, connector);
  }

  private IView<ULCComponent> createTimePropertyView(
      ITimePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCTextField viewComponent = createULCTextField();
    SimpleDateFormat format = createTimeFormat(propertyDescriptor, locale);

    viewComponent.setDataType(createTimeDataType(propertyDescriptor, locale,
        format));

    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getTimeTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private ULCDateDataType createDateDataType(@SuppressWarnings("unused")
  IDatePropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
  Locale locale, SimpleDateFormat format) {
    return new ULCDateDataType(format.toPattern());
  }

  private ULCDateDataType createTimeDataType(@SuppressWarnings("unused")
  ITimePropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
  Locale locale, SimpleDateFormat format) {
    return new ULCDateDataType(format.toPattern());
  }

  private IView<ULCComponent> createStringPropertyView(
      IStringPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof IPasswordPropertyDescriptor) {
      return createPasswordPropertyView(
          (IPasswordPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    if (propertyDescriptor instanceof ISourceCodePropertyDescriptor) {
      return createSourceCodePropertyView(
          (ISourceCodePropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ITextPropertyDescriptor) {
      return createTextPropertyView(
          (ITextPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    ULCTextField viewComponent = createULCTextField();
    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<ULCComponent> createPasswordPropertyView(
      IPasswordPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    ULCPasswordField viewComponent = createULCPasswordField();
    ULCPasswordFieldConnector connector = new ULCPasswordFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<ULCComponent> createTextPropertyView(
      ITextPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {
    ULCTextArea viewComponent = createULCTextArea();
    viewComponent.setLineWrap(true);
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    scrollPane
        .setHorizontalScrollBarPolicy(ULCScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    ULCTextAreaConnector connector = new ULCTextAreaConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(scrollPane, null, connector);
  }

  private IView<ULCComponent> createSourceCodePropertyView(
      ISourceCodePropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    ULCJEditTextArea viewComponent = createULCJEditTextArea(propertyDescriptor
        .getLanguage());
    ULCJEditTextAreaConnector connector = new ULCJEditTextAreaConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private IView<ULCComponent> createCollectionPropertyView(
      ICollectionPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {

    IView<ULCComponent> view;
    if (renderedChildProperties != null && renderedChildProperties.size() > 1) {
      BasicTableViewDescriptor viewDescriptor = new BasicTableViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      List<ISubViewDescriptor> columnViewDescriptors = new ArrayList<ISubViewDescriptor>();
      for (String renderedProperty : renderedChildProperties) {
        BasicSubviewDescriptor columnDescriptor = new BasicSubviewDescriptor();
        columnDescriptor.setName(renderedProperty);
        columnViewDescriptors.add(columnDescriptor);
      }
      viewDescriptor.setColumnViewDescriptors(columnViewDescriptors);
      viewDescriptor.setName(propertyDescriptor.getName());
      view = createTableView(viewDescriptor, actionHandler, locale);
    } else {
      BasicListViewDescriptor viewDescriptor = new BasicListViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      if (renderedChildProperties != null
          && renderedChildProperties.size() == 1) {
        viewDescriptor.setRenderedProperty(renderedChildProperties.get(0));
      }
      viewDescriptor.setName(propertyDescriptor.getName());
      view = createListView(viewDescriptor, actionHandler, locale);
    }
    return view;
  }

  private IView<ULCComponent> createReferencePropertyView(
      IReferencePropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCActionField viewComponent = createULCActionField(true);
    ULCReferenceFieldConnector connector = new ULCReferenceFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setToStringPropertyConnector(new BasicValueConnector(
        propertyDescriptor.getComponentDescriptor().getToStringProperty()));
    connector.setExceptionHandler(actionHandler);
    IAction fieldAction = actionFactory.createAction(lovAction, actionHandler,
        viewComponent, propertyDescriptor, connector, locale);
    fieldAction.putValue(IAction.NAME, getTranslationProvider().getTranslation(
        "lov.element.name",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            translationProvider, locale)}, locale));
    fieldAction.putValue(IAction.SHORT_DESCRIPTION, getTranslationProvider()
        .getTranslation(
            "lov.element.description",
            new Object[] {propertyDescriptor.getReferencedDescriptor()
                .getI18nName(translationProvider, locale)}, locale)
        + TOOLTIP_ELLIPSIS);
    if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
      fieldAction.putValue(IAction.SMALL_ICON, iconFactory.getIcon(
          propertyDescriptor.getReferencedDescriptor().getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }
    viewComponent.setActions(Collections.singletonList(fieldAction));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, null, connector);
  }

  private IView<ULCComponent> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCActionField viewComponent = createULCActionField(false);
    ULCActionFieldConnector connector = new ULCActionFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    IAction openAction = actionFactory.createAction(
        openFileAsBinaryPropertyAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    IAction saveAction = actionFactory.createAction(
        saveBinaryPropertyAsFileAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    IAction resetAction = actionFactory.createAction(resetPropertyAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    IAction infoAction = actionFactory.createAction(binaryPropertyInfoAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    viewComponent.setActions(Arrays.asList(new IAction[] {openAction,
        saveAction, resetAction, infoAction}));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, null, connector);
  }

  private IView<ULCComponent> createDecimalPropertyView(
      IDecimalPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(
          (IPercentPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    ULCTextField viewComponent = createULCTextField();
    NumberFormat format = createDecimalFormat(propertyDescriptor, locale);

    viewComponent.setDataType(createDecimalDataType(propertyDescriptor, locale,
        format));

    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getDecimalTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private ULCNumberDataType createDecimalDataType(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale,
      NumberFormat format) {
    ULCNumberDataType numberDataType = new ULCNumberDataType(locale);
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      numberDataType.setMaxFractionDigits(propertyDescriptor
          .getMaxFractionDigit().intValue());
    }
    numberDataType.setMin(propertyDescriptor.getMinValue());
    numberDataType.setMax(propertyDescriptor.getMaxValue());
    numberDataType.setGroupingUsed(format.isGroupingUsed());
    return numberDataType;
  }

  private IView<ULCComponent> createPercentPropertyView(
      IPercentPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCTextField viewComponent = createULCTextField();
    NumberFormat format = createPercentFormat(propertyDescriptor, locale);

    viewComponent.setDataType(createPercentDataType(propertyDescriptor, locale,
        format));
    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getPercentTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private ULCPercentDataType createPercentDataType(
      IPercentPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale, @SuppressWarnings("unused")
      NumberFormat format) {
    ULCPercentDataType percentDataType = new ULCPercentDataType();
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      percentDataType.setFractionalDigits(propertyDescriptor
          .getMaxFractionDigit().intValue());
    }
    return percentDataType;
  }

  private IView<ULCComponent> createIntegerPropertyView(
      IIntegerPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCTextField viewComponent = createULCTextField();
    NumberFormat format = createIntegerFormat(propertyDescriptor, locale);

    viewComponent.setDataType(createIntegerDataType(propertyDescriptor, locale,
        format));

    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getIntegerTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private ULCNumberDataType createIntegerDataType(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale,
      NumberFormat format) {
    ULCNumberDataType numberDataType = new ULCNumberDataType(locale);
    numberDataType.setInteger(true);
    numberDataType.setMin(propertyDescriptor.getMinValue());
    numberDataType.setMax(propertyDescriptor.getMaxValue());
    numberDataType.setMaxFractionDigits(0);
    numberDataType.setMinFractionDigits(0);
    numberDataType.setGroupingUsed(format.isGroupingUsed());
    return numberDataType;
  }

  private IView<ULCComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCComboBox viewComponent = createULCComboBox();
    for (String enumElement : propertyDescriptor.getEnumerationValues()) {
      viewComponent.addItem(enumElement);
    }
    viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(
        propertyDescriptor, locale));
    adjustSizes(viewComponent, null, getEnumerationTemplateValue(
        propertyDescriptor, locale), ClientContext.getScreenResolution() / 3);
    ULCComboBoxConnector connector = new ULCComboBoxConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private final class TranslatedEnumerationListCellRenderer extends
      DefaultComboBoxCellRenderer {

    private static final long              serialVersionUID = -5694559709701757582L;
    private IEnumerationPropertyDescriptor propertyDescriptor;
    private Locale                         locale;

    /**
     * Constructs a new <code>TranslatedEnumerationCellRenderer</code>
     * instance.
     *
     * @param propertyDescriptor
     *          the property descriptor from which the enumeration name is
     *          taken. The prefix used to lookup translation keys in the form
     *          keyPrefix.value is the propertyDescriptor enumeration name.
     * @param locale
     *          the locale to lookup the translation.
     */
    public TranslatedEnumerationListCellRenderer(
        IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
      this.propertyDescriptor = propertyDescriptor;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getComboBoxCellRendererComponent(
        ULCComboBox comboBox, Object value, boolean isSelected, int index) {
      if (propertyDescriptor.isTranslated()) {
        setDataType(translationDataTypeFactory.getTranslationDataType(
            propertyDescriptor.getEnumerationName(), locale,
            computeTranslationMapping(propertyDescriptor, locale)));
      }
      setIcon(iconFactory.getIcon(propertyDescriptor.getIconImageURL(String
          .valueOf(value)), IIconFactory.TINY_ICON_SIZE));
      return super.getComboBoxCellRendererComponent(comboBox, value,
          isSelected, index);
    }
  }

  private String computeEnumerationKey(String keyPrefix, Object value) {
    return keyPrefix + "." + value;
  }

  private IView<ULCComponent> createDurationPropertyView(
      IDurationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCTextField viewComponent = createULCTextField();
    DurationFormatter formatter = createDurationFormatter(propertyDescriptor,
        locale);
    viewComponent.setDataType(createDurationDataType(propertyDescriptor,
        locale, formatter));
    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDurationTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private ULCDurationDataType createDurationDataType(
      @SuppressWarnings("unused")
      IDurationPropertyDescriptor propertyDescriptor, Locale locale,
      @SuppressWarnings("unused")
      DurationFormatter formatter) {
    ULCDurationDataType durationDataType = durationDataTypeFactory
        .getTranslationDataType(locale);
    return durationDataType;
  }

  private IView<ULCComponent> createBooleanPropertyView(
      IBooleanPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    ULCCheckBox viewComponent = createULCCheckBox();
    ULCToggleButtonConnector connector = new ULCToggleButtonConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private IView<ULCComponent> constructView(ULCComponent viewComponent,
      IViewDescriptor descriptor, IValueConnector connector) {
    BasicView<ULCComponent> view = new BasicView<ULCComponent>(viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

  private BasicCompositeView<ULCComponent> constructCompositeView(
      ULCComponent viewComponent, IViewDescriptor descriptor) {
    BasicCompositeView<ULCComponent> view = new BasicCompositeView<ULCComponent>(
        viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  private BasicMapView<ULCComponent> constructMapView(
      ULCComponent viewComponent, IViewDescriptor descriptor) {
    BasicMapView<ULCComponent> view = new BasicMapView<ULCComponent>(
        viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  // ////////////////// //
  // Popup menu Section //
  // ////////////////// //

  private ULCPopupMenu createPopupMenu(ULCComponent sourceComponent,
      IView<ULCComponent> view, IActionHandler actionHandler, Locale locale) {
    if (sourceComponent instanceof ULCExtendedTable) {
      return createULCTablePopupMenu((ULCExtendedTable) sourceComponent, view,
          actionHandler, locale);
    }
    return null;
  }

  private ULCPopupMenu createULCTablePopupMenu(ULCExtendedTable table,
      IView<ULCComponent> tableView, IActionHandler actionHandler, Locale locale) {

    IValueConnector elementConnector = tableView.getConnector();
    IModelDescriptor modelDescriptor = tableView.getDescriptor()
        .getModelDescriptor();
    Map<String, List<IDisplayableAction>> actionMap = ((ICollectionViewDescriptor) tableView
        .getDescriptor()).getActions();

    if (actionMap == null) {
      return null;
    }

    return createULCPopupMenu(table, actionMap, modelDescriptor, tableView
        .getDescriptor(), elementConnector, actionHandler, locale);
  }

  private ULCPopupMenu createULCTreePopupMenu(ULCTree tree,
      IView<ULCComponent> treeView, TreePath path,
      IActionHandler actionHandler, Locale locale) {

    if (path == null) {
      return null;
    }

    if (!tree.isPathSelected(path)) {
      tree.setSelectionPath(path);
    }
    if (path.getLastPathComponent() instanceof ICollectionConnector) {
      TreePath[] allNodePaths = new TreePath[((ICollectionConnector) path
          .getLastPathComponent()).getChildConnectorCount()];
      for (int i = 0; i < allNodePaths.length; i++) {
        allNodePaths[i] = path.pathByAddingChild(((ICollectionConnector) path
            .getLastPathComponent()).getChildConnector(i));
      }
      tree.addSelectionPaths(allNodePaths);
    }

    IValueConnector viewConnector = (IValueConnector) path
        .getLastPathComponent();
    IModelDescriptor modelDescriptor;
    Map<String, List<IDisplayableAction>> actionMap;
    IViewDescriptor viewDescriptor;
    if (viewConnector == tree.getModel().getRoot()) {
      modelDescriptor = treeView.getDescriptor().getModelDescriptor();
      actionMap = treeView.getDescriptor().getActions();
      viewDescriptor = treeView.getDescriptor();
    } else {
      viewDescriptor = TreeDescriptorHelper.getSubtreeDescriptorFromPath(
          ((ITreeViewDescriptor) treeView.getDescriptor())
              .getRootSubtreeDescriptor(),
          getDescriptorPathFromConnectorTreePath(path))
          .getNodeGroupDescriptor();
      modelDescriptor = viewDescriptor.getModelDescriptor();
      actionMap = viewDescriptor.getActions();
      if (!(viewConnector instanceof ICollectionConnector)) {
        viewConnector = viewConnector.getParentConnector();
      }
    }

    if (actionMap == null) {
      return null;
    }

    return createULCPopupMenu(tree, actionMap, modelDescriptor, viewDescriptor,
        viewConnector, actionHandler, locale);
  }

  private final class TreeNodePopupFactory implements ITreePathPopupFactory {

    private ULCTree             tree;
    private IView<ULCComponent> view;
    private IActionHandler      actionHandler;
    private Locale              locale;

    /**
     * Constructs a new <code>TreeNodePopupFactory</code> instance.
     *
     * @param tree
     * @param view
     * @param actionHandler
     * @param locale
     */
    public TreeNodePopupFactory(ULCTree tree, IView<ULCComponent> view,
        IActionHandler actionHandler, Locale locale) {
      this.tree = tree;
      this.view = view;
      this.actionHandler = actionHandler;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    public ULCPopupMenu createPopupForTreepath(TreePath path) {
      return createULCTreePopupMenu(tree, view, path, actionHandler, locale);
    }
  }

  private ULCPopupMenu createULCPopupMenu(ULCComponent sourceComponent,
      Map<String, List<IDisplayableAction>> actionMap,
      IModelDescriptor modelDescriptor, IViewDescriptor viewDescriptor,
      IValueConnector viewConnector, IActionHandler actionHandler, Locale locale) {
    ULCPopupMenu popupMenu = createULCPopupMenu();
    ULCLabel titleLabel = createULCLabel();
    titleLabel.setText(viewDescriptor.getI18nName(getTranslationProvider(),
        locale));
    titleLabel.setIcon(iconFactory.getIcon(viewDescriptor.getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));
    titleLabel.setHorizontalAlignment(IDefaults.CENTER);
    popupMenu.add(titleLabel);
    popupMenu.addSeparator();
    for (Iterator<Map.Entry<String, List<IDisplayableAction>>> iter = actionMap
        .entrySet().iterator(); iter.hasNext();) {
      Map.Entry<String, List<IDisplayableAction>> nextActionSet = iter.next();
      for (IDisplayableAction action : nextActionSet.getValue()) {
        IAction ulcAction = actionFactory.createAction(action, actionHandler,
            sourceComponent, modelDescriptor, viewConnector, locale);
        ULCMenuItem actionItem = createULCMenuItem();
        actionItem.setAction(ulcAction);
        popupMenu.add(actionItem);
      }
      if (iter.hasNext()) {
        popupMenu.addSeparator();
      }
    }
    return popupMenu;
  }

  // /////////////// //
  // Helpers Section //
  // /////////////// //

  private Object getTemplateValue(IPropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      return getDateTemplateValue((IDatePropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      return getTimeTemplateValue((ITimePropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      return getDurationTemplateValue((IDurationPropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      return getStringTemplateValue((IStringPropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      return getDecimalTemplateValue((IDecimalPropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return getPercentTemplateValue((IPercentPropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      return getIntegerTemplateValue((IIntegerPropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return getTemplateValue(((IReferencePropertyDescriptor) propertyDescriptor)
          .getReferencedDescriptor().getPropertyDescriptor(
              ((IReferencePropertyDescriptor) propertyDescriptor)
                  .getReferencedDescriptor().getToStringProperty()));
    }
    return null;
  }

  private IFormatter createFormatter(IPropertyDescriptor propertyDescriptor,
      Locale locale) {
    if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      return createDateFormatter((IDatePropertyDescriptor) propertyDescriptor,
          locale);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      return createTimeFormatter((ITimePropertyDescriptor) propertyDescriptor,
          locale);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      return createDurationFormatter(
          (IDurationPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      return createDecimalFormatter(
          (IDecimalPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentFormatter(
          (IPercentPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      return createIntegerFormatter(
          (IIntegerPropertyDescriptor) propertyDescriptor, locale);
    }
    return null;
  }

  private Object getDateTemplateValue(@SuppressWarnings("unused")
  IDatePropertyDescriptor propertyDescriptor) {
    return TEMPLATE_DATE;
  }

  private Object getTimeTemplateValue(@SuppressWarnings("unused")
  ITimePropertyDescriptor propertyDescriptor) {
    return TEMPLATE_TIME;
  }

  private SimpleDateFormat createDateFormat(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    DateFormat format;
    if (IDatePropertyDescriptor.DATE_TYPE.equals(propertyDescriptor.getType())) {
      format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    } else {
      format = DateFormat.getDateTimeInstance(DateFormat.SHORT,
          DateFormat.SHORT, locale);
    }
    return (SimpleDateFormat) format;
  }

  private IFormatter createDateFormatter(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createDateFormat(propertyDescriptor, locale));
  }

  private SimpleDateFormat createTimeFormat(@SuppressWarnings("unused")
  ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
    return (SimpleDateFormat) format;
  }

  private IFormatter createTimeFormatter(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createTimeFormat(propertyDescriptor, locale));
  }

  private IFormatter createFormatter(Format format) {
    return new FormatAdapter(format);
  }

  private Object getDurationTemplateValue(@SuppressWarnings("unused")
  IDurationPropertyDescriptor propertyDescriptor) {
    return TEMPLATE_DURATION;
  }

  private DurationFormatter createDurationFormatter(@SuppressWarnings("unused")
  IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new DurationFormatter(locale);
  }

  private String getStringTemplateValue(
      IStringPropertyDescriptor propertyDescriptor) {
    return getStringTemplateValue(propertyDescriptor.getMaxLength());
  }

  private String getStringTemplateValue(Integer maxLength) {
    StringBuffer templateValue = new StringBuffer();
    int fieldLength = maxCharacterLength;
    if (maxLength != null) {
      fieldLength = maxLength.intValue();
    }
    for (int i = 0; i < fieldLength; i++) {
      templateValue.append(TEMPLATE_CHAR);
    }
    return templateValue.toString();
  }

  private Object getDecimalTemplateValue(
      IDecimalPropertyDescriptor propertyDescriptor) {
    double templateValue = DEF_DISP_MAX_VALUE;
    if (propertyDescriptor.getMaxValue() != null) {
      templateValue = propertyDescriptor.getMaxValue().doubleValue();
    }
    int maxFractionDigit = DEF_DISP_MAX_FRACTION_DIGIT;
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      maxFractionDigit = propertyDescriptor.getMaxFractionDigit().intValue();
    }
    double decimalPart = 0;
    for (int i = 0; i < maxFractionDigit; i++) {
      decimalPart += Math.pow(10.0D, -i);
    }
    templateValue += decimalPart;
    return new Double(templateValue);
  }

  private IFormatter createDecimalFormatter(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createDecimalFormat(propertyDescriptor, locale));
  }

  private NumberFormat createDecimalFormat(@SuppressWarnings("unused")
  IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    NumberFormat format = NumberFormat.getNumberInstance(locale);
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit()
          .intValue());
    }
    return format;
  }

  private Object getPercentTemplateValue(
      IPercentPropertyDescriptor propertyDescriptor) {
    double templateValue = DEF_DISP_TEMPLATE_PERCENT;
    if (propertyDescriptor.getMaxValue() != null) {
      templateValue = propertyDescriptor.getMaxValue().doubleValue();
    }
    int maxFractionDigit = DEF_DISP_MAX_FRACTION_DIGIT;
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      maxFractionDigit = propertyDescriptor.getMaxFractionDigit().intValue();
    }
    double decimalPart = 0;
    for (int i = 0; i < maxFractionDigit; i++) {
      decimalPart += Math.pow(10.0D, -i);
    }
    templateValue += decimalPart;
    return new Double(templateValue);
  }

  private IFormatter createPercentFormatter(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createPercentFormat(propertyDescriptor, locale));
  }

  private NumberFormat createPercentFormat(@SuppressWarnings("unused")
  IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    NumberFormat format = NumberFormat.getPercentInstance(locale);
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit()
          .intValue());
    }
    return format;
  }

  private Object getIntegerTemplateValue(
      IIntegerPropertyDescriptor propertyDescriptor) {
    double templateValue = DEF_DISP_MAX_VALUE;
    if (propertyDescriptor.getMaxValue() != null) {
      templateValue = propertyDescriptor.getMaxValue().doubleValue();
    }
    return new Integer((int) templateValue);
  }

  private IFormatter createIntegerFormatter(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createIntegerFormat(propertyDescriptor, locale));
  }

  private NumberFormat createIntegerFormat(@SuppressWarnings("unused")
  IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return NumberFormat.getIntegerInstance(locale);
  }

  private int getFormatLength(IFormatter formatter, Object templateValue) {
    int formatLength;
    if (formatter != null) {
      formatLength = formatter.format(templateValue).length();
    } else {
      if (templateValue != null) {
        formatLength = templateValue.toString().length();
      } else {
        formatLength = maxCharacterLength;
      }
    }
    return formatLength;
  }

  private String getEnumerationTemplateValue(
      IEnumerationPropertyDescriptor descriptor, Locale locale) {
    int maxTranslationLength = -1;
    if (translationProvider != null && descriptor.isTranslated()) {
      for (Object enumerationValue : descriptor.getEnumerationValues()) {
        String translation = translationProvider.getTranslation(
            computeEnumerationKey(descriptor.getEnumerationName(),
                enumerationValue), locale);
        if (translation.length() > maxTranslationLength) {
          maxTranslationLength = translation.length();
        }
      }
    } else {
      maxTranslationLength = descriptor.getMaxLength().intValue();
    }
    if (maxTranslationLength == -1 || maxTranslationLength > maxCharacterLength) {
      maxTranslationLength = maxCharacterLength;
    }
    return getStringTemplateValue(new Integer(maxTranslationLength));
  }

  private void adjustSizes(ULCComponent component, IFormatter formatter,
      Object templateValue) {
    adjustSizes(component, formatter, templateValue, 0);
  }

  private void adjustSizes(ULCComponent component, IFormatter formatter,
      Object templateValue, int extraWidth) {
    int preferredWidth = computePixelWidth(component, getFormatLength(
        formatter, templateValue))
        + extraWidth;
    Dimension size = new Dimension(preferredWidth, component.getFont()
        .getSize() + 6);
    component.setMinimumSize(new Dimension(size.getWidth() / 2, size
        .getHeight()));
    component.setPreferredSize(size);
    component.setMaximumSize(size);
  }

  private int computePixelWidth(ULCComponent component, int characterLength) {
    int charLength = maxCharacterLength + 2;
    if (characterLength > 0 && characterLength < maxCharacterLength) {
      charLength = characterLength + 2;
    }
    return (int) ((component.getFont().getSize() * charLength) / 1.5);
  }

  /**
   * Creates a text field.
   *
   * @return the created text field.
   */
  protected ULCTextField createULCTextField() {
    ULCTextField textField = new ULCOnFocusSelectTextField();
    ClientContext.setEventDeliveryMode(textField,
        IUlcEventConstants.FOCUS_EVENT, IUlcEventConstants.ASYNCHRONOUS_MODE);
    return textField;
  }

  /**
   * Creates a password field.
   *
   * @return the created password field.
   */
  protected ULCPasswordField createULCPasswordField() {
    ULCPasswordField passwordField = new ULCPasswordField();
    return passwordField;
  }

  /**
   * Creates a text field.
   *
   * @return the created text field.
   */
  protected ULCTextArea createULCTextArea() {
    ULCTextArea textArea = new ULCTextArea();
    textArea.setDragEnabled(true);
    textArea.setWrapStyleWord(true);
    ClientContext.setEventDeliveryMode(textArea,
        IUlcEventConstants.FOCUS_EVENT, IUlcEventConstants.ASYNCHRONOUS_MODE);
    return textArea;
  }

  /**
   * Creates a ULC JEdit text area.
   *
   * @param language
   *          the language to add syntax highlighting for.
   * @return the created text area.
   */
  protected ULCJEditTextArea createULCJEditTextArea(String language) {
    ULCJEditTextArea textArea = new ULCJEditTextArea(language);
    return textArea;
  }

  /**
   * Creates an action field.
   *
   * @param showTextField
   *          is the text field visible to the user.
   * @return the created action field.
   */
  protected ULCActionField createULCActionField(boolean showTextField) {
    return new ULCActionField(showTextField);
  }

  /**
   * Creates a date field.
   *
   * @param formatPattern
   *          the (simple date format) pattern this date field uses.
   * @return the created date field.
   */
  protected ULCDateField createULCDateField(String formatPattern) {
    ULCDateField dateField = new ULCDateField(formatPattern);
    ClientContext.setEventDeliveryMode(dateField,
        IUlcEventConstants.VALUE_CHANGED_EVENT,
        IUlcEventConstants.ASYNCHRONOUS_MODE);
    return dateField;
  }

  /**
   * Creates a combo box.
   *
   * @return the created combo box.
   */
  protected ULCComboBox createULCComboBox() {
    return new com.d2s.framework.gui.ulc.components.server.ULCExtendedComboBox();
  }

  /**
   * Creates a label.
   *
   * @return the created label.
   */
  protected ULCLabel createULCLabel() {
    return new ULCLabel();
  }

  /**
   * Creates a tool bar.
   *
   * @return the created tool bar.
   */
  protected ULCToolBar createULCToolBar() {
    ULCToolBar toolBar = new ULCToolBar();
    toolBar.setFloatable(true);
    toolBar.setBorderPainted(false);
    return toolBar;
  }

  /**
   * Creates a tabbed pane.
   *
   * @return the created tabbed pane.
   */
  protected ULCTabbedPane createULCTabbedPane() {
    return new ULCTabbedPane();
  }

  /**
   * Creates a split pane.
   *
   * @return the created split pane.
   */
  protected ULCSplitPane createULCSplitPane() {
    ULCSplitPane splitPane = new ULCSplitPane();
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    return splitPane;
  }

  /**
   * Creates a tree.
   *
   * @return the created tree.
   */
  protected ULCExtendedTree createULCTree() {
    ULCExtendedTree tree = new ULCExtendedTree();
    tree.setDragEnabled(true);
    return tree;
  }

  /**
   * Creates a table tree.
   *
   * @return the created table tree.
   */
  protected ULCTableTree createULCTableTree() {
    ULCTableTree tableTree = new ULCTableTree();
    tableTree.setDragEnabled(true);
    return tableTree;
  }

  /**
   * Creates a table.
   *
   * @return the created table.
   */
  protected ULCExtendedTable createULCTable() {
    ULCExtendedTable table = new com.d2s.framework.gui.ulc.components.server.ULCExtendedTable();
    table.setDragEnabled(true);
    return table;
  }

  /**
   * Creates a list.
   *
   * @return the created list.
   */
  protected ULCList createULCList() {
    ULCList list = new ULCList();
    list.setDragEnabled(true);
    return list;
  }

  /**
   * Creates a button.
   *
   * @return the created button.
   */
  protected ULCButton createULCButton() {
    ULCButton ulcButton = new ULCExtendedButton();
    return ulcButton;
  }

  /**
   * Creates a popup menu.
   *
   * @return the created popup menu.
   */
  protected ULCPopupMenu createULCPopupMenu() {
    return new ULCPopupMenu();
  }

  /**
   * Creates a menu item.
   *
   * @return the created menu item.
   */
  protected ULCMenuItem createULCMenuItem() {
    return new ULCMenuItem();
  }

  /**
   * Creates a scroll pane.
   *
   * @return the created scroll pane.
   */
  protected ULCScrollPane createULCScrollPane() {
    ULCScrollPane scrollPane = new ULCScrollPane();
    scrollPane.setMinimumSize(MINIMUM_AREA_SIZE);
    return scrollPane;
  }

  /**
   * Creates a check box.
   *
   * @return the created check box.
   */
  protected ULCCheckBox createULCCheckBox() {
    return new ULCCheckBox();
  }

  /**
   * Creates a borderlayout pane.
   *
   * @return the created borderlayout pane.
   */
  protected ULCBorderLayoutPane createBorderLayoutPane() {
    ULCBorderLayoutPane pane = new ULCBorderLayoutPane();
    return pane;
  }

  /**
   * Creates a security pane.
   *
   * @return the created security pane.
   */
  protected ULCBorderLayoutPane createSecurityPanel() {
    ULCBorderLayoutPane panel = new ULCBorderLayoutPane();
    ULCLabel label = createULCLabel();
    label.setIcon(iconFactory.getForbiddenIcon(IIconFactory.LARGE_ICON_SIZE));
    label.setHorizontalAlignment(IDefaults.CENTER);
    label.setVerticalAlignment(IDefaults.CENTER);
    panel.add(label, ULCBorderLayoutPane.CENTER);
    return panel;
  }

  /**
   * Creates a gridlayout pane.
   *
   * @return the created gridlayout pane.
   */
  protected ULCGridLayoutPane createGridLayoutPane() {
    ULCGridLayoutPane pane = new ULCGridLayoutPane();
    return pane;
  }

  /**
   * Creates a cardlayout pane.
   *
   * @return the created cardlayout pane.
   */
  protected ULCCardPane createCardPane() {
    ULCCardPane pane = new ULCCardPane();
    return pane;
  }

  /**
   * Creates a gridbaglayout pane.
   *
   * @return the created gridbaglayout pane.
   */
  protected ULCGridBagLayoutPane createGridBagLayoutPane() {
    ULCGridBagLayoutPane pane = new ULCGridBagLayoutPane();
    return pane;
  }

  // ///////////////////// //
  // Configuration Section //
  // ///////////////////// //

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
   * Sets the mvcBinder.
   *
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
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
   * Gets the translationProvider.
   *
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  /**
   * Sets the listSelectionModelBinder.
   *
   * @param listSelectionModelBinder
   *          the listSelectionModelBinder to set.
   */
  public void setListSelectionModelBinder(
      IListSelectionModelBinder listSelectionModelBinder) {
    this.listSelectionModelBinder = listSelectionModelBinder;
  }

  /**
   * Sets the treeSelectionModelBinder.
   *
   * @param treeSelectionModelBinder
   *          the treeSelectionModelBinder to set.
   */
  public void setTreeSelectionModelBinder(
      ITreeSelectionModelBinder treeSelectionModelBinder) {
    this.treeSelectionModelBinder = treeSelectionModelBinder;
  }

  /**
   * Sets the masterDetailBinder.
   *
   * @param masterDetailBinder
   *          the masterDetailBinder to set.
   */
  public void setMasterDetailBinder(IMasterDetailBinder masterDetailBinder) {
    this.masterDetailBinder = masterDetailBinder;
  }

  /**
   * Sets the maxCharacterLength.
   *
   * @param maxCharacterLength
   *          the maxCharacterLength to set.
   */
  public void setMaxCharacterLength(int maxCharacterLength) {
    this.maxCharacterLength = maxCharacterLength;
  }

  /**
   * Sets the iconFactory.
   *
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<ULCIcon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IIconFactory<ULCIcon> getIconFactory() {
    return iconFactory;
  }

  /**
   * Sets the actionFactory.
   *
   * @param actionFactory
   *          the actionFactory to set.
   */
  public void setActionFactory(
      IActionFactory<IAction, ULCComponent> actionFactory) {
    this.actionFactory = actionFactory;
  }

  /**
   * Gets the actionFactory.
   *
   * @return the actionFactory.
   */
  public IActionFactory<IAction, ULCComponent> getActionFactory() {
    return actionFactory;
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
   * Sets the resetPropertyAction.
   *
   * @param resetPropertyAction
   *          the resetPropertyAction to set.
   */
  public void setResetPropertyAction(IDisplayableAction resetPropertyAction) {
    this.resetPropertyAction = resetPropertyAction;
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
}
