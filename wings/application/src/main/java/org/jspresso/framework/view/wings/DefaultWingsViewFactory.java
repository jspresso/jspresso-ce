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
package org.jspresso.framework.view.wings;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.binding.wings.CollectionConnectorListModel;
import org.jspresso.framework.binding.wings.CollectionConnectorTableModel;
import org.jspresso.framework.binding.wings.ConnectorHierarchyTreeModel;
import org.jspresso.framework.binding.wings.IListSelectionModelBinder;
import org.jspresso.framework.binding.wings.ITreeSelectionModelBinder;
import org.jspresso.framework.binding.wings.SActionFieldConnector;
import org.jspresso.framework.binding.wings.SCheckBoxConnector;
import org.jspresso.framework.binding.wings.SColorPickerConnector;
import org.jspresso.framework.binding.wings.SComboBoxConnector;
import org.jspresso.framework.binding.wings.SFormattedFieldConnector;
import org.jspresso.framework.binding.wings.SImageConnector;
import org.jspresso.framework.binding.wings.SLabelConnector;
import org.jspresso.framework.binding.wings.SPasswordFieldConnector;
import org.jspresso.framework.binding.wings.SPercentFieldConnector;
import org.jspresso.framework.binding.wings.SReferenceFieldConnector;
import org.jspresso.framework.binding.wings.STextAreaConnector;
import org.jspresso.framework.binding.wings.STextFieldConnector;
import org.jspresso.framework.binding.wings.XCalendarConnector;
import org.jspresso.framework.gui.wings.components.ClickableHeaderSTable;
import org.jspresso.framework.gui.wings.components.SActionField;
import org.jspresso.framework.gui.wings.components.SColorPicker;
import org.jspresso.framework.gui.wings.components.plaf.ClickableHeaderTableCG;
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
import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPasswordPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPercentPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.gui.FontHelper;
import org.jspresso.framework.view.AbstractViewFactory;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ELabelPosition;
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
import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SCardLayout;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDefaultListCellRenderer;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SGridBagLayout;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SInternalFrame;
import org.wings.SLabel;
import org.wings.SLayoutManager;
import org.wings.SList;
import org.wings.SMenuItem;
import org.wings.SPanel;
import org.wings.SPasswordField;
import org.wings.SPopupMenu;
import org.wings.SScrollPane;
import org.wings.SSpacer;
import org.wings.SSplitPane;
import org.wings.STabbedPane;
import org.wings.STable;
import org.wings.STextArea;
import org.wings.STextField;
import org.wings.SToolBar;
import org.wings.STree;
import org.wings.border.SBevelBorder;
import org.wings.border.SEmptyBorder;
import org.wings.border.SEtchedBorder;
import org.wings.border.SLineBorder;
import org.wings.style.CSSProperty;
import org.wings.table.SDefaultTableCellRenderer;
import org.wings.table.STableCellEditor;
import org.wings.table.STableCellRenderer;
import org.wings.table.STableColumn;
import org.wings.text.SDateFormatter;
import org.wings.tree.SDefaultTreeCellRenderer;
import org.wingx.XCalendar;

/**
 * Factory for Wings views.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultWingsViewFactory extends
    AbstractViewFactory<SComponent, SIcon, Action> {

  private IListSelectionModelBinder listSelectionModelBinder;
  private ITreeSelectionModelBinder treeSelectionModelBinder;
  private static final SFont        DEFAULT_FONT = new SFont(null, SFont.PLAIN,
                                                     SFont.DEFAULT_SIZE);

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
      SComponent component, IFormatter formatter, Object templateValue,
      int extraWidth) {
    if (viewDescriptor.getFont() != null) {
      // must set font before computing size.
      component.setFont(createFont(viewDescriptor.getFont(), component
          .getFont()));
    }
    int preferredWidth = computePixelWidth(component, getFormatLength(
        formatter, templateValue))
        + extraWidth;
    SDimension size = new SDimension(preferredWidth + "px", null);
    component.setPreferredSize(size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computePixelWidth(SComponent component, int characterLength) {
    int charLength = getMaxCharacterLength() + 2;
    if (characterLength > 0 && characterLength < getMaxCharacterLength()) {
      charLength = characterLength + 2;
    }
    int fontSize = DEFAULT_FONT.getSize();
    if (fontSize == SFont.DEFAULT_SIZE) {
      fontSize = 12;
    }
    if (component.getFont() != null) {
      fontSize = component.getFont().getSize();
    }
    return (int) ((fontSize * charLength) / 4.0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createBinaryPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IBinaryPropertyDescriptor propertyDescriptor = (IBinaryPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    SActionField viewComponent = createSActionField(false);
    SActionFieldConnector connector = new SActionFieldConnector(
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
  protected IView<SComponent> createBooleanPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IBooleanPropertyDescriptor propertyDescriptor = (IBooleanPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    SCheckBox viewComponent = createSCheckBox();
    SCheckBoxConnector connector = new SCheckBoxConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<SComponent> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SPanel viewComponent = createSPanel(new SBorderLayout());
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IMapView<SComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SCardLayout layout = new SCardLayout();
    SPanel viewComponent = createSPanel(layout);
    BasicMapView<SComponent> view = constructMapView(viewComponent,
        viewDescriptor);
    viewComponent.add(createEmptyComponent(), ICardViewDescriptor.DEFAULT_CARD);
    viewComponent.add(createSecurityComponent(),
        ICardViewDescriptor.SECURITY_CARD);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<SComponent> childView = createView(childViewDescriptor.getValue(),
          actionHandler, locale);
      viewComponent.add(childView.getPeer(), childViewDescriptor.getKey());
      view.addToChildrenMap(childViewDescriptor.getKey(), childView);
    }
    viewComponent.setPreferredSize(SDimension.FULLAREA);
    view.setConnector(createCardViewConnector(view, actionHandler, locale));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void addCard(IMapView<SComponent> cardView, IView<SComponent> card,
      String cardName) {
    ((SContainer) cardView.getPeer()).add(card.getPeer(), cardName);
    cardView.addToChildrenMap(cardName, card);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createColorPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IColorPropertyDescriptor propertyDescriptor = (IColorPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
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
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            getConnectorIdForComponentView(viewDescriptor), null);
    SPanel viewComponent = createSPanel(new SGridBagLayout());
    IView<SComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    int currentX = 0;
    int currentY = 0;

    boolean isSpaceFilled = false;
    boolean lastRowNeedsFilling = true;

    for (Iterator<IPropertyViewDescriptor> ite = viewDescriptor
        .getPropertyViewDescriptors().iterator(); ite.hasNext();) {
      IPropertyViewDescriptor propertyViewDescriptor = ite.next();
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
      IView<SComponent> propertyView = createView(propertyViewDescriptor,
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
      SLabel propertyLabel = createPropertyLabel(propertyViewDescriptor,
          propertyView.getPeer(), locale);
      if (forbidden) {
        propertyLabel.setText(" ");
      }

      int propertyWidth = propertyViewDescriptor.getWidth();
      if (propertyWidth > viewDescriptor.getColumnCount()) {
        propertyWidth = viewDescriptor.getColumnCount();
      }
      if (currentX + propertyWidth > viewDescriptor.getColumnCount()) {
        fillLastRow(viewComponent);
        currentX = 0;
        currentY++;
      }

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

      // SComponent positionning
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
        case NONE:
          constraints.gridx = currentX;
          constraints.gridy = currentY;
          constraints.insets = new Insets(0, 5, 0, 5);
          constraints.gridwidth = propertyWidth;
          break;
        default:
          break;
      }

      constraints.anchor = GridBagConstraints.WEST;
      if (isHeightExtensible(propertyViewDescriptor)) {
        constraints.weightx = 1.0D;
        constraints.weighty = 1.0D;
        constraints.fill = GridBagConstraints.BOTH;
        if (!ite.hasNext()) {
          constraints.gridwidth = GridBagConstraints.REMAINDER;
          lastRowNeedsFilling = false;
        }
        isSpaceFilled = true;
      } else {
        constraints.weightx = 0.0D;
        constraints.weighty = 0.0D;
        constraints.fill = GridBagConstraints.HORIZONTAL;
      }
      viewComponent.add(propertyView.getPeer(), constraints);

      currentX += propertyWidth;
    }
    if (lastRowNeedsFilling) {
      fillLastRow(viewComponent);
    }
    if (!isSpaceFilled) {
      SPanel filler = createSPanel(new SBorderLayout());
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridx = 0;
      constraints.weightx = 1.0D;
      constraints.weighty = 1.0D;
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
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<SComponent> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    SPanel viewComponent = createSPanel(new SGridBagLayout());
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<SComponent> childView = createView(childViewDescriptor,
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
   * Creates a date field.
   * 
   * @return the created date field.
   */
  protected XCalendar createDateField() {
    XCalendar dateField = new XCalendar();
    dateField.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return dateField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createDatePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDatePropertyDescriptor propertyDescriptor = (IDatePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    SComponent viewComponent;
    DateFormat format = createDateFormat(propertyDescriptor, locale);
    IFormatter formatter = createFormatter(format);
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createSLabel(true);
      connector = new SLabelConnector(propertyDescriptor.getName(),
          (SLabel) viewComponent);
      ((SLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createDateField();
      ((XCalendar) viewComponent).setFormatter(new SDateFormatter(format));
      connector = new XCalendarConnector(propertyDescriptor.getName(),
          (XCalendar) viewComponent);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getDateTemplateValue(propertyDescriptor), 64);
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createDecimalPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDecimalPropertyDescriptor propertyDescriptor = (IDecimalPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    SComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createSLabel(true);
      connector = new SLabelConnector(propertyDescriptor.getName(),
          (SLabel) viewComponent);
      ((SLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createSTextField();
      connector = new SFormattedFieldConnector(propertyDescriptor.getName(),
          (STextField) viewComponent, formatter);
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
  protected IView<SComponent> createDurationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDurationPropertyDescriptor propertyDescriptor = (IDurationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    SComponent viewComponent;
    IValueConnector connector;
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createSLabel(true);
      connector = new SLabelConnector(propertyDescriptor.getName(),
          (SLabel) viewComponent);
      ((SLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createSTextField();
      connector = new SFormattedFieldConnector(propertyDescriptor.getName(),
          (STextField) viewComponent, formatter);
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
  protected SComponent createEmptyComponent() {
    return createSPanel(new SBorderLayout());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createEnumerationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IEnumerationPropertyDescriptor propertyDescriptor = (IEnumerationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    SComboBox viewComponent = createSComboBox();
    if (!propertyDescriptor.isMandatory()) {
      viewComponent.addItem(null);
    }
    for (Object enumElement : propertyDescriptor.getEnumerationValues()) {
      viewComponent.addItem(enumElement);
    }
    viewComponent.setRenderer(new TranslatedEnumerationListCellRenderer(
        propertyDescriptor, locale));
    adjustSizes(propertyViewDescriptor, viewComponent, null,
        getEnumerationTemplateValue(propertyDescriptor, locale), 48);
    SComboBoxConnector connector = new SComboBoxConnector(propertyDescriptor
        .getName(), viewComponent);
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<SComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SGridLayout layout = new SGridLayout();
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
    SPanel viewComponent = createSPanel(layout);
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    SLabel imageLabel = createSLabel(false);
    imageLabel.setHorizontalAlignment(SConstants.CENTER);
    SImageConnector connector = new SImageConnector(viewDescriptor
        .getModelDescriptor().getName(), imageLabel);
    connector.setExceptionHandler(actionHandler);
    SPanel viewComponent = createSPanel(new SBorderLayout());
    IView<SComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    viewComponent.add(imageLabel, SBorderLayout.CENTER);
    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    view.setPeer(scrollPane);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createIntegerPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IIntegerPropertyDescriptor propertyDescriptor = (IIntegerPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    SComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createSLabel(true);
      connector = new SLabelConnector(propertyDescriptor.getName(),
          (SLabel) viewComponent);
      ((SLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createSTextField();
      connector = new SFormattedFieldConnector(propertyDescriptor.getName(),
          (STextField) viewComponent, formatter);
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
  protected IView<SComponent> createListView(
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
    SList viewComponent = createSList();

    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    IView<SComponent> view = constructView(scrollPane, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createListConnector(viewDescriptor
          .getRenderedProperty(), modelDescriptor.getCollectionDescriptor()
          .getElementDescriptor());
      rowConnectorPrototype.addChildConnector(cellConnector);
    }
    viewComponent.setCellRenderer(new EvenOddListCellRenderer());
    viewComponent.setModel(new CollectionConnectorListModel(connector));
    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));
    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), null);
    // TODO check how we could react on cell double clicks
    // if (viewDescriptor.getRowAction() != null) {
    // final Action rowAction = getActionFactory().createAction(
    // viewDescriptor.getRowAction(), actionHandler, view, locale);
    // viewComponent.addMouseListener(new MouseAdapter() {
    //
    // @Override
    // public void mouseClicked(MouseEvent e) {
    // if (e.getClickCount() == 2) {
    // ActionEvent ae = new ActionEvent(e.getSource(),
    // ActionEvent.ACTION_PERFORMED, null, e.getWhen(), e
    // .getModifiers());
    // rowAction.actionPerformed(ae);
    // }
    // }
    // });
    // }
    attachDefaultCollectionListener(connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createPasswordPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IPasswordPropertyDescriptor propertyDescriptor = (IPasswordPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    SPasswordField viewComponent = createSPasswordField();
    SPasswordFieldConnector connector = new SPasswordFieldConnector(
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
  protected IView<SComponent> createPercentPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IPercentPropertyDescriptor propertyDescriptor = (IPercentPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    SComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createSLabel(true);
      connector = new SLabelConnector(propertyDescriptor.getName(),
          (SLabel) viewComponent);
      ((SLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createSTextField();
      connector = new SPercentFieldConnector(propertyDescriptor.getName(),
          (STextField) viewComponent, formatter);
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
  protected IView<SComponent> createReferencePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IReferencePropertyDescriptor<?> propertyDescriptor = (IReferencePropertyDescriptor<?>) propertyViewDescriptor
        .getModelDescriptor();
    SActionField viewComponent = createSActionField(true);
    SReferenceFieldConnector connector = new SReferenceFieldConnector(
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
    Action lovAction = createLovAction(viewComponent, connector,
        propertyViewDescriptor, actionHandler, locale);
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
          getIconFactory().getTinyIconSize()));
    }
    viewComponent.setActions(Collections.singletonList(lovAction));
    adjustSizes(propertyViewDescriptor, viewComponent, null, null);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * Creates an action field.
   * 
   * @param showTextField
   *          is the text field visible to the user.
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
    button.setActionCommand("*"); // For LOV actions to avoid "1".
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
   * {@inheritDoc}
   */
  @Override
  protected SPanel createSecurityComponent() {
    SPanel panel = createSPanel(new SBorderLayout());
    // SLabel label = createSLabel();
    // label.setHorizontalAlignment(SConstants.CENTER);
    // label.setVerticalAlignment(SConstants.CENTER);
    // label.setIcon(iconFactory.getForbiddenIcon(IIconFactory.LARGE_ICON_SIZE));
    // panel.add(label, SBorderLayout.CENTER);
    return panel;
  }

  /**
   * Creates an internal frame.
   * 
   * @return the created panel.
   */
  protected SInternalFrame createSInternalFrame() {
    SInternalFrame iFrame = new SInternalFrame();
    iFrame.setPreferredSize(SDimension.FULLAREA);
    iFrame.getContentPane().setPreferredSize(SDimension.FULLAREA);
    iFrame.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    iFrame.setVerticalAlignment(SConstants.TOP_ALIGN);
    return iFrame;
  }

  /**
   * Creates a label.
   * 
   * @param bold
   *          make it bold ?
   * @return the created label.
   */
  protected SLabel createSLabel(boolean bold) {
    // To have preferred height computed.
    SLabel label = new SLabel(" ");
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
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createSourceCodePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createTextPropertyView(propertyViewDescriptor, actionHandler, locale);
  }

  /**
   * Creates a panel.
   * 
   * @param layout
   *          the layout to apply to the panel.
   * @return the created panel.
   */
  protected SPanel createSPanel(SLayoutManager layout) {
    SPanel panel = new SPanel(layout);
    panel.setPreferredSize(SDimension.FULLAREA);
    panel.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    panel.setVerticalAlignment(SConstants.TOP_ALIGN);
    return panel;
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
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<SComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    SPanel viewComponent = createSPanel(new SGridBagLayout());
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    Insets insets = new Insets(0, 0, 0, 0);
    if (viewDescriptor.getLeftTopViewDescriptor() != null) {
      IView<SComponent> leftTopView = createView(viewDescriptor
          .getLeftTopViewDescriptor(), actionHandler, locale);
      leftTopView.getPeer().setHorizontalAlignment(SConstants.LEFT_ALIGN);
      leftTopView.getPeer().setVerticalAlignment(SConstants.TOP_ALIGN);
      switch (viewDescriptor.getOrientation()) {
        case HORIZONTAL:
          double weightx = 0.0d;
          // double weightx = 0.3d;
          // if (leftTopView.getDescriptor() instanceof ITreeViewDescriptor) {
          // weightx = 0.0d;
          // }
          viewComponent.add(leftTopView.getPeer(), new GridBagConstraints(0, 0,
              1, 1, weightx, 1.0d, GridBagConstraints.NORTHWEST,
              GridBagConstraints.BOTH, insets, 0, 0));
          break;
        case VERTICAL:
          double weighty = 0.0d;
          if (leftTopView.getDescriptor() instanceof ICollectionViewDescriptor) {
            weighty = 1.0d;
          }
          viewComponent.add(leftTopView.getPeer(), new GridBagConstraints(0, 0,
              1, 1, 1.0d, weighty, GridBagConstraints.NORTHWEST,
              GridBagConstraints.BOTH, insets, 0, 0));
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
        case HORIZONTAL:
          gridx = 1;
          gridy = 0;
          break;
        case VERTICAL:
          gridx = 0;
          gridy = 1;
          break;
        default:
          break;
      }
      viewComponent.add(rightBottomView.getPeer(), new GridBagConstraints(
          gridx, gridy, GridBagConstraints.REMAINDER,
          GridBagConstraints.REMAINDER, 1.0d, 1.0d,
          GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
      childrenViews.add(rightBottomView);
    }
    view.setChildren(childrenViews);
    return view;
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
    scrollPane.setPreferredSize(SDimension.FULLAREA);
    scrollPane.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    scrollPane.setVerticalAlignment(SConstants.TOP_ALIGN);
    // TODO remove workaround asa WingS is fixed.
    scrollPane.setAttribute(CSSProperty.TABLE_LAYOUT, "fixed");
    return scrollPane;
  }

  /**
   * Creates a split pane.
   * 
   * @return the created split pane.
   */
  protected SSplitPane createSSplitPane() {
    SSplitPane splitPane = new SSplitPane();
    splitPane.setPreferredSize(SDimension.FULLAREA);
    splitPane.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    splitPane.setVerticalAlignment(SConstants.TOP_ALIGN);
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
    tabbedPane.setPreferredSize(SDimension.FULLAREA);
    tabbedPane.setVerticalAlignment(SConstants.TOP_ALIGN);
    tabbedPane.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return tabbedPane;
  }

  /**
   * Creates a table.
   * 
   * @return the created table.
   */
  protected ClickableHeaderSTable createSTable() {
    ClickableHeaderSTable table = new ClickableHeaderSTable();
    table.setVerticalAlignment(SConstants.TOP_ALIGN);
    table.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    table.setPreferredSize(SDimension.FULLWIDTH);
    table.setSelectable(true);

    table.setCG(new ClickableHeaderTableCG());
    return table;
  }

  /**
   * Creates a text area.
   * 
   * @return the created text area.
   */
  protected STextArea createSTextArea() {
    STextArea textArea = new STextArea();
    textArea.setVerticalAlignment(SConstants.TOP_ALIGN);
    textArea.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    textArea.setPreferredSize(SDimension.FULLAREA);
    textArea.setRows(20);
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
    SBoxLayout toolBarLayout = new SBoxLayout(toolBar, SConstants.LEFT_ALIGN);
    toolBar.setLayout(toolBarLayout);
    SBevelBorder toolBarBorder = new SBevelBorder(SBevelBorder.RAISED,
        new Insets(2, 2, 2, 2), 2);
    toolBarBorder.setColor(Color.LIGHT_GRAY);
    toolBar.setBorder(toolBarBorder);
    // toolBar.setPreferredSize(SDimension.FULLWIDTH);
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
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createStringPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    SComponent viewComponent;
    IValueConnector connector;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createSLabel(true);
      connector = new SLabelConnector(propertyDescriptor.getName(),
          (SLabel) viewComponent);
    } else {
      viewComponent = createSTextField();
      connector = new STextFieldConnector(propertyDescriptor.getName(),
          (STextField) viewComponent);
      adjustSizes(propertyViewDescriptor, viewComponent, null,
          getStringTemplateValue(propertyDescriptor));
    }
    connector.setExceptionHandler(actionHandler);
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
  protected STableCellRenderer createTableCellRenderer(
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createTableView(
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
    ClickableHeaderSTable viewComponent = createSTable();
    viewComponent.setBorder(new SLineBorder(Color.LIGHT_GRAY));
    if (viewDescriptor.isReadOnly()) {
      viewComponent.setEditable(false);
    }
    List<Class<?>> columnClasses = new ArrayList<Class<?>>();
    Set<String> forbiddenColumns = new HashSet<String>();
    int tableWidth = 0;
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String columnId = columnViewDescriptor.getModelDescriptor().getName();
      if (actionHandler.isAccessGranted(columnViewDescriptor)) {
        IValueConnector columnConnector = createColumnConnector(
            columnViewDescriptor, modelDescriptor.getCollectionDescriptor()
                .getElementDescriptor(), actionHandler);
        rowConnectorPrototype.addChildConnector(columnConnector);
        columnClasses.add(modelDescriptor.getCollectionDescriptor()
            .getElementDescriptor().getPropertyDescriptor(columnId)
            .getModelType());
        // already handled in createColumnConnector
        // if (columnViewDescriptor.getReadabilityGates() != null) {
        // for (IGate gate : columnViewDescriptor.getReadabilityGates()) {
        // columnConnector.addReadabilityGate(gate.clone());
        // }
        // }
        // if (columnViewDescriptor.getWritabilityGates() != null) {
        // for (IGate gate : columnViewDescriptor.getWritabilityGates()) {
        // columnConnector.addWritabilityGate(gate.clone());
        // }
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
        connector, columnConnectorKeys, columnClasses);
    tableModel.setExceptionHandler(actionHandler);

    AbstractTableSorter sorterDecorator;
    if (viewDescriptor.getSortingAction() != null) {
      sorterDecorator = new ActionTableSorter(tableModel, viewComponent,
          actionHandler, viewDescriptor.getSortingAction());
    } else {
      sorterDecorator = new TableSorter(tableModel, viewComponent);
      ((TableSorter) sorterDecorator).setColumnComparator(String.class,
          String.CASE_INSENSITIVE_ORDER);
    }

    org.jspresso.framework.util.gui.Dimension iconSize = new org.jspresso.framework.util.gui.Dimension(
        12, 12);
    sorterDecorator.setUpIcon(getIconFactory().getUpIcon(iconSize));
    sorterDecorator.setDownIcon(getIconFactory().getDownIcon(iconSize));
    viewComponent.setModel(sorterDecorator);

    viewComponent.setSelectionMode(getSelectionMode(viewDescriptor));

    listSelectionModelBinder.bindSelectionModel(connector, viewComponent
        .getSelectionModel(), sorterDecorator);

    int maxColumnSize = computePixelWidth(viewComponent,
        getMaxColumnCharacterLength());
    int columnIndex = 0;
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String propertyName = columnViewDescriptor.getModelDescriptor().getName();
      if (!forbiddenColumns.contains(propertyName)) {
        STableColumn column = viewComponent.getColumnModel().getColumn(
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

        if (!viewDescriptor.isReadOnly()) {
          IView<SComponent> editorView = createView(columnViewDescriptor,
              actionHandler, locale);
          // if (editorView.getPeer() instanceof SActionField) {
          // SActionField actionField = (SActionField) editorView.getPeer();
          // actionField.setActions(Collections.singletonList(actionField
          // .getActions().get(0)));
          // }
          if (editorView.getConnector().getParentConnector() == null) {
            editorView.getConnector().setParentConnector(connector);
          }
          column
              .setCellEditor(createTableCellEditor(editorView, actionHandler));
        }
        STableCellRenderer cellRenderer = createTableCellRenderer(
            propertyDescriptor, locale);
        if (cellRenderer != null) {
          column.setCellRenderer(cellRenderer);
        } else {
          column.setCellRenderer(new EvenOddTableCellRenderer());
        }
        int minHeaderWidth = computePixelWidth(viewComponent, columnName
            .length());
        int columnWidth;
        if (propertyDescriptor instanceof IBooleanPropertyDescriptor
            || propertyDescriptor instanceof IBinaryPropertyDescriptor) {
          columnWidth = Math.max(computePixelWidth(viewComponent, 2),
              minHeaderWidth);
        } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
          columnWidth = Math.max(computePixelWidth(viewComponent,
              getEnumerationTemplateValue(
                  (IEnumerationPropertyDescriptor) propertyDescriptor, locale)
                  .length() + 4), minHeaderWidth);
        } else {
          columnWidth = Math.max(Math.min(computePixelWidth(viewComponent,
              getFormatLength(createFormatter(propertyDescriptor, locale),
                  getTemplateValue(propertyDescriptor))), maxColumnSize),
              minHeaderWidth);
        }
        column.setWidth(columnWidth + "px");
        tableWidth += columnWidth;
      }
    }

    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    // if (viewDescriptor.getColumnViewDescriptors().size() < 4) {
    // scrollPane.setPreferredSize(new SDimension((tableWidth * 3 / 2) + "px",
    // scrollPane.getPreferredSize().getHeight()));
    // }
    IView<SComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    // TODO check how we could react on table row dbl click.
    // if (viewDescriptor.getRowAction() != null) {
    // final Action rowAction = getActionFactory().createAction(
    // viewDescriptor.getRowAction(), actionHandler, view, locale);
    // viewComponent.addMouseListener(new SMouseAdapter() {
    //        
    // @Override
    // public void mouseClicked(SMouseEvent e) {
    // if (e.getClickCount() == 2) {
    // ActionEvent ae = new ActionEvent(e.getSource(),
    // ActionEvent.ACTION_PERFORMED, null, e.getWhen(), e
    // .getModifiers());
    // rowAction.actionPerformed(ae);
    // }
    // }
    // });
    // }
    attachDefaultCollectionListener(connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<SComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    STabbedPane viewComponent = createSTabbedPane();
    BasicCompositeView<SComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<SComponent>> childrenViews = new ArrayList<IView<SComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      if (actionHandler.isAccessGranted(childViewDescriptor)) {
        IView<SComponent> childView = createView(childViewDescriptor,
            actionHandler, locale);
        SIcon childIcon = getIconFactory().getIcon(
            childViewDescriptor.getIconImageURL(),
            getIconFactory().getSmallIconSize());
        SComponent tabView = childView.getPeer();
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
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createTextPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    STextArea viewComponent = createSTextArea();
    viewComponent.setLineWrap(STextArea.VIRTUAL_WRAP);
    connector = new STextAreaConnector(propertyDescriptor.getName(),
        viewComponent);
    connector.setExceptionHandler(actionHandler);
    IView<SComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createHtmlPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IHtmlPropertyDescriptor propertyDescriptor = (IHtmlPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    SLabel viewComponent = createSLabel(true);
    viewComponent.setVerticalAlignment(SConstants.TOP);
    viewComponent.setHorizontalAlignment(SConstants.LEFT);
    connector = new SLabelConnector(propertyDescriptor.getName(), viewComponent);
    ((SLabelConnector) connector).setForceHtml(true);
    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    connector.setExceptionHandler(actionHandler);
    IView<SComponent> view = constructView(scrollPane, propertyViewDescriptor,
        connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createTimePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ITimePropertyDescriptor propertyDescriptor = (ITimePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    SComponent viewComponent;
    IFormatter formatter = createTimeFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createSLabel(true);
      connector = new SLabelConnector(propertyDescriptor.getName(),
          (SLabel) viewComponent);
      ((SLabelConnector) connector).setFormatter(formatter);
    } else {
      viewComponent = createSTextField();
      connector = new SFormattedFieldConnector(propertyDescriptor.getName(),
          (STextField) viewComponent, formatter);
      adjustSizes(propertyViewDescriptor, viewComponent, formatter,
          getTimeTemplateValue(propertyDescriptor), 64);
    }
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, propertyViewDescriptor, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<SComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {

    ICompositeValueConnector connector = createTreeViewConnector(
        viewDescriptor, actionHandler, locale);

    final STree viewComponent = createSTree();
    ConnectorHierarchyTreeModel treeModel = new ConnectorHierarchyTreeModel(
        connector);
    viewComponent.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    viewComponent.setModel(treeModel);
    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer());
    treeSelectionModelBinder.bindSelectionModel(connector, viewComponent);

    if (viewDescriptor.isExpanded()) {
      viewComponent.getModel().addTreeModelListener(new TreeModelListener() {

        public void treeStructureChanged(TreeModelEvent e) {
          expandAll(viewComponent, e.getTreePath());
        }

        public void treeNodesRemoved(
            @SuppressWarnings("unused") TreeModelEvent e) {
          // NO-OP.
        }

        public void treeNodesInserted(TreeModelEvent e) {
          expandAll(viewComponent, e.getTreePath());
        }

        public void treeNodesChanged(
            @SuppressWarnings("unused") TreeModelEvent e) {
          // NO-OP.
        }
      });
    }

    SScrollPane scrollPane = createSScrollPane();
    scrollPane.setViewportView(viewComponent);
    scrollPane.setPreferredSize(new SDimension("180px", scrollPane
        .getPreferredSize().getHeight()));
    IView<SComponent> view = constructView(scrollPane, viewDescriptor,
        connector);
    return view;
  }

  private void expandAll(final STree tree, final TreePath tp) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithActions(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale, IView<SComponent> view) {
    if (viewDescriptor.getActionMap() != null) {
      SToolBar toolBar = createSToolBar();
      for (Iterator<ActionList> iter = viewDescriptor.getActionMap()
          .getActionLists().iterator(); iter.hasNext();) {
        ActionList nextActionList = iter.next();
        for (IDisplayableAction action : nextActionList.getActions()) {
          if (actionHandler.isAccessGranted(action)) {
            Action wingsAction = getActionFactory().createAction(action,
                actionHandler, view, locale);
            SButton actionButton = createSButton();
            actionButton.setShowAsFormComponent(false);
            actionButton.setAction(wingsAction);
            actionButton.setDisabledIcon(actionButton.getIcon());
            if (action.getAcceleratorAsString() != null) {
              KeyStroke ks = KeyStroke.getKeyStroke(action
                  .getAcceleratorAsString());
              view.getPeer().getActionMap().put(
                  wingsAction.getValue(Action.NAME), wingsAction);
              view.getPeer().getInputMap(
                  SComponent.WHEN_FOCUSED_OR_ANCESTOR_OF_FOCUSED_COMPONENT)
                  .put(ks, wingsAction.getValue(Action.NAME));
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
        }
        if (iter.hasNext()) {
          toolBar.add(new SSpacer(10, 0));
        }
      }
      SPanel viewPanel = createSPanel(new SBorderLayout());
      viewPanel.add(toolBar, SBorderLayout.NORTH);
      viewPanel.add(view.getPeer(), SBorderLayout.CENTER);
      view.setPeer(viewPanel);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithBorder(IView<SComponent> view, Locale locale) {
    switch (view.getDescriptor().getBorderType()) {
      case SIMPLE:
        view.getPeer().setBorder(new SEtchedBorder());
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
      IView<SComponent> view) {
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
      Locale locale, IView<SComponent> view) {
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
  protected void showCardInPanel(SComponent cardsPeer, String cardName) {
    ((SCardLayout) ((SContainer) cardsPeer).getLayout()).show(
        (SContainer) cardsPeer, cardName);
  }

  private STableCellRenderer createBooleanTableCellRenderer(
      @SuppressWarnings("unused") IBooleanPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new BooleanTableCellRenderer();
  }

  private STableCellRenderer createCollectionTableCellRenderer(
      @SuppressWarnings("unused") ICollectionPropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return null;
  }

  private Color createColor(String colorAsHexString) {
    int[] rgba = ColorHelper.fromHexString(colorAsHexString);
    return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
  }

  private STableCellRenderer createColorTableCellRenderer(
      @SuppressWarnings("unused") IColorPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new ColorTableCellRenderer();
  }

  private STableCellRenderer createDateTableCellRenderer(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDateFormatter(
        propertyDescriptor, locale));
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

  private STableCellRenderer createDurationTableCellRenderer(
      IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createDurationFormatter(
        propertyDescriptor, locale));
  }

  private STableCellRenderer createEnumerationTableCellRenderer(
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new TranslatedEnumerationTableCellRenderer(propertyDescriptor,
        locale);
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

  private SFont createFont(String fontString, SFont defaultFont) {
    SFont actualDefaultFont;
    if (defaultFont != null) {
      actualDefaultFont = defaultFont;
    } else {
      actualDefaultFont = DEFAULT_FONT;
    }
    org.jspresso.framework.util.gui.Font font = FontHelper
        .fromString(fontString);
    int fontStyle;
    if (font.isBold() && font.isItalic()) {
      fontStyle = SFont.BOLD | SFont.ITALIC;
    } else if (font.isBold()) {
      fontStyle = SFont.BOLD;
    } else if (font.isItalic()) {
      fontStyle = SFont.ITALIC;
    } else {
      fontStyle = SFont.PLAIN;
    }
    if (font.getName() == null || font.getName().length() == 0) {
      font.setName(actualDefaultFont.getFace());
    }
    if (font.getSize() < 0) {
      font.setSize(actualDefaultFont.getSize());
    }
    return new SFont(font.getName(), fontStyle, font.getSize());
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

  private STableCellRenderer createIntegerTableCellRenderer(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createIntegerFormatter(
        propertyDescriptor, locale));
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

  private STableCellRenderer createPercentTableCellRenderer(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createPercentFormatter(
        propertyDescriptor, locale));
  }

  private SLabel createPropertyLabel(
      IPropertyViewDescriptor propertyViewDescriptor,
      @SuppressWarnings("unused") SComponent propertyComponent, Locale locale) {
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    SLabel propertyLabel = createSLabel(false);
    StringBuffer labelText = new StringBuffer(propertyDescriptor.getI18nName(
        getTranslationProvider(), locale));
    if (propertyDescriptor.isMandatory()) {
      labelText.append("*");
      propertyLabel.setForeground(Color.RED);
    }
    propertyLabel.setText(labelText.toString());
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

  private STableCellRenderer createReferenceTableCellRenderer(
      @SuppressWarnings("unused") IReferencePropertyDescriptor<?> propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return null;
  }

  private STableCellRenderer createRelationshipEndTableCellRenderer(
      IRelationshipEndPropertyDescriptor propertyDescriptor, Locale locale) {
    STableCellRenderer cellRenderer = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      cellRenderer = createReferenceTableCellRenderer(
          (IReferencePropertyDescriptor<?>) propertyDescriptor, locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
      cellRenderer = createCollectionTableCellRenderer(
          (ICollectionPropertyDescriptor<?>) propertyDescriptor, locale);
    }
    return cellRenderer;
  }

  private STableCellRenderer createStringTableCellRenderer(
      @SuppressWarnings("unused") IStringPropertyDescriptor propertyDescriptor,
      @SuppressWarnings("unused") Locale locale) {
    return new FormattedTableCellRenderer(null);
  }

  private STableCellEditor createTableCellEditor(IView<SComponent> editorView,
      IActionHandler actionHandler) {
    WingsViewCellEditorAdapter editor;
    editor = new WingsViewCellEditorAdapter(editorView,
        getModelConnectorFactory(), getMvcBinder(), actionHandler.getSubject());
    return editor;
  }

  private STableCellRenderer createTimeTableCellRenderer(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormattedTableCellRenderer(createTimeFormatter(
        propertyDescriptor, locale));
  }

  private void decorateWithTitle(IView<SComponent> view, Locale locale) {
    // SInternalFrame iFrame = createSInternalFrame();
    // iFrame.setTitle(view.getDescriptor().getI18nName(
    // getTranslationProvider(), locale));
    // iFrame.setMaximizable(false);
    // iFrame.setClosable(false);
    // iFrame.setIconifyable(true);
    // iFrame.setIcon(iconFactory.getIcon(view.getDescriptor()
    // .getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    // iFrame.getContentPane().setLayout(new SBorderLayout());
    // iFrame.getContentPane().add(view.getPeer(), SBorderLayout.CENTER);
    // iFrame.setPreferredSize(new SDimension(SDimension.AUTO,
    // WingsUtil.FULL_DIM_PERCENT));
    // view.setPeer(iFrame);

    // view.getPeer().setBorder(new
    // STitledBorder(view.getDescriptor().getI18nName(
    // getTranslationProvider(), locale)));

    SPanel titledPanel = createSPanel(new SBorderLayout());
    SLabel titleLabel = createSLabel(true);
    titleLabel.setIcon(getIconFactory().getIcon(
        view.getDescriptor().getIconImageURL(),
        getIconFactory().getTinyIconSize()));
    titleLabel.setText(view.getDescriptor().getI18nName(
        getTranslationProvider(), locale));
    titleLabel.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    titleLabel.setBorder(new SEmptyBorder(new Insets(2, 2, 6, 2)));
    titledPanel.add(titleLabel, SBorderLayout.NORTH);
    titledPanel.add(view.getPeer(), SBorderLayout.CENTER);
    titledPanel.setBorder(new SLineBorder(Color.LIGHT_GRAY, 2, new Insets(0, 0,
        2, 2)));
    view.setPeer(titledPanel);
  }

  private void fillLastRow(SPanel viewComponent) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.weightx = 1.0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    SPanel filler = createSPanel(new SBorderLayout());
    // filler.setBorder(new SLineBorder(Color.BLUE));
    viewComponent.add(filler, constraints);
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

    private static final long serialVersionUID = -5153268751092971328L;

    /**
     * {@inheritDoc}
     */
    @Override
    public SComponent getTreeCellRendererComponent(STree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row,
        boolean nodeHasFocus) {
      SLabel renderer = (SLabel) super.getTreeCellRendererComponent(tree,
          value, sel, expanded, leaf, row, nodeHasFocus);
      if (value instanceof IValueConnector) {
        if (value instanceof IRenderableCompositeValueConnector) {
          renderer.setText(((IRenderableCompositeValueConnector) value)
              .getDisplayValue());
          renderer.setIcon(getIconFactory().getIcon(
              ((IRenderableCompositeValueConnector) value)
                  .getDisplayIconImageUrl(),
              getIconFactory().getSmallIconSize()));
          if (((IRenderableCompositeValueConnector) value)
              .getDisplayDescription() != null) {
            // SToolTipManager.sharedInstance().registerComponent(tree);
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

  private final class TranslatedEnumerationListCellRenderer extends
      SDefaultListCellRenderer {

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
    public SComponent getListCellRendererComponent(SComponent list,
        Object value, boolean isSelected, int index) {
      SLabel label = (SLabel) super.getListCellRendererComponent(list, value,
          isSelected, index);
      label.setIcon(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(String.valueOf(value)),
          getIconFactory().getTinyIconSize()));
      if (value != null && propertyDescriptor.isTranslated()) {
        setText(getTranslationProvider().getTranslation(
            computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                value), locale));
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
    public SComponent getTableCellRendererComponent(STable table, Object value,
        boolean isSelected, int row, int column) {
      SLabel renderer = (SLabel) super.getTableCellRendererComponent(table,
          value, isSelected, row, column);
      renderer.setIcon(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(String.valueOf(value)),
          getIconFactory().getTinyIconSize()));
      if (value instanceof IValueConnector) {
        Object connectorValue = ((IValueConnector) value).getConnectorValue();
        if (connectorValue != null && propertyDescriptor.isTranslated()) {
          renderer.setText(getTranslationProvider().getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  connectorValue), locale));
        } else {
          renderer.setText(String.valueOf(connectorValue));
        }
      } else {
        if (value != null && propertyDescriptor.isTranslated()) {
          renderer.setText(getTranslationProvider().getTranslation(
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
