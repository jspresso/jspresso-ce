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
package org.jspresso.framework.view.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import javax.swing.ListSelectionModel;
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

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
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
import org.jspresso.framework.binding.swing.JImageConnector;
import org.jspresso.framework.binding.swing.JLabelConnector;
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
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.gui.FontHelper;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.AbstractViewFactory;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.INestingViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.TreeDescriptorHelper;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.TokenMarker;

/**
 * Factory for swing views.
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
 */
public class DefaultSwingViewFactory extends
    AbstractViewFactory<JComponent, Icon, Action> {

  private static final Dimension    TREE_PREFERRED_SIZE = new Dimension(128,
                                                            128);
  private IListSelectionModelBinder listSelectionModelBinder;

  private ITreeSelectionModelBinder treeSelectionModelBinder;

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
   * {@inheritDoc}
   */
  @Override
  protected void adjustSizes(JComponent component, IFormatter formatter,
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computePixelWidth(JComponent component, int characterLength) {
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
  protected IView<JComponent> createBinaryPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IBinaryPropertyDescriptor propertyDescriptor = (IBinaryPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JActionField viewComponent = createJActionField(false);
    JActionFieldConnector connector = new JActionFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    viewComponent.setActions(createBinaryActions(viewComponent, connector,
        propertyDescriptor, actionHandler, locale));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createBooleanPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IBooleanPropertyDescriptor propertyDescriptor = (IBooleanPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JCheckBox viewComponent = createJCheckBox();
    JToggleButtonConnector connector = new JToggleButtonConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createBorderView(
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IMapView<JComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JPanel viewComponent = createJPanel();
    CardLayout layout = new CardLayout();
    viewComponent.setLayout(layout);
    BasicMapView<JComponent> view = constructMapView(viewComponent,
        viewDescriptor);
    Map<String, IView<JComponent>> childrenViews = new HashMap<String, IView<JComponent>>();

    viewComponent.add(createEmptyComponent(), ICardViewDescriptor.DEFAULT_CARD);
    viewComponent.add(createSecurityComponent(),
        ICardViewDescriptor.SECURITY_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<JComponent> childView = createView(childViewDescriptor.getValue(),
          actionHandler, locale);
      viewComponent.add(childView.getPeer(), childViewDescriptor.getKey());
      childrenViews.put(childViewDescriptor.getKey(), childView);
    }
    view.setChildrenMap(childrenViews);
    view.setConnector(createCardViewConnector(view, actionHandler));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createColorPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IColorPropertyDescriptor propertyDescriptor = (IColorPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
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
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = getConnectorFactory()
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
    // boolean lastRowNeedsFilling = true;

    for (Iterator<IPropertyViewDescriptor> ite = viewDescriptor
        .getPropertyViewDescriptors().iterator(); ite.hasNext();) {
      IPropertyViewDescriptor propertyViewDescriptor = ite.next();
      String propertyName = propertyViewDescriptor.getName();
      IPropertyDescriptor propertyDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor
          .getModelDescriptor()).getComponentDescriptor()
          .getPropertyDescriptor(propertyName);
      if (propertyDescriptor == null) {
        throw new ViewException("Property descriptor [" + propertyName
            + "] does not exist for model descriptor "
            + viewDescriptor.getModelDescriptor().getName() + ".");
      }
      IView<JComponent> propertyView = createPropertyView(
          propertyViewDescriptor, actionHandler, locale);
      boolean forbidden = false;
      try {
        actionHandler.checkAccess(propertyViewDescriptor);
      } catch (SecurityException ex) {
        forbidden = true;
        propertyView.setPeer(createSecurityComponent());
      }
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
      if (forbidden) {
        propertyLabel.setText(" ");
      }

      int propertyWidth = propertyViewDescriptor.getWidth();
      if (propertyWidth > viewDescriptor.getColumnCount()) {
        propertyWidth = viewDescriptor.getColumnCount();
      }
      if (currentX + propertyWidth > viewDescriptor.getColumnCount()) {
        currentX = 0;
        currentY++;
      }
      // if (currentX + propertyWidth > viewDescriptor.getColumnCount()) {
      // fillLastRow(viewComponent);
      // currentX = 0;
      // currentY++;
      // }

      // label positionning
      GridBagConstraints constraints = new GridBagConstraints();
      switch (viewDescriptor.getLabelsPosition()) {
        case ASIDE:
          constraints.insets = new Insets(5, 5, 5, 5);
          if (isHeightExtensible(propertyViewDescriptor)) {
            constraints.anchor = GridBagConstraints.NORTHEAST;
          } else {
            constraints.anchor = GridBagConstraints.EAST;
          }
          constraints.gridx = currentX * 2;
          constraints.gridy = currentY;
          break;
        case ABOVE:
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
        case ASIDE:
          constraints.gridx++;
          constraints.insets = new Insets(5, 0, 5, 5);
          constraints.gridwidth = propertyWidth * 2 - 1;
          break;
        case ABOVE:
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
      if (isHeightExtensible(propertyViewDescriptor)) {
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        isSpaceFilled = true;
        // if (!ite.hasNext()) {
        // constraints.gridwidth = GridBagConstraints.REMAINDER;
        // lastRowNeedsFilling = false;
        // }
      } else {
        constraints.fill = GridBagConstraints.NONE;
      }
      viewComponent.add(propertyView.getPeer(), constraints);

      currentX += propertyWidth;
    }
    // if (lastRowNeedsFilling) {
    // fillLastRow(viewComponent);
    // }
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
        default:
          break;
      }
      viewComponent.add(filler, constraints);
    }
    return view;
  }

  // private void fillLastRow(JPanel viewComponent) {
  // GridBagConstraints constraints = new GridBagConstraints();
  // constraints.gridx = GridBagConstraints.RELATIVE;
  // constraints.weightx = 1.0;
  // constraints.fill = GridBagConstraints.HORIZONTAL;
  // constraints.gridwidth = GridBagConstraints.REMAINDER;
  // JPanel filler = createJPanel();
  // // filler.setBorder(new SLineBorder(Color.BLUE));
  // viewComponent.add(filler, constraints);
  // }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createConstrainedGridView(
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
              .getCellConstraints(childViewDescriptor)));
      childrenViews.add(childView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createDatePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDatePropertyDescriptor propertyDescriptor = (IDatePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
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
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createDecimalPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDecimalPropertyDescriptor propertyDescriptor = (IDecimalPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDecimalTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createDurationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDurationPropertyDescriptor propertyDescriptor = (IDurationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDurationTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
  protected IView<JComponent> createEnumerationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IEnumerationPropertyDescriptor propertyDescriptor = (IEnumerationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
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
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JPanel viewComponent = createJPanel();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<IView<JComponent>>();

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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createIntegerPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IIntegerPropertyDescriptor propertyDescriptor = (IIntegerPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createJLabel();
      connector = new JLabelConnector(propertyDescriptor.getName(),
          (JLabel) viewComponent);
      ((JLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createJTextField();
      connector = new JFormattedFieldConnector(propertyDescriptor.getName(),
          (JTextField) viewComponent, formatter);
    }
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getIntegerTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
   *          the user locale.
   * @return the created date field.
   */
  protected JDateField createJDateField(Locale locale) {
    JDateField dateField = new JDateField(locale);
    return dateField;
  }

  /**
   * Creates a JEdit text area.
   * 
   * @param language
   *          the language to add syntax highlighting for.
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
    // To have preferred height computed.
    return new JLabel(" ");
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
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createListView(
      IListViewDescriptor viewDescriptor,
      @SuppressWarnings("unused") IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
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
    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), null);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            viewDescriptor.getModelDescriptor().getName(), null);

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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createPasswordPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IPasswordPropertyDescriptor propertyDescriptor = (IPasswordPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JPasswordField viewComponent = createJPasswordField();
    JPasswordFieldConnector connector = new JPasswordFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createPercentPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IPercentPropertyDescriptor propertyDescriptor = (IPercentPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    JPercentFieldConnector connector = new JPercentFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getPercentTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createReferencePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IReferencePropertyDescriptor<?> propertyDescriptor = (IReferencePropertyDescriptor<?>) propertyViewDescriptor
        .getModelDescriptor();
    JActionField viewComponent = createJActionField(true);
    JReferenceFieldConnector connector = new JReferenceFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setToStringPropertyConnector(new BasicValueConnector(
        propertyDescriptor.getComponentDescriptor().getToStringProperty()));
    connector.setExceptionHandler(actionHandler);
    Action lovAction = createLovAction(viewComponent, connector,
        propertyDescriptor, actionHandler, locale);
    lovAction.putValue(Action.NAME, getTranslationProvider().getTranslation(
        "lov.element.name",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            getTranslationProvider(), locale)}, locale));
    lovAction.putValue(Action.SHORT_DESCRIPTION, getTranslationProvider()
        .getTranslation(
            "lov.element.description",
            new Object[] {propertyDescriptor.getReferencedDescriptor()
                .getI18nName(getTranslationProvider(), locale)}, locale)
        + TOOLTIP_ELLIPSIS);
    if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
      lovAction.putValue(Action.SMALL_ICON, getIconFactory().getIcon(
          propertyDescriptor.getReferencedDescriptor().getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }
    viewComponent.setActions(Collections.singletonList(lovAction));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
    // JLabel label = createJLabel();
    // label.setHorizontalAlignment(SwingConstants.CENTER);
    // label.setVerticalAlignment(SwingConstants.CENTER);
    // label.setIcon(iconFactory.getForbiddenIcon(IIconFactory.LARGE_ICON_SIZE));
    // panel.add(label, BorderLayout.CENTER);
    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createSourceCodePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ISourceCodePropertyDescriptor propertyDescriptor = (ISourceCodePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JEditTextArea viewComponent = createJEditTextArea(propertyDescriptor
        .getLanguage());
    JEditTextAreaConnector connector = new JEditTextAreaConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    JSplitPane viewComponent = createJSplitPane();
    BasicCompositeView<JComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<JComponent>> childrenViews = new ArrayList<IView<JComponent>>();

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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createStringPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createJLabel();
      connector = new JLabelConnector(propertyDescriptor.getName(),
          (JLabel) viewComponent);
    } else {
      viewComponent = createJTextField();
      connector = new JTextFieldConnector(propertyDescriptor.getName(),
          (JTextField) viewComponent);
    }
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * Creates a table cell renderer for a given property descriptor.
   * 
   * @param propertyDescriptor
   *          the property descriptor to create the renderer for.
   * @param locale
   *          the locale.
   * @return the created table cell renderer.
   */
  protected TableCellRenderer createTableCellRenderer(
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createTableView(
      ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(
            modelDescriptor.getName() + "Element",
            modelDescriptor.getCollectionDescriptor().getElementDescriptor()
                .getToStringProperty());
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
            rowConnectorPrototype);
    JTable viewComponent = createJTable();
    JScrollPane scrollPane = createJScrollPane();
    scrollPane.setViewportView(viewComponent);
    JLabel iconLabel = createJLabel();
    iconLabel.setIcon(getIconFactory().getIcon(
        modelDescriptor.getCollectionDescriptor().getElementDescriptor()
            .getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    iconLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER, iconLabel);
    IView<JComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    Map<String, Class<?>> columnClassesByIds = new HashMap<String, Class<?>>();
    List<String> columnConnectorKeys = new ArrayList<String>();
    Set<String> forbiddenColumns = new HashSet<String>();
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String columnId = columnViewDescriptor.getName();
      try {
        actionHandler.checkAccess(columnViewDescriptor);
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
      } catch (SecurityException ex) {
        // The column simply won't be added.
        forbiddenColumns.add(columnId);
      }
    }
    CollectionConnectorTableModel tableModel = new CollectionConnectorTableModel(
        connector, columnConnectorKeys);
    tableModel.setExceptionHandler(actionHandler);
    tableModel.setColumnClassesByIds(columnClassesByIds);
    TableSorter sorterDecorator = new TableSorter(tableModel, viewComponent
        .getTableHeader());
    Dimension iconSize = new Dimension(viewComponent.getTableHeader().getFont()
        .getSize(), viewComponent.getTableHeader().getFont().getSize());
    sorterDecorator
        .setUpIcon(getIconFactory()
            .getIcon(
                "classpath:org/jspresso/framework/application/images/1uparrow-48x48.png",
                iconSize));
    sorterDecorator
        .setDownIcon(getIconFactory()
            .getIcon(
                "classpath:org/jspresso/framework/application/images/1downarrow-48x48.png",
                iconSize));
    sorterDecorator.setColumnComparator(String.class,
        String.CASE_INSENSITIVE_ORDER);
    viewComponent.setModel(sorterDecorator);
    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), sorterDecorator);
    int maxColumnSize = computePixelWidth(viewComponent,
        getMaxColumnCharacterLength());
    int columnIndex = 0;
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String propertyName = columnViewDescriptor.getName();
      if (!forbiddenColumns.contains(propertyName)) {
        TableColumn column = viewComponent.getColumnModel().getColumn(
            columnIndex++);
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

        IView<JComponent> editorView = createPropertyView(columnViewDescriptor,
            actionHandler, locale);
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
        int minHeaderWidth = computePixelWidth(viewComponent, columnName
            .length());
        if (propertyDescriptor instanceof IBooleanPropertyDescriptor
            || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
          column.setPreferredWidth(Math.max(
              computePixelWidth(viewComponent, 2), minHeaderWidth));
        } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
          column.setPreferredWidth(Math.max(computePixelWidth(viewComponent,
              getEnumerationTemplateValue(
                  (IEnumerationPropertyDescriptor) propertyDescriptor, locale)
                  .length() + 4), minHeaderWidth));
        } else {
          column.setPreferredWidth(Math.max(Math.min(computePixelWidth(
              viewComponent, getFormatLength(createFormatter(
                  propertyDescriptor, locale),
                  getTemplateValue(propertyDescriptor))), maxColumnSize),
              minHeaderWidth));
        }
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<JComponent> createTabView(
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
      Icon childIcon = getIconFactory().getIcon(
          childViewDescriptor.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE);
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createTextPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    JScrollPane scrollPane = createJScrollPane();
    if (propertyViewDescriptor.isReadOnly()) {
      JLabel viewComponent = createJLabel();
      viewComponent.setVerticalAlignment(SwingConstants.TOP);
      viewComponent.setHorizontalAlignment(SwingConstants.LEADING);
      connector = new JLabelConnector(propertyDescriptor.getName(),
          viewComponent);
      ((JLabelConnector) connector).setMultiLine(true);
      scrollPane.setViewportView(viewComponent);
    } else {
      JTextArea viewComponent = createJTextArea();
      viewComponent.setLineWrap(true);
      scrollPane.setViewportView(viewComponent);
      scrollPane
          .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      connector = new JTextAreaConnector(propertyDescriptor.getName(),
          viewComponent);
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(scrollPane, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createTimePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ITimePropertyDescriptor propertyDescriptor = (ITimePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    JTextField viewComponent = createJTextField();
    IFormatter formatter = createTimeFormatter(propertyDescriptor, locale);
    JFormattedFieldConnector connector = new JFormattedFieldConnector(
        propertyDescriptor.getName(), viewComponent, formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getTimeTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<JComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = createTreeViewConnector(
        viewDescriptor, locale);

    JTree viewComponent = createJTree();
    ConnectorHierarchyTreeModel treeModel = new ConnectorHierarchyTreeModel(
        connector);
    viewComponent.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    viewComponent.setModel(treeModel);
    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer());
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithActions(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale, IView<JComponent> view) {
    if (viewDescriptor.getActionMap() != null) {
      JToolBar toolBar = createJToolBar();
      for (Iterator<ActionList> iter = viewDescriptor.getActionMap()
          .getActionLists().iterator(); iter.hasNext();) {
        ActionList nextActionList = iter.next();
        for (IDisplayableAction action : nextActionList.getActions()) {
          Action swingAction = getActionFactory().createAction(action,
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
  }

  /**
   * Decorates the created view with the apropriate border.
   * 
   * @param view
   *          the view to descorate.
   * @param locale
   *          the locale to use.
   */
  @Override
  protected void decorateWithBorder(IView<JComponent> view, Locale locale) {
    switch (view.getDescriptor().getBorderType()) {
      case SIMPLE:
        view.getPeer().setBorder(BorderFactory.createEtchedBorder());
        break;
      case TITLED:
        decorateWithTitle(view, locale);
        break;
      default:
        break;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithDescription(
      IPropertyDescriptor propertyDescriptor, Locale locale,
      IView<JComponent> view) {
    if (view != null && propertyDescriptor.getDescription() != null) {
      view.getPeer().setToolTipText(
          propertyDescriptor.getI18nDescription(getTranslationProvider(),
              locale)
              + TOOLTIP_ELLIPSIS);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finishComponentConfiguration(IViewDescriptor viewDescriptor,
      Locale locale, IView<JComponent> view) {
    if (viewDescriptor.getForeground() != null) {
      view.getPeer().setForeground(createColor(viewDescriptor.getForeground()));
    }
    if (viewDescriptor.getBackground() != null) {
      view.getPeer().setBackground(createColor(viewDescriptor.getBackground()));
    }
    if (viewDescriptor.getFont() != null) {
      view.getPeer().setFont(createFont(viewDescriptor.getFont()));
    }
    if (viewDescriptor.getDescription() != null) {
      view.getPeer().setToolTipText(
          viewDescriptor.getI18nDescription(getTranslationProvider(), locale)
              + TOOLTIP_ELLIPSIS);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void showCardInPanel(JComponent cardsPeer, String cardName) {
    ((CardLayout) cardsPeer.getLayout()).show(cardsPeer, cardName);
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

  private Color createColor(String colorAsHexString) {
    int[] rgba = ColorHelper.fromHexString(colorAsHexString);
    return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
  }

  private TableCellRenderer createColorTableCellRenderer(
      @SuppressWarnings("unused") IColorPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new ColorTableCellRenderer();
  }

  private TableCellRenderer createDateTableCellRenderer(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDateFormatter(
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

  private Font createFont(String fontString) {
    org.jspresso.framework.util.gui.Font font = FontHelper
        .fromString(fontString);
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
    return new Font(font.getName(), fontStyle, font.getSize());
  }

  private GridBagConstraints createGridBagConstraints(
      CellConstraints viewConstraints) {
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
    titleLabel.setIcon(getIconFactory().getIcon(
        viewDescriptor.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    popupMenu.add(titleLabel);
    popupMenu.addSeparator();
    for (Iterator<ActionList> iter = actionMap.getActionLists().iterator(); iter
        .hasNext();) {
      ActionList nextActionSet = iter.next();
      for (IDisplayableAction action : nextActionSet.getActions()) {
        Action swingAction = getActionFactory().createAction(action,
            actionHandler, sourceComponent, modelDescriptor, viewConnector,
            locale);
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

  private TableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused") IReferencePropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return null;
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

  private TableCellRenderer createStringTableCellRenderer(
      @SuppressWarnings("unused") IStringPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
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

  private TableCellRenderer createTimeTableCellRenderer(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createTimeFormatter(
        propertyDescriptor, locale));
  }

  private void decorateWithTitle(IView<JComponent> view, Locale locale) {
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

  private int getSelectionMode(ICollectionViewDescriptor viewDescriptor) {
    int selectionMode;
    switch (viewDescriptor.getSelectionMode()) {
      case SINGLE_SELECTION:
        selectionMode = ListSelectionModel.SINGLE_SELECTION;
        break;
      case SINGLE_INTERVAL_SELECTION:
        selectionMode = ListSelectionModel.SINGLE_INTERVAL_SELECTION;
        break;
      default:
        selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
        break;
    }
    return selectionMode;
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

    private static final long serialVersionUID = -5153268751092971328L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row,
        boolean nodeHasFocus) {
      JLabel renderer = (JLabel) super.getTreeCellRendererComponent(tree,
          value, sel, expanded, leaf, row, nodeHasFocus);
      if (value instanceof IValueConnector) {
        if (value instanceof IRenderableCompositeValueConnector) {
          renderer.setText(((IRenderableCompositeValueConnector) value)
              .getDisplayValue());
          renderer.setIcon(getIconFactory().getIcon(
              ((IRenderableCompositeValueConnector) value)
                  .getDisplayIconImageUrl(), IIconFactory.SMALL_ICON_SIZE));
          if (((IRenderableCompositeValueConnector) value)
              .getDisplayDescription() != null) {
            ToolTipManager.sharedInstance().registerComponent(tree);
            renderer
                .setToolTipText(((IRenderableCompositeValueConnector) value)
                    .getDisplayDescription()
                    + TOOLTIP_ELLIPSIS);
          }
        } else {
          renderer.setText(value.toString());
        }
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
     * Constructs a new <code>TranslatedEnumerationCellRenderer</code> instance.
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
      label.setIcon(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(String.valueOf(value)),
          IIconFactory.TINY_ICON_SIZE));
      if (value != null && propertyDescriptor.isTranslated()) {
        label.setText(getTranslationProvider().getTranslation(
            computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                value), locale));
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
      setIcon(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(String.valueOf(value)),
          IIconFactory.TINY_ICON_SIZE));
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
          super.setValue(getTranslationProvider().getTranslation(
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
          super.setValue(getTranslationProvider().getTranslation(
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
