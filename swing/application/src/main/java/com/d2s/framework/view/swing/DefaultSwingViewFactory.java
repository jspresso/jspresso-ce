/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.nachocalendar.components.DefaultDayRenderer;
import net.sf.nachocalendar.components.DefaultHeaderRenderer;

import com.d2s.framework.action.IActionHandler;
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
import com.d2s.framework.binding.masterdetail.IMasterDetailBinder;
import com.d2s.framework.binding.model.ModelRefPropertyConnector;
import com.d2s.framework.binding.swing.CollectionConnectorListModel;
import com.d2s.framework.binding.swing.CollectionConnectorTableModel;
import com.d2s.framework.binding.swing.ConnectorHierarchyTreeModel;
import com.d2s.framework.binding.swing.ConnectorTreeHelper;
import com.d2s.framework.binding.swing.IListSelectionModelBinder;
import com.d2s.framework.binding.swing.ITreeSelectionModelBinder;
import com.d2s.framework.binding.swing.JActionFieldConnector;
import com.d2s.framework.binding.swing.JComboBoxConnector;
import com.d2s.framework.binding.swing.JDateFieldConnector;
import com.d2s.framework.binding.swing.JFormattedFieldConnector;
import com.d2s.framework.binding.swing.JImageConnector;
import com.d2s.framework.binding.swing.JTextAreaConnector;
import com.d2s.framework.binding.swing.JTextFieldConnector;
import com.d2s.framework.binding.swing.JToggleButtonConnector;
import com.d2s.framework.gui.swing.components.JActionField;
import com.d2s.framework.gui.swing.components.JDateField;
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
import com.d2s.framework.model.descriptor.IPercentPropertyDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import com.d2s.framework.model.descriptor.IStringPropertyDescriptor;
import com.d2s.framework.model.descriptor.ITextPropertyDescriptor;
import com.d2s.framework.util.IGate;
import com.d2s.framework.util.format.DurationFormatter;
import com.d2s.framework.util.format.FormatAdapter;
import com.d2s.framework.util.format.IFormatter;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.util.swing.SwingUtil;
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
import com.d2s.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ISplitViewDescriptor;
import com.d2s.framework.view.descriptor.ITabViewDescriptor;
import com.d2s.framework.view.descriptor.ITableViewDescriptor;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ITreeViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.TreeDescriptorHelper;
import com.d2s.framework.view.descriptor.ViewConstraints;
import com.d2s.framework.view.descriptor.basic.BasicListViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * Factory for swing views.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultSwingViewFactory implements
    IViewFactory<JComponent, Icon, Action> {

  private IConfigurableConnectorFactory      connectorFactory;
  private IMvcBinder                         mvcBinder;
  private IMasterDetailBinder                masterDetailBinder;
  private IListSelectionModelBinder          listSelectionModelBinder;
  private ITreeSelectionModelBinder          treeSelectionModelBinder;
  private ITranslationProvider               translationProvider;
  private int                                maxCharacterLength          = 30;
  private int                                maxColumnCharacterLength    = 15;
  private static final char                  TEMPLATE_CHAR               = 'O';
  private static final Date                  TEMPLATE_DATE               = new Date(
                                                                             3661 * 1000);
  private static final Long                  TEMPLATE_DURATION           = new Long(
                                                                             IDurationPropertyDescriptor.ONE_SECOND
                                                                                 + IDurationPropertyDescriptor.ONE_MINUTE
                                                                                 + IDurationPropertyDescriptor.ONE_HOUR
                                                                                 + IDurationPropertyDescriptor.ONE_DAY
                                                                                 + IDurationPropertyDescriptor.ONE_WEEK);
  private static final double                DEF_DISP_MAX_VALUE          = 1000;
  private static final int                   DEF_DISP_MAX_FRACTION_DIGIT = 2;
  private static final double                DEF_DISP_TEMPLATE_PERCENT   = 99;

  private static final Dimension             MINIMUM_AREA_SIZE           = new Dimension(
                                                                             100,
                                                                             100);
  private IIconFactory<Icon>                 iconFactory;
  private IActionFactory<Action, JComponent> actionFactory;
  private IDisplayableAction                 lovAction;
  private IDisplayableAction                 openFileAsBinaryPropertyAction;
  private IDisplayableAction                 saveBinaryPropertyAsFileAction;
  private IDisplayableAction                 resetPropertyAction;

  /**
   * Constructs a new <code>DefaultSwingViewFactory</code> instance.
   */
  public DefaultSwingViewFactory() {
    this(null);
  }

  /**
   * Constructs a new <code>DefaultSwingViewFactory</code> instance.
   * 
   * @param lookAndFeel
   *          the look and feel class name
   */
  public DefaultSwingViewFactory(String lookAndFeel) {
    if (lookAndFeel != null) {
      try {
        UIManager.setLookAndFeel(lookAndFeel);
      } catch (ClassNotFoundException ex) {
        throw new ViewException(ex);
      } catch (InstantiationException ex) {
        throw new ViewException(ex);
      } catch (IllegalAccessException ex) {
        throw new ViewException(ex);
      } catch (UnsupportedLookAndFeelException ex) {
        throw new ViewException(ex);
      }
    }
    JFrame.setDefaultLookAndFeelDecorated(true);
    SwingUtil.installDefaults();
  }

  /**
   * {@inheritDoc}
   */
  public IView<JComponent> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<JComponent> view = null;
    if (viewDescriptor instanceof IComponentViewDescriptor) {
      view = createComponentView((IComponentViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof INestingViewDescriptor) {
      view = createNestingView((INestingViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IImageViewDescriptor) {
      view = createImageView((IImageViewDescriptor) viewDescriptor,
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
    if (viewDescriptor.getForeground() != null) {
      view.getPeer().setForeground(viewDescriptor.getForeground());
    }
    if (viewDescriptor.getBackground() != null) {
      view.getPeer().setBackground(viewDescriptor.getBackground());
    }
    if (viewDescriptor.getFont() != null) {
      view.getPeer().setFont(viewDescriptor.getFont());
    }
    if (viewDescriptor.isReadOnly()) {
      view.getConnector().setLocallyWritable(false);
    }
    if (viewDescriptor.getReadabilityGates() != null) {
      for (IGate gate : viewDescriptor.getReadabilityGates()) {
        view.getConnector().addReadabilityGate(gate.clone());
      }
    }
    if (viewDescriptor.getWritabilityGates() != null) {
      for (IGate gate : viewDescriptor.getWritabilityGates()) {
        view.getConnector().addWritabilityGate(gate.clone());
      }
    }
    if (viewDescriptor.getActions() != null) {
      JToolBar toolBar = createJToolBar();
      toolBar.setRollover(true);
      toolBar.setFloatable(true);
      for (Iterator<Map.Entry<String, List<IDisplayableAction>>> iter = viewDescriptor
          .getActions().entrySet().iterator(); iter.hasNext();) {
        Map.Entry<String, List<IDisplayableAction>> nextActionSet = iter.next();
        for (IDisplayableAction action : nextActionSet.getValue()) {
          Action swingAction = actionFactory.createAction(action,
              actionHandler, view, locale);
          JButton actionButton = createJButton();
          actionButton.setAction(swingAction);
          if (action.getAcceleratorAsString() != null) {
            KeyStroke ks = KeyStroke.getKeyStroke(action
                .getAcceleratorAsString());
            view.getPeer().getActionMap().put(
                swingAction.getValue(Action.NAME), swingAction);
            view.getPeer().getInputMap(
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks,
                swingAction.getValue(Action.NAME));
            String acceleratorString = KeyEvent.getKeyModifiersText(ks
                .getModifiers())
                + "-" + KeyEvent.getKeyText(ks.getKeyCode());
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
      JPanel viewPanel = createJPanel();
      viewPanel.setLayout(new BorderLayout());
      viewPanel.add(toolBar, BorderLayout.NORTH);
      viewPanel.add(view.getPeer(), BorderLayout.CENTER);
      view.setPeer(viewPanel);
    }
    decorateWithBorder(view, locale);
    if (viewDescriptor.getDescription() != null) {
      view.getPeer().setToolTipText(
          viewDescriptor.getI18nDescription(getTranslationProvider(), locale)
              + TOOLTIP_ELLIPSIS);
    }
    return view;
  }

  /**
   * Decorates the created view with the apropriate border.
   * 
   * @param view
   *          the view to descorate.
   * @param locale
   *          the locale to use.
   */
  protected void decorateWithBorder(IView<JComponent> view, Locale locale) {
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

  private ICompositeView<JComponent> createCompositeView(
      ICompositeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<JComponent> view = null;
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
      IView<JComponent> masterView = view.getChildren().get(0);
      view.setConnector(masterView.getConnector());
      for (int i = 1; i < view.getChildren().size(); i++) {
        IView<JComponent> detailView = view.getChildren().get(i);
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
      for (IView<JComponent> childView : view.getChildren()) {
        childView.setParent(view);
        connector.addChildConnector(childView.getConnector());
      }
    }
    return view;
  }

  private ICompositeView<JComponent> createGridView(
      IGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<JComponent> view = null;
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

  private ICompositeView<JComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JTabbedPane viewComponent = createJTabbedPane();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<IView<JComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<JComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      Icon childIcon = iconFactory.getIcon(childViewDescriptor
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

  private IMapView<JComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JPanel viewComponent = createJPanel();
    CardLayout layout = new CardLayout();
    viewComponent.setLayout(layout);
    BasicMapView<JComponent> view = constructMapView(viewComponent,
        viewDescriptor);
    Map<String, IView<JComponent>> childrenViews = new HashMap<String, IView<JComponent>>();

    viewComponent.add(createJPanel(), ICardViewDescriptor.DEFAULT_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<JComponent> childView = createView(childViewDescriptor.getValue(),
          actionHandler, locale);
      viewComponent.add(childView.getPeer(), childViewDescriptor.getKey());
      childrenViews.put(childViewDescriptor.getKey(), childView);
    }
    view.setChildren(childrenViews);
    view.setConnector(createCardViewConnector(view));
    return view;
  }

  private IValueConnector createCardViewConnector(
      final IMapView<JComponent> cardView) {
    IValueConnector cardViewConnector = connectorFactory
        .createValueConnector(cardView.getDescriptor().getName());
    cardViewConnector
        .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

          public void connectorValueChange(ConnectorValueChangeEvent evt) {
            Object cardModel = evt.getNewValue();
            String cardName = ((ICardViewDescriptor) cardView.getDescriptor())
                .getCardNameForModel(cardModel);
            JPanel cardPanel = (JPanel) cardView.getPeer();
            if (cardName != null) {
              IView<JComponent> childCardView = cardView.getChild(cardName);
              if (childCardView != null) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, cardName);
                IValueConnector childCardConnector = childCardView
                    .getConnector();
                if (cardView.getDescriptor() instanceof ModuleCardViewDescriptor) {
                  if (childCardView.getDescriptor() instanceof ICollectionViewDescriptor) {
                    if (cardModel != null && cardModel instanceof BeanModule) {
                      childCardConnector.getModelConnector().setConnectorValue(
                          ((BeanModule) cardModel).getModuleObjects());
                    } else {
                      childCardConnector.getModelConnector().setConnectorValue(
                          cardModel);
                    }
                  } else {
                    if (cardModel != null && cardModel instanceof BeanModule) {
                      childCardConnector.getModelConnector().setConnectorValue(
                          ((BeanModule) cardModel).getModuleObject());
                    } else {
                      childCardConnector.getModelConnector().setConnectorValue(
                          cardModel);
                    }
                  }
                } else {
                  if (childCardConnector != null) {
                    mvcBinder.bind(childCardConnector, cardView.getConnector()
                        .getModelConnector());
                  }
                }
              } else {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel,
                    ICardViewDescriptor.DEFAULT_CARD);
              }
            } else {
              ((CardLayout) cardPanel.getLayout()).show(cardPanel,
                  ICardViewDescriptor.DEFAULT_CARD);
            }
          }
        });
    return cardViewConnector;
  }

  private ICompositeView<JComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JSplitPane viewComponent = createJSplitPane();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<IView<JComponent>>();

    switch (viewDescriptor.getOrientation()) {
      case ISplitViewDescriptor.HORIZONTAL:
        viewComponent.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        break;
      case ISplitViewDescriptor.VERTICAL:
        viewComponent.setOrientation(JSplitPane.VERTICAL_SPLIT);
        break;
      default:
        break;
    }

    if (viewDescriptor.getLeftTopViewDescriptor() != null) {
      IView<JComponent> leftTopView = createView(viewDescriptor
          .getLeftTopViewDescriptor(), actionHandler, locale);
      viewComponent.setLeftComponent(leftTopView.getPeer());
      childrenViews.add(leftTopView);
    }
    if (viewDescriptor.getRightBottomViewDescriptor() != null) {
      IView<JComponent> rightBottomView = createView(viewDescriptor
          .getRightBottomViewDescriptor(), actionHandler, locale);
      viewComponent.setRightComponent(rightBottomView.getPeer());
      childrenViews.add(rightBottomView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private ICompositeView<JComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JPanel viewComponent = createJPanel();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<IView<JComponent>>();

    GridLayout layout = new GridLayout();
    switch (viewDescriptor.getDrivingDimension()) {
      case IEvenGridViewDescriptor.ROW:
        layout.setColumns(viewDescriptor.getDrivingDimensionCellCount());
        layout.setRows(0);
        break;
      case IEvenGridViewDescriptor.COLUMN:
        layout.setRows(viewDescriptor.getDrivingDimensionCellCount());
        layout.setColumns(0);
        break;
      default:
        break;
    }
    layout.setHgap(5);
    layout.setVgap(5);
    viewComponent.setLayout(layout);

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<JComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      viewComponent.add(childView.getPeer());
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private ICompositeView<JComponent> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JPanel viewComponent = createJPanel();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<IView<JComponent>>();

    GridBagLayout layout = new GridBagLayout();
    viewComponent.setLayout(layout);

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<JComponent> childView = createView(childViewDescriptor,
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
    constraints.gridx = viewConstraints.getColumn();
    constraints.gridy = viewConstraints.getRow();
    constraints.gridwidth = viewConstraints.getWidth();
    constraints.gridheight = viewConstraints.getHeight();
    if (viewConstraints.isWidthResizable()) {
      constraints.weightx = 1.0D;
      if (viewConstraints.isHeightResizable()) {
        constraints.fill = GridBagConstraints.BOTH;
      } else {
        constraints.fill = GridBagConstraints.HORIZONTAL;
      }
    }
    if (viewConstraints.isHeightResizable()) {
      constraints.weighty = 1.0D;
      if (viewConstraints.isWidthResizable()) {
        constraints.fill = GridBagConstraints.BOTH;
      } else {
        constraints.fill = GridBagConstraints.VERTICAL;
      }
    }
    return constraints;
  }

  private ICompositeView<JComponent> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JPanel viewComponent = createJPanel();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<IView<JComponent>>();

    BorderLayout layout = new BorderLayout();
    viewComponent.setLayout(layout);

    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<JComponent> eastView = createView(viewDescriptor
          .getEastViewDescriptor(), actionHandler, locale);
      viewComponent.add(eastView.getPeer(), BorderLayout.EAST);
      childrenViews.add(eastView);
    }
    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<JComponent> northView = createView(viewDescriptor
          .getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.add(northView.getPeer(), BorderLayout.NORTH);
      childrenViews.add(northView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<JComponent> centerView = createView(viewDescriptor
          .getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.add(centerView.getPeer(), BorderLayout.CENTER);
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<JComponent> westView = createView(viewDescriptor
          .getWestViewDescriptor(), actionHandler, locale);
      viewComponent.add(westView.getPeer(), BorderLayout.WEST);
      childrenViews.add(westView);
    }
    if (viewDescriptor.getSouthViewDescriptor() != null) {
      IView<JComponent> southView = createView(viewDescriptor
          .getSouthViewDescriptor(), actionHandler, locale);
      viewComponent.add(southView.getPeer(), BorderLayout.SOUTH);
      childrenViews.add(southView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  // //////////// //
  // Tree Section //
  // //////////// //
  private IView<JComponent> createTreeView(ITreeViewDescriptor viewDescriptor,
      @SuppressWarnings("unused")
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
              viewDescriptor, subtreeViewDescriptor, 1);
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
                .getChildDescriptor(), 1);
        simpleConnector.addChildConnector(subtreeConnector);
        simpleConnector.setCollectionConnectorProvider(subtreeConnector);
      }
      connector = simpleConnector;
    }

    if (connector instanceof IConnectorSelector) {
      ((IConnectorSelector) connector).setTracksChildrenSelection(true);
    }

    JTree viewComponent = createJTree();
    ConnectorHierarchyTreeModel treeModel = new ConnectorHierarchyTreeModel(
        connector, viewComponent);
    viewComponent.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    viewComponent.setModel(treeModel);
    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer(viewDescriptor,
        locale));
    treeSelectionModelBinder.bindSelectionModel(connector, viewComponent);
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent.addMouseListener(new PopupListener(viewComponent, view,
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
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row,
        boolean nodeHasFocus) {
      Component renderer = super.getTreeCellRendererComponent(tree, value, sel,
          expanded, leaf, row, nodeHasFocus);
      if (value instanceof IValueConnector) {
        IValueConnector rootConnector = (IValueConnector) tree.getModel()
            .getRoot();
        Icon nodeIcon = null;
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
        setIcon(nodeIcon);
        String labelText = null;
        String toolTipText = null;
        if (value instanceof ICollectionConnector) {
          IListViewDescriptor nodeGroupDescriptor = TreeDescriptorHelper
              .getSubtreeDescriptorFromPath(
                  viewDescriptor.getRootSubtreeDescriptor(),
                  getDescriptorPathFromConnectorTreePath(ConnectorTreeHelper
                      .getTreePathForConnector((IValueConnector) tree
                          .getModel().getRoot(), (IValueConnector) value)))
              .getNodeGroupDescriptor();
          String labelKey = nodeGroupDescriptor.getName();
          if (labelKey == null) {
            labelKey = nodeGroupDescriptor.getModelDescriptor().getName();
          }
          labelText = nodeGroupDescriptor.getI18nName(getTranslationProvider(),
              locale);
          if (nodeGroupDescriptor.getDescription() != null) {
            ToolTipManager.sharedInstance().registerComponent(tree);
            toolTipText = nodeGroupDescriptor.getI18nDescription(
                getTranslationProvider(), locale)
                + TOOLTIP_ELLIPSIS;
          }
          setText(labelText);
        } else {
          if (((IValueConnector) value).getConnectorValue() != null) {
            labelText = ((IValueConnector) value).getConnectorValue()
                .toString();
          } else {
            labelText = "";
          }
        }
        setText(labelText);
        setToolTipText(toolTipText);
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

  private IView<JComponent> createCollectionView(
      ICollectionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<JComponent> view = null;
    if (viewDescriptor instanceof IListViewDescriptor) {
      view = createListView((IListViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITableViewDescriptor) {
      view = createTableView((ITableViewDescriptor) viewDescriptor,
          actionHandler, locale);
    }
    return view;
  }

  private IView<JComponent> createListView(IListViewDescriptor viewDescriptor,
      @SuppressWarnings("unused")
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    ICollectionDescriptorProvider modelDescriptor = ((ICollectionDescriptorProvider) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = connectorFactory
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    ICollectionConnector connector = connectorFactory
        .createCollectionConnector(modelDescriptor.getName(), mvcBinder,
            rowConnectorPrototype);
    JList viewComponent = createJList();
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createColumnConnector(viewDescriptor
          .getRenderedProperty(), modelDescriptor.getCollectionDescriptor()
          .getElementDescriptor());
      rowConnectorPrototype.addChildConnector(cellConnector);
    }
    viewComponent.setCellRenderer(new EvenOddListCellRenderer());
    viewComponent.setModel(new CollectionConnectorListModel(connector));
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), null);
    return view;
  }

  private IView<JComponent> createTableView(
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
    JTable viewComponent = createJTable();
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    JLabel iconLabel = createJLabel();
    iconLabel.setIcon(iconFactory.getIcon(modelDescriptor
        .getCollectionDescriptor().getElementDescriptor().getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));
    iconLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER, iconLabel);
    scrollPane
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    Map<String, Class> columnClassesByIds = new HashMap<String, Class>();
    List<String> columnConnectorKeys = new ArrayList<String>();
    for (String columnId : viewDescriptor.getRenderedProperties()) {
      IValueConnector columnConnector = createColumnConnector(columnId,
          modelDescriptor.getCollectionDescriptor().getElementDescriptor());
      rowConnectorPrototype.addChildConnector(columnConnector);
      columnClassesByIds.put(columnId, modelDescriptor
          .getCollectionDescriptor().getElementDescriptor()
          .getPropertyDescriptor(columnId).getModelType());
      columnConnectorKeys.add(columnId);
    }
    CollectionConnectorTableModel tableModel = new CollectionConnectorTableModel(
        connector, columnConnectorKeys);
    tableModel.setColumnClassesByIds(columnClassesByIds);
    TableSorter sorterDecorator = new TableSorter(tableModel, viewComponent
        .getTableHeader());
    Dimension iconSize = new Dimension(viewComponent.getTableHeader().getFont()
        .getSize(), viewComponent.getTableHeader().getFont().getSize());
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
    for (int i = 0; i < viewDescriptor.getRenderedProperties().size(); i++) {
      TableColumn column = viewComponent.getColumnModel().getColumn(i);
      String propertyName = viewDescriptor.getRenderedProperties().get(i);
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

      IView<JComponent> editorView = createPropertyView(propertyDescriptor,
          null, actionHandler, locale);
      if (editorView.getConnector().getParentConnector() == null) {
        editorView.getConnector().setParentConnector(connector);
      }
      column.setCellEditor(createTableCellEditor(editorView));
      TableCellRenderer cellRenderer = createTableCellRenderer(
          propertyDescriptor, locale);
      if (cellRenderer != null) {
        column.setCellRenderer(cellRenderer);
      } else {
        column.setCellRenderer(new EvenOddTableCellRenderer());
      }
      if (propertyDescriptor instanceof IBooleanPropertyDescriptor
          || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
        column.setPreferredWidth(computePixelWidth(viewComponent, 2));
      } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
        column.setPreferredWidth(computePixelWidth(viewComponent,
            getMaxTranslationLength(
                (IEnumerationPropertyDescriptor) propertyDescriptor, locale)));
      } else {
        column.setPreferredWidth(Math.min(computePixelWidth(viewComponent,
            getFormatLength(createFormatter(propertyDescriptor, locale),
                getTemplateValue(propertyDescriptor))), maxColumnSize));
      }
    }
    viewComponent.addMouseListener(new PopupListener(viewComponent, view,
        actionHandler, locale));
    return view;
  }

  private TableCellEditor createTableCellEditor(IView<JComponent> editorView) {
    SwingViewCellEditorAdapter editor;
    if (editorView.getPeer() instanceof JActionField) {
      editor = new SwingViewCellEditorAdapter(editorView) {

        private static final long serialVersionUID = -1551909997448473681L;

        @Override
        public boolean stopCellEditing() {
          if (((JActionField) getEditorView().getPeer()).isSynchronized()) {
            fireEditingStopped();
            return true;
          }
          ((JActionFieldConnector) getEditorView().getConnector())
              .performActionIfNeeded();
          return false;
        }
      };
    } else {
      editor = new SwingViewCellEditorAdapter(editorView);
    }
    return editor;
  }

  private TableCellRenderer createTableCellRenderer(
      IPropertyDescriptor propertyDescriptor, Locale locale) {
    TableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      cellRenderer = createBooleanTableCellRenderer(
          (IBooleanPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      cellRenderer = createDateTableCellRenderer(
          (IDatePropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      cellRenderer = createDurationTableCellRenderer(
          (IDurationPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
      cellRenderer = createEnumerationTableCellRenderer(
          (IEnumerationPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof INumberPropertyDescriptor) {
      cellRenderer = createNumberTableCellRenderer(
          (INumberPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      cellRenderer = createRelationshipEndTableCellRenderer(
          (IRelationshipEndPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      cellRenderer = createStringTableCellRenderer(
          (IStringPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private TableCellRenderer createBooleanTableCellRenderer(
      @SuppressWarnings("unused")
      IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return new BooleanTableCellRenderer();
  }

  private TableCellRenderer createDateTableCellRenderer(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDateFormatter(
        propertyDescriptor, locale));
  }

  private TableCellRenderer createDurationTableCellRenderer(
      IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDurationFormatter(
        propertyDescriptor, locale));
  }

  private TableCellRenderer createEnumerationTableCellRenderer(
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new TranslatedEnumerationTableCellRenderer(propertyDescriptor,
        locale);
  }

  private TableCellRenderer createNumberTableCellRenderer(
      INumberPropertyDescriptor propertyDescriptor, Locale locale) {
    TableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      cellRenderer = createIntegerTableCellRenderer(
          (IIntegerPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      cellRenderer = createDecimalTableCellRenderer(
          (IDecimalPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private TableCellRenderer createIntegerTableCellRenderer(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createIntegerFormatter(
        propertyDescriptor, locale));
  }

  private TableCellRenderer createDecimalTableCellRenderer(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentTableCellRenderer(
          (IPercentPropertyDescriptor) propertyDescriptor, locale);
    }
    return new FormattedTableCellRenderer(createDecimalFormatter(
        propertyDescriptor, locale));
  }

  private TableCellRenderer createPercentTableCellRenderer(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createPercentFormatter(
        propertyDescriptor, locale));
  }

  private TableCellRenderer createRelationshipEndTableCellRenderer(
      IRelationshipEndPropertyDescriptor propertyDescriptor, Locale locale) {
    TableCellRenderer cellRenderer = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      cellRenderer = createReferenceTableCellRenderer(
          (IReferencePropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      cellRenderer = createCollectionTableCellRenderer(
          (ICollectionPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private TableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused")
      IReferencePropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

  private TableCellRenderer createCollectionTableCellRenderer(
      @SuppressWarnings("unused")
      ICollectionPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

  private TableCellRenderer createStringTableCellRenderer(
      @SuppressWarnings("unused")
      IStringPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      Locale locale) {
    return new FormattedTableCellRenderer(null);
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
     * @param propertyDescriptor
     *          the property descriptor from which the enumeration name is
     *          taken. The prefix used to lookup translation keys in the form
     *          keyPrefix.value is the propertyDescriptor enumeration name.
     * @param locale
     *          the locale to lookup the translation.
     */
    public TranslatedEnumerationTableCellRenderer(
        IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
      super();
      this.propertyDescriptor = propertyDescriptor;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
      setIcon(iconFactory.getIcon(propertyDescriptor.getIconImageURL(String
          .valueOf(value)), IIconFactory.TINY_ICON_SIZE));
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row, column);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(Object value) {
      if (value instanceof IValueConnector) {
        Object connectorValue = ((IValueConnector) value).getConnectorValue();
        if (connectorValue != null) {
          super.setValue(translationProvider.getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  connectorValue), locale));
        } else {
          super.setValue(connectorValue);
        }
      } else {
        if (value != null) {
          super.setValue(translationProvider.getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  value), locale));
        } else {
          super.setValue(value);
        }
      }
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

  private IView<JComponent> createImageView(
      IImageViewDescriptor viewDescriptor, @SuppressWarnings("unused")
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    JLabel imageLabel = createJLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    JImageConnector connector = new JImageConnector(viewDescriptor
        .getModelDescriptor().getName(), imageLabel);
    JPanel viewComponent = createJPanel();
    BorderLayout layout = new BorderLayout();
    viewComponent.setLayout(layout);
    IView<JComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(imageLabel);
    viewComponent.add(scrollPane, BorderLayout.CENTER);
    return view;
  }

  // /////////////// //
  // Nesting Section //
  // /////////////// //

  private IView<JComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = connectorFactory
        .createCompositeValueConnector(viewDescriptor.getModelDescriptor()
            .getName(), null);

    JPanel viewComponent = createJPanel();
    BorderLayout layout = new BorderLayout();
    viewComponent.setLayout(layout);

    IView<JComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    IView<JComponent> nestedView = createView(viewDescriptor
        .getNestedViewDescriptor(), actionHandler, locale);

    connector.addChildConnector(nestedView.getConnector());

    viewComponent.add(nestedView.getPeer(), BorderLayout.CENTER);

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

  private IView<JComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = connectorFactory
        .createCompositeValueConnector(
            getConnectorIdForComponentView(viewDescriptor), null);
    JPanel viewComponent = createJPanel();
    IView<JComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    GridBagLayout layout = new GridBagLayout();
    viewComponent.setLayout(layout);

    int currentX = 0;
    int currentY = 0;

    boolean isSpaceFilled = false;

    for (String propertyName : viewDescriptor.getRenderedProperties()) {
      IPropertyDescriptor propertyDescriptor = ((IComponentDescriptorProvider) viewDescriptor
          .getModelDescriptor()).getComponentDescriptor()
          .getPropertyDescriptor(propertyName);
      if (propertyDescriptor == null) {
        throw new ViewException("Property descriptor [" + propertyName
            + "] does not exist for model descriptor "
            + viewDescriptor.getModelDescriptor().getName() + ".");
      }
      IView<JComponent> propertyView = createPropertyView(propertyDescriptor,
          viewDescriptor.getRenderedChildProperties(propertyName),
          actionHandler, locale);
      propertyView.setParent(view);
      connector.addChildConnector(propertyView.getConnector());
      JLabel propertyLabel = createPropertyLabel(propertyDescriptor,
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
          constraints.insets = new Insets(5, 5, 5, 5);
          if (propertyView.getPeer() instanceof JTextArea
              || propertyView.getPeer() instanceof JList
              || propertyView.getPeer() instanceof JScrollPane
              || propertyView.getPeer() instanceof JTable) {
            constraints.anchor = GridBagConstraints.NORTHEAST;
          } else {
            constraints.anchor = GridBagConstraints.EAST;
          }
          constraints.gridx = currentX * 2;
          constraints.gridy = currentY;
          break;
        case IComponentViewDescriptor.ABOVE:
          constraints.insets = new Insets(5, 5, 0, 5);
          constraints.anchor = GridBagConstraints.WEST;
          constraints.gridx = currentX;
          constraints.gridy = currentY * 2;
          constraints.gridwidth = propertyWidth;
          break;
        default:
          break;
      }
      viewComponent.add(propertyLabel, constraints);

      // component positionning
      switch (viewDescriptor.getLabelsPosition()) {
        case IComponentViewDescriptor.ASIDE:
          constraints.gridx++;
          constraints.insets = new Insets(5, 0, 5, 5);
          constraints.gridwidth = propertyWidth * 2 - 1;
          break;
        case IComponentViewDescriptor.ABOVE:
          constraints.gridy++;
          constraints.insets = new Insets(0, 5, 0, 5);
          constraints.gridwidth = propertyWidth;
          break;
        default:
          break;
      }

      constraints.anchor = GridBagConstraints.WEST;
      // constraints.weightx = 1.0;
      constraints.weightx = propertyView.getPeer().getPreferredSize().width;
      if (propertyView.getPeer() instanceof JTextArea
          || propertyView.getPeer() instanceof JList
          || propertyView.getPeer() instanceof JScrollPane
          || propertyView.getPeer() instanceof JTable) {
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        isSpaceFilled = true;
      } else {
        constraints.fill = GridBagConstraints.NONE;
      }
      viewComponent.add(propertyView.getPeer(), constraints);

      currentX += propertyWidth;
    }
    if (!isSpaceFilled) {
      JPanel filler = createJPanel();
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridx = 0;
      constraints.weightx = 1.0;
      constraints.weighty = 1.0;
      constraints.fill = GridBagConstraints.BOTH;
      switch (viewDescriptor.getLabelsPosition()) {
        case IComponentViewDescriptor.ASIDE:
          constraints.gridy = currentY + 1;
          constraints.gridwidth = viewDescriptor.getColumnCount() * 2;
          break;
        case IComponentViewDescriptor.ABOVE:
          constraints.gridy = (currentY + 1) * 2;
          constraints.gridwidth = viewDescriptor.getColumnCount();
          break;
        default:
          break;
      }
      viewComponent.add(filler, constraints);
    }
    return view;
  }

  private JLabel createPropertyLabel(IPropertyDescriptor propertyDescriptor,
      JComponent propertyComponent, Locale locale) {
    JLabel propertyLabel = createJLabel();
    StringBuffer labelText = new StringBuffer(propertyDescriptor.getI18nName(
        getTranslationProvider(), locale));
    if (propertyDescriptor.isMandatory()) {
      labelText.append("*");
      propertyLabel.setForeground(Color.RED);
    }
    propertyLabel.setText(labelText.toString());
    propertyLabel.setLabelFor(propertyComponent);
    return propertyLabel;
  }

  private IView<JComponent> createPropertyView(
      IPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<JComponent> view = null;
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      view = createBooleanPropertyView(
          (IBooleanPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      view = createDatePropertyView(
          (IDatePropertyDescriptor) propertyDescriptor, actionHandler, locale);
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

  private IView<JComponent> createRelationshipEndPropertyView(
      IRelationshipEndPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<JComponent> view = null;

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

  private IView<JComponent> createNumberPropertyView(
      INumberPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      IActionHandler actionHandler, Locale locale) {
    IView<JComponent> view = null;
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      view = createIntegerPropertyView(
          (IIntegerPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      view = createDecimalPropertyView(
          (IDecimalPropertyDescriptor) propertyDescriptor, locale);
    }
    return view;
  }

  private IView<JComponent> createDatePropertyView(
      IDatePropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      IActionHandler actionHandler, Locale locale) {
    JDateField viewComponent = createJDateField();
    DateFormat format = createDateFormat(propertyDescriptor, locale);
    viewComponent.getFormattedTextField().setFormatterFactory(
        new DefaultFormatterFactory(new DateFormatter(format)));
    JDateFieldConnector connector = new JDateFieldConnector(propertyDescriptor
        .getName(), viewComponent);
    adjustSizes(viewComponent, createFormatter(format),
        getDateTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createStringPropertyView(
      IStringPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof ITextPropertyDescriptor) {
      return createTextPropertyView(
          (ITextPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    JTextField viewComponent = createJTextField();
    JTextFieldConnector connector = new JTextFieldConnector(propertyDescriptor
        .getName(), viewComponent);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createTextPropertyView(
      ITextPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    JTextArea viewComponent = createJTextArea();
    viewComponent.setLineWrap(true);
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    scrollPane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    JTextAreaConnector connector = new JTextAreaConnector(propertyDescriptor
        .getName(), viewComponent);
    return constructView(scrollPane, null, connector);
  }

  private IView<JComponent> createCollectionPropertyView(
      ICollectionPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {

    IView<JComponent> view;
    if (renderedChildProperties != null && renderedChildProperties.size() > 1) {
      BasicTableViewDescriptor viewDescriptor = new BasicTableViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      viewDescriptor.setRenderedProperties(renderedChildProperties);
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

  private IView<JComponent> createReferencePropertyView(
      IReferencePropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JActionField viewComponent = createJActionField(true);
    JActionFieldConnector connector = new JActionFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    Action fieldAction = actionFactory.createAction(lovAction, actionHandler,
        viewComponent, propertyDescriptor, connector, locale);
    fieldAction.putValue(Action.NAME, getTranslationProvider().getTranslation(
        "lov.element.name",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            translationProvider, locale)}, locale));
    fieldAction.putValue(Action.SHORT_DESCRIPTION, getTranslationProvider()
        .getTranslation(
            "lov.element.description",
            new Object[] {propertyDescriptor.getReferencedDescriptor()
                .getI18nName(translationProvider, locale)}, locale)
        + TOOLTIP_ELLIPSIS);
    if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
      fieldAction.putValue(Action.SMALL_ICON, iconFactory.getIcon(
          propertyDescriptor.getReferencedDescriptor().getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }
    viewComponent.setActions(Collections.singletonList(fieldAction));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JActionField viewComponent = createJActionField(false);
    JActionFieldConnector connector = new JActionFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    Action openAction = actionFactory.createAction(
        openFileAsBinaryPropertyAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    Action saveAction = actionFactory.createAction(
        saveBinaryPropertyAsFileAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    Action resetAction = actionFactory.createAction(resetPropertyAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    viewComponent.setActions(Arrays.asList(new Action[] {openAction,
        saveAction, resetAction}));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createDecimalPropertyView(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(
          (IPercentPropertyDescriptor) propertyDescriptor, locale);
    }
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    adjustSizes(viewComponent, formatter,
        getDecimalTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createPercentPropertyView(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    adjustSizes(viewComponent, formatter,
        getPercentTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createIntegerPropertyView(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    adjustSizes(viewComponent, formatter,
        getIntegerTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      IActionHandler actionHandler, Locale locale) {
    JComboBox viewComponent = createJComboBox();
    for (Object enumElement : propertyDescriptor.getEnumerationValues()) {
      viewComponent.addItem(enumElement);
    }
    viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(
        propertyDescriptor, locale));

    JComboBoxConnector connector = new JComboBoxConnector(propertyDescriptor
        .getName(), viewComponent);
    return constructView(viewComponent, null, connector);
  }

  private final class TranslatedEnumerationListCellRenderer extends
      DefaultListCellRenderer {

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
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value,
          index, isSelected, cellHasFocus);
      label
          .setIcon(iconFactory.getIcon(propertyDescriptor
              .getIconImageURL(String.valueOf(value)),
              IIconFactory.TINY_ICON_SIZE));
      if (value != null) {
        setText(translationProvider.getTranslation(computeEnumerationKey(
            propertyDescriptor.getEnumerationName(), value), locale));
      }
      return label;
    }
  }

  private String computeEnumerationKey(String keyPrefix, Object value) {
    return keyPrefix + "." + value;
  }

  private IView<JComponent> createDurationPropertyView(
      IDurationPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      IActionHandler actionHandler, Locale locale) {
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    adjustSizes(viewComponent, formatter,
        getDurationTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> createBooleanPropertyView(
      IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    JCheckBox viewComponent = createJCheckBox();
    JToggleButtonConnector connector = new JToggleButtonConnector(
        propertyDescriptor.getName(), viewComponent);
    return constructView(viewComponent, null, connector);
  }

  private IView<JComponent> constructView(JComponent viewComponent,
      IViewDescriptor descriptor, IValueConnector connector) {
    BasicView<JComponent> view = new BasicView<JComponent>(viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

  private BasicCompositeView<JComponent> constructCompositeView(
      JComponent viewComponent, IViewDescriptor descriptor) {
    BasicCompositeView<JComponent> view = new BasicCompositeView<JComponent>(
        viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  private BasicMapView<JComponent> constructMapView(JComponent viewComponent,
      IViewDescriptor descriptor) {
    BasicMapView<JComponent> view = new BasicMapView<JComponent>(viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  // ////////////////// //
  // Popup menu Section //
  // ////////////////// //

  private final class PopupListener extends MouseAdapter {

    private JComponent        sourceComponent;
    private IView<JComponent> view;
    private IActionHandler    actionHandler;
    private Locale            locale;

    /**
     * Constructs a new <code>PopupListener</code> instance.
     * 
     * @param sourceComponent
     * @param view
     * @param actionHandler
     * @param locale
     */
    public PopupListener(JComponent sourceComponent, IView<JComponent> view,
        IActionHandler actionHandler, Locale locale) {
      this.sourceComponent = sourceComponent;
      this.view = view;
      this.actionHandler = actionHandler;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent evt) {
      maybeShowPopup(evt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
      maybeShowPopup(evt);
    }

    private void maybeShowPopup(MouseEvent evt) {
      if (evt.isPopupTrigger()) {
        showPopupMenu(sourceComponent, view, evt, actionHandler, locale);
      }
    }
  }

  private void showPopupMenu(JComponent sourceComponent,
      IView<JComponent> view, MouseEvent evt, IActionHandler actionHandler,
      Locale locale) {
    if (sourceComponent instanceof JTree) {
      showJTreePopupMenu((JTree) sourceComponent, view, evt, actionHandler,
          locale);
    } else if (sourceComponent instanceof JTable) {
      showJTablePopupMenu((JTable) sourceComponent, view, evt, actionHandler,
          locale);
    }
  }

  private void showJTreePopupMenu(JTree tree, IView<JComponent> treeView,
      MouseEvent evt, IActionHandler actionHandler, Locale locale) {
    TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());
    if (path == null) {
      return;
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
      return;
    }

    JPopupMenu popupMenu = createJPopupMenu(tree, actionMap, modelDescriptor,
        viewDescriptor, viewConnector, actionHandler, locale);
    popupMenu.show(tree, evt.getX(), evt.getY());
  }

  private void showJTablePopupMenu(JTable table, IView<JComponent> tableView,
      MouseEvent evt, IActionHandler actionHandler, Locale locale) {
    int row = table.rowAtPoint(evt.getPoint());
    if (row < 0) {
      return;
    }

    if (!table.isRowSelected(row)) {
      table.setRowSelectionInterval(row, row);
    }

    IValueConnector elementConnector = tableView.getConnector();
    IModelDescriptor modelDescriptor = tableView.getDescriptor()
        .getModelDescriptor();
    Map<String, List<IDisplayableAction>> actionMap = ((ICollectionViewDescriptor) tableView
        .getDescriptor()).getActions();

    if (actionMap == null) {
      return;
    }

    JPopupMenu popupMenu = createJPopupMenu(table, actionMap, modelDescriptor,
        tableView.getDescriptor(), elementConnector, actionHandler, locale);
    popupMenu.show(table, evt.getX(), evt.getY());
  }

  private JPopupMenu createJPopupMenu(JComponent sourceComponent,
      Map<String, List<IDisplayableAction>> actionMap,
      IModelDescriptor modelDescriptor, IViewDescriptor viewDescriptor,
      IValueConnector viewConnector, IActionHandler actionHandler, Locale locale) {
    JPopupMenu popupMenu = createJPopupMenu();
    JLabel titleLabel = createJLabel();
    titleLabel.setText(viewDescriptor.getI18nName(getTranslationProvider(),
        locale));
    titleLabel.setIcon(iconFactory.getIcon(viewDescriptor.getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    popupMenu.add(titleLabel);
    popupMenu.addSeparator();
    for (Iterator<Map.Entry<String, List<IDisplayableAction>>> iter = actionMap
        .entrySet().iterator(); iter.hasNext();) {
      Map.Entry<String, List<IDisplayableAction>> nextActionSet = iter.next();
      for (IDisplayableAction action : nextActionSet.getValue()) {
        Action swingAction = actionFactory.createAction(action, actionHandler,
            sourceComponent, modelDescriptor, viewConnector, locale);
        JMenuItem actionItem = createJMenuItem();
        actionItem.setAction(swingAction);
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

  private DateFormat createDateFormat(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    DateFormat format;
    if (IDatePropertyDescriptor.DATE_TYPE.equals(propertyDescriptor.getType())) {
      format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    } else if (IDatePropertyDescriptor.TIME_TYPE.equals(propertyDescriptor
        .getType())) {
      format = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
    } else {
      format = DateFormat.getDateTimeInstance(DateFormat.SHORT,
          DateFormat.SHORT, locale);
    }
    return format;
  }

  private IFormatter createDateFormatter(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createDateFormat(propertyDescriptor, locale));
  }

  private IFormatter createFormatter(Format format) {
    return new FormatAdapter(format);
  }

  private Object getDurationTemplateValue(@SuppressWarnings("unused")
  IDurationPropertyDescriptor propertyDescriptor) {
    return TEMPLATE_DURATION;
  }

  private IFormatter createDurationFormatter(@SuppressWarnings("unused")
  IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new DurationFormatter(locale);
  }

  private Object getStringTemplateValue(
      IStringPropertyDescriptor propertyDescriptor) {
    StringBuffer templateValue = new StringBuffer();
    if (propertyDescriptor.getMaxLength() != null) {
      for (int i = 0; i < propertyDescriptor.getMaxLength().intValue(); i++) {
        templateValue.append(TEMPLATE_CHAR);
      }
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

  private IFormatter createDecimalFormatter(@SuppressWarnings("unused")
  IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(NumberFormat.getNumberInstance(locale));
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

  private IFormatter createPercentFormatter(@SuppressWarnings("unused")
  IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(NumberFormat.getPercentInstance(locale));
  }

  private Object getIntegerTemplateValue(
      IIntegerPropertyDescriptor propertyDescriptor) {
    double templateValue = DEF_DISP_MAX_VALUE;
    if (propertyDescriptor.getMaxValue() != null) {
      templateValue = propertyDescriptor.getMaxValue().doubleValue();
    }
    return new Integer((int) templateValue);
  }

  private IFormatter createIntegerFormatter(@SuppressWarnings("unused")
  IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(NumberFormat.getIntegerInstance(locale));
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

  private int getMaxTranslationLength(
      IEnumerationPropertyDescriptor descriptor, Locale locale) {
    int maxTranslationLength = -1;
    if (translationProvider != null) {
      for (Object enumerationValue : descriptor.getEnumerationValues()) {
        String translation = translationProvider.getTranslation(
            computeEnumerationKey(descriptor.getEnumerationName(),
                enumerationValue), locale);
        if (translation.length() > maxTranslationLength) {
          maxTranslationLength = translation.length();
        }
      }
    }
    if (maxTranslationLength == -1 || maxTranslationLength > maxCharacterLength) {
      maxTranslationLength = maxCharacterLength;
    }
    return maxTranslationLength;
  }

  private void adjustSizes(Component component, IFormatter formatter,
      Object templateValue) {
    Dimension size = new Dimension(computePixelWidth(component,
        getFormatLength(formatter, templateValue)), component
        .getPreferredSize().height);
    component.setPreferredSize(size);
    component.setMinimumSize(size);
    component.setMaximumSize(size);
  }

  private int computePixelWidth(Component component, int characterLength) {
    int charLength = maxCharacterLength + 1;
    if (characterLength > 0 && characterLength < maxCharacterLength) {
      charLength = characterLength + 1;
    }
    return (int) ((component.getFont().getSize() * charLength) / 1.5);
  }

  /**
   * Creates a text field.
   * 
   * @return the created text field.
   */
  protected JTextField createJTextField() {
    JTextField textField = new JTextField();
    SwingUtil.enableSelectionOnFocusGained(textField);
    return textField;
  }

  /**
   * Creates a text area.
   * 
   * @return the created text area.
   */
  protected JTextArea createJTextArea() {
    JTextArea textArea = new JTextArea();
    textArea.setDragEnabled(true);
    textArea.setWrapStyleWord(true);
    return textArea;
  }

  /**
   * Creates an action field.
   * 
   * @param showTextField
   *          is the text field visible to the user.
   * @return the created action field.
   */
  protected JActionField createJActionField(boolean showTextField) {
    return new JActionField(showTextField);
  }

  /**
   * Creates a date field.
   * 
   * @return the created date field.
   */
  protected JDateField createJDateField() {
    JDateField dateField = new JDateField();
    dateField.setRenderer(new DefaultDayRenderer());
    dateField.setHeaderRenderer(new DefaultHeaderRenderer());
    return dateField;
  }

  /**
   * Creates a combo box.
   * 
   * @return the created combo box.
   */
  protected JComboBox createJComboBox() {
    return new JComboBox();
  }

  /**
   * Creates a label.
   * 
   * @return the created label.
   */
  protected JLabel createJLabel() {
    return new JLabel();
  }

  /**
   * Creates a tool bar.
   * 
   * @return the created tool bar.
   */
  protected JToolBar createJToolBar() {
    return new JToolBar();
  }

  /**
   * Creates a tabbed pane.
   * 
   * @return the created tabbed pane.
   */
  protected JTabbedPane createJTabbedPane() {
    return new JTabbedPane();
  }

  /**
   * Creates a split pane.
   * 
   * @return the created split pane.
   */
  protected JSplitPane createJSplitPane() {
    JSplitPane splitPane = new JSplitPane();
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    return splitPane;
  }

  /**
   * Creates a tree.
   * 
   * @return the created tree.
   */
  protected JTree createJTree() {
    JTree tree = new JTree();
    tree.setDragEnabled(true);
    return tree;
  }

  /**
   * Creates a table.
   * 
   * @return the created table.
   */
  protected JTable createJTable() {
    JTable table = new JTable() {

      private static final long serialVersionUID = -2766744091893464462L;

      /**
       * Override this method to fix a bug in the JVM which causes the table to
       * start editing when a mnuemonic key or function key is pressed.
       */
      @Override
      protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
          int condition, boolean pressed) {
        if (SwingUtilities.getUIInputMap(this, condition) != null
            && SwingUtilities.getUIInputMap(this, condition).get(ks) != null) {
          return super.processKeyBinding(ks, e, condition, pressed);
        }
        /**
         * ignore all keys that have not been registered
         */
        if (getInputMap(condition).get(ks) != null) {
          return false;
        }
        boolean foundInAncestors = false;
        JComponent parent = null;
        if (getParent() instanceof JComponent) {
          parent = (JComponent) getParent();
        }
        while (!foundInAncestors && parent != null) {
          if (parent.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
              .get(ks) != null) {
            foundInAncestors = true;
          }
          if (parent.getParent() instanceof JComponent) {
            parent = (JComponent) parent.getParent();
          } else {
            parent = null;
          }
        }
        if (!foundInAncestors) {
          return super.processKeyBinding(ks, e, condition, pressed);
        }
        return false;
      }
    };
    table.setSurrendersFocusOnKeystroke(true);
    table.setDragEnabled(true);
    return table;
  }

  /**
   * Creates a list.
   * 
   * @return the created list.
   */
  protected JList createJList() {
    JList list = new JList();
    list.setDragEnabled(true);
    return list;
  }

  /**
   * Creates a button.
   * 
   * @return the created button.
   */
  protected JButton createJButton() {
    JButton button = new JButton();
    button.setMultiClickThreshhold(300);
    return button;
  }

  /**
   * Creates a popup menu.
   * 
   * @return the created popup menu.
   */
  protected JPopupMenu createJPopupMenu() {
    return new JPopupMenu();
  }

  /**
   * Creates a menu item.
   * 
   * @return the created menu item.
   */
  protected JMenuItem createJMenuItem() {
    return new JMenuItem();
  }

  /**
   * Creates a scroll pane.
   * 
   * @return the created scroll pane.
   */
  protected JScrollPane createJScrollPane() {
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setMinimumSize(MINIMUM_AREA_SIZE);
    return scrollPane;
  }

  /**
   * Creates a check box.
   * 
   * @return the created check box.
   */
  protected JCheckBox createJCheckBox() {
    return new JCheckBox();
  }

  /**
   * Creates a panel.
   * 
   * @return the created panel.
   */
  protected JPanel createJPanel() {
    JPanel panel = new JPanel();
    return panel;
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
  public void setIconFactory(IIconFactory<Icon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  public IIconFactory<Icon> getIconFactory() {
    return iconFactory;
  }

  /**
   * Sets the actionFactory.
   * 
   * @param actionFactory
   *          the actionFactory to set.
   */
  public void setActionFactory(IActionFactory<Action, JComponent> actionFactory) {
    this.actionFactory = actionFactory;
  }

  /**
   * Gets the actionFactory.
   * 
   * @return the actionFactory.
   */
  public IActionFactory<Action, JComponent> getActionFactory() {
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
}
