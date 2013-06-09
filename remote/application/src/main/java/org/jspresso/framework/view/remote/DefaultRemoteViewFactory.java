/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.application.view.ControllerAwareViewFactory;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.binding.remote.RemoteValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionComponent;
import org.jspresso.framework.gui.remote.RActionField;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.gui.remote.RActionable;
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
import org.jspresso.framework.gui.remote.REnumBox;
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
import org.jspresso.framework.gui.remote.RRadioBox;
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
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.security.EAuthorization;
import org.jspresso.framework.server.remote.RemotePeerRegistryServlet;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteStateValueMapper;
import org.jspresso.framework.state.remote.IRemoteValueStateFactory;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.gui.Font;
import org.jspresso.framework.util.gui.FontHelper;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.DateDto;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicIndexedView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.IActionFactory;
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
import org.jspresso.framework.view.descriptor.IEnumerationPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IScrollableViewDescriptor;
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
    ControllerAwareViewFactory<RComponent, RIcon, RAction> {

  private boolean                              dateServerParse;

  private boolean                              durationServerParse;
  private IGUIDGenerator<String>               guidGenerator;
  private boolean                              numberServerParse;

  private IRemoteCommandHandler                remoteCommandHandler;
  private IRemotePeerRegistry                  remotePeerRegistry;
  private static final IRemoteStateValueMapper FONT_MAPPER = new IRemoteStateValueMapper() {

                                                             @Override
                                                             public Object getValueFromState(
                                                                 Object originalValue) {
                                                               if (originalValue instanceof Font) {
                                                                 return FontHelper
                                                                     .toString((Font) originalValue);
                                                               }
                                                               return null;
                                                             }

                                                             @Override
                                                             public Object getValueForState(
                                                                 Object originalValue) {
                                                               if (originalValue instanceof String
                                                                   && FontHelper
                                                                       .isFontSpec((String) originalValue)) {
                                                                 return FontHelper
                                                                     .fromString((String) originalValue);
                                                               }
                                                               return null;
                                                             }
                                                           };

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
  public void setGuidGenerator(IGUIDGenerator<String> guidGenerator) {
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
  protected void adjustSizes(IViewDescriptor viewDescriptor,
      RComponent component, IFormatter<?, String> formatter,
      Object templateValue, int extraWidth) {
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
  protected int computePixelWidth(RComponent component, int characterLength) {
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
    RActionComponent viewComponent = createRActionComponent(viewDescriptor);
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

        @Override
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

        @Override
        public Object getValueFromState(Object originalValue) {
          return originalValue;
        }
      });
    }
    connector.setExceptionHandler(actionHandler);
    RActionField viewComponent = createRActionField(propertyViewDescriptor,
        false);
    IView<RComponent> propertyView = constructView(viewComponent,
        propertyViewDescriptor, connector);
    RActionList actionList = new RActionList(getGuidGenerator().generateGUID());
    List<RAction> binaryActions = createBinaryActions(propertyView,
        actionHandler, locale);
    actionList.setActions(binaryActions.toArray(new RAction[binaryActions.size()]));
    viewComponent.setActionLists(actionList);
    return propertyView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createBooleanPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IBooleanPropertyDescriptor propertyDescriptor = (IBooleanPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RCheckBox viewComponent = createRCheckBox(propertyViewDescriptor);
    if (!propertyDescriptor.isMandatory()) {
      viewComponent.setTriState(true);
    }
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
    RBorderContainer viewComponent = createRBorderContainer(viewDescriptor);
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

    viewComponent.setCardNames(cardNames.toArray(new String[cardNames.size()]));
    viewComponent.setCards(cards.toArray(new RComponent[cards.size()]));
    view.setConnector(createCardViewConnector(view, actionHandler, locale));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createColorPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IColorPropertyDescriptor propertyDescriptor = (IColorPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RColorField viewComponent = createRColorField(propertyViewDescriptor);
    String defaultColor = (String) propertyDescriptor.getDefaultValue();
    viewComponent.setDefaultColor(defaultColor);
    if (defaultColor == null && propertyDescriptor.isMandatory()) {
      viewComponent.setResetEnabled(false);
    }
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
    IComponentDescriptor<?> modelDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor()).getComponentDescriptor();
    // Dynamic toolTips
    String toolTipProperty = computeComponentDynamicToolTip(viewDescriptor,
        modelDescriptor);
    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            getConnectorIdForBeanView(viewDescriptor), toolTipProperty);
    RForm viewComponent = createRForm(viewDescriptor);
    viewComponent.setVerticallyScrollable(viewDescriptor
        .isVerticallyScrollable());
    viewComponent.setColumnCount(viewDescriptor.getColumnCount());
    viewComponent.setLabelsPosition(viewDescriptor.getLabelsPosition().name());

    List<Integer> elementWidths = new ArrayList<Integer>();
    List<RComponent> elements = new ArrayList<RComponent>();
    List<RComponent> elementLabels = new ArrayList<RComponent>();
    List<IView<RComponent>> propertyViews = new ArrayList<IView<RComponent>>();

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
      // Fix bug 782
      propertyView.getPeer().setIcon(null);
      propertyView.setParent(view);

      boolean forbidden = !actionHandler
          .isAccessGranted(propertyViewDescriptor);
      if (forbidden) {
        RComponent securityComponent = createSecurityComponent();
        securityComponent.setPreferredSize(new Dimension(1, 1));
        propertyView.setPeer(securityComponent);
      } else {
        propertyViews.add(propertyView);
      }
      elements.add(propertyView.getPeer());
      RLabel propertyLabel = createFormPropertyLabel(actionHandler, locale,
          propertyViewDescriptor, propertyDescriptor, propertyView, forbidden);
      elementLabels.add(propertyLabel);
      elementWidths.add(propertyViewDescriptor.getWidth());
      connector.addChildConnector(propertyName, propertyView.getConnector());
      // already handled in createView.
      // if (propertyViewDescriptor.getReadabilityGates() != null) {
      // if (propertyViewDescriptor.getWritabilityGates() != null) {
      // propertyView.getConnector().setLocallyWritable(
      // !propertyViewDescriptor.isReadOnly());
      if (propertyView.getPeer() instanceof RActionable
          && propertyViewDescriptor.getAction() != null) {
        IView<RComponent> targetView;
        if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
          targetView = propertyView;
        } else {
          targetView = view;
        }
        RAction action = getActionFactory().createAction(
            propertyViewDescriptor.getAction(), actionHandler, targetView,
            locale);
        configurePropertyViewAction(propertyViewDescriptor, action);
        ((RActionable) propertyView.getPeer()).setAction(action);
      }
    }
    completePropertyViewsWithDynamicToolTips(connector, propertyViews,
        modelDescriptor);
    completePropertyViewsWithDynamicBackgrounds(connector, propertyViews,
        modelDescriptor);
    completePropertyViewsWithDynamicForegrounds(connector, propertyViews,
        modelDescriptor);
    completePropertyViewsWithDynamicFonts(connector, propertyViews,
        modelDescriptor);
    viewComponent.setElementWidths(elementWidths.toArray(new Integer[elementWidths.size()]));
    viewComponent.setElements(elements.toArray(new RComponent[elements.size()]));
    viewComponent.setElementLabels(elementLabels.toArray(new RComponent[elementLabels.size()]));
    return view;
  }

  private RLabel createFormPropertyLabel(IActionHandler actionHandler,
      Locale locale, IPropertyViewDescriptor propertyViewDescriptor,
      IPropertyDescriptor propertyDescriptor, IView<RComponent> propertyView,
      boolean forbidden) {
    RLabel propertyLabel = createPropertyLabel(propertyViewDescriptor,
        propertyView.getPeer(), actionHandler, locale);
    if (propertyDescriptor.isMandatory()
        && !(propertyDescriptor instanceof IBooleanPropertyDescriptor)) {
      if (propertyViewDescriptor.getLabelForeground() == null) {
        propertyLabel.setForeground(getFormLabelMandatoryPropertyColorHex());
      }
      propertyLabel.setLabel(decorateMandatoryPropertyLabel(propertyLabel
          .getLabel()));
    }
    if (forbidden) {
      propertyLabel.setLabel(" ");
      propertyLabel.setIcon(null);
    }
    return propertyLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    RConstrainedGridContainer viewComponent = createRConstrainedGridContainer(viewDescriptor);
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
    viewComponent.setCells(cells.toArray(new RComponent[cells.size()]));
    viewComponent.setCellConstraints(cellConstraints
        .toArray(new CellConstraints[cellConstraints.size()]));
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
    IFormatter<?, String> formatter = createDateFormatter(propertyDescriptor,
        actionHandler.getClientTimeZone(), actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter,
          getDateTemplateValue(propertyDescriptor)));
    } else {
      if (isDateServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
        final TimeZone serverTz = TimeZone.getDefault();
        if (propertyDescriptor.isTimeZoneAware()) {
          ((RemoteValueConnector) connector)
              .setRemoteStateValueMapper(new IRemoteStateValueMapper() {

                @Override
                public Object getValueForState(Object originalValue) {
                  return originalValue;
                }

                @SuppressWarnings("MagicConstant")
                @Override
                public Object getValueFromState(Object originalValue) {
                  Calendar serverCalendar = Calendar.getInstance(serverTz);
                  if (originalValue instanceof Date) {
                    serverCalendar.setTime((Date) originalValue);
                    if (serverCalendar.getTime().getTime() < 24 * 3600 * 1000) {
                      // This is a default date. Set it today.
                      Calendar today = Calendar.getInstance(serverTz);
                      serverCalendar.set(today.get(Calendar.YEAR),
                          today.get(Calendar.MONTH), today.get(Calendar.DATE));
                    }
                    Date connectorDate = serverCalendar.getTime();
                    return connectorDate;
                  }
                  return originalValue;
                }

              });
        } else {
          // In that case, we have to use a Date DTO to avoid any
          // transformation by the network layer
          ((RemoteValueConnector) connector)
              .setRemoteStateValueMapper(new IRemoteStateValueMapper() {

                @SuppressWarnings("MagicConstant")
                @Override
                public Object getValueFromState(Object originalValue) {
                  Calendar serverCalendar = Calendar.getInstance(serverTz);
                  if (originalValue instanceof DateDto) {
                    DateDto stateDate = (DateDto) originalValue;
                    serverCalendar.set(stateDate.getYear(),
                        stateDate.getMonth(), stateDate.getDate(),
                        stateDate.getHour(), stateDate.getMinute(),
                        stateDate.getSecond());
                    serverCalendar.set(Calendar.MILLISECOND, 0);
                    if (serverCalendar.getTime().getTime() >= 0
                        && serverCalendar.getTime().getTime() < 24 * 3600 * 1000) {
                      // This is a default date. Set it today.
                      Calendar today = Calendar.getInstance(serverTz);
                      serverCalendar.set(today.get(Calendar.YEAR),
                          today.get(Calendar.MONTH), today.get(Calendar.DATE));
                    }
                    Date connectorDate = serverCalendar.getTime();
                    return connectorDate;
                  }
                  return originalValue;
                }

                @Override
                public Object getValueForState(Object originalValue) {
                  if (originalValue instanceof Date) {
                    Date connectorDate = (Date) originalValue;
                    Calendar serverCalendar = Calendar.getInstance(serverTz);
                    serverCalendar.setTime(connectorDate);

                    DateDto stateDate = new DateDto();
                    stateDate.setYear(serverCalendar.get(Calendar.YEAR));
                    stateDate.setMonth(serverCalendar.get(Calendar.MONTH));
                    stateDate.setDate(serverCalendar.get(Calendar.DATE));
                    stateDate.setHour(serverCalendar.get(Calendar.HOUR_OF_DAY));
                    stateDate.setMinute(serverCalendar.get(Calendar.MINUTE));
                    stateDate.setSecond(serverCalendar.get(Calendar.SECOND));
                    return stateDate;
                  }
                  return originalValue;
                }
              });
        }
      }
      viewComponent = createRDateField(propertyViewDescriptor);
      ((RDateField) viewComponent).setType(propertyDescriptor.getType().name());
      ((RDateField) viewComponent).setTimezoneAware(propertyDescriptor
          .isTimeZoneAware());
      ((RDateField) viewComponent).setSecondsAware(propertyDescriptor
          .isSecondsAware());
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
    IFormatter<Object, String> formatter = createDecimalFormatter(
        propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter,
          getDecimalTemplateValue(propertyDescriptor)));
    } else {
      if (isNumberServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      connector.setExceptionHandler(actionHandler);
      viewComponent = createRDecimalField(propertyViewDescriptor);
      ((RDecimalComponent) viewComponent)
          .setMaxFractionDigit(propertyDescriptor.getMaxFractionDigit());
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
    IFormatter<?, String> formatter = createDurationFormatter(
        propertyDescriptor, actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter,
          getDurationTemplateValue(propertyDescriptor)));
    } else {
      if (isDurationServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      viewComponent = createRDurationField(propertyViewDescriptor);
      if (propertyDescriptor.getMaxMillis() != null) {
        ((RDurationField) viewComponent).setMaxMillis(propertyDescriptor
            .getMaxMillis());
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
    RComponent emptyComponent = createRBorderContainer(null);
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
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter<Object, String> formatter = createEnumerationFormatter(
        propertyDescriptor, actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()
        && propertyViewDescriptor.getAction() != null) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      viewComponent = createRLink(propertyViewDescriptor, false);
      ((RLabel) viewComponent)
          .setMaxLength(getFormatLength(
              formatter,
              getEnumerationTemplateValue(propertyDescriptor, actionHandler,
                  locale)));
    } else {
      connector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
      List<String> values = new ArrayList<String>();
      List<String> translations = new ArrayList<String>();
      List<String> enumerationValues = new ArrayList<String>(
          propertyDescriptor.getEnumerationValues());
      filterEnumerationValues(enumerationValues, propertyViewDescriptor);
      if (!propertyDescriptor.isMandatory()) {
        enumerationValues.add(0, "");
      }
      for (String value : enumerationValues) {
        values.add(value);
        if (value != null && propertyDescriptor.isTranslated()) {
          if ("".equals(value)) {
            translations.add(" ");
          } else {
            translations.add(propertyDescriptor.getI18nValue(value,
                actionHandler, locale));
          }
        } else {
          if (value == null) {
            translations.add(" ");
          } else {
            translations.add(value);
          }
        }
      }
      if (propertyViewDescriptor instanceof IEnumerationPropertyViewDescriptor
          && ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor)
              .isRadio()) {
        viewComponent = createRRadioBox(propertyViewDescriptor);
        ((RRadioBox) viewComponent)
            .setOrientation(((IEnumerationPropertyViewDescriptor) propertyViewDescriptor)
                .getOrientation().name());
      } else {
        viewComponent = createRComboBox(propertyViewDescriptor);
        ((RComboBox) viewComponent).setReadOnly(propertyViewDescriptor
            .isReadOnly());
        List<RIcon> icons = new ArrayList<RIcon>();
        for (String value : enumerationValues) {
          icons.add(getIconFactory().getIcon(
              propertyDescriptor.getIconImageURL(value),
              getIconFactory().getTinyIconSize()));
        }
        ((RComboBox) viewComponent).setIcons(icons.toArray(new RIcon[icons.size()]));
      }
      ((REnumBox) viewComponent).setValues(values.toArray(new String[values.size()]));
      ((REnumBox) viewComponent).setTranslations(translations
          .toArray(new String[translations.size()]));
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
  protected ICompositeView<RComponent> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    REvenGridContainer viewComponent = createREvenGridContainer(viewDescriptor);
    viewComponent.setDrivingDimension(viewDescriptor.getDrivingDimension()
        .name());
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
    viewComponent.setCells(cells.toArray(new RComponent[cells.size()]));
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createHtmlPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IHtmlPropertyDescriptor propertyDescriptor = (IHtmlPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RHtmlArea viewComponent = createRHtmlArea(propertyViewDescriptor);
    viewComponent.setReadOnly(propertyViewDescriptor.isReadOnly());
    if (propertyViewDescriptor instanceof IScrollableViewDescriptor) {
      viewComponent
          .setVerticallyScrollable(((IScrollableViewDescriptor) propertyViewDescriptor)
              .isVerticallyScrollable());
      viewComponent
          .setHorizontallyScrollable(((IScrollableViewDescriptor) propertyViewDescriptor)
              .isHorizontallyScrollable());
    } else {
      viewComponent.setVerticallyScrollable(true);
      viewComponent.setHorizontallyScrollable(false);
    }
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createImagePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyViewDescriptor.getModelDescriptor().getName());
    connector.setExceptionHandler(actionHandler);
    if (connector instanceof RemoteValueConnector) {
      final RemoteValueConnector rConnector = (RemoteValueConnector) connector;
      rConnector.setRemoteStateValueMapper(new IRemoteStateValueMapper() {

        @Override
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

        @Override
        public Object getValueFromState(Object originalValue) {
          return originalValue;
        }
      });
    }
    RImageComponent viewComponent = createRImageComponent(propertyViewDescriptor);
    if (propertyViewDescriptor instanceof IScrollableViewDescriptor) {
      viewComponent
          .setScrollable(((IScrollableViewDescriptor) propertyViewDescriptor)
              .isScrollable());
    } else {
      viewComponent.setScrollable(false);
    }
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
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
    IFormatter<Object, String> formatter = createIntegerFormatter(
        propertyDescriptor, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter,
          getIntegerTemplateValue(propertyDescriptor)));
    } else {
      if (isNumberServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      viewComponent = createRIntegerField(propertyViewDescriptor);
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
    IComponentDescriptor<?> rowDescriptor = modelDescriptor
        .getCollectionDescriptor().getElementDescriptor();
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            rowDescriptor.getToHtmlProperty());
    if (rowConnectorPrototype instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) rowConnectorPrototype)
          .setDisplayIcon(viewDescriptor.getIcon());
      ((AbstractCompositeValueConnector) rowConnectorPrototype)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
            rowConnectorPrototype);
    RList viewComponent = createRList(viewDescriptor);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createListConnector(
          viewDescriptor.getRenderedProperty(), rowDescriptor);
      rowConnectorPrototype.addChildConnector(
          viewDescriptor.getRenderedProperty(), cellConnector);
    }
    viewComponent.setSelectionMode(viewDescriptor.getSelectionMode().name());
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale));
    }
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

        @Override
        public Object getValueForState(Object originalValue) {
          if (originalValue instanceof BigDecimal) {
            return ((BigDecimal) originalValue).doubleValue();
          } else if (originalValue instanceof BigInteger) {
            return ((BigInteger) originalValue).longValue();
          }
          return originalValue;
        }

        @Override
        public Object getValueFromState(Object originalValue) {
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
      IActionHandler actionHandler, Locale locale) {
    IPasswordPropertyDescriptor propertyDescriptor = (IPasswordPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RPasswordField viewComponent = createRPasswordField(propertyViewDescriptor);
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
    IFormatter<Object, String> formatter = createPercentFormatter(
        propertyDescriptor, locale);
    IValueConnector connector;
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter,
          getPercentTemplateValue(propertyDescriptor)));
    } else {
      if (isNumberServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
      }
      connector.setExceptionHandler(actionHandler);
      viewComponent = createRPercentField(propertyViewDescriptor);
      ((RPercentField) viewComponent).setMaxFractionDigit(propertyDescriptor
          .getMaxFractionDigit());
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
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the created property label.
   */
  protected RLabel createPropertyLabel(
      IPropertyViewDescriptor propertyViewDescriptor,
      RComponent propertyComponent, ITranslationProvider translationProvider,
      Locale locale) {
    RLabel propertyLabel = createRLabel(null, false);
    String labelText = propertyViewDescriptor.getI18nName(translationProvider, locale);
    propertyLabel.setLabel(labelText);
    configurePropertyLabel(propertyLabel, propertyViewDescriptor);
    return propertyLabel;
  }

  private void configurePropertyLabel(RLabel propertyLabel,
      IPropertyViewDescriptor propertyViewDescriptor) {
    if (propertyViewDescriptor.getLabelFont() != null) {
      propertyLabel.setFont(createFont(propertyViewDescriptor.getLabelFont()));
    }
    if (propertyViewDescriptor.getLabelForeground() != null) {
      propertyLabel.setForeground(propertyViewDescriptor.getLabelForeground());
    }
    if (propertyViewDescriptor.getLabelBackground() != null) {
      propertyLabel.setBackground(propertyViewDescriptor.getLabelBackground());
    }
    if (propertyViewDescriptor.getIcon() != null) {
      propertyLabel
          .setIcon(getIconFactory().getIcon(propertyViewDescriptor.getIcon(),
              getIconFactory().getTinyIconSize()));
    }
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
    IView<RComponent> propertyView = super.createPropertyView(
        propertyViewDescriptor, actionHandler, locale);
    if (propertyView != null) {
      if (propertyDescriptor.getName() != null) {
        propertyView.getPeer().setLabel(
            propertyDescriptor.getI18nName(actionHandler, locale));
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
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RActionComponent createRActionComponent(
      IActionViewDescriptor viewDescriptor) {
    RActionComponent component = new RActionComponent(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote action field.
   * 
   * @param showTextField
   *          does it actually show a text field ?
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RActionField createRActionField(
      IPropertyViewDescriptor viewDescriptor, boolean showTextField) {
    RActionField component = new RActionField(getGuidGenerator().generateGUID());
    component.setShowTextField(showTextField);
    return component;
  }

  /**
   * Creates a remote border container.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RBorderContainer createRBorderContainer(
      IBorderViewDescriptor viewDescriptor) {
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
            viewDescriptor.getPermId()));
    return cardContainer;
  }

  /**
   * Creates a remote check box.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RCheckBox createRCheckBox(IPropertyViewDescriptor viewDescriptor) {
    RCheckBox component = new RCheckBox(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote color field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RColorField createRColorField(IPropertyViewDescriptor viewDescriptor) {
    RColorField component = new RColorField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote combo box.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RComboBox createRComboBox(IPropertyViewDescriptor viewDescriptor) {
    RComboBox component = new RComboBox(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote radio box.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RRadioBox createRRadioBox(IPropertyViewDescriptor viewDescriptor) {
    RRadioBox component = new RRadioBox(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote contrained grid container.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RConstrainedGridContainer createRConstrainedGridContainer(
      IConstrainedGridViewDescriptor viewDescriptor) {
    return new RConstrainedGridContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote date field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RDateField createRDateField(IPropertyViewDescriptor viewDescriptor) {
    RDateField component = new RDateField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote decimal field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RDecimalField createRDecimalField(
      IPropertyViewDescriptor viewDescriptor) {
    RDecimalField component = new RDecimalField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote duration field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RDurationField createRDurationField(
      IPropertyViewDescriptor viewDescriptor) {
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
    String renderedProperty = computeRenderedProperty(propertyViewDescriptor);
    IValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(propertyDescriptor.getName(),
            renderedProperty);
    connector.setExceptionHandler(actionHandler);
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
    } else {
      viewComponent = createRActionField(propertyViewDescriptor, true);
    }
    IView<RComponent> view = constructView(viewComponent,
        propertyViewDescriptor, connector);
    if (viewComponent instanceof RActionField) {
      RAction lovAction = createLovAction(view, actionHandler, locale);
      // lovAction.setName(getTranslationProvider().getTranslation(
      // "lov.element.name",
      // new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
      // getTranslationProvider(), locale)}, locale));
      lovAction.setDescription(actionHandler.getTranslation(
          "lov.element.description",
          new Object[] {
            propertyDescriptor.getReferencedDescriptor().getI18nName(
                actionHandler, locale)
          }, locale)
          + IActionFactory.TOOLTIP_ELLIPSIS);
      if (propertyDescriptor.getReferencedDescriptor().getIcon() != null) {
        lovAction.setIcon(getIconFactory().getIcon(
            propertyDescriptor.getReferencedDescriptor().getIcon(),
            getIconFactory().getTinyIconSize()));
      }
      RActionList actionList = new RActionList(getGuidGenerator()
          .generateGUID());
      actionList.setActions(lovAction);
      viewComponent.setActionLists(actionList);
    }
    return view;
  }

  /**
   * Creates a remote even grid container.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected REvenGridContainer createREvenGridContainer(
      IEvenGridViewDescriptor viewDescriptor) {
    return new REvenGridContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote form.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RForm createRForm(IComponentViewDescriptor viewDescriptor) {
    return new RForm(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote html area.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RHtmlArea createRHtmlArea(IPropertyViewDescriptor viewDescriptor) {
    RHtmlArea component = new RHtmlArea(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote image component.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RImageComponent createRImageComponent(
      IPropertyViewDescriptor viewDescriptor) {
    RImageComponent component = new RImageComponent(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote integer field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RIntegerField createRIntegerField(
      IPropertyViewDescriptor viewDescriptor) {
    RIntegerField component = new RIntegerField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote label.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @param bold
   *          make it bold ?
   * @return the created remote component.
   */
  protected RLabel createRLabel(IPropertyViewDescriptor viewDescriptor,
      boolean bold) {
    RLabel component = new RLabel(getGuidGenerator().generateGUID());
    if (bold) {
      component.setFont(createFont(BOLD_FONT));
    }
    return component;
  }

  /**
   * Creates a remote link.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @param bold
   *          make it bold ?
   * @return the created remote component.
   */
  protected RLink createRLink(IPropertyViewDescriptor viewDescriptor,
      boolean bold) {
    RLink component = new RLink(getGuidGenerator().generateGUID());
    if (bold) {
      component.setFont(createFont(BOLD_FONT));
    }
    return component;
  }

  /**
   * Creates a remote list.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RList createRList(IListViewDescriptor viewDescriptor) {
    RList component = new RList(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote password field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RPasswordField createRPasswordField(
      IPropertyViewDescriptor viewDescriptor) {
    RPasswordField component = new RPasswordField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote percent field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RPercentField createRPercentField(
      IPropertyViewDescriptor viewDescriptor) {
    RPercentField component = new RPercentField(getGuidGenerator()
        .generateGUID());
    return component;
  }

  /**
   * Creates a remote split container.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RSplitContainer createRSplitContainer(
      ISplitViewDescriptor viewDescriptor) {
    return new RSplitContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote tab container.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RTabContainer createRTabContainer(ITabViewDescriptor viewDescriptor) {
    return new RTabContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote table.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RTable createRTable(ITableViewDescriptor viewDescriptor) {
    RTable component = new RTable(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote text area.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RTextArea createRTextArea(IPropertyViewDescriptor viewDescriptor) {
    RTextArea component = new RTextArea(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote text field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RTextField createRTextField(IPropertyViewDescriptor viewDescriptor) {
    RTextField component = new RTextField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote time field.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RTimeField createRTimeField(IPropertyViewDescriptor viewDescriptor) {
    RTimeField component = new RTimeField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote tree.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the created remote component.
   */
  protected RTree createRTree(ITreeViewDescriptor viewDescriptor) {
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
    RSplitContainer viewComponent = createRSplitContainer(viewDescriptor);
    viewComponent.setOrientation(viewDescriptor.getOrientation().name());
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
      IActionHandler actionHandler, Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
    } else {
      viewComponent = createRTextField(propertyViewDescriptor);
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
            rowDescriptor.getToHtmlProperty());
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
            rowConnectorPrototype);
    RTable viewComponent = createRTable(viewDescriptor);
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
    List<RComponent> columnHeaders = new ArrayList<RComponent>();
    List<String> columnIds = new ArrayList<String>();
    List<IView<RComponent>> propertyViews = new ArrayList<IView<RComponent>>();
    Map<IPropertyViewDescriptor, Integer> userColumnViewDescriptors = getUserColumnViewDescriptors(
        viewDescriptor, actionHandler);
    for (Map.Entry<IPropertyViewDescriptor, Integer> columnViewDescriptorEntry : userColumnViewDescriptors
        .entrySet()) {
      IPropertyViewDescriptor columnViewDescriptor = columnViewDescriptorEntry
          .getKey();
      if (actionHandler.isAccessGranted(columnViewDescriptor)) {
        IView<RComponent> column = createView(columnViewDescriptor,
            actionHandler, locale);
        column.setParent(view);
        // Do not use standard createColumnConnector method to preserve
        // formatted value connectors.
        // IValueConnector columnConnector = createColumnConnector(columnId,
        // rowDescriptor);
        IValueConnector columnConnector = column.getConnector();
        String propertyName = columnViewDescriptor.getModelDescriptor()
            .getName();
        rowConnectorPrototype.addChildConnector(propertyName, columnConnector);
        boolean locallyWritable = !columnViewDescriptor.isReadOnly();
        if (locallyWritable) {
          try {
            actionHandler.pushToSecurityContext(EAuthorization.ENABLED);
            locallyWritable = actionHandler
                .isAccessGranted(columnViewDescriptor);
          } finally {
            actionHandler.restoreLastSecurityContextSnapshot();
          }
        }
        if (columnViewDescriptor.getAction() != null
            && !columnViewDescriptor.isReadOnly()) {
          for (IValueChangeListener listener : columnConnector
              .getValueChangeListeners()) {
            if (listener instanceof ConnectorActionAdapter) {
              // to avoid the action to be fired by the editor.
              columnConnector.removeValueChangeListener(listener);
            }
          }
          // We must listen for incoming connector value change to trigger the
          // action.
          columnConnector
              .addValueChangeListener(new ConnectorActionAdapter<RComponent, RAction>(
                  columnViewDescriptor.getAction(), getActionFactory(),
                  actionHandler, view));
        }
        columnConnector.setLocallyWritable(locallyWritable);
        IPropertyDescriptor propertyDescriptor = rowDescriptor
            .getPropertyDescriptor(propertyName);
        columns.add(column.getPeer());
        RLabel headerLabel = createPropertyLabel(columnViewDescriptor,
            column.getPeer(), actionHandler, locale);
        columnHeaders.add(headerLabel);
        if (propertyDescriptor.isMandatory()
            && !(propertyDescriptor instanceof IBooleanPropertyDescriptor)) {
          if (columnViewDescriptor.getLabelForeground() == null) {
            headerLabel
                .setForeground(getTableHeaderMandatoryPropertyColorHex());
          }
          headerLabel.setLabel(decorateMandatoryPropertyLabel(headerLabel
              .getLabel()));
        }
        columnIds.add(computeColumnIdentifier(viewDescriptor,
            columnViewDescriptor));
        if (column.getPeer() instanceof RActionable
            && columnViewDescriptor.getAction() != null) {
          RAction action = getActionFactory().createAction(
              columnViewDescriptor.getAction(), actionHandler, view, locale);
          configurePropertyViewAction(columnViewDescriptor, action);
          ((RActionable) column.getPeer()).setAction(action);
        }
        if (columnViewDescriptorEntry.getValue() != null) {
          column.getPeer()
              .setPreferredSize(
                  new Dimension(
                      columnViewDescriptorEntry.getValue(), -1));
        }
        propertyViews.add(column);
      }
    }
    completePropertyViewsWithDynamicToolTips(rowConnectorPrototype,
        propertyViews, rowDescriptor);
    completePropertyViewsWithDynamicBackgrounds(rowConnectorPrototype,
        propertyViews, rowDescriptor);
    completePropertyViewsWithDynamicForegrounds(rowConnectorPrototype,
        propertyViews, rowDescriptor);
    completePropertyViewsWithDynamicFonts(rowConnectorPrototype, propertyViews,
        rowDescriptor);
    viewComponent.setColumns(columns.toArray(new RComponent[columns.size()]));
    viewComponent.setColumnHeaders(columnHeaders.toArray(new RComponent[columnHeaders.size()]));
    viewComponent.setColumnIds(columnIds.toArray(new String[columnIds.size()]));
    viewComponent.setSelectionMode(viewDescriptor.getSelectionMode().name());
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale));
    }

    completeViewWithDynamicBackground(viewComponent, viewDescriptor,
        rowDescriptor, rowConnectorPrototype);
    completeViewWithDynamicForeground(viewComponent, viewDescriptor,
        rowDescriptor, rowConnectorPrototype);
    completeViewWithDynamicFont(viewComponent, viewDescriptor, rowDescriptor,
        rowConnectorPrototype);

    if (rowConnectorPrototype instanceof IRemoteStateOwner) {
      viewComponent
          .setRowPrototype((RemoteCompositeValueState) ((IRemoteStateOwner) rowConnectorPrototype)
              .getState());
    }
    return view;
  }

  private void completeViewWithDynamicBackground(RTable viewComponent,
      ITableViewDescriptor viewDescriptor,
      IComponentDescriptor<?> rowDescriptor,
      ICompositeValueConnector rowConnectorPrototype) {
    String dynamicBackgroundProperty = computeComponentDynamicBackground(
        viewDescriptor, rowDescriptor);
    if (dynamicBackgroundProperty != null) {
      IValueConnector backgroundConnector = rowConnectorPrototype
          .getChildConnector(dynamicBackgroundProperty);
      if (backgroundConnector == null) {
        backgroundConnector = getConnectorFactory().createValueConnector(
            dynamicBackgroundProperty);
        rowConnectorPrototype.addChildConnector(dynamicBackgroundProperty,
            backgroundConnector);
      }
      if (backgroundConnector instanceof IRemoteStateOwner) {
        viewComponent
            .setBackgroundState(((IRemoteStateOwner) backgroundConnector)
                .getState());
      }
    }
  }

  private void completeViewWithDynamicForeground(RTable viewComponent,
      ITableViewDescriptor viewDescriptor,
      IComponentDescriptor<?> rowDescriptor,
      ICompositeValueConnector rowConnectorPrototype) {
    String dynamicForegroundProperty = computeComponentDynamicForeground(
        viewDescriptor, rowDescriptor);
    if (dynamicForegroundProperty != null) {
      IValueConnector foregroundConnector = rowConnectorPrototype
          .getChildConnector(dynamicForegroundProperty);
      if (foregroundConnector == null) {
        foregroundConnector = getConnectorFactory().createValueConnector(
            dynamicForegroundProperty);
        rowConnectorPrototype.addChildConnector(dynamicForegroundProperty,
            foregroundConnector);
      }
      if (foregroundConnector instanceof IRemoteStateOwner) {
        viewComponent
            .setForegroundState(((IRemoteStateOwner) foregroundConnector)
                .getState());
      }
    }
  }

  private void completeViewWithDynamicFont(RTable viewComponent,
      ITableViewDescriptor viewDescriptor,
      IComponentDescriptor<?> rowDescriptor,
      ICompositeValueConnector rowConnectorPrototype) {
    String dynamicFontProperty = computeComponentDynamicFont(viewDescriptor,
        rowDescriptor);
    if (dynamicFontProperty != null) {
      IValueConnector fontConnector = rowConnectorPrototype
          .getChildConnector(dynamicFontProperty);
      if (fontConnector == null) {
        fontConnector = getConnectorFactory().createValueConnector(
            dynamicFontProperty);
        ((RemoteValueConnector) fontConnector)
            .setRemoteStateValueMapper(FONT_MAPPER);
        rowConnectorPrototype.addChildConnector(dynamicFontProperty,
            fontConnector);
      }
      if (fontConnector instanceof IRemoteStateOwner) {
        viewComponent.setFontState(((IRemoteStateOwner) fontConnector)
            .getState());
      }
    }
  }

  private void completePropertyViewsWithDynamicToolTips(
      ICompositeValueConnector connector,
      List<IView<RComponent>> propertyViews,
      IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic tooltips
    for (IView<RComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView
          .getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
          .getModelDescriptor();
      String dynamicToolTipProperty = computePropertyDynamicToolTip(
          modelDescriptor, propertyViewDescriptor, propertyDescriptor);
      // Dynamic tooltip
      if (dynamicToolTipProperty != null) {
        IValueConnector tooltipConnector = connector
            .getChildConnector(dynamicToolTipProperty);
        if (tooltipConnector == null) {
          tooltipConnector = getConnectorFactory().createValueConnector(
              dynamicToolTipProperty);
          connector.addChildConnector(dynamicToolTipProperty, tooltipConnector);
        }
        if (tooltipConnector instanceof IRemoteStateOwner) {
          propertyView.getPeer().setToolTipState(
              ((IRemoteStateOwner) tooltipConnector).getState());
        }
      }
    }
  }

  private void completePropertyViewsWithDynamicBackgrounds(
      ICompositeValueConnector connector,
      List<IView<RComponent>> propertyViews,
      IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic background
    for (IView<RComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView
          .getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
          .getModelDescriptor();
      String dynamicBackgroundProperty = computePropertyDynamicBackground(
          modelDescriptor, propertyViewDescriptor, propertyDescriptor);
      // Dynamic background
      if (dynamicBackgroundProperty != null) {
        IValueConnector backgroundConnector = connector
            .getChildConnector(dynamicBackgroundProperty);
        if (backgroundConnector == null) {
          backgroundConnector = getConnectorFactory().createValueConnector(
              dynamicBackgroundProperty);
          connector.addChildConnector(dynamicBackgroundProperty,
              backgroundConnector);
        }
        if (backgroundConnector instanceof IRemoteStateOwner) {
          propertyView.getPeer().setBackgroundState(
              ((IRemoteStateOwner) backgroundConnector).getState());
        }
      }
    }
  }

  private void completePropertyViewsWithDynamicForegrounds(
      ICompositeValueConnector connector,
      List<IView<RComponent>> propertyViews,
      IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic foreground
    for (IView<RComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView
          .getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
          .getModelDescriptor();
      String dynamicForegroundProperty = computePropertyDynamicForeground(
          modelDescriptor, propertyViewDescriptor, propertyDescriptor);
      // Dynamic foreground
      if (dynamicForegroundProperty != null) {
        IValueConnector foregroundConnector = connector
            .getChildConnector(dynamicForegroundProperty);
        if (foregroundConnector == null) {
          foregroundConnector = getConnectorFactory().createValueConnector(
              dynamicForegroundProperty);
          connector.addChildConnector(dynamicForegroundProperty,
              foregroundConnector);
        }
        if (foregroundConnector instanceof IRemoteStateOwner) {
          propertyView.getPeer().setForegroundState(
              ((IRemoteStateOwner) foregroundConnector).getState());
        }
      }
    }
  }

  private void completePropertyViewsWithDynamicFonts(
      ICompositeValueConnector connector,
      List<IView<RComponent>> propertyViews,
      IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic font
    for (IView<RComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView
          .getDescriptor();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
          .getModelDescriptor();
      String dynamicFontProperty = computePropertyDynamicFont(modelDescriptor,
          propertyViewDescriptor, propertyDescriptor);
      // Dynamic font
      if (dynamicFontProperty != null) {
        IValueConnector fontConnector = connector
            .getChildConnector(dynamicFontProperty);
        if (fontConnector == null) {
          fontConnector = getConnectorFactory().createValueConnector(
              dynamicFontProperty);
          ((RemoteValueConnector) fontConnector)
              .setRemoteStateValueMapper(FONT_MAPPER);
          connector.addChildConnector(dynamicFontProperty, fontConnector);
        }
        if (fontConnector instanceof IRemoteStateOwner) {
          propertyView.getPeer().setFontState(
              ((IRemoteStateOwner) fontConnector).getState());
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    final RTabContainer viewComponent = createRTabContainer(viewDescriptor);
    final BasicIndexedView<RComponent> view = constructIndexedView(
        viewComponent, viewDescriptor);

    viewComponent.addPropertyChangeListener("selectedIndex",
        new PropertyChangeListener() {

          @Override
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
    viewComponent.setTabs(tabs.toArray(new RComponent[tabs.size()]));
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void selectChildViewIndex(RComponent viewComponent, int index) {
    if (viewComponent instanceof RTabContainer) {
      RTabContainer rTab = ((RTabContainer) viewComponent);
      if (rTab.getSelectedIndex() != index) {
        rTab.setSelectedIndex(index);

        RemoteSelectionCommand selectionCommand = new RemoteSelectionCommand();
        selectionCommand.setTargetPeerGuid(rTab.getGuid());
        selectionCommand.setLeadingIndex(index);
        getRemoteCommandHandler().registerCommand(selectionCommand);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTextPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RTextArea viewComponent = createRTextArea(propertyViewDescriptor);
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
    if (view.getPeer() instanceof RTextComponent) {
      if (propertyDescriptor.getMaxLength() != null) {
        ((RTextComponent) view.getPeer()).setMaxLength(propertyDescriptor
            .getMaxLength());
      }
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
    IFormatter<?, String> formatter = createTimeFormatter(propertyDescriptor,
        actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(
          propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter,
          getTimeTemplateValue(propertyDescriptor)));
    } else {
      if (isDateServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(
            propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(
            propertyDescriptor.getName());
        final TimeZone serverTz = TimeZone.getDefault();
        ((RemoteValueConnector) connector)
            .setRemoteStateValueMapper(new IRemoteStateValueMapper() {

              @SuppressWarnings("MagicConstant")
              @Override
              public Object getValueFromState(Object originalValue) {
                Calendar serverCalendar = Calendar.getInstance(serverTz);
                if (originalValue instanceof DateDto) {
                  DateDto stateDate = (DateDto) originalValue;
                  serverCalendar.set(stateDate.getYear(), stateDate.getMonth(),
                      stateDate.getDate(), stateDate.getHour(),
                      stateDate.getMinute(), stateDate.getSecond());
                  serverCalendar.set(Calendar.MILLISECOND, 0);
                  Date connectorDate = serverCalendar.getTime();
                  return connectorDate;
                }
                return originalValue;
              }

              @Override
              public Object getValueForState(Object originalValue) {
                if (originalValue instanceof Date) {
                  Date connectorDate = (Date) originalValue;
                  Calendar serverCalendar = Calendar.getInstance(serverTz);
                  serverCalendar.setTime(connectorDate);

                  DateDto stateDate = new DateDto();
                  stateDate.setYear(serverCalendar.get(Calendar.YEAR));
                  stateDate.setMonth(serverCalendar.get(Calendar.MONTH));
                  stateDate.setDate(serverCalendar.get(Calendar.DATE));
                  stateDate.setHour(serverCalendar.get(Calendar.HOUR_OF_DAY));
                  stateDate.setMinute(serverCalendar.get(Calendar.MINUTE));
                  stateDate.setSecond(serverCalendar.get(Calendar.SECOND));
                  return stateDate;
                }
                return originalValue;
              }
            });
      }
      viewComponent = createRTimeField(propertyViewDescriptor);
      ((RTimeField) viewComponent).setSecondsAware(propertyDescriptor
          .isSecondsAware());
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

    RTree viewComponent = createRTree(viewDescriptor);
    viewComponent.setExpanded(viewDescriptor.isExpanded());
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(getActionFactory().createAction(
          viewDescriptor.getRowAction(), actionHandler, view, locale));
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
    if (actionMap != null && actionHandler.isAccessGranted(actionMap)) {
      try {
        actionHandler.pushToSecurityContext(actionMap);
        List<RActionList> viewActionLists = createViewToolBar(actionMap, view,
            actionHandler, locale);
        view.getPeer().setActionLists(
            viewActionLists.toArray(new RActionList[viewActionLists.size()]));
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
    if (secondaryActionMap != null
        && actionHandler.isAccessGranted(secondaryActionMap)) {
      try {
        actionHandler.pushToSecurityContext(secondaryActionMap);
        List<RActionList> viewActionLists = createViewToolBar(
            secondaryActionMap, view, actionHandler, locale);
        view.getPeer().setSecondaryActionLists(
            viewActionLists.toArray(new RActionList[viewActionLists.size()]));
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
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
    for (ActionList nextActionList : actionMap.getActionLists(actionHandler)) {
      if (actionHandler.isAccessGranted(nextActionList)) {
        try {
          actionHandler.pushToSecurityContext(nextActionList);
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
          actionList.setIcon(getIconFactory().getIcon(nextActionList.getIcon(),
              getIconFactory().getTinyIconSize()));
          viewActionLists.add(actionList);
          List<RAction> actions = new ArrayList<RAction>();
          for (IDisplayableAction action : nextActionList.getActions()) {
            if (actionHandler.isAccessGranted(action)) {
              try {
                actionHandler.pushToSecurityContext(action);
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
              } finally {
                actionHandler.restoreLastSecurityContextSnapshot();
              }
            }
          }
          actionList.setActions(actions.toArray(new RAction[actions.size()]));
        } finally {
          actionHandler.restoreLastSecurityContextSnapshot();
        }
      }
    }
    return viewActionLists;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithBorder(IView<RComponent> view,
      ITranslationProvider translationProvider, Locale locale) {
    view.getPeer().setBorderType(view.getDescriptor().getBorderType().name());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finishComponentConfiguration(IViewDescriptor viewDescriptor,
      ITranslationProvider translationProvider, Locale locale,
      IView<RComponent> view) {
    RComponent viewPeer = view.getPeer();
    configureComponent(viewDescriptor, translationProvider, locale, viewPeer);
  }

  private void configureComponent(IViewDescriptor viewDescriptor,
      ITranslationProvider translationProvider, Locale locale,
      RComponent viewPeer) {
    viewPeer.setLabel(viewDescriptor.getI18nName(translationProvider, locale));
    String viewDescription = viewDescriptor.getI18nDescription(
        translationProvider, locale);
    if (viewDescription != null && viewDescription.length() > 0) {
      viewPeer.setToolTip(viewDescription);
    }
    if (viewDescriptor.getForeground() != null
        && ColorHelper.isColorSpec(viewDescriptor.getForeground())) {
      viewPeer.setForeground(viewDescriptor.getForeground());
    }
    if (viewDescriptor.getBackground() != null
        && ColorHelper.isColorSpec(viewDescriptor.getBackground())) {
      viewPeer.setBackground(viewDescriptor.getBackground());
    }
    if (viewDescriptor.getFont() != null
        && FontHelper.isFontSpec(viewDescriptor.getFont())) {
      viewPeer.setFont(createFont(viewDescriptor.getFont()));
    }
    if (viewDescriptor.getIcon() != null) {
      viewPeer.setIcon(getIconFactory().getIcon(viewDescriptor.getIcon(),
          getIconFactory().getSmallIconSize()));
    } else {
      viewPeer.setIcon(null);
    }
    if (viewDescriptor.getStyleName() != null) {
      viewPeer.setStyleName(viewDescriptor.getStyleName());
    }
  }

  /**
   * Gets the getGuidGenerator().
   * 
   * @return the getGuidGenerator().
   */
  protected IGUIDGenerator<String> getGuidGenerator() {
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
    RemoteValueState cardState = cardsPeer.getState();
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
    if (connector instanceof IPermIdSource) {
      ((IPermIdSource) connector).setPermId(descriptor.getPermId());
    }
    if (viewComponent.getState() == null) {
      viewComponent.setState(((IRemoteStateOwner) connector).getState());
    }
    viewComponent.setPermId(descriptor.getPermId());
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected BasicIndexedView<RComponent> constructIndexedView(
      RComponent viewComponent, ITabViewDescriptor descriptor) {
    BasicIndexedView<RComponent> indexedView = super.constructIndexedView(
        viewComponent, descriptor);
    getRemotePeerRegistry().register(viewComponent);
    viewComponent.setPermId(getRemotePeerRegistry().registerPermId(
        descriptor.getPermId(), viewComponent.getGuid()));
    return indexedView;
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
      textField.setHorizontalAlignment(horizontalAlignment.name());
    }
  }

  private void configureAlignment(RLabel label,
      EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      label.setHorizontalAlignment(horizontalAlignment.name());
    }
  }

  private void configureAlignment(RNumericComponent numericField,
      EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      numericField.setHorizontalAlignment(horizontalAlignment.name());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void focus(RComponent component) {
    RemoteFocusCommand focusCommand = new RemoteFocusCommand();
    focusCommand.setTargetPeerGuid(component.getGuid());
    getRemoteCommandHandler().registerCommand(focusCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void edit(RComponent component) {
    RemoteEditCommand editCommand = new RemoteEditCommand();
    editCommand.setTargetPeerGuid(component.getGuid());
    getRemoteCommandHandler().registerCommand(editCommand);
  }

  /**
   * Configures a property view action by initializing its static context.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor the action is attached to.
   * @param propertyViewAction
   *          the action to configure.
   */
  protected void configurePropertyViewAction(
      IPropertyViewDescriptor propertyViewDescriptor, RAction propertyViewAction) {
    Map<String, Object> staticContext = new HashMap<String, Object>();
    staticContext.put(ActionContextConstants.PROPERTY_VIEW_DESCRIPTOR,
        propertyViewDescriptor);
    propertyViewAction.putValue(IAction.STATIC_CONTEXT_KEY, staticContext);
  }
}
