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
package org.jspresso.framework.view.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionField;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.gui.remote.RBorderContainer;
import org.jspresso.framework.gui.remote.RCardContainer;
import org.jspresso.framework.gui.remote.RCheckBox;
import org.jspresso.framework.gui.remote.RColorField;
import org.jspresso.framework.gui.remote.RComboBox;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RConstrainedGridContainer;
import org.jspresso.framework.gui.remote.RDateField;
import org.jspresso.framework.gui.remote.RDecimalComponent;
import org.jspresso.framework.gui.remote.RDecimalField;
import org.jspresso.framework.gui.remote.RDurationField;
import org.jspresso.framework.gui.remote.REvenGridContainer;
import org.jspresso.framework.gui.remote.RForm;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.gui.remote.RImageComponent;
import org.jspresso.framework.gui.remote.RIntegerField;
import org.jspresso.framework.gui.remote.RLabel;
import org.jspresso.framework.gui.remote.RList;
import org.jspresso.framework.gui.remote.RNumericComponent;
import org.jspresso.framework.gui.remote.RPasswordField;
import org.jspresso.framework.gui.remote.RPercentField;
import org.jspresso.framework.gui.remote.RSecurityComponent;
import org.jspresso.framework.gui.remote.RSplitContainer;
import org.jspresso.framework.gui.remote.RTabContainer;
import org.jspresso.framework.gui.remote.RTable;
import org.jspresso.framework.gui.remote.RTextArea;
import org.jspresso.framework.gui.remote.RTextComponent;
import org.jspresso.framework.gui.remote.RTextField;
import org.jspresso.framework.gui.remote.RTimeField;
import org.jspresso.framework.gui.remote.RTree;
import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IColorPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDurationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPasswordPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPercentPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteValueStateFactory;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.AbstractViewFactory;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
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

/**
 * Factory for remote views.
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
 * @version $LastChangedRevision: 1463 $
 * @author Vincent Vandenschrick
 */
public class DefaultRemoteViewFactory extends
    AbstractViewFactory<RComponent, RIcon, RAction> {

  private boolean               dateServerParse;

  private boolean               durationServerParse;
  private IGUIDGenerator        guidGenerator;
  private boolean               numberServerParse;

  private IRemoteCommandHandler remoteCommandHandler;

  /**
   * Constructs a new <code>DefaultRemoteViewFactory</code> instance.
   */
  public DefaultRemoteViewFactory() {
    durationServerParse = false;
    dateServerParse = false;
    numberServerParse = false;
  }

  /**
   * Computes the component state based on its bound connectorr.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IView<RComponent> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = super.createView(viewDescriptor, actionHandler,
        locale);
    view.getPeer().setState(
        ((IRemoteStateOwner) view.getConnector()).getState());
    return view;
  }

  /**
   * Sets the dateServerParse.
   * 
   * @param dateServerParse
   *          the dateServerParse to set.
   */
  public void setDateServerParse(boolean dateServerParse) {
    this.dateServerParse = dateServerParse;
  }

  /**
   * Sets the durationServerParse.
   * 
   * @param durationServerParse
   *          the durationServerParse to set.
   */
  public void setDurationServerParse(boolean durationServerParse) {
    this.durationServerParse = durationServerParse;
  }

  /**
   * Sets the guidGenerator.
   * 
   * @param guidGenerator
   *          the guidGenerator to set.
   */
  public void setGuidGenerator(IGUIDGenerator guidGenerator) {
    this.guidGenerator = guidGenerator;
  }

  /**
   * Sets the numberServerParse.
   * 
   * @param numberServerParse
   *          the numberServerParse to set.
   */
  public void setNumberServerParse(boolean numberServerParse) {
    this.numberServerParse = numberServerParse;
  }

  /**
   * Sets the remoteCommandHandler.
   * 
   * @param remoteCommandHandler
   *          the remoteCommandHandler to set.
   */
  public void setRemoteCommandHandler(IRemoteCommandHandler remoteCommandHandler) {
    this.remoteCommandHandler = remoteCommandHandler;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void adjustSizes(@SuppressWarnings("unused") RComponent component,
      @SuppressWarnings("unused") IFormatter formatter,
      @SuppressWarnings("unused") Object templateValue,
      @SuppressWarnings("unused") int extraWidth) {
    // Empty as of now.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computePixelWidth(
      @SuppressWarnings("unused") RComponent component,
      @SuppressWarnings("unused") int characterLength) {
    // Empty as of now.
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createBinaryPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IBinaryPropertyDescriptor propertyDescriptor = (IBinaryPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RActionField viewComponent = createRActionField(false, connector);
    IView<RComponent> view = constructView(viewComponent, null, connector);
    RActionList actionList = new RActionList(guidGenerator.generateGUID());
    actionList.setActions(createBinaryActions(viewComponent, connector,
        propertyDescriptor, actionHandler, locale).toArray(new RAction[0]));
    viewComponent.setActionLists(new RActionList[] {actionList});
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createBooleanPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IBooleanPropertyDescriptor propertyDescriptor = (IBooleanPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RCheckBox viewComponent = createRCheckBox(connector);
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    RBorderContainer viewComponent = createRBorderContainer();
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<IView<RComponent>>();

    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<RComponent> eastView = createView(viewDescriptor
          .getEastViewDescriptor(), actionHandler, locale);
      viewComponent.setEast(eastView.getPeer());
      childrenViews.add(eastView);
    }
    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<RComponent> northView = createView(viewDescriptor
          .getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.setNorth(northView.getPeer());
      childrenViews.add(northView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<RComponent> centerView = createView(viewDescriptor
          .getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.setCenter(centerView.getPeer());
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<RComponent> westView = createView(viewDescriptor
          .getWestViewDescriptor(), actionHandler, locale);
      viewComponent.setWest(westView.getPeer());
      childrenViews.add(westView);
    }
    if (viewDescriptor.getSouthViewDescriptor() != null) {
      IView<RComponent> southView = createView(viewDescriptor
          .getSouthViewDescriptor(), actionHandler, locale);
      viewComponent.setSouth(southView.getPeer());
      childrenViews.add(southView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    RCardContainer viewComponent = createRCardContainer();
    List<String> cardNames = new ArrayList<String>();
    List<RComponent> cards = new ArrayList<RComponent>();
    cardNames.add(ICardViewDescriptor.DEFAULT_CARD);
    cards.add(createEmptyComponent());
    cardNames.add(ICardViewDescriptor.SECURITY_CARD);
    cards.add(createSecurityComponent());

    BasicMapView<RComponent> view = constructMapView(viewComponent,
        viewDescriptor);
    Map<String, IView<RComponent>> childrenViews = new HashMap<String, IView<RComponent>>();

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<RComponent> childView = createView(childViewDescriptor.getValue(),
          actionHandler, locale);
      childrenViews.put(childViewDescriptor.getKey(), childView);
      cardNames.add(childViewDescriptor.getKey());
      cards.add(childView.getPeer());
    }
    viewComponent.setCardNames(cardNames.toArray(new String[0]));
    viewComponent.setCards(cards.toArray(new RComponent[0]));
    view.setChildrenMap(childrenViews);
    view.setConnector(createCardViewConnector(view, actionHandler));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createColorPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IColorPropertyDescriptor propertyDescriptor = (IColorPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RColorField viewComponent = createRColorField(connector);
    viewComponent
        .setDefaultColor((String) propertyDescriptor.getDefaultValue());
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            getConnectorIdForComponentView(viewDescriptor), null);
    RForm viewComponent = createRForm();
    viewComponent.setColumnCount(viewDescriptor.getColumnCount());
    viewComponent.setLabelsPosition(viewDescriptor.getLabelsPosition()
        .toString());

    List<Integer> elementWidths = new ArrayList<Integer>();
    List<RComponent> elements = new ArrayList<RComponent>();

    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

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
      IView<RComponent> propertyView = createPropertyView(propertyViewDescriptor,
          actionHandler,
          locale);
      try {
        actionHandler.checkAccess(propertyViewDescriptor);
      } catch (SecurityException ex) {
        propertyView.setPeer(createSecurityComponent());
      }
      elements.add(propertyView.getPeer());
      elementWidths.add(new Integer(propertyViewDescriptor.getWidth()));
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
    }
    viewComponent.setElementWidths(elementWidths.toArray(new Integer[0]));
    viewComponent.setElements(elements.toArray(new RComponent[0]));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    RConstrainedGridContainer viewComponent = createRConstrainedGridContainer();
    List<RComponent> cells = new ArrayList<RComponent>();
    List<CellConstraints> cellConstraints = new ArrayList<CellConstraints>();
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<IView<RComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<RComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      cellConstraints.add(viewDescriptor
          .getCellConstraints(childViewDescriptor));
      cells.add(childView.getPeer());
      childrenViews.add(childView);
    }
    viewComponent.setCells(cells.toArray(new RComponent[0]));
    viewComponent.setCellConstraints(cellConstraints
        .toArray(new CellConstraints[0]));
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDatePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IValueConnector connector;
    IDatePropertyDescriptor propertyDescriptor = (IDatePropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    if (isDateServerParse()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(),
          createDateFormatter(propertyDescriptor, locale));
    } else {
      connector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
    }
    connector.setExceptionHandler(actionHandler);
    RDateField viewComponent = createRDateField(connector);
    viewComponent.setType(propertyDescriptor.getType().toString());
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDecimalPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDecimalPropertyDescriptor propertyDescriptor = (IDecimalPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IView<RComponent> view;
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      view = createPercentPropertyView(
          propertyViewDescriptor, actionHandler,
          locale);
    } else {
      IValueConnector connector;
      if (isNumberServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(),
            createDecimalFormatter(propertyDescriptor, locale));
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      connector.setExceptionHandler(actionHandler);
      RDecimalField viewComponent = createRDecimalField(connector);
      view = constructView(viewComponent, null, connector);
    }
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      ((RDecimalComponent) view.getPeer())
          .setMaxFractionDigit(propertyDescriptor.getMaxFractionDigit()
              .intValue());
    } else {
      ((RDecimalComponent) view.getPeer()).setMaxFractionDigit(2);
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDurationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDurationPropertyDescriptor propertyDescriptor = (IDurationPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector;
    if (isDurationServerParse()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(),
          createDurationFormatter(propertyDescriptor, locale));
    } else {
      connector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
    }
    connector.setExceptionHandler(actionHandler);
    RDurationField viewComponent = createRDurationField(connector);
    if (propertyDescriptor.getMaxMillis() != null) {
      viewComponent.setMaxMillis(propertyDescriptor.getMaxMillis().longValue());
    } else {
      viewComponent.setMaxMillis(-1);
    }
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent createEmptyComponent() {
    RComponent emptyComponent = createRBorderContainer();
    emptyComponent.setState(new RemoteCompositeValueState(guidGenerator
        .generateGUID()));
    return emptyComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createEnumerationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IEnumerationPropertyDescriptor propertyDescriptor = (IEnumerationPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RComboBox viewComponent = createRComboBox(connector);
    List<String> values = new ArrayList<String>();
    List<String> translations = new ArrayList<String>();
    List<RIcon> icons = new ArrayList<RIcon>();
    IView<RComponent> view = constructView(viewComponent, null, connector);
    for (String value : propertyDescriptor.getEnumerationValues()) {
      if (value != null && propertyDescriptor.isTranslated()) {
        values.add(value);
        translations.add(getTranslationProvider().getTranslation(
            computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                value), locale));
        icons.add(getIconFactory().getIcon(
            propertyDescriptor.getIconImageURL(value),
            IIconFactory.TINY_ICON_SIZE));
      }
    }
    viewComponent.setValues(values.toArray(new String[0]));
    viewComponent.setTranslations(translations.toArray(new String[0]));
    viewComponent.setIcons(icons.toArray(new RIcon[0]));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    REvenGridContainer viewComponent = createREvenGridContainer();
    viewComponent.setDrivingDimension(viewDescriptor.getDrivingDimension()
        .toString());
    viewComponent.setDrivingDimensionCellCount(viewDescriptor
        .getDrivingDimensionCellCount());
    List<RComponent> cells = new ArrayList<RComponent>();
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<IView<RComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<RComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      cells.add(childView.getPeer());
      childrenViews.add(childView);
    }
    viewComponent.setCells(cells.toArray(new RComponent[0]));
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    IValueConnector connector = getConnectorFactory().createValueConnector(
        viewDescriptor.getModelDescriptor().getName());
    connector.setExceptionHandler(actionHandler);
    RImageComponent viewComponent = createRImageComponent(connector);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createIntegerPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IIntegerPropertyDescriptor propertyDescriptor = (IIntegerPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector;
    if (isNumberServerParse()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(),
          createIntegerFormatter(propertyDescriptor, locale));
    } else {
      connector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
    }
    connector.setExceptionHandler(actionHandler);
    RIntegerField viewComponent = createRIntegerField(connector);
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createListView(
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
    RList viewComponent = createRList(connector);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createColumnConnector(viewDescriptor
          .getRenderedProperty(), modelDescriptor.getCollectionDescriptor()
          .getElementDescriptor());
      rowConnectorPrototype.addChildConnector(cellConnector);
    }
    viewComponent
        .setSelectionMode(viewDescriptor.getSelectionMode().toString());
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            viewDescriptor.getModelDescriptor().getName(), null);

    RBorderContainer viewComponent = createRBorderContainer();

    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    IView<RComponent> nestedView = createView(viewDescriptor
        .getNestedViewDescriptor(), actionHandler, locale);

    connector.addChildConnector(nestedView.getConnector());

    viewComponent.setCenter(nestedView.getPeer());

    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createNumberPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    INumberPropertyDescriptor propertyDescriptor = (INumberPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IView<RComponent> view = super.createNumberPropertyView(propertyViewDescriptor,
        actionHandler, locale);
    ((RNumericComponent) view.getPeer()).setMaxValue(propertyDescriptor
        .getMaxValue());
    ((RNumericComponent) view.getPeer()).setMinValue(propertyDescriptor
        .getMinValue());
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createPasswordPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IPasswordPropertyDescriptor propertyDescriptor = (IPasswordPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RPasswordField viewComponent = createRPasswordField(connector);
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createPercentPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IPercentPropertyDescriptor propertyDescriptor = (IPercentPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector;
    if (isNumberServerParse()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(),
          createPercentFormatter(propertyDescriptor, locale));
    } else {
      connector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
    }
    connector.setExceptionHandler(actionHandler);
    RPercentField viewComponent = createRPercentField(connector);
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * Override to set the property view name that will be useful on client-side.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler,
      Locale locale) {
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IView<RComponent> view = super.createPropertyView(propertyViewDescriptor,
        actionHandler, locale);
    if (view != null) {
      if (propertyDescriptor.getName() != null) {
        view.getPeer().setLabel(
            propertyDescriptor.getI18nName(getTranslationProvider(), locale));
      }
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createReferencePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IReferencePropertyDescriptor<?> propertyDescriptor = (IReferencePropertyDescriptor<?>) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(propertyDescriptor.getName(),
            propertyDescriptor.getReferencedDescriptor().getToStringProperty());
    connector.setExceptionHandler(actionHandler);
    RActionField viewComponent = createRActionField(true, connector);
    IView<RComponent> view = constructView(viewComponent, null, connector);
    RAction lovAction = createLovAction(view.getPeer(), view.getConnector(),
        propertyDescriptor, actionHandler, locale);
    lovAction.setName(getTranslationProvider().getTranslation(
        "lov.element.name",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            getTranslationProvider(), locale)}, locale));
    lovAction.setDescription(getTranslationProvider().getTranslation(
        "lov.element.description",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            getTranslationProvider(), locale)}, locale));
    if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
      lovAction.setIcon(getIconFactory().getIcon(
          propertyDescriptor.getReferencedDescriptor().getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }
    RActionList actionList = new RActionList(guidGenerator.generateGUID());
    actionList.setActions(new RAction[] {lovAction});
    viewComponent.setActionLists(new RActionList[] {actionList});
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent createSecurityComponent() {
    RComponent securityComponent = new RSecurityComponent(guidGenerator
        .generateGUID());
    securityComponent.setState(new RemoteValueState(guidGenerator
        .generateGUID()));
    return securityComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createSourceCodePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createTextPropertyView(propertyViewDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    RSplitContainer viewComponent = createRSplitContainer();
    viewComponent.setOrientation(viewDescriptor.getOrientation().toString());
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<IView<RComponent>>();

    if (viewDescriptor.getLeftTopViewDescriptor() != null) {
      IView<RComponent> leftTopView = createView(viewDescriptor
          .getLeftTopViewDescriptor(), actionHandler, locale);
      viewComponent.setLeftTop(leftTopView.getPeer());
      childrenViews.add(leftTopView);
    }
    if (viewDescriptor.getRightBottomViewDescriptor() != null) {
      IView<RComponent> rightBottomView = createView(viewDescriptor
          .getRightBottomViewDescriptor(), actionHandler, locale);
      viewComponent.setRightBottom(rightBottomView.getPeer());
      childrenViews.add(rightBottomView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createStringPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createRLabel(connector);
    } else {
      viewComponent = createRTextField(connector);
    }
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTableView(
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
    RTable viewComponent = createRTable(connector);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    List<RComponent> columns = new ArrayList<RComponent>();
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      try {
        actionHandler.checkAccess(columnViewDescriptor);
        IView<RComponent> column = createPropertyView(columnViewDescriptor,
            actionHandler, locale);
        // Do not use standard createColumnConnector method to preserve
        // formatted value connectors.
        // IValueConnector columnConnector = createColumnConnector(columnId,
        // modelDescriptor.getCollectionDescriptor().getElementDescriptor());
        IValueConnector columnConnector = column.getConnector();
        rowConnectorPrototype.addChildConnector(columnConnector);
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
        columns.add(column.getPeer());
      } catch (SecurityException ex) {
        // The column simply won't be added.
      }
    }
    viewComponent.setColumns(columns.toArray(new RComponent[0]));
    viewComponent
        .setSelectionMode(viewDescriptor.getSelectionMode().toString());
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    RTabContainer viewComponent = createRTabContainer();
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    List<RComponent> tabs = new ArrayList<RComponent>();
    List<IView<RComponent>> childrenViews = new ArrayList<IView<RComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      IView<RComponent> childView = createView(childViewDescriptor,
          actionHandler, locale);
      tabs.add(childView.getPeer());
      childrenViews.add(childView);
    }
    viewComponent.setTabs(tabs.toArray(new RComponent[0]));
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTextPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      viewComponent = createRLabel(connector);
      ((RLabel) viewComponent).setMultiLine(true);
    } else {
      viewComponent = createRTextArea(connector);
    }
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTextualPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IView<RComponent> view = super.createTextualPropertyView(
        propertyViewDescriptor, actionHandler, locale);
    if (propertyDescriptor.getMaxLength() != null) {
      ((RTextComponent) view.getPeer()).setMaxLength(propertyDescriptor
          .getMaxLength().intValue());
    } else {
      ((RTextComponent) view.getPeer()).setMaxLength(-1);
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTimePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ITimePropertyDescriptor propertyDescriptor = (ITimePropertyDescriptor) propertyViewDescriptor
    .getModelDescriptor();
    IValueConnector connector;
    if (isDateServerParse()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(),
          createTimeFormatter(propertyDescriptor, locale));
    } else {
      connector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
    }
    connector.setExceptionHandler(actionHandler);
    RTimeField viewComponent = createRTimeField(connector);
    IView<RComponent> view = constructView(viewComponent, null, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor,
      @SuppressWarnings("unused") IActionHandler actionHandler, Locale locale) {
    ICompositeValueConnector connector = createTreeViewConnector(
        viewDescriptor, locale);

    RTree viewComponent = createRTree(connector);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithActions(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale, IView<RComponent> view) {
    if (viewDescriptor.getActionMap() != null) {
      List<RActionList> viewActionLists = new ArrayList<RActionList>();
      for (Iterator<ActionList> iter = viewDescriptor.getActionMap()
          .getActionLists().iterator(); iter.hasNext();) {
        ActionList nextActionList = iter.next();
        RActionList actionList = new RActionList(guidGenerator.generateGUID());
        actionList.setName(nextActionList.getName());
        actionList.setDescription(nextActionList.getDescription());
        actionList.setIcon(getIconFactory().getIcon(
            nextActionList.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
        viewActionLists.add(actionList);
        List<RAction> actions = new ArrayList<RAction>();
        for (IDisplayableAction action : nextActionList.getActions()) {
          RAction rAction = getActionFactory().createAction(action,
              actionHandler, view, locale);
          rAction.setAcceleratorAsString(action.getAcceleratorAsString());
          actions.add(rAction);
        }
        actionList.setActions(actions.toArray(new RAction[0]));
      }
      view.getPeer()
          .setActionLists(viewActionLists.toArray(new RActionList[0]));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithBorder(IView<RComponent> view,
      @SuppressWarnings("unused") Locale locale) {
    view.getPeer().setBorderType(
        view.getDescriptor().getBorderType().toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithDescription(
      IPropertyDescriptor propertyDescriptor, Locale locale,
      IView<RComponent> view) {
    if (view != null && propertyDescriptor.getDescription() != null) {
      view.getPeer().setTooltip(
          propertyDescriptor.getI18nDescription(getTranslationProvider(),
              locale));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finishComponentConfiguration(IViewDescriptor viewDescriptor,
      Locale locale, IView<RComponent> view) {
    view.getPeer().setLabel(
        view.getDescriptor().getI18nName(getTranslationProvider(), locale));
    if (viewDescriptor.getDescription() != null) {
      view.getPeer().setTooltip(
          viewDescriptor.getI18nDescription(getTranslationProvider(), locale));
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
    if (viewDescriptor.getIconImageURL() != null) {
      view.getPeer().setIcon(
          getIconFactory().getIcon(viewDescriptor.getIconImageURL(),
              IIconFactory.SMALL_ICON_SIZE));
    }
  }

  /**
   * Gets the dateServerParse.
   * 
   * @return the dateServerParse.
   */
  protected boolean isDateServerParse() {
    return dateServerParse;
  }

  /**
   * Gets the durationServerParse.
   * 
   * @return the durationServerParse.
   */
  protected boolean isDurationServerParse() {
    return durationServerParse;
  }

  /**
   * Gets the numberServerParse.
   * 
   * @return the numberServerParse.
   */
  protected boolean isNumberServerParse() {
    return numberServerParse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void showCardInPanel(RComponent cardsPeer, String cardName) {
    RemoteValueState cardState = ((RCardContainer) cardsPeer).getState();
    cardState.setValue(cardName);

    RemoteValueCommand command = new RemoteValueCommand();
    command.setTargetPeerGuid(cardState.getGuid());
    command.setValue(cardState.getValue());
    remoteCommandHandler.registerCommand(command);
  }

  private RActionField createRActionField(boolean showTextField,
      IValueConnector connector) {
    RActionField component = new RActionField(guidGenerator.generateGUID());
    component.setShowTextField(showTextField);
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RBorderContainer createRBorderContainer() {
    return new RBorderContainer(guidGenerator.generateGUID());
  }

  private RCardContainer createRCardContainer() {
    RCardContainer cardContainer = new RCardContainer(guidGenerator
        .generateGUID());
    cardContainer.setState(((IRemoteValueStateFactory) getConnectorFactory())
        .createRemoteValueState(guidGenerator.generateGUID()));
    return cardContainer;
  }

  private RCheckBox createRCheckBox(IValueConnector connector) {
    RCheckBox component = new RCheckBox(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RColorField createRColorField(IValueConnector connector) {
    RColorField component = new RColorField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RComboBox createRComboBox(IValueConnector connector) {
    RComboBox component = new RComboBox(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RConstrainedGridContainer createRConstrainedGridContainer() {
    return new RConstrainedGridContainer(guidGenerator.generateGUID());
  }

  private RDateField createRDateField(IValueConnector connector) {
    RDateField component = new RDateField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RDecimalField createRDecimalField(IValueConnector connector) {
    RDecimalField component = new RDecimalField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RDurationField createRDurationField(IValueConnector connector) {
    RDurationField component = new RDurationField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private REvenGridContainer createREvenGridContainer() {
    return new REvenGridContainer(guidGenerator.generateGUID());
  }

  private RForm createRForm() {
    return new RForm(guidGenerator.generateGUID());
  }

  private RImageComponent createRImageComponent(IValueConnector connector) {
    RImageComponent component = new RImageComponent(guidGenerator
        .generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RIntegerField createRIntegerField(IValueConnector connector) {
    RIntegerField component = new RIntegerField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RList createRList(ICollectionConnector connector) {
    RList component = new RList(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RPasswordField createRPasswordField(IValueConnector connector) {
    RPasswordField component = new RPasswordField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RPercentField createRPercentField(IValueConnector connector) {
    RPercentField component = new RPercentField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RSplitContainer createRSplitContainer() {
    return new RSplitContainer(guidGenerator.generateGUID());
  }

  private RTabContainer createRTabContainer() {
    return new RTabContainer(guidGenerator.generateGUID());
  }

  private RTable createRTable(IValueConnector connector) {
    RTable component = new RTable(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RTextArea createRTextArea(IValueConnector connector) {
    RTextArea component = new RTextArea(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RLabel createRLabel(IValueConnector connector) {
    RLabel component = new RLabel(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RTextField createRTextField(IValueConnector connector) {
    RTextField component = new RTextField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RTimeField createRTimeField(IValueConnector connector) {
    RTimeField component = new RTimeField(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }

  private RTree createRTree(IValueConnector connector) {
    RTree component = new RTree(guidGenerator.generateGUID());
    if (connector instanceof IRemoteStateOwner) {
      component.setState(((IRemoteStateOwner) connector).getState());
    }
    return component;
  }
}
