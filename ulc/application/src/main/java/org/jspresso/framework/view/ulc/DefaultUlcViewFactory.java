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
package org.jspresso.framework.view.ulc;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.binding.ulc.CollectionConnectorListModel;
import org.jspresso.framework.binding.ulc.CollectionConnectorTableModel;
import org.jspresso.framework.binding.ulc.ConnectorHierarchyTreeModel;
import org.jspresso.framework.binding.ulc.IListSelectionModelBinder;
import org.jspresso.framework.binding.ulc.ITreeSelectionModelBinder;
import org.jspresso.framework.binding.ulc.ULCActionFieldConnector;
import org.jspresso.framework.binding.ulc.ULCColorPickerConnector;
import org.jspresso.framework.binding.ulc.ULCComboBoxConnector;
import org.jspresso.framework.binding.ulc.ULCDateFieldConnector;
import org.jspresso.framework.binding.ulc.ULCImageConnector;
import org.jspresso.framework.binding.ulc.ULCJEditTextAreaConnector;
import org.jspresso.framework.binding.ulc.ULCLabelConnector;
import org.jspresso.framework.binding.ulc.ULCPasswordFieldConnector;
import org.jspresso.framework.binding.ulc.ULCReferenceFieldConnector;
import org.jspresso.framework.binding.ulc.ULCTextAreaConnector;
import org.jspresso.framework.binding.ulc.ULCTextFieldConnector;
import org.jspresso.framework.binding.ulc.ULCToggleButtonConnector;
import org.jspresso.framework.gui.ulc.components.server.ITreePathPopupFactory;
import org.jspresso.framework.gui.ulc.components.server.ULCActionField;
import org.jspresso.framework.gui.ulc.components.server.ULCColorPicker;
import org.jspresso.framework.gui.ulc.components.server.ULCDateField;
import org.jspresso.framework.gui.ulc.components.server.ULCDurationDataType;
import org.jspresso.framework.gui.ulc.components.server.ULCDurationDataTypeFactory;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedButton;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedScrollPane;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedTable;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedTree;
import org.jspresso.framework.gui.ulc.components.server.ULCJEditTextArea;
import org.jspresso.framework.gui.ulc.components.server.ULCOnFocusSelectTextField;
import org.jspresso.framework.gui.ulc.components.server.ULCTranslationDataTypeFactory;
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
import org.jspresso.framework.util.ulc.UlcUtil;
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
import com.ulcjava.base.application.ULCListSelectionModel;
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
import com.ulcjava.base.application.table.DefaultTableCellRenderer;
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
public class DefaultUlcViewFactory extends
    AbstractViewFactory<ULCComponent, ULCIcon, IAction> {

  private static final Dimension        TREE_PREFERRED_SIZE        = new Dimension(
                                                                       128, 128);
  private ULCDurationDataTypeFactory    durationDataTypeFactory    = new ULCDurationDataTypeFactory();
  private IListSelectionModelBinder     listSelectionModelBinder;

  private ULCTranslationDataTypeFactory translationDataTypeFactory = new ULCTranslationDataTypeFactory();

  private ITreeSelectionModelBinder     treeSelectionModelBinder;

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
  protected void adjustSizes(ULCComponent component, IFormatter formatter,
      Object templateValue, int extraWidth) {
    int preferredWidth = computePixelWidth(component, getFormatLength(
        formatter, templateValue))
        + extraWidth;
    Dimension size = new Dimension(preferredWidth, component.getFont()
        .getSize() + 6);
    component.setMinimumSize(size);
    component.setPreferredSize(size);
    component.setMaximumSize(size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computePixelWidth(ULCComponent component, int characterLength) {
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
  protected IView<ULCComponent> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCActionField viewComponent = createULCActionField(false);
    ULCActionFieldConnector connector = new ULCActionFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    viewComponent.setActions(createBinaryActions(viewComponent, connector,
        propertyDescriptor, actionHandler, locale));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createBooleanPropertyView(
      IBooleanPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ULCCheckBox viewComponent = createULCCheckBox();
    ULCToggleButtonConnector connector = new ULCToggleButtonConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
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
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<ULCComponent> createBorderView(
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
   * {@inheritDoc}
   */
  @Override
  protected IMapView<ULCComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCCardPane viewComponent = createCardPane();
    BasicMapView<ULCComponent> view = constructMapView(viewComponent,
        viewDescriptor);
    Map<String, IView<ULCComponent>> childrenViews = new HashMap<String, IView<ULCComponent>>();

    viewComponent.add(createEmptyComponent(), ICardViewDescriptor.DEFAULT_CARD);
    viewComponent.add(createSecurityComponent(),
        ICardViewDescriptor.SECURITY_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<ULCComponent> childView = createView(
          childViewDescriptor.getValue(), actionHandler, locale);
      viewComponent.addCard(childViewDescriptor.getKey(), childView.getPeer());
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
  protected IView<ULCComponent> createColorPropertyView(
      IColorPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ULCColorPicker viewComponent = createULCColorPicker();
    if (propertyDescriptor.getDefaultValue() != null) {
      int[] rgba = ColorHelper.fromHexString((String) propertyDescriptor
          .getDefaultValue());
      viewComponent
          .setResetValue(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
    }
    ULCColorPickerConnector connector = new ULCColorPickerConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            getConnectorIdForComponentView(viewDescriptor), null);
    ULCGridBagLayoutPane viewComponent = createGridBagLayoutPane();
    IView<ULCComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    int currentX = 0;
    int currentY = 0;

    boolean isSpaceFilled = false;

    for (IPropertyViewDescriptor propertyViewDescriptor : viewDescriptor
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
      IView<ULCComponent> propertyView = createPropertyView(propertyDescriptor,
          propertyViewDescriptor.getRenderedChildProperties(), actionHandler,
          locale);
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
      ULCLabel propertyLabel = createPropertyLabel(propertyDescriptor,
          propertyView.getPeer(), locale);
      if (forbidden) {
        propertyLabel.setText(" ");
      }

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
        case ASIDE:
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
        case ABOVE:
          constraints.setInsets(new Insets(5, 5, 0, 5));
          constraints.setAnchor(GridBagConstraints.SOUTHWEST);
          constraints.setGridX(currentX);
          constraints.setGridY(currentY * 2);
          constraints.setGridWidth(propertyWidth);
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
          constraints.setGridX(constraints.getGridX() + 1);
          constraints.setInsets(new Insets(5, 0, 5, 5));
          constraints.setGridWidth(propertyWidth * 2 - 1);
          break;
        case ABOVE:
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
      if (propertyView.getPeer() instanceof ULCCheckBox) {
        constraints.setWeightX(ClientContext.getScreenResolution());
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
        case ASIDE:
          constraints.setGridY(currentY + 1);
          constraints.setGridWidth(viewDescriptor.getColumnCount() * 2);
          break;
        case ABOVE:
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<ULCComponent> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCGridBagLayoutPane viewComponent = createGridBagLayoutPane();
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<ULCComponent> childView = createView(childViewDescriptor,
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
  protected IView<ULCComponent> createDatePropertyView(
      IDatePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {

    SimpleDateFormat format = createDateFormat(propertyDescriptor, locale);

    ULCDateField viewComponent = createULCDateField(format.toPattern(), locale);
    ULCDateFieldConnector connector = new ULCDateFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, createFormatter(format),
        getDateTemplateValue(propertyDescriptor), ClientContext
            .getScreenResolution() / 3);
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createDecimalPropertyView(
      IDecimalPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(
          (IPercentPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    ULCTextField viewComponent = createULCTextField();

    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setFormatter(formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDecimalTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createDurationPropertyView(
      IDurationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCTextField viewComponent = createULCTextField();
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);

    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setFormatter(formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getDurationTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ULCComponent createEmptyComponent() {
    return createBorderLayoutPane();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCComboBox viewComponent = createULCComboBox();
    if (!propertyDescriptor.isMandatory()) {
      viewComponent.addItem(null);
    }
    for (String enumElement : propertyDescriptor.getEnumerationValues()) {
      viewComponent.addItem(enumElement);
    }
    viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(
        propertyDescriptor, locale));
    adjustSizes(viewComponent, null, getEnumerationTemplateValue(
        propertyDescriptor, locale),
        ClientContext.getScreenResolution() * 2 / 6);
    ULCComboBoxConnector connector = new ULCComboBoxConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<ULCComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCGridLayoutPane viewComponent = createGridLayoutPane();
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    switch (viewDescriptor.getDrivingDimension()) {
      case ROW:
        viewComponent.setColumns(viewDescriptor.getDrivingDimensionCellCount());
        viewComponent.setRows(0);
        break;
      case COLUMN:
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

  /**
   * Creates a gridbaglayout pane.
   * 
   * @return the created gridbaglayout pane.
   */
  protected ULCGridBagLayoutPane createGridBagLayoutPane() {
    ULCGridBagLayoutPane pane = new ULCGridBagLayoutPane();
    return pane;
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
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createIntegerPropertyView(
      IIntegerPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCTextField viewComponent = createULCTextField();

    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setFormatter(formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getIntegerTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createListView(
      IListViewDescriptor viewDescriptor,
      @SuppressWarnings("unused") IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = (ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor();
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
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
    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), null);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            viewDescriptor.getModelDescriptor().getName(), null);
    ULCBorderLayoutPane viewComponent = createBorderLayoutPane();
    IView<ULCComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    IView<ULCComponent> nestedView = createView(viewDescriptor
        .getNestedViewDescriptor(), actionHandler, locale);
    connector.addChildConnector(nestedView.getConnector());
    viewComponent.add(nestedView.getPeer(), ULCBorderLayoutPane.CENTER);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createPasswordPropertyView(
      IPasswordPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ULCPasswordField viewComponent = createULCPasswordField();
    ULCPasswordFieldConnector connector = new ULCPasswordFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createPercentPropertyView(
      IPercentPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCTextField viewComponent = createULCTextField();

    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setFormatter(formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getPercentTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createReferencePropertyView(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ULCActionField viewComponent = createULCActionField(true);
    ULCReferenceFieldConnector connector = new ULCReferenceFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setToStringPropertyConnector(new BasicValueConnector(
        propertyDescriptor.getComponentDescriptor().getToStringProperty()));
    connector.setExceptionHandler(actionHandler);
    IAction lovAction = createLovAction(viewComponent, connector,
        propertyDescriptor, actionHandler, locale);
    lovAction.putValue(IAction.NAME, getTranslationProvider().getTranslation(
        "lov.element.name",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            getTranslationProvider(), locale)}, locale));
    lovAction.putValue(IAction.SHORT_DESCRIPTION, getTranslationProvider()
        .getTranslation(
            "lov.element.description",
            new Object[] {propertyDescriptor.getReferencedDescriptor()
                .getI18nName(getTranslationProvider(), locale)}, locale)
        + TOOLTIP_ELLIPSIS);
    if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
      lovAction.putValue(IAction.SMALL_ICON, getIconFactory().getIcon(
          propertyDescriptor.getReferencedDescriptor().getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }
    viewComponent.setActions(Collections.singletonList(lovAction));
    adjustSizes(viewComponent, null, null);
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ULCBorderLayoutPane createSecurityComponent() {
    ULCBorderLayoutPane panel = new ULCBorderLayoutPane();
    // ULCLabel label = createULCLabel();
    // label.setIcon(iconFactory.getForbiddenIcon(IIconFactory.LARGE_ICON_SIZE));
    // label.setHorizontalAlignment(IDefaults.CENTER);
    // label.setVerticalAlignment(IDefaults.CENTER);
    // panel.add(label, ULCBorderLayoutPane.CENTER);
    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createSourceCodePropertyView(
      ISourceCodePropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ULCJEditTextArea viewComponent = createULCJEditTextArea(propertyDescriptor
        .getLanguage());
    ULCJEditTextAreaConnector connector = new ULCJEditTextAreaConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<ULCComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCSplitPane viewComponent = createULCSplitPane();
    BasicCompositeView<ULCComponent> view = constructCompositeView(
        viewComponent, viewDescriptor);
    List<IView<ULCComponent>> childrenViews = new ArrayList<IView<ULCComponent>>();

    switch (viewDescriptor.getOrientation()) {
      case HORIZONTAL:
        viewComponent.setOrientation(ULCSplitPane.HORIZONTAL_SPLIT);
        break;
      case VERTICAL:
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
  protected IView<ULCComponent> createStringPropertyView(
      IStringPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ULCComponent viewComponent;
    IValueConnector connector;
    if (propertyDescriptor.isReadOnly()) {
      viewComponent = createULCLabel();
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
    } else {
      viewComponent = createULCTextField();
      connector = new ULCTextFieldConnector(propertyDescriptor.getName(),
          (ULCTextField) viewComponent);
    }
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, null, getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  /**
   * Creates a table cell renderer for a given property descriptor.
   * 
   * @param column
   *          the table column index.
   * @param propertyDescriptor
   *          the property descriptor to create the renderer for.
   * @param locale
   *          the locale.
   * @return the created table cell renderer.
   */
  protected ITableCellRenderer createTableCellRenderer(int column,
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
  protected IView<ULCComponent> createTableView(
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
    ULCExtendedTable viewComponent = createULCTable();
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    ULCLabel iconLabel = createULCLabel();
    iconLabel.setIcon(getIconFactory().getIcon(
        modelDescriptor.getCollectionDescriptor().getElementDescriptor()
            .getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    iconLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setCorner(ULCScrollPane.UPPER_RIGHT_CORNER, iconLabel);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent
        .setAutoResizeMode(com.ulcjava.base.application.ULCTable.AUTO_RESIZE_OFF);

    Map<String, Class<?>> columnClassesByIds = new HashMap<String, Class<?>>();
    Map<String, IFormatter> columnFormattersByIds = new HashMap<String, IFormatter>();
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
        IPropertyDescriptor columnModelDescriptor = modelDescriptor
            .getCollectionDescriptor().getElementDescriptor()
            .getPropertyDescriptor(columnId);
        if (columnModelDescriptor instanceof IReferencePropertyDescriptor) {
          columnClassesByIds.put(columnId, String.class);
        } else if (columnModelDescriptor instanceof IBooleanPropertyDescriptor) {
          columnClassesByIds.put(columnId, Boolean.class);
        } else {
          if (columnModelDescriptor instanceof IDecimalPropertyDescriptor
              && ((IDecimalPropertyDescriptor) columnModelDescriptor)
                  .isUsingBigDecimal()) {
            columnClassesByIds.put(columnId, String.class);
            columnFormattersByIds.put(columnId, createFormatter(
                columnModelDescriptor, locale));
          } else {
            columnClassesByIds.put(columnId, columnModelDescriptor
                .getModelType());
          }
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
      } catch (SecurityException ex) {
        // The column simply won't be added.
        forbiddenColumns.add(columnId);
      }
    }
    CollectionConnectorTableModel tableModel = new CollectionConnectorTableModel(
        connector, columnConnectorKeys);
    tableModel.setExceptionHandler(actionHandler);
    tableModel.setColumnClassesByIds(columnClassesByIds);
    tableModel.setColumnFormattersByIds(columnFormattersByIds);

    TableSorter sorterDecorator = new TableSorter(tableModel, viewComponent
        .getTableHeader());
    java.awt.Dimension iconSize = new java.awt.Dimension(viewComponent
        .getTableHeader().getFont().getSize(), viewComponent.getTableHeader()
        .getFont().getSize());
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
    ClientContext.setModelUpdateMode(sorterDecorator,
        IUlcEventConstants.ASYNCHRONOUS_MODE);
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
        ULCTableColumn column = viewComponent.getColumnModel().getColumn(
            columnIndex++);
        column.setHeaderRenderer(null);
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
        int minHeaderWidth = computePixelWidth(viewComponent, columnName
            .length());
        if (propertyDescriptor instanceof IBooleanPropertyDescriptor
            || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
          column.setPreferredWidth(Math.max(
              computePixelWidth(viewComponent, 2), minHeaderWidth));
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
    viewComponent.setComponentPopupMenu(createPopupMenu(viewComponent, view,
        actionHandler, locale));
    int minimumWidth = 0;
    for (int i = 0; i < 1
        && i < viewComponent.getColumnModel().getColumnCount(); i++) {
      minimumWidth += viewComponent.getColumnModel().getColumn(i)
          .getPreferredWidth();
    }
    scrollPane.setMinimumSize(new Dimension(minimumWidth, viewComponent
        .getRowHeight() * 7));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<ULCComponent> createTabView(
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
      ULCIcon childIcon = getIconFactory().getIcon(
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
  protected IView<ULCComponent> createTextPropertyView(
      ITextPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    IValueConnector connector;
    ULCScrollPane scrollPane = createULCScrollPane();
    if (propertyDescriptor.isReadOnly()) {
      ULCLabel viewComponent = createULCLabel();
      viewComponent.setVerticalAlignment(IDefaults.TOP);
      viewComponent.setHorizontalAlignment(IDefaults.LEADING);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          viewComponent);
      ((ULCLabelConnector) connector).setMultiLine(true);
      scrollPane.setViewPortView(viewComponent);
    } else {
      ULCTextArea viewComponent = createULCTextArea();
      viewComponent.setLineWrap(true);
      scrollPane.setViewPortView(viewComponent);
      scrollPane
          .setHorizontalScrollBarPolicy(ULCScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      connector = new ULCTextAreaConnector(propertyDescriptor.getName(),
          viewComponent);
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(scrollPane, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createTimePropertyView(
      ITimePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCTextField viewComponent = createULCTextField();

    IFormatter formatter = createTimeFormatter(propertyDescriptor, locale);
    ULCTextFieldConnector connector = new ULCTextFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setFormatter(formatter);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(viewComponent, formatter,
        getTimeTemplateValue(propertyDescriptor));
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = createTreeViewConnector(
        viewDescriptor, locale);

    ULCExtendedTree viewComponent = createULCTree();
    ConnectorHierarchyTreeModel treeModel = new ConnectorHierarchyTreeModel(
        connector);

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
    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer());
    treeSelectionModelBinder.bindSelectionModel(connector, viewComponent);
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent.setPopupFactory(new TreeNodePopupFactory(viewComponent, view,
        actionHandler, locale));
    scrollPane.setMinimumSize(TREE_PREFERRED_SIZE);
    return view;
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
   * Creates a button.
   * 
   * @return the created button.
   */
  protected ULCButton createULCButton() {
    ULCButton ulcButton = new ULCExtendedButton();
    return ulcButton;
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
   * Creates a color picker.
   * 
   * @return the created color picker.
   */
  protected ULCColorPicker createULCColorPicker() {
    return new ULCColorPicker();
  }

  /**
   * Creates a combo box.
   * 
   * @return the created combo box.
   */
  protected ULCComboBox createULCComboBox() {
    return new org.jspresso.framework.gui.ulc.components.server.ULCExtendedComboBox();
  }

  /**
   * Creates a date field.
   * 
   * @param formatPattern
   *          the (simple date format) pattern this date field uses.
   * @param locale
   *          the user locale.
   * @return the created date field.
   */
  protected ULCDateField createULCDateField(String formatPattern, Locale locale) {
    ULCDateField dateField = new ULCDateField(formatPattern, locale);
    ClientContext.setEventDeliveryMode(dateField,
        IUlcEventConstants.VALUE_CHANGED_EVENT,
        IUlcEventConstants.ASYNCHRONOUS_MODE);
    return dateField;
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
   * Creates a label.
   * 
   * @return the created label.
   */
  protected ULCLabel createULCLabel() {
    return new ULCLabel();
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
   * Creates a menu item.
   * 
   * @return the created menu item.
   */
  protected ULCMenuItem createULCMenuItem() {
    return new ULCMenuItem();
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
   * Creates a popup menu.
   * 
   * @return the created popup menu.
   */
  protected ULCPopupMenu createULCPopupMenu() {
    return new ULCPopupMenu();
  }

  /**
   * Creates a scroll pane.
   * 
   * @return the created scroll pane.
   */
  protected ULCScrollPane createULCScrollPane() {
    ULCScrollPane scrollPane = new ULCExtendedScrollPane();
    scrollPane.setMinimumSize(TREE_PREFERRED_SIZE);
    return scrollPane;
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
   * Creates a tabbed pane.
   * 
   * @return the created tabbed pane.
   */
  protected ULCTabbedPane createULCTabbedPane() {
    return new ULCTabbedPane();
  }

  /**
   * Creates a table.
   * 
   * @return the created table.
   */
  protected ULCExtendedTable createULCTable() {
    ULCExtendedTable table = new org.jspresso.framework.gui.ulc.components.server.ULCExtendedTable();
    table.setDragEnabled(true);
    ClientContext.setEventDeliveryMode(table.getSelectionModel(),
        IUlcEventConstants.LIST_SELECTION_EVENT,
        IUlcEventConstants.ASYNCHRONOUS_MODE);
    return table;
  }

  /**
   * Creates a table tree.
   * 
   * @return the created table tree.
   */
  protected ULCTableTree createULCTableTree() {
    ULCTableTree tableTree = new ULCTableTree();
    tableTree.setDragEnabled(true);
    ClientContext.setEventDeliveryMode(tableTree.getSelectionModel(),
        IUlcEventConstants.LIST_SELECTION_EVENT,
        IUlcEventConstants.ASYNCHRONOUS_MODE);
    ClientContext.setModelUpdateMode(tableTree.getModel(),
        IUlcEventConstants.ASYNCHRONOUS_MODE);
    return tableTree;
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
    ClientContext.setEventDeliveryMode(textArea,
        IUlcEventConstants.VALUE_CHANGED_EVENT,
        IUlcEventConstants.ASYNCHRONOUS_MODE);
    return textArea;
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
    ClientContext.setEventDeliveryMode(textField,
        IUlcEventConstants.VALUE_CHANGED_EVENT,
        IUlcEventConstants.ASYNCHRONOUS_MODE);
    return textField;
  }

  /**
   * Creates a tool bar.
   * 
   * @return the created tool bar.
   */
  protected ULCToolBar createULCToolBar() {
    ULCToolBar toolBar = new ULCToolBar();
    toolBar.setFloatable(true);
    toolBar.setBorder(BorderFactory.createRaisedBevelBorder());
    return toolBar;
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
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithActions(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale, IView<ULCComponent> view) {
    if (viewDescriptor.getActionMap() != null) {
      ULCToolBar toolBar = createULCToolBar();
      for (Iterator<ActionList> iter = viewDescriptor.getActionMap()
          .getActionLists().iterator(); iter.hasNext();) {
        ActionList nextActionList = iter.next();
        for (IDisplayableAction action : nextActionList.getActions()) {
          IAction ulcAction = getActionFactory().createAction(action,
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithBorder(IView<ULCComponent> view, Locale locale) {
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
      IView<ULCComponent> view) {
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
      Locale locale, IView<ULCComponent> view) {
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
  protected void showCardInPanel(ULCComponent cardsPeer, String cardName) {
    ((ULCCardPane) cardsPeer).setSelectedName(cardName);
  }

  private Map<String, String> computeTranslationMapping(
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    Map<String, String> translationMapping = new HashMap<String, String>();
    for (String enumerationValue : propertyDescriptor.getEnumerationValues()) {
      translationMapping.put(enumerationValue, getTranslationProvider()
          .getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  enumerationValue), locale));
    }
    return translationMapping;
  }

  private ITableCellRenderer createBooleanTableCellRenderer(
      @SuppressWarnings("unused") IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new BooleanTableCellRenderer();
  }

  private ITableCellRenderer createCollectionTableCellRenderer(
      @SuppressWarnings("unused") ICollectionPropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return null;
  }

  private Color createColor(String colorAsHexString) {
    int[] rgba = ColorHelper.fromHexString(colorAsHexString);
    return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
  }

  private ITableCellRenderer createColorTableCellRenderer(
      @SuppressWarnings("unused") IColorPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new ColorTableCellRenderer();
  }

  private ULCDateDataType createDateDataType(
      @SuppressWarnings("unused") IDatePropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale, SimpleDateFormat format) {
    return new ULCDateDataType(format.toPattern());
  }

  private ITableCellRenderer createDateTableCellRenderer(int column,
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createDateDataType(
        propertyDescriptor, locale,
        createDateFormat(propertyDescriptor, locale)));
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

  private ULCDurationDataType createDurationDataType(
      @SuppressWarnings("unused") IDurationPropertyDescriptor propertyDescriptor,
      Locale locale, @SuppressWarnings("unused") IFormatter formatter) {
    ULCDurationDataType durationDataType = durationDataTypeFactory
        .getTranslationDataType(locale);
    return durationDataType;
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

  private ITableCellRenderer createIntegerTableCellRenderer(int column,
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createIntegerDataType(
        propertyDescriptor, locale, createIntegerFormat(propertyDescriptor,
            locale)));
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

  private ULCPercentDataType createPercentDataType(
      IPercentPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    ULCPercentDataType percentDataType = new ULCPercentDataType();
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      percentDataType.setFractionalDigits(propertyDescriptor
          .getMaxFractionDigit().intValue());
    }
    return percentDataType;
  }

  private ITableCellRenderer createPercentTableCellRenderer(int column,
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createPercentDataType(
        propertyDescriptor, locale));
  }

  private ULCPopupMenu createPopupMenu(ULCComponent sourceComponent,
      IView<ULCComponent> view, IActionHandler actionHandler, Locale locale) {
    if (sourceComponent instanceof ULCExtendedTable) {
      return createULCTablePopupMenu((ULCExtendedTable) sourceComponent, view,
          actionHandler, locale);
    }
    return null;
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

  private ITableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused") IReferencePropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return null;
  }

  private ITableCellRenderer createRelationshipEndTableCellRenderer(
      IRelationshipEndPropertyDescriptor propertyDescriptor, Locale locale) {
    ITableCellRenderer cellRenderer = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      cellRenderer = createReferenceTableCellRenderer(
          (IReferencePropertyDescriptor<?>) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      cellRenderer = createCollectionTableCellRenderer(
          (ICollectionPropertyDescriptor<?>) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private ITableCellRenderer createStringTableCellRenderer(int column,
      @SuppressWarnings("unused") IStringPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new FormattedTableCellRenderer(column, null);
  }

  private ULCDateDataType createTimeDataType(
      @SuppressWarnings("unused") ITimePropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale, SimpleDateFormat format) {
    return new ULCDateDataType(format.toPattern());
  }

  private ITableCellRenderer createTimeTableCellRenderer(int column,
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(column, createTimeDataType(
        propertyDescriptor, locale,
        createTimeFormat(propertyDescriptor, locale)));
  }

  private ULCPopupMenu createULCPopupMenu(ULCComponent sourceComponent,
      ActionMap actionMap, IModelDescriptor modelDescriptor,
      IViewDescriptor viewDescriptor, IValueConnector viewConnector,
      IActionHandler actionHandler, Locale locale) {
    ULCPopupMenu popupMenu = createULCPopupMenu();
    ULCLabel titleLabel = createULCLabel();
    titleLabel.setText(viewDescriptor.getI18nName(getTranslationProvider(),
        locale));
    titleLabel.setIcon(getIconFactory().getIcon(
        viewDescriptor.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    titleLabel.setHorizontalAlignment(IDefaults.CENTER);
    popupMenu.add(titleLabel);
    popupMenu.addSeparator();
    for (Iterator<ActionList> iter = actionMap.getActionLists().iterator(); iter
        .hasNext();) {
      ActionList nextActionList = iter.next();
      for (IDisplayableAction action : nextActionList.getActions()) {
        IAction ulcAction = getActionFactory().createAction(action,
            actionHandler, sourceComponent, modelDescriptor, viewConnector,
            locale);
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

  private ULCPopupMenu createULCTablePopupMenu(ULCExtendedTable table,
      IView<ULCComponent> tableView, IActionHandler actionHandler, Locale locale) {

    IValueConnector elementConnector = tableView.getConnector();
    IModelDescriptor modelDescriptor = tableView.getDescriptor()
        .getModelDescriptor();
    ActionMap actionMap = ((ICollectionViewDescriptor) tableView
        .getDescriptor()).getActionMap();

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
      return null;
    }

    return createULCPopupMenu(tree, actionMap, modelDescriptor, viewDescriptor,
        viewConnector, actionHandler, locale);
  }

  private void decorateWithTitle(IView<ULCComponent> view, Locale locale) {
    view.getPeer()
        .setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), view.getDescriptor()
                    .getI18nName(getTranslationProvider(), locale)));
    // ULCInternalFrame iFrame = new ULCInternalFrame(view.getDescriptor()
    // .getI18nName(getTranslationProvider(), locale), true, true,
    // true, true);
    // iFrame.setFrameIcon(iconFactory.getIcon(view.getDescriptor()
    // .getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    // iFrame.add(view.getPeer());
    // iFrame.pack();
    // iFrame.setPreferredSize(new Dimension(100,100));
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
        selectionMode = ULCListSelectionModel.SINGLE_SELECTION;
        break;
      case SINGLE_INTERVAL_SELECTION:
        selectionMode = ULCListSelectionModel.SINGLE_INTERVAL_SELECTION;
        break;
      default:
        selectionMode = ULCListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
        break;
    }
    return selectionMode;
  }

  private final class ColorTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -8013724509060227795L;

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getTableCellRendererComponent(
        com.ulcjava.base.application.ULCTable table, Object value,
        boolean isSelected, boolean hasFocus, int row) {
      if (value != null) {
        int[] rgba = ColorHelper.fromHexString((String) value);
        setBackground(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
      } else {
        setBackground(null);
      }
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row);
    }
  }

  private final class ConnectorTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -5153268751092971328L;

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getTreeCellRendererComponent(
        com.ulcjava.base.application.ULCTree tree, Object value, boolean sel,
        boolean expanded, boolean leaf, boolean nodeHasFocus) {
      ULCLabel renderer = (ULCLabel) super.getTreeCellRendererComponent(tree,
          value, sel, expanded, leaf, nodeHasFocus);
      if (value instanceof IValueConnector) {
        if (value instanceof IRenderableCompositeValueConnector) {
          renderer.setIcon(getIconFactory().getIcon(
              ((IRenderableCompositeValueConnector) value)
                  .getDisplayIconImageUrl(), IIconFactory.SMALL_ICON_SIZE));
        }
        renderer.setOpaque(false);
        renderer.setBackground(null);
      }
      return renderer;
    }
  }

  private final class TranslatedEnumerationListCellRenderer extends
      DefaultComboBoxCellRenderer {

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
    public IRendererComponent getComboBoxCellRendererComponent(
        ULCComboBox comboBox, Object value, boolean isSelected, int index) {
      if (propertyDescriptor.isTranslated()) {
        setDataType(translationDataTypeFactory.getTranslationDataType(
            propertyDescriptor.getEnumerationName(), locale,
            computeTranslationMapping(propertyDescriptor, locale)));
      }
      setIcon(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(String.valueOf(value)),
          IIconFactory.TINY_ICON_SIZE));
      return super.getComboBoxCellRendererComponent(comboBox, value,
          isSelected, index);
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
      setIcon(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(String.valueOf(value)),
          IIconFactory.TINY_ICON_SIZE));
      UlcUtil.alternateEvenOddBackground(this, table, isSelected, row);
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row);
    }
  }

  private final class TreeNodePopupFactory implements ITreePathPopupFactory {

    private IActionHandler      actionHandler;
    private Locale              locale;
    private ULCTree             tree;
    private IView<ULCComponent> view;

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
}
