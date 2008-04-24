/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
import javax.swing.table.DefaultTableCellRenderer;
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

import org.jspresso.framework.action.IActionHandler;
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
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.binding.masterdetail.IModelCascadingBinder;
import org.jspresso.framework.binding.model.IModelValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.binding.swing.CollectionConnectorListModel;
import org.jspresso.framework.binding.swing.CollectionConnectorTableModel;
import org.jspresso.framework.binding.swing.ConnectorHierarchyTreeModel;
import org.jspresso.framework.binding.swing.ConnectorTreeHelper;
import org.jspresso.framework.binding.swing.IListSelectionModelBinder;
import org.jspresso.framework.binding.swing.ITreeSelectionModelBinder;
import org.jspresso.framework.binding.swing.JActionFieldConnector;
import org.jspresso.framework.binding.swing.JColorPickerConnector;
import org.jspresso.framework.binding.swing.JComboBoxConnector;
import org.jspresso.framework.binding.swing.JDateFieldConnector;
import org.jspresso.framework.binding.swing.JEditTextAreaConnector;
import org.jspresso.framework.binding.swing.JFormattedFieldConnector;
import org.jspresso.framework.binding.swing.JImageConnector;
import org.jspresso.framework.binding.swing.JPasswordFieldConnector;
import org.jspresso.framework.binding.swing.JPercentFieldConnector;
import org.jspresso.framework.binding.swing.JReferenceFieldConnector;
import org.jspresso.framework.binding.swing.JTextAreaConnector;
import org.jspresso.framework.binding.swing.JTextFieldConnector;
import org.jspresso.framework.binding.swing.JToggleButtonConnector;
import org.jspresso.framework.gui.swing.components.JActionField;
import org.jspresso.framework.gui.swing.components.JColorPicker;
import org.jspresso.framework.gui.swing.components.JDateField;
import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IColorPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDurationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPasswordPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPercentPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ISourceCodePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.format.DurationFormatter;
import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.format.NullableSimpleDateFormat;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.BasicView;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.INestingViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ISubViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.TreeDescriptorHelper;
import org.jspresso.framework.view.descriptor.ViewConstraints;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicSubviewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.TokenMarker;


/**
 * Factory for swing views.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultSwingViewFactory implements
    IViewFactory<JComponent, Icon, Action> {

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
  private static final Dimension             TREE_PREFERRED_SIZE         = new Dimension(
                                                                             128,
                                                                             128);
  private IActionFactory<Action, JComponent> actionFactory;
  private IDisplayableAction                 binaryPropertyInfoAction;
  private IConfigurableConnectorFactory      connectorFactory;
  private IIconFactory<Icon>                 iconFactory;
  private IListSelectionModelBinder          listSelectionModelBinder;
  private IDisplayableAction                 lovAction;
  private IModelCascadingBinder              modelCascadingBinder;

  private int                                maxCharacterLength          = 32;
  private int                                maxColumnCharacterLength    = 32;
  private IMvcBinder                         mvcBinder;
  private IDisplayableAction                 openFileAsBinaryPropertyAction;
  private IDisplayableAction                 resetPropertyAction;
  private IDisplayableAction                 saveBinaryPropertyAsFileAction;
  private ITranslationProvider               translationProvider;
  private ITreeSelectionModelBinder          treeSelectionModelBinder;

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
        if (viewDescriptor.getDescription() != null) {
          view.getPeer().setToolTipText(
              viewDescriptor.getI18nDescription(getTranslationProvider(),
                  locale)
                  + TOOLTIP_ELLIPSIS);
        }
        if (viewDescriptor.getActionMap() != null) {
          JToolBar toolBar = createJToolBar();
          for (Iterator<ActionList> iter = viewDescriptor.getActionMap()
              .getActionLists().iterator(); iter.hasNext();) {
            ActionList nextActionList = iter.next();
            for (IDisplayableAction action : nextActionList.getActions()) {
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
                    + " <FONT SIZE=\"-2\" COLOR=\"#993366\">"
                    + acceleratorString + "</FONT></HTML>");
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
      } catch (SecurityException ex) {
        view.setPeer(createSecurityPanel());
      }
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  public void decorateWithTitle(IView<JComponent> view, Locale locale) {
    view.getPeer()
        .setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), view.getDescriptor()
                    .getI18nName(getTranslationProvider(), locale)));
    // JInternalFrame iFrame = new JInternalFrame(view.getDescriptor()
    // .getI18nName(getTranslationProvider(), locale), false, false,
    // false, false);
    // iFrame.setFrameIcon(iconFactory.getIcon(view.getDescriptor()
    // .getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    // iFrame.getContentPane().add(view.getPeer());
    // iFrame.pack();
    // view.setPeer(iFrame);
  }

  // ///////////////// //
  // Composite Section //
  // ///////////////// //

  /**
   * Gets the actionFactory.
   * 
   * @return the actionFactory.
   */
  public IActionFactory<Action, JComponent> getActionFactory() {
    return actionFactory;
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
   *            the actionFactory to set.
   */
  public void setActionFactory(IActionFactory<Action, JComponent> actionFactory) {
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
  public void setIconFactory(IIconFactory<Icon> iconFactory) {
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
   * Sets the modelCascadingBinder.
   * 
   * @param modelCascadingBinder
   *            the modelCascadingBinder to set.
   */
  public void setModelCascadingBinder(IModelCascadingBinder modelCascadingBinder) {
    this.modelCascadingBinder = modelCascadingBinder;
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
  protected JActionField createJActionField(boolean showTextField) {
    return new JActionField(showTextField);
  }

  /**
   * Creates a button.
   * 
   * @return the created button.
   */
  protected JButton createJButton() {
    JButton button = new JButton();
    SwingUtil.configureButton(button);
    return button;
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
   * Creates an color picker.
   * 
   * @return the created color picker.
   */
  protected JColorPicker createJColorPicker() {
    return new JColorPicker();
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
   * Creates a date field.
   * 
   * @param locale
   *            the user locale.
   * @return the created date field.
   */
  protected JDateField createJDateField(Locale locale) {
    JDateField dateField = new JDateField(locale);
    dateField.setRenderer(new DefaultDayRenderer());
    dateField.setHeaderRenderer(new DefaultHeaderRenderer());
    return dateField;
  }

  /**
   * Creates a JEdit text area.
   * 
   * @param language
   *            the language to add syntax highlighting for.
   * @return the created text area.
   */
  protected JEditTextArea createJEditTextArea(String language) {
    JEditTextArea textArea = new JEditTextArea();
    try {
      textArea.setTokenMarker((TokenMarker) Class.forName(
          "org.syntax.jedit.tokenmarker." + language + "TokenMarker")
          .newInstance());
    } catch (InstantiationException ex) {
      // Nothing to do. just don't colorize.
    } catch (IllegalAccessException ex) {
      // Nothing to do. just don't colorize.
    } catch (ClassNotFoundException ex) {
      // Nothing to do. just don't colorize.
    }
    return textArea;
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
   * Creates a menu item.
   * 
   * @return the created menu item.
   */
  protected JMenuItem createJMenuItem() {
    return new JMenuItem();
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

  /**
   * Creates a password field.
   * 
   * @return the created password field.
   */
  protected JPasswordField createJPasswordField() {
    JPasswordField passwordField = new JPasswordField();
    return passwordField;
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
   * Creates a scroll pane.
   * 
   * @return the created scroll pane.
   */
  protected JScrollPane createJScrollPane() {
    JScrollPane scrollPane = new JScrollPane();
    return scrollPane;
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
   * Creates a tabbed pane.
   * 
   * @return the created tabbed pane.
   */
  protected JTabbedPane createJTabbedPane() {
    return new JTabbedPane();
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
    // There is a bug regarding editing table when drag is enabled.
    // table.setDragEnabled(true);
    return table;
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
   * Creates a tool bar.
   * 
   * @return the created tool bar.
   */
  protected JToolBar createJToolBar() {
    JToolBar toolBar = new JToolBar();
    toolBar.setRollover(true);
    toolBar.setFloatable(true);
    toolBar.setBorder(BorderFactory.createRaisedBevelBorder());
    return toolBar;
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
   * Creates a security panel.
   * 
   * @return the created security panel.
   */
  protected JPanel createSecurityPanel() {
    JPanel panel = createJPanel();
    panel.setLayout(new BorderLayout());
//    JLabel label = createJLabel();
//    label.setHorizontalAlignment(SwingConstants.CENTER);
//    label.setVerticalAlignment(SwingConstants.CENTER);
//    label.setIcon(iconFactory.getForbiddenIcon(IIconFactory.LARGE_ICON_SIZE));
//    panel.add(label, BorderLayout.CENTER);
    return panel;
  }

  /**
   * Decorates the created view with the apropriate border.
   * 
   * @param view
   *            the view to descorate.
   * @param locale
   *            the locale to use.
   */
  protected void decorateWithBorder(IView<JComponent> view, Locale locale) {
    switch (view.getDescriptor().getBorderType()) {
      case IViewDescriptor.SIMPLE:
        view.getPeer().setBorder(BorderFactory.createEtchedBorder());
        break;
      case IViewDescriptor.TITLED:
        decorateWithTitle(view, locale);
        break;
      default:
        break;
    }
  }

  // ///////////// //
  // Image Section //
  // ///////////// //

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  // /////////////// //
  // Nesting Section //
  // /////////////// //

  private void adjustSizes(Component component, IFormatter formatter,
      Object templateValue) {
    adjustSizes(component, formatter, templateValue, 32);
  }

  // ///////////////// //
  // Component Section //
  // ///////////////// //

  private void adjustSizes(Component component, IFormatter formatter,
      Object templateValue, int extraWidth) {
    int preferredWidth = computePixelWidth(component, getFormatLength(
        formatter, templateValue))
        + extraWidth;
    Dimension size = new Dimension(preferredWidth,
        component.getPreferredSize().height);
    component.setMinimumSize(size);
    component.setPreferredSize(size);
    component.setMaximumSize(size);
  }

  private String computeEnumerationKey(String keyPrefix, Object value) {
    return keyPrefix + "." + value;
  }

  private int computePixelWidth(Component component, int characterLength) {
    int charLength = maxCharacterLength + 2;
    if (characterLength > 0 && characterLength < maxCharacterLength) {
      charLength = characterLength + 2;
    }
    return component.getFont().getSize() * charLength / 2;
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

  private IView<JComponent> constructView(JComponent viewComponent,
      IViewDescriptor descriptor, IValueConnector connector) {
    BasicView<JComponent> view = new BasicView<JComponent>(viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

  private IView<JComponent> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JActionField viewComponent = createJActionField(false);
    JActionFieldConnector connector = new JActionFieldConnector(
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

  private IView<JComponent> createBooleanPropertyView(
      IBooleanPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    JCheckBox viewComponent = createJCheckBox();
    JToggleButtonConnector connector = new JToggleButtonConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createBooleanTableCellRenderer(
      @SuppressWarnings("unused")
      IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return new BooleanTableCellRenderer();
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
    viewComponent.add(createSecurityPanel(), ICardViewDescriptor.SECURITY_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<JComponent> childView = createView(childViewDescriptor.getValue(),
          actionHandler, locale);
      viewComponent.add(childView.getPeer(), childViewDescriptor.getKey());
      childrenViews.put(childViewDescriptor.getKey(), childView);
    }
    view.setChildren(childrenViews);
    view.setConnector(createCardViewConnector(view, actionHandler));
    return view;
  }

  private IValueConnector createCardViewConnector(
      final IMapView<JComponent> cardView, final IActionHandler actionHandler) {
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
            JPanel cardPanel = (JPanel) cardView.getPeer();
            if (accessGranted) {
              String cardName = ((ICardViewDescriptor) cardView.getDescriptor())
                  .getCardNameForModel(cardModel);
              if (cardName != null) {
                IView<JComponent> childCardView = cardView.getChild(cardName);
                if (childCardView != null) {
                  ((CardLayout) cardPanel.getLayout())
                      .show(cardPanel, cardName);
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
                    mvcBinder.bind(childCardConnector, cardView.getConnector()
                        .getModelConnector());
                  }
                } else {
                  ((CardLayout) cardPanel.getLayout()).show(cardPanel,
                      ICardViewDescriptor.DEFAULT_CARD);
                }
              } else {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel,
                    ICardViewDescriptor.DEFAULT_CARD);
              }
            } else {
              ((CardLayout) cardPanel.getLayout()).show(cardPanel,
                  ICardViewDescriptor.SECURITY_CARD);
            }
          }
        });
    return cardViewConnector;
  }

  private IView<JComponent> createCollectionPropertyView(
      ICollectionPropertyDescriptor<?> propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {

    IView<JComponent> view;
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

  private TableCellRenderer createCollectionTableCellRenderer(
      @SuppressWarnings("unused")
      ICollectionPropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

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

  private IView<JComponent> createColorPropertyView(
      IColorPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    JColorPicker viewComponent = createJColorPicker();
    if (propertyDescriptor.getDefaultValue() != null) {
      int[] rgba = ColorHelper.fromHexString((String) propertyDescriptor
          .getDefaultValue());
      viewComponent
          .setResetValue(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
    }
    JColorPickerConnector connector = new JColorPickerConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createColorTableCellRenderer(
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
      IView<JComponent> propertyView = createPropertyView(propertyDescriptor,
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
              || propertyView.getPeer() instanceof JTable
              || propertyView.getPeer() instanceof JEditTextArea) {
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
      if (propertyLabel.getText() != null
          && propertyLabel.getText().length() > 0) {
        viewComponent.add(propertyLabel, constraints);
      }

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
      if (propertyView.getPeer() instanceof JCheckBox) {
        constraints.weightx = Toolkit.getDefaultToolkit().getScreenResolution();
      }
      if (propertyView.getPeer() instanceof JTextArea
          || propertyView.getPeer() instanceof JList
          || propertyView.getPeer() instanceof JScrollPane
          || propertyView.getPeer() instanceof JTable
          || propertyView.getPeer() instanceof JEditTextArea) {
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
    if (view != null) {
      if (viewDescriptor.isCascadingModels()) {
        IView<JComponent> masterView = view.getChildren().get(0);
        view.setConnector(masterView.getConnector());
        for (int i = 1; i < view.getChildren().size(); i++) {
          IView<JComponent> detailView = view.getChildren().get(i);
          detailView.setParent(view);

          IValueConnector detailConnector = null;
          if (detailView.getDescriptor().getModelDescriptor() instanceof IPropertyDescriptor) {
            IConfigurableCollectionConnectorProvider wrapper = connectorFactory
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
          modelCascadingBinder.bind(masterView.getConnector(), detailConnector);
          masterView = detailView;
        }
      } else {
        String connectorId;
        if (viewDescriptor.getModelDescriptor() instanceof IPropertyDescriptor) {
          connectorId = viewDescriptor.getModelDescriptor().getName();
        } else {
          connectorId = ModelRefPropertyConnector.THIS_PROPERTY;
        }
        ICompositeValueConnector connector = connectorFactory
            .createCompositeValueConnector(connectorId, null);
        view.setConnector(connector);
        for (IView<JComponent> childView : view.getChildren()) {
          childView.setParent(view);
          connector.addChildConnector(childView.getConnector());
        }
      }
    }
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

  private IView<JComponent> createDatePropertyView(
      IDatePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JDateField viewComponent = createJDateField(locale);
    DateFormat format = createDateFormat(propertyDescriptor, locale);
    viewComponent.getFormattedTextField().setFormatterFactory(
        new DefaultFormatterFactory(new DateFormatter(format)));
    JDateFieldConnector connector = new JDateFieldConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getDateTemplateValue(propertyDescriptor), Toolkit.getDefaultToolkit()
            .getScreenResolution() / 3);
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createDateTableCellRenderer(
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

  // ////////////////// //
  // Popup menu Section //
  // ////////////////// //

  private IView<JComponent> createDecimalPropertyView(
      IDecimalPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(
          (IPercentPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDecimalTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
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

  private IFormatter createDurationFormatter(@SuppressWarnings("unused")
  IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new DurationFormatter(locale);
  }

  private IView<JComponent> createDurationPropertyView(
      IDurationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDurationTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createDurationTableCellRenderer(
      IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDurationFormatter(
        propertyDescriptor, locale));
  }

  // /////////////// //
  // Helpers Section //
  // /////////////// //

  private IView<JComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JComboBox viewComponent = createJComboBox();
    if (!propertyDescriptor.isMandatory()) {
      viewComponent.addItem(null);
    }
    for (Object enumElement : propertyDescriptor.getEnumerationValues()) {
      viewComponent.addItem(enumElement);
    }
    viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(
        propertyDescriptor, locale));
    adjustSizes(viewComponent, null, getEnumerationTemplateValue(
        propertyDescriptor, locale), Toolkit.getDefaultToolkit()
        .getScreenResolution() * 2 / 6);
    JComboBoxConnector connector = new JComboBoxConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createEnumerationTableCellRenderer(
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new TranslatedEnumerationTableCellRenderer(propertyDescriptor,
        locale);
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

  private IView<JComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {
    JLabel imageLabel = createJLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    JImageConnector connector = new JImageConnector(viewDescriptor
        .getModelDescriptor().getName(), imageLabel);
    connector.setExceptionHandler(actionHandler);
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

  private NumberFormat createIntegerFormat(@SuppressWarnings("unused")
  IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return NumberFormat.getIntegerInstance(locale);
  }

  private IFormatter createIntegerFormatter(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(createIntegerFormat(propertyDescriptor, locale));
  }

  private IView<JComponent> createIntegerPropertyView(
      IIntegerPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getIntegerTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createIntegerTableCellRenderer(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createIntegerFormatter(
        propertyDescriptor, locale));
  }

  private JPopupMenu createJPopupMenu(JComponent sourceComponent,
      ActionMap actionMap, IModelDescriptor modelDescriptor,
      IViewDescriptor viewDescriptor, IValueConnector viewConnector,
      IActionHandler actionHandler, Locale locale) {
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
    for (Iterator<ActionList> iter = actionMap.getActionLists().iterator(); iter
        .hasNext();) {
      ActionList nextActionSet = iter.next();
      for (IDisplayableAction action : nextActionSet.getActions()) {
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

  private IView<JComponent> createListView(IListViewDescriptor viewDescriptor,
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

  private IView<JComponent> createNumberPropertyView(
      INumberPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<JComponent> view = null;
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

  private IView<JComponent> createPasswordPropertyView(
      IPasswordPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    JPasswordField viewComponent = createJPasswordField();
    JPasswordFieldConnector connector = new JPasswordFieldConnector(
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

  private IView<JComponent> createPercentPropertyView(
      IPercentPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    JPercentFieldConnector connector = new JPercentFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getPercentTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createPercentTableCellRenderer(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createPercentFormatter(
        propertyDescriptor, locale));
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

  private IView<JComponent> createPropertyView(
      IPropertyViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<JComponent> view = createPropertyView(
        (IPropertyDescriptor) viewDescriptor.getModelDescriptor(),
        viewDescriptor.getRenderedChildProperties(), actionHandler, locale);
    return constructView(view.getPeer(), viewDescriptor, view.getConnector());
  }

  private IView<JComponent> createReferencePropertyView(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    JActionField viewComponent = createJActionField(true);
    JReferenceFieldConnector connector = new JReferenceFieldConnector(
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

  private TableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused")
      IReferencePropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused")
      Locale locale) {
    return null;
  }

  private IView<JComponent> createRelationshipEndPropertyView(
      IRelationshipEndPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<JComponent> view = null;

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

  private TableCellRenderer createRelationshipEndTableCellRenderer(
      IRelationshipEndPropertyDescriptor propertyDescriptor, Locale locale) {
    TableCellRenderer cellRenderer = null;

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

  private IView<JComponent> createSourceCodePropertyView(
      ISourceCodePropertyDescriptor propertyDescriptor,

      IActionHandler actionHandler, @SuppressWarnings("unused")
      Locale locale) {
    JEditTextArea viewComponent = createJEditTextArea(propertyDescriptor
        .getLanguage());
    JEditTextAreaConnector connector = new JEditTextAreaConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
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
      rightBottomView.getPeer().setMinimumSize(new Dimension(0, 0));
      childrenViews.add(rightBottomView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private IView<JComponent> createStringPropertyView(
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
    JTextField viewComponent = createJTextField();
    JTextFieldConnector connector = new JTextFieldConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createStringTableCellRenderer(
      @SuppressWarnings("unused")
      IStringPropertyDescriptor propertyDescriptor, @SuppressWarnings("unused")
      Locale locale) {
    return new FormattedTableCellRenderer(null);
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

  private IView<JComponent> createTableView(
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
    JTable viewComponent = createJTable();
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    JLabel iconLabel = createJLabel();
    iconLabel.setIcon(iconFactory.getIcon(modelDescriptor
        .getCollectionDescriptor().getElementDescriptor().getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));
    iconLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER, iconLabel);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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
    TableSorter sorterDecorator = new TableSorter(tableModel, viewComponent
        .getTableHeader());
    Dimension iconSize = new Dimension(viewComponent.getTableHeader().getFont()
        .getSize(), viewComponent.getTableHeader().getFont().getSize());
    sorterDecorator.setUpIcon(iconFactory.getIcon(
        "classpath:org/jspresso/framework/application/images/1uparrow-48x48.png",
        iconSize));
    sorterDecorator.setDownIcon(iconFactory.getIcon(
        "classpath:org/jspresso/framework/application/images/1downarrow-48x48.png",
        iconSize));
    sorterDecorator.setColumnComparator(String.class,
        String.CASE_INSENSITIVE_ORDER);
    viewComponent.setModel(sorterDecorator);
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), sorterDecorator);
    int maxColumnSize = computePixelWidth(viewComponent,
        maxColumnCharacterLength);
    for (int i = 0; i < viewDescriptor.getColumnViewDescriptors().size(); i++) {
      TableColumn column = viewComponent.getColumnModel().getColumn(i);
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

      IView<JComponent> editorView = createPropertyView(propertyDescriptor,
          null, actionHandler, locale);
      if (editorView.getPeer() instanceof JActionField) {
        JActionField actionField = (JActionField) editorView.getPeer();
        actionField.setActions(Collections.singletonList(actionField
            .getActions().get(0)));
      }
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
      int minHeaderWidth = computePixelWidth(viewComponent, columnName.length());
      if (propertyDescriptor instanceof IBooleanPropertyDescriptor
          || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
        column.setPreferredWidth(Math.max(computePixelWidth(viewComponent, 2),
            minHeaderWidth));
      } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
        column.setPreferredWidth(Math.max(computePixelWidth(viewComponent,
            getEnumerationTemplateValue(
                (IEnumerationPropertyDescriptor) propertyDescriptor, locale)
                .length() + 4), minHeaderWidth));
      } else {
        column.setPreferredWidth(Math.max(
            Math.min(computePixelWidth(viewComponent, getFormatLength(
                createFormatter(propertyDescriptor, locale),
                getTemplateValue(propertyDescriptor))), maxColumnSize),
            minHeaderWidth));
      }
    }
    viewComponent.addMouseListener(new PopupListener(viewComponent, view,
        actionHandler, locale));
    int minimumWidth = 0;
    for (int i = 0; i < 1
        && i < viewComponent.getColumnModel().getColumnCount(); i++) {
      minimumWidth += viewComponent.getColumnModel().getColumn(i)
          .getPreferredWidth();
    }
    scrollPane.setMinimumSize(new Dimension(minimumWidth, viewComponent
        .getRowHeight()
        * 6 + viewComponent.getTableHeader().getPreferredSize().height));
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
                locale)
                + TOOLTIP_ELLIPSIS);
      } else {
        viewComponent.addTab(childViewDescriptor.getI18nName(
            getTranslationProvider(), locale), childIcon, childView.getPeer());
      }
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  private IView<JComponent> createTextPropertyView(
      ITextPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused")
      Locale locale) {
    JTextArea viewComponent = createJTextArea();
    viewComponent.setLineWrap(true);
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    scrollPane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    JTextAreaConnector connector = new JTextAreaConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(scrollPane, null, connector);
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

  private IView<JComponent> createTimePropertyView(
      ITimePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createTimeFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getTimeTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  private TableCellRenderer createTimeTableCellRenderer(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createTimeFormatter(
        propertyDescriptor, locale));
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
        connector);
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
    scrollPane.setMinimumSize(TREE_PREFERRED_SIZE);
    return view;
  }

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

  // ///////////////////// //
  // Configuration Section //
  // ///////////////////// //

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
    ActionMap actionMap = ((ICollectionViewDescriptor) tableView
        .getDescriptor()).getActionMap();

    if (actionMap == null) {
      return;
    }

    JPopupMenu popupMenu = createJPopupMenu(table, actionMap, modelDescriptor,
        tableView.getDescriptor(), elementConnector, actionHandler, locale);
    popupMenu.show(table, evt.getX(), evt.getY());
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
    ActionMap actionMap;
    IViewDescriptor viewDescriptor;
    if (viewConnector == tree.getModel().getRoot()) {
      modelDescriptor = treeView.getDescriptor().getModelDescriptor();
      actionMap = treeView.getDescriptor().getActionMap();
      viewDescriptor = treeView.getDescriptor();
    } else {
      viewDescriptor = TreeDescriptorHelper.getSubtreeDescriptorFromPath(
          ((ITreeViewDescriptor) treeView.getDescriptor())
              .getRootSubtreeDescriptor(),
          getDescriptorPathFromConnectorTreePath(path))
          .getNodeGroupDescriptor();
      modelDescriptor = viewDescriptor.getModelDescriptor();
      actionMap = viewDescriptor.getActionMap();
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

  private final class ColorTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 6014260077437906330L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
      if (value != null) {
        int[] rgba = ColorHelper.fromHexString((String) value);
        setBackground(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
      } else {
        setBackground(null);
      }
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row, column);
    }
  }

  private final class ConnectorTreeCellRenderer extends DefaultTreeCellRenderer {

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

  private final class PopupListener extends MouseAdapter {

    private IActionHandler    actionHandler;
    private Locale            locale;
    private JComponent        sourceComponent;
    private IView<JComponent> view;

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

  private final class TranslatedEnumerationListCellRenderer extends
      DefaultListCellRenderer {

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
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value,
          index, isSelected, cellHasFocus);
      label
          .setIcon(iconFactory.getIcon(propertyDescriptor
              .getIconImageURL(String.valueOf(value)),
              IIconFactory.TINY_ICON_SIZE));
      if (value != null && propertyDescriptor.isTranslated()) {
        label.setText(translationProvider.getTranslation(computeEnumerationKey(
            propertyDescriptor.getEnumerationName(), value), locale));
      } else {
        if (value == null) {
          label.setText(" ");
        } else {
          label.setText(String.valueOf(value));
        }
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
        if (connectorValue != null && propertyDescriptor.isTranslated()) {
          super.setValue(translationProvider.getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  connectorValue), locale));
        } else {
          if (connectorValue == null) {
            super.setValue("");
          } else {
            super.setValue(String.valueOf(connectorValue));
          }
        }
      } else {
        if (value != null && propertyDescriptor.isTranslated()) {
          super.setValue(translationProvider.getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  value), locale));
        } else {
          if (value == null) {
            super.setValue("");
          } else {
            super.setValue(String.valueOf(value));
          }
        }
      }
    }
  }
}
