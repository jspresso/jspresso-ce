/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.wings;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.IOException;
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

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SCardLayout;
import org.wings.SCellRendererPane;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDefaultListCellRenderer;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SGridBagLayout;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SInternalFrame;
import org.wings.SLabel;
import org.wings.SList;
import org.wings.SMenuItem;
import org.wings.SPanel;
import org.wings.SPasswordField;
import org.wings.SPopupMenu;
import org.wings.SScrollPane;
import org.wings.SSeparator;
import org.wings.SSplitPane;
import org.wings.STabbedPane;
import org.wings.STable;
import org.wings.STextArea;
import org.wings.STextField;
import org.wings.SToolBar;
import org.wings.STree;
import org.wings.border.SEmptyBorder;
import org.wings.border.SEtchedBorder;
import org.wings.io.Device;
import org.wings.table.SDefaultTableCellRenderer;
import org.wings.table.STableCellEditor;
import org.wings.table.STableCellRenderer;
import org.wings.table.STableColumn;
import org.wings.text.SDateFormatter;
import org.wings.tree.SDefaultTreeCellRenderer;
import org.wingx.XCalendar;

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
import com.d2s.framework.binding.wings.CollectionConnectorListModel;
import com.d2s.framework.binding.wings.CollectionConnectorTableModel;
import com.d2s.framework.binding.wings.ConnectorHierarchyTreeModel;
import com.d2s.framework.binding.wings.ConnectorTreeHelper;
import com.d2s.framework.binding.wings.IListSelectionModelBinder;
import com.d2s.framework.binding.wings.ITreeSelectionModelBinder;
import com.d2s.framework.binding.wings.SActionFieldConnector;
import com.d2s.framework.binding.wings.SCheckBoxConnector;
import com.d2s.framework.binding.wings.SColorPickerConnector;
import com.d2s.framework.binding.wings.SComboBoxConnector;
import com.d2s.framework.binding.wings.SFormattedFieldConnector;
import com.d2s.framework.binding.wings.SImageConnector;
import com.d2s.framework.binding.wings.SPasswordFieldConnector;
import com.d2s.framework.binding.wings.SPercentFieldConnector;
import com.d2s.framework.binding.wings.SReferenceFieldConnector;
import com.d2s.framework.binding.wings.STextAreaConnector;
import com.d2s.framework.binding.wings.STextFieldConnector;
import com.d2s.framework.binding.wings.XCalendarConnector;
import com.d2s.framework.gui.wings.components.SActionField;
import com.d2s.framework.gui.wings.components.SColorPicker;
import com.d2s.framework.model.descriptor.IBinaryPropertyDescriptor;
import com.d2s.framework.model.descriptor.IBooleanPropertyDescriptor;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IColorPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;
import com.d2s.framework.model.descriptor.IDatePropertyDescriptor;
import com.d2s.framework.model.descriptor.IDecimalPropertyDescriptor;
import com.d2s.framework.model.descriptor.IDurationPropertyDescriptor;
import com.d2s.framework.model.descriptor.IEnumerationPropertyDescriptor;
import com.d2s.framework.model.descriptor.IIntegerPropertyDescriptor;
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
import com.d2s.framework.util.format.NullableSimpleDateFormat;
import com.d2s.framework.util.gate.IGate;
import com.d2s.framework.util.gui.ColorHelper;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.util.wings.WingsUtil;
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

/**
 * Factory for swing views.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultWingsViewFactory implements
    IViewFactory<SComponent, SIcon, Action> {

  private static final int                   DEF_DISP_MAX_FRACTION_DIGIT = 2;
  private static final double                DEF_DISP_MAX_VALUE          = 1000;
  private static final double                DEF_DISP_TEMPLATE_PERCENT   = 99;
  private static final char                  TEMPLATE_CHAR               = 'O';
  private static final Date                  TEMPLATE_DATE               = new Date(
                                                                             3661 * 1000);
  private static final Long                  TEMPLATE_DURATION           = new Long(
                                                                             IDurationPropertyDescriptor.ONE_SECOND
                                                                                 + IDurationPropertyDescriptor.ONE_MINUTE
                                                                                 + IDurationPropertyDescriptor.ONE_HOUR
                                                                                 + IDurationPropertyDescriptor.ONE_DAY
                                                                                 + IDurationPropertyDescriptor.ONE_WEEK);
  private static final Date                  TEMPLATE_TIME               = new Date(
                                                                             3661 * 1000);
  private IActionFactory<Action, SComponent> actionFactory;
  private IDisplayableAction                 binaryPropertyInfoAction;
  private IConfigurableConnectorFactory      connectorFactory;
  private IIconFactory<SIcon>                iconFactory;
  private IListSelectionModelBinder          listSelectionModelBinder;
  private IDisplayableAction                 lovAction;
  private IMasterDetailBinder                masterDetailBinder;
  private int                                maxCharacterLength          = 30;

  private int                                maxColumnCharacterLength    = 15;
  private IMvcBinder                         mvcBinder;
  private IDisplayableAction                 openFileAsBinaryPropertyAction;
  private IDisplayableAction                 resetPropertyAction;
  private IDisplayableAction                 saveBinaryPropertyAsFileAction;
  private ITranslationProvider               translationProvider;
  private ITreeSelectionModelBinder          treeSelectionModelBinder;

  /**
   * {@inheritDoc}
   */
  public IView<SComponent> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<SComponent> view = null;
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
    if (view != null) {
      try {
        if (actionHandler != null) {
          actionHandler.checkAccess(viewDescriptor);
        }
        if (viewDescriptor.getForeground() != null) {
          view.getPeer().setForeground(viewDescriptor.getForeground());
        }
        if (viewDescriptor.getBackground() != null) {
          view.getPeer().setBackground(viewDescriptor.getBackground());
        }
        if (viewDescriptor.getFont() != null) {
          view.getPeer().setFont(
              new SFont(viewDescriptor.getFont().getFontName(), viewDescriptor
                  .getFont().getStyle(), viewDescriptor.getFont().getSize()));
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
        if (viewDescriptor.getDescription() != null) {
          view.getPeer().setToolTipText(
              viewDescriptor.getI18nDescription(getTranslationProvider(),
                  locale)
                  + TOOLTIP_ELLIPSIS);
        }
        if (viewDescriptor.getActions() != null) {
          SToolBar toolBar = createSToolBar();
          for (Iterator<Map.Entry<String, List<IDisplayableAction>>> iter = viewDescriptor
              .getActions().entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, List<IDisplayableAction>> nextActionSet = iter
                .next();
            for (IDisplayableAction action : nextActionSet.getValue()) {
              Action swingAction = actionFactory.createAction(action,
                  actionHandler, view, locale);
              SButton actionButton = createSButton();
              actionButton.setShowAsFormComponent(false);
              actionButton.setAction(swingAction);
              if (action.getAcceleratorAsString() != null) {
                KeyStroke ks = KeyStroke.getKeyStroke(action
                    .getAcceleratorAsString());
                view.getPeer().getActionMap().put(
                    swingAction.getValue(Action.NAME), swingAction);
                view.getPeer().getInputMap(
                    SComponent.WHEN_FOCUSED_OR_ANCESTOR_OF_FOCUSED_COMPONENT)
                    .put(ks, swingAction.getValue(Action.NAME));
                String acceleratorString = KeyEvent.getKeyModifiersText(ks
                    .getModifiers())
                    + "-" + KeyEvent.getKeyText(ks.getKeyCode());
                actionButton.setToolTipText("<HTML>"
                    + actionButton.getToolTipText()
                    + " <FONT SIZE=\"-2\" COLOR=\"#993366\">"
                    + acceleratorString + "</FONT></HTML>");
              }
              actionButton.setText("");
              toolBar.add(actionButton);
            }
            if (iter.hasNext()) {
              toolBar.add(new SSeparator());
            }
          }
          SPanel viewPanel = createSPanel();
          viewPanel.setLayout(new SBorderLayout());
          viewPanel.add(toolBar, SBorderLayout.NORTH);
          viewPanel.add(view.getPeer(), SBorderLayout.CENTER);
          view.setPeer(viewPanel);
        }
        decorateWithBorder(view, locale);
      } catch (SecurityException ex) {
        view.setPeer(createSecurityPanel());
      }
    }
    return view;
  }

  /**
   * Gets the actionFactory.
   * 
   * @return the actionFactory.
   */
  public IActionFactory<Action, SComponent> getActionFactory() {
    return actionFactory;
  }

  // ///////////////// //
  // Composite Section //
  // ///////////////// //

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  public IIconFactory<SIcon> getIconFactory() {
    return iconFactory;
  }

  /**
   * Sets the actionFactory.
   * 
   * @param actionFactory
   *            the actionFactory to set.
   */
  public void setActionFactory(IActionFactory<Action, SComponent> actionFactory) {
    this.actionFactory = actionFactory;
  }

  /**
   * Sets the binaryPropertyInfoAction.
   * 
   * @param binaryPropertyInfoAction
   *            the binaryPropertyInfoAction to set.
   */
  public void setBinaryPropertyInfoAction(
      IDisplayableAction binaryPropertyInfoAction) {
    this.binaryPropertyInfoAction = binaryPropertyInfoAction;
  }

  /**
   * Sets the connectorFactory.
   * 
   * @param connectorFactory
   *            the connectorFactory to set.
   */
  public void setConnectorFactory(IConfigurableConnectorFactory connectorFactory) {
    this.connectorFactory = connectorFactory;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *            the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<SIcon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the listSelectionModelBinder.
   * 
   * @param listSelectionModelBinder
   *            the listSelectionModelBinder to set.
   */
  public void setListSelectionModelBinder(
      IListSelectionModelBinder listSelectionModelBinder) {
    this.listSelectionModelBinder = listSelectionModelBinder;
  }

  /**
   * Sets the lovAction.
   * 
   * @param lovAction
   *            the lovAction to set.
   */
  public void setLovAction(IDisplayableAction lovAction) {
    this.lovAction = lovAction;
  }

  /**
   * Sets the masterDetailBinder.
   * 
   * @param masterDetailBinder
   *            the masterDetailBinder to set.
   */
  public void setMasterDetailBinder(IMasterDetailBinder masterDetailBinder) {
    this.masterDetailBinder = masterDetailBinder;
  }

  /**
   * Sets the maxCharacterLength.
   * 
   * @param maxCharacterLength
   *            the maxCharacterLength to set.
   */
  public void setMaxCharacterLength(int maxCharacterLength) {
    this.maxCharacterLength = maxCharacterLength;
  }

  /**
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *            the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Sets the openFileAsBinaryPropertyAction.
   * 
   * @param openFileAsBinaryPropertyAction
   *            the openFileAsBinaryPropertyAction to set.
   */
  public void setOpenFileAsBinaryPropertyAction(
      IDisplayableAction openFileAsBinaryPropertyAction) {
    this.openFileAsBinaryPropertyAction = openFileAsBinaryPropertyAction;
  }

  /**
   * Sets the resetPropertyAction.
   * 
   * @param resetPropertyAction
   *            the resetPropertyAction to set.
   */
  public void setResetPropertyAction(IDisplayableAction resetPropertyAction) {
    this.resetPropertyAction = resetPropertyAction;
  }

  /**
   * Sets the saveBinaryPropertyAsFileAction.
   * 
   * @param saveBinaryPropertyAsFileAction
   *            the saveBinaryPropertyAsFileAction to set.
   */
  public void setSaveBinaryPropertyAsFileAction(
      IDisplayableAction saveBinaryPropertyAsFileAction) {
    this.saveBinaryPropertyAsFileAction = saveBinaryPropertyAsFileAction;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *            the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  /**
   * Sets the treeSelectionModelBinder.
   * 
   * @param treeSelectionModelBinder
   *            the treeSelectionModelBinder to set.
   */
  public void setTreeSelectionModelBinder(
      ITreeSelectionModelBinder treeSelectionModelBinder) {
    this.treeSelectionModelBinder = treeSelectionModelBinder;
  }

  /**
   * Creates a date field.
   * 
   * @return the created date field.
   */
  protected XCalendar createDateField() {
    XCalendar dateField = new XCalendar();
    dateField.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return dateField;
  }

  // ////////////////// //
  // Collection Section //
  // ////////////////// //

  /**
   * Creates an action field.
   * 
   * @param showTextField
   *            is the text field visible to the user.
   * @return the created action field.
   */
  protected SActionField createSActionField(boolean showTextField) {
    SActionField actionField = new SActionField(showTextField);
    actionField.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return actionField;
  }

  /**
   * Creates a button.
   * 
   * @return the created button.
   */
  protected SButton createSButton() {
    SButton button = new SButton();
    return button;
  }

  /**
   * Creates a check box.
   * 
   * @return the created check box.
   */
  protected SCheckBox createSCheckBox() {
    SCheckBox checkBox = new SCheckBox();
    checkBox.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return checkBox;
  }

  /**
   * Creates an color picker.
   * 
   * @return the created color picker.
   */
  protected SColorPicker createSColorPicker() {
    SColorPicker colorPicker = new SColorPicker();
    colorPicker.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return colorPicker;
  }

  /**
   * Creates a combo box.
   * 
   * @return the created combo box.
   */
  protected SComboBox createSComboBox() {
    SComboBox comboBox = new SComboBox();
    comboBox.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return comboBox;
  }

  /**
   * Creates a security panel.
   * 
   * @return the created security panel.
   */
  protected SPanel createSecurityPanel() {
    SPanel panel = createSPanel();
    panel.setLayout(new SBorderLayout());
    SLabel label = createSLabel();
    label.setHorizontalAlignment(SConstants.CENTER);
    label.setVerticalAlignment(SConstants.CENTER);
    label.setIcon(iconFactory.getForbiddenIcon(IIconFactory.LARGE_ICON_SIZE));
    panel.add(label, SBorderLayout.CENTER);
    return panel;
  }

  /**
   * Creates a label.
   * 
   * @return the created label.
   */
  protected SLabel createSLabel() {
    return new SLabel();
  }

  /**
   * Creates a list.
   * 
   * @return the created list.
   */
  protected SList createSList() {
    SList list = new SList();
    return list;
  }

  /**
   * Creates a menu item.
   * 
   * @return the created menu item.
   */
  protected SMenuItem createSMenuItem() {
    return new SMenuItem();
  }

  /**
   * Creates a panel.
   * 
   * @return the created panel.
   */
  protected SPanel createSPanel() {
    SPanel panel = new SPanel();
    panel.setPreferredSize(WingsUtil.FULLAREA);
    panel.setHorizontalAlignment(SConstants.CENTER_ALIGN);
    panel.setVerticalAlignment(SConstants.TOP_ALIGN);
    return panel;
  }

  /**
   * Creates an internal frame.
   * 
   * @return the created panel.
   */
  protected SInternalFrame createSInternalFrame() {
    SInternalFrame iFrame = new SInternalFrame();
    iFrame.setPreferredSize(WingsUtil.FULLAREA);
    iFrame.setHorizontalAlignment(SConstants.CENTER_ALIGN);
    iFrame.setVerticalAlignment(SConstants.TOP_ALIGN);
    return iFrame;
  }

  /**
   * Creates a password field.
   * 
   * @return the created password field.
   */
  protected SPasswordField createSPasswordField() {
    SPasswordField passwordField = new SPasswordField();
    passwordField.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return passwordField;
  }

  /**
   * Creates a popup menu.
   * 
   * @return the created popup menu.
   */
  protected SPopupMenu createSPopupMenu() {
    return new SPopupMenu();
  }

  /**
   * Creates a scroll pane.
   * 
   * @return the created scroll pane.
   */
  protected SScrollPane createSScrollPane() {
    SScrollPane scrollPane = new SScrollPane();
    scrollPane.setMode(SScrollPane.MODE_COMPLETE);
    scrollPane.setPreferredSize(WingsUtil.FULLAREA);
    scrollPane.setHorizontalAlignment(SConstants.CENTER_ALIGN);
    scrollPane.setVerticalAlignment(SConstants.TOP_ALIGN);
    return scrollPane;
  }

  /**
   * Creates a split pane.
   * 
   * @return the created split pane.
   */
  protected SSplitPane createSSplitPane() {
    SSplitPane splitPane = new SSplitPane();
    splitPane.setContinuousLayout(true);
    // splitPane.setOneTouchExpandable(true);
    return splitPane;
  }

  /**
   * Creates a tabbed pane.
   * 
   * @return the created tabbed pane.
   */
  protected STabbedPane createSTabbedPane() {
    STabbedPane tabbedPane = new STabbedPane();
    tabbedPane.setPreferredSize(WingsUtil.FULLAREA);
    tabbedPane.setVerticalAlignment(SConstants.TOP_ALIGN);
    tabbedPane.setHorizontalAlignment(SConstants.CENTER_ALIGN);
    return tabbedPane;
  }

  /**
   * Creates a table.
   * 
   * @return the created table.
   */
  protected STable createSTable() {
    STable table = new STable() {

      private static final long serialVersionUID = -8821125434835138650L;
      private SCellRendererPane cellRendererPane = new SCellRendererPane() {

                                                   private static final long serialVersionUID = 3159574506651887983L;

                                                   @Override
                                                   public void writeComponent(
                                                       Device d, SComponent c,
                                                       SComponent p)
                                                       throws IOException {
                                                     if (c != null
                                                         && p instanceof STable) {
                                                       STable renderedTable = (STable) p;
                                                       if (renderedTable
                                                           .isEditing()
                                                           && renderedTable
                                                               .getEditorComponent() == c) {
                                                         addComponent(c);
                                                         c.write(d);
                                                       } else {
                                                         super.writeComponent(
                                                             d, c, p);
                                                       }
                                                     } else {
                                                       super.writeComponent(d,
                                                           c, p);
                                                     }
                                                   }
                                                 };

      @Override
      public SCellRendererPane getCellRendererPane() {
        return cellRendererPane;
      }
    };
    table.setVerticalAlignment(SConstants.TOP_ALIGN);
    table.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    table.setSelectable(true);
    return table;
  }

  /**
   * Creates a text area.
   * 
   * @return the created text area.
   */
  protected STextArea createSTextArea() {
    STextArea textArea = new STextArea();
    textArea.setPreferredSize(WingsUtil.FULLAREA);
    return textArea;
  }

  /**
   * Creates a text field.
   * 
   * @return the created text field.
   */
  protected STextField createSTextField() {
    STextField textField = new STextField();
    textField.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return textField;
  }

  /**
   * Creates a tool bar.
   * 
   * @return the created tool bar.
   */
  protected SToolBar createSToolBar() {
    SToolBar toolBar = new SToolBar();
    toolBar.setBorder(new SEmptyBorder(2, 2, 2, 2));
    toolBar.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return toolBar;
  }

  /**
   * Creates a tree.
   * 
   * @return the created tree.
   */
  protected STree createSTree() {
    STree tree = new STree();
    tree.setVerticalAlignment(SConstants.TOP_ALIGN);
    tree.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return tree;
  }

  /**
   * Decorates the created view with the apropriate border.
   * 
   * @param view
   *            the view to descorate.
   * @param locale
   *            the locale to use.
   */
  protected void decorateWithBorder(IView<SComponent> view, Locale locale) {
    switch (view.getDescriptor().getBorderType()) {
      case IViewDescriptor.SIMPLE:
        view.getPeer().setBorder(new SEtchedBorder());
        break;
      case IViewDescriptor.TITLED:
        // FIXME until titled border is re-integrated into wings.
        // view.getPeer().setBorder(new SEtchedBorder());
        // view.getPeer().setBorder(
        // new STitledBorder(new SEtchedBorder(), view.getDescriptor()
        // .getI18nName(getTranslationProvider(), locale)));
        SInternalFrame iFrame = createSInternalFrame();
        iFrame.setTitle(view.getDescriptor().getI18nName(
            getTranslationProvider(), locale));
        iFrame.setMaximizable(false);
        iFrame.setClosable(false);
        iFrame.setIconifyable(true);
        iFrame.setIcon(iconFactory.getIcon(view.getDescriptor()
            .getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
        iFrame.getContentPane().add(view.getPeer());
        view.setPeer(iFrame);
        break;
      default:
        break;
    }
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  private void adjustSizes(SComponent component, IFormatter formatter,
      Object templateValue) {
    adjustSizes(component, formatter, templateValue, 32);
  }

  // ///////////// //
  // Image Section //
  // ///////////// //

  private void adjustSizes(SComponent component, IFormatter formatter,
      Object templateValue, int extraWidth) {
    int preferredWidth = computePixelWidth(component, getFormatLength(
        formatter, templateValue))
        + extraWidth;
    SDimension size = new SDimension(preferredWidth + "px", null);
    component.setPreferredSize(size);
  }

  // /////////////// //
  // Nesting Section //
  // /////////////// //

  private String computeEnumerationKey(String keyPrefix, Object value) {
    return keyPrefix + "." + value;
  }

  // ///////////////// //
  // SComponent Section //
  // ///////////////// //

  private int computePixelWidth(SComponent component, int characterLength) {
    int charLength = maxCharacterLength + 2;
    if (characterLength > 0 && characterLength < maxCharacterLength) {
      charLength = characterLength + 2;
    }
    int fontSize = 12;
    if (component.getFont() != null) {
      fontSize = component.getFont().getSize();
    }
    return (int) ((fontSize * charLength) / 4.0);
  }

  private BasicCompositeView<SComponent> constructCompositeView(
      SComponent viewComponent, IViewDescriptor descriptor) {
    BasicCompositeView<SComponent> view = new BasicCompositeView<SComponent>(
        viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  private BasicMapView<SComponent> constructMapView(SComponent viewComponent,
      IViewDescriptor descriptor) {
    BasicMapView<SComponent> view = new BasicMapView<SComponent>(viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  private IView<SComponent> constructView(SComponent viewComponent,
      IViewDescriptor descriptor, IValueConnector connector) {
    BasicView<SComponent> view = new BasicView<SComponent>(viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

  private IView<SComponent> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    SActionField viewComponent = createSActionField(false);
    SActionFieldConnector connector = new SActionFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    Action openAction = actionFactory.createAction(
        openFileAsBinaryPropertyAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    Action saveAction = actionFactory.createAction(
        saveBinaryPropertyAsFileAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    Action resetAction = actionFactory.createAction(resetPropertyAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    Action infoAction = actionFactory.createAction(binaryPropertyInfoAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    viewComponent.setActions(Arrays.asList(new Action[] {openAction,
        saveAction, resetAction, infoAction}));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, null, connector);
  }

  private IView<SComponent> createBooleanPropertyView(
      IBooleanPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    SCheckBox viewComponent = createSCheckBox();
    SCheckBoxConnector connector = new SCheckBoxConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createBooleanTableCellRenderer(
      @SuppressWarnings("unused")
      IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return new BooleanTableCellRenderer();
  }

  private ICompositeView<SComponent> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SPanel viewComponent = createSPanel();
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    SBorderLayout layout = new SBorderLayout();
    viewComponent.setLayout(layout);

    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<SComponent> eastView = createView(viewDescriptor
          .getEastViewDescriptor(), actionHandler, locale);
      viewComponent.add(eastView.getPeer(), SBorderLayout.EAST);
      childrenViews.add(eastView);
    }
    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<SComponent> northView = createView(viewDescriptor
          .getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.add(northView.getPeer(), SBorderLayout.NORTH);
      childrenViews.add(northView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<SComponent> centerView = createView(viewDescriptor
          .getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.add(centerView.getPeer(), SBorderLayout.CENTER);
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<SComponent> westView = createView(viewDescriptor
          .getWestViewDescriptor(), actionHandler, locale);
      viewComponent.add(westView.getPeer(), SBorderLayout.WEST);
      childrenViews.add(westView);
    }
    if (viewDescriptor.getSouthViewDescriptor() != null) {
      IView<SComponent> southView = createView(viewDescriptor
          .getSouthViewDescriptor(), actionHandler, locale);
      viewComponent.add(southView.getPeer(), SBorderLayout.SOUTH);
      childrenViews.add(southView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private IMapView<SComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SPanel viewComponent = createSPanel();
    SCardLayout layout = new SCardLayout();
    viewComponent.setLayout(layout);
    BasicMapView<SComponent> view = constructMapView(viewComponent,
        viewDescriptor);
    Map<String, IView<SComponent>> childrenViews = new HashMap<String, IView<SComponent>>();

    viewComponent.add(createSPanel(), ICardViewDescriptor.DEFAULT_CARD);
    viewComponent.add(createSecurityPanel(), ICardViewDescriptor.SECURITY_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<SComponent> childView = createView(childViewDescriptor.getValue(),
          actionHandler, locale);
      viewComponent.add(childView.getPeer(), childViewDescriptor.getKey());
      childrenViews.put(childViewDescriptor.getKey(), childView);
    }
    viewComponent.setPreferredSize(WingsUtil.FULLAREA);
    view.setChildren(childrenViews);
    view.setConnector(createCardViewConnector(view, actionHandler));
    return view;
  }

  private IValueConnector createCardViewConnector(
      final IMapView<SComponent> cardView, final IActionHandler actionHandler) {
    IValueConnector cardViewConnector = connectorFactory
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
            SPanel cardPanel = (SPanel) cardView.getPeer();
            if (accessGranted) {
              String cardName = ((ICardViewDescriptor) cardView.getDescriptor())
                  .getCardNameForModel(cardModel);
              if (cardName != null) {
                IView<SComponent> childCardView = cardView.getChild(cardName);
                if (childCardView != null) {
                  ((SCardLayout) cardPanel.getLayout()).show(cardPanel,
                      cardName);
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
                  ((SCardLayout) cardPanel.getLayout()).show(cardPanel,
                      ICardViewDescriptor.DEFAULT_CARD);
                }
              } else {
                ((SCardLayout) cardPanel.getLayout()).show(cardPanel,
                    ICardViewDescriptor.DEFAULT_CARD);
              }
            } else {
              ((SCardLayout) cardPanel.getLayout()).show(cardPanel,
                  ICardViewDescriptor.SECURITY_CARD);
            }
          }
        });
    return cardViewConnector;
  }

  private IView<SComponent> createCollectionPropertyView(
      ICollectionPropertyDescriptor<?> propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {

    IView<SComponent> view;
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

  private STableCellRenderer createCollectionTableCellRenderer(
      @SuppressWarnings("unused")
      ICollectionPropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

  private IView<SComponent> createCollectionView(
      ICollectionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<SComponent> view = null;
    if (viewDescriptor instanceof IListViewDescriptor) {
      view = createListView((IListViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITableViewDescriptor) {
      view = createTableView((ITableViewDescriptor) viewDescriptor,
          actionHandler, locale);
    }
    return view;
  }

  private IView<SComponent> createColorPropertyView(
      IColorPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    SColorPicker viewComponent = createSColorPicker();
    if (propertyDescriptor.getDefaultValue() != null) {
      int[] rgba = ColorHelper.fromHexString((String) propertyDescriptor
          .getDefaultValue());
      viewComponent
          .setResetValue(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
    }
    SColorPickerConnector connector = new SColorPickerConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createColorTableCellRenderer(
      @SuppressWarnings("unused")
      IColorPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      Locale locale) {
    return new ColorTableCellRenderer();
  }

  private IValueConnector createColumnConnector(String columnId,
      IComponentDescriptor<?> descriptor) {
    IPropertyDescriptor propertyDescriptor = descriptor
        .getPropertyDescriptor(columnId);
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + columnId + " defined for "
          + descriptor.getComponentContract());
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return connectorFactory.createCompositeValueConnector(columnId,
          ((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getToStringProperty());
    }
    return connectorFactory.createValueConnector(propertyDescriptor.getName());
  }

  private IView<SComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = connectorFactory
        .createCompositeValueConnector(
            getConnectorIdForComponentView(viewDescriptor), null);
    SPanel viewComponent = createSPanel();
    IView<SComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    SGridBagLayout layout = new SGridBagLayout();
    viewComponent.setLayout(layout);

    int currentX = 0;
    int currentY = 0;

    boolean isSpaceFilled = false;

    for (ISubViewDescriptor propertyViewDescriptor : viewDescriptor
        .getPropertyViewDescriptors()) {
      String propertyName = propertyViewDescriptor.getName();
      IPropertyDescriptor propertyDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor
          .getModelDescriptor()).getComponentDescriptor()
          .getPropertyDescriptor(propertyName);
      if (propertyDescriptor == null) {
        throw new ViewException("Property descriptor [" + propertyName
            + "] does not exist for model descriptor "
            + viewDescriptor.getModelDescriptor().getName() + ".");
      }
      IView<SComponent> propertyView = createPropertyView(propertyDescriptor,
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
      SLabel propertyLabel = createPropertyLabel(propertyDescriptor,
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
          if (propertyView.getPeer() instanceof STextArea
              || propertyView.getPeer() instanceof SList
              || propertyView.getPeer() instanceof SScrollPane
              || propertyView.getPeer() instanceof STable) {
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

      // SComponent positionning
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
      constraints.weightx = 1.0;
      if (propertyView.getPeer() instanceof STextArea
          || propertyView.getPeer() instanceof SList
          || propertyView.getPeer() instanceof SScrollPane
          || propertyView.getPeer() instanceof STable) {
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
      SPanel filler = createSPanel();
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

  private ICollectionConnectorProvider createCompositeNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
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
    return nodeGroupCollectionConnector;
  }

  private ICompositeView<SComponent> createCompositeView(
      ICompositeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<SComponent> view = null;
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
      if (viewDescriptor.isMasterDetail()) {
        IView<SComponent> masterView = view.getChildren().get(0);
        view.setConnector(masterView.getConnector());
        for (int i = 1; i < view.getChildren().size(); i++) {
          IView<SComponent> detailView = view.getChildren().get(i);
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
        for (IView<SComponent> childView : view.getChildren()) {
          childView.setParent(view);
          connector.addChildConnector(childView.getConnector());
        }
      }
    }
    return view;
  }

  private ICompositeView<SComponent> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    SPanel viewComponent = createSPanel();
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    SGridBagLayout layout = new SGridBagLayout();
    viewComponent.setLayout(layout);

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<SComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      viewComponent.add(childView.getPeer(),
          createGridBagConstraints(viewDescriptor
              .getViewConstraints(childViewDescriptor)));
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private DateFormat createDateFormat(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    DateFormat format;
    if (IDatePropertyDescriptor.DATE_TYPE.equals(propertyDescriptor.getType())) {
      format = new NullableSimpleDateFormat(((SimpleDateFormat) DateFormat
          .getDateInstance(DateFormat.SHORT, locale)).toLocalizedPattern(),
          locale);
    } else {
      format = new NullableSimpleDateFormat(((SimpleDateFormat) DateFormat
          .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale))
          .toLocalizedPattern(), locale);
    }
    return format;
  }

  private IFormatter createDateFormatter(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createDateFormat(propertyDescriptor, locale));
  }

  private IView<SComponent> createDatePropertyView(
      IDatePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    XCalendar viewComponent = createDateField();
    DateFormat format = createDateFormat(propertyDescriptor, locale);
    viewComponent.setFormatter(new SDateFormatter(format));
    XCalendarConnector connector = new XCalendarConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getDateTemplateValue(propertyDescriptor), 24);
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createDateTableCellRenderer(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDateFormatter(
        propertyDescriptor, locale));
  }

  private NumberFormat createDecimalFormat(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    NumberFormat format = NumberFormat.getNumberInstance(locale);
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit()
          .intValue());
    } else {
      format.setMaximumFractionDigits(DEF_DISP_MAX_FRACTION_DIGIT);
    }
    format.setMinimumFractionDigits(format.getMaximumFractionDigits());
    return format;
  }

  private IFormatter createDecimalFormatter(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(createDecimalFormat(propertyDescriptor, locale));
  }

  private IView<SComponent> createDecimalPropertyView(
      IDecimalPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(
          (IPercentPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    STextField viewComponent = createSTextField();
    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    SFormattedFieldConnector connector = new SFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDecimalTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createDecimalTableCellRenderer(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentTableCellRenderer(
          (IPercentPropertyDescriptor) propertyDescriptor, locale);
    }
    return new FormattedTableCellRenderer(createDecimalFormatter(
        propertyDescriptor, locale));
  }

  // /////////////// //
  // Helpers Section //
  // /////////////// //

  private IFormatter createDurationFormatter(@SuppressWarnings("unused")
  IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new DurationFormatter(locale);
  }

  private IView<SComponent> createDurationPropertyView(
      IDurationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    STextField viewComponent = createSTextField();
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);
    SFormattedFieldConnector connector = new SFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDurationTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createDurationTableCellRenderer(
      IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDurationFormatter(
        propertyDescriptor, locale));
  }

  private IView<SComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    SComboBox viewComponent = createSComboBox();
    for (Object enumElement : propertyDescriptor.getEnumerationValues()) {
      viewComponent.addItem(enumElement);
    }
    viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(
        propertyDescriptor, locale));
    adjustSizes(viewComponent, null, getEnumerationTemplateValue(
        propertyDescriptor, locale), 48);
    SComboBoxConnector connector = new SComboBoxConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createEnumerationTableCellRenderer(
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new TranslatedEnumerationTableCellRenderer(propertyDescriptor,
        locale);
  }

  private ICompositeView<SComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SPanel viewComponent = createSPanel();
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    SGridLayout layout = new SGridLayout();
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
      IView<SComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      viewComponent.add(childView.getPeer());
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private IFormatter createFormatter(Format format) {
    return new FormatAdapter(format);
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

  private ICompositeView<SComponent> createGridView(
      IGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<SComponent> view = null;
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

  private IView<SComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {
    SLabel imageLabel = createSLabel();
    imageLabel.setHorizontalAlignment(SConstants.CENTER);
    SImageConnector connector = new SImageConnector(viewDescriptor
        .getModelDescriptor().getName(), imageLabel);
    connector.setExceptionHandler(actionHandler);
    SPanel viewComponent = createSPanel();
    SBorderLayout layout = new SBorderLayout();
    viewComponent.setLayout(layout);
    IView<SComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    // SScrollPane scrollPane = createSScrollPane();
    // scrollPane.setMode(SScrollPane.MODE_COMPLETE);
    // scrollPane.setViewportView(imageLabel);
    // viewComponent.add(scrollPane, SBorderLayout.CENTER);
    viewComponent.add(imageLabel, SBorderLayout.CENTER);
    return view;
  }

  private NumberFormat createIntegerFormat(@SuppressWarnings("unused")
  IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return NumberFormat.getIntegerInstance(locale);
  }

  private IFormatter createIntegerFormatter(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(createIntegerFormat(propertyDescriptor, locale));
  }

  private IView<SComponent> createIntegerPropertyView(
      IIntegerPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    STextField viewComponent = createSTextField();
    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    SFormattedFieldConnector connector = new SFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getIntegerTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createIntegerTableCellRenderer(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createIntegerFormatter(
        propertyDescriptor, locale));
  }

  private IView<SComponent> createListView(IListViewDescriptor viewDescriptor,
      @SuppressWarnings("unused")
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = connectorFactory
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    ICollectionConnector connector = connectorFactory
        .createCollectionConnector(modelDescriptor.getName(), mvcBinder,
            rowConnectorPrototype);
    SList viewComponent = createSList();

    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    IView<SComponent> view = constructView(scrollPane, viewDescriptor,
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

  private IView<SComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = connectorFactory
        .createCompositeValueConnector(viewDescriptor.getModelDescriptor()
            .getName(), null);

    SPanel viewComponent = createSPanel();
    SBorderLayout layout = new SBorderLayout();
    viewComponent.setLayout(layout);

    IView<SComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    IView<SComponent> nestedView = createView(viewDescriptor
        .getNestedViewDescriptor(), actionHandler, locale);

    connector.addChildConnector(nestedView.getConnector());

    viewComponent.add(nestedView.getPeer(), SBorderLayout.CENTER);

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

  private IView<SComponent> createNumberPropertyView(
      INumberPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<SComponent> view = null;
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

  private STableCellRenderer createNumberTableCellRenderer(
      INumberPropertyDescriptor propertyDescriptor, Locale locale) {
    STableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      cellRenderer = createIntegerTableCellRenderer(
          (IIntegerPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      cellRenderer = createDecimalTableCellRenderer(
          (IDecimalPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private IView<SComponent> createPasswordPropertyView(
      IPasswordPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    SPasswordField viewComponent = createSPasswordField();
    SPasswordFieldConnector connector = new SPasswordFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private NumberFormat createPercentFormat(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    NumberFormat format = NumberFormat.getPercentInstance(locale);
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit()
          .intValue());
    } else {
      format.setMaximumFractionDigits(DEF_DISP_MAX_FRACTION_DIGIT);
    }
    format.setMinimumFractionDigits(format.getMaximumFractionDigits());
    return format;
  }

  private IFormatter createPercentFormatter(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(createPercentFormat(propertyDescriptor, locale));
  }

  private IView<SComponent> createPercentPropertyView(
      IPercentPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    STextField viewComponent = createSTextField();
    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    SPercentFieldConnector connector = new SPercentFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getPercentTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createPercentTableCellRenderer(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createPercentFormatter(
        propertyDescriptor, locale));
  }

  private SLabel createPropertyLabel(IPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      SComponent propertyComponent, Locale locale) {
    SLabel propertyLabel = createSLabel();
    StringBuffer labelText = new StringBuffer(propertyDescriptor.getI18nName(
        getTranslationProvider(), locale));
    if (propertyDescriptor.isMandatory()) {
      labelText.append("*");
      propertyLabel.setForeground(Color.RED);
    }
    propertyLabel.setText(labelText.toString());
    return propertyLabel;
  }

  private IView<SComponent> createPropertyView(
      IPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<SComponent> view = null;
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
    } else if (propertyDescriptor instanceof IColorPropertyDescriptor) {
      view = createColorPropertyView(
          (IColorPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    if (view != null && propertyDescriptor.getDescription() != null) {
      view.getPeer().setToolTipText(
          propertyDescriptor.getI18nDescription(getTranslationProvider(),
              locale)
              + TOOLTIP_ELLIPSIS);
    }
    return view;
  }

  private IView<SComponent> createPropertyView(
      IPropertyViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<SComponent> view = createPropertyView(
        (IPropertyDescriptor) viewDescriptor.getModelDescriptor(),
        viewDescriptor.getRenderedChildProperties(), actionHandler, locale);
    return constructView(view.getPeer(), viewDescriptor, view.getConnector());
  }

  private IView<SComponent> createReferencePropertyView(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    SActionField viewComponent = createSActionField(true);
    SReferenceFieldConnector connector = new SReferenceFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setToStringPropertyConnector(new BasicValueConnector(
        propertyDescriptor.getComponentDescriptor().getToStringProperty()));
    connector.setExceptionHandler(actionHandler);
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

  private STableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused")
      IReferencePropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

  private IView<SComponent> createRelationshipEndPropertyView(
      IRelationshipEndPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<SComponent> view = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      view = createReferencePropertyView(
          (IReferencePropertyDescriptor<?>) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      view = createCollectionPropertyView(
          (ICollectionPropertyDescriptor<?>) propertyDescriptor,
          renderedChildProperties, actionHandler, locale);
    }
    return view;
  }

  private STableCellRenderer createRelationshipEndTableCellRenderer(
      IRelationshipEndPropertyDescriptor propertyDescriptor, Locale locale) {
    STableCellRenderer cellRenderer = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      cellRenderer = createReferenceTableCellRenderer(
          (IReferencePropertyDescriptor<?>) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      cellRenderer = createCollectionTableCellRenderer(
          (ICollectionPropertyDescriptor<?>) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private ICollectionConnectorProvider createSimpleNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
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
          viewDescriptor, subtreeViewDescriptor.getChildDescriptor(), depth + 1);
      nodeGroupPrototypeConnector.addChildConnector(childConnector);
      nodeGroupPrototypeConnector
          .setCollectionConnectorProvider(childConnector);
    }
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  private IView<SComponent> createSourceCodePropertyView(
      ISourceCodePropertyDescriptor propertyDescriptor,

      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {

    return createTextPropertyView(propertyDescriptor, actionHandler, locale);
  }

  private ICompositeView<SComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SPanel viewComponent = createSPanel();
    viewComponent.setLayout(new SGridBagLayout());
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    if (viewDescriptor.getLeftTopViewDescriptor() != null) {
      IView<SComponent> leftTopView = createView(viewDescriptor
          .getLeftTopViewDescriptor(), actionHandler, locale);
      leftTopView.getPeer().setHorizontalAlignment(SConstants.LEFT_ALIGN);
      leftTopView.getPeer().setVerticalAlignment(SConstants.TOP_ALIGN);
      switch (viewDescriptor.getOrientation()) {
        case ISplitViewDescriptor.HORIZONTAL:
          double weightx = 0.3d;
          if (viewDescriptor.getLeftTopViewDescriptor() instanceof ITreeViewDescriptor) {
            weightx = 0.0d;
          }
          viewComponent.add(leftTopView.getPeer(), new GridBagConstraints(0, 0,
              1, 1, weightx, 1.0d, GridBagConstraints.NORTHWEST,
              GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
          break;
        case ISplitViewDescriptor.VERTICAL:
          viewComponent.add(leftTopView.getPeer(), new GridBagConstraints(0, 0,
              1, 1, 1.0d, 0.0d, GridBagConstraints.NORTHWEST,
              GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
          break;
        default:
          break;
      }
      childrenViews.add(leftTopView);
    }
    if (viewDescriptor.getRightBottomViewDescriptor() != null) {
      IView<SComponent> rightBottomView = createView(viewDescriptor
          .getRightBottomViewDescriptor(), actionHandler, locale);
      rightBottomView.getPeer().setHorizontalAlignment(SConstants.LEFT_ALIGN);
      rightBottomView.getPeer().setVerticalAlignment(SConstants.TOP_ALIGN);
      int gridx = 0;
      int gridy = 0;
      switch (viewDescriptor.getOrientation()) {
        case ISplitViewDescriptor.HORIZONTAL:
          gridx = 1;
          gridy = 0;
          break;
        case ISplitViewDescriptor.VERTICAL:
          gridx = 0;
          gridy = 1;
          break;
        default:
          break;
      }
      viewComponent.add(rightBottomView.getPeer(), new GridBagConstraints(
          gridx, gridy, GridBagConstraints.REMAINDER,
          GridBagConstraints.REMAINDER, 1.0d, 1.0d,
          GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,
              0, 0, 0), 0, 0));
      childrenViews.add(rightBottomView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  // private ICompositeView<SComponent> createSplitView(
  // ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
  // Locale locale) {
  // SSplitPane viewComponent = createSSplitPane();
  // BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
  // viewDescriptor);
  // List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();
  //
  // switch (viewDescriptor.getOrientation()) {
  // case ISplitViewDescriptor.HORIZONTAL:
  // viewComponent.setOrientation(SSplitPane.HORIZONTAL_SPLIT);
  // break;
  // case ISplitViewDescriptor.VERTICAL:
  // viewComponent.setOrientation(SSplitPane.VERTICAL_SPLIT);
  // break;
  // default:
  // break;
  // }
  //
  // if (viewDescriptor.getLeftTopViewDescriptor() != null) {
  // IView<SComponent> leftTopView = createView(viewDescriptor
  // .getLeftTopViewDescriptor(), actionHandler, locale);
  // viewComponent.setLeftComponent(leftTopView.getPeer());
  // childrenViews.add(leftTopView);
  // }
  // if (viewDescriptor.getRightBottomViewDescriptor() != null) {
  // IView<SComponent> rightBottomView = createView(viewDescriptor
  // .getRightBottomViewDescriptor(), actionHandler, locale);
  // viewComponent.setRightComponent(rightBottomView.getPeer());
  // childrenViews.add(rightBottomView);
  // }
  // view.setChildren(childrenViews);
  // return view;
  // }

  private IView<SComponent> createStringPropertyView(
      IStringPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof IPasswordPropertyDescriptor) {
      return createPasswordPropertyView(
          (IPasswordPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ISourceCodePropertyDescriptor) {
      return createSourceCodePropertyView(
          (ISourceCodePropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ITextPropertyDescriptor) {
      return createTextPropertyView(
          (ITextPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    STextField viewComponent = createSTextField();
    STextFieldConnector connector = new STextFieldConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createStringTableCellRenderer(
      @SuppressWarnings("unused")
      IStringPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      Locale locale) {
    return new FormattedTableCellRenderer(null);
  }

  private STableCellEditor createTableCellEditor(IView<SComponent> editorView) {
    WingsViewCellEditorAdapter editor;
    editor = new WingsViewCellEditorAdapter(editorView);
    return editor;
  }

  private STableCellRenderer createTableCellRenderer(
      IPropertyDescriptor propertyDescriptor, Locale locale) {
    STableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      cellRenderer = createBooleanTableCellRenderer(
          (IBooleanPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      cellRenderer = createDateTableCellRenderer(
          (IDatePropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      cellRenderer = createTimeTableCellRenderer(
          (ITimePropertyDescriptor) propertyDescriptor, locale);
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
    } else if (propertyDescriptor instanceof IColorPropertyDescriptor) {
      cellRenderer = createColorTableCellRenderer(
          (IColorPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private IView<SComponent> createTableView(
      ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = connectorFactory
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            modelDescriptor.getCollectionDescriptor().getElementDescriptor()
                .getToStringProperty());
    ICollectionConnector connector = connectorFactory
        .createCollectionConnector(modelDescriptor.getName(), mvcBinder,
            rowConnectorPrototype);
    STable viewComponent = createSTable();
    if (viewDescriptor.isReadOnly()) {
      viewComponent.setEditable(false);
    }
    Map<String, Class<?>> columnClassesByIds = new HashMap<String, Class<?>>();
    List<String> columnConnectorKeys = new ArrayList<String>();
    for (ISubViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String columnId = columnViewDescriptor.getName();
      IValueConnector columnConnector = createColumnConnector(columnId,
          modelDescriptor.getCollectionDescriptor().getElementDescriptor());
      rowConnectorPrototype.addChildConnector(columnConnector);
      columnClassesByIds.put(columnId, modelDescriptor
          .getCollectionDescriptor().getElementDescriptor()
          .getPropertyDescriptor(columnId).getModelType());
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
    viewComponent.setModel(tableModel);
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), null);
    int maxColumnSize = computePixelWidth(viewComponent,
        maxColumnCharacterLength);
    for (int i = 0; i < viewDescriptor.getColumnViewDescriptors().size(); i++) {
      STableColumn column = viewComponent.getColumnModel().getColumn(i);
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

      if (!viewDescriptor.isReadOnly()) {
        IView<SComponent> editorView = createPropertyView(propertyDescriptor,
            null, actionHandler, locale);
        if (editorView.getPeer() instanceof SActionField) {
          SActionField actionField = (SActionField) editorView.getPeer();
          actionField.setActions(Collections.singletonList(actionField
              .getActions().get(0)));
        }
        if (editorView.getConnector().getParentConnector() == null) {
          editorView.getConnector().setParentConnector(connector);
        }
        column.setCellEditor(createTableCellEditor(editorView));
      }
      STableCellRenderer cellRenderer = createTableCellRenderer(
          propertyDescriptor, locale);
      if (cellRenderer != null) {
        column.setCellRenderer(cellRenderer);
      } else {
        column.setCellRenderer(new EvenOddTableCellRenderer());
      }
      int minHeaderWidth = computePixelWidth(viewComponent, columnName.length());
      if (propertyDescriptor instanceof IBooleanPropertyDescriptor
          || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
        column.setWidth(Math.max(computePixelWidth(viewComponent, 2),
            minHeaderWidth)
            + "px");
      } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
        column.setWidth(Math.max(computePixelWidth(viewComponent,
            getEnumerationTemplateValue(
                (IEnumerationPropertyDescriptor) propertyDescriptor, locale)
                .length()), minHeaderWidth)
            + "px");
      } else {
        column.setWidth(Math.max(Math.min(computePixelWidth(viewComponent,
            getFormatLength(createFormatter(propertyDescriptor, locale),
                getTemplateValue(propertyDescriptor))), maxColumnSize),
            minHeaderWidth)
            + "px");
      }
    }

    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    IView<SComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    scrollPane.setPreferredSize(new SDimension("99%", SDimension.AUTO));
    return view;
  }

  private ICompositeView<SComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    STabbedPane viewComponent = createSTabbedPane();
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<SComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      SIcon childIcon = iconFactory.getIcon(childViewDescriptor
          .getIconImageURL(), IIconFactory.SMALL_ICON_SIZE);
      SComponent tabView = childView.getPeer();
      tabView.setPreferredSize(new SDimension("99%", "99%"));
      if (childViewDescriptor.getDescription() != null) {
        viewComponent.addTab(childViewDescriptor.getI18nName(
            getTranslationProvider(), locale), childIcon, tabView,
            childViewDescriptor.getI18nDescription(getTranslationProvider(),
                locale)
                + TOOLTIP_ELLIPSIS);
      } else {
        viewComponent.addTab(childViewDescriptor.getI18nName(
            getTranslationProvider(), locale), childIcon, tabView);
      }
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    viewComponent.setPreferredSize(new SDimension("99%", "800px"));
    return view;
  }

  private IView<SComponent> createTextPropertyView(
      ITextPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {
    STextArea viewComponent = createSTextArea();
    viewComponent.setLineWrap(STextArea.VIRTUAL_WRAP);

    STextAreaConnector connector = new STextAreaConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);

    IView<SComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  private DateFormat createTimeFormat(@SuppressWarnings("unused")
  ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
    return format;
  }

  private IFormatter createTimeFormatter(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createTimeFormat(propertyDescriptor, locale));
  }

  private IView<SComponent> createTimePropertyView(
      ITimePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    STextField viewComponent = createSTextField();
    IFormatter formatter = createTimeFormatter(propertyDescriptor, locale);
    SFormattedFieldConnector connector = new SFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getTimeTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private STableCellRenderer createTimeTableCellRenderer(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createTimeFormatter(
        propertyDescriptor, locale));
  }

  // //////////// //
  // Tree Section //
  // //////////// //
  private IView<SComponent> createTreeView(ITreeViewDescriptor viewDescriptor,
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

    STree viewComponent = createSTree();
    ConnectorHierarchyTreeModel treeModel = new ConnectorHierarchyTreeModel(
        connector);
    viewComponent.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    viewComponent.setModel(treeModel);
    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer(viewDescriptor,
        locale));
    treeSelectionModelBinder.bindSelectionModel(connector, viewComponent);

    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    scrollPane.setPreferredSize(new SDimension("200px", "500px"));
    IView<SComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    return view;
  }

  // ///////////////////// //
  // Configuration Section //
  // ///////////////////// //

  private String getConnectorIdForComponentView(
      IComponentViewDescriptor viewDescriptor) {
    if (viewDescriptor.getModelDescriptor() instanceof IComponentDescriptor) {
      return ModelRefPropertyConnector.THIS_PROPERTY;
    }
    return viewDescriptor.getModelDescriptor().getName();
  }

  private Object getDateTemplateValue(@SuppressWarnings("unused")
  IDatePropertyDescriptor propertyDescriptor) {
    return TEMPLATE_DATE;
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
    for (int i = 1; i <= maxFractionDigit; i++) {
      decimalPart += Math.pow(10.0D, -i);
    }
    templateValue += decimalPart;
    return new Double(templateValue);
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

  private Object getDurationTemplateValue(@SuppressWarnings("unused")
  IDurationPropertyDescriptor propertyDescriptor) {
    return TEMPLATE_DURATION;
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

  private Object getIntegerTemplateValue(
      IIntegerPropertyDescriptor propertyDescriptor) {
    double templateValue = DEF_DISP_MAX_VALUE;
    if (propertyDescriptor.getMaxValue() != null) {
      templateValue = propertyDescriptor.getMaxValue().doubleValue();
    }
    return new Integer((int) templateValue);
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
    for (int i = 1; i <= maxFractionDigit; i++) {
      decimalPart += Math.pow(10.0D, -i);
    }
    templateValue += decimalPart;
    return new Double(templateValue / 100.0D);
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

  private String getStringTemplateValue(
      IStringPropertyDescriptor propertyDescriptor) {
    return getStringTemplateValue(propertyDescriptor.getMaxLength());
  }

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
      return getTemplateValue(((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor().getPropertyDescriptor(
              ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                  .getReferencedDescriptor().getToStringProperty()));
    }
    return null;
  }

  private Object getTimeTemplateValue(@SuppressWarnings("unused")
  ITimePropertyDescriptor propertyDescriptor) {
    return TEMPLATE_TIME;
  }

  private final class ColorTableCellRenderer extends SDefaultTableCellRenderer {

    private static final long serialVersionUID = 2463885562098867443L;

    /**
     * {@inheritDoc}
     */
    @Override
    public SComponent getTableCellRendererComponent(STable table, Object value,
        boolean isSelected, int row, int column) {
      if (value != null) {
        int[] rgba = ColorHelper.fromHexString((String) value);
        setBackground(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
      } else {
        setBackground(null);
      }
      return super.getTableCellRendererComponent(table, value, isSelected, row,
          column);
    }
  }

  private final class ConnectorTreeCellRenderer extends
      SDefaultTreeCellRenderer {

    private static final long   serialVersionUID = -5153268751092971328L;
    private Locale              locale;
    private ITreeViewDescriptor viewDescriptor;

    /**
     * Constructs a new <code>ConnectorTreeCellRenderer</code> instance.
     * 
     * @param viewDescriptor
     *            the tree view descriptor used by the tree view.
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
    public SComponent getTreeCellRendererComponent(STree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row,
        boolean nodeHasFocus) {
      SComponent renderer = super.getTreeCellRendererComponent(tree, value,
          sel, expanded, leaf, row, nodeHasFocus);
      if (value instanceof IValueConnector) {
        IValueConnector rootConnector = (IValueConnector) tree.getModel()
            .getRoot();
        SIcon nodeIcon = null;
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
            // SToolTipManager.sharedInstance().registerComponent(tree);
            toolTipText = nodeGroupDescriptor.getI18nDescription(
                getTranslationProvider(), locale)
                + TOOLTIP_ELLIPSIS;
          }
          setText(labelText);
        } else {
          // if (((IValueConnector) value).getConnectorValue() != null) {
          // labelText = ((IValueConnector) value).getConnectorValue()
          // .toString();
          // } else {
          // labelText = "";
          // }
          // the previous code shortcuts the rendering connector.
          labelText = value.toString();
        }
        setText(labelText);
        setToolTipText(toolTipText);
      }
      return renderer;
    }
  }

  private final class TranslatedEnumerationListCellRenderer extends
      SDefaultListCellRenderer {

    private static final long              serialVersionUID = -5694559709701757582L;
    private Locale                         locale;
    private IEnumerationPropertyDescriptor propertyDescriptor;

    /**
     * Constructs a new <code>TranslatedEnumerationCellRenderer</code>
     * instance.
     * 
     * @param propertyDescriptor
     *            the property descriptor from which the enumeration name is
     *            taken. The prefix used to lookup translation keys in the form
     *            keyPrefix.value is the propertyDescriptor enumeration name.
     * @param locale
     *            the locale to lookup the translation.
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
    public SComponent getListCellRendererComponent(SComponent list,
        Object value, boolean isSelected, int index) {
      SLabel label = (SLabel) super.getListCellRendererComponent(list, value,
          isSelected, index);
      label
          .setIcon(iconFactory.getIcon(propertyDescriptor
              .getIconImageURL(String.valueOf(value)),
              IIconFactory.TINY_ICON_SIZE));
      if (value != null && propertyDescriptor.isTranslated()) {
        setText(translationProvider.getTranslation(computeEnumerationKey(
            propertyDescriptor.getEnumerationName(), value), locale));
      } else {
        setText(String.valueOf(value));
      }
      return label;
    }
  }

  private final class TranslatedEnumerationTableCellRenderer extends
      EvenOddTableCellRenderer {

    private static final long              serialVersionUID = -4500472602998482756L;
    private Locale                         locale;
    private IEnumerationPropertyDescriptor propertyDescriptor;

    /**
     * Constructs a new <code>TranslatedEnumerationTableCellRenderer</code>
     * instance.
     * 
     * @param propertyDescriptor
     *            the property descriptor from which the enumeration name is
     *            taken. The prefix used to lookup translation keys in the form
     *            keyPrefix.value is the propertyDescriptor enumeration name.
     * @param locale
     *            the locale to lookup the translation.
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
    public SComponent getTableCellRendererComponent(STable table, Object value,
        boolean isSelected, int row, int column) {
      SLabel renderer = (SLabel) super.getTableCellRendererComponent(table,
          value, isSelected, row, column);
      renderer
          .setIcon(iconFactory.getIcon(propertyDescriptor
              .getIconImageURL(String.valueOf(value)),
              IIconFactory.TINY_ICON_SIZE));
      if (value instanceof IValueConnector) {
        Object connectorValue = ((IValueConnector) value).getConnectorValue();
        if (connectorValue != null && propertyDescriptor.isTranslated()) {
          renderer.setText(translationProvider.getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  connectorValue), locale));
        } else {
          renderer.setText(String.valueOf(connectorValue));
        }
      } else {
        if (value != null && propertyDescriptor.isTranslated()) {
          renderer.setText(translationProvider.getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  value), locale));
        } else {
          if (value == null) {
            renderer.setText("");
          } else {
            renderer.setText(String.valueOf(value));
          }
        }
      }
      return renderer;
    }
  }
}
