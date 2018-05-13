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
package org.jspresso.framework.view.remote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.jspresso.framework.application.frontend.command.remote.RemoteAddRepeatedCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.application.view.ControllerAwareViewFactory;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.binding.remote.RemoteValueConnector;
import org.jspresso.framework.gui.remote.IRemoteScrollable;
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
import org.jspresso.framework.gui.remote.RDateField;
import org.jspresso.framework.gui.remote.RDecimalComponent;
import org.jspresso.framework.gui.remote.RDecimalField;
import org.jspresso.framework.gui.remote.RDurationField;
import org.jspresso.framework.gui.remote.REmptyComponent;
import org.jspresso.framework.gui.remote.REnumBox;
import org.jspresso.framework.gui.remote.RForm;
import org.jspresso.framework.gui.remote.RHtmlArea;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.gui.remote.RImageComponent;
import org.jspresso.framework.gui.remote.RIntegerField;
import org.jspresso.framework.gui.remote.RLabel;
import org.jspresso.framework.gui.remote.RLink;
import org.jspresso.framework.gui.remote.RList;
import org.jspresso.framework.gui.remote.RMap;
import org.jspresso.framework.gui.remote.RNumericComponent;
import org.jspresso.framework.gui.remote.RPasswordField;
import org.jspresso.framework.gui.remote.RPercentField;
import org.jspresso.framework.gui.remote.RRadioBox;
import org.jspresso.framework.gui.remote.RRepeater;
import org.jspresso.framework.gui.remote.RSecurityComponent;
import org.jspresso.framework.gui.remote.RTabContainer;
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
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPasswordPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPercentPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.server.remote.RemotePeerRegistryServlet;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteStateValueMapper;
import org.jspresso.framework.state.remote.IRemoteValueStateFactory;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.gui.Font;
import org.jspresso.framework.util.gui.FontHelper;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.image.IScalableImageAware;
import org.jspresso.framework.util.image.ImageHelper;
import org.jspresso.framework.util.lang.DateDto;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicIndexedView;
import org.jspresso.framework.view.BasicMapView;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IIndexedView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.EHorizontalAlignment;
import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IEnumerationPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IHtmlViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IMapViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IRepeaterViewDescriptor;
import org.jspresso.framework.view.descriptor.IScrollableViewDescriptor;
import org.jspresso.framework.view.descriptor.IStaticTextViewDescriptor;
import org.jspresso.framework.view.descriptor.IStringPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicDatePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTimePropertyViewDescriptor;

/**
 * Abstract base implementation for remote views.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public abstract class AbstractRemoteViewFactory extends ControllerAwareViewFactory<RComponent, RIcon, RAction> {

  private boolean                 dateServerParse;
  private boolean                 durationServerParse;
  private IRemoteCommandHandler   remoteCommandHandler;
  private IGUIDGenerator<String>  guidGenerator;
  private boolean                 numberServerParse;
  private IRemotePeerRegistry     remotePeerRegistry;
  private IRemoteStateValueMapper binaryStateValueMapper;
  private IRemoteStateValueMapper enumerationStateValueMapper;
  private PropertyChangeListener  connectorStateListener;

  /**
   * Instantiates a new Abstract remote view factory.
   */
  public AbstractRemoteViewFactory() {
    numberServerParse = false;
    dateServerParse = false;
    durationServerParse = false;
    binaryStateValueMapper = new IRemoteStateValueMapper() {
      @Override
      public Object getValueForState(RemoteValueState state, Object originalValue) {
        if (originalValue instanceof byte[]) {
          String valueForStateUrl = RemotePeerRegistryServlet.computeDownloadUrl(state.getGuid());
          Checksum checksumEngine = new CRC32();
          checksumEngine.update((byte[]) originalValue, 0, ((byte[]) originalValue).length);
          // we must add a check sum so that the client knows when the url
          // content changes.
          valueForStateUrl += ("&cs=" + checksumEngine.getValue());
          return valueForStateUrl;
        }
        return originalValue;
      }

      @Override
      public Object getValueFromState(RemoteValueState state, Object originalValue) {
        return originalValue;
      }
    };
    enumerationStateValueMapper = new IRemoteStateValueMapper() {
      @Override
      public Object getValueForState(RemoteValueState state, Object originalValue) {
        if (originalValue == null) {
          return "";
        }
        return originalValue;
      }

      @Override
      public Object getValueFromState(RemoteValueState state, Object originalValue) {
        if ("".equals(originalValue)) {
          return null;
        }
        return originalValue;
      }
    };

    connectorStateListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        IView<RComponent> view = (IView<RComponent>) evt.getSource();
        RComponent peer = view.getPeer();
        IValueConnector connector = (IValueConnector) evt.getNewValue();
        if (connector instanceof IRemoteStateOwner) {
          peer.setState(((IRemoteStateOwner) connector).getState());
        } else {
          peer.setState(null);
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent applyComponentScrollability(RComponent viewComponent, IScrollableViewDescriptor viewDescriptor) {
    if (viewComponent instanceof IRemoteScrollable) {
      ((IRemoteScrollable) viewComponent).setVerticallyScrollable(viewDescriptor.isVerticallyScrollable());
      ((IRemoteScrollable) viewComponent).setHorizontallyScrollable(viewDescriptor.isHorizontallyScrollable());
    }
    return viewComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createColorPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                      IActionHandler actionHandler, Locale locale) {
    IColorPropertyDescriptor propertyDescriptor = (IColorPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(10);
    } else {
      viewComponent = createRColorField(propertyViewDescriptor);
      String defaultColor = (String) propertyDescriptor.getDefaultValue();
      ((RColorField) viewComponent).setDefaultColor(defaultColor);
      if (defaultColor == null && propertyDescriptor.isMandatory()) {
        ((RColorField) viewComponent).setResetEnabled(false);
      }
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Creates a remote color field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RColorField createRColorField(IPropertyViewDescriptor viewDescriptor) {
    RColorField component = new RColorField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Gets the getGuidGenerator().
   *
   * @return the getGuidGenerator().
   */
  public IGUIDGenerator<String> getGuidGenerator() {
    return guidGenerator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finishComponentConfiguration(IView<RComponent> view, IActionHandler actionHandler,
                                              Locale locale) {
    RComponent viewPeer = view.getPeer();
    IViewDescriptor viewDescriptor = view.getDescriptor();

    viewPeer.setLabel(viewDescriptor.getI18nName(actionHandler, locale));
    String viewDescription = computeViewDescription(viewDescriptor, actionHandler, locale);
    viewPeer.setToolTip(viewDescription);
    if (viewDescriptor.getForeground() != null && ColorHelper.isColorSpec(viewDescriptor.getForeground())) {
      viewPeer.setForeground(viewDescriptor.getForeground());
    }
    if (viewDescriptor.getBackground() != null && ColorHelper.isColorSpec(viewDescriptor.getBackground())) {
      viewPeer.setBackground(viewDescriptor.getBackground());
    }
    if (viewDescriptor.getFont() != null && FontHelper.isFontSpec(viewDescriptor.getFont())) {
      viewPeer.setFont(createFont(viewDescriptor.getFont()));
    }
    if (viewDescriptor.getIcon() != null) {
      viewPeer.setIcon(getIconFactory().getIcon(viewDescriptor.getIcon(), getIconFactory().getSmallIconSize()));
    } else {
      viewPeer.setIcon(null);
    }
    if (viewDescriptor.getStyleName() != null) {
      viewPeer.setStyleName(viewDescriptor.getStyleName());
    }
    viewPeer.setCollapsible(viewDescriptor.isCollapsible());
  }

  private Font createFont(String fontString) {
    Font font = FontHelper.fromString(fontString);
    return font;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createActionView(IActionViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                               Locale locale) {
    IValueConnector connector = getConnectorFactory().createValueConnector(ModelRefPropertyConnector.THIS_PROPERTY);
    connector.setExceptionHandler(actionHandler);
    RActionComponent viewComponent = createRActionComponent(viewDescriptor);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor, connector);
    ActionList actionList = viewDescriptor.getActionList();
    if (actionList != null) {
      ERenderingOptions defaultRenderingOptions = ERenderingOptions.ICON;
      if (viewDescriptor.getRenderingOptions() != null) {
        defaultRenderingOptions = viewDescriptor.getRenderingOptions();
      }
      boolean defaultHideActionWhenDisabled = getDefaultHideActionWhenDisabled();
      if (actionList.getHideActionWhenDisabled() != null) {
        defaultHideActionWhenDisabled = actionList.getHideActionWhenDisabled();
      }
      viewComponent.setActionList(
          createViewActionList(actionList, defaultRenderingOptions, defaultHideActionWhenDisabled, view, actionHandler,
              locale));
    } else {
      RAction action = getActionFactory().createAction(viewDescriptor.getAction(), viewDescriptor.getPreferredSize(),
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
    }
    return view;
  }

  /**
   * Creates a remote button component.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RActionComponent createRActionComponent(IActionViewDescriptor viewDescriptor) {
    RActionComponent component = new RActionComponent(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createIntegerPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IIntegerPropertyDescriptor propertyDescriptor = (IIntegerPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter<Object, String> formatter = createIntegerFormatter(propertyViewDescriptor, propertyDescriptor,
        actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter, getIntegerTemplateValue(propertyDescriptor)));
    } else {
      if (isNumberServerParse() || getOverloadedPattern(propertyViewDescriptor, propertyDescriptor) != null) {
        connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
        viewComponent = createRTextField(propertyViewDescriptor);
      } else {
        connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
        viewComponent = createRIntegerField(propertyViewDescriptor);
      }
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Creates a remote integer field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RIntegerField createRIntegerField(IPropertyViewDescriptor viewDescriptor) {
    RIntegerField component = new RIntegerField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote label.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @param bold
   *     make it bold ?
   * @return the created remote component.
   */
  protected RLabel createRLabel(IPropertyViewDescriptor viewDescriptor, boolean bold) {
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
   *     the component view descriptor.
   * @param bold
   *     make it bold ?
   * @return the created remote component.
   */
  protected RLink createRLink(IPropertyViewDescriptor viewDescriptor, boolean bold) {
    RLink component = new RLink(getGuidGenerator().generateGUID());
    if (bold) {
      component.setFont(createFont(BOLD_FONT));
    }
    return component;
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
  protected void decorateWithBorder(IView<RComponent> view, ITranslationProvider translationProvider, Locale locale) {
    view.getPeer().setBorderType(view.getDescriptor().getBorderType().name());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createHtmlPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {
    IHtmlPropertyDescriptor propertyDescriptor = (IHtmlPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RHtmlArea viewComponent = createRHtmlArea(propertyViewDescriptor);
    viewComponent.setReadOnly(propertyViewDescriptor.isReadOnly());
    if (propertyViewDescriptor instanceof IScrollableViewDescriptor) {
      viewComponent.setVerticallyScrollable(
          ((IScrollableViewDescriptor) propertyViewDescriptor).isVerticallyScrollable());
      viewComponent.setHorizontallyScrollable(
          ((IScrollableViewDescriptor) propertyViewDescriptor).isHorizontallyScrollable());
    } else {
      viewComponent.setVerticallyScrollable(true);
      viewComponent.setHorizontallyScrollable(false);
    }
    if (propertyViewDescriptor instanceof IHtmlViewDescriptor) {
      viewComponent.setEditorConfiguration(((IHtmlViewDescriptor) propertyViewDescriptor).getEditorConfiguration());
    }
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    viewComponent.setAction(
        getActionFactory().createAction(propertyViewDescriptor.getAction(), actionHandler, view, locale));
    return view;
  }

  /**
   * Creates a remote html area.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RHtmlArea createRHtmlArea(IPropertyViewDescriptor viewDescriptor) {
    RHtmlArea component = new RHtmlArea(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Sets the guidGenerator.
   *
   * @param guidGenerator
   *     the guidGenerator to set.
   */
  public void setGuidGenerator(IGUIDGenerator<String> guidGenerator) {
    this.guidGenerator = guidGenerator;
  }

  /**
   * Sets the numberServerParse.
   *
   * @param numberServerParse
   *     the numberServerParse to set.
   */
  public void setNumberServerParse(boolean numberServerParse) {
    this.numberServerParse = numberServerParse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createPasswordPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                         IActionHandler actionHandler, Locale locale) {
    IPasswordPropertyDescriptor propertyDescriptor = (IPasswordPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RPasswordField viewComponent = createRPasswordField(propertyViewDescriptor);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Creates a remote password field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RPasswordField createRPasswordField(IPropertyViewDescriptor viewDescriptor) {
    RPasswordField component = new RPasswordField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createCardView(ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    RCardContainer viewComponent = createRCardContainer(viewDescriptor);
    viewComponent.setState(((IRemoteValueStateFactory) getConnectorFactory())
        .createRemoteValueState(getGuidGenerator().generateGUID(), viewDescriptor.getPermId()));
    List<String> cardNames = new ArrayList<>();
    List<RComponent> cards = new ArrayList<>();
    cardNames.add(ICardViewDescriptor.DEFAULT_CARD);
    cards.add(createEmptyComponent());
    cardNames.add(ICardViewDescriptor.SECURITY_CARD);
    cards.add(createSecurityComponent());

    BasicMapView<RComponent> view = constructMapView(viewComponent, viewDescriptor);

    viewComponent.setCardNames(cardNames.toArray(new String[cardNames.size()]));
    viewComponent.setCards(cards.toArray(new RComponent[cards.size()]));
    view.setConnector(createCardViewConnector(view, actionHandler, locale));
    return view;
  }

  /**
   * Creates a remote card container.
   *
   * @param viewDescriptor
   *     the card view descriptor.
   * @return the created remote component.
   */
  protected RCardContainer createRCardContainer(ICardViewDescriptor viewDescriptor) {
    RCardContainer cardContainer = new RCardContainer(getGuidGenerator().generateGUID());
    return cardContainer;
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
   * Sets the remoteCommandHandler.
   *
   * @param remoteCommandHandler
   *     the remoteCommandHandler to set.
   */
  public void setRemoteCommandHandler(IRemoteCommandHandler remoteCommandHandler) {
    this.remoteCommandHandler = remoteCommandHandler;
  }

  /**
   * Creates a remote text field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RTextField createRTextField(IPropertyViewDescriptor viewDescriptor) {
    RTextField component = new RTextField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createStringPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
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
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    if (viewComponent instanceof RTextField) {
      if (propertyViewDescriptor instanceof IStringPropertyViewDescriptor
          && ((IStringPropertyViewDescriptor) propertyViewDescriptor).getCharacterAction() != null) {
        ((RTextField) viewComponent).setCharacterAction(getActionFactory()
            .createAction(((IStringPropertyViewDescriptor) propertyViewDescriptor).getCharacterAction(), actionHandler,
                view, locale));
      }
    }
    return view;
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
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createPercentPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IPercentPropertyDescriptor propertyDescriptor = (IPercentPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IFormatter<Object, String> formatter = createPercentFormatter(propertyViewDescriptor, propertyDescriptor,
        actionHandler, locale);
    IValueConnector connector;
    RComponent viewComponent;
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter, getPercentTemplateValue(propertyDescriptor)));
    } else {
      if (isNumberServerParse() || getOverloadedPattern(propertyViewDescriptor, propertyDescriptor) != null) {
        connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
        viewComponent = createRTextField(propertyViewDescriptor);
      } else {
        connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
        viewComponent = createRPercentField(propertyViewDescriptor);
        ((RPercentField) viewComponent).setMaxFractionDigit(propertyDescriptor.getMaxFractionDigit());
      }
      connector.setExceptionHandler(actionHandler);
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Creates a remote percent field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RPercentField createRPercentField(IPropertyViewDescriptor viewDescriptor) {
    RPercentField component = new RPercentField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Sets the dateServerParse.
   *
   * @param dateServerParse
   *     the dateServerParse to set.
   */
  public void setDateServerParse(boolean dateServerParse) {
    this.dateServerParse = dateServerParse;
  }

  /**
   * Sets the durationServerParse.
   *
   * @param durationServerParse
   *     the durationServerParse to set.
   */
  public void setDurationServerParse(boolean durationServerParse) {
    this.durationServerParse = durationServerParse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createBinaryPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    IBinaryPropertyDescriptor propertyDescriptor = (IBinaryPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
    if (connector instanceof RemoteValueConnector) {
      final RemoteValueConnector rConnector = (RemoteValueConnector) connector;
      rConnector.setRemoteStateValueMapper(getBinaryStateValueMapper());
    }
    connector.setExceptionHandler(actionHandler);
    RActionField viewComponent = createRActionField(propertyViewDescriptor, false);
    IView<RComponent> propertyView = constructView(viewComponent, propertyViewDescriptor, connector);
    RActionList actionList = createBinaryActionList(propertyView, actionHandler, locale);
    viewComponent.setActionLists(actionList);
    return propertyView;
  }

  /**
   * Create binary action list r action list.
   *
   * @param propertyView
   *     the property view
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   * @return the r action list
   */
  protected RActionList createBinaryActionList(IView<RComponent> propertyView, IActionHandler actionHandler,
                                               Locale locale) {
    RActionList actionList = new RActionList(getGuidGenerator().generateGUID());
    List<RAction> binaryActions = createBinaryActions(propertyView, actionHandler, locale);
    actionList.setActions(binaryActions.toArray(new RAction[binaryActions.size()]));
    return actionList;
  }

  /**
   * Gets binary state value mapper.
   *
   * @return the binary state value mapper
   */
  protected IRemoteStateValueMapper getBinaryStateValueMapper() {
    return binaryStateValueMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createBooleanPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IBooleanPropertyDescriptor propertyDescriptor = (IBooleanPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RCheckBox viewComponent = createRCheckBox(propertyViewDescriptor);
    if (!propertyDescriptor.isMandatory()) {
      viewComponent.setTriState(true);
    }
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDatePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {
    final IDatePropertyDescriptor propertyDescriptor = (IDatePropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    RComponent viewComponent;
    final TimeZone timeZone =
        propertyDescriptor.isTimeZoneAware() ? actionHandler.getClientTimeZone() : actionHandler.getReferenceTimeZone();
    IFormatter<?, String> formatter = createDateFormatter(propertyViewDescriptor, propertyDescriptor, timeZone,
        actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter, getDateTemplateValue(propertyDescriptor)));
    } else {
      if (isDateServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
        ((RemoteValueConnector) connector).setRemoteStateValueMapper(getDateStateValueMapper(timeZone));
      }
      viewComponent = createRDateField(propertyViewDescriptor);
      ((RDateField) viewComponent).setType(propertyDescriptor.getType().name());
      ((RDateField) viewComponent).setSecondsAware(propertyDescriptor.isSecondsAware());
      ((RDateField) viewComponent).setMillisecondsAware(propertyDescriptor.isMillisecondsAware());
      String formatPattern = propertyDescriptor.getFormatPattern();
      if (propertyViewDescriptor instanceof BasicDatePropertyViewDescriptor
          && ((BasicDatePropertyViewDescriptor) propertyViewDescriptor).getFormatPattern() != null) {
        formatPattern = ((BasicDatePropertyViewDescriptor) propertyViewDescriptor).getFormatPattern();
      }
      ((RDateField) viewComponent).setFormatPattern(formatPattern);
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Gets date state value mapper.
   *
   * @param timeZone
   *     the time zone
   * @return the date state value mapper
   */
  protected IRemoteStateValueMapper getDateStateValueMapper(final TimeZone timeZone) {
    return new IRemoteStateValueMapper() {

      @SuppressWarnings("MagicConstant")
      @Override
      public Object getValueFromState(RemoteValueState state, Object originalValue) {
        // We have to use a Date DTO to avoid any
        // transformation by the network layer
        Calendar fieldCalendar = Calendar.getInstance(timeZone);
        if (originalValue instanceof DateDto) {
          DateDto stateDate = (DateDto) originalValue;
          fieldCalendar.set(stateDate.getYear(), stateDate.getMonth(), stateDate.getDate(), stateDate.getHour(),
              stateDate.getMinute(), stateDate.getSecond());
          fieldCalendar.set(Calendar.MILLISECOND, stateDate.getMillisecond());
          if (fieldCalendar.getTime().getTime() >= 0 && fieldCalendar.getTime().getTime() < 24 * 3600 * 1000) {
            // This is a default date. Set it today.
            Calendar today = Calendar.getInstance(timeZone);
            fieldCalendar.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
          }
          Date connectorDate = fieldCalendar.getTime();
          return connectorDate;
        }
        return originalValue;
      }

      @Override
      public Object getValueForState(RemoteValueState state, Object originalValue) {
        if (originalValue instanceof Date) {
          Date connectorDate = (Date) originalValue;
          Calendar fieldCalendar = Calendar.getInstance(timeZone);
          fieldCalendar.setTime(connectorDate);

          DateDto stateDate = new DateDto();
          stateDate.setYear(fieldCalendar.get(Calendar.YEAR));
          stateDate.setMonth(fieldCalendar.get(Calendar.MONTH));
          stateDate.setDate(fieldCalendar.get(Calendar.DATE));
          stateDate.setHour(fieldCalendar.get(Calendar.HOUR_OF_DAY));
          stateDate.setMinute(fieldCalendar.get(Calendar.MINUTE));
          stateDate.setSecond(fieldCalendar.get(Calendar.SECOND));
          stateDate.setMillisecond(fieldCalendar.get(Calendar.MILLISECOND));
          return stateDate;
        }
        return originalValue;
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDecimalPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IDecimalPropertyDescriptor propertyDescriptor = (IDecimalPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentPropertyView(propertyViewDescriptor, actionHandler, locale);
    }
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter<Object, String> formatter = createDecimalFormatter(propertyViewDescriptor, propertyDescriptor,
        actionHandler, locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter, getDecimalTemplateValue(propertyDescriptor)));
    } else {
      if (isNumberServerParse() || getOverloadedPattern(propertyViewDescriptor, propertyDescriptor) != null) {
        connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
        viewComponent = createRTextField(propertyViewDescriptor);
      } else {
        connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
        viewComponent = createRDecimalField(propertyViewDescriptor);
        ((RDecimalComponent) viewComponent).setMaxFractionDigit(propertyDescriptor.getMaxFractionDigit());
      }
      connector.setExceptionHandler(actionHandler);
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDurationPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                         IActionHandler actionHandler, Locale locale) {
    IDurationPropertyDescriptor propertyDescriptor = (IDurationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter<?, String> formatter = createDurationFormatter(propertyViewDescriptor, propertyDescriptor, actionHandler,
        locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter, getDurationTemplateValue(propertyDescriptor)));
    } else {
      if (isDurationServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
      }
      viewComponent = createRDurationField(propertyViewDescriptor);
      if (propertyDescriptor.getMaxMillis() != null) {
        ((RDurationField) viewComponent).setMaxMillis(propertyDescriptor.getMaxMillis());
      } else {
        ((RDurationField) viewComponent).setMaxMillis(-1);
      }
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createEnumerationPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                            IActionHandler actionHandler, Locale locale) {
    IEnumerationPropertyDescriptor propertyDescriptor = (IEnumerationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter<Object, String> formatter = createEnumerationFormatter(propertyDescriptor, actionHandler, locale);
    // it's important to keep this special case for enumeration property views that are read-only.
    // r/o enumeration property views, if not assigned an action, should be transmitted as a read-only combo-box.
    // If not, enumeration images will disappear (see bug #1200)
    if (propertyViewDescriptor.isReadOnly() && propertyViewDescriptor.getAction() != null) {
      connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      viewComponent = createRLink(propertyViewDescriptor, false);
      ((RLabel) viewComponent).setMaxLength(
          getFormatLength(formatter, getEnumerationTemplateValue(propertyDescriptor, actionHandler, locale)));
    } else {
      connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
      List<String> values = new ArrayList<>();
      List<String> translations = new ArrayList<>();
      List<String> enumerationValues = new ArrayList<>(propertyDescriptor.getEnumerationValues());
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
            translations.add(propertyDescriptor.getI18nValue(value, actionHandler, locale));
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
          && ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor).isRadio()) {
        viewComponent = createRRadioBox(propertyViewDescriptor);
        ((RRadioBox) viewComponent).setOrientation(
            ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor).getOrientation().name());
      } else {
        viewComponent = createRComboBox(propertyViewDescriptor);
        ((RComboBox) viewComponent).setReadOnly(propertyViewDescriptor.isReadOnly());
        List<RIcon> icons = new ArrayList<>();
        for (String value : enumerationValues) {
          icons.add(
              getIconFactory().getIcon(propertyDescriptor.getIconImageURL(value), getIconFactory().getTinyIconSize()));
        }
        ((RComboBox) viewComponent).setIcons(icons.toArray(new RIcon[icons.size()]));
      }
      ((REnumBox) viewComponent).setValues(values.toArray(new String[values.size()]));
      ((REnumBox) viewComponent).setTranslations(translations.toArray(new String[translations.size()]));
    }
    ((IRemoteStateOwner) connector).setRemoteStateValueMapper(getEnumerationStateValueMapper());
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * Gets enumeration state value mapper.
   *
   * @return the enumeration state value mapper
   */
  protected IRemoteStateValueMapper getEnumerationStateValueMapper() {
    return enumerationStateValueMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createImagePropertyView(final IPropertyViewDescriptor propertyViewDescriptor,
                                                      IActionHandler actionHandler, Locale locale) {
    final IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyViewDescriptor.getModelDescriptor().getName());
    connector.setExceptionHandler(actionHandler);
    if (connector instanceof RemoteValueConnector) {
      final RemoteValueConnector rConnector = (RemoteValueConnector) connector;
      rConnector.setRemoteStateValueMapper(new IRemoteStateValueMapper() {

        @Override
        public Object getValueForState(RemoteValueState state, Object originalValue) {
          if (originalValue != null) {
            Integer scaledWidth = null;
            Integer scaledHeight = null;
            if (propertyViewDescriptor instanceof IScalableImageAware) {
              scaledWidth = ((IScalableImageAware) propertyViewDescriptor).getScaledWidth();
              scaledHeight = ((IScalableImageAware) propertyViewDescriptor).getScaledHeight();
            } else if (propertyDescriptor instanceof IScalableImageAware) {
              scaledWidth = ((IScalableImageAware) propertyDescriptor).getScaledWidth();
              scaledHeight = ((IScalableImageAware) propertyDescriptor).getScaledHeight();
            }
            String valueForStateUrl = null;
            if (originalValue instanceof byte[] || scaledWidth != null || scaledHeight != null) {
              valueForStateUrl = RemotePeerRegistryServlet.computeImageDownloadUrl(state.getGuid(), scaledWidth,
                  scaledHeight);
              byte[] checksumSource = null;
              if (originalValue instanceof byte[]) {
                checksumSource = ((byte[]) originalValue);
              } else if (originalValue instanceof String) {
                checksumSource = ((String) originalValue).getBytes();
              }
              if (checksumSource != null) {
                Checksum checksumEngine = new CRC32();
                checksumEngine.update(checksumSource, 0, checksumSource.length);
                // we must add a check sum so that the client knows when the url
                // content changes.
                valueForStateUrl += ("&cs=" + checksumEngine.getValue());
              }
            } else if (originalValue instanceof String) {
              valueForStateUrl = ResourceProviderServlet.computeLocalResourceDownloadUrl((String) originalValue);
            }
            return valueForStateUrl;
          }
          return null;
        }

        @Override
        public Object getValueFromState(RemoteValueState state, Object originalValue) {
          if (originalValue instanceof String && ((String) originalValue).contains("base64")) {
            return ImageHelper.fromBase64Src((String) originalValue);
          }
          return originalValue;
        }
      });
    }
    RImageComponent viewComponent = createRImageComponent(propertyViewDescriptor);
    if (propertyViewDescriptor instanceof IScrollableViewDescriptor) {
      viewComponent.setScrollable(((IScrollableViewDescriptor) propertyViewDescriptor).isScrollable());
    } else {
      viewComponent.setScrollable(false);
    }
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    if (propertyViewDescriptor.getAction() != null) {
      RAction action = getActionFactory().createAction(propertyViewDescriptor.getAction(), actionHandler, view, locale);
      configurePropertyViewAction(propertyViewDescriptor, action);
      viewComponent.setAction(action);
    }
    if (propertyDescriptor instanceof IBinaryPropertyDescriptor && !propertyViewDescriptor.isReadOnly()) {
      viewComponent.setActionLists(createBinaryActionList(view, actionHandler, locale));
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createStaticTextPropertyView(IStaticTextViewDescriptor propertyViewDescriptor,
                                                           IActionHandler actionHandler, Locale locale) {
    RTextComponent viewComponent;
    if (propertyViewDescriptor.isMultiLine()) {
      viewComponent = createRHtmlArea(propertyViewDescriptor);
      ((RHtmlArea) viewComponent).setReadOnly(true);
    } else if (propertyViewDescriptor.getAction() != null) {
      viewComponent = createRLink(propertyViewDescriptor, false);
    } else {
      viewComponent = createRLabel(propertyViewDescriptor, false);
    }
    IValueConnector connector = getConnectorFactory().createValueConnector(null);
    connector.setExceptionHandler(actionHandler);
    connector.setConnectorValue(actionHandler.getTranslation(propertyViewDescriptor.getI18nTextKey(), locale));
    IView<RComponent> propertyView = constructView(viewComponent, propertyViewDescriptor, connector);
    return propertyView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createNumberPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    INumberPropertyDescriptor propertyDescriptor = (INumberPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IView<RComponent> view = super.createNumberPropertyView(propertyViewDescriptor, actionHandler, locale);
    if (view.getPeer() instanceof RNumericComponent) {
      ((RNumericComponent) view.getPeer()).setMaxValue(propertyDescriptor.getMaxValue());
      ((RNumericComponent) view.getPeer()).setMinValue(propertyDescriptor.getMinValue());
      ((RNumericComponent) view.getPeer()).setThousandsGroupingUsed(propertyDescriptor.isThousandsGroupingUsed());
    }
    if (view.getConnector() instanceof RemoteValueConnector) {
      final RemoteValueConnector rConnector = (RemoteValueConnector) view.getConnector();
      rConnector.setRemoteStateValueMapper(new IRemoteStateValueMapper() {

        @Override
        public Object getValueForState(RemoteValueState state, Object originalValue) {
          if (originalValue instanceof BigDecimal) {
            return ((BigDecimal) originalValue).doubleValue();
          }
          if (originalValue instanceof BigInteger) {
            return ((BigInteger) originalValue).longValue();
          }
          return originalValue;
        }

        @Override
        public Object getValueFromState(RemoteValueState state, Object originalValue) {
          return originalValue;
        }
      });
    }
    return view;
  }

  /**
   * Override to set the property view name that will be useful on client-side.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                 IActionHandler actionHandler, Locale locale) {
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IView<RComponent> propertyView = super.createPropertyView(propertyViewDescriptor, actionHandler, locale);
    if (propertyView != null) {
      RComponent peer = propertyView.getPeer();
      if (propertyDescriptor != null && propertyDescriptor.getName() != null) {
        peer.setLabel(propertyDescriptor.getI18nName(actionHandler, locale));
      }
      if (peer instanceof RLabel) {
        configureAlignment((RLabel) peer, propertyViewDescriptor.getHorizontalAlignment());
      } else if (peer instanceof RTextField) {
        configureAlignment((RTextField) peer, propertyViewDescriptor.getHorizontalAlignment());
      } else if (peer instanceof RNumericComponent) {
        configureAlignment((RNumericComponent) peer, propertyViewDescriptor.getHorizontalAlignment());
      } else if (peer instanceof RImageComponent) {
        configureAlignment((RImageComponent) peer, propertyViewDescriptor.getHorizontalAlignment());
      }
      if (propertyViewDescriptor.getFocusGainedAction() != null) {
        peer.setFocusGainedAction(getActionFactory()
            .createAction(propertyViewDescriptor.getFocusGainedAction(), actionHandler, propertyView, locale));
      }
      if (propertyViewDescriptor.getFocusLostAction() != null) {
        peer.setFocusLostAction(getActionFactory()
            .createAction(propertyViewDescriptor.getFocusLostAction(), actionHandler, propertyView, locale));
      }
    }
    return propertyView;
  }

  /**
   * Creates a remote action field.
   *
   * @param showTextField
   *     does it actually show a text field ?
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RActionField createRActionField(IPropertyViewDescriptor viewDescriptor, boolean showTextField) {
    RActionField component = new RActionField(getGuidGenerator().generateGUID());
    component.setShowTextField(showTextField);
    return component;
  }

  /**
   * Creates a remote check box.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RCheckBox createRCheckBox(IPropertyViewDescriptor viewDescriptor) {
    RCheckBox component = new RCheckBox(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote combo box.
   *
   * @param viewDescriptor
   *     the component view descriptor.
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
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RRadioBox createRRadioBox(IPropertyViewDescriptor viewDescriptor) {
    RRadioBox component = new RRadioBox(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote date field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
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
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RDecimalField createRDecimalField(IPropertyViewDescriptor viewDescriptor) {
    RDecimalField component = new RDecimalField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote duration field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RDurationField createRDurationField(IPropertyViewDescriptor viewDescriptor) {
    RDurationField component = new RDurationField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote image component.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RImageComponent createRImageComponent(IPropertyViewDescriptor viewDescriptor) {
    RImageComponent component = new RImageComponent(getGuidGenerator().generateGUID());
    return component;
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

  private void configureAlignment(RTextField textField, EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      textField.setHorizontalAlignment(horizontalAlignment.name());
    }
  }

  private void configureAlignment(RLabel label, EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      label.setHorizontalAlignment(horizontalAlignment.name());
    }
  }

  private void configureAlignment(RNumericComponent numericField, EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      numericField.setHorizontalAlignment(horizontalAlignment.name());
    }
  }

  private void configureAlignment(RImageComponent image, EHorizontalAlignment horizontalAlignment) {
    if (horizontalAlignment != null) {
      image.setHorizontalAlignment(horizontalAlignment.name());
    }
  }

  /**
   * Creates a remote text area.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RTextArea createRTextArea(IPropertyViewDescriptor viewDescriptor) {
    RTextArea component = new RTextArea(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote time field.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RTimeField createRTimeField(IPropertyViewDescriptor viewDescriptor) {
    RTimeField component = new RTimeField(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTextPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {
    ITextPropertyDescriptor propertyDescriptor = (ITextPropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IValueConnector connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RTextArea viewComponent = createRTextArea(propertyViewDescriptor);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTextualPropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    IView<RComponent> view = super.createTextualPropertyView(propertyViewDescriptor, actionHandler, locale);
    RComponent viewPeer = view.getPeer();
    if (viewPeer instanceof RTextComponent) {
      Integer maxLength = propertyDescriptor.getMaxLength();
      if (maxLength != null && maxLength > 0) {
        ((RTextComponent) viewPeer).setMaxLength(maxLength);
      }
    }
    if (viewPeer instanceof RLabel || viewPeer instanceof RLink) {
      IValueConnector viewConnector = view.getConnector();
      if (viewConnector instanceof IRemoteStateOwner) {
        final IRemoteStateValueMapper remoteStateValueMapper = ((IRemoteStateOwner) viewConnector)
            .currentRemoteStateValueMapper();
        IRemoteStateValueMapper sanitizingValueMapper = new IRemoteStateValueMapper() {
          @Override
          public Object getValueForState(RemoteValueState state, Object originalValue) {
            Object valueForState = originalValue;
            if (remoteStateValueMapper != null) {
              valueForState = remoteStateValueMapper.getValueForState(state, originalValue);
            }
            if (valueForState instanceof String) {
              valueForState = HtmlHelper.sanitizeHtml((String) valueForState);
            }
            return valueForState;
          }

          @Override
          public Object getValueFromState(RemoteValueState state, Object originalValue) {
            Object valueFromState = originalValue;
            if (remoteStateValueMapper != null) {
              valueFromState = remoteStateValueMapper.getValueFromState(state, originalValue);
            }
            return valueFromState;
          }
        };
        ((IRemoteStateOwner) viewConnector).setRemoteStateValueMapper(sanitizingValueMapper);
      }
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTimePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                     IActionHandler actionHandler, Locale locale) {
    ITimePropertyDescriptor propertyDescriptor = (ITimePropertyDescriptor) propertyViewDescriptor.getModelDescriptor();
    IValueConnector connector;
    RComponent viewComponent;
    IFormatter<?, String> formatter = createTimeFormatter(propertyViewDescriptor, propertyDescriptor, actionHandler,
        locale);
    if (propertyViewDescriptor.isReadOnly()) {
      connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      if (propertyViewDescriptor.getAction() != null) {
        viewComponent = createRLink(propertyViewDescriptor, false);
      } else {
        viewComponent = createRLabel(propertyViewDescriptor, false);
      }
      ((RLabel) viewComponent).setMaxLength(getFormatLength(formatter, getTimeTemplateValue(propertyDescriptor)));
    } else {
      if (isDateServerParse()) {
        connector = getConnectorFactory().createFormattedValueConnector(propertyDescriptor.getName(), formatter);
      } else {
        connector = getConnectorFactory().createValueConnector(propertyDescriptor.getName());
        final TimeZone serverTz = actionHandler.getReferenceTimeZone();
        ((RemoteValueConnector) connector).setRemoteStateValueMapper(new IRemoteStateValueMapper() {

          @SuppressWarnings("MagicConstant")
          @Override
          public Object getValueFromState(RemoteValueState state, Object originalValue) {
            Calendar serverCalendar = Calendar.getInstance(serverTz);
            if (originalValue instanceof DateDto) {
              DateDto stateDate = (DateDto) originalValue;
              serverCalendar.set(stateDate.getYear(), stateDate.getMonth(), stateDate.getDate(), stateDate.getHour(),
                  stateDate.getMinute(), stateDate.getSecond());
              serverCalendar.set(Calendar.MILLISECOND, stateDate.getMillisecond());
              Date connectorDate = serverCalendar.getTime();
              return connectorDate;
            }
            return originalValue;
          }

          @Override
          public Object getValueForState(RemoteValueState state, Object originalValue) {
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
              stateDate.setMillisecond(serverCalendar.get(Calendar.MILLISECOND));
              return stateDate;
            }
            return originalValue;
          }
        });
      }
      viewComponent = createRTimeField(propertyViewDescriptor);
      ((RTimeField) viewComponent).setSecondsAware(propertyDescriptor.isSecondsAware());
      ((RTimeField) viewComponent).setMillisecondsAware(propertyDescriptor.isMillisecondsAware());
      String formatPattern = propertyDescriptor.getFormatPattern();
      if (propertyViewDescriptor instanceof BasicTimePropertyViewDescriptor
          && ((BasicTimePropertyViewDescriptor) propertyViewDescriptor).getFormatPattern() != null) {
        formatPattern = ((BasicTimePropertyViewDescriptor) propertyViewDescriptor).getFormatPattern();
      }
      ((RTimeField) viewComponent).setFormatPattern(formatPattern);
    }
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void addCard(IMapView<RComponent> cardView, IView<RComponent> card, String cardName) {
    cardView.addToChildrenMap(cardName, card);

    RCardContainer cardContainer = (RCardContainer) cardView.getPeer();

    RComponent[] newCards = new RComponent[cardContainer.getCards().length + 1];
    System.arraycopy(cardContainer.getCards(), 0, newCards, 0, cardContainer.getCards().length);
    newCards[newCards.length - 1] = card.getPeer();
    cardContainer.setCards(newCards);

    String[] newCardNames = new String[cardContainer.getCardNames().length + 1];
    System.arraycopy(cardContainer.getCardNames(), 0, newCardNames, 0, cardContainer.getCardNames().length);
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
  protected IView<RComponent> createComponentView(IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                  Locale locale) {
    IComponentDescriptor<?> modelDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor.getModelDescriptor())
        .getComponentDescriptor();
    // Dynamic toolTips
    ICompositeValueConnector connector = getConnectorFactory().createCompositeValueConnector(
        getConnectorIdForBeanView(viewDescriptor), modelDescriptor.getToHtmlProperty());
    RForm viewComponent = createRForm(viewDescriptor);
    viewComponent.setWidthResizeable(viewDescriptor.isWidthResizeable());
    viewComponent.setColumnCount(viewDescriptor.getColumnCount());
    viewComponent.setLabelsPosition(viewDescriptor.getLabelsPosition().name());

    List<Integer> elementWidths = new ArrayList<>();
    List<RComponent> elements = new ArrayList<>();
    List<RComponent> elementLabels = new ArrayList<>();
    List<String> labelHorizontalPositions = new ArrayList<>();
    List<IView<RComponent>> propertyViews = new ArrayList<>();

    IView<RComponent> view = constructView(viewComponent, viewDescriptor, connector);

    for (IPropertyViewDescriptor propertyViewDescriptor : viewDescriptor.getPropertyViewDescriptors()) {
      if (isAllowedForClientType(propertyViewDescriptor, actionHandler)) {
        String propertyName = null;
        IPropertyDescriptor propertyDescriptor = null;
        IModelDescriptor propertyModelDescriptor = propertyViewDescriptor.getModelDescriptor();
        if (propertyModelDescriptor != null) {
          propertyName = propertyModelDescriptor.getName();
          propertyDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor.getModelDescriptor())
              .getComponentDescriptor().getPropertyDescriptor(propertyName);
          if (propertyDescriptor == null) {
            throw new ViewException(
                "Property descriptor [" + propertyName + "] does not exist for model descriptor " + viewDescriptor
                    .getModelDescriptor().getName() + ".");
          }
        }
        IView<RComponent> propertyView = createView(propertyViewDescriptor, actionHandler, locale);
        // Fix bug 782
        propertyView.getPeer().setIcon(null);
        propertyView.setParent(view);

        boolean forbidden = !isPropertyViewAccessGranted(propertyViewDescriptor, modelDescriptor, actionHandler);
        if (forbidden) {
          RComponent securityComponent = createSecurityComponent();
          securityComponent.setPreferredSize(new Dimension(1, 1));
          propertyView.setPeer(securityComponent);
        } else {
          propertyViews.add(propertyView);
        }
        elements.add(propertyView.getPeer());
        RLabel propertyLabel = createFormPropertyLabel(actionHandler, locale, propertyViewDescriptor,
            propertyDescriptor, propertyView, forbidden);
        elementLabels.add(propertyLabel);
        elementWidths.add(propertyViewDescriptor.getWidth());
        EHorizontalPosition labelHorizontalPosition = propertyViewDescriptor.getLabelHorizontalPosition();
        if (labelHorizontalPosition == null) {
          labelHorizontalPosition = viewDescriptor.getLabelsHorizontalPosition();
        }
        if (labelHorizontalPosition == null) {
          labelHorizontalPosition = EHorizontalPosition.LEFT;
        }
        labelHorizontalPositions.add(labelHorizontalPosition.name());
        if (propertyName != null) {
          connector.addChildConnector(propertyName, propertyView.getConnector());
        }
        // already handled in createView.
        // if (propertyViewDescriptor.getReadabilityGates() != null) {
        // if (propertyViewDescriptor.getWritabilityGates() != null) {
        // propertyView.getConnector().setLocallyWritable(
        // !propertyViewDescriptor.isReadOnly());
        if (propertyView.getPeer() instanceof RActionable && propertyViewDescriptor.getAction() != null) {
          IView<RComponent> targetView;
          if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
            targetView = propertyView;
          } else {
            targetView = view;
          }
          RAction action = getActionFactory().createAction(propertyViewDescriptor.getAction(), actionHandler,
              targetView, locale);
          configurePropertyViewAction(propertyViewDescriptor, action);
          ((RActionable) propertyView.getPeer()).setAction(action);
        }
      }
    }
    completePropertyViewsWithDynamicToolTips(connector, propertyViews, modelDescriptor);
    completePropertyViewsWithDynamicBackgrounds(connector, propertyViews, modelDescriptor);
    completePropertyViewsWithDynamicForegrounds(connector, propertyViews, modelDescriptor);
    completePropertyViewsWithDynamicFonts(connector, propertyViews, modelDescriptor);
    viewComponent.setElementWidths(elementWidths.toArray(new Integer[elementWidths.size()]));
    viewComponent.setElements(elements.toArray(new RComponent[elements.size()]));
    viewComponent.setElementLabels(elementLabels.toArray(new RComponent[elementLabels.size()]));
    viewComponent.setLabelHorizontalPositions(
        labelHorizontalPositions.toArray(new String[labelHorizontalPositions.size()]));
    if (viewDescriptor.isScrollable()) {
      view.setPeer(applyComponentScrollability(viewComponent, viewDescriptor));
    }
    return view;
  }

  private RLabel createFormPropertyLabel(IActionHandler actionHandler, Locale locale,
                                         IPropertyViewDescriptor propertyViewDescriptor,
                                         IPropertyDescriptor propertyDescriptor, IView<RComponent> propertyView,
                                         boolean forbidden) {
    RLabel propertyLabel = createPropertyLabel(propertyViewDescriptor, propertyView.getPeer(), actionHandler, locale);
    if (!propertyViewDescriptor.isReadOnly() && propertyDescriptor != null && propertyDescriptor.isMandatory()
        && !(propertyDescriptor instanceof IBooleanPropertyDescriptor)) {
      if (propertyViewDescriptor.getLabelForeground() == null) {
        propertyLabel.setForeground(getFormLabelMandatoryPropertyColorHex());
      }
      propertyLabel.setLabel(decorateMandatoryPropertyLabel(propertyLabel.getLabel()));
    }
    if (forbidden) {
      propertyLabel.setLabel(" ");
      propertyLabel.setIcon(null);
    }
    return propertyLabel;
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
  protected RLabel createPropertyLabel(IPropertyViewDescriptor propertyViewDescriptor, RComponent propertyComponent,
                                       ITranslationProvider translationProvider, Locale locale) {
    RLabel propertyLabel = createRLabel(null, false);
    String labelText = propertyViewDescriptor.getI18nName(translationProvider, locale);
    propertyLabel.setLabel(labelText);
    configurePropertyLabel(propertyLabel, propertyViewDescriptor);
    return propertyLabel;
  }

  private void configurePropertyLabel(RLabel propertyLabel, IPropertyViewDescriptor propertyViewDescriptor) {
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
      propertyLabel.setIcon(
          getIconFactory().getIcon(propertyViewDescriptor.getIcon(), getIconFactory().getTinyIconSize()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createMapView(IMapViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                            Locale locale) {
    RMap viewComponent = createRMap(viewDescriptor);
    viewComponent.setDefaultZoom(viewDescriptor.getDefaultZoom());
    String connectorId = ModelRefPropertyConnector.THIS_PROPERTY;
    if (viewDescriptor.getModelDescriptor() instanceof IPropertyDescriptor) {
      connectorId = viewDescriptor.getModelDescriptor().getName();
    }
    ICompositeValueConnector connector = getConnectorFactory().createCompositeValueConnector(connectorId, null);
    connector.addChildConnector(getConnectorFactory().createValueConnector(viewDescriptor.getMapContentProperty()));
    connector.setExceptionHandler(actionHandler);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor, connector);
    return view;
  }

  /**
   * Creates a remote map.
   *
   * @param viewDescriptor
   *     the map view descriptor.
   * @return the created remote component.
   */
  protected RMap createRMap(IMapViewDescriptor viewDescriptor) {
    return new RMap(getGuidGenerator().generateGUID());
  }


  /**
   * Creates a remote form.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RForm createRForm(IComponentViewDescriptor viewDescriptor) {
    return new RForm(getGuidGenerator().generateGUID());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent createSecurityComponent() {
    RComponent securityComponent = new RSecurityComponent(getGuidGenerator().generateGUID());
    securityComponent.setState(new RemoteValueState(getGuidGenerator().generateGUID()));
    return securityComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyPreferredSize(RComponent component, Dimension preferredSize) {
    if (preferredSize != null) {
      component.setPreferredSize(preferredSize);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent createEmptyComponent() {
    RComponent emptyComponent = new REmptyComponent(getGuidGenerator().generateGUID());
    emptyComponent.setState(new RemoteValueState(getGuidGenerator().generateGUID()));
    return emptyComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createSourceCodePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                           IActionHandler actionHandler, Locale locale) {
    return createTextPropertyView(propertyViewDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createReferencePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                          IActionHandler actionHandler, Locale locale) {
    IReferencePropertyDescriptor<?> propertyDescriptor = (IReferencePropertyDescriptor<?>) propertyViewDescriptor
        .getModelDescriptor();
    String renderedProperty = computeRenderedProperty(propertyViewDescriptor);
    IValueConnector connector = getConnectorFactory().createCompositeValueConnector(propertyDescriptor.getName(),
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
    IView<RComponent> view = constructView(viewComponent, propertyViewDescriptor, connector);
    if (viewComponent instanceof RActionField) {
      if (propertyViewDescriptor instanceof IReferencePropertyViewDescriptor) {
        ((RActionField) viewComponent).setFieldEditable(
            ((IReferencePropertyViewDescriptor) propertyViewDescriptor).isAutoCompleteEnabled());
      } else {
        ((RActionField) viewComponent).setFieldEditable(true);
      }
      RAction lovAction = createLovAction(view, actionHandler, locale);
      lovAction.setDescription(actionHandler.getTranslation("lov.element.description",
          new Object[]{propertyDescriptor.getReferencedDescriptor().getI18nName(actionHandler, locale)}, locale)
          + IActionFactory.TOOLTIP_ELLIPSIS);
      RActionList actionList = new RActionList(getGuidGenerator().generateGUID());
      actionList.setActions(lovAction);
      viewComponent.setActionLists(actionList);
      if (propertyViewDescriptor instanceof IStringPropertyViewDescriptor
          && ((IStringPropertyViewDescriptor) propertyViewDescriptor).getCharacterAction() != null) {
        ((RActionField) viewComponent).setCharacterAction(getActionFactory()
            .createAction(((IStringPropertyViewDescriptor) propertyViewDescriptor).getCharacterAction(), actionHandler,
                view, locale));
      }
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createListView(IListViewDescriptor viewDescriptor, IActionHandler actionHandler,
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
    RList viewComponent = createRList(viewDescriptor);
    viewComponent.setDisplayIcon(viewDescriptor.isDisplayIcon());
    IView<RComponent> view = constructView(viewComponent, viewDescriptor, connector);

    String renderedProperty = viewDescriptor.getRenderedProperty();
    if (renderedProperty != null) {
      IValueConnector cellConnector = createListConnector(renderedProperty, rowDescriptor);
      rowConnectorPrototype.addChildConnector(renderedProperty, cellConnector);
    }
    viewComponent.setSelectionMode(viewDescriptor.getSelectionMode().name());
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(
          getActionFactory().createAction(viewDescriptor.getRowAction(), actionHandler, view, locale));
    }
    return view;
  }

  /**
   * Creates a remote list.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RList createRList(IListViewDescriptor viewDescriptor) {
    RList component = new RList(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * Creates a remote tree.
   *
   * @param viewDescriptor
   *     the component view descriptor.
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
  protected ICompositeView<RComponent> createBorderView(IBorderViewDescriptor viewDescriptor,
                                                        IActionHandler actionHandler, Locale locale) {
    RBorderContainer viewComponent = createRBorderContainer(viewDescriptor);
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();

    if (viewDescriptor.getNorthViewDescriptor() != null) {
      IView<RComponent> northView = createView(viewDescriptor.getNorthViewDescriptor(), actionHandler, locale);
      viewComponent.setNorth(northView.getPeer());
      childrenViews.add(northView);
    }
    if (viewDescriptor.getWestViewDescriptor() != null) {
      IView<RComponent> westView = createView(viewDescriptor.getWestViewDescriptor(), actionHandler, locale);
      viewComponent.setWest(westView.getPeer());
      childrenViews.add(westView);
    }
    if (viewDescriptor.getCenterViewDescriptor() != null) {
      IView<RComponent> centerView = createView(viewDescriptor.getCenterViewDescriptor(), actionHandler, locale);
      viewComponent.setCenter(centerView.getPeer());
      childrenViews.add(centerView);
    }
    if (viewDescriptor.getEastViewDescriptor() != null) {
      IView<RComponent> eastView = createView(viewDescriptor.getEastViewDescriptor(), actionHandler, locale);
      viewComponent.setEast(eastView.getPeer());
      childrenViews.add(eastView);
    }
    if (viewDescriptor.getSouthViewDescriptor() != null) {
      IView<RComponent> southView = createView(viewDescriptor.getSouthViewDescriptor(), actionHandler, locale);
      viewComponent.setSouth(southView.getPeer());
      childrenViews.add(southView);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * Creates a remote border container.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RBorderContainer createRBorderContainer(IBorderViewDescriptor viewDescriptor) {
    return new RBorderContainer(getGuidGenerator().generateGUID());
  }

  protected void completePropertyViewsWithDynamicToolTips(ICompositeValueConnector connector,
                                                          List<IView<RComponent>> propertyViews,
                                                          IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic tooltips
    for (IView<RComponent> propertyView : propertyViews) {
      IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView.getDescriptor();
      completeViewWithDynamicToolTip(propertyView.getPeer(), propertyView.getDescriptor(), modelDescriptor, connector);
    }
  }

  protected void completePropertyViewsWithDynamicBackgrounds(ICompositeValueConnector connector,
                                                             List<IView<RComponent>> propertyViews,
                                                             IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic background
    for (IView<RComponent> propertyView : propertyViews) {
      completeViewWithDynamicBackground(propertyView.getPeer(), propertyView.getDescriptor(), modelDescriptor,
          connector);
    }
  }

  protected void completePropertyViewsWithDynamicForegrounds(ICompositeValueConnector connector,
                                                             List<IView<RComponent>> propertyViews,
                                                             IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic foreground
    for (IView<RComponent> propertyView : propertyViews) {
      completeViewWithDynamicForeground(propertyView.getPeer(), propertyView.getDescriptor(), modelDescriptor,
          connector);
    }
  }

  protected void completePropertyViewsWithDynamicFonts(ICompositeValueConnector connector,
                                                       List<IView<RComponent>> propertyViews,
                                                       IComponentDescriptor<?> modelDescriptor) {
    // Compute dynamic font
    for (IView<RComponent> propertyView : propertyViews) {
      completeViewWithDynamicFont(propertyView.getPeer(), propertyView.getDescriptor(), modelDescriptor, connector);
    }
  }

  @Override
  protected void completeViewWithDynamicLabel(RComponent viewComponent, IViewDescriptor viewDescriptor,
                                              IComponentDescriptor<?> componentDescriptor,
                                              ICompositeValueConnector connectorToComplete) {
    if (viewComponent.getLabelState() == null) { // Not previously set by view creation
      String dynamicLabelProperty = computeDynamicLabelPropertyName(viewDescriptor, componentDescriptor, null);
      if (dynamicLabelProperty != null) {
        IValueConnector labelConnector = connectorToComplete.getChildConnector(dynamicLabelProperty);
        if (labelConnector == null) {
          labelConnector = getConnectorFactory().createValueConnector(dynamicLabelProperty);
          connectorToComplete.addChildConnector(dynamicLabelProperty, labelConnector);
        }
        if (labelConnector instanceof IRemoteStateOwner) {
          viewComponent.setLabelState(((IRemoteStateOwner) labelConnector).getState());
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
  public void configurePropertyViewAction(IPropertyViewDescriptor propertyViewDescriptor, RAction propertyViewAction) {
    Map<String, Object> staticContext = new HashMap<>();
    staticContext.put(ActionContextConstants.PROPERTY_VIEW_DESCRIPTOR, propertyViewDescriptor);
    propertyViewAction.putValue(IAction.STATIC_CONTEXT_KEY, staticContext);
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithActions(IView<RComponent> view, IActionHandler actionHandler, Locale locale) {
    IViewDescriptor viewDescriptor = view.getDescriptor();
    ActionMap actionMap = viewDescriptor.getActionMap();
    ActionMap secondaryActionMap = viewDescriptor.getSecondaryActionMap();
    RComponent peer = view.getPeer();
    if (actionMap != null && actionHandler.isAccessGranted(actionMap)) {
      try {
        actionHandler.pushToSecurityContext(actionMap);
        // There might be default actions like LOV
        RActionList[] defaultActionLists = peer.getActionLists();
        List<RActionList> declaredActionLists = createViewActionMap(actionMap, view, actionHandler, locale);
        if (defaultActionLists != null || declaredActionLists != null) {
          List<RActionList> viewActionLists = new ArrayList<>();
          if (defaultActionLists != null) {
            viewActionLists.addAll(Arrays.asList(defaultActionLists));
          }
          if (declaredActionLists != null) {
            viewActionLists.addAll(declaredActionLists);
          }
          peer.setActionLists(viewActionLists.toArray(new RActionList[viewActionLists.size()]));
        }
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
    if (secondaryActionMap != null && actionHandler.isAccessGranted(secondaryActionMap)) {
      try {
        actionHandler.pushToSecurityContext(secondaryActionMap);
        // There might be default actions like LOV
        RActionList[] defaultSecondaryActionLists = peer.getSecondaryActionLists();
        List<RActionList> declaredSecondaryActionLists = createViewActionMap(secondaryActionMap, view, actionHandler,
            locale);
        if (defaultSecondaryActionLists != null || declaredSecondaryActionLists != null) {
          List<RActionList> viewSecondaryActionLists = new ArrayList<>();
          if (defaultSecondaryActionLists != null) {
            viewSecondaryActionLists.addAll(Arrays.asList(defaultSecondaryActionLists));
          }
          if (declaredSecondaryActionLists != null) {
            viewSecondaryActionLists.addAll(declaredSecondaryActionLists);
          }
          peer.setSecondaryActionLists(
              viewSecondaryActionLists.toArray(new RActionList[viewSecondaryActionLists.size()]));
        }
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
  }

  /**
   * Creates a view toolbar based on an action map.
   *
   * @param actionMap
   *     the action map to create the toolbar for.
   * @param view
   *     the view to create the toolbar for.
   * @param actionHandler
   *     the action handler used.
   * @param locale
   *     the locale used.
   * @return the created tool bar.
   */
  protected List<RActionList> createViewActionMap(ActionMap actionMap, IView<RComponent> view,
                                                  IActionHandler actionHandler, Locale locale) {
    List<RActionList> viewActionLists = new ArrayList<>();
    ERenderingOptions actionMapRenderingOptions = getDefaultActionMapRenderingOptions();
    if (actionMap.getRenderingOptions() != null) {
      actionMapRenderingOptions = actionMap.getRenderingOptions();
    }
    boolean defaultHideActionWhenDisabled = getDefaultHideActionWhenDisabled();
    if (actionMap.getHideActionWhenDisabled() != null) {
      defaultHideActionWhenDisabled = actionMap.getHideActionWhenDisabled();
    }
    for (ActionList nextActionList : actionMap.getActionLists(actionHandler)) {
      RActionList viewActionList = createViewActionList(nextActionList, actionMapRenderingOptions,
          defaultHideActionWhenDisabled, view, actionHandler, locale);
      if (viewActionList != null) {
        viewActionLists.add(viewActionList);
      }
    }
    return viewActionLists;
  }

  /**
   * Create view action list r action list.
   *
   * @param actionList
   *     the action list
   * @param view
   *     the view
   * @param actionMapRenderingOptions
   *     the default rendering options
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   * @return the r action list
   */
  protected RActionList createViewActionList(ActionList actionList, ERenderingOptions actionMapRenderingOptions,
                                             boolean defaultHideActionWhenDisabled, IView<RComponent> view,
                                             IActionHandler actionHandler, Locale locale) {
    if (actionHandler.isAccessGranted(actionList)) {
      try {
        actionHandler.pushToSecurityContext(actionList);
        ERenderingOptions renderingOptions = actionMapRenderingOptions;
        if (actionList.getRenderingOptions() != null) {
          renderingOptions = actionList.getRenderingOptions();
        }
        boolean hideActionWhenDisabled = defaultHideActionWhenDisabled;
        if (actionList.getHideActionWhenDisabled() != null) {
          hideActionWhenDisabled = actionList.getHideActionWhenDisabled();
        }
        RActionList viewActionList = new RActionList(getGuidGenerator().generateGUID());
        viewActionList.setCollapsable(actionList.isCollapsable());
        viewActionList.setName(actionList.getName());
        viewActionList.setDescription(actionList.getDescription());
        Dimension iconDimension = getIconFactory().getSmallIconSize();
        if (isAsideActionDisplay(view.getDescriptor())) {
          iconDimension = getIconFactory().getTinyIconSize();
        }
        viewActionList.setIcon(getIconFactory().getIcon(actionList.getIcon(), iconDimension));
        List<RAction> actions = new ArrayList<>();
        for (IDisplayableAction action : actionList.getActions()) {
          if (actionHandler.isAccessGranted(action)) {
            try {
              boolean hiddenWhenDisabled = hideActionWhenDisabled;
              if (action.getHiddenWhenDisabled() != null) {
                hiddenWhenDisabled = action.getHiddenWhenDisabled();
              }
              actionHandler.pushToSecurityContext(action);
              RAction rAction = getActionFactory().createAction(action, iconDimension, actionHandler, view, locale);
              rAction.setAcceleratorAsString(action.getAcceleratorAsString());
              rAction.setHiddenWhenDisabled(hiddenWhenDisabled);
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
        viewActionList.setActions(actions.toArray(new RAction[actions.size()]));
        return viewActionList;
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
    return null;
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
   *     the remotePeerRegistry to set.
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    this.remotePeerRegistry = remotePeerRegistry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTreeView(ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                             Locale locale) {
    final ICompositeValueConnector connector = createTreeViewConnector(viewDescriptor, actionHandler, locale);

    RTree viewComponent = createRTree(viewDescriptor);
    viewComponent.setDisplayIcon(viewDescriptor.isDisplayIcon());
    viewComponent.setExpanded(viewDescriptor.isExpanded());
    IView<RComponent> view = constructView(viewComponent, viewDescriptor, connector);
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(
          getActionFactory().createAction(viewDescriptor.getRowAction(), actionHandler, view, locale));
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IView<RComponent> constructView(RComponent viewComponent, IViewDescriptor descriptor,
                                         IValueConnector connector) {
    IView<RComponent> view = super.constructView(viewComponent, descriptor, connector);
    if (connector instanceof IPermIdSource) {
      ((IPermIdSource) connector).setPermId(descriptor.getPermId());
    }
    if (viewComponent.getState() == null) {
      viewComponent.setState(((IRemoteStateOwner) connector).getState());
    }
    viewComponent.setPermId(descriptor.getPermId());
    return view;
  }

  @Override
  protected BasicCompositeView<RComponent> constructCompositeView(RComponent viewComponent,
                                                                  IViewDescriptor descriptor) {
    final BasicCompositeView<RComponent> compositeView = super.constructCompositeView(viewComponent, descriptor);
    compositeView.addPropertyChangeListener(IView.CONNECTOR_PROPERTY, connectorStateListener);
    return compositeView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected BasicIndexedView<RComponent> constructIndexedView(RComponent viewComponent, ITabViewDescriptor descriptor) {
    BasicIndexedView<RComponent> indexedView = super.constructIndexedView(viewComponent, descriptor);
    indexedView.addPropertyChangeListener(IView.CONNECTOR_PROPERTY, connectorStateListener);
    getRemotePeerRegistry().register(viewComponent);
    viewComponent.setPermId(getRemotePeerRegistry().registerPermId(descriptor.getPermId(), viewComponent.getGuid()));
    return indexedView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createTabView(final ITabViewDescriptor viewDescriptor,
                                                     final IActionHandler actionHandler, Locale locale) {
    final RTabContainer viewComponent = createRTabContainer(viewDescriptor);
    final BasicIndexedView<RComponent> view = constructIndexedView(viewComponent, viewDescriptor);

    viewComponent.addPropertyChangeListener("selectedIndex", new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        RTabContainer source = (RTabContainer) evt.getSource();
        int selectedIndex = source.getSelectedIndex();
        view.setCurrentViewIndex(selectedIndex);
        storeTabSelectionPreference(viewDescriptor, selectedIndex, actionHandler);
        triggerTabSelectionAction(selectedIndex, source, viewDescriptor, view, actionHandler);
      }
    });
    List<RComponent> tabs = new ArrayList<>();
    List<IView<RComponent>> childrenViews = new ArrayList<>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor.getChildViewDescriptors()) {
      if (actionHandler.isAccessGranted(childViewDescriptor)) {
        IView<RComponent> childView = createView(childViewDescriptor, actionHandler, locale);
        RComponent tab = childView.getPeer();
        ERenderingOptions renderingOptions = getDefaultTabRenderingOptions();
        if (viewDescriptor.getRenderingOptions() != null) {
          renderingOptions = viewDescriptor.getRenderingOptions();
        }
        switch (renderingOptions) {
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
    attachFirstTabSelectorIfNecessary(viewDescriptor, view);
    view.setChildren(childrenViews);
    view.setCurrentViewIndex(getTabSelectionPreference(viewDescriptor, actionHandler));
    return view;
  }

  /**
   * Creates a remote tab container.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RTabContainer createRTabContainer(ITabViewDescriptor viewDescriptor) {
    return new RTabContainer(getGuidGenerator().generateGUID());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createRepeaterView(final IRepeaterViewDescriptor viewDescriptor,
                                                 final IActionHandler actionHandler, final Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    final IView<RComponent> repeated = createView(viewDescriptor.getRepeatedViewDescriptor(), actionHandler, locale);
    ICompositeValueConnector elementConnectorPrototype = (ICompositeValueConnector) repeated.getConnector();
    ICollectionConnector connector = getConnectorFactory().createCollectionConnector(modelDescriptor.getName(),
        getMvcBinder(), elementConnectorPrototype);
    RRepeater viewComponent = createRRepeater(viewDescriptor);
    IView<RComponent> view = constructView(viewComponent, viewDescriptor, connector);
    viewComponent.setSelectionMode(viewDescriptor.getSelectionMode().name());
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(
          getActionFactory().createAction(viewDescriptor.getRowAction(), actionHandler, view, locale));
    }
    bindRepeaterConnector(view, actionHandler, locale);
    return view;
  }

  /**
   * Bind repeater connector.
   *
   * @param repeaterView
   *     the repeater view
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   */
  protected void bindRepeaterConnector(final IView<RComponent> repeaterView, final IActionHandler actionHandler,
                                       final Locale locale) {
    IValueConnector connector = repeaterView.getConnector();
    final IRepeaterViewDescriptor viewDescriptor = (IRepeaterViewDescriptor) repeaterView.getDescriptor();
    final RComponent repeater = repeaterView.getPeer();
    final Map<Integer, IView<RComponent>> childIds = new HashMap<>();
    connector.addValueChangeListener(new IValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent evt) {
        ICollectionConnector connector = (ICollectionConnector) evt.getSource();
        int targetCount = connector.getChildConnectorCount();
        List<RComponent> extraSections = new ArrayList<>();
        for (int i = 0; i < targetCount; i++) {
          IValueConnector elementConnector = connector.getChildConnector(i);
          int childId = System.identityHashCode(elementConnector);
          if (!childIds.containsKey(childId)) {
            IView<RComponent> extraSection = createView(viewDescriptor.getRepeatedViewDescriptor(), actionHandler,
                locale);
            extraSection.setParent(repeaterView);
            RComponent peer = extraSection.getPeer();
            RemoteValueState state = ((IRemoteStateOwner) elementConnector).getState();
            peer.setState(state);
            extraSections.add(peer);
            getMvcBinder().bind(extraSection.getConnector(), elementConnector);
            childIds.put(childId, extraSection);
          }
        }
        if (!extraSections.isEmpty()) {
          addRepeatedSections(repeater, extraSections);
        }
      }
    });
  }

  /**
   * Add repeated sections.
   *
   * @param repeater
   *     the repeater
   * @param extraSections
   *     the extra sections
   */
  protected void addRepeatedSections(RComponent repeater, List<RComponent> extraSections) {
    RemoteAddRepeatedCommand addRepeatedCommand = new RemoteAddRepeatedCommand();
    addRepeatedCommand.setTargetPeerGuid(repeater.getGuid());
    addRepeatedCommand.setNewSections(extraSections.toArray(new RComponent[extraSections.size()]));
    getRemoteCommandHandler().registerCommand(addRepeatedCommand);
  }

  /**
   * Creates a remote repeater container.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RRepeater createRRepeater(IRepeaterViewDescriptor viewDescriptor) {
    return new RRepeater(getGuidGenerator().generateGUID());
  }
}
