/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.binding.remote.RemoteValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionComponent;
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
import org.jspresso.framework.gui.remote.RHtmlArea;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.gui.remote.RImageComponent;
import org.jspresso.framework.gui.remote.RIntegerField;
import org.jspresso.framework.gui.remote.RLabel;
import org.jspresso.framework.gui.remote.RLink;
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
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
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
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.server.remote.RemotePeerRegistryServlet;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteStateValueMapper;
import org.jspresso.framework.state.remote.IRemoteValueStateFactory;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IAutomationSource;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.gui.Font;
import org.jspresso.framework.util.gui.FontHelper;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.AbstractViewFactory;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicIndexedView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.EHorizontalAlignment;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
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

/**
 * Factory for remote views.
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
  private IRemotePeerRegistry   remotePeerRegistry;

  /**
   * Constructs a new <code>DefaultRemoteViewFactory</code> instance.
   */
  public DefaultRemoteViewFactory() {
    durationServerParse = false;
    dateServerParse = false;
    numberServerParse = false;
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
  protected void addCard(IMapView<RComponent> cardView, IView<RComponent> card,
      String cardName) {
    cardView.addToChildrenMap(cardName, card);

    RCardContainer cardContainer = (RCardContainer) cardView.getPeer();

    RComponent[] newCards = new RComponent[cardContainer.getCards().length + 1];
    for (int i = 0; i < cardContainer.getCards().length; i++) {
      newCards[i] = cardContainer.getCards()[i];
    }
    newCards[newCards.length - 1] = card.getPeer();
    cardContainer.setCards(newCards);

    String[] newCardNames = new String[cardContainer.getCardNames().length + 1];
    for (int i = 0; i < cardContainer.getCardNames().length; i++) {
      newCardNames[i] = cardContainer.getCardNames()[i];
    }
    newCardNames[newCardNames.length - 1] = cardName;
    cardContainer.setCardNames(newCardNames);

    RemoteAddCardCommand command = new RemoteAddCardCommand();
    command.setTargetPeerGuid(cardContainer.getGuid());
    command.setCard(card.getPeer());
    command.setCardName(cardName);

    getRemoteCommandHandler().registerCommand(command);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void adjustSizes(
      @SuppressWarnings("unused") IViewDescriptor viewDescriptor,
      @SuppressWarnings("unused") RComponent component,
      @SuppressWarnings("unused") IFormatter formatter,
      @SuppressWarnings("unused") Object templateValue,
      @SuppressWarnings("unused") int extraWidth) {
    // Empty as of now.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyPreferredSize(RComponent component,
      Dimension preferredSize) {
    if (preferredSize != null) {
      component.setPreferredSize(preferredSize);
    }
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
  protected IView<RComponent> createActionView(
      IActionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IValueConnector connector = getConnectorFactory().createValueConnector(
        ModelRefPropertyConnector.THIS_PROPERTY);
    connector.setExceptionHandler(actionHandler);
    RActionComponent viewComponent = createRActionComponent(connector);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    RAction action = getActionFactory().createAction(
        viewDescriptor.getAction(), viewDescriptor.getPreferredSize(),
        actionHandler, view, locale);
    switch (viewDescriptor.getRenderingOptions()) {
      case ICON:
        action.setName(null);
        break;
      case LABEL:
        action.setIcon(null);
        break;
      default:
        break;
    }
    viewComponent.setAction(action);
    return view;
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
    if (connector instanceof RemoteValueConnector) {
      final RemoteValueConnector rConnector = (RemoteValueConnector) connector;
      rConnector.setRemoteStateValueMapper(new IRemoteStateValueMapper() {

        public Object getValueForState(Object originalValue) {
          if (originalValue instanceof byte[]) {
            String valueForStateUrl = RemotePeerRegistryServlet
                .computeDownloadUrl(rConnector.getGuid());
            Checksum checksumEngine = new CRC32();
            checksumEngine.update((byte[]) originalValue, 0,
                ((byte[]) originalValue).length);
            // we must add a check sum so that the client nows when the url
            // content
            // changes.
            valueForStateUrl += ("&cs=" + checksumEngine.getValue());
            return valueForStateUrl;
          }
          return originalValue;
        }
      });
    }
    connector.setExceptionHandler(actionHandler);
    RActionField viewComponent = createRActionField(false, connector);
    IView<RComponent> propertyView = constructView(viewComponent,
        propertyViewDescriptor, connector);
    RActionList actionList = new RActionList(getGuidGenerator().generateGUID());
    List<RAction> binaryActions = createBinaryActions(propertyView,
        actionHandler, locale);
    actionList.setActions(binaryActions.toArray(new RAction[0]));
    viewComponent.setActionLists(new RActionList[] {
      actionList
    });
    return propertyView;
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
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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

    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<RComponent> northView = createView(
          viewDescriptor.getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.setNorth(northView.getPeer());
      childrenViews.add(northView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<RComponent> westView = createView(
          viewDescriptor.getWestViewDescriptor(), actionHandler, locale);
      viewComponent.setWest(westView.getPeer());
      childrenViews.add(westView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<RComponent> centerView = createView(
          viewDescriptor.getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.setCenter(centerView.getPeer());
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<RComponent> eastView = createView(
          viewDescriptor.getEastViewDescriptor(), actionHandler, locale);
      viewComponent.setEast(eastView.getPeer());
      childrenViews.add(eastView);
    }
    if (viewDescriptor.getSouthViewDescriptor() != null) {
      IView<RComponent> southView = createView(
          viewDescriptor.getSouthViewDescriptor(), actionHandler, locale);
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
    RCardContainer viewComponent = createRCardContainer(viewDescriptor);
    List<String> cardNames = new ArrayList<String>();
    List<RComponent> cards = new ArrayList<RComponent>();
    cardNames.add(ICardViewDescriptor.DEFAULT_CARD);
    cards.add(createEmptyComponent());
    cardNames.add(ICardViewDescriptor.SECURITY_CARD);
    cards.add(createSecurityComponent());

    BasicMapView<RComponent> view = constructMapView(viewComponent,
        viewDescriptor);

    for (Map.Entry<String, IViewDescriptor> childViewDescriptor : viewDescriptor
        .getCardViewDescriptors().entrySet()) {
      IView<RComponent> childView = createView(childViewDescriptor.getValue(),
          actionHandler, locale);
      cardNames.add(childViewDescriptor.getKey());
      cards.add(childView.getPeer());
      view.addToChildrenMap(childViewDescriptor.getKey(), childView);
    }
    viewComponent.setCardNames(cardNames.toArray(new String[0]));
    viewComponent.setCards(cards.toArray(new RComponent[0]));
    view.setConnector(createCardViewConnector(view, actionHandler, locale));
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
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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
            getConnectorIdForBeanView(viewDescriptor), null);
    RForm viewComponent = createRForm();
    viewComponent.setColumnCount(viewDescriptor.getColumnCount());
    viewComponent.setLabelsPosition(viewDescriptor.getLabelsPosition()
        .toString());

    List<Integer> elementWidths = new ArrayList<Integer>();
    List<RComponent> elements = new ArrayList<RComponent>();
    List<RComponent> elementLabels = new ArrayList<RComponent>();

    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

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
      IView<RComponent> propertyView = createView(propertyViewDescriptor,
          actionHandler, locale);
      if (!actionHandler.isAccessGranted(propertyViewDescriptor)) {
        propertyView.setPeer(createSecurityComponent());
      }
      elements.add(propertyView.getPeer());
      elementLabels.add(createPropertyLabel(propertyViewDescriptor,
          propertyView.getPeer(), locale));
      elementWidths.add(propertyViewDescriptor.getWidth());
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
      if (propertyView.getPeer() instanceof RLink) {
        ((RLink) propertyView.getPeer()).setAction(getActionFactory()
            .createAction(propertyViewDescriptor.getAction(), actionHandler,
                view, locale));
      }
    }
    viewComponent.setElementWidths(elementWidths.toArray(new Integer[0]));
    viewComponent.setElements(elements.toArray(new RComponent[0]));
    viewComponent.setElementLabels(elementLabels.toArray(new RComponent[0]));
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
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDatePropertyDescriptor propertyDescriptor = (IDatePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter formatter = createDateFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      if (isDateServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(),
            createDateFormatter(propertyDescriptor, locale));
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      viewComponent = createRDateField(connector);
      ((RDateField) viewComponent).setType(propertyDescriptor.getType()
          .toString());
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter formatter = createDecimalFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      if (isNumberServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(),
            createDecimalFormatter(propertyDescriptor, locale));
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      connector.setExceptionHandler(actionHandler);
      viewComponent = createRDecimalField(connector);
      if (propertyDescriptor.getMaxFractionDigit() != null) {
        ((RDecimalComponent) viewComponent)
            .setMaxFractionDigit(propertyDescriptor.getMaxFractionDigit()
                .intValue());
      } else {
        ((RDecimalComponent) viewComponent).setMaxFractionDigit(2);
      }
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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
    RComponent viewComponent;
    IFormatter formatter = createDurationFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      if (isDurationServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(),
            createDurationFormatter(propertyDescriptor, locale));
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      viewComponent = createRDurationField(connector);
      if (propertyDescriptor.getMaxMillis() != null) {
        ((RDurationField) viewComponent).setMaxMillis(propertyDescriptor
            .getMaxMillis().longValue());
      } else {
        ((RDurationField) viewComponent).setMaxMillis(-1);
      }
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent createEmptyComponent() {
    RComponent emptyComponent = createRBorderContainer();
    emptyComponent.setState(new RemoteCompositeValueState(getGuidGenerator()
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
    viewComponent.setReadOnly(propertyViewDescriptor.isReadOnly());
    List<String> values = new ArrayList<String>();
    List<String> translations = new ArrayList<String>();
    List<RIcon> icons = new ArrayList<RIcon>();
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    List<String> enumerationValues = new ArrayList<String>(
        propertyDescriptor.getEnumerationValues());
    if (!propertyDescriptor.isMandatory()) {
      enumerationValues.add(0, "");
    }
    for (String value : enumerationValues) {
      values.add(value);
      icons.add(getIconFactory().getIcon(
          propertyDescriptor.getIconImageURL(value),
          getIconFactory().getTinyIconSize()));
      if (value != null && propertyDescriptor.isTranslated()) {
        if ("".equals(value)) {
          translations.add(" ");
        } else {
          translations.add(getTranslationProvider().getTranslation(
              computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                  value), locale));
        }
      } else {
        if (value == null) {
          translations.add(" ");
        } else {
          translations.add(value);
        }
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
  protected IView<RComponent> createHtmlPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    IHtmlPropertyDescriptor propertyDescriptor = (IHtmlPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RHtmlArea viewComponent = createRHtmlArea(connector);
    viewComponent.setReadOnly(propertyViewDescriptor.isReadOnly());
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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
    if (connector instanceof RemoteValueConnector) {
      final RemoteValueConnector rConnector = (RemoteValueConnector) connector;
      rConnector.setRemoteStateValueMapper(new IRemoteStateValueMapper() {

        public Object getValueForState(Object originalValue) {
          if (originalValue instanceof byte[]) {
            String valueForStateUrl = RemotePeerRegistryServlet
                .computeDownloadUrl(rConnector.getGuid());
            Checksum checksumEngine = new CRC32();
            checksumEngine.update((byte[]) originalValue, 0,
                ((byte[]) originalValue).length);
            // we must add a check sum so that the client nows when the url
            // content
            // changes.
            valueForStateUrl += ("&cs=" + checksumEngine.getValue());
            return valueForStateUrl;
          } else if (originalValue instanceof String) {
            return ResourceProviderServlet
                .computeLocalResourceDownloadUrl((String) originalValue);
          }
          return originalValue;
        }
      });
    }
    RImageComponent viewComponent = createRImageComponent(connector);
    viewComponent.setScrollable(viewDescriptor.isScrollable());
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
    RComponent viewComponent;
    IFormatter formatter = createIntegerFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      if (isNumberServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      viewComponent = createRIntegerField(connector);
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createListView(
      IListViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
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
    RList viewComponent = createRList(connector);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createListConnector(
          viewDescriptor.getRenderedProperty(), modelDescriptor
              .getCollectionDescriptor().getElementDescriptor());
      rowConnectorPrototype.addChildConnector(cellConnector);
    }
    viewComponent
        .setSelectionMode(viewDescriptor.getSelectionMode().toString());
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale));
    }
    attachDefaultCollectionListener(connector);
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
    IView<RComponent> view = super.createNumberPropertyView(
        propertyViewDescriptor, actionHandler, locale);
    if (view.getPeer() instanceof RNumericComponent) {
      ((RNumericComponent) view.getPeer()).setMaxValue(propertyDescriptor
          .getMaxValue());
      ((RNumericComponent) view.getPeer()).setMinValue(propertyDescriptor
          .getMinValue());
    }
    if (view.getConnector() instanceof RemoteValueConnector) {
      final RemoteValueConnector rConnector = (RemoteValueConnector) view
          .getConnector();
      rConnector.setRemoteStateValueMapper(new IRemoteStateValueMapper() {

        public Object getValueForState(Object originalValue) {
          if (originalValue instanceof BigDecimal) {
            return new Double(((BigDecimal) originalValue).doubleValue());
          } else if (originalValue instanceof BigInteger) {
            return new Long(((BigInteger) originalValue).longValue());
          }
          return originalValue;
        }
      });
    }
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
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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
    IFormatter formatter = createPercentFormatter(propertyDescriptor, locale);
    IValueConnector connector;
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      if (isNumberServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(),
            createPercentFormatter(propertyDescriptor, locale));
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      connector.setExceptionHandler(actionHandler);
      viewComponent = createRPercentField(connector);
      if (propertyDescriptor.getMaxFractionDigit() != null) {
        ((RPercentField) viewComponent).setMaxFractionDigit(propertyDescriptor
            .getMaxFractionDigit().intValue());
      } else {
        ((RPercentField) viewComponent).setMaxFractionDigit(2);
      }
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Creates a property label.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param propertyComponent
   *          the property component.
   * @param locale
   *          the locale.
   * @return the created property label.
   */
  protected RLabel createPropertyLabel(
      IPropertyViewDescriptor propertyViewDescriptor,
      RComponent propertyComponent, Locale locale) {
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    RLabel propertyLabel = createRLabel(null, false);
    StringBuffer labelText = new StringBuffer(
        propertyViewDescriptor.getI18nName(getTranslationProvider(), locale));
    if (propertyDescriptor.isMandatory()) {
      labelText.append("*");
      propertyLabel.setForeground("0x00FF0000");
    }
    propertyLabel.setLabel(labelText.toString());
    if (propertyViewDescriptor.getLabelFont() != null) {
      propertyLabel.setFont(createFont(propertyViewDescriptor.getLabelFont()));
    }
    if (propertyViewDescriptor.getLabelForeground() != null) {
      propertyLabel.setForeground(propertyViewDescriptor.getLabelForeground());
    }
    if (propertyViewDescriptor.getLabelBackground() != null) {
      propertyLabel.setBackground(propertyViewDescriptor.getLabelBackground());
    }
    return propertyLabel;
  }

  /**
   * Override to set the property view name that will be useful on client-side.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IView<RComponent> propertyView = super.createPropertyView(propertyViewDescriptor,
        actionHandler, locale);
    if (propertyView != null) {
      if (propertyDescriptor.getName() != null) {
        propertyView.getPeer().setLabel(
            propertyDescriptor.getI18nName(getTranslationProvider(), locale));
      }
      if (propertyView.getPeer() instanceof RLabel) {
        configureAlignment((RLabel) propertyView.getPeer(),
            propertyViewDescriptor.getHorizontalAlignment());
      } else if (propertyView.getPeer() instanceof RTextField) {
        configureAlignment((RTextField) propertyView.getPeer(),
            propertyViewDescriptor.getHorizontalAlignment());
      } else if (propertyView.getPeer() instanceof RNumericComponent) {
        configureAlignment((RNumericComponent) propertyView.getPeer(),
            propertyViewDescriptor.getHorizontalAlignment());
      }
    }
    return propertyView;
  }

  /**
   * Creates a remote button component.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RActionComponent createRActionComponent(IValueConnector connector) {
    RActionComponent component = new RActionComponent(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote action field.
   * 
   * @param showTextField
   *          does it actually show a text field ?
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RActionField createRActionField(boolean showTextField,
      IValueConnector connector) {
    RActionField component = new RActionField(getGuidGenerator().generateGUID());
    component.setShowTextField(showTextField);
    return component;
  }

  /**
   * Creates a remote border container.
   * 
   * @return the created remote component.
   */
  protected RBorderContainer createRBorderContainer() {
    return new RBorderContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote card container.
   * 
   * @param viewDescriptor
   *          the card view descriptor.
   * @return the created remote component.
   */
  protected RCardContainer createRCardContainer(
      ICardViewDescriptor viewDescriptor) {
    RCardContainer cardContainer = new RCardContainer(getGuidGenerator()
        .generateGUID());
    cardContainer.setState(((IRemoteValueStateFactory) getConnectorFactory())
        .createRemoteValueState(getGuidGenerator().generateGUID(),
            viewDescriptor.getAutomationSeed()));
    return cardContainer;
  }

  /**
   * Creates a remote check box.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RCheckBox createRCheckBox(IValueConnector connector) {
    RCheckBox component = new RCheckBox(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote color field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RColorField createRColorField(IValueConnector connector) {
    RColorField component = new RColorField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote combo box.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RComboBox createRComboBox(IValueConnector connector) {
    RComboBox component = new RComboBox(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote contrained grid container.
   * 
   * @return the created remote component.
   */
  protected RConstrainedGridContainer createRConstrainedGridContainer() {
    return new RConstrainedGridContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote date field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RDateField createRDateField(IValueConnector connector) {
    RDateField component = new RDateField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote decimal field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RDecimalField createRDecimalField(IValueConnector connector) {
    RDecimalField component = new RDecimalField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote duration field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RDurationField createRDurationField(IValueConnector connector) {
    RDurationField component = new RDurationField(getGuidGenerator()
        .generateGUID());
    return component;
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
    IValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(propertyDescriptor.getName(),
            renderedProperty);
    connector.setExceptionHandler(actionHandler);
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      viewComponent = createRActionField(true, connector);
    }
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    if (viewComponent instanceof RActionField) {
      RAction lovAction = createLovAction(view, actionHandler, locale);
      // lovAction.setName(getTranslationProvider().getTranslation(
      // "lov.element.name",
      // new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
      // getTranslationProvider(), locale)}, locale));
      lovAction.setDescription(getTranslationProvider().getTranslation(
          "lov.element.description",
          new Object[] {
            propertyDescriptor.getReferencedDescriptor().getI18nName(
                getTranslationProvider(), locale)
          }, locale));
      if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
        lovAction.setIcon(getIconFactory().getIcon(
            propertyDescriptor.getReferencedDescriptor().getIconImageURL(),
            getIconFactory().getTinyIconSize()));
      }
      RActionList actionList = new RActionList(getGuidGenerator()
          .generateGUID());
      actionList.setActions(new RAction[] {
        lovAction
      });
      viewComponent.setActionLists(new RActionList[] {
        actionList
      });
    }
    return view;
  }

  /**
   * Creates a remote even grid container.
   * 
   * @return the created remote component.
   */
  protected REvenGridContainer createREvenGridContainer() {
    return new REvenGridContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote form.
   * 
   * @return the created remote component.
   */
  protected RForm createRForm() {
    return new RForm(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote html area.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RHtmlArea createRHtmlArea(IValueConnector connector) {
    RHtmlArea component = new RHtmlArea(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote image component.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RImageComponent createRImageComponent(IValueConnector connector) {
    RImageComponent component = new RImageComponent(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote integer field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RIntegerField createRIntegerField(IValueConnector connector) {
    RIntegerField component = new RIntegerField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote label.
   * 
   * @param connector
   *          the component connector.
   * @param bold
   *          make it bold ?
   * @return the created remote component.
   */
  protected RLabel createRLabel(IValueConnector connector, boolean bold) {
    RLabel component = new RLabel(getGuidGenerator().generateGUID());
    if (bold) {
      if (bold) {
        component.setFont(createFont(BOLD_FONT));
      }
    }
    return component;
  }

  /**
   * Creates a remote link.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RLink createRLink(IValueConnector connector) {
    RLink component = new RLink(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote list.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RList createRList(ICollectionConnector connector) {
    RList component = new RList(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote password field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RPasswordField createRPasswordField(IValueConnector connector) {
    RPasswordField component = new RPasswordField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote percent field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RPercentField createRPercentField(IValueConnector connector) {
    RPercentField component = new RPercentField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote split container.
   * 
   * @return the created remote component.
   */
  protected RSplitContainer createRSplitContainer() {
    return new RSplitContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote tab container.
   * 
   * @return the created remote component.
   */
  protected RTabContainer createRTabContainer() {
    return new RTabContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote table.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RTable createRTable(IValueConnector connector) {
    RTable component = new RTable(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote text area.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RTextArea createRTextArea(IValueConnector connector) {
    RTextArea component = new RTextArea(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote text field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RTextField createRTextField(IValueConnector connector) {
    RTextField component = new RTextField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote time field.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RTimeField createRTimeField(IValueConnector connector) {
    RTimeField component = new RTimeField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote tree.
   * 
   * @param connector
   *          the component connector.
   * @return the created remote component.
   */
  protected RTree createRTree(IValueConnector connector) {
    RTree component = new RTree(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent createSecurityComponent() {
    RComponent securityComponent = new RSecurityComponent(getGuidGenerator()
        .generateGUID());
    securityComponent.setState(new RemoteValueState(getGuidGenerator()
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
      IView<RComponent> leftTopView = createView(
          viewDescriptor.getLeftTopViewDescriptor(), actionHandler, locale);
      viewComponent.setLeftTop(leftTopView.getPeer());
      childrenViews.add(leftTopView);
    }
    if (viewDescriptor.getRightBottomViewDescriptor() != null) {
      IView<RComponent> rightBottomView = createView(
          viewDescriptor.getRightBottomViewDescriptor(), actionHandler, locale);
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
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      viewComponent = createRTextField(connector);
    }
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent decorateWithPaginationView(RComponent viewPeer,
      RComponent paginationViewPeer) {
    RBorderContainer decorator = new RBorderContainer(getGuidGenerator()
        .generateGUID());
    decorator.setCenter(viewPeer);
    decorator.setSouth(paginationViewPeer);
    return decorator;
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
    IComponentDescriptor<?> rowDescriptor = modelDescriptor
        .getCollectionDescriptor().getElementDescriptor();
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            rowDescriptor.getToStringProperty());
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
            rowConnectorPrototype);
    RTable viewComponent = createRTable(connector);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    viewComponent.setSortable(viewDescriptor.isSortable());
    if (viewDescriptor.getSortingAction() != null) {
      viewComponent.setSortingAction(getActionFactory().createAction(
          viewDescriptor.getSortingAction(), actionHandler, view, locale));
    }
    viewComponent.setHorizontallyScrollable(viewDescriptor
        .isHorizontallyScrollable());
    List<RComponent> columns = new ArrayList<RComponent>();
    List<String> columnIds = new ArrayList<String>();
    for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      if (actionHandler.isAccessGranted(columnViewDescriptor)) {
        IView<RComponent> column = createView(columnViewDescriptor,
            actionHandler, locale);
        // Do not use standard createColumnConnector method to preserve
        // formatted value connectors.
        // IValueConnector columnConnector = createColumnConnector(columnId,
        // rowDescriptor);
        IValueConnector columnConnector = column.getConnector();
        rowConnectorPrototype.addChildConnector(columnConnector);
        String propertyName = columnViewDescriptor.getModelDescriptor()
            .getName();
        columnConnector.setLocallyWritable(!columnViewDescriptor.isReadOnly());
        IPropertyDescriptor propertyDescriptor = rowDescriptor
            .getPropertyDescriptor(propertyName);
        if (propertyDescriptor.isMandatory()) {
          if (column.getPeer().getLabel() != null) {
            column.getPeer().setLabel(column.getPeer().getLabel() + "*");
          } else {
            column.getPeer().setLabel("*");
          }
        }
        columns.add(column.getPeer());
        columnIds.add(computeColumnIdentifier(rowDescriptor, columnConnector));
        if (column.getPeer() instanceof RLink) {
          ((RLink) column.getPeer()).setAction(getActionFactory().createAction(
              columnViewDescriptor.getAction(), actionHandler, view, locale));
        }
      }
    }
    viewComponent.setColumns(columns.toArray(new RComponent[0]));
    viewComponent.setColumnIds(columnIds.toArray(new String[0]));
    viewComponent
        .setSelectionMode(viewDescriptor.getSelectionMode().toString());
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale));
    }
    attachDefaultCollectionListener(connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    final RTabContainer viewComponent = createRTabContainer();
    getRemotePeerRegistry().register(viewComponent);

    final BasicIndexedView<RComponent> view = constructIndexedView(
        viewComponent, viewDescriptor);

    viewComponent.addPropertyChangeListener("selectedIndex",
        new PropertyChangeListener() {

          public void propertyChange(PropertyChangeEvent evt) {
            RTabContainer source = (RTabContainer) evt.getSource();
            view.setCurrentViewIndex(source.getSelectedIndex());
          }
        });
    List<RComponent> tabs = new ArrayList<RComponent>();
    List<IView<RComponent>> childrenViews = new ArrayList<IView<RComponent>>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor
        .getChildViewDescriptors()) {
      if (actionHandler.isAccessGranted(childViewDescriptor)) {
        IView<RComponent> childView = createView(childViewDescriptor,
            actionHandler, locale);
        RComponent tab = childView.getPeer();
        switch (viewDescriptor.getRenderingOptions()) {
          case ICON:
            tab.setLabel(null);
            break;
          case LABEL:
            tab.setIcon(null);
            break;
          default:
            break;
        }
        tabs.add(tab);
        childrenViews.add(childView);
      }
    }
    viewComponent.setTabs(tabs.toArray(new RComponent[0]));
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void selectChildViewIndex(RComponent viewComponent, int index) {
    if (viewComponent instanceof RTabContainer) {
      ((RTabContainer) viewComponent).setSelectedIndex(index);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTextPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, @SuppressWarnings("unused") Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RTextArea viewComponent = createRTextArea(connector);
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ITimePropertyDescriptor propertyDescriptor = (ITimePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter formatter = createTimeFormatter(propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(connector);
      } else {
        viewComponent = createRLabel(connector, true);
      }
    } else {
      if (isDateServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      viewComponent = createRTimeField(connector);
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    final ICompositeValueConnector connector = createTreeViewConnector(
        viewDescriptor, actionHandler, locale);

    RTree viewComponent = createRTree(connector);
    viewComponent.setExpanded(viewDescriptor.isExpanded());
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale));
    }
    if (connector instanceof IItemSelectable
        && connector instanceof ICollectionConnectorProvider) {
      ((IItemSelectable) connector)
          .addItemSelectionListener(new IItemSelectionListener() {

            public void selectedItemChange(ItemSelectionEvent event) {
              if (event.getSelectedItem() == null) {
                // change selected item so that the root connector is considered
                // selected.
                event.setSelectedItem(connector);
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
  protected void decorateWithActions(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale, IView<RComponent> view) {
    ActionMap actionMap = viewDescriptor.getActionMap();
    ActionMap secondaryActionMap = viewDescriptor.getSecondaryActionMap();
    if (actionMap != null) {
      List<RActionList> viewActionLists = createViewToolBar(actionMap, view,
          actionHandler, locale);
      view.getPeer()
          .setActionLists(viewActionLists.toArray(new RActionList[0]));
    }
    if (secondaryActionMap != null) {
      List<RActionList> viewActionLists = createViewToolBar(secondaryActionMap,
          view, actionHandler, locale);
      view.getPeer().setSecondaryActionLists(
          viewActionLists.toArray(new RActionList[0]));
    }
  }

  /**
   * Creates a view toolbar based on an action map.
   * 
   * @param actionMap
   *          the action map to create the toolbar for.
   * @param view
   *          the view to create the toolbar for.
   * @param actionHandler
   *          the action handler used.
   * @param locale
   *          the locale used.
   * @return the created tool bar.
   */
  protected List<RActionList> createViewToolBar(ActionMap actionMap,
      IView<RComponent> view, IActionHandler actionHandler, Locale locale) {
    List<RActionList> viewActionLists = new ArrayList<RActionList>();
    for (Iterator<ActionList> iter = actionMap.getActionLists().iterator(); iter
        .hasNext();) {
      ActionList nextActionList = iter.next();
      ERenderingOptions renderingOptions = getDefaultActionMapRenderingOptions();
      if (nextActionList.getRenderingOptions() != null) {
        renderingOptions = nextActionList.getRenderingOptions();
      } else if (actionMap.getRenderingOptions() != null) {
        renderingOptions = actionMap.getRenderingOptions();
      }
      RActionList actionList = new RActionList(getGuidGenerator()
          .generateGUID());
      actionList.setCollapsable(nextActionList.isCollapsable());
      actionList.setName(nextActionList.getName());
      actionList.setDescription(nextActionList.getDescription());
      actionList
          .setIcon(getIconFactory().getIcon(nextActionList.getIconImageURL(),
              getIconFactory().getTinyIconSize()));
      viewActionLists.add(actionList);
      List<RAction> actions = new ArrayList<RAction>();
      for (IDisplayableAction action : nextActionList.getActions()) {
        if (actionHandler.isAccessGranted(action)) {
          RAction rAction = getActionFactory().createAction(action,
              actionHandler, view, locale);
          rAction.setAcceleratorAsString(action.getAcceleratorAsString());
          actions.add(rAction);
          switch (renderingOptions) {
            case ICON:
              rAction.setName(null);
              break;
            case LABEL:
              rAction.setIcon(null);
              break;
            default:
              break;
          }
        }
      }
      actionList.setActions(actions.toArray(new RAction[0]));
    }
    return viewActionLists;
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
    RComponent viewPeer = view.getPeer();
    configureComponent(viewDescriptor, locale, viewPeer);
  }

  private void configureComponent(IViewDescriptor viewDescriptor,
      Locale locale, RComponent viewPeer) {
    viewPeer.setLabel(
        viewDescriptor.getI18nName(getTranslationProvider(), locale));
    if (viewDescriptor.getDescription() != null) {
      viewPeer.setTooltip(
          viewDescriptor.getI18nDescription(getTranslationProvider(), locale));
    }
    if (viewDescriptor.getForeground() != null) {
      viewPeer.setForeground(viewDescriptor.getForeground());
    }
    if (viewDescriptor.getBackground() != null) {
      viewPeer.setBackground(viewDescriptor.getBackground());
    }
    if (viewDescriptor.getFont() != null) {
      viewPeer.setFont(createFont(viewDescriptor.getFont()));
    }
    if (viewDescriptor.getIconImageURL() != null) {
      viewPeer.setIcon(
          getIconFactory().getIcon(viewDescriptor.getIconImageURL(),
              getIconFactory().getSmallIconSize()));
    }
  }

  /**
   * Gets the getGuidGenerator().
   * 
   * @return the getGuidGenerator().
   */
  protected IGUIDGenerator getGuidGenerator() {
    return guidGenerator;
  }

  /**
   * Gets the remoteCommandHandler.
   * 
   * @return the remoteCommandHandler.
   */
  protected IRemoteCommandHandler getRemoteCommandHandler() {
    return remoteCommandHandler;
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
    getRemoteCommandHandler().registerCommand(command);
  }

  private Font createFont(String fontString) {
    Font font = FontHelper.fromString(fontString);
    return font;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> constructView(RComponent viewComponent,
      IViewDescriptor descriptor, IValueConnector connector) {
    IView<RComponent> view = super.constructView(viewComponent, descriptor,
        connector);
    if (connector instanceof IAutomationSource) {
      String automationSeed = descriptor.getAutomationSeed();
      if (automationSeed == null) {
        automationSeed = descriptor.getName();
      }
      ((IAutomationSource) connector).setAutomationSeed(automationSeed);
    }
    if (viewComponent.getState() == null) {
      viewComponent.setState(((IRemoteStateOwner) connector).getState());
    }
    return view;
  }

  /**
   * Gets the remotePeerRegistry.
   * 
   * @return the remotePeerRegistry.
   */
  public IRemotePeerRegistry getRemotePeerRegistry() {
    return remotePeerRegistry;
  }

  /**
   * Sets the remotePeerRegistry.
   * 
   * @param remotePeerRegistry
   *          the remotePeerRegistry to set.
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    this.remotePeerRegistry = remotePeerRegistry;
  }

  private void configureAlignment(RTextField textField,
      EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      textField.setHorizontalAlignment(horizontalAlignment.toString());
    }
  }

  private void configureAlignment(RLabel label,
      EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      label.setHorizontalAlignment(horizontalAlignment.toString());
    }
  }

  private void configureAlignment(RNumericComponent numericField,
      EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      numericField.setHorizontalAlignment(horizontalAlignment.toString());
    }
  }
}
