/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
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
import org.jspresso.framework.gui.ulc.components.server.ULCPasswordDataType;
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
import org.jspresso.framework.model.descriptor.IHtmlPropertyDescriptor;
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
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.gui.FontHelper;
import org.jspresso.framework.util.ulc.UlcUtil;
import org.jspresso.framework.view.AbstractViewFactory;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
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
import com.ulcjava.base.application.DefaultListCellRenderer;
import com.ulcjava.base.application.GridBagConstraints;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.IListModel;
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
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.ULCTableTree;
import com.ulcjava.base.application.ULCTextArea;
import com.ulcjava.base.application.ULCTextField;
import com.ulcjava.base.application.ULCToolBar;
import com.ulcjava.base.application.ULCTree;
import com.ulcjava.base.application.datatype.ULCDateDataType;
import com.ulcjava.base.application.datatype.ULCNumberDataType;
import com.ulcjava.base.application.datatype.ULCPercentDataType;
import com.ulcjava.base.application.event.TreeModelEvent;
import com.ulcjava.base.application.event.serializable.ITreeModelListener;
import com.ulcjava.base.application.table.DefaultTableCellRenderer;
import com.ulcjava.base.application.table.ITableCellRenderer;
import com.ulcjava.base.application.table.ULCTableColumn;
import com.ulcjava.base.application.tree.DefaultTreeCellRenderer;
import com.ulcjava.base.application.tree.ITreeModel;
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
  private ULCPasswordDataType           passwordDataType           = new ULCPasswordDataType();

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
  protected void adjustSizes(IViewDescriptor viewDescriptor,
      ULCComponent component, IFormatter formatter, Object templateValue,
      int extraWidth) {
    if (viewDescriptor.getFont() != null) {
      // must set font before computing size.
      component.setFont(createFont(viewDescriptor.getFont(), component
          .getFont()));
    }
    int preferredWidth = computePixelWidth(component, getFormatLength(
        formatter, templateValue))
        + extraWidth;
    Dimension size = new Dimension(preferredWidth, component.getFont()
        .getSize() * 5 / 3);
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
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IBinaryPropertyDescriptor propertyDescriptor = (IBinaryPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCActionField viewComponent = createULCActionField(false);
    ULCActionFieldConnector connector = new ULCActionFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    viewComponent.setActions(createBinaryActions(viewComponent, connector,
        propertyDescriptor, actionHandler, locale));
    adjustSizes(propertyViewDescriptor, viewComponent, null, null);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createBooleanPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IBooleanPropertyDescriptor propertyDescriptor = (IBooleanPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCCheckBox viewComponent = createULCCheckBox();
    ULCToggleButtonConnector connector = new ULCToggleButtonConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
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

    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<ULCComponent> northView = createView(viewDescriptor
          .getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.add(northView.getPeer(), ULCBorderLayoutPane.NORTH);
      childrenViews.add(northView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<ULCComponent> westView = createView(viewDescriptor
          .getWestViewDescriptor(), actionHandler, locale);
      viewComponent.add(westView.getPeer(), ULCBorderLayoutPane.WEST);
      childrenViews.add(westView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<ULCComponent> centerView = createView(viewDescriptor
          .getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.add(centerView.getPeer(), ULCBorderLayoutPane.CENTER);
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<ULCComponent> eastView = createView(viewDescriptor
          .getEastViewDescriptor(), actionHandler, locale);
      viewComponent.add(eastView.getPeer(), ULCBorderLayoutPane.EAST);
      childrenViews.add(eastView);
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
    viewComponent.add(createEmptyComponent(), ICardViewDescriptor.DEFAULT_CARD);
    viewComponent.add(createSecurityComponent(),
        ICardViewDescriptor.SECURITY_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<ULCComponent> childView = createView(
          childViewDescriptor.getValue(), actionHandler, locale);
      viewComponent.add(childView.getPeer(), childViewDescriptor.getKey());
      view.addToChildrenMap(childViewDescriptor.getKey(), childView);
    }
    view.setConnector(createCardViewConnector(view, actionHandler, locale));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void addCard(IMapView<ULCComponent> cardView,
      IView<ULCComponent> card, String cardName) {
    ((ULCCardPane) cardView.getPeer()).add(card.getPeer(), cardName);
    cardView.addToChildrenMap(cardName, card);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createColorPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IColorPropertyDescriptor propertyDescriptor = (IColorPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
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
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
    // boolean lastRowNeedsFilling = true;

    for (IPropertyViewDescriptor propertyViewDescriptor : viewDescriptor
        .getPropertyViewDescriptors()) {
      String propertyName = propertyViewDescriptor.getModelDescriptor()
          .getName();
      IPropertyDescriptor propertyDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor
          .getModelDescriptor()).getComponentDescriptor()
          .getPropertyDescriptor(propertyName);
      if (propertyDescriptor == null) {
        throw new ViewException("Property descriptor [" + propertyName
            + "] does not exist for model descriptor "
            + viewDescriptor.getModelDescriptor().getName() + ".");
      }
      IView<ULCComponent> propertyView = createView(propertyViewDescriptor,
          actionHandler, locale);
      boolean forbidden = false;
      if (!actionHandler.isAccessGranted(propertyViewDescriptor)) {
        forbidden = true;
        propertyView.setPeer(createSecurityComponent());
      }
      connector.addChildConnector(propertyView.getConnector());
      // already handled in createView.
      // if (propertyViewDescriptor.getReadabilityGates() != null) {
      // for (IGate gate : propertyViewDescriptor.getReadabilityGates()) {
      // propertyView.getConnector().addReadabilityGate(gate.clone());
      // }
      // }
      // if (propertyViewDescriptor.getWritabilityGates() != null) {
      // for (IGate gate : propertyViewDescriptor.getWritabilityGates()) {
      // propertyView.getConnector().addWritabilityGate(gate.clone());
      // }
      // }
      // propertyView.getConnector().setLocallyWritable(
      // !propertyViewDescriptor.isReadOnly());
      ULCLabel propertyLabel = createPropertyLabel(propertyViewDescriptor,
          propertyView.getPeer(), locale);
      if (forbidden) {
        propertyLabel.setText(" ");
      }

      int propertyWidth = propertyViewDescriptor.getWidth().intValue();
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
          constraints.setInsets(new Insets(5, 5, 5, 5));
          if (isHeightExtensible(propertyViewDescriptor)) {
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
        case NONE:
          break;
        default:
          break;
      }
      if (viewDescriptor.getLabelsPosition() != ELabelPosition.NONE
          && propertyLabel.getText() != null
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
        case NONE:
          constraints.setGridX(currentX);
          constraints.setGridY(currentY);
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
      if (isHeightExtensible(propertyViewDescriptor)) {
        constraints.setWeightY(1.0);
        constraints.setFill(GridBagConstraints.BOTH);
        isSpaceFilled = true;
        // if (!ite.hasNext()) {
        // constraints.gridwidth = GridBagConstraints.REMAINDER;
        // lastRowNeedsFilling = false;
        // }
      } else {
        constraints.setFill(GridBagConstraints.NONE);
      }
      viewComponent.add(propertyView.getPeer(), constraints);

      currentX += propertyWidth;
    }
    // if (lastRowNeedsFilling) {
    // fillLastRow(viewComponent);
    // }
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
        case NONE:
          constraints.setGridY(currentY + 1);
          constraints.setGridWidth(viewDescriptor.getColumnCount());
          break;
        default:
          break;
      }
      viewComponent.add(filler, constraints);
    }
    return view;
  }

  // private void fillLastRow(ULCGridBagLayoutPane viewComponent) {
  // GridBagConstraints constraints = new GridBagConstraints();
  // constraints.setGridX(GridBagConstraints.RELATIVE);
  // constraints.setWeightX(1.0);
  // constraints.setFill(GridBagConstraints.HORIZONTAL);
  // constraints.setGridWidth(GridBagConstraints.REMAINDER);
  // ULCBorderLayoutPane filler = createBorderLayoutPane();
  // // filler.setBorder(new SLineBorder(Color.BLUE));
  // viewComponent.add(filler, constraints);
  // }

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
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDatePropertyDescriptor propertyDescriptor = (IDatePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    ULCComponent viewComponent;
    SimpleDateFormat format = createDateFormat(propertyDescriptor, locale);
    IFormatter formatter = createFormatter(format);
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createULCLabel(true);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
      ((ULCLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createULCDateField(format.toPattern(), locale);
      connector = new ULCDateFieldConnector(propertyDescriptor.getName(),
          (ULCDateField) viewComponent);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getDateTemplateValue(propertyDescriptor), ClientContext
              .getScreenResolution() / 3);
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createDecimalPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDecimalPropertyDescriptor propertyDescriptor = (IDecimalPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    ULCComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createULCLabel(true);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
      ((ULCLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createULCTextField();
      connector = new ULCTextFieldConnector(propertyDescriptor.getName(),
          (ULCTextField) viewComponent);
      ((ULCTextFieldConnector) connector).setFormatter(formatter);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getDecimalTemplateValue(propertyDescriptor));
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createDurationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDurationPropertyDescriptor propertyDescriptor = (IDurationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCComponent viewComponent;
    IValueConnector connector;
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createULCLabel(true);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
      ((ULCLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createULCTextField();
      connector = new ULCTextFieldConnector(propertyDescriptor.getName(),
          (ULCTextField) viewComponent);
      ((ULCTextFieldConnector) connector).setFormatter(formatter);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getDurationTemplateValue(propertyDescriptor));
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IEnumerationPropertyDescriptor propertyDescriptor = (IEnumerationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCComboBox viewComponent = createULCComboBox();
    if (!propertyDescriptor.isMandatory()) {
      viewComponent.addItem(null);
    }
    for (String enumElement : propertyDescriptor.getEnumerationValues()) {
      viewComponent.addItem(enumElement);
    }
    viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(
        propertyDescriptor, locale));
    adjustSizes(propertyViewDescriptor, viewComponent, null,
        getEnumerationTemplateValue(propertyDescriptor, locale), ClientContext
            .getScreenResolution() * 2 / 6);
    ULCComboBoxConnector connector = new ULCComboBoxConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
    ULCLabel imageLabel = createULCLabel(false);
    ULCImageConnector connector = new ULCImageConnector(viewDescriptor
        .getModelDescriptor().getName(), imageLabel);
    connector.setExceptionHandler(actionHandler);
    ULCBorderLayoutPane viewComponent = createBorderLayoutPane();
    IView<ULCComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    if (viewDescriptor.isScrollable()) {
      imageLabel.setHorizontalAlignment(IDefaults.LEFT);
      imageLabel.setVerticalAlignment(IDefaults.TOP);
      ULCScrollPane scrollPane = createULCScrollPane();
      scrollPane.setViewPortView(imageLabel);
      viewComponent.add(scrollPane, ULCBorderLayoutPane.CENTER);
    } else {
      imageLabel.setHorizontalAlignment(IDefaults.CENTER);
      imageLabel.setVerticalAlignment(IDefaults.CENTER);
      viewComponent.add(imageLabel, ULCBorderLayoutPane.CENTER);
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createActionView(
      IActionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ULCButton viewComponent = createULCButton();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        ModelRefPropertyConnector.THIS_PROPERTY);
    connector.setExceptionHandler(actionHandler);
    IView<ULCComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    viewComponent.setAction(getActionFactory().createAction(
        viewDescriptor.getAction(), viewDescriptor.getPreferredSize(),
        actionHandler, view, locale));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createIntegerPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IIntegerPropertyDescriptor propertyDescriptor = (IIntegerPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    ULCComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createULCLabel(true);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
      ((ULCLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createULCTextField();
      connector = new ULCTextFieldConnector(propertyDescriptor.getName(),
          (ULCTextField) viewComponent);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getIntegerTemplateValue(propertyDescriptor));
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createListView(
      IListViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = (ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor();
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    if (rowConnectorPrototype instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) rowConnectorPrototype)
          .setDisplayIconImageUrl(viewDescriptor.getIconImageURL());
      ((AbstractCompositeValueConnector) rowConnectorPrototype)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
            rowConnectorPrototype);
    ULCList viewComponent = createULCList();
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createListConnector(viewDescriptor
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
    if (viewDescriptor.getRowAction() != null) {
      final IAction rowAction = getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale);
      viewComponent.addActionListener(rowAction);
    }
    attachDefaultCollectionListener(connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createPasswordPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IPasswordPropertyDescriptor propertyDescriptor = (IPasswordPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCPasswordField viewComponent = createULCPasswordField();
    ULCPasswordFieldConnector connector = new ULCPasswordFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    adjustSizes(propertyViewDescriptor, viewComponent, null,
        getStringTemplateValue(propertyDescriptor));
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createPercentPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IPercentPropertyDescriptor propertyDescriptor = (IPercentPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    ULCComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createULCLabel(true);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
      ((ULCLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createULCTextField();
      connector = new ULCTextFieldConnector(propertyDescriptor.getName(),
          (ULCTextField) viewComponent);
      ((ULCTextFieldConnector) connector).setFormatter(formatter);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getPercentTemplateValue(propertyDescriptor));
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createReferencePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IReferencePropertyDescriptor<?> propertyDescriptor = (IReferencePropertyDescriptor<?>) propertyViewDescriptor
        .getModelDescriptor();
    ULCActionField viewComponent = createULCActionField(true);
    ULCReferenceFieldConnector connector = new ULCReferenceFieldConnector(
        propertyDescriptor.getName(), viewComponent);
    List<String> renderedProperties = propertyViewDescriptor
        .getRenderedChildProperties();
    String renderedProperty;
    if (renderedProperties != null && !renderedProperties.isEmpty()) {
      // it's a custom rendered property.
      renderedProperty = renderedProperties.get(0);
    } else {
      renderedProperty = propertyDescriptor.getComponentDescriptor()
          .getToStringProperty();
    }
    connector.setRenderingConnector(new BasicValueConnector(renderedProperty));
    connector.setExceptionHandler(actionHandler);
    IAction lovAction = createLovAction(viewComponent, connector,
        propertyViewDescriptor, actionHandler, locale);
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
          getIconFactory().getTinyIconSize()));
    }
    viewComponent.setActions(Collections.singletonList(lovAction));
    adjustSizes(propertyViewDescriptor, viewComponent, null, null);
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ISourceCodePropertyDescriptor propertyDescriptor = (ISourceCodePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCJEditTextArea viewComponent = createULCJEditTextArea(propertyDescriptor
        .getLanguage());
    ULCJEditTextAreaConnector connector = new ULCJEditTextAreaConnector(
        propertyDescriptor.getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createULCLabel(true);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
    } else {
      viewComponent = createULCTextField();
      connector = new ULCTextFieldConnector(propertyDescriptor.getName(),
          (ULCTextField) viewComponent);
      adjustSizes(propertyViewDescriptor, viewComponent, null,
          getStringTemplateValue(propertyDescriptor));
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
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
    ULCLabel iconLabel = createULCLabel(false);
    iconLabel.setIcon(getIconFactory().getIcon(
        modelDescriptor.getCollectionDescriptor().getElementDescriptor()
            .getIconImageURL(), getIconFactory().getTinyIconSize()));
    iconLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setCorner(ULCScrollPane.UPPER_RIGHT_CORNER, iconLabel);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    if (viewDescriptor.isHorizontallyScrollable()) {
      viewComponent.setAutoResizeMode(ULCTable.AUTO_RESIZE_OFF);
    } else {
      scrollPane
          .setHorizontalScrollBarPolicy(ULCScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    List<Class<?>> columnClasses = new ArrayList<Class<?>>();
    List<IFormatter> columnFormatters = new ArrayList<IFormatter>();
    Set<String> forbiddenColumns = new HashSet<String>();
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String columnId = columnViewDescriptor.getModelDescriptor().getName();
      if (actionHandler.isAccessGranted(columnViewDescriptor)) {
        IValueConnector columnConnector = createColumnConnector(
            columnViewDescriptor, modelDescriptor.getCollectionDescriptor()
                .getElementDescriptor(), actionHandler);
        rowConnectorPrototype.addChildConnector(columnConnector);
        IPropertyDescriptor columnModelDescriptor = modelDescriptor
            .getCollectionDescriptor().getElementDescriptor()
            .getPropertyDescriptor(columnId);
        if (columnModelDescriptor instanceof IReferencePropertyDescriptor<?>) {
          columnClasses.add(String.class);
          columnFormatters.add(null);
        } else if (columnModelDescriptor instanceof IBooleanPropertyDescriptor) {
          columnClasses.add(Boolean.class);
          columnFormatters.add(null);
        } else {
          if (columnModelDescriptor instanceof IDecimalPropertyDescriptor
              && ((IDecimalPropertyDescriptor) columnModelDescriptor)
                  .isUsingBigDecimal()) {
            columnClasses.add(String.class);
            columnFormatters
                .add(createFormatter(columnModelDescriptor, locale));
          } else {
            columnClasses.add(columnModelDescriptor.getModelType());
            columnFormatters.add(null);
          }
        }
        // already handled in createColumnConnector
        // if (columnViewDescriptor.getReadabilityGates() != null) {
        // ...
        // if (columnViewDescriptor.getWritabilityGates() != null) {
        // ...
        // }
        // columnConnector.setLocallyWritable(!columnViewDescriptor.isReadOnly());
      } else {
        // The column simply won't be added.
        forbiddenColumns.add(columnId);
      }
    }
    List<String> columnConnectorKeys = new ArrayList<String>(
        rowConnectorPrototype.getChildConnectorKeys());
    // remove row rendering connector id
    columnConnectorKeys.remove(0);
    CollectionConnectorTableModel tableModel = new CollectionConnectorTableModel(
        connector, columnConnectorKeys, columnClasses, columnFormatters);
    tableModel.setExceptionHandler(actionHandler);

    if (viewDescriptor.isSortable()) {
      AbstractTableSorter sorterDecorator;
      if (viewDescriptor.getSortingAction() != null) {
        sorterDecorator = new ActionTableSorter(tableModel, viewComponent
            .getTableHeader(), actionHandler, viewDescriptor.getSortingAction());
      } else {
        sorterDecorator = new TableSorter(tableModel, viewComponent
            .getTableHeader());
        ((TableSorter) sorterDecorator).setColumnComparator(String.class,
            String.CASE_INSENSITIVE_ORDER);
      }
      org.jspresso.framework.util.gui.Dimension iconSize = new org.jspresso.framework.util.gui.Dimension(
          viewComponent.getTableHeader().getFont().getSize(), viewComponent
              .getTableHeader().getFont().getSize());
      sorterDecorator.setUpIcon(getIconFactory().getUpIcon(iconSize));
      sorterDecorator.setDownIcon(getIconFactory().getDownIcon(iconSize));
      ClientContext.setModelUpdateMode(sorterDecorator,
          IUlcEventConstants.ASYNCHRONOUS_MODE);
      viewComponent.setModel(sorterDecorator);
      listSelectionModelBinder.bindSelectionModel(connector, viewComponent
          .getSelectionModel(), sorterDecorator);
    } else {
      ClientContext.setModelUpdateMode(tableModel,
          IUlcEventConstants.ASYNCHRONOUS_MODE);
      viewComponent.setModel(tableModel);
      listSelectionModelBinder.bindSelectionModel(connector, viewComponent
          .getSelectionModel(), null);
    }

    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));
    int maxColumnSize = computePixelWidth(viewComponent,
        getMaxColumnCharacterLength());
    int columnIndex = 0;
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String propertyName = columnViewDescriptor.getModelDescriptor().getName();
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
        IView<ULCComponent> editorView = createView(columnViewDescriptor,
            actionHandler, locale);
        // if (editorView.getPeer() instanceof ULCActionField) {
        // ULCActionField actionField = (ULCActionField) editorView.getPeer();
        // actionField.setActions(Collections.singletonList(actionField
        // .getActions().get(0)));
        // }
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
    if (viewDescriptor.getRowAction() != null) {
      final IAction rowAction = getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale);
      viewComponent.addActionListener(rowAction);
    }
    int minimumWidth = 0;
    for (int i = 0; i < 1
        && i < viewComponent.getColumnModel().getColumnCount(); i++) {
      minimumWidth += viewComponent.getColumnModel().getColumn(i)
          .getPreferredWidth();
    }
    scrollPane.setMinimumSize(new Dimension(minimumWidth, viewComponent
        .getRowHeight() * 7));
    attachDefaultCollectionListener(connector);
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
      if (actionHandler.isAccessGranted(childViewDescriptor)) {
        IView<ULCComponent> childView = createView(childViewDescriptor,
            actionHandler, locale);
        ULCIcon childIcon = getIconFactory().getIcon(
            childViewDescriptor.getIconImageURL(),
            getIconFactory().getSmallIconSize());
        if (childViewDescriptor.getDescription() != null) {
          viewComponent.addTab(childViewDescriptor.getI18nName(
              getTranslationProvider(), locale), childIcon,
              childView.getPeer(), childViewDescriptor.getI18nDescription(
                  getTranslationProvider(), locale)
                  + TOOLTIP_ELLIPSIS);
        } else {
          viewComponent
              .addTab(childViewDescriptor.getI18nName(getTranslationProvider(),
                  locale), childIcon, childView.getPeer());
        }
        childrenViews.add(childView);
      }
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createTextPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCScrollPane scrollPane = createULCScrollPane();
    ULCTextArea viewComponent = createULCTextArea();
    viewComponent.setLineWrap(true);
    scrollPane.setViewPortView(viewComponent);
    scrollPane
        .setHorizontalScrollBarPolicy(ULCScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    IValueConnector connector = new ULCTextAreaConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(scrollPane, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createHtmlPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyViewDescriptor.isReadOnly()) {
      IHtmlPropertyDescriptor propertyDescriptor = (IHtmlPropertyDescriptor) propertyViewDescriptor
          .getModelDescriptor();
      ULCScrollPane scrollPane = createULCScrollPane();
      ULCLabel viewComponent = createULCLabel(true);
      viewComponent.setVerticalAlignment(IDefaults.TOP);
      viewComponent.setHorizontalAlignment(IDefaults.LEADING);
      IValueConnector connector = new ULCLabelConnector(propertyDescriptor
          .getName(), viewComponent);
      ((ULCLabelConnector) connector).setMultiLine(true);
      scrollPane.setViewPortView(viewComponent);
      connector.setExceptionHandler(actionHandler);
      return constructView(scrollPane, propertyViewDescriptor, connector);
    }
    return createTextPropertyView(propertyViewDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createTimePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ITimePropertyDescriptor propertyDescriptor = (ITimePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    ULCComponent viewComponent;
    IFormatter formatter = createTimeFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createULCLabel(true);
      connector = new ULCLabelConnector(propertyDescriptor.getName(),
          (ULCLabel) viewComponent);
      ((ULCLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createULCTextField();
      connector = new ULCTextFieldConnector(propertyDescriptor.getName(),
          (ULCTextField) viewComponent);
      ((ULCTextFieldConnector) connector).setFormatter(formatter);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getTimeTemplateValue(propertyDescriptor));
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<ULCComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = createTreeViewConnector(
        viewDescriptor, actionHandler, locale);

    final ULCExtendedTree viewComponent = createULCTree();
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
    if (viewDescriptor.isExpanded()) {
      viewComponent.getModel().addTreeModelListener(new ITreeModelListener() {

        private static final long serialVersionUID = 6875911618418554499L;

        public void treeStructureChanged(TreeModelEvent e) {
          expandAll(viewComponent, e.getTreePath());
        }

        public void treeNodesInserted(TreeModelEvent e) {
          expandAll(viewComponent, e.getTreePath());
        }

        public void treeNodesRemoved(
            @SuppressWarnings("unused") TreeModelEvent e) {
          // NO-OP
        }

        public void treeNodesChanged(
            @SuppressWarnings("unused") TreeModelEvent e) {
          // NO-OP
        }
      });
    }
    ULCScrollPane scrollPane = createULCScrollPane();
    scrollPane.setViewPortView(viewComponent);
    IView<ULCComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    viewComponent.setPopupFactory(new TreeNodePopupFactory(viewComponent, view,
        actionHandler, locale));
    scrollPane.setMinimumSize(TREE_PREFERRED_SIZE);
    return view;
  }

  private void expandAll(ULCTree tree, TreePath tp) {
    if (tp == null) {
      return;
    }
    Object node = tp.getLastPathComponent();
    ITreeModel model = tree.getModel();
    if (!model.isLeaf(node)) {
      tree.expandPath(tp);
      for (int i = 0; i < model.getChildCount(node); i++) {
        expandAll(tree, tp.pathByAddingChild(model.getChild(node, i)));
      }
    }
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
   * @param bold
   *          make it bold ?
   * @return the created label.
   */
  protected ULCLabel createULCLabel(boolean bold) {
    // To have preferred height computed.
    ULCLabel label = new ULCLabel(" ");
    if (bold) {
      label.setFont(createFont(BOLD_FONT, label.getFont()));
    }
    return label;
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
          if (actionHandler.isAccessGranted(action)) {
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
      view.getPeer().setFont(
          createFont(viewDescriptor.getFont(), view.getPeer().getFont()));
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

  private Font createFont(String fontString, Font defaultFont) {
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
    if (font.getName() == null || font.getName().length() == 0) {
      font.setName(defaultFont.getName());
    }
    if (font.getSize() < 0) {
      font.setSize(defaultFont.getSize());
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

  private ULCLabel createPropertyLabel(
      IPropertyViewDescriptor propertyViewDescriptor,
      ULCComponent propertyComponent, Locale locale) {
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    ULCLabel propertyLabel = createULCLabel(false);
    StringBuffer labelText = new StringBuffer(propertyDescriptor.getI18nName(
        getTranslationProvider(), locale));
    if (propertyDescriptor.isMandatory()) {
      labelText.append("*");
      propertyLabel.setForeground(Color.red);
    }
    propertyLabel.setText(labelText.toString());
    propertyLabel.setLabelFor(propertyComponent);
    if (propertyViewDescriptor.getLabelFont() != null) {
      propertyLabel.setFont(createFont(propertyViewDescriptor.getLabelFont(),
          propertyLabel.getFont()));
    }
    if (propertyViewDescriptor.getLabelForeground() != null) {
      propertyLabel.setForeground(createColor(propertyViewDescriptor
          .getLabelForeground()));
    }
    if (propertyViewDescriptor.getLabelBackground() != null) {
      propertyLabel.setBackground(createColor(propertyViewDescriptor
          .getLabelBackground()));
    }
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

    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      cellRenderer = createReferenceTableCellRenderer(
          (IReferencePropertyDescriptor<?>) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
      cellRenderer = createCollectionTableCellRenderer(
          (ICollectionPropertyDescriptor<?>) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private ITableCellRenderer createStringTableCellRenderer(int column,
      IStringPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    if (propertyDescriptor instanceof IPasswordPropertyDescriptor) {
      return new FormattedTableCellRenderer(column, createPasswordDataType());
    }
    return new FormattedTableCellRenderer(column, null);
  }

  private ULCPasswordDataType createPasswordDataType() {
    return passwordDataType;
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
    ULCLabel titleLabel = createULCLabel(false);
    titleLabel.setText(viewDescriptor.getI18nName(getTranslationProvider(),
        locale));
    titleLabel.setIcon(getIconFactory().getIcon(
        viewDescriptor.getIconImageURL(), getIconFactory().getTinyIconSize()));
    titleLabel.setHorizontalAlignment(IDefaults.CENTER);
    popupMenu.add(titleLabel);
    popupMenu.addSeparator();
    for (Iterator<ActionList> iter = actionMap.getActionLists().iterator(); iter
        .hasNext();) {
      ActionList nextActionList = iter.next();
      for (IDisplayableAction action : nextActionList.getActions()) {
        if (actionHandler.isAccessGranted(action)) {
          IAction ulcAction = getActionFactory().createAction(action,
              actionHandler, sourceComponent, modelDescriptor, viewConnector,
              locale);
          ULCMenuItem actionItem = createULCMenuItem();
          actionItem.setAction(ulcAction);
          popupMenu.add(actionItem);
        }
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
    public IRendererComponent getTableCellRendererComponent(ULCTable table,
        Object value, boolean isSelected, boolean hasFocus, int row) {
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
    public IRendererComponent getTreeCellRendererComponent(ULCTree tree,
        Object value, boolean sel, boolean expanded, boolean leaf,
        boolean nodeHasFocus) {
      ULCLabel renderer = (ULCLabel) super.getTreeCellRendererComponent(tree,
          value, sel, expanded, leaf, nodeHasFocus);
      if (value instanceof IValueConnector) {
        if (value instanceof IRenderableCompositeValueConnector) {
          renderer.setIcon(getIconFactory().getIcon(
              ((IRenderableCompositeValueConnector) value)
                  .getDisplayIconImageUrl(),
              getIconFactory().getSmallIconSize()));
        }
        renderer.setOpaque(false);
        renderer.setBackground(null);
      }
      return renderer;
    }
  }

  private final class EvenOddListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 3450896483325205907L;

    /**
     * {@inheritDoc}
     */
    @Override
    public IRendererComponent getListCellRendererComponent(ULCList list,
        Object value, boolean isSelected, boolean hasFocus, int row) {
      ULCLabel renderer = (ULCLabel) super.getListCellRendererComponent(list,
          value, isSelected, hasFocus, row);
      IListModel listModel = list.getModel();
      if (listModel instanceof CollectionConnectorListModel) {
        IValueConnector cellConnector = ((CollectionConnectorListModel) listModel)
            .getCellConnector(row);
        if (cellConnector instanceof IRenderableCompositeValueConnector) {
          renderer.setIcon(getIconFactory().getIcon(
              ((IRenderableCompositeValueConnector) cellConnector)
                  .getDisplayIconImageUrl(),
              getIconFactory().getSmallIconSize()));
        }
      }
      renderer.setOpaque(false);
      renderer.setBackground(null);
      UlcUtil.alternateEvenOddBackground(renderer, list, isSelected, row);
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
          getIconFactory().getTinyIconSize()));
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
    public IRendererComponent getTableCellRendererComponent(ULCTable table,
        Object value, boolean isSelected, boolean hasFocus, int row) {
      if (propertyDescriptor.isTranslated()) {
        setDataType(translationDataTypeFactory.getTranslationDataType(
            propertyDescriptor.getEnumerationName(), locale,
            computeTranslationMapping(propertyDescriptor, locale)));
      }
      setIcon(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(String.valueOf(value)),
          getIconFactory().getTinyIconSize()));
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyPreferredSize(ULCComponent component,
      org.jspresso.framework.util.gui.Dimension preferredSize) {
    if (preferredSize != null) {
      component.setPreferredSize(new Dimension(preferredSize.getWidth(),
          preferredSize.getHeight()));
    }
  }
}
