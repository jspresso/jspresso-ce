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
package org.jspresso.framework.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.action.IActionHandlerAware;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorListProvider;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.masterdetail.IModelCascadingBinder;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
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
import org.jspresso.framework.model.descriptor.ITimeAwarePropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.security.EAuthorization;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityHandlerAware;
import org.jspresso.framework.security.ISubjectAware;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.IValueChangeSource;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.format.DurationFormatter;
import org.jspresso.framework.util.format.EnumerationFormatter;
import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.format.NullableSimpleDateFormat;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.ModelTrackingGate;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.gui.IClientTypeAware;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.ICloneable;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IEnumerationPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IHtmlViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.IMapViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.PropertyViewDescriptorHelper;

/**
 * Abstract base class factory for views.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
@SuppressWarnings("UnusedParameters")
public abstract class AbstractViewFactory<E, F, G> implements
    IViewFactory<E, F, G> {

  /**
   * {@code BOLD_FONT}.
   */
  protected static final String BOLD_FONT                 = ";BOLD;";
  /**
   * {@code DEF_DISP_MAX_VALUE}.
   */
  protected static final double DEF_DISP_MAX_VALUE        = 1000;
  /**
   * {@code DEF_DISP_TEMPLATE_PERCENT}.
   */
  protected static final double DEF_DISP_TEMPLATE_PERCENT = 99;
  /**
   * {@code TEMPLATE_CHAR}.
   */
  protected static final char   TEMPLATE_CHAR             = 'O';
  /**
   * {@code TEMPLATE_DATE}.
   */
  protected static final Date   TEMPLATE_DATE             = new Date(27166271000L);
  /**
   * {@code TEMPLATE_DURATION}.
   */
  protected static final Long   TEMPLATE_DURATION         = (long) (EDuration.ONE_SECOND.getMillis() + EDuration
      .ONE_MINUTE.getMillis() + EDuration.ONE_HOUR.getMillis() + EDuration.ONE_DAY.getMillis() + EDuration.ONE_WEEK
                                                                                                          .getMillis());

  /**
   * {@code TEMPLATE_TIME}.
   */
  protected static final Date TEMPLATE_TIME = new Date(366000);

  private IActionFactory<G, E>          actionFactory;
  private IDisplayableAction            binaryPropertyInfoAction;
  private IConfigurableConnectorFactory connectorFactory;
  private ERenderingOptions defaultActionMapRenderingOptions = ERenderingOptions.ICON;
  private final IValueChangeListener        firstRowSelector;
  private       IIconFactory<F>             iconFactory;
  private       IComponentCollectionFactory componentCollectionFactory;

  private IDisplayableAction lovAction;
  private IDisplayableAction componentsLovActionTemplate;

  private int maxCharacterLength       = 32;
  private int maxColumnCharacterLength = 32;
  private IModelCascadingBinder  modelCascadingBinder;
  private IModelConnectorFactory modelConnectorFactory;
  private IMvcBinder             mvcBinder;

  private IDisplayableAction openFileAsBinaryPropertyAction;
  private IDisplayableAction resetPropertyAction;
  private IDisplayableAction saveBinaryPropertyAsFileAction;
  private String formLabelMandatoryPropertyColorHex   = "0xFFFF0000";
  private String tableHeaderMandatoryPropertyColorHex = null;

  private boolean liveDebugUI = false;

  /**
   * Constructs a new {@code AbstractViewFactory} instance.
   */
  protected AbstractViewFactory() {
    firstRowSelector = new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        if (evt.getNewValue() != null && !((Collection<?>) evt.getNewValue()).isEmpty()) {
          ((ICollectionConnector) evt.getSource()).setSelectedIndices(0);
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public IView<E> createView(IViewDescriptor viewDescriptor, IActionHandler actionHandler, Locale locale) {
    try {
      actionHandler.pushToSecurityContext(viewDescriptor);
      IView<E> view = createCustomView(viewDescriptor, actionHandler, locale);
      if (view == null) {
        if (viewDescriptor instanceof IComponentViewDescriptor) {
          view = createComponentView((IComponentViewDescriptor) viewDescriptor, actionHandler, locale);
        } else if (viewDescriptor instanceof IActionViewDescriptor) {
          view = createActionView((IActionViewDescriptor) viewDescriptor, actionHandler, locale);
        } else if (viewDescriptor instanceof IPropertyViewDescriptor) {
          view = createPropertyView((IPropertyViewDescriptor) viewDescriptor, actionHandler, locale);
        } else if (viewDescriptor instanceof ICollectionViewDescriptor) {
          view = createCollectionView((ICollectionViewDescriptor) viewDescriptor, actionHandler, locale);
          finishCollectionViewConfiguration(view, (ICollectionViewDescriptor) viewDescriptor, actionHandler, locale);
        } else if (viewDescriptor instanceof ICardViewDescriptor) {
          view = createCardView((ICardViewDescriptor) viewDescriptor, actionHandler, locale);
        } else if (viewDescriptor instanceof ITreeViewDescriptor) {
          view = createTreeView((ITreeViewDescriptor) viewDescriptor, actionHandler, locale);
          if (((ITreeViewDescriptor) viewDescriptor).getItemSelectionAction() != null) {
            ((IItemSelectable) view.getConnector()).addItemSelectionListener(new ConnectorActionAdapter<>(
                ((ITreeViewDescriptor) viewDescriptor).getItemSelectionAction(), getActionFactory(), actionHandler,
                view));
          }
        } else if (viewDescriptor instanceof IMapViewDescriptor) {
          view = createMapView((IMapViewDescriptor) viewDescriptor, actionHandler, locale);
        } else if (viewDescriptor instanceof ICompositeViewDescriptor) {
          view = createCompositeView((ICompositeViewDescriptor) viewDescriptor, actionHandler, locale);
        }
      }
      if (view != null) {
        view.getConnector().setSecurityHandler(actionHandler);
        boolean locallyWritable = !viewDescriptor.isReadOnly();
        if (locallyWritable) {
          try {
            actionHandler.pushToSecurityContext(EAuthorization.ENABLED);
            locallyWritable = actionHandler.isAccessGranted(viewDescriptor);
          } finally {
            actionHandler.restoreLastSecurityContextSnapshot();
          }
        }
        view.getConnector().setLocallyWritable(locallyWritable);
        if (viewDescriptor.getReadabilityGates() != null) {
          for (IGate gate : viewDescriptor.getReadabilityGates()) {
            if (!(gate instanceof ISecurable) || actionHandler.isAccessGranted((ISecurable) gate)) {
              IGate clonedGate = gate.clone();
              if (clonedGate instanceof IActionHandlerAware) {
                ((IActionHandlerAware) clonedGate).setActionHandler(actionHandler);
              }
              view.getConnector().addReadabilityGate(clonedGate);
            }
          }
        }
        if (viewDescriptor.getWritabilityGates() != null) {
          for (IGate gate : viewDescriptor.getWritabilityGates()) {
            if (!(gate instanceof ISecurable) || actionHandler.isAccessGranted((ISecurable) gate)) {
              IGate clonedGate = gate.clone();
              if (clonedGate instanceof IActionHandlerAware) {
                ((IActionHandlerAware) clonedGate).setActionHandler(actionHandler);
              }
              view.getConnector().addWritabilityGate(clonedGate);
            }
          }
        }
        finishComponentConfiguration(viewDescriptor, actionHandler, locale, view);
        decorateWithActions(viewDescriptor, actionHandler, locale, view);
        decorateWithBorder(view, actionHandler, locale);
        view.getConnector().setModelDescriptor(viewDescriptor.getModelDescriptor());
        if (!actionHandler.isAccessGranted(viewDescriptor)) {
          view.setPeer(createSecurityComponent());
        }
        applyPreferredSize(view.getPeer(), viewDescriptor.getPreferredSize());
      } else {
        view = createEmptyView(viewDescriptor, actionHandler, locale);
      }
      return view;
    } finally {
      actionHandler.restoreLastSecurityContextSnapshot();
    }
  }

  /**
   * Creates a and binds a collection pagination view.
   *
   * @param paginationViewDescriptor
   *          the pagination view descriptor.
   * @param view
   *          the view to complete.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the ready to assemble pagination view.
   */
  protected IView<E> createPaginationView(IViewDescriptor paginationViewDescriptor, IView<E> view,
                                          IActionHandler actionHandler, Locale locale) {
    final IView<E> paginationView = createView(paginationViewDescriptor, actionHandler, locale);
    (view.getConnector()).addPropertyChangeListener(IValueConnector.MODEL_CONNECTOR_PROPERTY,
        new PropertyChangeListener() {

          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() != null) {
              getMvcBinder().bind(paginationView.getConnector(),
                  ((IValueConnector) evt.getNewValue()).getParentConnector());
            } else {
              getMvcBinder().bind(paginationView.getConnector(), null);
            }
          }
        });
    return paginationView;
  }

  /**
   * Gets the actionFactory.
   *
   * @return the actionFactory.
   */
  @Override
  public IActionFactory<G, E> getActionFactory() {
    return actionFactory;
  }

  /**
   * Gets the connectorFactory.
   *
   * @return the connectorFactory.
   */
  @Override
  public IConfigurableConnectorFactory getConnectorFactory() {
    return connectorFactory;
  }

  /**
   * Gets the iconFactory.
   *
   * @return the iconFactory.
   */
  @Override
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
  public void setBinaryPropertyInfoAction(IDisplayableAction binaryPropertyInfoAction) {
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
   * Sets the defaultActionMapRenderingOptions.
   *
   * @param defaultActionMapRenderingOptions
   *          the defaultActionMapRenderingOptions to set.
   */
  public void setDefaultActionMapRenderingOptions(ERenderingOptions defaultActionMapRenderingOptions) {
    this.defaultActionMapRenderingOptions = defaultActionMapRenderingOptions;
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
   * Sets components lov action template.
   *
   * @param componentsLovActionTemplate the components lov action template
   */
  public void setComponentsLovActionTemplate(IDisplayableAction componentsLovActionTemplate) {
    this.componentsLovActionTemplate = componentsLovActionTemplate;
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
   * Selects a child view in an indexed view, e.g. a tab view.
   *
   * @param viewComponent
   *          the indexed view.
   * @param index
   *          the child view index to select.
   */
  protected abstract void selectChildViewIndex(E viewComponent, int index);

  /**
   * Applies a component preferred size.
   *
   * @param component
   *          the component to apply the preferred sze on.
   * @param preferredSize
   *          the preferred size to apply (might be null).
   */
  protected abstract void applyPreferredSize(E component,
      Dimension preferredSize);

  /**
   * Selects the first element of a collection connector when its value changes.
   *
   * @param collectionConnector
   *          the collection connector to attach the listener to.
   */
  public void attachDefaultCollectionListener(ICollectionConnector collectionConnector) {
    collectionConnector.addValueChangeListener(firstRowSelector);
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
      if (view.getDescriptor() instanceof ICompositeViewDescriptor) {
        ICompositeViewDescriptor viewDescriptor = (ICompositeViewDescriptor) view
            .getDescriptor();
        if (viewDescriptor.isCascadingModels()) {
          IView<E> masterView = view.getChildren().get(0);
          IValueConnector viewConnector;
          if (masterView.getDescriptor().getModelDescriptor() instanceof IPropertyDescriptor) {
            IConfigurableCollectionConnectorProvider mainConnector = getConnectorFactory()
                .createConfigurableCollectionConnectorProvider(
                    ModelRefPropertyConnector.THIS_PROPERTY, null);
            mainConnector.addChildConnector(masterView.getConnector().getId(),
                masterView.getConnector());
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
            mainConnector.addChildConnector(masterView.getConnector().getId(),
                masterView.getConnector());
            viewConnector = mainConnector;
          }
          view.setConnector(viewConnector);
          for (int i = 1; i < view.getChildren().size(); i++) {
            IView<E> detailView = view.getChildren().get(i);

            IValueConnector detailConnector;
            if (detailView.getDescriptor().getModelDescriptor() instanceof IPropertyDescriptor) {
              IConfigurableCollectionConnectorProvider wrapper = getConnectorFactory()
                  .createConfigurableCollectionConnectorProvider(
                      ModelRefPropertyConnector.THIS_PROPERTY, null);
              wrapper.addChildConnector(detailView.getConnector().getId(),
                  detailView.getConnector());
              if (detailView.getConnector() instanceof ICollectionConnector) {
                wrapper
                    .setCollectionConnectorProvider((ICollectionConnector) detailView
                        .getConnector());
              }
              detailConnector = wrapper;
            } else {
              detailConnector = detailView.getConnector();
            }

            // We must dig into the composite structure to find the 1st non
            // composite view
            // to cascade the model
            while (masterView instanceof ICompositeView<?>) {
              masterView = ((ICompositeView<E>) masterView).getChildren()
                  .get(0);
            }
            getModelCascadingBinder().bind(masterView.getConnector(),
                detailConnector);
            masterView = detailView;
          }
        } else {
          String connectorId;
          IModelDescriptor modelDescriptor = viewDescriptor.getModelDescriptor();
          if (modelDescriptor instanceof IPropertyDescriptor) {
            connectorId = modelDescriptor.getName();
          } else {
            connectorId = ModelRefPropertyConnector.THIS_PROPERTY;
          }
          ICompositeValueConnector connector = getConnectorFactory()
              .createCompositeValueConnector(connectorId, null);
          view.setConnector(connector);
          for (IView<E> childView : view.getChildren()) {
            connector.addChildConnector(childView.getConnector().getId(),
                childView.getConnector());
          }
        }
      }
    }
  }

  /**
   * Computes a table column identifier that is used for sorting.
   *
   * @param viewDescriptor
   *          the table view descriptor.
   * @param columnDescriptor
   *          the column descriptor behind the column.
   * @return the column identifier.
   */
  protected String computeColumnIdentifier(ITableViewDescriptor viewDescriptor,
      IPropertyViewDescriptor columnDescriptor) {
    IComponentDescriptor<?> rowDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor()).getCollectionDescriptor().getElementDescriptor();
    String propertyName = columnDescriptor.getModelDescriptor().getName();
    String identifier = propertyName;
    String renderedProperty = computeRenderedProperty(columnDescriptor);
    if (renderedProperty != null) {
      // for ref sorting to occur properly.
      identifier = identifier + "." + renderedProperty;
    }
    boolean sortable = columnDescriptor.isSortable();
    if (sortable
        && PropertyViewDescriptorHelper.isComputed(rowDescriptor, propertyName)) {
      if (viewDescriptor.getPaginationViewDescriptor() != null) {
        // disable sort only if the table is not paginated
        sortable = rowDescriptor.getPropertyDescriptor(propertyName)
            .getPersistenceFormula() != null;
      }
    }
    if (!sortable) {
      return "#" + identifier;
    }
    return identifier;
  }

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
    BasicCompositeView<E> view = new BasicCompositeView<>(viewComponent);
    view.setDescriptor(descriptor);
    return view;
  }

  /**
   * Creates an indexed view, e.g. for tab container.
   *
   * @param viewComponent
   *          the view component peer.
   * @param descriptor
   *          the view descriptor.
   * @return the created indexed view.
   */
  protected BasicIndexedView<E> constructIndexedView(final E viewComponent,
      final ITabViewDescriptor descriptor) {
    BasicIndexedView<E> indexedView = new BasicIndexedView<E>(viewComponent) {

      @Override
      public void setConnector(IValueConnector connector) {
        super.setConnector(connector);
        if (descriptor.isLazy() && connector != null) {
          // Only keep the selected tab connector bound
          connector.addValueChangeListener(new IValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent evt) {
              ICompositeValueConnector parentConnector = (AbstractCompositeValueConnector) getConnector();
              for (IView<E> tabView : getChildren()) {
                IValueConnector tabViewConnector = tabView.getConnector();
                if (tabViewConnector.getParentConnector() != null) {
                  getMvcBinder().bind(tabViewConnector, null);
                  parentConnector.removeChildConnector(tabViewConnector.getId());
                  tabViewConnector.setParentConnector(null);
                }
              }
              rebindTabViewIfNecessary(getChildView(getCurrentViewIndex()));
            }
          });
        }
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void setCurrentViewIndex(int index) {
        int oldIndex = getCurrentViewIndex();
        if (index == oldIndex) {
          return;
        }
        super.setCurrentViewIndex(index);
        selectChildViewIndex(viewComponent, index);
        if (descriptor.isLazy()) {
          IView<E> oldSelectedView = getChildView(oldIndex);
          IView<E> newSelectedView = getChildView(index);

          if (newSelectedView != null && oldSelectedView != null) {
            rebindTabViewIfNecessary(newSelectedView);
          }
        }
      }

      private void rebindTabViewIfNecessary(IView<E> tabView) {
        IValueConnector childConnector = tabView.getConnector();
        ICompositeValueConnector parentConnector = (AbstractCompositeValueConnector) getConnector();
        if (parentConnector != null && childConnector != null && childConnector.getParentConnector() == null) {
          parentConnector.addChildConnector(childConnector.getId(),
              childConnector);
          if (parentConnector.getModelConnector() != null) {
            getMvcBinder().bind(
                childConnector,
                ((ICompositeValueConnector) parentConnector
                    .getModelConnector()).getChildConnector(childConnector
                    .getId()));
          } else {
            getMvcBinder().bind(childConnector, null);
          }
        }
      }
    };
    indexedView.setDescriptor(descriptor);
    return indexedView;
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
    BasicMapView<E> view = new BasicMapView<>(viewComponent);
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
    BasicView<E> view = new BasicView<>(viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

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
   * Creates the action list for a binary property (open from file, save as
   * file, reset, size info).
   *
   * @param propertyView
   *          the view these actions will be triggered from.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the action list.
   */
  protected List<G> createBinaryActions(IView<E> propertyView,
      IActionHandler actionHandler, Locale locale) {
    G openAction = getActionFactory().createAction(
        openFileAsBinaryPropertyAction, actionHandler, propertyView, locale);
    G saveAction = getActionFactory().createAction(
        saveBinaryPropertyAsFileAction, actionHandler, propertyView, locale);
    G resetAction = getActionFactory().createAction(resetPropertyAction,
        actionHandler, propertyView, locale);
    G infoAction = getActionFactory().createAction(binaryPropertyInfoAction,
        actionHandler, propertyView, locale);
    List<G> binaryActions = new ArrayList<>();
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
        .createValueConnector(
            getConnectorIdForBeanView(cardView.getDescriptor()));
    cardViewConnector.addValueChangeListener(new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        refreshCardView(cardView, false, actionHandler, locale);
      }
    });
    return cardViewConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshCardView(IMapView<E> cardView, boolean unbindPrevious,
      IActionHandler actionHandler, Locale locale) {
    IValueConnector cardViewConnector = cardView.getConnector();
    Object cardModel = cardViewConnector.getConnectorValue();
    E cardsPeer = cardView.getPeer();
    IView<E> currentChildCardView = cardView.getCurrentView();
    ICardViewDescriptor cardViewDescriptor = cardView.getDescriptor();
    String cardName = cardViewDescriptor.getCardNameForModel(cardModel, actionHandler.getSubject());
    if (cardName != null) {
      IView<E> childCardView = cardView.getChild(cardName);
      if (childCardView == null) {
        IViewDescriptor childCardViewDescriptor = cardViewDescriptor
            .getCardViewDescriptor(cardName);
        if (childCardViewDescriptor != null) {
          childCardView = createView(childCardViewDescriptor, actionHandler, locale);
          addCard(cardView, childCardView, cardName);
        }
      }
      if (childCardView != null) {
        cardView.setCurrentView(childCardView);
        IViewDescriptor childCardViewDescriptor = childCardView.getDescriptor();
        boolean accessGranted = actionHandler.isAccessGranted(childCardViewDescriptor);
        if (cardModel instanceof ISecurable) {
          accessGranted = accessGranted
              && actionHandler.isAccessGranted((ISecurable) cardModel);
        }
        if (accessGranted) {
          showCardInPanel(cardsPeer, cardName);
        } else {
          // Do not unbind current connector for performance reason.

          if (unbindPrevious && currentChildCardView != null
              && currentChildCardView.getConnector() != null) {
            getMvcBinder().bind(currentChildCardView.getConnector(), null);
          }
          showCardInPanel(cardsPeer, ICardViewDescriptor.SECURITY_CARD);
        }
        IValueConnector childCardConnector = childCardView.getConnector();
        if (childCardConnector != null) {
          // To handle polymorphism, especially for modules, we refine
          // the model descriptor.
          IValueConnector modelConnector = cardViewConnector
              .getModelConnector();
          if (modelConnector != null
              && cardViewConnector
                  .getModelDescriptor()
                  .getModelType()
                  .isAssignableFrom(childCardViewDescriptor.getModelDescriptor().getModelType())) {
            modelConnector.setModelDescriptor(childCardViewDescriptor
                .getModelDescriptor());
          }
          if (unbindPrevious && currentChildCardView != null
              && currentChildCardView.getConnector() != null) {
            getMvcBinder().bind(currentChildCardView.getConnector(), null);
          }
          getMvcBinder().bind(childCardConnector, modelConnector);
        }
      } else {
        // Do not unbind current connector for performance reason.

        if (unbindPrevious && currentChildCardView != null
            && currentChildCardView.getConnector() != null) {
          getMvcBinder().bind(currentChildCardView.getConnector(), null);
        }
        showCardInPanel(cardsPeer, ICardViewDescriptor.DEFAULT_CARD);
      }
    } else {
      // Do not unbind current connector for performance reason.

      if (unbindPrevious && currentChildCardView != null
          && currentChildCardView.getConnector() != null) {
        getMvcBinder().bind(currentChildCardView.getConnector(), null);
      }
      showCardInPanel(cardsPeer, ICardViewDescriptor.DEFAULT_CARD);
    }
  }

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
   *          the column descriptor to create the connector for.
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
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) columnViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + columnId + " defined for "
          + descriptor.getComponentContract());
    }
    IValueConnector columnConnector;
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      String renderedProperty = computeRenderedProperty(columnViewDescriptor);
      columnConnector = getConnectorFactory().createCompositeValueConnector(
          columnId, renderedProperty);
    } else {
      columnConnector = getConnectorFactory().createValueConnector(
          propertyDescriptor.getName());
    }

    columnConnector.setSecurityHandler(actionHandler);
    boolean locallyWritable = !columnViewDescriptor.isReadOnly();
    if (locallyWritable) {
      try {
        actionHandler.pushToSecurityContext(EAuthorization.ENABLED);
        locallyWritable = actionHandler.isAccessGranted(columnViewDescriptor);
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
    columnConnector.setLocallyWritable(locallyWritable);
    if (columnViewDescriptor.getReadabilityGates() != null) {
      for (IGate gate : columnViewDescriptor.getReadabilityGates()) {
        if (!(gate instanceof ISecurable)
            || actionHandler.isAccessGranted((ISecurable) gate)) {
          IGate clonedGate = gate.clone();
          applyGateDependencyInjection(clonedGate, actionHandler);
          columnConnector.addReadabilityGate(clonedGate);
        }
      }
    }
    if (columnViewDescriptor.getWritabilityGates() != null) {
      for (IGate gate : columnViewDescriptor.getWritabilityGates()) {
        if (!(gate instanceof ISecurable)
            || actionHandler.isAccessGranted((ISecurable) gate)) {
          IGate clonedGate = gate.clone();
          applyGateDependencyInjection(clonedGate, actionHandler);
          columnConnector.addWritabilityGate(clonedGate);
        }
      }
    }
    return columnConnector;
  }

  /**
   * Performs dependency injection on a gate.
   *
   * @param gate
   *          the gate.
   * @param actionHandler
   *          the action handler.
   */
  protected void applyGateDependencyInjection(IGate gate,
      IActionHandler actionHandler) {
    if (gate instanceof ISecurityHandlerAware) {
      ((ISecurityHandlerAware) gate).setSecurityHandler(actionHandler);
    }
    if (gate instanceof IActionHandlerAware) {
      ((IActionHandlerAware) gate).setActionHandler(actionHandler);
    }
    if (gate instanceof ISubjectAware) {
      ((ISubjectAware) gate).setSubject(actionHandler.getSubject());
    }
  }

  /**
   * Compute the rendered nested property of a reference property view
   * descriptor.
   *
   * @param propertyViewDescriptor the property view descriptor.
   * @return the rendered property.
   */
  protected String computeRenderedProperty(
      IPropertyViewDescriptor propertyViewDescriptor) {
    String renderedProperty = null;
    IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      List<String> renderedProperties = propertyViewDescriptor
          .getRenderedChildProperties();
      if (renderedProperties != null && !renderedProperties.isEmpty()) {
        // it's a custom rendered property.
        renderedProperty = renderedProperties.get(0);
      } else {
        IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor();
        renderedProperty = referencedDescriptor.getToStringProperty();
      }
    }
    return renderedProperty;
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
   * Creates a map view.
   *
   * @param viewDescriptor
   *     the view descriptor.
   * @param actionHandler
   *     the action handler.
   * @param locale
   *     the locale.
   * @return the created component view.
   */
  protected abstract IView<E> createMapView(IMapViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                                  Locale locale);

  /**
   * Computes the property name used to compute a property view dynamic label
   * or null if none or if the label is a static one. Note that for the label to be considered as a dynamic one,
   * the name must be different from the bound property name.
   *
   * @param modelDescriptor
   *     the component model descriptor.
   * @param viewDescriptor
   *     the view descriptor
   * @param propertyDescriptor
   *     the property descriptor.
   * @return the property name used to compute a property view dynamic label
   * or null if none or if the label is a static one.
   */
  protected String computePropertyDynamicLabel(IComponentDescriptor<?> modelDescriptor, IViewDescriptor viewDescriptor,
                                               IPropertyDescriptor propertyDescriptor) {
    String dynamicLabelProperty = null;
    String labelKey = null;
    if (viewDescriptor.getName() != null) {
      labelKey = viewDescriptor.getName();
    }
    if (labelKey != null && !labelKey.equals(propertyDescriptor.getName())) {
      IPropertyDescriptor labelProperty = modelDescriptor.getPropertyDescriptor(labelKey);
      if (labelProperty != null) {
        dynamicLabelProperty = labelProperty.getName();
      }
    }
    return dynamicLabelProperty;
  }

  /**
   * Computes the property name used to compute a property view dynamic tooltip
   * or null if none or if the tooltip is a static one.
   *
   * @param modelDescriptor
   *          the component model descriptor.
   * @param viewDescriptor
   *          the view descriptor
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the property name used to compute a property view dynamic tooltip
   *         or null if none or if the tooltip is a static one.
   */
  protected String computePropertyDynamicToolTip(
      IComponentDescriptor<?> modelDescriptor,
      IViewDescriptor viewDescriptor,
      IPropertyDescriptor propertyDescriptor) {
    String dynamicToolTipProperty = null;
    String descriptionKey;
    if (viewDescriptor.getDescription() != null) {
      descriptionKey = viewDescriptor.getDescription();
    } else {
      descriptionKey = propertyDescriptor.getDescription();
    }
    if (descriptionKey != null) {
      IPropertyDescriptor descriptionProperty = modelDescriptor
          .getPropertyDescriptor(descriptionKey);
      if (descriptionProperty != null) {
        dynamicToolTipProperty = descriptionProperty.getName();
      }
    }
    return dynamicToolTipProperty;
  }

  /**
   * Computes the property name used to compute a property view dynamic
   * background or null if none or if the background is a static one.
   *
   * @param modelDescriptor
   *          the component model descriptor.
   * @param propertyViewDescriptor
   *          the property view descriptor
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the property name used to compute a property view dynamic
   *         background or null if none or if the background is a static one.
   */
  protected String computePropertyDynamicBackground(
      IComponentDescriptor<?> modelDescriptor,
      IPropertyViewDescriptor propertyViewDescriptor,
      IPropertyDescriptor propertyDescriptor) {
    String dynamicBackgroundProperty = null;
    String backgroundKey = null;
    if (propertyViewDescriptor.getBackground() != null) {
      backgroundKey = propertyViewDescriptor.getBackground();
    }
    if (backgroundKey != null) {
      IPropertyDescriptor backgroundProperty = modelDescriptor
          .getPropertyDescriptor(backgroundKey);
      if (backgroundProperty != null) {
        dynamicBackgroundProperty = backgroundProperty.getName();
      }
    }
    return dynamicBackgroundProperty;
  }

  /**
   * Computes the property name used to compute a property view dynamic
   * foreground or null if none or if the foreground is a static one.
   *
   * @param modelDescriptor
   *          the component model descriptor.
   * @param propertyViewDescriptor
   *          the property view descriptor
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the property name used to compute a property view dynamic
   *         foreground or null if none or if the foreground is a static one.
   */
  protected String computePropertyDynamicForeground(
      IComponentDescriptor<?> modelDescriptor,
      IPropertyViewDescriptor propertyViewDescriptor,
      IPropertyDescriptor propertyDescriptor) {
    String dynamicForegroundProperty = null;
    String foregroundKey = null;
    if (propertyViewDescriptor.getForeground() != null) {
      foregroundKey = propertyViewDescriptor.getForeground();
    }
    if (foregroundKey != null) {
      IPropertyDescriptor foregroundProperty = modelDescriptor
          .getPropertyDescriptor(foregroundKey);
      if (foregroundProperty != null) {
        dynamicForegroundProperty = foregroundProperty.getName();
      }
    }
    return dynamicForegroundProperty;
  }

  /**
   * Computes the property name used to compute a property view dynamic font or
   * null if none or if the font is a static one.
   *
   * @param modelDescriptor
   *          the component model descriptor.
   * @param propertyViewDescriptor
   *          the property view descriptor
   * @param propertyDescriptor
   *          the property descriptor.
   * @return the property name used to compute a property view dynamic font or
   *         null if none or if the font is a static one.
   */
  protected String computePropertyDynamicFont(
      IComponentDescriptor<?> modelDescriptor,
      IPropertyViewDescriptor propertyViewDescriptor,
      IPropertyDescriptor propertyDescriptor) {
    String dynamicFontProperty = null;
    String fontKey = null;
    if (propertyViewDescriptor.getFont() != null) {
      fontKey = propertyViewDescriptor.getFont();
    }
    if (fontKey != null) {
      IPropertyDescriptor fontProperty = modelDescriptor
          .getPropertyDescriptor(fontKey);
      if (fontProperty != null) {
        dynamicFontProperty = fontProperty.getName();
      }
    }
    return dynamicFontProperty;
  }

  /**
   * Computes the property name used to compute a component view dynamic tooltip
   * or null if none or if the tooltip is a static one.
   *
   * @param viewDescriptor
   *          the component view descriptor.
   * @param modelDescriptor
   *          the model descriptor.
   * @return the property name used to compute a component view dynamic tooltip
   *         or null if none or if the tooltip is a static one.
   */
  protected String computeComponentDynamicToolTip(
      IViewDescriptor viewDescriptor, IComponentDescriptor<?> modelDescriptor) {
    String dynamicToolTipProperty = null;
    if (viewDescriptor.getDescription() != null) {
      IPropertyDescriptor descriptionProperty = modelDescriptor
          .getPropertyDescriptor(viewDescriptor.getDescription());
      if (descriptionProperty != null) {
        dynamicToolTipProperty = descriptionProperty.getName();
      }
    } else {
      dynamicToolTipProperty = modelDescriptor.getToHtmlProperty();
    }
    return dynamicToolTipProperty;
  }

  /**
   * Computes the property name used to compute a component view background or
   * null if none or if the background is a static one.
   *
   * @param viewDescriptor
   *          the component view descriptor.
   * @param modelDescriptor
   *          the model descriptor.
   * @return the property name used to compute a component view dynamic
   *         background or null if none or if the background is a static one.
   */
  protected String computeComponentDynamicBackground(
      IViewDescriptor viewDescriptor, IComponentDescriptor<?> modelDescriptor) {
    String dynamicBackgroundProperty = null;
    if (viewDescriptor.getBackground() != null) {
      IPropertyDescriptor backgroundProperty = modelDescriptor
          .getPropertyDescriptor(viewDescriptor.getBackground());
      if (backgroundProperty != null) {
        dynamicBackgroundProperty = backgroundProperty.getName();
      }
    }
    return dynamicBackgroundProperty;
  }

  /**
   * Computes the property name used to compute a component view foreground or
   * null if none or if the foreground is a static one.
   *
   * @param viewDescriptor
   *          the component view descriptor.
   * @param modelDescriptor
   *          the model descriptor.
   * @return the property name used to compute a component view dynamic
   *         foreground or null if none or if the foreground is a static one.
   */
  protected String computeComponentDynamicForeground(
      IViewDescriptor viewDescriptor, IComponentDescriptor<?> modelDescriptor) {
    String dynamicForegroundProperty = null;
    if (viewDescriptor.getForeground() != null) {
      IPropertyDescriptor foregroundProperty = modelDescriptor
          .getPropertyDescriptor(viewDescriptor.getForeground());
      if (foregroundProperty != null) {
        dynamicForegroundProperty = foregroundProperty.getName();
      }
    }
    return dynamicForegroundProperty;
  }

  /**
   * Computes the property name used to compute a component view font or null if
   * none or if the font is a static one.
   *
   * @param viewDescriptor
   *          the component view descriptor.
   * @param modelDescriptor
   *          the model descriptor.
   * @return the property name used to compute a component view dynamic font or
   *         null if none or if the font is a static one.
   */
  protected String computeComponentDynamicFont(IViewDescriptor viewDescriptor,
      IComponentDescriptor<?> modelDescriptor) {
    String dynamicFontProperty = null;
    if (viewDescriptor.getFont() != null) {
      IPropertyDescriptor fontProperty = modelDescriptor
          .getPropertyDescriptor(viewDescriptor.getFont());
      if (fontProperty != null) {
        dynamicFontProperty = fontProperty.getName();
      }
    }
    return dynamicFontProperty;
  }

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
   *          the locale the view must use for i18n.
   * @return the created view or null.
   */
  protected IView<E> createCustomView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return null;
  }

  /**
   * Creates a date format based on a date property descriptor.
   *
   * @param propertyDescriptor
   *          the date property descriptor.
   * @param timeZone
   *          the timezone.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the date format.
   */
  protected SimpleDateFormat createDateFormat(IDatePropertyDescriptor propertyDescriptor, TimeZone timeZone,
                                              ITranslationProvider translationProvider, Locale locale) {
    SimpleDateFormat format;
    String formatPattern;
    if (propertyDescriptor.getFormatPattern() != null) {
      formatPattern = propertyDescriptor.getFormatPattern();
    } else {
      if (propertyDescriptor.getType() == EDateType.DATE) {
        formatPattern = getDatePattern(propertyDescriptor, translationProvider, locale);
      } else {
        formatPattern = getDatePattern(propertyDescriptor, translationProvider, locale) + " " + getTimePattern(
            propertyDescriptor, translationProvider, locale);
      }
    }
    format = new NullableSimpleDateFormat(formatPattern, locale);
    format.setTimeZone(timeZone);
    return format;
  }

  /**
   * Return the default date pattern expressed as a SimpleDateFormat pattern.
   *
   * @param propertyDescriptor
   *          the date property descriptor.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the default date pattern.
   */
  protected String getDatePattern(IDatePropertyDescriptor propertyDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    return translationProvider.getDatePattern(locale);
  }

  /**
   * Return the default first day of week expressed as a day int (0 is sunday to 6 is saturday).
   *
   * @param propertyDescriptor
   *          the date property descriptor.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the default first day of week.
   */
  protected int getFirstDayOfWeek(IDatePropertyDescriptor propertyDescriptor,
                                  ITranslationProvider translationProvider, Locale locale) {
    return translationProvider.getFirstDayOfWeek(locale);
  }

  /**
   * Return the default decimal separator.
   *
   * @param propertyDescriptor
   *          the decimal property descriptor.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the default decimal separator.
   */
  protected String getDecimalSeparator(IDecimalPropertyDescriptor propertyDescriptor,
                                       ITranslationProvider translationProvider, Locale locale) {
    return translationProvider.getDecimalSeparator(locale);
  }

  /**
   * Return the default thousands separator.
   *
   * @param propertyDescriptor
   *          the number property descriptor.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the default thousands separator.
   */
  protected String getThousandsSeparator(INumberPropertyDescriptor propertyDescriptor,
                                       ITranslationProvider translationProvider, Locale locale) {
    return translationProvider.getThousandsSeparator(locale);
  }

  /**
   * Return the default time pattern expressed as a SimpleDateFormat pattern.
   *
   * @param propertyDescriptor
   *          the time aware property descriptor.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the default date pattern.
   */
  protected String getTimePattern(
      ITimeAwarePropertyDescriptor propertyDescriptor,
      ITranslationProvider translationProvider, Locale locale) {

    if(propertyDescriptor.isMillisecondsAware()) {
      return translationProvider.getLongTimePattern(locale);
    } else if (propertyDescriptor.isSecondsAware()) {
      return translationProvider.getTimePattern(locale);
    }
    return translationProvider.getShortTimePattern(locale);
  }

  /**
   * Creates a date formatter based on a date property descriptor.
   *
   * @param propertyDescriptor
   *          the date property descriptor.
   * @param timeZone
   *          the timezone.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the date formatter.
   */
  protected IFormatter<?, String> createDateFormatter(
      IDatePropertyDescriptor propertyDescriptor, TimeZone timeZone,
      ITranslationProvider translationProvider, Locale locale) {
    return createFormatter(createDateFormat(propertyDescriptor, timeZone, translationProvider, locale));
  }

  /**
   * Creates an enumeration formatter.
   *
   * @param propertyDescriptor
   *          the enumeration property descriptor
   * @param translationProvider
   *          the translation provider to use to translate the enumeration
   *          values.
   * @param locale
   *          the locale to create the formatter for.
   * @return the formatter.
   */
  protected IFormatter<Object, String> createEnumerationFormatter(
      IEnumerationPropertyDescriptor propertyDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    Map<Object, String> translations = null;
    if (propertyDescriptor.isTranslated()) {
      translations = new HashMap<>();
      for (String value : propertyDescriptor.getEnumerationValues()) {
        translations
            .put(value, propertyDescriptor.getI18nValue(value,
                translationProvider, locale));
      }
    }
    IFormatter<Object, String> formatter = new EnumerationFormatter(
        translations);
    return formatter;
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
   * @param propertyDescriptor           the decimal property descriptor
   * @param translationProvider the translation provider
   * @param locale           the locale
   * @return the decimal format
   */
  protected NumberFormat createDecimalFormat(
      IDecimalPropertyDescriptor propertyDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);
    applyDecimalFormatSymbols(format, propertyDescriptor, translationProvider, locale);
    format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit());
    if (propertyDescriptor.isUsingBigDecimal()) {
      format.setParseBigDecimal(true);
    }
    format.setMinimumFractionDigits(format.getMaximumFractionDigits());
    format.setGroupingUsed(propertyDescriptor.isThousandsGroupingUsed());
    return format;
  }

  private void applyDecimalFormatSymbols(DecimalFormat format, INumberPropertyDescriptor propertyDescriptor,
                                         ITranslationProvider translationProvider, Locale locale) {
    DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
    if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      String decimalSeparator = getDecimalSeparator((IDecimalPropertyDescriptor) propertyDescriptor,
          translationProvider, locale);
      if (decimalSeparator != null && decimalSeparator.length() > 0) {
        symbols.setDecimalSeparator(decimalSeparator.charAt(0));
      }
    }
    String thousandsSeparator = getThousandsSeparator(propertyDescriptor, translationProvider, locale);
    if (thousandsSeparator != null && thousandsSeparator.length() > 0) {
      symbols.setGroupingSeparator(thousandsSeparator.charAt(0));
    } else {
      format.setGroupingUsed(false);
    }
    format.setDecimalFormatSymbols(symbols);
  }

  /**
   * Creates a decimal formatter based on a decimal property descriptor.
   *
   * @param propertyDescriptor           the decimal property descriptor
   * @param translationProvider the translation provider
   * @param locale           the locale
   * @return the decimal formatter
   */
  protected IFormatter<Object, String> createDecimalFormatter(
      IDecimalPropertyDescriptor propertyDescriptor, ITranslationProvider translationProvider, Locale locale) {
    return new FormatAdapter(createDecimalFormat(propertyDescriptor, translationProvider, locale));
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
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the duration formatter.
   */
  protected IFormatter<Number, String> createDurationFormatter(
      IDurationPropertyDescriptor propertyDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    return new DurationFormatter(translationProvider, locale, propertyDescriptor.isSecondsAware(),
        propertyDescriptor.isMillisecondsAware());
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
  protected IFormatter<?, String> createFormatter(Format format) {
    return new FormatAdapter(format);
  }

  /**
   * Creates a formatter based on a property descriptor.
   *
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the formatter.
   */
  protected IFormatter<?, String> createFormatter(
      IPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      TimeZone timeZone = ((IDatePropertyDescriptor) propertyDescriptor).isTimeZoneAware() ?
          actionHandler.getClientTimeZone() : actionHandler.getReferenceTimeZone();
      return createDateFormatter((IDatePropertyDescriptor) propertyDescriptor,
          timeZone, actionHandler, locale);
    }
    if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      return createTimeFormatter((ITimePropertyDescriptor) propertyDescriptor,
          actionHandler, locale);
    }
    if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      return createDurationFormatter(
          (IDurationPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return createPercentFormatter(
          (IPercentPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      return createDecimalFormatter(
          (IDecimalPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      return createIntegerFormatter(
          (IIntegerPropertyDescriptor) propertyDescriptor, actionHandler, locale);
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
   * Creates a image property view.
   *
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created image view.
   */
  protected abstract IView<E> createImagePropertyView(
      IPropertyViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates an integer format based on an integer property descriptor.
   *
   * @param propertyDescriptor           the integer property descriptor
   * @param translationProvider the translation provider
   * @param locale           the locale
   * @return the integer format
   */
  protected NumberFormat createIntegerFormat(
      IIntegerPropertyDescriptor propertyDescriptor, ITranslationProvider translationProvider, Locale locale) {
    DecimalFormat format = (DecimalFormat) NumberFormat.getIntegerInstance(locale);
    applyDecimalFormatSymbols(format, propertyDescriptor, translationProvider, locale);
    format.setGroupingUsed(propertyDescriptor.isThousandsGroupingUsed());
    return format;
  }

  /**
   * Creates an integer formatter based on an integer property descriptor.
   *
   * @param propertyDescriptor           the integer property descriptor
   * @param translationProvider the translation provider
   * @param locale           the locale
   * @return the integer formatter
   */
  protected IFormatter<Object, String> createIntegerFormatter(
      IIntegerPropertyDescriptor propertyDescriptor, ITranslationProvider translationProvider, Locale locale) {
    return new FormatAdapter(createIntegerFormat(propertyDescriptor, translationProvider, locale));
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
   * @param propertyView
   *          the view these actions will be triggered from.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the generic list of value action.
   */
  protected G createLovAction(IView<E> propertyView,
      IActionHandler actionHandler, Locale locale) {
    IPropertyViewDescriptor propertyViewDescriptor = (IPropertyViewDescriptor) propertyView
        .getDescriptor();
    IDisplayableAction listOfValueAction;
    if (propertyViewDescriptor instanceof IReferencePropertyViewDescriptor
        && ((IReferencePropertyViewDescriptor) propertyViewDescriptor)
            .getLovAction() != null) {
      listOfValueAction = ((IReferencePropertyViewDescriptor) propertyViewDescriptor)
          .getLovAction();
    } else {
      listOfValueAction = getLovAction();
    }
    Collection<IGate> aGates = listOfValueAction.getActionabilityGates();
    if (aGates != null) {
      aGates.remove(ModelTrackingGate.INSTANCE);
    }
    G action = getActionFactory().createAction(listOfValueAction,
        actionHandler, propertyView, locale);
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
      view = createIntegerPropertyView(propertyViewDescriptor, actionHandler, locale);
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
   * @param propertyDescriptor           the percent property descriptor
   * @param translationProvider the translation provider
   * @param locale           the locale
   * @return the percent format
   */
  protected NumberFormat createPercentFormat(
      IPercentPropertyDescriptor propertyDescriptor, ITranslationProvider translationProvider, Locale locale) {
    DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance(locale);
    applyDecimalFormatSymbols(format, propertyDescriptor, translationProvider, locale);
    format.setMaximumFractionDigits(propertyDescriptor.getMaxFractionDigit());
    if (propertyDescriptor.isUsingBigDecimal()) {
      format.setParseBigDecimal(true);
    }
    format.setMinimumFractionDigits(format.getMaximumFractionDigits());
    format.setGroupingUsed(propertyDescriptor.isThousandsGroupingUsed());
    return format;
  }

  /**
   * Creates a percent formatter based on a percent property descriptor.
   *
   * @param propertyDescriptor           the percent property descriptor
   * @param translationProvider the translation provider
   * @param locale           the locale
   * @return the percent formatter
   */
  protected IFormatter<Object, String> createPercentFormatter(
      IPercentPropertyDescriptor propertyDescriptor, ITranslationProvider translationProvider, Locale locale) {
    return new FormatAdapter(createPercentFormat(propertyDescriptor, translationProvider, locale));
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
    if (propertyViewDescriptor instanceof IImageViewDescriptor) {
      // First of all, test for Image property view before deciding based on the
      // model.
      view = createImagePropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyViewDescriptor instanceof IHtmlViewDescriptor) {
      // Then, test for Html property view before deciding based on the
      // model.
      view = createHtmlPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
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
    } else if (propertyDescriptor instanceof IImageBinaryPropertyDescriptor) {
      view = createImagePropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IBinaryPropertyDescriptor) {
      view = createBinaryPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IColorPropertyDescriptor) {
      view = createColorPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    if (view != null && propertyViewDescriptor.getAction() != null
        && !propertyViewDescriptor.isReadOnly()) {
      // We must listen for incoming connector value change to trigger the
      // action.
      final IValueConnector viewConnector = view.getConnector();
      if (viewConnector != null) {
        viewConnector.addValueChangeListener(new ConnectorActionAdapter<>(
            propertyViewDescriptor.getAction(), getActionFactory(),
            actionHandler, view));
      }
    }
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
    }
    if (propertyDescriptor instanceof ISourceCodePropertyDescriptor) {
      return createSourceCodePropertyView(propertyViewDescriptor,
          actionHandler, locale);
    }
    if (propertyDescriptor instanceof IHtmlPropertyDescriptor) {
      return createHtmlPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    if (propertyDescriptor instanceof ITextPropertyDescriptor) {
      return createTextPropertyView(propertyViewDescriptor, actionHandler,
          locale);
    }
    if (propertyDescriptor instanceof IImageUrlPropertyDescriptor) {
      return createImagePropertyView(propertyViewDescriptor, actionHandler,
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
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the time format.
   */
  protected SimpleDateFormat createTimeFormat(ITimePropertyDescriptor propertyDescriptor,
                                              ITranslationProvider translationProvider, Locale locale) {
    String formatPattern;
    if (propertyDescriptor.getFormatPattern() != null) {
      formatPattern = propertyDescriptor.getFormatPattern();
    } else {
      formatPattern = getTimePattern(propertyDescriptor, translationProvider, locale);
    }
    SimpleDateFormat format = new NullableSimpleDateFormat(formatPattern, locale);
    return format;
  }

  /**
   * Creates a time formatter based on an time property descriptor.
   *
   * @param propertyDescriptor
   *          the time property descriptor.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the time formatter.
   */
  protected IFormatter<?, String> createTimeFormatter(
      ITimePropertyDescriptor propertyDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    return createFormatter(createTimeFormat(propertyDescriptor, translationProvider, locale));
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
              rootDescriptor
                  .getNodeGroupDescriptor().getRenderedProperty());
      List<ICollectionConnectorProvider> subtreeConnectors = new ArrayList<>();
      if (((ICompositeTreeLevelDescriptor) rootDescriptor)
          .getChildrenDescriptors() != null) {
        for (ITreeLevelDescriptor subtreeViewDescriptor : ((ICompositeTreeLevelDescriptor) rootDescriptor)
            .getChildrenDescriptors()) {
          if (actionHandler.isAccessGranted(subtreeViewDescriptor)) {
            try {
              actionHandler.pushToSecurityContext(subtreeViewDescriptor);
              ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
                  viewDescriptor, actionHandler, locale, subtreeViewDescriptor,
                  1);
              compositeConnector.addChildConnector(subtreeConnector.getId(),
                  subtreeConnector);
              subtreeConnectors.add(subtreeConnector);
            } finally {
              actionHandler.restoreLastSecurityContextSnapshot();
            }
          }
        }
      }
      compositeConnector.setCollectionConnectorProviders(subtreeConnectors);
      connector = compositeConnector;
    } else if (rootDescriptor instanceof ISimpleTreeLevelDescriptor) {
      IConfigurableCollectionConnectorProvider simpleConnector = connectorFactory
          .createConfigurableCollectionConnectorProvider(
              ModelRefPropertyConnector.THIS_PROPERTY,
              rootDescriptor
                  .getNodeGroupDescriptor().getRenderedProperty());
      ITreeLevelDescriptor childDescriptor = ((ISimpleTreeLevelDescriptor) rootDescriptor)
          .getChildDescriptor();
      if (childDescriptor != null) {
        if (actionHandler.isAccessGranted(childDescriptor)) {
          try {
            actionHandler.pushToSecurityContext(childDescriptor);
            ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
                viewDescriptor, actionHandler, locale, childDescriptor, 1);
            simpleConnector.addChildConnector(subtreeConnector.getId(),
                subtreeConnector);
            simpleConnector.setCollectionConnectorProvider(subtreeConnector);
          } finally {
            actionHandler.restoreLastSecurityContextSnapshot();
          }
        }
      }
      connector = simpleConnector;
    }

    if (connector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) connector)
          .setDisplayValue(viewDescriptor.getI18nName(actionHandler, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayDescription(viewDescriptor.getI18nDescription(
              actionHandler, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayIcon(viewDescriptor.getIcon());
      ((AbstractCompositeValueConnector) connector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }

    //noinspection ConstantConditions
    if (connector instanceof ICollectionConnectorListProvider) {
      ((ICollectionConnectorListProvider) connector)
          .setTracksChildrenSelection(true);
    }

    if (connector instanceof IItemSelectable
        && connector instanceof ICollectionConnectorProvider) {
      final IItemSelectable rootConnector = ((IItemSelectable) connector);
      rootConnector.addItemSelectionListener(new IItemSelectionListener() {

        @Override
        public void selectedItemChange(ItemSelectionEvent event) {
          if (event.getSelectedItem() == null) {
            // change selected item so that the root connector is considered
            // selected.
            event.setSelectedItem(rootConnector);
          }
        }
      });
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
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale to be used for a titled border.
   */
  protected abstract void decorateWithBorder(IView<E> view,
      ITranslationProvider translationProvider, Locale locale);

  /**
   * Applies the font and color configuration to a view.
   *
   * @param viewDescriptor
   *          the view descriptor.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @param view
   *          the raw view.
   */
  protected abstract void finishComponentConfiguration(
      IViewDescriptor viewDescriptor, ITranslationProvider translationProvider,
      Locale locale, IView<E> view);

  /**
   * Computes the connector id for component view.
   *
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the computed connector id.
   * @deprecated use more generic getConnectorIdForBeanView instead.
   */
  @Deprecated
  protected String getConnectorIdForComponentView(
      IComponentViewDescriptor viewDescriptor) {
    return getConnectorIdForBeanView(viewDescriptor);
  }

  /**
   * Computes the connector id for bean based view.
   *
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the computed connector id.
   */
  protected String getConnectorIdForBeanView(IViewDescriptor viewDescriptor) {
    if (viewDescriptor.getModelDescriptor() == null
        || viewDescriptor.getModelDescriptor() instanceof IComponentDescriptor<?>) {
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
    int maxFractionDigit = propertyDescriptor.getMaxFractionDigit();
    double decimalPart = 0;
    for (int i = 1; i <= maxFractionDigit; i++) {
      decimalPart += Math.pow(10.0D, -i);
    }
    templateValue += decimalPart;
    return templateValue;
  }

  /**
   * Gets the defaultActionMapRenderingOptions.
   *
   * @return the defaultActionMapRenderingOptions.
   */
  protected ERenderingOptions getDefaultActionMapRenderingOptions() {
    return defaultActionMapRenderingOptions;
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
   * @param translationProvider
   *          the translation provider to use to translate the enumeration
   *          values.
   * @param locale
   *          the locale.
   * @return the enumeration template value.
   */
  protected String getEnumerationTemplateValue(
      IEnumerationPropertyDescriptor propertyDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    int maxTranslationLength = -1;
    if (translationProvider != null && propertyDescriptor.isTranslated()) {
      for (String enumerationValue : propertyDescriptor.getEnumerationValues()) {
        String translation = propertyDescriptor.getI18nValue(enumerationValue,
            translationProvider, locale);
        if (translation.length() > maxTranslationLength) {
          maxTranslationLength = translation.length();
        }
      }
    } else {
      maxTranslationLength = propertyDescriptor.getMaxLength();
    }
    if (maxTranslationLength == -1
        || maxTranslationLength > getMaxCharacterLength()) {
      maxTranslationLength = getMaxCharacterLength();
    }
    return getStringTemplateValue(maxTranslationLength);
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
  @SuppressWarnings("unchecked")
  protected int getFormatLength(IFormatter<?, String> formatter,
      Object templateValue) {
    int formatLength;
    if (formatter != null) {
      formatLength = ((IFormatter<Object, String>) formatter).format(
          templateValue).length();
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
    return (int) templateValue;
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
   * Gets components lov action template.
   *
   * @return the components lov action template
   */
  public IDisplayableAction getComponentsLovActionTemplate() {
    return componentsLovActionTemplate;
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
   * Gets the modelConnectorFactory.
   *
   * @return the modelConnectorFactory.
   */
  protected IModelConnectorFactory getModelConnectorFactory() {
    return modelConnectorFactory;
  }

  /**
   * Gets the mvcBinder.
   *
   * @return the mvcBinder.
   */
  public IMvcBinder getMvcBinder() {
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
    int maxFractionDigit = propertyDescriptor.getMaxFractionDigit();
    double decimalPart = 0;
    for (int i = 1; i <= maxFractionDigit; i++) {
      decimalPart += Math.pow(10.0D, -i);
    }
    templateValue += decimalPart;
    return templateValue / 100.0D;
  }

  /**
   * Gets an string template value.
   *
   * @param maxLength
   *          the attribute max length.
   * @return the string template value.
   */
  protected String getStringTemplateValue(Integer maxLength) {
    StringBuilder templateValue = new StringBuilder();
    int fieldLength = getMaxCharacterLength();
    if (maxLength != null) {
      fieldLength = maxLength;
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
    }
    if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      return getTimeTemplateValue((ITimePropertyDescriptor) propertyDescriptor);
    }
    if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      return getDurationTemplateValue((IDurationPropertyDescriptor) propertyDescriptor);
    }
    if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      return getStringTemplateValue((IStringPropertyDescriptor) propertyDescriptor);
    }
    if (propertyDescriptor instanceof IPercentPropertyDescriptor) {
      return getPercentTemplateValue((IPercentPropertyDescriptor) propertyDescriptor);
    }
    if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      return getDecimalTemplateValue((IDecimalPropertyDescriptor) propertyDescriptor);
    }
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      return getIntegerTemplateValue((IIntegerPropertyDescriptor) propertyDescriptor);
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
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
   * Gets whether a property view is considered to fill all the available height
   * space.
   *
   * @param propertyViewDescriptor
   *          the property view descriptor.
   * @return true if a property view is considered to fill all the available
   *         height space.
   */
  protected boolean isHeightExtensible(
      IPropertyViewDescriptor propertyViewDescriptor) {
    if (propertyViewDescriptor.getPreferredSize() == null
        || propertyViewDescriptor.getPreferredSize().getHeight() <= 0) {
      IModelDescriptor propertyDescriptor = propertyViewDescriptor
          .getModelDescriptor();
      if (propertyDescriptor instanceof ITextPropertyDescriptor
          || propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
        return true;
      }
    }
    return false;
  }

  /**
   * Shows a card in in card laid out panel.
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
    if (renderedChildProperties == null) {
      renderedChildProperties = propertyViewDescriptor
          .getDefaultRenderedChildProperties();
    }
    if (renderedChildProperties != null && renderedChildProperties.size() > 1) {
      BasicTableViewDescriptor viewDescriptor = new BasicTableViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      List<IPropertyViewDescriptor> columnViewDescriptors = new ArrayList<>();
      for (String renderedProperty : renderedChildProperties) {
        BasicPropertyViewDescriptor columnDescriptor = new BasicPropertyViewDescriptor();
        columnDescriptor.setName(renderedProperty);
        columnViewDescriptors.add(columnDescriptor);
      }
      viewDescriptor.setColumnViewDescriptors(columnViewDescriptors);
      viewDescriptor.setName(propertyDescriptor.getName());
      viewDescriptor
          .setPreferredSize(propertyViewDescriptor.getPreferredSize());
      view = createTableView(viewDescriptor, actionHandler, locale);
    } else {
      BasicListViewDescriptor viewDescriptor = new BasicListViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      if (renderedChildProperties != null
          && renderedChildProperties.size() == 1) {
        viewDescriptor.setRenderedProperty(renderedChildProperties.get(0));
      }
      viewDescriptor.setName(propertyDescriptor.getName());
      viewDescriptor
          .setPreferredSize(propertyViewDescriptor.getPreferredSize());
      view = createListView(viewDescriptor, actionHandler, locale);
    }
    if (view instanceof BasicView) {
      ((BasicView<E>) view).setDescriptor(propertyViewDescriptor);
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
  protected IView<E> createCollectionView(
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

  private void bindSelectionConnector(
      final ICollectionConnectorProvider viewConnector,
      final ICollectionConnector viewSelectionConnector) {
    viewSelectionConnector.addValueChangeListener(new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        viewConnector.setSelectedIndices(ConnectorHelper.getIndicesOf(
            viewConnector.getCollectionConnector(),
            (Collection<?>) evt.getNewValue()));
      }
    });
    viewConnector.addValueChangeListener(new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        viewConnector.setSelectedIndices(ConnectorHelper.getIndicesOf(
            viewConnector.getCollectionConnector(),
            (Collection<?>) viewSelectionConnector.getConnectorValue()));
      }
    });
    viewSelectionConnector.addPropertyChangeListener(
        IValueConnector.MODEL_CONNECTOR_PROPERTY, new PropertyChangeListener() {

          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            IValueConnector oldModelConnector = (IValueConnector) evt
                .getOldValue();
            if (oldModelConnector != null) {
              oldModelConnector.setConnectorValue(null);
            }
          }
        });
    viewConnector.addSelectionChangeListener(new ISelectionChangeListener() {

      @Override
      public void selectionChange(SelectionChangeEvent evt) {
        int[] selectedIndices = evt.getNewSelection();
        Collection<?> elements = viewConnector
            .getConnectorValue();
        Collection<Object> selectedElements = null;
        if (selectedIndices != null && selectedIndices.length > 0
            && elements != null && elements.size() > 0) {
          selectedElements = getComponentCollectionFactory()
              .createComponentCollection(
                  ((ICollectionDescriptorProvider<?>) viewSelectionConnector
                      .getModelDescriptor()).getCollectionDescriptor()
                      .getCollectionInterface());
          List<?> elementsList = new ArrayList<Object>(elements);
          for (int iselectedIndex : selectedIndices) {
            if (iselectedIndex >= 0 && iselectedIndex < elementsList.size()) {
              selectedElements.add(elementsList.get(iselectedIndex));
            }
          }
        }
        viewSelectionConnector.setConnectorValue(selectedElements);
      }
    });
  }

  /**
   * Binds the item selection action and decorates with the pagination view.
   *
   * @param view
   *          the collection view to configure.
   * @param viewDescriptor
   *          the collection view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   */
  public void finishCollectionViewConfiguration(IView<E> view, ICollectionViewDescriptor viewDescriptor,
                                                IActionHandler actionHandler, Locale locale) {
    if (view != null) {
      if (viewDescriptor.getSelectionModelDescriptor() != null) {
        ICollectionDescriptorProvider<?> selectionModelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
            .getSelectionModelDescriptor());
        IComponentDescriptor<?> rowDescriptor = selectionModelDescriptor
            .getCollectionDescriptor().getElementDescriptor();
        ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
            .createCompositeValueConnector(
                selectionModelDescriptor.getName() + "Element",
                rowDescriptor.getToHtmlProperty());
        ICollectionConnector viewSelectionConnector = getConnectorFactory()
            .createCollectionConnector(selectionModelDescriptor.getName(),
                getMvcBinder(), rowConnectorPrototype);
        ICollectionConnectorProvider viewConnector = (ICollectionConnectorProvider) view
            .getConnector();
        bindSelectionConnector(viewConnector, viewSelectionConnector);
        IConfigurableCollectionConnectorProvider wrapperConnector = getConnectorFactory()
            .createConfigurableCollectionConnectorProvider(
                ModelRefPropertyConnector.THIS_PROPERTY, null);
        wrapperConnector.addChildConnector(viewSelectionConnector);
        wrapperConnector.addChildConnector(viewConnector);
        wrapperConnector.setCollectionConnectorProvider(viewConnector);
        view.setConnector(wrapperConnector);
      }
      if (viewDescriptor.getItemSelectionAction() != null) {
        ((IItemSelectable) view.getConnector())
            .addItemSelectionListener(new ConnectorActionAdapter<>(
                viewDescriptor.getItemSelectionAction(), getActionFactory(),
                actionHandler, view));
      }
      if (viewDescriptor.getPaginationViewDescriptor() != null) {
        IView<E> paginationView = createPaginationView(
            viewDescriptor.getPaginationViewDescriptor(), view, actionHandler,
            locale);
        paginationView.setParent(view);
        view.setPeer(decorateWithPaginationView(view.getPeer(),
            paginationView.getPeer()));
        ((ICollectionConnectorProvider) view.getConnector()).getCollectionConnector().addSelectionChangeListener(
            new ISelectionChangeListener() {
              @Override
              public void selectionChange(SelectionChangeEvent evt) {
                IValueConnector modelConnector = ((ICollectionConnector) evt.getSource()).getModelConnector();
                if (modelConnector != null) { // Fixes bug #1181
                  IPageable pageable = modelConnector.getModelProvider().getModel();
                  if (pageable != null) {
                    int[] selectedIndices = evt.getNewSelection();
                    pageable.setSelectedRecordCount(selectedIndices == null ? 0 : selectedIndices.length);
                  }
                }
              }
            });
      }
      if (viewDescriptor.isAutoSelectFirstRow()) {
        attachDefaultCollectionListener(((ICollectionConnectorProvider) view
            .getConnector()).getCollectionConnector());
      }
    }
  }

  /**
   * Gets enumeration icon dimension.
   *
   * @param propertyViewDescriptor the property view descriptor
   * @return the enumeration icon dimension
   */
  protected Dimension getEnumerationIconDimension(IPropertyViewDescriptor propertyViewDescriptor) {
    Dimension iconSize = getIconFactory().getTinyIconSize();
    if (propertyViewDescriptor instanceof IEnumerationPropertyViewDescriptor
        && ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor).getEnumIconDimension() != null) {
      iconSize = ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor).getEnumIconDimension();
    }
    return iconSize;
  }

  /**
   * Decorates a view with a pagination view.
   *
   * @param viewPeer
   *          the collection view to decorate.
   * @param paginationViewPeer
   *          the pagination view to use.
   * @return the assembled view decorated with pagination view.
   */
  protected abstract E decorateWithPaginationView(E viewPeer,
      E paginationViewPeer);

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
    List<ICollectionConnectorProvider> subtreeConnectors = new ArrayList<>();
    if (subtreeViewDescriptor.getChildrenDescriptors() != null
        && depth < viewDescriptor.getMaxDepth()) {
      for (ITreeLevelDescriptor childDescriptor : subtreeViewDescriptor
          .getChildrenDescriptors()) {
        if (actionHandler.isAccessGranted(childDescriptor)) {
          try {
            actionHandler.pushToSecurityContext(childDescriptor);
            ICollectionConnectorProvider childConnector = createNodeGroupConnector(
                viewDescriptor, actionHandler, locale, childDescriptor,
                depth + 1);
            nodeGroupPrototypeConnector.addChildConnector(
                childConnector.getId(), childConnector);
            subtreeConnectors.add(childConnector);
          } finally {
            actionHandler.restoreLastSecurityContextSnapshot();
          }
        }
      }
    }
    nodeGroupPrototypeConnector
        .setCollectionConnectorProviders(subtreeConnectors);
    if (nodeGroupPrototypeConnector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(actionHandler, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(actionHandler, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayIcon(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getIcon());
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }

    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
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
   *          the locale the view must use for i18n.
   * @return the empty view.
   */
  private IView<E> createEmptyView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IValueConnector connector = getConnectorFactory().createValueConnector(
        ModelRefPropertyConnector.THIS_PROPERTY);
    E viewComponent = createEmptyComponent();
    return constructView(viewComponent, viewDescriptor, connector);
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
              .getI18nName(actionHandler, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(actionHandler, locale));
      ((AbstractCompositeValueConnector) connector)
          .setDisplayIcon(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getIcon());
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
        .createConfigurableCollectionConnectorProvider(
            nodeGroupModelDescriptor.getName() + "Element",
            subtreeViewDescriptor.getNodeGroupDescriptor()
                .getRenderedProperty());
    ITreeLevelDescriptor childDescriptor = subtreeViewDescriptor
        .getChildDescriptor();
    if (childDescriptor != null && depth < viewDescriptor.getMaxDepth()
        && actionHandler.isAccessGranted(childDescriptor)) {
      try {
        actionHandler.pushToSecurityContext(childDescriptor);
        ICollectionConnectorProvider childConnector = createNodeGroupConnector(
            viewDescriptor, actionHandler, locale, childDescriptor, depth + 1);
        nodeGroupPrototypeConnector.addChildConnector(childConnector.getId(),
            childConnector);
        nodeGroupPrototypeConnector
            .setCollectionConnectorProvider(childConnector);
      } finally {
        actionHandler.restoreLastSecurityContextSnapshot();
      }
    }
    if (nodeGroupPrototypeConnector instanceof AbstractCompositeValueConnector) {
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayValue(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nName(actionHandler, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayDescription(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getI18nDescription(actionHandler, locale));
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setDisplayIcon(subtreeViewDescriptor.getNodeGroupDescriptor()
              .getIcon());
      ((AbstractCompositeValueConnector) nodeGroupPrototypeConnector)
          .setIconImageURLProvider(viewDescriptor.getIconImageURLProvider());
    }
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(), mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  /**
   * Is live debug UI structure.
   *
   * @return the boolean
   */
  protected boolean isLiveDebugUI() {
    return liveDebugUI;
  }

  /**
   * Sets live debug UI structure.
   *
   * @param liveDebugUI the live debug uI structure
   */
  public void setLiveDebugUI(boolean liveDebugUI) {
    this.liveDebugUI = liveDebugUI;
  }

  /**
   * Complete description with live debug uI.
   *
   * @param viewDescriptor the view descriptor
   * @param viewDescription the view description
   * @return the completed view description.
   */
  protected String completeDescriptionWithLiveDebugUI(IViewDescriptor viewDescriptor, String viewDescription) {
    if (isLiveDebugUI()) {
      if (viewDescriptor.getPermId() != null) {
        if (viewDescription == null) {
          viewDescription = "";
        } else {
          viewDescription = viewDescription + " ";
        }
        viewDescription = viewDescription + "(Descriptor PermId -> [" + viewDescriptor.getPermId() + "])";
      }
    }
    return viewDescription;
  }

  /**
   * Connector action adapter.
   *
     * @author Vincent Vandenschrick
   * @param <E>
   *          the actual component type.
   * @param <F>
   *          the actual action type.
   */
  protected static class ConnectorActionAdapter<E, F> implements
      IItemSelectionListener, IValueChangeListener, ICloneable {

    private final IAction              actionDelegate;
    private final IActionFactory<F, E> actionFactory;
    private final IActionHandler       actionHandler;
    private final IView<E>             view;

    /**
     * Constructs a new {@code ConnectorActionAdapter} instance.
     *
     * @param actionDelegate
     *          the action to trigger when the connector value/selection
     *          changes.
     * @param actionFactory
     *          the action factory.
     * @param actionHandler
     *          the action handler.
     * @param view
     *          the view to use in the context.
     */
    public ConnectorActionAdapter(IAction actionDelegate,
        IActionFactory<F, E> actionFactory, IActionHandler actionHandler,
        IView<E> view) {
      this.actionDelegate = actionDelegate;
      this.actionFactory = actionFactory;
      this.actionHandler = actionHandler;
      this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public ConnectorActionAdapter<E, F> clone() {
      try {
        return (ConnectorActionAdapter<E, F>) super.clone();
      } catch (CloneNotSupportedException ex) {
        // Cannot happen.
        return null;
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectedItemChange(ItemSelectionEvent event) {
      Object actionParam = event.getSelectedItem();
      triggerAction(actionParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(ValueChangeEvent evt) {
      IValueConnector viewConnector = (IValueConnector) evt.getSource();
      IValueConnector modelConnector = viewConnector.getModelConnector();
      if (modelConnector != null
          && !ObjectUtils.equals(evt.getNewValue(),
              modelConnector.getConnectorValue())) {
        if (modelConnector.getConnectorValue() instanceof Map<?, ?>
            && modelConnector.getModelDescriptor() instanceof IReferencePropertyDescriptor<?>
            && viewConnector instanceof IRenderableCompositeValueConnector
            && ((IRenderableCompositeValueConnector) viewConnector)
                .getRenderingConnector() != null) {
          modelConnector = ((IRenderableCompositeValueConnector) viewConnector)
              .getRenderingConnector().getModelConnector();
        }
        // this is not a model notification, so arm to trigger the action once
        // the model is actually updated.
        modelConnector.addValueChangeListener(new IValueChangeListener() {

          @Override
          public void valueChange(ValueChangeEvent modelEvt) {
            // This is a 1 shot event.
            ((IValueChangeSource) modelEvt.getSource())
                .removeValueChangeListener(this);
            triggerAction(modelEvt.getOldValue());
          }
        });
      }

    }

    private void triggerAction(Object actionParam) {
      Map<String, Object> context = actionFactory.createActionContext(
          actionHandler, view, view.getConnector(), null, view.getPeer());
      context.put(ActionContextConstants.ACTION_PARAM, actionParam);
      actionHandler.execute(actionDelegate, context);
    }
  }

  /**
   * Stores user table preferences.
   *
   * @param tableId
   *          the table id used as preference key in the user store.
   * @param columnPrefs
   *          the array of {columnId,columnSize} for the table
   * @param actionHandler
   *          the action handler.
   */
  @Override
  public void storeTablePreferences(String tableId, Object[][] columnPrefs,
      IActionHandler actionHandler) {
    StringBuilder buff = new StringBuilder();
    for (int i = 0; i < columnPrefs.length; i++) {
      if (i > 0) {
        buff.append("!");
      }
      buff.append(columnPrefs[i][0]).append(",").append(columnPrefs[i][1]);
    }
    actionHandler.putUserPreference(tableId, buff.toString());
  }

  /**
   * Reworks column view descriptors to align with user preferences.
   *
   * @param viewDescriptor
   *          the table view descriptor.
   * @param actionHandler
   *          the action handler to load the user preferences from.
   * @return an ordered map of column view descriptors and widths.
   */
  protected Map<IPropertyViewDescriptor, Integer> getUserColumnViewDescriptors(
      ITableViewDescriptor viewDescriptor, IActionHandler actionHandler) {

    Object[][] columnPrefs = null;
    if (viewDescriptor.getPermId() != null) {
      String prefs = actionHandler
          .getUserPreference(viewDescriptor.getPermId());
      if (prefs != null) {
        String[] columns = prefs.split("!");
        columnPrefs = new Object[columns.length][2];
        for (int i = 0; i < columns.length; i++) {
          String[] column = columns[i].split(",");
          columnPrefs[i] = new Object[] {
              column[0], Integer.valueOf(column[1])
          };
        }
      }
    }
    Map<IPropertyViewDescriptor, Integer> userColumnViewDescriptors = new LinkedHashMap<>();
    if (columnPrefs == null) {
      for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
          .getColumnViewDescriptors()) {
        userColumnViewDescriptors.put(columnViewDescriptor, null);
      }
    } else {
      Map<String, IPropertyViewDescriptor> columnsDirectory = new LinkedHashMap<>();
      for (IPropertyViewDescriptor columnViewDescriptor : viewDescriptor
          .getColumnViewDescriptors()) {
        columnsDirectory.put(
            computeColumnIdentifier(viewDescriptor, columnViewDescriptor),
            columnViewDescriptor);
      }
      for (Object[] columnPref : columnPrefs) {
        //noinspection SuspiciousMethodCalls
        IPropertyViewDescriptor userColumn = columnsDirectory
            .remove(columnPref[0]);
        if (userColumn != null) {
          userColumnViewDescriptors
              .put(userColumn, (Integer) columnPref[1]);
        }
      }
      // Add remaining new columns
      for (IPropertyViewDescriptor extraColumn : columnsDirectory.values()) {
        userColumnViewDescriptors.put(extraColumn, null);
      }
    }
    return userColumnViewDescriptors;
  }

  /**
   * Store tab selection preference.
   *
   * @param tabViewDescriptor
   *     the tab view descriptor
   * @param selectedTabIndex
   *     the selected tab index
   * @param actionHandler
   *     the action handler
   */
  protected void storeTabSelectionPreference(ITabViewDescriptor tabViewDescriptor, int selectedTabIndex, IActionHandler actionHandler) {
    if (tabViewDescriptor.getPermId() != null) {
      actionHandler.putUserPreference(tabViewDescriptor.getPermId(), Integer.toString(selectedTabIndex));
    }
  }

  /**
   * Gets tab selection preference.
   *
   * @param tabViewDescriptor
   *     the tab view descriptor
   * @param actionHandler
   *     the action handler
   * @return the tab selection preference
   */
  protected int getTabSelectionPreference(ITabViewDescriptor tabViewDescriptor, IActionHandler actionHandler) {
    if (tabViewDescriptor.getPermId() != null) {
      String tabSelectionPreference = actionHandler.getUserPreference(tabViewDescriptor.getPermId());
      if (tabSelectionPreference != null) {
        return Integer.parseInt(tabSelectionPreference);
      }
    }
    return 0;
  }

  /**
   * Filters enumeration values if refined.
   *
   * @param enumerationValues
   *          the original collection to filter.
   * @param propertyViewDescriptor
   *          the property view descriptor for the enumeration.
   */
  protected void filterEnumerationValues(List<String> enumerationValues,
      IPropertyViewDescriptor propertyViewDescriptor) {
    if (propertyViewDescriptor instanceof IEnumerationPropertyViewDescriptor) {
      Set<String> allowedValues = ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor)
          .getAllowedValues();
      Set<String> forbiddenValues = ((IEnumerationPropertyViewDescriptor) propertyViewDescriptor)
          .getForbiddenValues();
      if (allowedValues != null && !allowedValues.isEmpty()) {
        enumerationValues.retainAll(allowedValues);
      }
      if (forbiddenValues != null && !forbiddenValues.isEmpty()) {
        enumerationValues.removeAll(forbiddenValues);
      }
    }
  }

  /**
   * Configures the color to be used to highlight mandatory labels. It defaults
   * to red, i.e. {@code 0xFFFF0000}.
   *
   * @param mandatoryPropertyColorHex
   *          the mandatoryPropertyColorHex to set.
   */
  public void setMandatoryPropertyColorHex(String mandatoryPropertyColorHex) {
    this.formLabelMandatoryPropertyColorHex = mandatoryPropertyColorHex;
    this.tableHeaderMandatoryPropertyColorHex = mandatoryPropertyColorHex;
  }

  /**
   * Decorates mandatory property label. Default is to append a &quot;*&quot;.
   *
   * @param labelText
   *          the original label text.
   * @return the decorated label text.
   */
  protected String decorateMandatoryPropertyLabel(String labelText) {
    String decoratedLabelText;
    if (labelText != null) {
      decoratedLabelText = labelText + "*";
    } else {
      decoratedLabelText = "*";
    }
    return decoratedLabelText;
  }

  /**
   * Gets the formLabelMandatoryPropertyColorHex.
   *
   * @return the formLabelMandatoryPropertyColorHex.
   */
  public String getFormLabelMandatoryPropertyColorHex() {
    return formLabelMandatoryPropertyColorHex;
  }

  /**
   * Sets the formLabelMandatoryPropertyColorHex.
   *
   * @param formLabelMandatoryPropertyColorHex
   *          the formLabelMandatoryPropertyColorHex to set.
   */
  public void setFormLabelMandatoryPropertyColorHex(
      String formLabelMandatoryPropertyColorHex) {
    this.formLabelMandatoryPropertyColorHex = formLabelMandatoryPropertyColorHex;
  }

  /**
   * Gets the tableHeaderMandatoryPropertyColorHex.
   *
   * @return the tableHeaderMandatoryPropertyColorHex.
   */
  public String getTableHeaderMandatoryPropertyColorHex() {
    return tableHeaderMandatoryPropertyColorHex;
  }

  /**
   * Sets the tableHeaderMandatoryPropertyColorHex.
   *
   * @param tableHeaderMandatoryPropertyColorHex
   *          the tableHeaderMandatoryPropertyColorHex to set.
   */
  public void setTableHeaderMandatoryPropertyColorHex(
      String tableHeaderMandatoryPropertyColorHex) {
    this.tableHeaderMandatoryPropertyColorHex = tableHeaderMandatoryPropertyColorHex;
  }

  /**
   * Gets the componentCollectionFactory.
   *
   * @return the componentCollectionFactory.
   */
  public IComponentCollectionFactory getComponentCollectionFactory() {
    return componentCollectionFactory;
  }

  /**
   * Sets the componentCollectionFactory.
   *
   * @param componentCollectionFactory
   *          the componentCollectionFactory to set.
   */
  public void setComponentCollectionFactory(
      IComponentCollectionFactory componentCollectionFactory) {
    this.componentCollectionFactory = componentCollectionFactory;
  }

  /**
   * Is allowed for client type.
   *
   * @param clientTypeAwareView the property view
   * @param actionHandler the action handler
   * @return the boolean
   */
  protected boolean isAllowedForClientType(IClientTypeAware clientTypeAwareView, IActionHandler actionHandler) {
    List<String> forClientTypes = clientTypeAwareView.getForClientTypes();
    if (forClientTypes == null) {
      return true;
    }
    String clientType = actionHandler.getClientType().name();
    for (String allowedClientType : forClientTypes) {
      if (clientType.matches(".*" + allowedClientType + ".*")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Trigger tab selection action.
   *
   * @param selectedIndex
   *     the selected index
   * @param tabComponent
   *     the tab component
   * @param tabViewDescriptor
   *     the tab view descriptor
   * @param tabView
   *     the tab view
   * @param actionHandler
   *     the action handler
   */
  protected void triggerTabSelectionAction(int selectedIndex, E tabComponent,
                                           ITabViewDescriptor tabViewDescriptor, BasicIndexedView<E> tabView,
                                           IActionHandler actionHandler) {
    IAction tabSelectionAction = tabViewDescriptor.getTabSelectionAction();
    if (tabSelectionAction != null) {
      Map<String, Object> actionContext = getActionFactory().createActionContext(actionHandler, tabView,
          tabView.getConnector(), Integer.toString(selectedIndex), tabComponent);
      actionContext.put(ActionContextConstants.ACTION_PARAM, selectedIndex);
      actionHandler.execute(tabSelectionAction, actionContext);
    }
  }

}
