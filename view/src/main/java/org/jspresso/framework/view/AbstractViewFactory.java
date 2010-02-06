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
package org.jspresso.framework.view;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.action.IActionHandlerAware;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorListProvider;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.masterdetail.IModelCascadingBinder;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.model.descriptor.EDateType;
import org.jspresso.framework.model.descriptor.EDuration;
import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IColorPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
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
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.format.DurationFormatter;
import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.format.NullableSimpleDateFormat;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;
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
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptorProvider;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * Abstract base class factory for views.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class AbstractViewFactory<E, F, G> implements
    IViewFactory<E, F, G> {

  /**
   * <code>DEF_DISP_MAX_FRACTION_DIGIT</code>.
   */
  protected static final int            DEF_DISP_MAX_FRACTION_DIGIT      = 2;
  /**
   * <code>DEF_DISP_MAX_VALUE</code>.
   */
  protected static final double         DEF_DISP_MAX_VALUE               = 1000;
  /**
   * <code>DEF_DISP_TEMPLATE_PERCENT</code>.
   */
  protected static final double         DEF_DISP_TEMPLATE_PERCENT        = 99;
  /**
   * <code>TEMPLATE_CHAR</code>.
   */
  protected static final char           TEMPLATE_CHAR                    = 'O';
  /**
   * <code>TEMPLATE_DATE</code>.
   */
  protected static final Date           TEMPLATE_DATE                    = new Date(
                                                                             27166271000L);
  /**
   * <code>TEMPLATE_DURATION</code>.
   */
  protected static final Long           TEMPLATE_DURATION                = new Long(
                                                                             EDuration.ONE_SECOND
                                                                                 .getMillis()
                                                                                 + EDuration.ONE_MINUTE
                                                                                     .getMillis()
                                                                                 + EDuration.ONE_HOUR
                                                                                     .getMillis()
                                                                                 + EDuration.ONE_DAY
                                                                                     .getMillis()
                                                                                 + EDuration.ONE_WEEK
                                                                                     .getMillis());
  /**
   * <code>TEMPLATE_TIME</code>.
   */
  protected static final Date           TEMPLATE_TIME                    = new Date(
                                                                             366000);

  /**
   * <code>BOLD_FONT</code>.
   */
  protected static final String         BOLD_FONT                        = ";BOLD;";

  private IActionFactory<G, E>          actionFactory;
  private IDisplayableAction            binaryPropertyInfoAction;
  private IConfigurableConnectorFactory connectorFactory;
  private IIconFactory<F>               iconFactory;
  private IDisplayableAction            lovAction;
  private int                           maxCharacterLength               = 32;

  private int                           maxColumnCharacterLength         = 32;
  private IModelCascadingBinder         modelCascadingBinder;
  private IModelConnectorFactory        modelConnectorFactory;
  private IMvcBinder                    mvcBinder;
  private IDisplayableAction            openFileAsBinaryPropertyAction;
  private IDisplayableAction            resetPropertyAction;

  private IDisplayableAction            saveBinaryPropertyAsFileAction;
  private ITranslationProvider          translationProvider;
  private IValueChangeListener          firstRowSelector;

  private ERenderingOptions             defaultActionMapRenderingOptions = ERenderingOptions.ICON;

  /**
   * Constructs a new <code>AbstractViewFactory</code> instance.
   */
  protected AbstractViewFactory() {
    firstRowSelector = new IValueChangeListener() {

      public void valueChange(ValueChangeEvent evt) {
        if (evt.getNewValue() != null
            && !((Collection<?>) evt.getNewValue()).isEmpty()) {
          ((ICollectionConnector) evt.getSource())
              .setSelectedIndices(new int[] {0});
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  public IView<E> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<E> view = createCustomView(viewDescriptor, actionHandler, locale);
    if (view == null) {
      if (viewDescriptor instanceof IComponentViewDescriptor) {
        view = createComponentView((IComponentViewDescriptor) viewDescriptor,
            actionHandler, locale);
      } else if (viewDescriptor instanceof IImageViewDescriptor) {
        view = createImageView((IImageViewDescriptor) viewDescriptor,
            actionHandler, locale);
      } else if (viewDescriptor instanceof IActionViewDescriptor) {
        view = createActionView((IActionViewDescriptor) viewDescriptor,
            actionHandler, locale);
      } else if (viewDescriptor instanceof IPropertyViewDescriptor) {
        view = createPropertyView((IPropertyViewDescriptor) viewDescriptor,
            actionHandler, locale);
      } else if (viewDescriptor instanceof ICollectionViewDescriptor) {
        view = createCollectionView((ICollectionViewDescriptor) viewDescriptor,
            actionHandler, locale);
        if (((ICollectionViewDescriptor) viewDescriptor)
            .getItemSelectionAction() != null) {
          ((IItemSelectable) view.getConnector())
              .addItemSelectionListener(new ItemSelectionAdapter(
                  ((ICollectionViewDescriptor) viewDescriptor)
                      .getItemSelectionAction(), actionHandler, view));
        }
      } else if (viewDescriptor instanceof ICompositeViewDescriptor) {
        view = createCompositeView((ICompositeViewDescriptor) viewDescriptor,
            actionHandler, locale);
      } else if (viewDescriptor instanceof ICardViewDescriptor) {
        view = createCardView((ICardViewDescriptor) viewDescriptor,
            actionHandler, locale);
      } else if (viewDescriptor instanceof ITreeViewDescriptor) {
        view = createTreeView((ITreeViewDescriptor) viewDescriptor,
            actionHandler, locale);
        if (((ITreeViewDescriptor) viewDescriptor).getItemSelectionAction() != null) {
          ((IItemSelectable) view.getConnector())
              .addItemSelectionListener(new ItemSelectionAdapter(
                  ((ITreeViewDescriptor) viewDescriptor)
                      .getItemSelectionAction(), actionHandler, view));
        }
      }
    }
    if (view != null) {
      view.getConnector().setLocallyWritable(!viewDescriptor.isReadOnly());
      if (viewDescriptor.getReadabilityGates() != null) {
        for (IGate gate : viewDescriptor.getReadabilityGates()) {
          IGate clonedGate = gate.clone();
          if (clonedGate instanceof IActionHandlerAware) {
            ((IActionHandlerAware) clonedGate).setActionHandler(actionHandler);
          }
          view.getConnector().addReadabilityGate(clonedGate);
        }
      }
      if (viewDescriptor.getWritabilityGates() != null) {
        for (IGate gate : viewDescriptor.getWritabilityGates()) {
          IGate clonedGate = gate.clone();
          if (clonedGate instanceof IActionHandlerAware) {
            ((IActionHandlerAware) clonedGate).setActionHandler(actionHandler);
          }
          view.getConnector().addWritabilityGate(clonedGate);
        }
      }
      view.getConnector().setSubject(actionHandler.getSubject());
      finishComponentConfiguration(viewDescriptor, locale, view);
      decorateWithActions(viewDescriptor, actionHandler, locale, view);
      decorateWithBorder(view, locale);
      view.getConnector().setModelDescriptor(
          viewDescriptor.getModelDescriptor());
      if (!actionHandler.isAccessGranted(viewDescriptor)) {
        view.setPeer(createSecurityComponent());
      }
      applyPreferredSize(view.getPeer(), viewDescriptor.getPreferredSize());
    } else {
      view = createEmptyView(viewDescriptor, actionHandler, locale);
    }
    return view;
  }

  /**
   * Gives a chance to subclasses to create custom views. Returns null by
   * default.
   * 
   * @param viewDescriptor
   *          the view descriptor being the root of the view hierarchy to be
   *          constructed.
   * @param actionHandler
   *          the object responsible for executing the view actions (generally
   *          the frontend controller itself).
   * @param locale
   *          the locale the view must use for I18N.
   * @return the created view or null.
   */
  protected IView<E> createCustomView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return null;
  }

  /**
   * Creates an empty view whenever this specific view descriptor is not
   * supported.
   * 
   * @param viewDescriptor
   *          the view descriptor being the root of the view hierarchy to be
   *          constructed.
   * @param actionHandler
   *          the object responsible for executing the view actions (generally
   *          the frontend controller itself).
   * @param locale
   *          the locale the view must use for I18N.
   * @return the empty view.
   */
  private IView<E> createEmptyView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IValueConnector connector = getConnectorFactory().createValueConnector(
        ModelRefPropertyConnector.THIS_PROPERTY);
    E viewComponent = createEmptyComponent();
    return constructView(viewComponent, viewDescriptor, connector);
  }

  /**
   * Gets the actionFactory.
   * 
   * @return the actionFactory.
   */
  public IActionFactory<G, E> getActionFactory() {
    return actionFactory;
  }

  /**
   * Gets the connectorFactory.
   * 
   * @return the connectorFactory.
   */
  public IConfigurableConnectorFactory getConnectorFactory() {
    return connectorFactory;
  }

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  public IIconFactory<F> getIconFactory() {
    return iconFactory;
  }

  /**
   * Sets the actionFactory.
   * 
   * @param actionFactory
   *          the actionFactory to set.
   */
  public void setActionFactory(IActionFactory<G, E> actionFactory) {
    this.actionFactory = actionFactory;
  }

  /**
   * Sets the binaryPropertyInfoAction.
   * 
   * @param binaryPropertyInfoAction
   *          the binaryPropertyInfoAction to set.
   */
  public void setBinaryPropertyInfoAction(
      IDisplayableAction binaryPropertyInfoAction) {
    this.binaryPropertyInfoAction = binaryPropertyInfoAction;
  }

  /**
   * Sets the connectorFactory.
   * 
   * @param connectorFactory
   *          the connectorFactory to set.
   */
  public void setConnectorFactory(IConfigurableConnectorFactory connectorFactory) {
    this.connectorFactory = connectorFactory;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<F> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the lovAction.
   * 
   * @param lovAction
   *          the lovAction to set.
   */
  public void setLovAction(IDisplayableAction lovAction) {
    this.lovAction = lovAction;
  }

  /**
   * Gets the lovAction.
   * 
   * @return the lovAction.
   */
  protected IDisplayableAction getLovAction() {
    return lovAction;
  }

  /**
   * Sets the maxCharacterLength.
   * 
   * @param maxCharacterLength
   *          the maxCharacterLength to set.
   */
  public void setMaxCharacterLength(int maxCharacterLength) {
    this.maxCharacterLength = maxCharacterLength;
  }

  /**
   * Sets the maxColumnCharacterLength.
   * 
   * @param maxColumnCharacterLength
   *          the maxColumnCharacterLength to set.
   */
  public void setMaxColumnCharacterLength(int maxColumnCharacterLength) {
    this.maxColumnCharacterLength = maxColumnCharacterLength;
  }

  /**
   * Sets the modelCascadingBinder.
   * 
   * @param modelCascadingBinder
   *          the modelCascadingBinder to set.
   */
  public void setModelCascadingBinder(IModelCascadingBinder modelCascadingBinder) {
    this.modelCascadingBinder = modelCascadingBinder;
  }

  /**
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Sets the openFileAsBinaryPropertyAction.
   * 
   * @param openFileAsBinaryPropertyAction
   *          the openFileAsBinaryPropertyAction to set.
   */
  public void setOpenFileAsBinaryPropertyAction(
      IDisplayableAction openFileAsBinaryPropertyAction) {
    this.openFileAsBinaryPropertyAction = openFileAsBinaryPropertyAction;
  }

  /**
   * Sets the resetPropertyAction.
   * 
   * @param resetPropertyAction
   *          the resetPropertyAction to set.
   */
  public void setResetPropertyAction(IDisplayableAction resetPropertyAction) {
    this.resetPropertyAction = resetPropertyAction;
  }

  /**
   * Sets the saveBinaryPropertyAsFileAction.
   * 
   * @param saveBinaryPropertyAsFileAction
   *          the saveBinaryPropertyAsFileAction to set.
   */
  public void setSaveBinaryPropertyAsFileAction(
      IDisplayableAction saveBinaryPropertyAsFileAction) {
    this.saveBinaryPropertyAsFileAction = saveBinaryPropertyAsFileAction;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  /**
   * Adjusts a component various sizes (e.g. min, max, preferred) based on a
   * formatter and a template value.
   * 
   * @param component
   *          the component to adjust the sizes for.
   * @param formatter
   *          the formatter used if any.
   * @param templateValue
   *          the template value used.
   * @param viewDescriptor
   *          the underlying view descriptor.
   */
  protected void adjustSizes(IViewDescriptor viewDescriptor, E component,
      IFormatter formatter, Object templateValue) {
    adjustSizes(viewDescriptor, component, formatter, templateValue, 32);
  }

  /**
   * Adjusts a component various sizes (e.g. min, max, preferred) based on a
   * formatter and a template value.
   * 
   * @param component
   *          the component to adjust the sizes for.
   * @param formatter
   *          the formatter used if any.
   * @param templateValue
   *          the template value used.
   * @param viewDescriptor
   *          the underlying view descriptor.
   * @param extraWidth
   *          the extra size to be added.
   */
  protected abstract void adjustSizes(IViewDescriptor viewDescriptor,
      E component, IFormatter formatter, Object templateValue, int extraWidth);

  /**
   * Computes an enumeration key.
   * 
   * @param keyPrefix
   *          the prefix to use.
   * @param value
   *          the enumeration value.
   * @return the enumeration key.
   */
  protected String computeEnumerationKey(String keyPrefix, Object value) {
    return keyPrefix + "." + value;
  }

  /**
   * Computes a size in pixels based on a number of characters and a component.
   * It should use component font do do so.
   * 
   * @param component
   *          the component.
   * @param characterLength
   *          the number of characters.
   * @return the ize in pixels.
   */
  protected abstract int computePixelWidth(E component, int characterLength);

  /**
   * Constructs a composite view.
   * 
   * @param viewComponent
   *          the peer view component
   * @param descriptor
   *          the view descriptor
   * @return the created composite view.
   */
  protected BasicCompositeView<E> constructCompositeView(E viewComponent,
      IViewDescriptor descriptor) {
    BasicCompositeView<E> view = new BasicCompositeView<E>(viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  /**
   * Constructs a map view.
   * 
   * @param viewComponent
   *          the peer view component
   * @param descriptor
   *          the view descriptor
   * @return the created map view.
   */
  protected BasicMapView<E> constructMapView(E viewComponent,
      IViewDescriptor descriptor) {
    BasicMapView<E> view = new BasicMapView<E>(viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  /**
   * Constructs a view.
   * 
   * @param viewComponent
   *          the peer view component
   * @param descriptor
   *          the view descriptor
   * @param connector
   *          the view connector.
   * @return the created view.
   */
  protected IView<E> constructView(E viewComponent, IViewDescriptor descriptor,
      IValueConnector connector) {
    BasicView<E> view = new BasicView<E>(viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

  /**
   * Creates the action list for a binary property (open from file, save as
   * file, reset, size info).
   * 
   * @param viewComponent
   *          the component these actions will be triggered from.
   * @param connector
   *          the connector these actions will be triggered from.
   * @param propertyDescriptor
   *          the binary property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the action list.
   */
  protected List<G> createBinaryActions(E viewComponent,
      IValueConnector connector, IPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    G openAction = getActionFactory().createAction(
        openFileAsBinaryPropertyAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    G saveAction = getActionFactory().createAction(
        saveBinaryPropertyAsFileAction, actionHandler, viewComponent,
        propertyDescriptor, connector, locale);
    G resetAction = getActionFactory().createAction(resetPropertyAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    G infoAction = getActionFactory().createAction(binaryPropertyInfoAction,
        actionHandler, viewComponent, propertyDescriptor, connector, locale);
    List<G> binaryActions = new ArrayList<G>();
    getActionFactory().setActionName(openAction, null);
    getActionFactory().setActionName(saveAction, null);
    getActionFactory().setActionName(resetAction, null);
    getActionFactory().setActionName(infoAction, null);
    binaryActions.add(openAction);
    binaryActions.add(saveAction);
    binaryActions.add(resetAction);
    binaryActions.add(infoAction);
    return binaryActions;
  }

  /**
   * Creates a binary property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createBinaryPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a boolean property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createBooleanPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a border view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler
   * @param locale
   *          the locale.
   * @return the border view.
   */
  protected abstract ICompositeView<E> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a tree view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created tree view.
   */
  protected abstract IView<E> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a card view connector.
   * 
   * @param cardView
   *          the card view to create the connector for.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the card view connector.
   */
  protected IValueConnector createCardViewConnector(final IMapView<E> cardView,
      final IActionHandler actionHandler, final Locale locale) {
    IValueConnector cardViewConnector = getConnectorFactory()
        .createValueConnector(cardView.getDescriptor().getName());
    cardViewConnector.addValueChangeListener(new IValueChangeListener() {

      public void valueChange(ValueChangeEvent evt) {
        Object cardModel = evt.getNewValue();
        E cardsPeer = cardView.getPeer();
        String cardName = ((ICardViewDescriptor) cardView.getDescriptor())
            .getCardNameForModel(cardModel, actionHandler.getSubject());
        if (cardName != null) {
          IView<E> childCardView = cardView.getChild(cardName);
          if (childCardView == null
              && cardModel instanceof IViewDescriptorProvider) {
            IViewDescriptor providedViewDescriptor = ((IViewDescriptorProvider) cardModel)
                .getViewDescriptor();
            if (providedViewDescriptor != null) {
              childCardView = createView(providedViewDescriptor, actionHandler,
                  locale);
              addCard(cardView, childCardView, cardName);
            }
          }
          if (childCardView != null) {
            boolean accessGranted = true;
            accessGranted = accessGranted
                && actionHandler.isAccessGranted(childCardView.getDescriptor());
            if (cardModel instanceof ISecurable) {
              accessGranted = accessGranted
                  && actionHandler.isAccessGranted((ISecurable) cardModel);
            }
            if (accessGranted) {
              showCardInPanel(cardsPeer, cardName);
            } else {
              showCardInPanel(cardsPeer, ICardViewDescriptor.SECURITY_CARD);
            }
            IValueConnector childCardConnector = childCardView.getConnector();
            if (childCardConnector != null) {
              // To handle polymorphism, especially for modules, we refine
              // the model descriptor.
              if (cardView.getConnector().getModelConnector()
                  .getModelDescriptor().getModelType().isAssignableFrom(
                      childCardView.getDescriptor().getModelDescriptor()
                          .getModelType())) {
                cardView.getConnector().getModelConnector().setModelDescriptor(
                    childCardView.getDescriptor().getModelDescriptor());
              }
              getMvcBinder().bind(childCardConnector,
                  cardView.getConnector().getModelConnector());
            }
          } else {
            showCardInPanel(cardsPeer, ICardViewDescriptor.DEFAULT_CARD);
          }
        } else {
          showCardInPanel(cardsPeer, ICardViewDescriptor.DEFAULT_CARD);
        }
      }
    });
    return cardViewConnector;
  }

  /**
   * Adds a card in a card view.
   * 
   * @param cardView
   *          the card view to add the card to.
   * @param card
   *          the card to add.
   * @param cardName
   *          the card name.
   */
  protected abstract void addCard(IMapView<E> cardView, IView<E> card,
      String cardName);

  /**
   * Creates a color property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createColorPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a table column connector.
   * 
   * @param columnViewDescriptor
   *          the column decriptor to create the connector for.
   * @param descriptor
   *          the component descriptor this table relies on.
   * @param actionHandler
   *          the action handler.
   * @return the connector for the table column.
   */
  protected IValueConnector createColumnConnector(
      IPropertyViewDescriptor columnViewDescriptor,
      IComponentDescriptor<?> descriptor, IActionHandler actionHandler) {
    String columnId = columnViewDescriptor.getModelDescriptor().getName();
    IPropertyDescriptor propertyDescriptor = descriptor
        .getPropertyDescriptor(columnId);
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + columnId + " defined for "
          + descriptor.getComponentContract());
    }
    IValueConnector columnConnector;
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      List<String> renderedProperties = columnViewDescriptor
          .getRenderedChildProperties();
      String renderedProperty;
      if (renderedProperties != null && !renderedProperties.isEmpty()) {
        // it's a custom rendered property.
        renderedProperty = renderedProperties.get(0);
      } else {
        renderedProperty = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getToStringProperty();
      }
      columnConnector = getConnectorFactory().createCompositeValueConnector(
          columnId, renderedProperty);
    } else {
      columnConnector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
    }

    columnConnector.setLocallyWritable(!columnViewDescriptor.isReadOnly());
    if (columnViewDescriptor.getReadabilityGates() != null) {
      for (IGate gate : columnViewDescriptor.getReadabilityGates()) {
        IGate clonedGate = gate.clone();
        if (clonedGate instanceof IActionHandlerAware) {
          ((IActionHandlerAware) clonedGate).setActionHandler(actionHandler);
        }
        columnConnector.addReadabilityGate(clonedGate);
      }
    }
    if (columnViewDescriptor.getWritabilityGates() != null) {
      for (IGate gate : columnViewDescriptor.getWritabilityGates()) {
        IGate clonedGate = gate.clone();
        if (clonedGate instanceof IActionHandlerAware) {
          ((IActionHandlerAware) clonedGate).setActionHandler(actionHandler);
        }
        columnConnector.addWritabilityGate(clonedGate);
      }
    }
    columnConnector.setSubject(actionHandler.getSubject());
    return columnConnector;
  }

  /**
   * Creates a list column connector.
   * 
   * @param renderedProperty
   *          the list rendered property.
   * @param descriptor
   *          the component descriptor this list relies on.
   * @return the connector for the list.
   */
  protected IValueConnector createListConnector(String renderedProperty,
      IComponentDescriptor<?> descriptor) {
    IPropertyDescriptor propertyDescriptor = descriptor
        .getPropertyDescriptor(renderedProperty);
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + renderedProperty
          + " defined for " + descriptor.getComponentContract());
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      return getConnectorFactory().createCompositeValueConnector(
          renderedProperty,
          ((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getToStringProperty());
    }
    return getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
  }

  /**
   * Creates a component view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created component view.
   */
  protected abstract IView<E> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a composite view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler
   * @param locale
   *          the locale.
   * @return the composite view.
   */
  protected ICompositeView<E> createCompositeView(
      ICompositeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<E> view = null;
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
    bindCompositeView(view);
    return view;
  }

  /**
   * Performs all the necessary connectors binding among the composite view and
   * its children.
   * 
   * @param view
   *          the composite view to bind.
   */
  protected void bindCompositeView(ICompositeView<E> view) {
    if (view != null) {
      ICompositeViewDescriptor viewDescriptor = view.getDescriptor();
      if (viewDescriptor.isCascadingModels()) {
        IView<E> masterView = view.getChildren().get(0);
        IValueConnector viewConnector;
        if (masterView.getDescriptor().getModelDescriptor() instanceof IPropertyDescriptor) {
          IConfigurableCollectionConnectorProvider mainConnector = getConnectorFactory()
              .createConfigurableCollectionConnectorProvider(
                  ModelRefPropertyConnector.THIS_PROPERTY, null);
          mainConnector.addChildConnector(masterView.getConnector());
          if (masterView.getConnector() instanceof ICollectionConnector) {
            mainConnector
                .setCollectionConnectorProvider((ICollectionConnector) masterView
                    .getConnector());
          }
          viewConnector = mainConnector;
        } else {
          ICompositeValueConnector mainConnector = getConnectorFactory()
              .createCompositeValueConnector(
                  ModelRefPropertyConnector.THIS_PROPERTY, null);
          mainConnector.addChildConnector(masterView.getConnector());
          viewConnector = mainConnector;
        }
        view.setConnector(viewConnector);
        for (int i = 1; i < view.getChildren().size(); i++) {
          IView<E> detailView = view.getChildren().get(i);

          IValueConnector detailConnector = null;
          if (detailView.getDescriptor().getModelDescriptor() instanceof IPropertyDescriptor) {
            IConfigurableCollectionConnectorProvider wrapper = getConnectorFactory()
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
          getModelCascadingBinder().bind(masterView.getConnector(),
              detailConnector);
          masterView = detailView;
        }
      } else {
        String connectorId;
        if (viewDescriptor.getModelDescriptor() instanceof IPropertyDescriptor) {
          connectorId = viewDescriptor.getModelDescriptor().getName();
        } else {
          connectorId = ModelRefPropertyConnector.THIS_PROPERTY;
        }
        ICompositeValueConnector connector = getConnectorFactory()
            .createCompositeValueConnector(connectorId, null);
        view.setConnector(connector);
        for (IView<E> childView : view.getChildren()) {
          connector.addChildConnector(childView.getConnector());
        }
      }
    }
  }

  /**
   * Creates a constrained grid view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the constrained grid view.
   */
  protected abstract ICompositeView<E> createConstrainedGridView(
      IConstrainedGridViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a date format based on a date property descriptor.
   * 
   * @param propertyDescriptor
   *          the date property descriptor.
   * @param locale
   *          the locale.
   * @return the date format.
   */
  protected SimpleDateFormat createDateFormat(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    SimpleDateFormat format;
    if (propertyDescriptor.getType() == EDateType.DATE) {
      format = new NullableSimpleDateFormat(((SimpleDateFormat) DateFormat
          .getDateInstance(DateFormat.SHORT, locale)).toPattern(), locale);
    } else {
      format = new NullableSimpleDateFormat(((SimpleDateFormat) DateFormat
          .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale))
          .toPattern(), locale);
    }
    return format;
  }

  /**
   * Creates a date formatter based on a date property descriptor.
   * 
   * @param propertyDescriptor
   *          the date property descriptor.
   * @param locale
   *          the locale.
   * @return the date formatter.
   */
  protected IFormatter createDateFormatter(
      IDatePropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createDateFormat(propertyDescriptor, locale));
  }

  /**
   * Creates a date property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createDatePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a decimal format based on a decimal property descriptor.
   * 
   * @param propertyDescriptor
   *          the decimal property descriptor.
   * @param locale
   *          the locale.
   * @return the decimal format.
   */
  protected NumberFormat createDecimalFormat(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    NumberFormat format = NumberFormat.getNumberInstance(locale);
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit()
          .intValue());
    } else {
      format.setMaximumFractionDigits(DEF_DISP_MAX_FRACTION_DIGIT);
    }
    if (propertyDescriptor.isUsingBigDecimal()
        && (format instanceof DecimalFormat)) {
      ((DecimalFormat) format).setParseBigDecimal(true);
    }
    format.setMinimumFractionDigits(format.getMaximumFractionDigits());
    return format;
  }

  /**
   * Creates a decimal formatter based on a decimal property descriptor.
   * 
   * @param propertyDescriptor
   *          the decimal property descriptor.
   * @param locale
   *          the locale.
   * @return the decimal formatter.
   */
  protected IFormatter createDecimalFormatter(
      IDecimalPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(createDecimalFormat(propertyDescriptor, locale));
  }

  /**
   * Creates a decimal property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createDecimalPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a duration formatter based on a duration property descriptor.
   * 
   * @param propertyDescriptor
   *          the duration property descriptor.
   * @param locale
   *          the locale.
   * @return the duration formatter.
   */
  protected IFormatter createDurationFormatter(
      IDurationPropertyDescriptor propertyDescriptor, Locale locale) {
    return new DurationFormatter(locale);
  }

  /**
   * Creates a duration property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createDurationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates an empty panel.
   * 
   * @return the security panel.
   */
  protected abstract E createEmptyComponent();

  /**
   * Creates an enumeration property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createEnumerationPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates an evenly distributed grid view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the evenly distributed grid view.
   */
  protected abstract ICompositeView<E> createEvenGridView(
      IEvenGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Wraps a format in a formatter.
   * 
   * @param format
   *          the format to wrap.
   * @return the resulting formatter.
   */
  protected IFormatter createFormatter(Format format) {
    return new FormatAdapter(format);
  }

  /**
   * Creates a formatter based on a property descriptor.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param locale
   *          the locale.
   * @return the formatter.
   */
  protected IFormatter createFormatter(IPropertyDescriptor propertyDescriptor,
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

  /**
   * Creates a grid view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler
   * @param locale
   *          the locale.
   * @return the grid view.
   */
  protected ICompositeView<E> createGridView(
      IGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeView<E> view = null;
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

  /**
   * Creates a image view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created image view.
   */
  protected abstract IView<E> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates an action view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created action view.
   */
  protected abstract IView<E> createActionView(
      IActionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates an integer format based on an integer property descriptor.
   * 
   * @param propertyDescriptor
   *          the integer property descriptor.
   * @param locale
   *          the locale.
   * @return the integer format.
   */
  protected NumberFormat createIntegerFormat(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return NumberFormat.getIntegerInstance(locale);
  }

  /**
   * Creates an integer formatter based on an integer property descriptor.
   * 
   * @param propertyDescriptor
   *          the integer property descriptor.
   * @param locale
   *          the locale.
   * @return the integer formatter.
   */
  protected IFormatter createIntegerFormatter(
      IIntegerPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(createIntegerFormat(propertyDescriptor, locale));
  }

  /**
   * Creates an integer property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createIntegerPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a list view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created list view.
   */
  protected abstract IView<E> createListView(
      IListViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates the list of value action.
   * 
   * @param viewComponent
   *          the component these actions will be triggered from.
   * @param connector
   *          the connector these actions will be triggered from.
   * @param propertyViewDescriptor
   *          the reference property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the generic list of value action.
   */
  protected G createLovAction(E viewComponent, IValueConnector connector,
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IDisplayableAction listOfValueAction;
    if (propertyViewDescriptor instanceof IReferencePropertyViewDescriptor
        && ((IReferencePropertyViewDescriptor) propertyViewDescriptor)
            .getLovAction() != null) {
      listOfValueAction = ((IReferencePropertyViewDescriptor) propertyViewDescriptor)
          .getLovAction();
    } else {
      listOfValueAction = getLovAction();
    }
    G action = getActionFactory().createAction(listOfValueAction,
        actionHandler, viewComponent,
        propertyViewDescriptor.getModelDescriptor(), connector, locale);
    getActionFactory().setActionName(action, null);
    return action;
  }

  /**
   * Creates a number property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createNumberPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<E> view = null;
    INumberPropertyDescriptor propertyDescriptor = (INumberPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      view = createIntegerPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      view = createDecimalPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    return view;
  }

  /**
   * Creates a password property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createPasswordPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a percent format based on a percent property descriptor.
   * 
   * @param propertyDescriptor
   *          the percent property descriptor.
   * @param locale
   *          the locale.
   * @return the percent format.
   */
  protected NumberFormat createPercentFormat(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    NumberFormat format = NumberFormat.getPercentInstance(locale);
    if (propertyDescriptor.getMaxFractionDigit() != null) {
      format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit()
          .intValue());
    } else {
      format.setMaximumFractionDigits(DEF_DISP_MAX_FRACTION_DIGIT);
    }
    if (propertyDescriptor.isUsingBigDecimal()
        && (format instanceof DecimalFormat)) {
      ((DecimalFormat) format).setParseBigDecimal(true);
    }
    format.setMinimumFractionDigits(format.getMaximumFractionDigits());
    return format;
  }

  /**
   * Creates a percent formatter based on a percent property descriptor.
   * 
   * @param propertyDescriptor
   *          the percent property descriptor.
   * @param locale
   *          the locale.
   * @return the percent formatter.
   */
  protected IFormatter createPercentFormatter(
      IPercentPropertyDescriptor propertyDescriptor, Locale locale) {
    return new FormatAdapter(createPercentFormat(propertyDescriptor, locale));
  }

  /**
   * Creates a percent property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the evenly distributed grid view.
   */
  protected abstract IView<E> createPercentPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a single property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<E> view = null;
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      view = createBooleanPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      view = createDatePropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      view = createTimePropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      view = createDurationPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
      view = createEnumerationPropertyView(propertyViewDescriptor,
          actionHandler, locale);
    } else if (propertyDescriptor instanceof INumberPropertyDescriptor) {
      view = createNumberPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      view = createRelationshipEndPropertyView(propertyViewDescriptor,
          actionHandler, locale);
    } else if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      view = createTextualPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IBinaryPropertyDescriptor) {
      view = createBinaryPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IColorPropertyDescriptor) {
      view = createColorPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    decorateWithDescription(propertyDescriptor, locale, view);
    return view;
  }

  /**
   * Creates a reference property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createReferencePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a relationship end property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createRelationshipEndPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<E> view = null;
    IRelationshipEndPropertyDescriptor propertyDescriptor = (IRelationshipEndPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      view = createReferencePropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
      view = createCollectionPropertyView(propertyViewDescriptor,
          actionHandler, locale);
    }
    return view;
  }

  /**
   * Creates a panel to be substituted with any view when the user is not
   * granted access.
   * 
   * @return the security panel.
   */
  protected abstract E createSecurityComponent();

  /**
   * Creates an html property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createHtmlPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a source code property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createSourceCodePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a split view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler
   * @param locale
   *          the locale.
   * @return the split view.
   */
  protected abstract ICompositeView<E> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a string property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createStringPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a table view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created table view.
   */
  protected abstract IView<E> createTableView(
      ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a tab view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler
   * @param locale
   *          the locale.
   * @return the tab view.
   */
  protected abstract ICompositeView<E> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a text property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createTextPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a textual property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createTextualPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IStringPropertyDescriptor propertyDescriptor = (IStringPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IPasswordPropertyDescriptor) {
      return createPasswordPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ISourceCodePropertyDescriptor) {
      return createSourceCodePropertyView(propertyViewDescriptor,
          actionHandler, locale);
    } else if (propertyDescriptor instanceof IHtmlPropertyDescriptor) {
      return createHtmlPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ITextPropertyDescriptor) {
      return createTextPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    return createStringPropertyView(propertyViewDescriptor, actionHandler,
        locale);
  }

  /**
   * Creates a time format based on a time property descriptor.
   * 
   * @param propertyDescriptor
   *          the time property descriptor.
   * @param locale
   *          the locale.
   * @return the time format.
   */
  protected SimpleDateFormat createTimeFormat(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    SimpleDateFormat format = (SimpleDateFormat) DateFormat.getTimeInstance(
        DateFormat.SHORT, locale);
    return format;
  }

  /**
   * Creates a time formatter based on an time property descriptor.
   * 
   * @param propertyDescriptor
   *          the time property descriptor.
   * @param locale
   *          the locale.
   * @return the time formatter.
   */
  protected IFormatter createTimeFormatter(
      ITimePropertyDescriptor propertyDescriptor, Locale locale) {
    return createFormatter(createTimeFormat(propertyDescriptor, locale));
  }

  /**
   * Creates a time property view.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createTimePropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a tree view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created tree view.
   */
  protected abstract IView<E> createTreeView(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates the connector for a tree view.
   * 
   * @param viewDescriptor
   *          the tree view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale to use.
   * @return the connector for the tree view.
   */
  protected ICompositeValueConnector createTreeViewConnector(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
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
          if (actionHandler.isAccessGranted(subtreeViewDescriptor)) {
            ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
                viewDescriptor, actionHandler, locale, subtreeViewDescriptor, 1);
            compositeConnector.addChildConnector(subtreeConnector);
            subtreeConnectors.add(subtreeConnector);
          }
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
      ITreeLevelDescriptor childDescriptor = ((ISimpleTreeLevelDescriptor) rootDescriptor)
          .getChildDescriptor();
      if (childDescriptor != null) {
        if (actionHandler.isAccessGranted(childDescriptor)) {
          ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
              viewDescriptor, actionHandler, locale, childDescriptor, 1);
          simpleConnector.addChildConnector(subtreeConnector);
          simpleConnector.setCollectionConnectorProvider(subtreeConnector);
        }
      }
      connector = simpleConnector;
    }

    if (connector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) connector)
          .setDisplayValue(viewDescriptor.getI18nName(translationProvider,
              locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayDescription(viewDescriptor.getI18nDescription(
              translationProvider, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayIconImageUrl(viewDescriptor.getIconImageURL());
      ((AbstractCompositeValueConnector) connector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }

    if (connector instanceof ICollectionConnectorListProvider) {
      ((ICollectionConnectorListProvider) connector)
          .setTracksChildrenSelection(true);
    }
    return connector;
  }

  /**
   * Decorates a view with the actions registered in the view descriptor.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @param view
   *          the raw view.
   */
  protected abstract void decorateWithActions(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale, IView<E> view);

  /**
   * Decorates a view with a border.
   * 
   * @param view
   *          the view to decorate.
   * @param locale
   *          the locale to be used for a titled border.
   */
  protected abstract void decorateWithBorder(IView<E> view, Locale locale);

  /**
   * Decorates a property view with its description.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param locale
   *          the locale.
   * @param view
   *          the property view.
   */
  protected abstract void decorateWithDescription(
      IPropertyDescriptor propertyDescriptor, Locale locale, IView<E> view);

  /**
   * Applies the font and color configuration to a view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param locale
   *          the locale.
   * @param view
   *          the raw view.
   */
  protected abstract void finishComponentConfiguration(
      IViewDescriptor viewDescriptor, Locale locale, IView<E> view);

  /**
   * Computes the connector id for component view.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the computed connector id.
   */
  protected String getConnectorIdForComponentView(
      IComponentViewDescriptor viewDescriptor) {
    if (viewDescriptor.getModelDescriptor() instanceof IComponentDescriptor<?>) {
      return ModelRefPropertyConnector.THIS_PROPERTY;
    }
    return viewDescriptor.getModelDescriptor().getName();
  }

  /**
   * Gets a date template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the date template value.
   */
  protected Date getDateTemplateValue(IDatePropertyDescriptor propertyDescriptor) {
    return TEMPLATE_DATE;
  }

  /**
   * Gets a decimal template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the decimal template value.
   */
  protected Double getDecimalTemplateValue(
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

  /**
   * Gets a duration template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the duration template value.
   */
  protected Long getDurationTemplateValue(
      IDurationPropertyDescriptor propertyDescriptor) {
    return TEMPLATE_DURATION;
  }

  /**
   * Gets an enumeration template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param locale
   *          the locale.
   * @return the enumeration template value.
   */
  protected String getEnumerationTemplateValue(
      IEnumerationPropertyDescriptor propertyDescriptor, Locale locale) {
    int maxTranslationLength = -1;
    if (getTranslationProvider() != null && propertyDescriptor.isTranslated()) {
      for (Object enumerationValue : propertyDescriptor.getEnumerationValues()) {
        String translation = getTranslationProvider().getTranslation(
            computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                enumerationValue), locale);
        if (translation.length() > maxTranslationLength) {
          maxTranslationLength = translation.length();
        }
      }
    } else {
      maxTranslationLength = propertyDescriptor.getMaxLength().intValue();
    }
    if (maxTranslationLength == -1
        || maxTranslationLength > getMaxCharacterLength()) {
      maxTranslationLength = getMaxCharacterLength();
    }
    return getStringTemplateValue(new Integer(maxTranslationLength));
  }

  /**
   * Computes the number of characters used to represent a template value based
   * on a formatter.
   * 
   * @param formatter
   *          the formatter.
   * @param templateValue
   *          the template value.
   * @return the number of characters used to represent the template value.
   */
  protected int getFormatLength(IFormatter formatter, Object templateValue) {
    int formatLength;
    if (formatter != null) {
      formatLength = formatter.format(templateValue).length();
    } else {
      if (templateValue != null) {
        formatLength = templateValue.toString().length();
      } else {
        formatLength = getMaxCharacterLength();
      }
    }
    return formatLength;
  }

  /**
   * Gets an integer template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the integer template value.
   */
  protected Integer getIntegerTemplateValue(
      IIntegerPropertyDescriptor propertyDescriptor) {
    double templateValue = DEF_DISP_MAX_VALUE;
    if (propertyDescriptor.getMaxValue() != null) {
      templateValue = propertyDescriptor.getMaxValue().doubleValue();
    }
    return new Integer((int) templateValue);
  }

  /**
   * Gets the maxCharacterLength.
   * 
   * @return the maxCharacterLength.
   */
  protected int getMaxCharacterLength() {
    return maxCharacterLength;
  }

  /**
   * Gets the maxColumnCharacterLength.
   * 
   * @return the maxColumnCharacterLength.
   */
  protected int getMaxColumnCharacterLength() {
    return maxColumnCharacterLength;
  }

  /**
   * Gets the modelCascadingBinder.
   * 
   * @return the modelCascadingBinder.
   */
  protected IModelCascadingBinder getModelCascadingBinder() {
    return modelCascadingBinder;
  }

  /**
   * Gets the mvcBinder.
   * 
   * @return the mvcBinder.
   */
  protected IMvcBinder getMvcBinder() {
    return mvcBinder;
  }

  /**
   * Gets an percent template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the percent template value.
   */
  protected Double getPercentTemplateValue(
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

  /**
   * Gets an string template value.
   * 
   * @param maxLength
   *          the attribute max length.
   * @return the string template value.
   */
  protected String getStringTemplateValue(Integer maxLength) {
    StringBuffer templateValue = new StringBuffer();
    int fieldLength = getMaxCharacterLength();
    if (maxLength != null) {
      fieldLength = maxLength.intValue();
    }
    for (int i = 0; i < fieldLength; i++) {
      templateValue.append(TEMPLATE_CHAR);
    }
    return templateValue.toString();
  }

  /**
   * Gets an string template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the string template value.
   */
  protected String getStringTemplateValue(
      IStringPropertyDescriptor propertyDescriptor) {
    return getStringTemplateValue(propertyDescriptor.getMaxLength());
  }

  /**
   * Gets a template value matching a property descriptor. This is useful for
   * computing preferred width on components.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return a field template value.
   */
  protected Object getTemplateValue(IPropertyDescriptor propertyDescriptor) {
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
    } else if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      return getTemplateValue(((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor().getPropertyDescriptor(
              ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                  .getReferencedDescriptor().getToStringProperty()));
    }
    return null;
  }

  /**
   * Gets a time template value.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the time template value.
   */
  protected Date getTimeTemplateValue(ITimePropertyDescriptor propertyDescriptor) {
    return TEMPLATE_TIME;
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  /**
   * Shows a card in in card layouted panel.
   * 
   * @param cardsPeer
   *          the component that holds the cards
   * @param cardName
   *          the card identifier to show.
   */
  protected abstract void showCardInPanel(E cardsPeer, String cardName);

  private IView<E> createCollectionPropertyView(
      IPropertyViewDescriptor propertyViewDescriptor,
      IActionHandler actionHandler, Locale locale) {

    IView<E> view;
    ICollectionPropertyDescriptor<?> propertyDescriptor = (ICollectionPropertyDescriptor<?>) propertyViewDescriptor
        .getModelDescriptor();
    List<String> renderedChildProperties = propertyViewDescriptor
        .getRenderedChildProperties();
    if (renderedChildProperties != null && renderedChildProperties.size() > 1) {
      BasicTableViewDescriptor viewDescriptor = new BasicTableViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      List<IPropertyViewDescriptor> columnViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
      for (String renderedProperty : renderedChildProperties) {
        BasicPropertyViewDescriptor columnDescriptor = new BasicPropertyViewDescriptor();
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

  /**
   * Creates a collection view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created collection view.
   */
  private IView<E> createCollectionView(
      ICollectionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<E> view = null;
    if (viewDescriptor instanceof IListViewDescriptor) {
      view = createListView((IListViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITableViewDescriptor) {
      view = createTableView((ITableViewDescriptor) viewDescriptor,
          actionHandler, locale);
    }
    return view;
  }

  private ICollectionConnectorProvider createCompositeNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale, ICompositeTreeLevelDescriptor subtreeViewDescriptor,
      int depth) {
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
        if (actionHandler.isAccessGranted(childDescriptor)) {
          ICollectionConnectorProvider childConnector = createNodeGroupConnector(
              viewDescriptor, actionHandler, locale, childDescriptor, depth + 1);
          nodeGroupPrototypeConnector.addChildConnector(childConnector);
          subtreeConnectors.add(childConnector);
        }
      }
    }
    nodeGroupPrototypeConnector
        .setCollectionConnectorProviders(subtreeConnectors);
    if (nodeGroupPrototypeConnector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayIconImageUrl(subtreeViewDescriptor
              .getNodeGroupDescriptor().getIconImageURL());
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }

    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  private ICollectionConnectorProvider createNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale, ITreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionConnectorProvider connector = null;
    if (subtreeViewDescriptor instanceof ICompositeTreeLevelDescriptor) {
      connector = createCompositeNodeGroupConnector(viewDescriptor,
          actionHandler, locale,
          (ICompositeTreeLevelDescriptor) subtreeViewDescriptor, depth);
    } else if (subtreeViewDescriptor instanceof ISimpleTreeLevelDescriptor) {
      connector = createSimpleNodeGroupConnector(viewDescriptor, actionHandler,
          locale, (ISimpleTreeLevelDescriptor) subtreeViewDescriptor, depth);
    }
    if (connector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) connector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(translationProvider, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(translationProvider, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayIconImageUrl(subtreeViewDescriptor
              .getNodeGroupDescriptor().getIconImageURL());
      ((AbstractCompositeValueConnector) connector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }
    return connector;
  }

  private ICollectionConnectorProvider createSimpleNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale, ISimpleTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionPropertyDescriptor<?> nodeGroupModelDescriptor = (ICollectionPropertyDescriptor<?>) subtreeViewDescriptor
        .getNodeGroupDescriptor().getModelDescriptor();
    IConfigurableCollectionConnectorProvider nodeGroupPrototypeConnector = connectorFactory
        .createConfigurableCollectionConnectorProvider(nodeGroupModelDescriptor
            .getName()
            + "Element", subtreeViewDescriptor.getNodeGroupDescriptor()
            .getRenderedProperty());
    ITreeLevelDescriptor childDescriptor = subtreeViewDescriptor
        .getChildDescriptor();
    if (childDescriptor != null && depth < viewDescriptor.getMaxDepth()
        && actionHandler.isAccessGranted(childDescriptor)) {
      ICollectionConnectorProvider childConnector = createNodeGroupConnector(
          viewDescriptor, actionHandler, locale, childDescriptor, depth + 1);
      nodeGroupPrototypeConnector.addChildConnector(childConnector);
      nodeGroupPrototypeConnector
          .setCollectionConnectorProvider(childConnector);
    }
    if (nodeGroupPrototypeConnector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(translationProvider, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayIconImageUrl(subtreeViewDescriptor
              .getNodeGroupDescriptor().getIconImageURL());
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  /**
   * Gets wether a property view is considered to fill all the available height
   * space.
   * 
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @return true if a property view is considered to fill all the available
   *         height space.
   */
  protected boolean isHeightExtensible(
      IPropertyViewDescriptor propertyViewDescriptor) {
    IModelDescriptor propertyDescriptor = propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof ITextPropertyDescriptor
        || propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
      return true;
    }
    return false;
  }

  /**
   * Gets the modelConnectorFactory.
   * 
   * @return the modelConnectorFactory.
   */
  protected IModelConnectorFactory getModelConnectorFactory() {
    return modelConnectorFactory;
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *          the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Selects the first element of a collection connector when its value changes.
   * 
   * @param collectionConnector
   *          the collection connector to attach the listener to.
   */
  protected void attachDefaultCollectionListener(
      ICollectionConnector collectionConnector) {
    collectionConnector.addValueChangeListener(firstRowSelector);

  }

  private class ItemSelectionAdapter implements IItemSelectionListener {

    private IAction        actionDelegate;
    private IActionHandler actionHandler;
    private IView<E>       view;

    public ItemSelectionAdapter(IAction actionDelegate,
        IActionHandler actionHandler, IView<E> view) {
      this.actionDelegate = actionDelegate;
      this.actionHandler = actionHandler;
      this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    public void selectedItemChange(ItemSelectionEvent event) {
      Map<String, Object> context = getActionFactory().createActionContext(
          actionHandler, view.getDescriptor().getModelDescriptor(),
          view.getPeer(), view.getConnector(), null, view.getPeer());
      context.put(ActionContextConstants.ACTION_PARAM, event.getSelectedItem());
      actionHandler.execute(actionDelegate, context);
    }
  }

  /**
   * Applies a component preferred size.
   * 
   * @param component
   *          the component to apply the preferred sze on.
   * @param preferredSize
   *          vthe preferred size to apply (might be null).
   */
  protected abstract void applyPreferredSize(E component,
      Dimension preferredSize);

  /**
   * Gets the defaultActionMapRenderingOptions.
   * 
   * @return the defaultActionMapRenderingOptions.
   */
  protected ERenderingOptions getDefaultActionMapRenderingOptions() {
    return defaultActionMapRenderingOptions;
  }

  /**
   * Sets the defaultActionMapRenderingOptions.
   * 
   * @param defaultActionMapRenderingOptions
   *          the defaultActionMapRenderingOptions to set.
   */
  public void setDefaultActionMapRenderingOptions(
      ERenderingOptions defaultActionMapRenderingOptions) {
    this.defaultActionMapRenderingOptions = defaultActionMapRenderingOptions;
  }
}
