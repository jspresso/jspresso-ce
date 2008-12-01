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
package org.jspresso.framework.view;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IConnectorSelector;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.masterdetail.IModelCascadingBinder;
import org.jspresso.framework.binding.model.IModelValueConnector;
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
import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;
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
import org.jspresso.framework.util.format.DurationFormatter;
import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.format.NullableSimpleDateFormat;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;
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
import org.jspresso.framework.view.descriptor.INestingViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ISubViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicSubviewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * Abstract base class factory for views.
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
  protected static final int            DEF_DISP_MAX_FRACTION_DIGIT = 2;
  /**
   * <code>DEF_DISP_MAX_VALUE</code>.
   */
  protected static final double         DEF_DISP_MAX_VALUE          = 1000;
  /**
   * <code>DEF_DISP_TEMPLATE_PERCENT</code>.
   */
  protected static final double         DEF_DISP_TEMPLATE_PERCENT   = 99;
  /**
   * <code>TEMPLATE_CHAR</code>.
   */
  protected static final char           TEMPLATE_CHAR               = 'O';
  /**
   * <code>TEMPLATE_DATE</code>.
   */
  protected static final Date           TEMPLATE_DATE               = new Date(
                                                                        27166271000L);
  /**
   * <code>TEMPLATE_DURATION</code>.
   */
  protected static final Long           TEMPLATE_DURATION           = new Long(
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
  protected static final Date           TEMPLATE_TIME               = new Date(
                                                                        366000);

  private ITranslationProvider          translationProvider;
  private IConfigurableConnectorFactory connectorFactory;
  private IMvcBinder                    mvcBinder;
  private IActionFactory<G, E>          actionFactory;
  private IIconFactory<F>               iconFactory;
  private IModelCascadingBinder         modelCascadingBinder;

  private IDisplayableAction            binaryPropertyInfoAction;
  private IDisplayableAction            lovAction;
  private IDisplayableAction            openFileAsBinaryPropertyAction;
  private IDisplayableAction            resetPropertyAction;
  private IDisplayableAction            saveBinaryPropertyAsFileAction;

  private int                           maxCharacterLength          = 32;
  private int                           maxColumnCharacterLength    = 32;

  /**
   * {@inheritDoc}
   */
  public IView<E> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<E> view = null;
    if (viewDescriptor instanceof IComponentViewDescriptor) {
      view = createComponentView((IComponentViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof INestingViewDescriptor) {
      view = createNestingView((INestingViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IImageViewDescriptor) {
      view = createImageView((IImageViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IPropertyViewDescriptor) {
      view = createPropertyView((IPropertyViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICollectionViewDescriptor) {
      view = createCollectionView((ICollectionViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICompositeViewDescriptor) {
      view = createCompositeView((ICompositeViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICardViewDescriptor) {
      view = createCardView((ICardViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITreeViewDescriptor) {
      view = createTreeView((ITreeViewDescriptor) viewDescriptor,
          actionHandler, locale);
    }
    if (view != null) {
      try {
        if (actionHandler != null) {
          actionHandler.checkAccess(viewDescriptor);
        }
        if (viewDescriptor.isReadOnly()) {
          view.getConnector().setLocallyWritable(false);
        }
        if (viewDescriptor.getReadabilityGates() != null) {
          for (IGate gate : viewDescriptor.getReadabilityGates()) {
            view.getConnector().addReadabilityGate(gate.clone());
          }
        }
        if (viewDescriptor.getWritabilityGates() != null) {
          for (IGate gate : viewDescriptor.getWritabilityGates()) {
            view.getConnector().addWritabilityGate(gate.clone());
          }
        }
        finishComponentConfiguration(viewDescriptor, locale, view);
        decorateWithActions(viewDescriptor, actionHandler, locale, view);
        decorateWithBorder(view, locale);
      } catch (SecurityException ex) {
        view.setPeer(createSecurityComponent());
      }
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
   * Creates a property view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createPropertyView(IPropertyViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<E> view = createPropertyView((IPropertyDescriptor) viewDescriptor
        .getModelDescriptor(), viewDescriptor.getRenderedChildProperties(),
        actionHandler, locale);
    return constructView(view.getPeer(), viewDescriptor, view.getConnector());
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
   * Creates a nesting view.
   * 
   * @param viewDescriptor
   *          the view descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created nesting view.
   */
  protected abstract IView<E> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale);

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
   * Decorates a view with a border.
   * 
   * @param view
   *          the view to decorate.
   * @param locale
   *          the locale to be used for a titled border.
   */
  protected abstract void decorateWithBorder(IView<E> view, Locale locale);

  /**
   * Creates the connector for a tree view.
   * 
   * @param viewDescriptor
   *          the tree view descriptor.
   * @param locale
   *          the locale to use.
   * @return the connector for the tree view.
   */
  protected ICompositeValueConnector createTreeViewConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale) {
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
          ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
              viewDescriptor, locale, subtreeViewDescriptor, 1);
          compositeConnector.addChildConnector(subtreeConnector);
          subtreeConnectors.add(subtreeConnector);
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
      if (((ISimpleTreeLevelDescriptor) rootDescriptor).getChildDescriptor() != null) {
        ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
            viewDescriptor, locale,
            ((ISimpleTreeLevelDescriptor) rootDescriptor).getChildDescriptor(),
            1);
        simpleConnector.addChildConnector(subtreeConnector);
        simpleConnector.setCollectionConnectorProvider(subtreeConnector);
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

    if (connector instanceof IConnectorSelector) {
      ((IConnectorSelector) connector).setTracksChildrenSelection(true);
    }
    return connector;
  }

  private ICollectionConnectorProvider createNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale,
      ITreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionConnectorProvider connector = null;
    if (subtreeViewDescriptor instanceof ICompositeTreeLevelDescriptor) {
      connector = createCompositeNodeGroupConnector(viewDescriptor, locale,
          (ICompositeTreeLevelDescriptor) subtreeViewDescriptor, depth);
    } else if (subtreeViewDescriptor instanceof ISimpleTreeLevelDescriptor) {
      connector = createSimpleNodeGroupConnector(viewDescriptor, locale,
          (ISimpleTreeLevelDescriptor) subtreeViewDescriptor, depth);
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

  private ICollectionConnectorProvider createCompositeNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale,
      ICompositeTreeLevelDescriptor subtreeViewDescriptor, int depth) {
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
        ICollectionConnectorProvider childConnector = createNodeGroupConnector(
            viewDescriptor, locale, childDescriptor, depth + 1);
        nodeGroupPrototypeConnector.addChildConnector(childConnector);
        subtreeConnectors.add(childConnector);
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

  private ICollectionConnectorProvider createSimpleNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor, Locale locale,
      ISimpleTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionPropertyDescriptor<?> nodeGroupModelDescriptor = (ICollectionPropertyDescriptor<?>) subtreeViewDescriptor
        .getNodeGroupDescriptor().getModelDescriptor();
    IConfigurableCollectionConnectorProvider nodeGroupPrototypeConnector = connectorFactory
        .createConfigurableCollectionConnectorProvider(nodeGroupModelDescriptor
            .getName()
            + "Element", subtreeViewDescriptor.getNodeGroupDescriptor()
            .getRenderedProperty());
    if (subtreeViewDescriptor.getChildDescriptor() != null
        && depth < viewDescriptor.getMaxDepth()) {
      ICollectionConnectorProvider childConnector = createNodeGroupConnector(
          viewDescriptor, locale, subtreeViewDescriptor.getChildDescriptor(),
          depth + 1);
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
   * Creates a table column connector.
   * 
   * @param columnId
   *          the column id to create the connector for.
   * @param descriptor
   *          the component descriptor this table relies on.
   * @return the connector for the table column.
   */
  protected IValueConnector createColumnConnector(String columnId,
      IComponentDescriptor<?> descriptor) {
    IPropertyDescriptor propertyDescriptor = descriptor
        .getPropertyDescriptor(columnId);
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + columnId + " defined for "
          + descriptor.getComponentContract());
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return getConnectorFactory().createCompositeValueConnector(
          columnId,
          ((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getToStringProperty());
    }
    return getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
  }

  /**
   * Creates a card view connector.
   * 
   * @param cardView
   *          the card view to create the connector for.
   * @param actionHandler
   *          the action handler.
   * @return the card view connector.
   */
  protected IValueConnector createCardViewConnector(final IMapView<E> cardView,
      final IActionHandler actionHandler) {
    IValueConnector cardViewConnector = getConnectorFactory()
        .createValueConnector(cardView.getDescriptor().getName());
    cardViewConnector
        .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

          public void connectorValueChange(ConnectorValueChangeEvent evt) {
            Object cardModel = evt.getNewValue();
            boolean accessGranted = true;
            if (cardModel instanceof ISecurable && actionHandler != null) {
              try {
                actionHandler.checkAccess((ISecurable) cardModel);
              } catch (SecurityException se) {
                accessGranted = false;
              }
            }
            E cardsPeer = cardView.getPeer();
            if (accessGranted) {
              String cardName = ((ICardViewDescriptor) cardView.getDescriptor())
                  .getCardNameForModel(cardModel);
              if (cardName != null) {
                IView<E> childCardView = cardView.getChild(cardName);
                if (childCardView != null) {
                  showCardInPanel(cardsPeer, cardName);
                  IValueConnector childCardConnector = childCardView
                      .getConnector();
                  if (childCardConnector != null) {
                    // To handle polymorphism, especially for modules, we refine
                    // the model descriptor.
                    if (((IModelValueConnector) cardView.getConnector()
                        .getModelConnector()).getModelDescriptor().getClass()
                        .isAssignableFrom(
                            childCardView.getDescriptor().getModelDescriptor()
                                .getClass())) {
                      ((IModelValueConnector) cardView.getConnector()
                          .getModelConnector())
                          .setModelDescriptor(childCardView.getDescriptor()
                              .getModelDescriptor());
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
            } else {
              showCardInPanel(cardsPeer, ICardViewDescriptor.SECURITY_CARD);
            }
          }
        });
    return cardViewConnector;
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

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  /**
   * Gets the connectorFactory.
   * 
   * @return the connectorFactory.
   */
  protected IConfigurableConnectorFactory getConnectorFactory() {
    return connectorFactory;
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
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
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
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
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
    binaryActions.add(openAction);
    binaryActions.add(saveAction);
    binaryActions.add(resetAction);
    binaryActions.add(infoAction);
    return binaryActions;
  }

  /**
   * Creates the list of value action.
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
   * @return the generic list of value action.
   */
  protected G createLovAction(E viewComponent, IValueConnector connector,
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    G action = getActionFactory().createAction(lovAction, actionHandler,
        viewComponent, propertyDescriptor, connector, locale);
    return action;
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
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  public IIconFactory<F> getIconFactory() {
    return iconFactory;
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
   * Sets the modelCascadingBinder.
   * 
   * @param modelCascadingBinder
   *          the modelCascadingBinder to set.
   */
  public void setModelCascadingBinder(IModelCascadingBinder modelCascadingBinder) {
    this.modelCascadingBinder = modelCascadingBinder;
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
    if (view != null) {
      if (viewDescriptor.isCascadingModels()) {
        IView<E> masterView = view.getChildren().get(0);
        view.setConnector(masterView.getConnector());
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
    return view;
  }

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
   * Creates a number property view.
   * 
   * @param propertyDescriptor
   *          the number property descriptor
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createNumberPropertyView(
      INumberPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<E> view = null;
    if (propertyDescriptor instanceof IIntegerPropertyDescriptor) {
      view = createIntegerPropertyView(
          (IIntegerPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDecimalPropertyDescriptor) {
      view = createDecimalPropertyView(
          (IDecimalPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    }
    return view;
  }

  /**
   * Creates a decimal property view.
   * 
   * @param propertyDescriptor
   *          the number property descriptor
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createDecimalPropertyView(
      IDecimalPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates an integer property view.
   * 
   * @param propertyDescriptor
   *          the number property descriptor
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createIntegerPropertyView(
      IIntegerPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a single property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param renderedChildProperties
   *          the rendered children properties if the property is a reference
   *          property.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createPropertyView(IPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<E> view = null;
    if (propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      view = createBooleanPropertyView(
          (IBooleanPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IDatePropertyDescriptor) {
      view = createDatePropertyView(
          (IDatePropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof ITimePropertyDescriptor) {
      view = createTimePropertyView(
          (ITimePropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IDurationPropertyDescriptor) {
      view = createDurationPropertyView(
          (IDurationPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
      view = createEnumerationPropertyView(
          (IEnumerationPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof INumberPropertyDescriptor) {
      view = createNumberPropertyView(
          (INumberPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      view = createRelationshipEndPropertyView(
          (IRelationshipEndPropertyDescriptor) propertyDescriptor,
          renderedChildProperties, actionHandler, locale);
    } else if (propertyDescriptor instanceof IStringPropertyDescriptor) {
      view = createTextualPropertyView(
          (IStringPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IBinaryPropertyDescriptor) {
      view = createBinaryPropertyView(
          (IBinaryPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else if (propertyDescriptor instanceof IColorPropertyDescriptor) {
      view = createColorPropertyView(
          (IColorPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    decorateWithDescription(propertyDescriptor, locale, view);
    return view;
  }

  /**
   * Creates a textual property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createTextualPropertyView(
      IStringPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    if (propertyDescriptor instanceof IPasswordPropertyDescriptor) {
      return createPasswordPropertyView(
          (IPasswordPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ISourceCodePropertyDescriptor) {
      return createSourceCodePropertyView(
          (ISourceCodePropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ITextPropertyDescriptor) {
      return createTextPropertyView(
          (ITextPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    }
    return createStringPropertyView(propertyDescriptor, actionHandler, locale);
  }

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
   * Creates a color property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createColorPropertyView(
      IColorPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a binary property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a password property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createPasswordPropertyView(
      IPasswordPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a text property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createTextPropertyView(
      ITextPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a source code property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createSourceCodePropertyView(
      ISourceCodePropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a string property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createStringPropertyView(
      IStringPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a relationship end property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param renderedChildProperties
   *          the rendered child properties.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected IView<E> createRelationshipEndPropertyView(
      IRelationshipEndPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<E> view = null;

    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      view = createReferencePropertyView(
          (IReferencePropertyDescriptor<?>) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      view = createCollectionPropertyView(
          (ICollectionPropertyDescriptor<?>) propertyDescriptor,
          renderedChildProperties, actionHandler, locale);
    }
    return view;
  }

  /**
   * Creates a reference property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createReferencePropertyView(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  private IView<E> createCollectionPropertyView(
      ICollectionPropertyDescriptor<?> propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {

    IView<E> view;
    if (renderedChildProperties != null && renderedChildProperties.size() > 1) {
      BasicTableViewDescriptor viewDescriptor = new BasicTableViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      List<ISubViewDescriptor> columnViewDescriptors = new ArrayList<ISubViewDescriptor>();
      for (String renderedProperty : renderedChildProperties) {
        BasicSubviewDescriptor columnDescriptor = new BasicSubviewDescriptor();
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
   * Creates an enumeration property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a duration property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createDurationPropertyView(
      IDurationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Creates a time property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createTimePropertyView(
      ITimePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a date property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createDatePropertyView(
      IDatePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale);

  /**
   * Creates a boolean property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the created property view.
   */
  protected abstract IView<E> createBooleanPropertyView(
      IBooleanPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Computes the connector id for component view.
   * 
   * @param viewDescriptor
   *          the component view descriptor.
   * @return the computed connector id.
   */
  protected String getConnectorIdForComponentView(
      IComponentViewDescriptor viewDescriptor) {
    if (viewDescriptor.getModelDescriptor() instanceof IComponentDescriptor) {
      return ModelRefPropertyConnector.THIS_PROPERTY;
    }
    return viewDescriptor.getModelDescriptor().getName();
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
   * Creates a percent property view.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   * @return the evenly distributed grid view.
   */
  protected abstract IView<E> createPercentPropertyView(
      IPercentPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale);

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
    } else if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return getTemplateValue(((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor().getPropertyDescriptor(
              ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                  .getReferencedDescriptor().getToStringProperty()));
    }
    return null;
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
   * Sets the maxCharacterLength.
   * 
   * @param maxCharacterLength
   *          the maxCharacterLength to set.
   */
  public void setMaxCharacterLength(int maxCharacterLength) {
    this.maxCharacterLength = maxCharacterLength;
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
   * Sets the maxColumnCharacterLength.
   * 
   * @param maxColumnCharacterLength
   *          the maxColumnCharacterLength to set.
   */
  public void setMaxColumnCharacterLength(int maxColumnCharacterLength) {
    this.maxColumnCharacterLength = maxColumnCharacterLength;
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
   * Adjusts a component various sizes (e.g. min, max, preferred) based on a
   * formatter and a template value.
   * 
   * @param component
   *          the component to adjust the sizes for.
   * @param formatter
   *          the formatter used if any.
   * @param templateValue
   *          the template value used.
   */
  protected void adjustSizes(E component, IFormatter formatter,
      Object templateValue) {
    adjustSizes(component, formatter, templateValue, 32);
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
   * @param extraWidth
   *          the extra size to be added.
   */
  protected abstract void adjustSizes(E component, IFormatter formatter,
      Object templateValue, int extraWidth);

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
}
