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
package org.jspresso.framework.view.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import chrriis.dj.swingsuite.JComboButton;
import chrriis.dj.swingsuite.JLink;
import chrriis.dj.swingsuite.JTriStateCheckBox;
import chrriis.dj.swingsuite.LinkListener;
import com.bbn.openmap.LayerHandler;
import com.bbn.openmap.MapBean;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.event.OMMouseMode;
import com.bbn.openmap.gui.BasicMapPanel;
import com.bbn.openmap.gui.EmbeddedNavPanel;
import com.bbn.openmap.gui.EmbeddedScaleDisplayPanel;
import com.bbn.openmap.gui.OverlayMapPanel;
import com.bbn.openmap.layer.imageTile.MapTileLayer;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.TokenMarker;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.view.ControllerAwareViewFactory;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.binding.swing.CollectionConnectorListModel;
import org.jspresso.framework.binding.swing.CollectionConnectorTableModel;
import org.jspresso.framework.binding.swing.ConnectorHierarchyTreeModel;
import org.jspresso.framework.binding.swing.IListSelectionModelBinder;
import org.jspresso.framework.binding.swing.ITreeSelectionModelBinder;
import org.jspresso.framework.binding.swing.JActionFieldConnector;
import org.jspresso.framework.binding.swing.JColorPickerConnector;
import org.jspresso.framework.binding.swing.JComboBoxConnector;
import org.jspresso.framework.binding.swing.JDateFieldConnector;
import org.jspresso.framework.binding.swing.JEditTextAreaConnector;
import org.jspresso.framework.binding.swing.JFormattedFieldConnector;
import org.jspresso.framework.binding.swing.JHTMLEditorConnector;
import org.jspresso.framework.binding.swing.JImageConnector;
import org.jspresso.framework.binding.swing.JLabelConnector;
import org.jspresso.framework.binding.swing.JMapViewConnector;
import org.jspresso.framework.binding.swing.JPasswordFieldConnector;
import org.jspresso.framework.binding.swing.JPercentFieldConnector;
import org.jspresso.framework.binding.swing.JRadioButtonConnector;
import org.jspresso.framework.binding.swing.JReferenceFieldConnector;
import org.jspresso.framework.binding.swing.JTextAreaConnector;
import org.jspresso.framework.binding.swing.JTextFieldConnector;
import org.jspresso.framework.binding.swing.JTextPaneConnector;
import org.jspresso.framework.binding.swing.JToggleButtonConnector;
import org.jspresso.framework.binding.swing.JTriStateCheckBoxConnector;
import org.jspresso.framework.gui.swing.components.JActionField;
import org.jspresso.framework.gui.swing.components.JColorPicker;
import org.jspresso.framework.gui.swing.components.JDateField;
import org.jspresso.framework.gui.swing.components.JHTMLEditor;
import org.jspresso.framework.gui.swing.components.JScrollablePanel;
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
import org.jspresso.framework.model.descriptor.IHtmlPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IImageBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IImageUrlPropertyDescriptor;
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
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.format.PasswordFormatter;
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.gui.FontHelper;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.image.IScalableImageAware;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.util.url.UrlHelper;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicIndexedView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.BasicView;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.EHorizontalAlignment;
import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEnumerationPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IMapViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IScrollableViewDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.IStringPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.TreeDescriptorHelper;

/**
 * Factory for swing views.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public class DefaultSwingViewFactory extends ControllerAwareViewFactory<JComponent, Icon, Action> {

  private static final Dimension TREE_PREFERRED_SIZE = new Dimension(128, 128);
  private IListSelectionModelBinder listSelectionModelBinder;
  private ITreeSelectionModelBinder treeSelectionModelBinder;

  /**
   * Sets the listSelectionModelBinder.
   *
   * @param listSelectionModelBinder
   *     the listSelectionModelBinder to set.
   */
  public void setListSelectionModelBinder(IListSelectionModelBinder listSelectionModelBinder) {
    this.listSelectionModelBinder = listSelectionModelBinder;
  }

  /**
   * Sets the treeSelectionModelBinder.
   *
   * @param treeSelectionModelBinder
   *     the treeSelectionModelBinder to set.
   */
  public void setTreeSelectionModelBinder(ITreeSelectionModelBinder treeSelectionModelBinder) {
    this.treeSelectionModelBinder = treeSelectionModelBinder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void addCard(IMapView<JComponent> cardView, IView<JComponent> card, String cardName) {
    Container cardPanel = findFirstCardPanel(cardView.getPeer());
    if (cardPanel != null) {
      cardPanel.add(card.getPeer(), cardName);
      cardView.addToChildrenMap(cardName, card);
    }
  }

  /**
   * Finds the first card panel starting from a root component.
   *
   * @param root
   *     the root component to start for searching.
   * @return the 1st found card panel or null if none.
   */
  protected JComponent findFirstCardPanel(Component root) {
    if (root instanceof JComponent && ((JComponent) root).getLayout() instanceof CardLayout) {
      return (JComponent) root;
    }
    if (root instanceof Container) {
      for (Component child : ((Container) root).getComponents()) {
        JComponent childCardPanel = findFirstCardPanel(child);
        if (childCardPanel != null) {
          return childCardPanel;
        }
      }
    }
    return null;
  }

  /**
   * Adjusts a component various sizes (e.g. min, max, preferred) based on a
   * formatter and a template value.
   *
   * @param component
   *     the component to adjust the sizes for.
   * @param formatter
   *     the formatter used if any.
   * @param templateValue
   *     the template value used.
   * @param viewDescriptor
   *     the underlying view descriptor.
   */
  protected void adjustSizes(IViewDescriptor viewDescriptor, JComponent component, IFormatter<?, String> formatter,
                             Object templateValue) {
    adjustSizes(viewDescriptor, component, formatter, templateValue, 32);
  }

  /**
   * Adjusts a component various sizes (e.g. min, max, preferred) based on a
   * formatter and a template value.
   *
   * @param component
   *     the component to adjust the sizes for.
   * @param formatter
   *     the formatter used if any.
   * @param templateValue
   *     the template value used.
   * @param viewDescriptor
   *     the underlying view descriptor.
   * @param extraWidth
   *     the extra size to be added.
   */
  protected void adjustSizes(IViewDescriptor viewDescriptor, JComponent component, IFormatter<?, String> formatter,
                             Object templateValue, int extraWidth) {
    if (viewDescriptor.getFont() != null && FontHelper.isFontSpec(viewDescriptor.getFont())) {
      // must set font before computing size.
      component.setFont(createFont(viewDescriptor.getFont(), component.getFont()));
    }
    int preferredWidth = computePixelWidth(component, getFormatLength(formatter, templateValue)) + extraWidth;
    Dimension size = new Dimension(preferredWidth, component.getPreferredSize().height);
    component.setMinimumSize(size);
    component.setPreferredSize(size);
    component.setMaximumSize(size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyPreferredSize(JComponent component, org.jspresso.framework.util.gui.Dimension preferredSize) {
    if (preferredSize != null) {
      int pW = preferredSize.getWidth();
      if (pW <= 0) {
        pW = component.getPreferredSize().width;
      }
      int pH = preferredSize.getHeight();
      if (pH <= 0) {
        pH = component.getPreferredSize().height;
      }
      component.setPreferredSize(new Dimension(pW, pH));
    }
  }

  private int computePixelWidth(JComponent component, int characterLength) {
    int charLength = getMaxCharacterLength() + 2;
    if (characterLength > 0 && characterLength < getMaxCharacterLength()) {
      charLength = characterLength + 2;
    }
    return component.getFont().getSize() * charLength / 2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createActionView(IActionViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                               Locale locale) {
    JButton viewComponent = createJButton();
    IValueConnector connector = getConnectorFactory().createValueConnector(ModelRefPropertyConnector.THIS_PROPERTY);
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, viewDescriptor, connector);
    viewComponent.setAction(getActionFactory()
        .createAction(viewDescriptor.getAction(), viewDescriptor.getPreferredSize(), actionHandler, view, locale));
    viewComponent.setBorderPainted(false);
    switch (viewDescriptor.getRenderingOptions()) {
      case ICON:
        viewComponent.setText(null);
        break;
      case LABEL:
        viewComponent.setIcon(null);
        break;
      default:
        break;
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createBinaryPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    IBinaryPropertyDescriptor propertyDescriptor = (IBinaryPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JActionField viewComponent = createJActionField(propertyViewDescriptor, false);
    JActionFieldConnector connector = new JActionFieldConnector(propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> propertyView = constructView(viewComponent, propertyViewDescriptor, connector);
    viewComponent.setActions(createBinaryActions(propertyView, actionHandler, locale));
    adjustSizes(propertyViewDescriptor, viewComponent, null, null);
    return propertyView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createBooleanPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IBooleanPropertyDescriptor propertyDescriptor = (IBooleanPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyDescriptor.isMandatory()) {
      viewComponent = createJCheckBox(propertyViewDescriptor);
      connector = new JToggleButtonConnector<>(propertyDescriptor.getName(), (JCheckBox) viewComponent);
    } else {
      viewComponent = createJTriStateCheckBox(propertyViewDescriptor);
      connector = new JTriStateCheckBoxConnector(propertyDescriptor.getName(), (JTriStateCheckBox) viewComponent);
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createBorderView(IBorderViewDescriptor viewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    JPanel viewComponent = createJPanel();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<>();

    BorderLayout layout = new BorderLayout();
    viewComponent.setLayout(layout);

    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<JComponent> northView = createView(viewDescriptor.getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.add(northView.getPeer(), BorderLayout.NORTH);
      childrenViews.add(northView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<JComponent> westView = createView(viewDescriptor.getWestViewDescriptor(), actionHandler, locale);
      viewComponent.add(westView.getPeer(), BorderLayout.WEST);
      childrenViews.add(westView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<JComponent> centerView = createView(viewDescriptor.getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.add(centerView.getPeer(), BorderLayout.CENTER);
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<JComponent> eastView = createView(viewDescriptor.getEastViewDescriptor(), actionHandler, locale);
      viewComponent.add(eastView.getPeer(), BorderLayout.EAST);
      childrenViews.add(eastView);
    }
    if (viewDescriptor.getSouthViewDescriptor() != null) {
      IView<JComponent> southView = createView(viewDescriptor.getSouthViewDescriptor(), actionHandler, locale);
      viewComponent.add(southView.getPeer(), BorderLayout.SOUTH);
      childrenViews.add(southView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IMapView<JComponent> createCardView(ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                Locale locale) {
    JPanel viewComponent = createJPanel();
    CardLayout layout = new CardLayout();
    viewComponent.setLayout(layout);
    BasicMapView<JComponent> view = constructMapView(viewComponent, viewDescriptor);

    viewComponent.add(createEmptyComponent(), ICardViewDescriptor.DEFAULT_CARD);
    viewComponent.add(createSecurityComponent(), ICardViewDescriptor.SECURITY_CARD);

    view.setConnector(createCardViewConnector(view, actionHandler, locale));
    return view;
  }

  private void fillLastRow(JPanel viewComponent) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.weightx = 2000.0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    JPanel filler = createJPanel();
    // filler.setBorder(new LineBorder(Color.BLUE));
    viewComponent.add(filler, constraints);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createColorPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                      IActionHandler actionHandler, Locale locale) {
    IColorPropertyDescriptor propertyDescriptor = (IColorPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JColorPicker viewComponent = createJColorPicker(propertyViewDescriptor);
    if (propertyDescriptor.getDefaultValue() != null) {
      int[] rgba = ColorHelper.fromHexString((String) propertyDescriptor.getDefaultValue());
      viewComponent.setResetValue(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
    } else {
      if (propertyDescriptor.isMandatory()) {
        viewComponent.setResetEnabled(false);
      }
    }
    JColorPickerConnector connector = new JColorPickerConnector(propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({"unchecked", "ConstantConditions"})
  @Override
  protected IView<JComponent> createComponentView(IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                  Locale locale) {
    final JPanel viewComponent = createJPanel();
    IComponentDescriptor<?> modelDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor.getModelDescriptor())
        .getComponentDescriptor();
    String toolTipProperty = computeComponentDynamicToolTip(viewDescriptor, modelDescriptor);
    IRenderableCompositeValueConnector connector = getConnectorFactory().createCompositeValueConnector(
        getConnectorIdForBeanView(viewDescriptor), toolTipProperty);
    attachToolTipListener(viewComponent, connector.getRenderingConnector());
    IView<JComponent> view = constructView(viewComponent, viewDescriptor, connector);

    GridBagLayout layout = new GridBagLayout();
    viewComponent.setLayout(layout);
    int currentX = 0;
    int extraRowOffset = 0;
    int currentY = 0;
    boolean isSpaceFilled = false;
    boolean lastRowNeedsFilling = true;
    List<IView<JComponent>> propertyViews = new ArrayList<>();
    int formInset = 2;
    for (Iterator<IPropertyViewDescriptor> ite = viewDescriptor.getPropertyViewDescriptors().iterator();
         ite.hasNext(); ) {
      IPropertyViewDescriptor propertyViewDescriptor = ite.next();
      if (isAllowedForClientType(propertyViewDescriptor, actionHandler)) {
        String propertyName = propertyViewDescriptor.getModelDescriptor().getName();
        IPropertyDescriptor propertyDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor.getModelDescriptor())
            .getComponentDescriptor().getPropertyDescriptor(propertyName);
        if (propertyDescriptor == null) {
          throw new ViewException(
              "Property descriptor [" + propertyName + "] does not exist for model descriptor " + viewDescriptor
                  .getModelDescriptor().getName() + ".");
        }
        IView<JComponent> propertyView = createView(propertyViewDescriptor, actionHandler, locale);
        propertyView.setParent(view);

        boolean forbidden = !actionHandler.isAccessGranted(propertyViewDescriptor);
        if (forbidden) {
          propertyView.setPeer(createSecurityComponent());
        } else {
          propertyViews.add(propertyView);
        }
        connector.addChildConnector(propertyView.getConnector().getId(), propertyView.getConnector());
        // already handled in createView.
        // if (propertyViewDescriptor.getReadabilityGates() != null) {
        // if (propertyViewDescriptor.getWritabilityGates() != null) {
        JLabel propertyLabel = createFormPropertyLabel(actionHandler, locale, propertyViewDescriptor,
            propertyDescriptor, propertyView, forbidden);
        int propertyWidth = propertyViewDescriptor.getWidth();
        EHorizontalPosition labelHorizontalPosition = propertyViewDescriptor.getLabelHorizontalPosition();
        if (labelHorizontalPosition == null) {
          labelHorizontalPosition = viewDescriptor.getLabelsHorizontalPosition();
        }
        if (labelHorizontalPosition == null) {
          labelHorizontalPosition = EHorizontalPosition.LEFT;
        }
        if (propertyWidth > viewDescriptor.getColumnCount()) {
          propertyWidth = viewDescriptor.getColumnCount();
        }
        if (currentX + propertyWidth > viewDescriptor.getColumnCount()) {
          fillLastRow(viewComponent);
          currentX = 0;
          currentY++;
          currentY += extraRowOffset;
          extraRowOffset = 0;
        }
        // label positioning
        GridBagConstraints constraints = new GridBagConstraints();
        computeLabelGridBagConstraints(viewDescriptor, currentX, currentY, formInset, propertyWidth,
            labelHorizontalPosition, constraints);
        if (viewDescriptor.getLabelsPosition() != ELabelPosition.NONE && propertyLabel.getText() != null
            && propertyLabel.getText().length() > 0) {
          viewComponent.add(propertyLabel, constraints);
        }
        // component positioning
        computeComponentGridBagConstraints(viewDescriptor, currentX, currentY, formInset, propertyWidth,
            labelHorizontalPosition, constraints);
        constraints.weightx = propertyView.getPeer().getPreferredSize().width;
        if (propertyView.getPeer() instanceof JCheckBox) {
          constraints.weightx = Toolkit.getDefaultToolkit().getScreenResolution();
        }
        if (isHeightExtensible(propertyViewDescriptor)) {
          constraints.weighty = 1.0;
          constraints.fill = GridBagConstraints.BOTH;
          constraints.gridheight = 2;
          extraRowOffset = 1;
          isSpaceFilled = true;
          if (!ite.hasNext()) {
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            lastRowNeedsFilling = false;
          }
        } else {
          constraints.fill = GridBagConstraints.NONE;
        }
        viewComponent.add(propertyView.getPeer(), constraints);
        if (propertyView.getPeer() instanceof JLink<?> && propertyViewDescriptor.getAction() != null) {
          IView<JComponent> targetView;
          if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
            targetView = propertyView;
          } else {
            targetView = view;
          }
          Action action = getActionFactory().createAction(propertyViewDescriptor.getAction(), actionHandler, targetView,
              locale);
          configurePropertyViewAction(propertyViewDescriptor, action);
          ((JLink<Action>) propertyView.getPeer()).setTarget(action);
        }

        currentX += propertyWidth;
      }
    }
    if (lastRowNeedsFilling) {
      fillLastRow(viewComponent);
    }
    if (!isSpaceFilled) {
      JPanel filler = createJPanel();
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridx = 0;
      constraints.weightx = 1.0;
      constraints.weighty = 1.0;
      constraints.fill = GridBagConstraints.BOTH;
      switch (viewDescriptor.getLabelsPosition()) {
        case ASIDE:
          constraints.gridy = currentY + 1;
          constraints.gridwidth = viewDescriptor.getColumnCount() * 2;
          break;
        case ABOVE:
          constraints.gridy = (currentY + 1) * 2;
          constraints.gridwidth = viewDescriptor.getColumnCount();
          break;
        case NONE:
          constraints.gridy = currentY + 1;
          constraints.gridwidth = viewDescriptor.getColumnCount();
          break;
        default:
          break;
      }
      viewComponent.add(filler, constraints);
    }
    completePropertyViewsWithDynamicToolTips(connector, propertyViews, modelDescriptor);
    completePropertyViewsWithDynamicBackgrounds(connector, propertyViews, modelDescriptor);
    completePropertyViewsWithDynamicForegrounds(connector, propertyViews, modelDescriptor);
    completePropertyViewsWithDynamicFonts(connector, propertyViews, modelDescriptor);
    applyComponentViewScrollability(viewDescriptor, viewComponent, view);
    if (!viewDescriptor.isWidthResizeable()) {
      JPanel lefter = new JPanel(new BorderLayout());
      lefter.add(viewComponent, BorderLayout.WEST);
      view.setPeer(lefter);
    }
    return view;
  }

  private void computeComponentGridBagConstraints(IComponentViewDescriptor viewDescriptor, int currentX, int currentY,
                                                  int formInset, int propertyWidth,
                                                  EHorizontalPosition labelHorizontalPosition,
                                                  GridBagConstraints constraints) {
    switch (viewDescriptor.getLabelsPosition()) {
      case ASIDE:
        switch (labelHorizontalPosition) {
          case RIGHT:
            constraints.anchor = GridBagConstraints.EAST;
            constraints.gridx = currentX * 2;
            break;
          case LEFT:
          default:
            constraints.anchor = GridBagConstraints.WEST;
            constraints.gridx = currentX * 2 + 1;
            break;
        }
        constraints.insets = new Insets(formInset, 0, formInset, formInset);
        constraints.gridwidth = propertyWidth * 2 - 1;
        break;
      case ABOVE:
        constraints.gridy++;
        constraints.insets = new Insets(0, formInset, 0, formInset);
        constraints.gridwidth = propertyWidth;
        constraints.anchor = GridBagConstraints.WEST;
        break;
      case NONE:
        constraints.gridx = currentX;
        constraints.gridy = currentY;
        constraints.insets = new Insets(0, formInset, 0, formInset);
        constraints.gridwidth = propertyWidth;
        constraints.anchor = GridBagConstraints.WEST;
        break;
      default:
        break;
    }
  }

  private void computeLabelGridBagConstraints(IComponentViewDescriptor viewDescriptor, int currentX, int currentY,
                                              int formInset, int propertyWidth,
                                              EHorizontalPosition labelHorizontalPosition,
                                              GridBagConstraints constraints) {
    switch (viewDescriptor.getLabelsPosition()) {
      case ASIDE:
        constraints.insets = new Insets(formInset, formInset, formInset, formInset);
        switch (labelHorizontalPosition) {
          case RIGHT:
            constraints.anchor = GridBagConstraints.WEST;
            constraints.gridx = currentX * 2 + propertyWidth * 2 - 1;
            break;
          case LEFT:
          default:
            constraints.anchor = GridBagConstraints.EAST;
            constraints.gridx = currentX * 2;
            break;
        }
        constraints.gridy = currentY;
        break;
      case ABOVE:
        constraints.insets = new Insets(formInset, formInset, 0, formInset);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        constraints.gridx = currentX;
        constraints.gridy = currentY * 2;
        constraints.gridwidth = propertyWidth;
        break;
      case NONE:
        break;
      default:
        break;
    }
  }

  private void completePropertyViewsWithDynamicToolTips(ICompositeValueConnector connector,
                                                        List<IView<JComponent>> propertyViews,
                                                        IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic tooltips
    for (IView<JComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView.getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
      String dynamicToolTipProperty = computePropertyDynamicToolTip(modelDescriptor, propertyViewDescriptor,
          propertyDescriptor);
      // Dynamic tooltip
      if (dynamicToolTipProperty != null) {
        IValueConnector tooltipConnector = connector.getChildConnector(dynamicToolTipProperty);
        if (tooltipConnector == null) {
          tooltipConnector = getConnectorFactory().createValueConnector(dynamicToolTipProperty);
          connector.addChildConnector(dynamicToolTipProperty, tooltipConnector);
        }
        attachToolTipListener(propertyView.getPeer(), tooltipConnector);
      }
    }
  }

  private void completePropertyViewsWithDynamicBackgrounds(ICompositeValueConnector connector,
                                                           List<IView<JComponent>> propertyViews,
                                                           IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic background
    for (IView<JComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView.getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
      String dynamicBackgroundProperty = computePropertyDynamicBackground(modelDescriptor, propertyViewDescriptor,
          propertyDescriptor);
      // Dynamic background
      if (dynamicBackgroundProperty != null) {
        IValueConnector backgroundConnector = connector.getChildConnector(dynamicBackgroundProperty);
        if (backgroundConnector == null) {
          backgroundConnector = getConnectorFactory().createValueConnector(dynamicBackgroundProperty);
          connector.addChildConnector(dynamicBackgroundProperty, backgroundConnector);
        }
        attachBackgroundListener(propertyView.getPeer(), backgroundConnector);
      }
    }
  }

  private void completePropertyViewsWithDynamicForegrounds(ICompositeValueConnector connector,
                                                           List<IView<JComponent>> propertyViews,
                                                           IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic foreground
    for (IView<JComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView.getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
      String dynamicForegroundProperty = computePropertyDynamicForeground(modelDescriptor, propertyViewDescriptor,
          propertyDescriptor);
      // Dynamic foreground
      if (dynamicForegroundProperty != null) {
        IValueConnector foregroundConnector = connector.getChildConnector(dynamicForegroundProperty);
        if (foregroundConnector == null) {
          foregroundConnector = getConnectorFactory().createValueConnector(dynamicForegroundProperty);
          connector.addChildConnector(dynamicForegroundProperty, foregroundConnector);
        }
        attachForegroundListener(propertyView.getPeer(), foregroundConnector);
      }
    }
  }

  private void completePropertyViewsWithDynamicFonts(ICompositeValueConnector connector,
                                                     List<IView<JComponent>> propertyViews,
                                                     IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic font
    for (IView<JComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView.getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
      String dynamicFontProperty = computePropertyDynamicFont(modelDescriptor, propertyViewDescriptor,
          propertyDescriptor);
      // Dynamic font
      if (dynamicFontProperty != null) {
        IValueConnector fontConnector = connector.getChildConnector(dynamicFontProperty);
        if (fontConnector == null) {
          fontConnector = getConnectorFactory().createValueConnector(dynamicFontProperty);
          connector.addChildConnector(dynamicFontProperty, fontConnector);
        }
        attachFontListener(propertyView.getPeer(), fontConnector);
      }
    }
  }

  private void applyComponentViewScrollability(IComponentViewDescriptor viewDescriptor, final JPanel viewComponent,
                                               IView<JComponent> view) {
    if (viewDescriptor.isVerticallyScrollable()) {
      JScrollPane scrollPane = createJScrollPane();
      scrollPane.setBorder(BorderFactory.createEmptyBorder());
      if (viewComponent instanceof JScrollablePanel) {
        ((JScrollablePanel) viewComponent).setScrollableTracksViewportWidth(true);
      }
      scrollPane.setViewportView(viewComponent);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      view.setPeer(scrollPane);
    }
  }

  /**
   * Attaches a dynamic tooltip listener.
   *
   * @param viewComponent
   *     the view component to attach the tooltip to
   * @param connector
   *     the view connector responsible for the tooltip.
   */
  protected void attachToolTipListener(final JComponent viewComponent, IValueConnector connector) {
    // Special toolTip handling
    if (connector != null) {
      connector.addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          if (evt.getNewValue() != null) {
            viewComponent.setToolTipText(evt.getNewValue().toString());
          } else {
            viewComponent.setToolTipText(null);
          }
        }
      });
    }
  }

  /**
   * Attaches a dynamic background listener.
   *
   * @param viewComponent
   *     the view component to attach the background to
   * @param connector
   *     the view connector responsible for the background.
   */
  protected void attachBackgroundListener(final JComponent viewComponent, IValueConnector connector) {
    if (connector != null) {
      connector.addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          if (evt.getNewValue() != null) {
            viewComponent.setBackground(createColor(evt.getNewValue().toString()));
          } else {
            viewComponent.setBackground(null);
          }
        }
      });
    }
  }

  /**
   * Attaches a dynamic foreground listener.
   *
   * @param viewComponent
   *     the view component to attach the foreground to
   * @param connector
   *     the view connector responsible for the foreground.
   */
  protected void attachForegroundListener(final JComponent viewComponent, IValueConnector connector) {
    if (connector != null) {
      connector.addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          if (evt.getNewValue() != null) {
            viewComponent.setForeground(createColor(evt.getNewValue().toString()));
          } else {
            viewComponent.setForeground(null);
          }
        }
      });
    }
  }

  /**
   * Attaches a dynamic font listener.
   *
   * @param viewComponent
   *     the view component to attach the font to
   * @param connector
   *     the view connector responsible for the font.
   */
  protected void attachFontListener(final JComponent viewComponent, IValueConnector connector) {
    final Font defaultFont = viewComponent.getFont();
    if (connector != null) {
      connector.addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          if (evt.getNewValue() != null) {
            viewComponent.setFont(createFont(evt.getNewValue().toString(), defaultFont));
          } else {
            viewComponent.setFont(defaultFont);
          }
        }
      });
    }
  }

  private JLabel createFormPropertyLabel(IActionHandler actionHandler, Locale locale,
                                         IPropertyViewDescriptor propertyViewDescriptor,
                                         IPropertyDescriptor propertyDescriptor, IView<JComponent> propertyView,
                                         boolean forbidden) {
    JLabel propertyLabel = createPropertyLabel(propertyViewDescriptor, propertyView.getPeer(), actionHandler, locale);
    if (!propertyViewDescriptor.isReadOnly() && propertyDescriptor.isMandatory()
        && !(propertyDescriptor instanceof IBooleanPropertyDescriptor)) {
      if (propertyViewDescriptor.getLabelForeground() == null) {
        propertyLabel.setForeground(createColor(getFormLabelMandatoryPropertyColorHex()));
      }
      propertyLabel.setText(decorateMandatoryPropertyLabel(propertyLabel.getText()));
    }
    if (forbidden) {
      propertyLabel.setText(" ");
      propertyLabel.setIcon(null);
    }
    return propertyLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createMapView(IMapViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                            Locale locale) {
    BasicMapPanel viewComponent = createMapView();
    MapBean mapBean = viewComponent.getMapBean();
    mapBean.setScale(100000);

    MapHandler mapHandler = viewComponent.getMapHandler();
    // Add navigation tools over the map
    mapHandler.add(new EmbeddedNavPanel());
    // Add scale display widget over the map
    mapHandler.add(new EmbeddedScaleDisplayPanel());
    // Add MouseDelegator, which handles mouse modes (managing mouse
    // events)
    mapHandler.add(new MouseDelegator());
    // Add OMMouseMode, which handles how the map reacts to mouse
    // movements
    mapHandler.add(new OMMouseMode());

    mapHandler.add(new LayerHandler());
    MapTileLayer mapTileLayer = new MapTileLayer();
    Properties tileProperties = new Properties();
    tileProperties.setProperty("rootDir", "http://c.tile.openstreetmap.org/");
    mapTileLayer.setProperties(tileProperties);
    mapTileLayer.setVisible(true);

    mapHandler.add(mapTileLayer);

    IModelDescriptor modelDescriptor = viewDescriptor.getModelDescriptor();
    String connectorId;
    if (modelDescriptor instanceof IPropertyDescriptor) {
      connectorId = modelDescriptor.getName();
    } else {
      connectorId = ModelRefPropertyConnector.THIS_PROPERTY;
    }
    IValueConnector connector = new JMapViewConnector(connectorId, mapBean, viewDescriptor.getLongitudeProperty(),
        viewDescriptor.getLatitudeProperty());
    IView<JComponent> view = constructView(viewComponent, viewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createConstrainedGridView(IConstrainedGridViewDescriptor viewDescriptor,
                                                                 IActionHandler actionHandler, Locale locale) {
    JPanel viewComponent = createJPanel();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<>();

    GridBagLayout layout = new GridBagLayout();
    viewComponent.setLayout(layout);

    for (IViewDescriptor childViewDescriptor : viewDescriptor.getChildViewDescriptors()) {
      IView<JComponent> childView = createView(childViewDescriptor, actionHandler, locale);
      viewComponent.add(childView.getPeer(),
          createGridBagConstraints(viewDescriptor.getCellConstraints(childViewDescriptor)));
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createDatePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {
    IDatePropertyDescriptor propertyDescriptor = (IDatePropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IValueConnector connector;
    JComponent viewComponent;
    TimeZone timeZone =
        propertyDescriptor.isTimeZoneAware() ? actionHandler.getClientTimeZone() : actionHandler.getReferenceTimeZone();
    DateFormat format = createDateFormat(propertyDescriptor, timeZone, actionHandler, locale);
    IFormatter<?, String> formatter = createFormatter(format);
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName(), (JLabel) viewComponent);
      ((JLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createJDateField(propertyViewDescriptor, locale);
      ((JDateField) viewComponent).getFormattedTextField().setFormatterFactory(
          new DefaultFormatterFactory(new DateFormatter(format)));
      connector = new JDateFieldConnector(propertyDescriptor.getName(), (JDateField) viewComponent);
    }
    adjustSizes(propertyViewDescriptor, viewComponent, formatter, getDateTemplateValue(propertyDescriptor),
        Toolkit.getDefaultToolkit().getScreenResolution() / 3);
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createDecimalPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IDecimalPropertyDescriptor propertyDescriptor = (IDecimalPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(propertyViewDescriptor, actionHandler, locale);
    }
    IFormatter<Object, String> formatter = createDecimalFormatter(propertyDescriptor, actionHandler, locale);
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName(), (JLabel) viewComponent);
      ((JLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createJTextField(propertyViewDescriptor);
      connector = new JFormattedFieldConnector(propertyDescriptor.getName(), (JTextField) viewComponent, formatter);
    }
    adjustSizes(propertyViewDescriptor, viewComponent, formatter, getDecimalTemplateValue(propertyDescriptor));
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createDurationPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                         IActionHandler actionHandler, Locale locale) {
    IDurationPropertyDescriptor propertyDescriptor = (IDurationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JComponent viewComponent;
    IValueConnector connector;
    IFormatter<?, String> formatter = createDurationFormatter(propertyDescriptor, actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName(), (JLabel) viewComponent);
      ((JLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createJTextField(propertyViewDescriptor);
      connector = new JFormattedFieldConnector(propertyDescriptor.getName(), (JTextField) viewComponent, formatter);
    }
    adjustSizes(propertyViewDescriptor, viewComponent, formatter, getDurationTemplateValue(propertyDescriptor));
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JComponent createEmptyComponent() {
    return createJPanel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createEnumerationPropertyView(final IPropertyViewDescriptor propertyViewDescriptor,
                                                            IActionHandler actionHandler, Locale locale) {
    final IEnumerationPropertyDescriptor propertyDescriptor = (IEnumerationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IView<JComponent> view;
    if (propertyViewDescriptor.isReadOnly()) {
      IFormatter<?, String> formatter = createEnumerationFormatter(propertyDescriptor, actionHandler, locale);
      final JLabel viewComponent;
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      IValueConnector connector = new JLabelConnector(propertyDescriptor.getName(), viewComponent);
      connector.addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          viewComponent.setIcon(getIconFactory()
              .getIcon(propertyDescriptor.getIconImageURL(String.valueOf(evt.getNewValue())),
                  getEnumerationIconDimension(propertyViewDescriptor)));
        }
      });
      ((JLabelConnector) connector).setFormatter(formatter);
      adjustSizes(propertyViewDescriptor, viewComponent, null,
          getEnumerationTemplateValue(propertyDescriptor, actionHandler, locale),
          Toolkit.getDefaultToolkit().getScreenResolution() * 2 / 6);
      connector.setExceptionHandler(actionHandler);
      view = constructView(viewComponent, propertyViewDescriptor, connector);
    } else {
      if (propertyDescriptor.isLov()) {
        return createEnumerationReferencePropertyView(propertyViewDescriptor, actionHandler, locale);
      } else {
        List<String> enumerationValues = new ArrayList<>(propertyDescriptor.getEnumerationValues());
        filterEnumerationValues(enumerationValues, propertyViewDescriptor);
        if (propertyViewDescriptor instanceof IEnumerationPropertyViewDescriptor
            && ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor).isRadio()) {
          IRenderableCompositeValueConnector connector = getConnectorFactory().createCompositeValueConnector(
              ModelRefPropertyConnector.THIS_PROPERTY, propertyDescriptor.getName());
          JPanel viewComponent = createJPanel();
          GridLayout layout;
          switch (((IEnumerationPropertyViewDescriptor) propertyViewDescriptor).getOrientation()) {
            case HORIZONTAL:
              layout = new GridLayout(1, 0);
              break;
            case VERTICAL:
            default:
              layout = new GridLayout(0, 1);
          }
          viewComponent.setLayout(layout);
          List<IView<JComponent>> childrenViews = new ArrayList<>();
          for (String enumElement : enumerationValues) {
            JRadioButton subViewComponent = new JRadioButton();
            subViewComponent.setText(propertyDescriptor.getI18nValue(enumElement, actionHandler, locale));
            JRadioButtonConnector subConnector = new JRadioButtonConnector(propertyDescriptor.getName(),
                subViewComponent, enumElement);
            adjustSizes(propertyViewDescriptor, subViewComponent, null,
                getEnumerationTemplateValue(propertyDescriptor, actionHandler, locale),
                Toolkit.getDefaultToolkit().getScreenResolution() * 2 / 6);
            viewComponent.add(subViewComponent);
            subConnector.setExceptionHandler(actionHandler);
            IView<JComponent> subView = constructView(subViewComponent, propertyViewDescriptor, subConnector);
            connector.addChildConnector(subConnector);
            childrenViews.add(subView);
          }
          view = constructCompositeView(viewComponent, propertyViewDescriptor);
          ((BasicCompositeView<JComponent>) view).setChildren(childrenViews);
          connector.setExceptionHandler(actionHandler);
          view.setConnector(connector);
        } else {
          JComboBox<String> viewComponent = createJComboBox(propertyViewDescriptor);
          if (!propertyDescriptor.isMandatory()) {
            viewComponent.addItem(null);
          }
          for (String enumElement : enumerationValues) {
            viewComponent.addItem(enumElement);
          }
          viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(propertyDescriptor,
              getEnumerationIconDimension(propertyViewDescriptor), actionHandler, locale));
          adjustSizes(propertyViewDescriptor, viewComponent, null,
              getEnumerationTemplateValue(propertyDescriptor, actionHandler, locale),
              getEnumerationIconDimension(propertyViewDescriptor).getWidth()/*Toolkit.getDefaultToolkit()
                                                                                      .getScreenResolution() * 3 / 5*/);
          IValueConnector connector = new JComboBoxConnector(propertyDescriptor.getName(), viewComponent);
          connector.setExceptionHandler(actionHandler);
          view = constructView(viewComponent, propertyViewDescriptor, connector);
        }
      }
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createEvenGridView(IEvenGridViewDescriptor viewDescriptor,
                                                          IActionHandler actionHandler, Locale locale) {
    JPanel viewComponent = createJPanel();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<>();

    GridLayout layout = new GridLayout();
    switch (viewDescriptor.getDrivingDimension()) {
      case ROW:
        layout.setColumns(viewDescriptor.getDrivingDimensionCellCount());
        layout.setRows(0);
        break;
      case COLUMN:
        layout.setRows(viewDescriptor.getDrivingDimensionCellCount());
        layout.setColumns(0);
        break;
      default:
        break;
    }
    layout.setHgap(5);
    layout.setVgap(5);
    viewComponent.setLayout(layout);

    for (IViewDescriptor childViewDescriptor : viewDescriptor.getChildViewDescriptors()) {
      IView<JComponent> childView = createView(childViewDescriptor, actionHandler, locale);
      viewComponent.add(childView.getPeer());
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createHtmlPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {
    IHtmlPropertyDescriptor propertyDescriptor = (IHtmlPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      JTextPane htmlPane = createJTextPane(propertyViewDescriptor);
      JTextPaneConnector textPaneConnector = new JTextPaneConnector(propertyDescriptor.getName(), htmlPane);
      JScrollPane scrollPane = createJScrollPane();
      scrollPane.setViewportView(htmlPane);
      if (propertyViewDescriptor instanceof IScrollableViewDescriptor) {
        if (((IScrollableViewDescriptor) propertyViewDescriptor).isHorizontallyScrollable()) {
          JPanel noWrapPanel = createJPanel();
          noWrapPanel.setLayout(new BorderLayout());
          noWrapPanel.add(htmlPane, BorderLayout.CENTER);
          scrollPane.setViewportView(noWrapPanel);
          scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        } else {
          scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
        if (((IScrollableViewDescriptor) propertyViewDescriptor).isVerticallyScrollable()) {
          scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        } else {
          scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        }
      } else {
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      }
      viewComponent = scrollPane;
      connector = textPaneConnector;
    } else {
      JHTMLEditor htmlEditor = createJHTMLEditor(propertyViewDescriptor, locale);
      JHTMLEditorConnector htmlEditorConnector = new JHTMLEditorConnector(propertyDescriptor.getName(), htmlEditor);
      if (propertyViewDescriptor instanceof IScrollableViewDescriptor) {
        if (((IScrollableViewDescriptor) propertyViewDescriptor).isHorizontallyScrollable()) {
          htmlEditor.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        } else {
          htmlEditor.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
        if (((IScrollableViewDescriptor) propertyViewDescriptor).isVerticallyScrollable()) {
          htmlEditor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        } else {
          htmlEditor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        }
      } else {
        htmlEditor.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        htmlEditor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      }
      viewComponent = htmlEditor;
      connector = htmlEditorConnector;
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IView<JComponent> createImagePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                      IActionHandler actionHandler, Locale locale) {
    final IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    if (propertyViewDescriptor.isReadOnly() || propertyViewDescriptor instanceof IImageViewDescriptor
        || !(propertyDescriptor instanceof IBinaryPropertyDescriptor)) {
      JLabel imageLabel;
      if (propertyViewDescriptor.getAction() != null) {
        imageLabel = createJLink(propertyViewDescriptor);
      } else {
        imageLabel = createJLabel(propertyViewDescriptor, false);
      }
      Integer scaledWidth = null;
      Integer scaledHeight = null;
      if (propertyViewDescriptor instanceof IScalableImageAware) {
        scaledWidth = ((IScalableImageAware) propertyViewDescriptor).getScaledWidth();
        scaledHeight = ((IScalableImageAware) propertyViewDescriptor).getScaledHeight();
      } else if (propertyDescriptor instanceof IScalableImageAware) {
        scaledWidth = ((IScalableImageAware) propertyDescriptor).getScaledWidth();
        scaledHeight = ((IScalableImageAware) propertyDescriptor).getScaledHeight();
      }
      JImageConnector connector = new JImageConnector(propertyViewDescriptor.getModelDescriptor().getName(), imageLabel,
          scaledWidth, scaledHeight);
      connector.setExceptionHandler(actionHandler);
      JPanel viewComponent = createJPanel();
      BorderLayout layout = new BorderLayout();
      viewComponent.setLayout(layout);
      IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
      if ((propertyViewDescriptor instanceof IScrollableViewDescriptor)
          && ((IScrollableViewDescriptor) propertyViewDescriptor).isScrollable()) {
        imageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        JScrollPane scrollPane = createJScrollPane();
        scrollPane.setViewportView(imageLabel);
        viewComponent.add(scrollPane, BorderLayout.CENTER);
      } else {
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        viewComponent.add(imageLabel, BorderLayout.CENTER);
      }
      if (imageLabel instanceof JLink<?>) {
        ((JLink<Action>) imageLabel).setTarget(
            getActionFactory().createAction(propertyViewDescriptor.getAction(), actionHandler, view, locale));
      }
      return view;
    } else {
      return createBinaryPropertyView(propertyViewDescriptor, actionHandler, locale);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createIntegerPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IIntegerPropertyDescriptor propertyDescriptor = (IIntegerPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter<?, String> formatter = createIntegerFormatter(propertyDescriptor, actionHandler, locale);
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName(), (JLabel) viewComponent);
      ((JLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createJTextField(propertyViewDescriptor);
      connector = new JFormattedFieldConnector(propertyDescriptor.getName(), (JTextField) viewComponent, formatter);
    }
    adjustSizes(propertyViewDescriptor, viewComponent, formatter, getIntegerTemplateValue(propertyDescriptor));
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Creates an action field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @param showTextField
   *     is the text field visible to the user.
   * @return the created action field.
   */
  protected JActionField createJActionField(IPropertyViewDescriptor viewDescriptor, boolean showTextField) {
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
   * Creates a button.
   *
   * @return the created button.
   */
  protected JComboButton createJComboButton() {
    JComboButton button = new JComboButton(true);
    SwingUtil.configureButton(button);
    return button;
  }

  /**
   * Creates a check box.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created check box.
   */
  protected JCheckBox createJCheckBox(IPropertyViewDescriptor viewDescriptor) {
    return new JCheckBox();
  }

  /**
   * Creates a radio button.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created radio button.
   */
  protected JRadioButton createJRadioButton(IPropertyViewDescriptor viewDescriptor) {
    return new JRadioButton();
  }

  /**
   * Creates a tri-state check box.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created tri-state check box.
   */
  protected JCheckBox createJTriStateCheckBox(IPropertyViewDescriptor viewDescriptor) {
    return new JTriStateCheckBox();
  }

  /**
   * Creates an color picker.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created color picker.
   */
  protected JColorPicker createJColorPicker(IPropertyViewDescriptor viewDescriptor) {
    return new JColorPicker();
  }

  /**
   * Creates a combo box.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created combo box.
   */
  @SuppressWarnings("unchecked")
  protected JComboBox<String> createJComboBox(IPropertyViewDescriptor viewDescriptor) {
    return new JComboBox<>();
  }

  /**
   * Creates a date field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @param locale
   *     the user locale.
   * @return the created date field.
   */
  protected JDateField createJDateField(IPropertyViewDescriptor viewDescriptor, Locale locale) {
    JDateField dateField = new JDateField(locale);
    return dateField;
  }

  /**
   * Creates a JEdit text area.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @param language
   *     the language to add syntax highlighting for.
   * @return the created text area.
   */
  protected JEditTextArea createJEditTextArea(IPropertyViewDescriptor viewDescriptor, String language) {
    JEditTextArea textArea = new JEditTextArea();
    try {
      textArea.setTokenMarker(
          (TokenMarker) Class.forName("org.syntax.jedit.tokenmarker." + language + "TokenMarker").newInstance());
    } catch (InstantiationException | ClassNotFoundException | IllegalAccessException ex) {
      // Nothing to do. just don't colorize.
    }
    return textArea;
  }

  /**
   * Creates an HTML editor.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @param locale
   *     the locale to create the HTML editor for.
   * @return the created HTML editor.
   */
  protected JHTMLEditor createJHTMLEditor(IPropertyViewDescriptor viewDescriptor, Locale locale) {
    JHTMLEditor htmlEditor = new JHTMLEditor(locale);
    return htmlEditor;
  }

  /**
   * Creates a label.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @param bold
   *     make it bold ?
   * @return the created label.
   */
  protected JLabel createJLabel(IPropertyViewDescriptor viewDescriptor, boolean bold) {
    // To have preferred height computed.
    JLabel label = new JLabel(" ");
    if (bold) {
      label.setFont(createFont(BOLD_FONT, label.getFont()));
    }
    return label;
  }

  /**
   * Creates a map view.
   *
   * @return the created map view.
   */
  protected BasicMapPanel createMapView() {
    return new OverlayMapPanel();
  }

  /**
   * Created an action link.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created action link.
   */
  protected JLink<Action> createJLink(IPropertyViewDescriptor viewDescriptor) {
    // To have preferred height computed.
    JLink<Action> actionLink = new JLink<>(" ", null, "");
    actionLink.addLinkListener(new LinkListener<Action>() {

      @Override
      public boolean linkActivated(JLink<Action> link, Action target) {
        if (target != null) {
          if (target.isEnabled()) {
            ActionEvent ae = new ActionEvent(link, ActionEvent.ACTION_PERFORMED, null);
            target.actionPerformed(ae);
          }
        }
        return false;
      }
    });
    return actionLink;
  }

  /**
   * Creates a list.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created list.
   */
  @SuppressWarnings("unchecked")
  protected JList<IValueConnector> createJList(IListViewDescriptor viewDescriptor) {
    JList<IValueConnector> list = new JList<>();
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
    JScrollablePanel panel = new JScrollablePanel();
    return panel;
  }

  /**
   * Creates a password field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created password field.
   */
  protected JPasswordField createJPasswordField(IPropertyViewDescriptor viewDescriptor) {
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
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created split pane.
   */
  protected JSplitPane createJSplitPane(ISplitViewDescriptor viewDescriptor) {
    JSplitPane splitPane = new JSplitPane();
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    return splitPane;
  }

  /**
   * Creates a tabbed pane.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created tabbed pane.
   */
  protected JTabbedPane createJTabbedPane(ITabViewDescriptor viewDescriptor) {
    return new JTabbedPane();
  }

  /**
   * Creates a table.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created table.
   */
  protected JTable createJTable(ITableViewDescriptor viewDescriptor) {
    JTable table = new JTable() {

      private static final long serialVersionUID = -2766744091893464462L;

      /**
       * Override this method to fix a bug in the JVM which causes the table to
       * start editing when a mnemonic key or function key is pressed.
       */
      @Override
      protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (SwingUtilities.getUIInputMap(this, condition) != null && SwingUtilities.getUIInputMap(this, condition).get(
            ks) != null) {
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
          if (parent.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(ks) != null) {
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
    table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    return table;
  }

  /**
   * Creates a text area.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created text area.
   */
  protected JTextArea createJTextArea(IPropertyViewDescriptor viewDescriptor) {
    JTextArea textArea = new JTextArea();
    textArea.setDragEnabled(true);
    textArea.setWrapStyleWord(true);
    return textArea;
  }

  /**
   * Creates a text field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created text field.
   */
  protected JTextField createJTextField(IPropertyViewDescriptor viewDescriptor) {
    JTextField textField = new JTextField();
    textField.getDocument().putProperty("filterNewlines", Boolean.FALSE);
    SwingUtil.enableSelectionOnFocusGained(textField);
    return textField;
  }

  /**
   * Creates a text pane.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created text pane.
   */
  protected JTextPane createJTextPane(IPropertyViewDescriptor viewDescriptor) {
    JTextPane textPane = new JTextPane();
    textPane.setEditorKit(new HTMLEditorKit());
    textPane.setDragEnabled(true);
    return textPane;
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
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created tree.
   */
  protected JTree createJTree(ITreeViewDescriptor viewDescriptor) {
    JTree tree = new JTree();
    tree.setDragEnabled(true);
    return tree;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("MagicConstant")
  @Override
  protected IView<JComponent> createListView(IListViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    IComponentDescriptor<?> rowDescriptor = modelDescriptor.getCollectionDescriptor().getElementDescriptor();
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory().createCompositeValueConnector(
        modelDescriptor.getName() + "Element", rowDescriptor.getToHtmlProperty());
    if (rowConnectorPrototype instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) rowConnectorPrototype).setDisplayIcon(viewDescriptor.getIcon());
      ((AbstractCompositeValueConnector) rowConnectorPrototype).setIconImageURLProvider(
          viewDescriptor.getIconImageURLProvider());
    }
    ICollectionConnector connector = getConnectorFactory().createCollectionConnector(modelDescriptor.getName(),
        getMvcBinder(), rowConnectorPrototype);
    JList<IValueConnector> viewComponent = createJList(viewDescriptor);
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor, connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createListConnector(viewDescriptor.getRenderedProperty(), rowDescriptor);
      rowConnectorPrototype.addChildConnector(viewDescriptor.getRenderedProperty(), cellConnector);
    }
    viewComponent.setCellRenderer(new EvenOddListCellRenderer(getIconFactory(), viewDescriptor.getRenderedProperty(),
        viewDescriptor.isDisplayIcon()));
    viewComponent.setModel(new CollectionConnectorListModel(connector));
    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));
    listSelectionModelBinder.bindSelectionModel(viewComponent, connector, viewComponent.getSelectionModel(), null);
    if (viewDescriptor.getRowAction() != null) {
      final Action rowAction = getActionFactory().createAction(viewDescriptor.getRowAction(), actionHandler, view,
          locale);
      viewComponent.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            ActionEvent ae = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null, e.getWhen(),
                e.getModifiers());
            rowAction.actionPerformed(ae);
          }
        }
      });
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createPasswordPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                         IActionHandler actionHandler, Locale locale) {
    IPasswordPropertyDescriptor propertyDescriptor = (IPasswordPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JPasswordField viewComponent = createJPasswordField(propertyViewDescriptor);
    JPasswordFieldConnector connector = new JPasswordFieldConnector(propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(propertyViewDescriptor, viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createPercentPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IPercentPropertyDescriptor propertyDescriptor = (IPercentPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter<?, String> formatter = createPercentFormatter(propertyDescriptor, actionHandler, locale);
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName(), (JLabel) viewComponent);
      ((JLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createJTextField(propertyViewDescriptor);
      connector = new JPercentFieldConnector(propertyDescriptor.getName(), (JTextField) viewComponent, formatter);
    }
    adjustSizes(propertyViewDescriptor, viewComponent, formatter, getPercentTemplateValue(propertyDescriptor));
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Creates a property label.
   *
   * @param propertyViewDescriptor
   *     the property view descriptor.
   * @param propertyComponent
   *     the property component.
   * @param translationProvider
   *     the translation provider.
   * @param locale
   *     the locale.
   * @return the created property label.
   */
  protected JLabel createPropertyLabel(IPropertyViewDescriptor propertyViewDescriptor, JComponent propertyComponent,
                                       ITranslationProvider translationProvider, Locale locale) {
    JLabel propertyLabel = createJLabel(propertyViewDescriptor, false);
    String labelText = propertyViewDescriptor.getI18nName(translationProvider, locale);
    propertyLabel.setText(labelText);
    propertyLabel.setLabelFor(propertyComponent);
    configurePropertyLabel(propertyLabel, propertyViewDescriptor);
    return propertyLabel;
  }

  private void configurePropertyLabel(JLabel propertyLabel, IPropertyViewDescriptor propertyViewDescriptor) {
    if (propertyViewDescriptor.getLabelFont() != null) {
      propertyLabel.setFont(createFont(propertyViewDescriptor.getLabelFont(), propertyLabel.getFont()));
    }
    if (propertyViewDescriptor.getLabelForeground() != null) {
      propertyLabel.setForeground(createColor(propertyViewDescriptor.getLabelForeground()));
    }
    if (propertyViewDescriptor.getLabelBackground() != null) {
      propertyLabel.setBackground(createColor(propertyViewDescriptor.getLabelBackground()));
    }
    if (propertyViewDescriptor.getIcon() != null) {
      propertyLabel.setIcon(
          getIconFactory().getIcon(propertyViewDescriptor.getIcon(), getIconFactory().getTinyIconSize()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createReferencePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                          IActionHandler actionHandler, Locale locale) {
    IReferencePropertyDescriptor<?> propertyDescriptor = (IReferencePropertyDescriptor<?>) propertyViewDescriptor
        .getModelDescriptor();
    JComponent viewComponent;
    IValueConnector connector;
    String renderedProperty = computeRenderedProperty(propertyViewDescriptor);
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName() + "." + renderedProperty, (JLabel) viewComponent);
    } else {
      viewComponent = createJActionField(propertyViewDescriptor, true);
      connector = new JReferenceFieldConnector(propertyDescriptor.getName(), (JActionField) viewComponent);
      ((JReferenceFieldConnector) connector).setRenderingConnector(new BasicValueConnector(renderedProperty));
    }
    adjustSizes(propertyViewDescriptor, viewComponent, null, null);
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    if (viewComponent instanceof JActionField) {
      if (propertyViewDescriptor instanceof IReferencePropertyViewDescriptor) {
        ((JActionField) viewComponent).setFieldEditable(
            ((IReferencePropertyViewDescriptor) propertyViewDescriptor).isAutoCompleteEnabled());
      }
      Action lovAction = createLovAction(view, actionHandler, locale);
      // lovAction.putValue(Action.NAME,
      // actionHandler.getTranslation(
      // "lov.element.name",
      // new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
      // actionHandler, locale)}, locale));
      lovAction.putValue(Action.SHORT_DESCRIPTION, actionHandler.getTranslation("lov.element.description",
          new Object[]{propertyDescriptor.getReferencedDescriptor().getI18nName(actionHandler, locale)}, locale)
          + IActionFactory.TOOLTIP_ELLIPSIS);
      if (propertyDescriptor.getReferencedDescriptor().getIcon() != null) {
        lovAction.putValue(Action.SMALL_ICON, getIconFactory()
            .getIcon(propertyDescriptor.getReferencedDescriptor().getIcon(), getIconFactory().getTinyIconSize()));
      }
      ((JActionField) viewComponent).setActions(Collections.singletonList(lovAction));
      if (propertyViewDescriptor instanceof IStringPropertyViewDescriptor) {
        attachCharAction(view, ((JActionField) viewComponent).getTextField(),
            ((IStringPropertyViewDescriptor) propertyViewDescriptor).getCharacterAction(), actionHandler, locale);
      }
    }
    return view;
  }

  /**
   * Creates a security panel.
   *
   * @return the created security panel.
   */
  @Override
  protected JPanel createSecurityComponent() {
    JPanel panel = createJPanel();
    panel.setLayout(new BorderLayout());
    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createSourceCodePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                           IActionHandler actionHandler, Locale locale) {
    ISourceCodePropertyDescriptor propertyDescriptor = (ISourceCodePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JEditTextArea viewComponent = createJEditTextArea(propertyViewDescriptor, propertyDescriptor.getLanguage());
    JEditTextAreaConnector connector = new JEditTextAreaConnector(propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createSplitView(ISplitViewDescriptor viewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    JSplitPane viewComponent = createJSplitPane(viewDescriptor);
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<>();

    switch (viewDescriptor.getOrientation()) {
      case HORIZONTAL:
        viewComponent.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        break;
      case VERTICAL:
        viewComponent.setOrientation(JSplitPane.VERTICAL_SPLIT);
        break;
      default:
        break;
    }

    if (viewDescriptor.getLeftTopViewDescriptor() != null) {
      IView<JComponent> leftTopView = createView(viewDescriptor.getLeftTopViewDescriptor(), actionHandler, locale);
      viewComponent.setLeftComponent(leftTopView.getPeer());
      childrenViews.add(leftTopView);
    }
    if (viewDescriptor.getRightBottomViewDescriptor() != null) {
      IView<JComponent> rightBottomView = createView(viewDescriptor.getRightBottomViewDescriptor(), actionHandler,
          locale);
      viewComponent.setRightComponent(rightBottomView.getPeer());
      rightBottomView.getPeer().setMinimumSize(new Dimension(0, 0));
      childrenViews.add(rightBottomView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createStringPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName(), (JLabel) viewComponent);
    } else {
      viewComponent = createJTextField(propertyViewDescriptor);
      connector = new JTextFieldConnector(propertyDescriptor.getName(), (JTextField) viewComponent);
    }
    adjustSizes(propertyViewDescriptor, viewComponent, null, getStringTemplateValue(propertyDescriptor));
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);

    if (viewComponent instanceof JTextComponent && propertyViewDescriptor instanceof IStringPropertyViewDescriptor) {
      attachCharAction(view, (JTextComponent) viewComponent,
          ((IStringPropertyViewDescriptor) propertyViewDescriptor).getCharacterAction(), actionHandler, locale);
    }
    return view;
  }

  /**
   * Creates a table cell renderer for a given property descriptor.
   *
   * @param propertyDescriptor
   *     the property descriptor to create the renderer for.
   * @param propertyViewDescriptor
   *     the property view descriptor
   * @param actionHandler
   *     the action handler.
   * @param locale
   *     the locale.
   * @return the created table cell renderer.
   */
  protected TableCellRenderer createTableCellRenderer(IPropertyDescriptor propertyDescriptor,
                                                      IPropertyViewDescriptor propertyViewDescriptor,
                                                      IActionHandler actionHandler, Locale locale) {
    TableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      cellRenderer = createBooleanTableCellRenderer((IBooleanPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      TimeZone timeZone =
          ((IDatePropertyDescriptor) propertyDescriptor).isTimeZoneAware() ? actionHandler.getClientTimeZone() :
              actionHandler.getReferenceTimeZone();
      cellRenderer = createDateTableCellRenderer((IDatePropertyDescriptor) propertyDescriptor, timeZone, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      cellRenderer = createTimeTableCellRenderer((ITimePropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      cellRenderer = createDurationTableCellRenderer((IDurationPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
      org.jspresso.framework.util.gui.Dimension iconSize = getEnumerationIconDimension(propertyViewDescriptor);
      cellRenderer = createEnumerationTableCellRenderer((IEnumerationPropertyDescriptor) propertyDescriptor, iconSize,
          actionHandler, locale);
    } else if (propertyDescriptor instanceof INumberPropertyDescriptor) {
      cellRenderer = createNumberTableCellRenderer((INumberPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      cellRenderer = createRelationshipEndTableCellRenderer((IRelationshipEndPropertyDescriptor) propertyDescriptor,
          locale);
    } else if (propertyDescriptor instanceof IBinaryPropertyDescriptor) {
      cellRenderer = createBinaryTableCellRenderer((IBinaryPropertyDescriptor) propertyDescriptor,
          propertyViewDescriptor);
    } else if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      cellRenderer = createStringTableCellRenderer((IStringPropertyDescriptor) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof IColorPropertyDescriptor) {
      cellRenderer = createColorTableCellRenderer((IColorPropertyDescriptor) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JComponent decorateWithPaginationView(JComponent viewPeer, JComponent paginationViewPeer) {
    JPanel decorator = new JPanel(new BorderLayout());
    decorator.add(viewPeer, BorderLayout.CENTER);
    decorator.add(paginationViewPeer, BorderLayout.SOUTH);
    return decorator;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({"ConstantConditions", "MagicConstant"})
  @Override
  protected IView<JComponent> createTableView(ITableViewDescriptor viewDescriptor, final IActionHandler actionHandler,
                                              Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    IComponentDescriptor<?> rowDescriptor = modelDescriptor.getCollectionDescriptor().getElementDescriptor();
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory().createCompositeValueConnector(
        modelDescriptor.getName() + "Element", rowDescriptor.getToHtmlProperty());
    ICollectionConnector connector = getConnectorFactory().createCollectionConnector(modelDescriptor.getName(),
        getMvcBinder(), rowConnectorPrototype);
    JTable viewComponent = createJTable(viewDescriptor);
    viewComponent.getTableHeader().setReorderingAllowed(viewDescriptor.isColumnReorderingAllowed());
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    JLabel iconLabel = new JLabel();
    iconLabel.setIcon(getIconFactory().getIcon(rowDescriptor.getIcon(), getIconFactory().getTinyIconSize()));
    iconLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER, iconLabel);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor, connector);
    if (viewDescriptor.isHorizontallyScrollable()) {
      viewComponent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    } else {
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    List<Class<?>> columnClasses = new ArrayList<>();
    Set<String> forbiddenColumns = new HashSet<>();
    Map<IPropertyViewDescriptor, Integer> userColumnViewDescriptors = getUserColumnViewDescriptors(viewDescriptor,
        actionHandler);
    for (Map.Entry<IPropertyViewDescriptor, Integer> columnViewDescriptorEntry : userColumnViewDescriptors.entrySet()) {
      IPropertyViewDescriptor columnViewDescriptor = columnViewDescriptorEntry.getKey();
      String columnId = columnViewDescriptor.getModelDescriptor().getName();
      if (actionHandler.isAccessGranted(columnViewDescriptor)) {
        try {
          actionHandler.pushToSecurityContext(columnViewDescriptor);
          IValueConnector columnConnector = createColumnConnector(columnViewDescriptor, rowDescriptor, actionHandler);
          if (columnViewDescriptor.getAction() != null && !columnViewDescriptor.isReadOnly()) {
            // We must listen for incoming connector value change to trigger the
            // action.
            columnConnector.addValueChangeListener(
                new ConnectorActionAdapter<>(columnViewDescriptor.getAction(), getActionFactory(), actionHandler,
                    view));
          }
          String propertyName = columnViewDescriptor.getModelDescriptor().getName();
          rowConnectorPrototype.addChildConnector(propertyName, columnConnector);
          columnClasses.add(rowDescriptor.getPropertyDescriptor(columnId).getModelType());
          // already handled in createColumnConnector
          // if (columnViewDescriptor.getReadabilityGates() != null) {
          // if (columnViewDescriptor.getWritabilityGates() != null) {
        } finally {
          actionHandler.restoreLastSecurityContextSnapshot();
        }
      } else {
        // The column simply won't be added.
        forbiddenColumns.add(columnId);
      }
    }
    List<String> columnConnectorKeys = new ArrayList<>(rowConnectorPrototype.getChildConnectorKeys());
    // remove row rendering connector id
    columnConnectorKeys.remove(0);
    CollectionConnectorTableModel tableModel = new CollectionConnectorTableModel(connector, columnConnectorKeys,
        columnClasses);
    tableModel.setExceptionHandler(actionHandler);
    setupTableModel(viewDescriptor, actionHandler, connector, viewComponent, tableModel);
    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));
    int maxColumnSize = computePixelWidth(viewComponent, getMaxColumnCharacterLength());
    int columnIndex = 0;
    for (Map.Entry<IPropertyViewDescriptor, Integer> columnViewDescriptorEntry : userColumnViewDescriptors.entrySet()) {
      IPropertyViewDescriptor columnViewDescriptor = columnViewDescriptorEntry.getKey();
      String propertyName = columnViewDescriptor.getModelDescriptor().getName();
      if (!forbiddenColumns.contains(propertyName)) {
        configureTableColumn(actionHandler, locale, viewDescriptor, connector, rowConnectorPrototype, viewComponent,
            view, maxColumnSize, columnIndex, columnViewDescriptorEntry, columnViewDescriptor, propertyName,
            viewComponent.getModel());
        columnIndex++;
      }
    }
    viewComponent.addMouseListener(new PopupListener(viewComponent, view, actionHandler, locale));
    int minimumWidth = 0;
    for (int i = 0; i < 1 && i < viewComponent.getColumnModel().getColumnCount(); i++) {
      minimumWidth += viewComponent.getColumnModel().getColumn(i).getPreferredWidth();
    }
    scrollPane.setMinimumSize(new Dimension(minimumWidth,
        viewComponent.getRowHeight() * 6 + viewComponent.getTableHeader().getPreferredSize().height));
    if (viewDescriptor.getRowAction() != null) {
      final Action rowAction = getActionFactory().createAction(viewDescriptor.getRowAction(), actionHandler, view,
          locale);
      viewComponent.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            ActionEvent ae = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null, e.getWhen(),
                e.getModifiers());
            rowAction.actionPerformed(ae);
          }
        }
      });
    }
    if (viewDescriptor.getPermId() != null) {
      viewComponent.getColumnModel().addColumnModelListener(
          new ColumnPreferencesListener(viewComponent, viewDescriptor.getPermId(), actionHandler));
    }

    String dynamicBackgroundProperty = computeComponentDynamicBackground(viewDescriptor, rowDescriptor);
    if (dynamicBackgroundProperty != null) {
      IValueConnector backgroundConnector = rowConnectorPrototype.getChildConnector(dynamicBackgroundProperty);
      if (backgroundConnector == null) {
        backgroundConnector = getConnectorFactory().createValueConnector(dynamicBackgroundProperty);
        rowConnectorPrototype.addChildConnector(dynamicBackgroundProperty, backgroundConnector);
      }
    }
    tableModel.setRowBackgroundProperty(dynamicBackgroundProperty);

    String dynamicForegroundProperty = computeComponentDynamicForeground(viewDescriptor, rowDescriptor);
    if (dynamicForegroundProperty != null) {
      IValueConnector backgroundConnector = rowConnectorPrototype.getChildConnector(dynamicForegroundProperty);
      if (backgroundConnector == null) {
        backgroundConnector = getConnectorFactory().createValueConnector(dynamicForegroundProperty);
        rowConnectorPrototype.addChildConnector(dynamicForegroundProperty, backgroundConnector);
      }
    }
    tableModel.setRowForegroundProperty(dynamicForegroundProperty);

    String dynamicFontProperty = computeComponentDynamicFont(viewDescriptor, rowDescriptor);
    if (dynamicFontProperty != null) {
      IValueConnector backgroundConnector = rowConnectorPrototype.getChildConnector(dynamicFontProperty);
      if (backgroundConnector == null) {
        backgroundConnector = getConnectorFactory().createValueConnector(dynamicFontProperty);
        rowConnectorPrototype.addChildConnector(dynamicFontProperty, backgroundConnector);
      }
    }
    tableModel.setRowFontProperty(dynamicFontProperty);

    return view;
  }

  @SuppressWarnings("ConstantConditions")
  private void configureTableColumn(final IActionHandler actionHandler, Locale locale,
                                    ITableViewDescriptor viewDescriptor, ICollectionConnector connector,
                                    ICompositeValueConnector rowConnectorPrototype, JTable viewComponent,
                                    IView<JComponent> view, int maxColumnSize, int columnIndex,
                                    Map.Entry<IPropertyViewDescriptor, Integer> columnViewDescriptorEntry,
                                    IPropertyViewDescriptor columnViewDescriptor, String propertyName,
                                    TableModel tableModel) {
    IComponentDescriptor<?> rowDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor.getModelDescriptor())
        .getCollectionDescriptor().getElementDescriptor();
    TableColumn column = viewComponent.getColumnModel().getColumn(columnIndex);
    column.setIdentifier(computeColumnIdentifier(viewDescriptor, columnViewDescriptor));
    IPropertyDescriptor propertyDescriptor = rowDescriptor.getPropertyDescriptor(propertyName);
    String columnName = columnViewDescriptor.getI18nName(actionHandler, locale);
    if (!columnViewDescriptor.isReadOnly() && propertyDescriptor.isMandatory()
        && !(propertyDescriptor instanceof IBooleanPropertyDescriptor)) {
      columnName = decorateMandatoryPropertyLabel(columnName);
    }
    column.setHeaderValue(columnName);

    IView<JComponent> editorView = createView(columnViewDescriptor, actionHandler, locale);
    editorView.setParent(view);
    editorView.getConnector().resetReadabilityGates();
    editorView.getConnector().resetWritabilityGates();
    for (IValueChangeListener listener : editorView.getConnector().getValueChangeListeners()) {
      if (listener instanceof ConnectorActionAdapter) {
        // to avoid the action to be fired by the editor.
        editorView.getConnector().removeValueChangeListener(listener);
      }
    }
    if (editorView.getConnector().getParentConnector() == null) {
      editorView.getConnector().setParentConnector(connector);
    }
    column.setCellEditor(createTableCellEditor(editorView, actionHandler));
    TableCellRenderer cellRenderer = createTableCellRenderer(propertyDescriptor, columnViewDescriptor, actionHandler,
        locale);
    if (cellRenderer == null) {
      cellRenderer = new EvenOddTableCellRenderer();
    }
    if (cellRenderer instanceof JLabel) {
      configureHorizontalAlignment((JLabel) cellRenderer, columnViewDescriptor.getHorizontalAlignment());
    }
    if (cellRenderer instanceof JComponent) {
      configureComponent(columnViewDescriptor, actionHandler, locale, (JComponent) cellRenderer);
      if (cellRenderer instanceof EvenOddTableCellRenderer) {
        // To preserve font that has been set and avoid JTable changing it.
        ((EvenOddTableCellRenderer) cellRenderer).setCustomFont(((JComponent) cellRenderer).getFont());
      }
    }
    if (cellRenderer instanceof DynamicStyleRenderer) {
      String dynamicToolTipProperty = computePropertyDynamicToolTip(rowDescriptor, columnViewDescriptor,
          propertyDescriptor);
      if (dynamicToolTipProperty != null) {
        IValueConnector toolTipConnector = rowConnectorPrototype.getChildConnector(dynamicToolTipProperty);
        if (toolTipConnector == null) {
          toolTipConnector = getConnectorFactory().createValueConnector(dynamicToolTipProperty);
          rowConnectorPrototype.addChildConnector(dynamicToolTipProperty, toolTipConnector);
        }
      }
      ((DynamicStyleRenderer) cellRenderer).setToolTipProperty(dynamicToolTipProperty);

      String dynamicBackgroundProperty = computePropertyDynamicBackground(rowDescriptor, columnViewDescriptor,
          propertyDescriptor);
      if (dynamicBackgroundProperty != null) {
        IValueConnector backgroundConnector = rowConnectorPrototype.getChildConnector(dynamicBackgroundProperty);
        if (backgroundConnector == null) {
          backgroundConnector = getConnectorFactory().createValueConnector(dynamicBackgroundProperty);
          rowConnectorPrototype.addChildConnector(dynamicBackgroundProperty, backgroundConnector);
        }
      }
      ((DynamicStyleRenderer) cellRenderer).setBackgroundProperty(dynamicBackgroundProperty);

      String dynamicForegroundProperty = computePropertyDynamicForeground(rowDescriptor, columnViewDescriptor,
          propertyDescriptor);
      if (dynamicForegroundProperty != null) {
        IValueConnector foregroundConnector = rowConnectorPrototype.getChildConnector(dynamicForegroundProperty);
        if (foregroundConnector == null) {
          foregroundConnector = getConnectorFactory().createValueConnector(dynamicForegroundProperty);
          rowConnectorPrototype.addChildConnector(dynamicForegroundProperty, foregroundConnector);
        }
      }
      ((DynamicStyleRenderer) cellRenderer).setForegroundProperty(dynamicForegroundProperty);

      String dynamicFontProperty = computePropertyDynamicFont(rowDescriptor, columnViewDescriptor, propertyDescriptor);
      if (dynamicFontProperty != null) {
        IValueConnector fontConnector = rowConnectorPrototype.getChildConnector(dynamicFontProperty);
        if (fontConnector == null) {
          fontConnector = getConnectorFactory().createValueConnector(dynamicFontProperty);
          rowConnectorPrototype.addChildConnector(dynamicFontProperty, fontConnector);
        }
      }
      ((DynamicStyleRenderer) cellRenderer).setFontProperty(dynamicFontProperty);
    }
    if (columnViewDescriptor.getAction() != null && columnViewDescriptor.isReadOnly()) {
      Action colAction = getActionFactory().createAction(columnViewDescriptor.getAction(), actionHandler, view, locale);
      configurePropertyViewAction(columnViewDescriptor, colAction);
      cellRenderer = new HyperlinkTableCellRenderer(cellRenderer, colAction, columnIndex);
      viewComponent.addMouseListener((MouseListener) cellRenderer);
    }
    column.setCellRenderer(cellRenderer);
    EvenOddTableCellRenderer headerRenderer = new EvenOddTableCellRenderer();
    configurePropertyLabel(headerRenderer, columnViewDescriptor);
    if (propertyDescriptor.isMandatory() && !(propertyDescriptor instanceof IBooleanPropertyDescriptor)) {
      if (columnViewDescriptor.getLabelForeground() == null) {
        headerRenderer.setForeground(createColor(getTableHeaderMandatoryPropertyColorHex()));
      }
      headerRenderer.setText(decorateMandatoryPropertyLabel(headerRenderer.getText()));
    }
    // To preserve font that has been set and avoid JTable changing it.
    headerRenderer.setCustomFont(headerRenderer.getFont());
    String viewDescription = columnViewDescriptor.getI18nDescription(actionHandler, locale);
    viewDescription = completeDescriptionWithLiveDebugUI(columnViewDescriptor, viewDescription);
    if (viewDescription != null && viewDescription.length() > 0) {
      headerRenderer.setToolTipText(viewDescription);
    }
    if (tableModel instanceof AbstractTableSorter) {
      column.setHeaderRenderer(
          new AbstractTableSorter.SortableHeaderRenderer((AbstractTableSorter) tableModel, headerRenderer));
    } else {
      column.setHeaderRenderer(headerRenderer);
    }
    if (columnViewDescriptorEntry.getValue() != null) {
      column.setPreferredWidth(columnViewDescriptorEntry.getValue());
    } else {
      if (columnViewDescriptor.getPreferredSize() != null && columnViewDescriptor.getPreferredSize().getWidth() > 0) {
        column.setPreferredWidth(columnViewDescriptor.getPreferredSize().getWidth());
      } else {
        int minHeaderWidth = computePixelWidth(viewComponent, columnName.length());
        if (propertyDescriptor instanceof IBooleanPropertyDescriptor
            || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
          column.setPreferredWidth(Math.max(computePixelWidth(viewComponent, 2), minHeaderWidth));
        } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
          column.setPreferredWidth(Math.max(editorView.getPeer().getPreferredSize().width, minHeaderWidth));
        } else {
          column.setPreferredWidth(Math.max(Math.min(computePixelWidth(viewComponent,
              getFormatLength(createFormatter(propertyDescriptor, actionHandler, locale),
                  getTemplateValue(propertyDescriptor))), maxColumnSize), minHeaderWidth));
        }
      }
    }
  }

  private class ColumnPreferencesListener implements TableColumnModelListener {

    private final TableColumnModel columnModel;
    private final String           tableId;
    private final IActionHandler   actionHandler;
    private       boolean          columnsChanged;

    /**
     * Constructs a new {@code ColumnPreferencesListener} instance.
     *
     * @param table
     *     the table.
     * @param tableId
     *     the table id.
     * @param actionHandler
     *     the action handler.
     */
    public ColumnPreferencesListener(JTable table, String tableId, IActionHandler actionHandler) {
      this.columnModel = table.getColumnModel();
      this.tableId = tableId;
      this.actionHandler = actionHandler;
      this.columnsChanged = false;

      table.getTableHeader().addMouseListener(new MouseAdapter() {

        @Override
        public void mouseReleased(MouseEvent e) {
          saveColumns();
        }
      });
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
      // NO-OP.
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {
      // NO-OP.
    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {
      columnsChanged = true;
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
      columnsChanged = true;
    }

    @Override
    public void columnAdded(TableColumnModelEvent e) {
      // NO-OP.
    }

    private void saveColumns() {
      if (columnsChanged) {
        columnsChanged = false;
        Object[][] columnPrefs = new Object[columnModel.getColumnCount()][2];
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
          Object[] columnPref = new Object[]{columnModel.getColumn(i).getIdentifier(), columnModel.getColumn(
              i).getWidth()};
          columnPrefs[i] = columnPref;
        }
        storeTablePreferences(tableId, columnPrefs, actionHandler);
      }
    }
  }

  private void setupTableModel(ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
                               ICollectionConnector connector, JTable viewComponent, TableModel tableModel) {
    if (viewDescriptor.isSortable()) {
      AbstractTableSorter sorterDecorator;
      if (viewDescriptor.getSortingAction() != null) {
        sorterDecorator = new ActionTableSorter(tableModel, viewComponent.getTableHeader(), actionHandler,
            viewDescriptor.getSortingAction());
      } else {
        sorterDecorator = new TableSorter(tableModel, viewComponent.getTableHeader());
        ((TableSorter) sorterDecorator).setColumnComparator(String.class, String.CASE_INSENSITIVE_ORDER);
      }
      org.jspresso.framework.util.gui.Dimension iconSize = new org.jspresso.framework.util.gui.Dimension(
          viewComponent.getTableHeader().getFont().getSize(), viewComponent.getTableHeader().getFont().getSize());
      sorterDecorator.setUpIcon(getIconFactory().getUpIcon(iconSize));
      sorterDecorator.setDownIcon(getIconFactory().getDownIcon(iconSize));
      viewComponent.setModel(sorterDecorator);
      listSelectionModelBinder.bindSelectionModel(viewComponent, connector, viewComponent.getSelectionModel(),
          sorterDecorator);
    } else {
      viewComponent.setModel(tableModel);
      listSelectionModelBinder.bindSelectionModel(viewComponent, connector, viewComponent.getSelectionModel(), null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createTabView(final ITabViewDescriptor viewDescriptor,
                                                     final IActionHandler actionHandler, Locale locale) {
    final JTabbedPane viewComponent = createJTabbedPane(viewDescriptor);
    final BasicIndexedView<JComponent> view = constructIndexedView(viewComponent, viewDescriptor);
    viewComponent.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent e) {
        JTabbedPane source = (JTabbedPane) e.getSource();
        int selectedIndex = source.getSelectedIndex();
        view.setCurrentViewIndex(selectedIndex);
        storeTabSelectionPreference(viewDescriptor, selectedIndex, actionHandler);
        triggerTabSelectionAction(selectedIndex, source, viewDescriptor,  view,  actionHandler);
      }
    });
    List<IView<JComponent>> childrenViews = new ArrayList<>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor.getChildViewDescriptors()) {
      if (actionHandler.isAccessGranted(childViewDescriptor)) {
        IView<JComponent> childView = createView(childViewDescriptor, actionHandler, locale);
        Icon childIcon = getIconFactory().getIcon(childViewDescriptor.getIcon(), getIconFactory().getSmallIconSize());
        String tabText = childViewDescriptor.getI18nName(actionHandler, locale);
        switch (viewDescriptor.getRenderingOptions()) {
          case ICON:
            tabText = null;
            break;
          case LABEL:
            childIcon = null;
            break;
          default:
            break;
        }
        if (childViewDescriptor.getDescription() != null) {
          viewComponent.addTab(tabText, childIcon, childView.getPeer(),
              childViewDescriptor.getI18nDescription(actionHandler, locale));
        } else {
          viewComponent.addTab(tabText, childIcon, childView.getPeer());
        }
        childrenViews.add(childView);
      }
    }
    view.setChildren(childrenViews);
    viewComponent.setSelectedIndex(getTabSelectionPreference(viewDescriptor, actionHandler));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void selectChildViewIndex(JComponent viewComponent, int index) {
    if (viewComponent instanceof JTabbedPane) {
      ((JTabbedPane) viewComponent).setSelectedIndex(index);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createTextPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IValueConnector connector;
    JScrollPane scrollPane = createJScrollPane();
    JTextArea viewComponent = createJTextArea(propertyViewDescriptor);
    viewComponent.setLineWrap(true);
    scrollPane.setViewportView(viewComponent);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    connector = new JTextAreaConnector(propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(scrollPane, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createTimePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {

    ITimePropertyDescriptor propertyDescriptor = (ITimePropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IValueConnector connector;
    JComponent viewComponent;
    IFormatter<?, String> formatter = createTimeFormatter(propertyDescriptor, actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createJLink(propertyViewDescriptor);
      } else {
        viewComponent = createJLabel(propertyViewDescriptor, true);
      }
      connector = new JLabelConnector(propertyDescriptor.getName(), (JLabel) viewComponent);
      ((JLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createJTextField(propertyViewDescriptor);
      connector = new JFormattedFieldConnector(propertyDescriptor.getName(), (JTextField) viewComponent, formatter);
    }
    adjustSizes(propertyViewDescriptor, viewComponent, formatter, getTimeTemplateValue(propertyDescriptor));
    connector.setExceptionHandler(actionHandler);
    IView<JComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createTreeView(ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {

    ICompositeValueConnector connector = createTreeViewConnector(viewDescriptor, actionHandler, locale);

    final JTree viewComponent = createJTree(viewDescriptor);
    ConnectorHierarchyTreeModel treeModel = new ConnectorHierarchyTreeModel(connector);
    viewComponent.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    viewComponent.setModel(treeModel);
    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer(viewDescriptor.isDisplayIcon()));
    treeSelectionModelBinder.bindSelectionModel(connector, viewComponent);
    if (viewDescriptor.isExpanded()) {
      viewComponent.getModel().addTreeModelListener(new TreeModelListener() {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
          // NO-OP.
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
          expandAll(viewComponent, e.getTreePath());
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
          // NO-OP.
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
          expandAll(viewComponent, e.getTreePath());
        }
      });
    }
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor, connector);
    viewComponent.addMouseListener(new PopupListener(viewComponent, view, actionHandler, locale));
    scrollPane.setMinimumSize(TREE_PREFERRED_SIZE);
    if (viewDescriptor.getRowAction() != null) {
      final Action rowAction = getActionFactory().createAction(viewDescriptor.getRowAction(), actionHandler, view,
          locale);
      viewComponent.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            ActionEvent ae = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null, e.getWhen(),
                e.getModifiers());
            rowAction.actionPerformed(ae);
          }
        }
      });
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithActions(IViewDescriptor viewDescriptor, IActionHandler actionHandler, Locale locale,
                                     IView<JComponent> view) {
    ActionMap actionMap = viewDescriptor.getActionMap();
    ActionMap secondaryActionMap = viewDescriptor.getSecondaryActionMap();
    if (actionMap != null || secondaryActionMap != null) {
      JPanel viewPanel = createJPanel();
      viewPanel.setLayout(new BorderLayout());
      viewPanel.add(view.getPeer(), BorderLayout.CENTER);

      boolean asideActions = false;
      ERenderingOptions defaultRenderingOptions = null;
      IModelDescriptor modelDescriptor = viewDescriptor.getModelDescriptor();
      if (modelDescriptor instanceof IStringPropertyDescriptor || modelDescriptor instanceof IDatePropertyDescriptor
          || modelDescriptor instanceof INumberPropertyDescriptor || modelDescriptor instanceof ITimePropertyDescriptor
          || modelDescriptor instanceof IEnumerationPropertyDescriptor
          || modelDescriptor instanceof IBooleanPropertyDescriptor) {
        asideActions = true;
        defaultRenderingOptions = ERenderingOptions.ICON;
      }
      if (actionMap != null && actionHandler.isAccessGranted(actionMap)) {
        try {
          actionHandler.pushToSecurityContext(actionMap);
          JToolBar toolBar = createViewToolBar(actionMap, view, defaultRenderingOptions, actionHandler, locale);
          if (asideActions) {
            viewPanel.add(toolBar, BorderLayout.EAST);
          } else {
            viewPanel.add(toolBar, BorderLayout.NORTH);
          }
        } finally {
          actionHandler.restoreLastSecurityContextSnapshot();
        }
      }
      if (secondaryActionMap != null && actionHandler.isAccessGranted(secondaryActionMap)) {
        try {
          actionHandler.pushToSecurityContext(secondaryActionMap);
          JToolBar toolBar = createViewToolBar(secondaryActionMap, view, defaultRenderingOptions, actionHandler,
              locale);
          if (asideActions) {
            viewPanel.add(toolBar, BorderLayout.EAST);
          } else {
            viewPanel.add(toolBar, BorderLayout.SOUTH);
          }
        } finally {
          actionHandler.restoreLastSecurityContextSnapshot();
        }
      }

      view.setPeer(viewPanel);
    }
  }

  /**
   * Creates a view toolbar based on an action map.
   *
   * @param actionMap
   *     the action map to create the toolbar for.
   * @param view
   *     the view to create the toolbar for.
   * @param defaultRenderingOptions
   *     overrides default rendering options.
   * @param actionHandler
   *     the action handler used.
   * @param locale
   *     the locale used.   @return the created tool bar.
   * @return the tool bar
   */
  @SuppressWarnings("ConstantConditions")
  protected JToolBar createViewToolBar(ActionMap actionMap, IView<JComponent> view,
                                       ERenderingOptions defaultRenderingOptions, IActionHandler actionHandler,
                                       Locale locale) {
    JToolBar toolBar = createJToolBar();
    for (Iterator<ActionList> iter = actionMap.getActionLists(actionHandler).iterator(); iter.hasNext(); ) {
      ActionList nextActionList = iter.next();
      if (actionHandler.isAccessGranted(nextActionList)) {
        try {
          actionHandler.pushToSecurityContext(nextActionList);
          ERenderingOptions renderingOptions = defaultRenderingOptions;
          if (renderingOptions == null) {
            renderingOptions = getDefaultActionMapRenderingOptions();
          }
          if (nextActionList.getRenderingOptions() != null) {
            renderingOptions = nextActionList.getRenderingOptions();
          } else if (actionMap.getRenderingOptions() != null) {
            renderingOptions = actionMap.getRenderingOptions();
          }
          if (nextActionList.isCollapsable()) {
            JButton actionButton;
            List<IDisplayableAction> actions = new ArrayList<>();
            for (IDisplayableAction action : nextActionList.getActions()) {
              if (actionHandler.isAccessGranted(action)) {
                actions.add(action);
              }
            }
            if (!actions.isEmpty()) {
              if (actions.size() > 1) {
                actionButton = createJComboButton();
              } else {
                actionButton = createJButton();
              }
              IDisplayableAction mainAction = actions.get(0);
              Action swingAction = getActionFactory().createAction(mainAction, actionHandler, view, locale);
              actionButton.setAction(swingAction);
              switch (renderingOptions) {
                case ICON:
                  actionButton.setText("");
                  break;
                case LABEL:
                  actionButton.setIcon(null);
                  break;
                default:
                  break;
              }
              if (mainAction.getAcceleratorAsString() != null) {
                KeyStroke ks = KeyStroke.getKeyStroke(mainAction.getAcceleratorAsString());
                view.getPeer().getActionMap().put(swingAction.getValue(Action.NAME), swingAction);
                view.getPeer().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks,
                    swingAction.getValue(Action.NAME));
                String acceleratorString = KeyEvent.getKeyModifiersText(ks.getModifiers()) + "-" + KeyEvent.getKeyText(
                    ks.getKeyCode());
                actionButton.setToolTipText(
                    "<HTML>" + actionButton.getToolTipText() + " <FONT SIZE=\"-2\" COLOR=\"#993366\">"
                        + acceleratorString + "</FONT></HTML>");
              }
              if (actions.size() > 1) {
                JPopupMenu popupMenu = new JPopupMenu();
                for (IDisplayableAction menuAction : actions) {
                  JMenuItem actionItem = createMenuItem(menuAction, view, actionHandler, locale);
                  switch (renderingOptions) {
                    case ICON:
                      actionItem.setText("");
                      break;
                    case LABEL:
                      actionItem.setIcon(null);
                      break;
                    default:
                      break;
                  }
                  if (menuAction.getAcceleratorAsString() != null) {
                    KeyStroke ks = KeyStroke.getKeyStroke(menuAction.getAcceleratorAsString());
                    view.getPeer().getActionMap().put(swingAction.getValue(Action.NAME), swingAction);
                    view.getPeer().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks,
                        swingAction.getValue(Action.NAME));
                    String acceleratorString = KeyEvent.getKeyModifiersText(ks.getModifiers()) + "-" + KeyEvent
                        .getKeyText(ks.getKeyCode());
                    actionItem.setToolTipText(
                        "<HTML>" + actionItem.getToolTipText() + " <FONT SIZE=\"-2\" COLOR=\"#993366\">"
                            + acceleratorString + "</FONT></HTML>");
                  }
                  popupMenu.add(actionItem);
                }
                ((JComboButton) actionButton).setArrowPopupMenu(popupMenu);
              }
              toolBar.add(actionButton);
            }
          } else {
            for (IDisplayableAction action : nextActionList.getActions()) {
              if (actionHandler.isAccessGranted(action)) {
                Action swingAction = getActionFactory().createAction(action, actionHandler, view, locale);
                JButton actionButton = createJButton();
                actionButton.setAction(swingAction);
                if (action.getAcceleratorAsString() != null) {
                  KeyStroke ks = KeyStroke.getKeyStroke(action.getAcceleratorAsString());
                  view.getPeer().getActionMap().put(swingAction.getValue(Action.NAME), swingAction);
                  view.getPeer().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks,
                      swingAction.getValue(Action.NAME));
                  String acceleratorString = KeyEvent.getKeyModifiersText(ks.getModifiers()) + "-" + KeyEvent
                      .getKeyText(ks.getKeyCode());
                  actionButton.setToolTipText(
                      "<HTML>" + actionButton.getToolTipText() + " <FONT SIZE=\"-2\" COLOR=\"#993366\">"
                          + acceleratorString + "</FONT></HTML>");
                }
                switch (renderingOptions) {
                  case ICON:
                    actionButton.setText("");
                    break;
                  case LABEL:
                    actionButton.setIcon(null);
                    break;
                  default:
                    break;
                }
                toolBar.add(actionButton);
              }
            }
          }
          if (iter.hasNext()) {
            toolBar.addSeparator();
          }
        } finally {
          actionHandler.restoreLastSecurityContextSnapshot();
        }
      }
    }
    return toolBar;
  }

  /**
   * Decorates the created view with the appropriate border.
   *
   * @param view
   *     the view to decorate.
   * @param translationProvider
   *     the translation provider.
   * @param locale
   *     the locale to use.
   */
  @Override
  protected void decorateWithBorder(IView<JComponent> view, ITranslationProvider translationProvider, Locale locale) {
    switch (view.getDescriptor().getBorderType()) {
      case SIMPLE:
        view.getPeer().setBorder(BorderFactory.createEtchedBorder());
        break;
      case TITLED:
        decorateWithTitle(view, translationProvider, locale);
        break;
      default:
        break;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finishComponentConfiguration(IViewDescriptor viewDescriptor, ITranslationProvider translationProvider,
                                              Locale locale, IView<JComponent> view) {
    JComponent viewPeer = view.getPeer();
    configureComponent(viewDescriptor, translationProvider, locale, viewPeer);
  }

  private void configureComponent(IViewDescriptor viewDescriptor, ITranslationProvider translationProvider,
                                  Locale locale, JComponent viewPeer) {
    if (viewDescriptor.getForeground() != null) {
      viewPeer.setForeground(createColor(viewDescriptor.getForeground()));
    }
    if (viewDescriptor.getBackground() != null) {
      viewPeer.setBackground(createColor(viewDescriptor.getBackground()));
    }
    if (viewDescriptor.getFont() != null && FontHelper.isFontSpec(viewDescriptor.getFont())) {
      viewPeer.setFont(createFont(viewDescriptor.getFont(), viewPeer.getFont()));
    }
    String viewDescription = viewDescriptor.getI18nDescription(translationProvider, locale);
    viewDescription = completeDescriptionWithLiveDebugUI(viewDescriptor, viewDescription);
    if (viewDescription != null && viewDescription.length() > 0) {
      viewPeer.setToolTipText(viewDescription);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void showCardInPanel(JComponent cardsPeer, String cardName) {
    JComponent cardPanel = findFirstCardPanel(cardsPeer);
    if (cardPanel != null) {
      ((CardLayout) cardPanel.getLayout()).show(cardPanel, cardName);
    }
  }

  private TableCellRenderer createBooleanTableCellRenderer(
      @SuppressWarnings("unused") IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new BooleanTableCellRenderer();
  }

  private TableCellRenderer createCollectionTableCellRenderer(
      @SuppressWarnings("unused") ICollectionPropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return null;
  }

  static Color createColor(String colorAsHexString) {
    if (colorAsHexString != null && ColorHelper.isColorSpec(colorAsHexString)) {
      int[] rgba = ColorHelper.fromHexString(colorAsHexString);
      return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
    return null;
  }

  private TableCellRenderer createColorTableCellRenderer(
      @SuppressWarnings("unused") IColorPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new ColorTableCellRenderer();
  }

  private TableCellRenderer createDateTableCellRenderer(IDatePropertyDescriptor propertyDescriptor, TimeZone timeZone,
                                                        ITranslationProvider translationProvider, Locale locale) {
    return new FormattedTableCellRenderer(
        createDateFormatter(propertyDescriptor, timeZone, translationProvider, locale));
  }

  private TableCellRenderer createDecimalTableCellRenderer(IDecimalPropertyDescriptor propertyDescriptor,
                                                           ITranslationProvider translationProvider, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentTableCellRenderer((IPercentPropertyDescriptor) propertyDescriptor, translationProvider,
          locale);
    }
    return new FormattedTableCellRenderer(createDecimalFormatter(propertyDescriptor, translationProvider, locale));
  }

  private TableCellRenderer createDurationTableCellRenderer(IDurationPropertyDescriptor propertyDescriptor,
                                                            ITranslationProvider translationProvider, Locale locale) {
    return new FormattedTableCellRenderer(createDurationFormatter(propertyDescriptor, translationProvider, locale));
  }

  private TableCellRenderer createEnumerationTableCellRenderer(IEnumerationPropertyDescriptor propertyDescriptor,
                                                               org.jspresso.framework.util.gui.Dimension iconDimension,
                                                               ITranslationProvider translationProvider,
                                                               Locale locale) {
    return new TranslatedEnumerationTableCellRenderer(propertyDescriptor, iconDimension, translationProvider, locale);
  }

  private TableCellRenderer createImageTableCellRenderer(IPropertyDescriptor propertyDescriptor) {
    return new ImageTableCellRenderer(propertyDescriptor);
  }

  static Font createFont(String fontString, Font defaultFont) {
    org.jspresso.framework.util.gui.Font font = FontHelper.fromString(fontString);
    int fontStyle;
    if (font.isBold() && font.isItalic()) {
      fontStyle = Font.BOLD | Font.ITALIC;
    } else if (font.isBold()) {
      fontStyle = Font.BOLD;
    } else if (font.isItalic()) {
      fontStyle = Font.ITALIC;
    } else {
      fontStyle = Font.PLAIN;
    }
    if (font.getName() == null || font.getName().length() == 0) {
      font.setName(defaultFont.getName());
    }
    if (font.getSize() < 0) {
      font.setSize(defaultFont.getSize());
    }
    return new Font(font.getName(), fontStyle, font.getSize());
  }

  private GridBagConstraints createGridBagConstraints(CellConstraints viewConstraints) {
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

  private TableCellRenderer createIntegerTableCellRenderer(IIntegerPropertyDescriptor propertyDescriptor,
                                                           ITranslationProvider translationProvider, Locale locale) {
    return new FormattedTableCellRenderer(createIntegerFormatter(propertyDescriptor, translationProvider, locale));
  }

  private JPopupMenu createJPopupMenu(IView<JComponent> view, ActionMap actionMap, IActionHandler actionHandler,
                                      Locale locale) {
    IViewDescriptor viewDescriptor = view.getDescriptor();
    JPopupMenu popupMenu = createJPopupMenu();
    JLabel titleLabel = new JLabel();
    titleLabel.setText(viewDescriptor.getI18nName(actionHandler, locale));
    titleLabel.setIcon(getIconFactory().getIcon(viewDescriptor.getIcon(), getIconFactory().getTinyIconSize()));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    popupMenu.add(titleLabel);
    popupMenu.addSeparator();
    for (Iterator<ActionList> iter = actionMap.getActionLists(actionHandler).iterator(); iter.hasNext(); ) {
      ActionList nextActionList = iter.next();
      if (actionHandler.isAccessGranted(nextActionList)) {
        try {
          actionHandler.pushToSecurityContext(nextActionList);
          for (IDisplayableAction action : nextActionList.getActions()) {
            if (actionHandler.isAccessGranted(action)) {
              try {
                actionHandler.pushToSecurityContext(action);
                JMenuItem actionItem = createMenuItem(action, view, actionHandler, locale);
                popupMenu.add(actionItem);
              } finally {
                actionHandler.restoreLastSecurityContextSnapshot();
              }
            }
          }
          if (iter.hasNext()) {
            popupMenu.addSeparator();
          }
        } finally {
          actionHandler.restoreLastSecurityContextSnapshot();
        }
      }
    }
    return popupMenu;
  }

  private JMenuItem createMenuItem(IDisplayableAction action, IView<JComponent> view, IActionHandler actionHandler,
                                   Locale locale) {
    Action swingAction = getActionFactory().createAction(action, actionHandler, view, locale);
    JMenuItem actionItem = createJMenuItem();
    actionItem.setAction(swingAction);
    return actionItem;
  }

  private TableCellRenderer createNumberTableCellRenderer(INumberPropertyDescriptor propertyDescriptor,
                                                          ITranslationProvider translationProvider, Locale locale) {
    TableCellRenderer cellRenderer = null;
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      cellRenderer = createIntegerTableCellRenderer((IIntegerPropertyDescriptor) propertyDescriptor,
          translationProvider, locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      cellRenderer = createDecimalTableCellRenderer((IDecimalPropertyDescriptor) propertyDescriptor,
          translationProvider, locale);
    }
    return cellRenderer;
  }

  private TableCellRenderer createPercentTableCellRenderer(IPercentPropertyDescriptor propertyDescriptor,
                                                           ITranslationProvider translationProvider, Locale locale) {
    return new FormattedTableCellRenderer(createPercentFormatter(propertyDescriptor, translationProvider, locale));
  }

  private TableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused") IReferencePropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return null;
  }

  private TableCellRenderer createRelationshipEndTableCellRenderer(
      IRelationshipEndPropertyDescriptor propertyDescriptor, Locale locale) {
    TableCellRenderer cellRenderer = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      cellRenderer = createReferenceTableCellRenderer((IReferencePropertyDescriptor<?>) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
      cellRenderer = createCollectionTableCellRenderer((ICollectionPropertyDescriptor<?>) propertyDescriptor, locale);
    }
    return null;
  }

  private TableCellRenderer createBinaryTableCellRenderer(IBinaryPropertyDescriptor propertyDescriptor,
                                                          IPropertyViewDescriptor propertyViewDescriptor) {
    if (propertyDescriptor instanceof IImageBinaryPropertyDescriptor
        || propertyViewDescriptor instanceof IImageViewDescriptor) {
      return createImageTableCellRenderer(propertyDescriptor);
    }
    return new BinaryTableCellRenderer(propertyDescriptor);
  }

  private TableCellRenderer createStringTableCellRenderer(IStringPropertyDescriptor propertyDescriptor,
                                                          @SuppressWarnings("unused") Locale locale) {
    if (propertyDescriptor instanceof IPasswordPropertyDescriptor) {
      return new FormattedTableCellRenderer(new PasswordFormatter());
    }
    if (propertyDescriptor instanceof IImageUrlPropertyDescriptor) {
      return createImageTableCellRenderer(propertyDescriptor);
    }
    return new FormattedTableCellRenderer(null);
  }

  private TableCellEditor createTableCellEditor(IView<JComponent> editorView, IActionHandler actionHandler) {
    SwingViewCellEditorAdapter editor;
    if (editorView.getPeer() instanceof JActionField && ((JActionField) editorView.getPeer()).isShowingTextField()) {
      editor = new SwingViewCellEditorAdapter(editorView, getModelConnectorFactory(), getMvcBinder(), actionHandler) {

        private static final long serialVersionUID = -1551909997448473681L;

        @Override
        public boolean stopCellEditing() {
          if (((JActionField) getEditorView().getPeer()).isSynchronized()) {
            fireEditingStopped();
            return true;
          }
          ((JActionFieldConnector) getEditorView().getConnector()).performActionIfNeeded();
          return false;
        }
      };
    } else {
      editor = new SwingViewCellEditorAdapter(editorView, getModelConnectorFactory(), getMvcBinder(), actionHandler);
    }
    return editor;
  }

  private TableCellRenderer createTimeTableCellRenderer(ITimePropertyDescriptor propertyDescriptor,
                                                        ITranslationProvider translationProvider, Locale locale) {
    return new FormattedTableCellRenderer(createTimeFormatter(propertyDescriptor, translationProvider, locale));
  }

  private void decorateWithTitle(IView<JComponent> view, ITranslationProvider translationProvider, Locale locale) {
    view.getPeer().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
        view.getDescriptor().getI18nName(translationProvider, locale)));
  }

  private void expandAll(final JTree tree, final TreePath tp) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        if (tp == null) {
          return;
        }
        Object node = tp.getLastPathComponent();
        TreeModel model = tree.getModel();
        if (!model.isLeaf(node)) {
          tree.expandPath(tp);
          for (int i = 0; i < model.getChildCount(node); i++) {
            expandAll(tree, tp.pathByAddingChild(model.getChild(node, i)));
          }
        }
      }
    });
  }

  private List<String> getDescriptorPathFromConnectorTreePath(TreePath connectorTreePath) {
    List<String> descriptorPath = new ArrayList<>();
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

  private int getSelectionMode(ICollectionViewDescriptor viewDescriptor) {
    int selectionMode;
    switch (viewDescriptor.getSelectionMode()) {
      case SINGLE_SELECTION:
      case SINGLE_CUMULATIVE_SELECTION:
        selectionMode = ListSelectionModel.SINGLE_SELECTION;
        break;
      case SINGLE_INTERVAL_SELECTION:
      case SINGLE_INTERVAL_CUMULATIVE_SELECTION:
        selectionMode = ListSelectionModel.SINGLE_INTERVAL_SELECTION;
        break;
      case MULTIPLE_INTERVAL_SELECTION:
      case MULTIPLE_INTERVAL_CUMULATIVE_SELECTION:
      default:
        selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
        break;
    }
    return selectionMode;
  }

  private void showJTablePopupMenu(JTable table, IView<JComponent> tableView, MouseEvent evt,
                                   IActionHandler actionHandler, Locale locale) {
    int row = table.rowAtPoint(evt.getPoint());
    if (row < 0) {
      return;
    }

    if (!table.isRowSelected(row)) {
      table.setRowSelectionInterval(row, row);
    }

    ActionMap actionMap = tableView.getDescriptor().getActionMap();

    if (actionMap != null && actionHandler.isAccessGranted(actionMap)) {
      try {
        actionHandler.pushToSecurityContext(actionMap);
        JPopupMenu popupMenu = createJPopupMenu(tableView, actionMap, actionHandler, locale);
        popupMenu.show(table, evt.getX(), evt.getY());
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
  }

  private void showJTreePopupMenu(JTree tree, IView<JComponent> treeView, MouseEvent evt, IActionHandler actionHandler,
                                  Locale locale) {
    TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());
    if (path == null) {
      return;
    }

    if (!tree.isPathSelected(path)) {
      tree.setSelectionPath(path);
    }
    if (path.getLastPathComponent() instanceof ICollectionConnector) {
      TreePath[] allNodePaths = new TreePath[((ICollectionConnector) path.getLastPathComponent())
          .getChildConnectorCount()];
      for (int i = 0; i < allNodePaths.length; i++) {
        allNodePaths[i] = path.pathByAddingChild(
            ((ICollectionConnector) path.getLastPathComponent()).getChildConnector(i));
      }
      tree.addSelectionPaths(allNodePaths);
    }

    IValueConnector viewConnector = (IValueConnector) path.getLastPathComponent();
    ActionMap actionMap;
    IViewDescriptor viewDescriptor;
    if (viewConnector == tree.getModel().getRoot()) {
      viewDescriptor = treeView.getDescriptor();
    } else {
      viewDescriptor = TreeDescriptorHelper.getSubtreeDescriptorFromPath(
          ((ITreeViewDescriptor) treeView.getDescriptor()).getRootSubtreeDescriptor(),
          getDescriptorPathFromConnectorTreePath(path)).getNodeGroupDescriptor();
      if (!(viewConnector instanceof ICollectionConnector)) {
        viewConnector = viewConnector.getParentConnector();
      }
    }
    actionMap = viewDescriptor.getActionMap();

    if (actionMap != null && actionHandler.isAccessGranted(actionMap)) {
      try {
        actionHandler.pushToSecurityContext(actionMap);
        BasicView<JComponent> treeLevelView = new BasicView<JComponent>(tree);
        treeLevelView.setConnector(viewConnector);
        treeLevelView.setDescriptor(viewDescriptor);
        JPopupMenu popupMenu = createJPopupMenu(treeLevelView, actionMap, actionHandler, locale);
        popupMenu.show(tree, evt.getX(), evt.getY());
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
  }

  private void showPopupMenu(JComponent sourceComponent, IView<JComponent> view, MouseEvent evt,
                             IActionHandler actionHandler, Locale locale) {
    if (sourceComponent instanceof JTree) {
      showJTreePopupMenu((JTree) sourceComponent, view, evt, actionHandler, locale);
    } else if (sourceComponent instanceof JTable) {
      showJTablePopupMenu((JTable) sourceComponent, view, evt, actionHandler, locale);
    }
  }

  private final class ColorTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 6014260077437906330L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
      if (value != null) {
        int[] rgba = ColorHelper.fromHexString((String) value);
        setBackground(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
      } else {
        setBackground(null);
      }
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
  }

  private final class ConnectorTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -5153268751092971328L;
    private final boolean displayIcon;

    public ConnectorTreeCellRenderer(boolean displayIcon) {
      this.displayIcon = displayIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
                                                  int row, boolean nodeHasFocus) {
      JLabel renderer = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
          nodeHasFocus);
      if (value instanceof IValueConnector) {
        if (value instanceof IRenderableCompositeValueConnector) {
          renderer.setText(((IRenderableCompositeValueConnector) value).getDisplayValue());
          if (displayIcon) {
            renderer.setIcon(getIconFactory().getIcon(((IRenderableCompositeValueConnector) value).getDisplayIcon(),
                getIconFactory().getSmallIconSize()));
          }
          String displayDescription = ((IRenderableCompositeValueConnector) value).getDisplayDescription();
          if (displayDescription != null && displayDescription.length() > 0) {
            ToolTipManager.sharedInstance().registerComponent(tree);
            renderer.setToolTipText(displayDescription);
          }
        } else {
          renderer.setText(value.toString());
        }
      }
      return renderer;
    }
  }

  private final class PopupListener extends MouseAdapter {

    private final IActionHandler    actionHandler;
    private final Locale            locale;
    private final JComponent        sourceComponent;
    private final IView<JComponent> view;

    /**
     * Constructs a new {@code PopupListener} instance.
     *
     * @param sourceComponent
     *     the source component.
     * @param view
     *     the view.
     * @param actionHandler
     *     the action handler.
     * @param locale
     *     the locale.
     */
    public PopupListener(JComponent sourceComponent, IView<JComponent> view, IActionHandler actionHandler,
                         Locale locale) {
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

  private final class TranslatedEnumerationListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -5694559709701757582L;
    private final ITranslationProvider                      translationProvider;
    private final Locale                                    locale;
    private final IEnumerationPropertyDescriptor            propertyDescriptor;
    private final org.jspresso.framework.util.gui.Dimension iconDimension;

    /**
     * Constructs a new {@code TranslatedEnumerationCellRenderer} instance.
     *
     * @param propertyDescriptor
     *     the property descriptor from which the enumeration name is
     *     taken. The prefix used to lookup translation keys in the form
     *     keyPrefix.value is the propertyDescriptor enumeration name.
     * @param iconDimension
     *     the icon dimension
     * @param translationProvider
     *     the translation provider.
     * @param locale
     *     the locale to lookup the translation.
     */
    public TranslatedEnumerationListCellRenderer(IEnumerationPropertyDescriptor propertyDescriptor,
                                                 org.jspresso.framework.util.gui.Dimension iconDimension,
                                                 ITranslationProvider translationProvider, Locale locale) {
      this.propertyDescriptor = propertyDescriptor;
      this.iconDimension = iconDimension;
      this.translationProvider = translationProvider;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      label.setIcon(getIconFactory().getIcon(propertyDescriptor.getIconImageURL(String.valueOf(value)), iconDimension));
      if (value != null && propertyDescriptor.isTranslated()) {
        label.setText(propertyDescriptor.getI18nValue((String) value, translationProvider, locale));
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

  private final class TranslatedEnumerationTableCellRenderer extends EvenOddTableCellRenderer {

    private static final long serialVersionUID = -4500472602998482756L;
    private final ITranslationProvider                      translationProvider;
    private final Locale                                    locale;
    private final IEnumerationPropertyDescriptor            propertyDescriptor;
    private final org.jspresso.framework.util.gui.Dimension iconDimension;

    /**
     * Constructs a new {@code TranslatedEnumerationTableCellRenderer}
     * instance.
     *
     * @param propertyDescriptor
     *     the property descriptor from which the enumeration name is
     *     taken. The prefix used to lookup translation keys in the form
     *     keyPrefix.value is the propertyDescriptor enumeration name.
     * @param iconDimension
     *     the icon dimension
     * @param translationProvider
     *     the translation provider.
     * @param locale
     *     the locale to lookup the translation.
     */
    public TranslatedEnumerationTableCellRenderer(IEnumerationPropertyDescriptor propertyDescriptor,
                                                  org.jspresso.framework.util.gui.Dimension iconDimension,
                                                  ITranslationProvider translationProvider, Locale locale) {
      super();
      this.propertyDescriptor = propertyDescriptor;
      this.iconDimension = iconDimension;
      this.translationProvider = translationProvider;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
      setIcon(getIconFactory().getIcon(propertyDescriptor.getIconImageURL(String.valueOf(value)), iconDimension));
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(Object value) {
      if (value instanceof IValueConnector) {
        Object connectorValue = ((IValueConnector) value).getConnectorValue();
        if (connectorValue != null && propertyDescriptor.isTranslated()) {
          super.setValue(propertyDescriptor.getI18nValue((String) connectorValue, translationProvider, locale));
        } else {
          if (connectorValue == null) {
            super.setValue("");
          } else {
            super.setValue(String.valueOf(connectorValue));
          }
        }
      } else {
        if (value != null && propertyDescriptor.isTranslated()) {
          super.setValue(propertyDescriptor.getI18nValue((String) value, translationProvider, locale));
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

  private final class ImageTableCellRenderer extends EvenOddTableCellRenderer {

    private static final long serialVersionUID = 9155173076041284128L;

    @SuppressWarnings("unused")
    private final IPropertyDescriptor propertyDescriptor;

    /**
     * Constructs a new {@code ImageTableCellRenderer} instance.
     *
     * @param propertyDescriptor
     *     the image property descriptor (either image url or image
     *     binary).
     */
    public ImageTableCellRenderer(IPropertyDescriptor propertyDescriptor) {
      this.propertyDescriptor = propertyDescriptor;
      setHorizontalAlignment(SwingConstants.CENTER);
      setVerticalAlignment(SwingConstants.CENTER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
      if (value instanceof byte[]) {
        setIcon(new ImageIcon((byte[]) value));
      } else if (value instanceof String) {
        setIcon(new ImageIcon(UrlHelper.createURL((String) value)));
      } else {
        setIcon(null);
      }
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(Object value) {
      // No string rendering
    }
  }

  private final class BinaryTableCellRenderer extends BooleanTableCellRenderer {

    private static final long serialVersionUID = 9155173076041284128L;

    @SuppressWarnings("unused")
    private final IPropertyDescriptor propertyDescriptor;

    /**
     * Constructs a new {@code BinaryTableCellRenderer} instance.
     *
     * @param propertyDescriptor
     *     the binary property descriptor.
     */
    public BinaryTableCellRenderer(IPropertyDescriptor propertyDescriptor) {
      this.propertyDescriptor = propertyDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(Object value) {
      if (value != null) {
        setSelected(true);
      } else {
        setSelected(false);
      }
    }
  }

  private void configureHorizontalAlignment(JTextField textField, EHorizontalAlignment horizontalAlignment) {
    switch (horizontalAlignment) {
      case LEFT:
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        break;
      case CENTER:
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        break;
      case RIGHT:
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        break;

      default:
        break;
    }
  }

  private void configureHorizontalAlignment(JLabel label, EHorizontalAlignment horizontalAlignment) {
    switch (horizontalAlignment) {
      case LEFT:
        label.setHorizontalAlignment(SwingConstants.LEFT);
        break;
      case CENTER:
        label.setHorizontalAlignment(SwingConstants.CENTER);
        break;
      case RIGHT:
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        break;

      default:
        break;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                 IActionHandler actionHandler, Locale locale) {
    IView<JComponent> propertyView = super.createPropertyView(propertyViewDescriptor, actionHandler, locale);
    if (propertyView.getPeer() instanceof JLabel) {
      configureHorizontalAlignment((JLabel) propertyView.getPeer(), propertyViewDescriptor.getHorizontalAlignment());
    } else if (propertyView.getPeer() instanceof JTextField) {
      configureHorizontalAlignment((JTextField) propertyView.getPeer(),
          propertyViewDescriptor.getHorizontalAlignment());
    }
    return propertyView;
  }

  /**
   * Finds the first focusable component in the hierarchy.
   *
   * @param root
   *     th hierarchy root to explore.
   * @return the first focusable component or null if none.
   */
  protected JComponent findFirstFocusableComponent(Component root) {
    if (root instanceof JTextComponent || root instanceof JCheckBox || root instanceof JComboBox
        || root instanceof JDateField || root instanceof JTable) {

      return (JComponent) root;
    }
    if (root instanceof Container) {
      for (Component child : ((Container) root).getComponents()) {
        JComponent focusableChild = findFirstFocusableComponent(child);
        if (focusableChild != null) {
          return focusableChild;
        }
      }
    }
    return null;
  }

  /**
   * Finds the first editable component in the hierarchy.
   *
   * @param root
   *     th hierarchy root to explore.
   * @return the first editable component or null if none.
   */
  protected JComponent findFirstEditableComponent(Component root) {
    if (root instanceof JTable) {
      return (JComponent) root;
    }
    if (root instanceof Container) {
      for (Component child : ((Container) root).getComponents()) {
        JComponent editableChild = findFirstEditableComponent(child);
        if (editableChild != null) {
          return editableChild;
        }
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void focus(JComponent component) {
    final JComponent focusableChild = findFirstFocusableComponent(component);
    if (focusableChild != null) {
      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          focusableChild.requestFocusInWindow();
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void edit(JComponent component) {
    JComponent editableChild = findFirstEditableComponent(component);
    if (editableChild instanceof JTable) {
      JTable table = (JTable) editableChild;
      if (table.getSelectedRow() >= 0) {
        table.editCellAt(table.getSelectedRow(), 0);
        Component editor = table.getEditorComponent();
        if (editor instanceof JComponent) {
          focus((JComponent) editor);
        }
      }
    }
  }

  /**
   * Configures a property view action by initializing its static context.
   *
   * @param propertyViewDescriptor
   *     the property view descriptor the action is attached to.
   * @param propertyViewAction
   *     the action to configure.
   */
  protected void configurePropertyViewAction(IPropertyViewDescriptor propertyViewDescriptor,
                                             Action propertyViewAction) {
    Map<String, Object> staticContext = new HashMap<>();
    staticContext.put(ActionContextConstants.PROPERTY_VIEW_DESCRIPTOR, propertyViewDescriptor);
    propertyViewAction.putValue(IAction.STATIC_CONTEXT_KEY, staticContext);
  }

  /**
   * Attach char action.
   *
   * @param view
   *     the view
   * @param viewComponent
   *     the view component
   * @param action
   *     the action
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   */
  protected void attachCharAction(final IView<JComponent> view, final JTextComponent viewComponent,
                                  final IDisplayableAction action, final IActionHandler actionHandler, Locale locale) {
    if (action != null) {
      viewComponent.addKeyListener(new KeyAdapter() {

        private void triggerCharAction() {
          String text = viewComponent.getText();
          Map<String, Object> context = getActionFactory().createActionContext(actionHandler, view, view.getConnector(),
              null, view.getPeer());
          context.put(ActionContextConstants.ACTION_COMMAND, text);
          actionHandler.execute(action, context);
        }

        @Override
        public void keyTyped(KeyEvent e) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              triggerCharAction();
            }
          });
        }
      });
    }
  }
}
