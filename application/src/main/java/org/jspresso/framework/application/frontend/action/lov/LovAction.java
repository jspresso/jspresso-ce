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
package org.jspresso.framework.application.frontend.action.lov;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractQbeAction;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.backend.action.StaticQueryComponentsAction;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.RefQueryComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is a standard &quot;List Of Values&quot; action for reference property
 * views. Although this action is used by default in view factories on reference
 * fields, it can also be used in a more standard way, i.e. registered as a view
 * action. In the latter, the {@code okAction} must be configured to
 * perform a custom treatment once the entity is chosen from the LOV.
 * Additionally you can statically configure the descriptor of the searched
 * entities using the {@code entityDescriptor} parameter so that the LOV
 * will act on this type of entities.
 * <p/>
 * The LOV action prepares a QBE view (filter / result list) along with 3
 * actions that can be further refined : {@code findAction},
 * {@code okAction} and {@code cancelAction}. It must the be linked to
 * a {@code ModalDialogAction} so that the LOV actually pops up.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 10134 $
 */
public class LovAction<E, F, G> extends FrontendAction<E, F, G> {

  private static final Logger LOG = LoggerFactory.getLogger(LovAction.class);

  /**
   * {@code LOV_PRESELECTED_ITEM}.
   */
  public static final  String LOV_PRESELECTED_ITEM     = "LOV_PRESELECTED_ITEM";
  /**
   * {@code LOV_SELECTED_ITEM}.
   */
  public static final  String LOV_SELECTED_ITEM        = "LOV_SELECTED_ITEM";
  private static final String NON_LOV_TRIGGERING_CHARS =
      "%" + IQueryComponent.DISJUNCT + IQueryComponent.NOT_VAL + IQueryComponent.NULL_VAL;
  /**
   * {@code REF_VIEW_DESCRIPTOR}.
   */
  public static final  String REF_VIEW_DESCRIPTOR      = "REF_VIEW_DESCRIPTOR";
  private boolean                                    autoquery;
  private Integer                                    pageSize;
  private IDisplayableAction                         cancelAction;
  private CreateQueryComponentAction                 createQueryComponentAction;
  private IComponentDescriptor<? extends IComponent> entityDescriptor;
  private IReferencePropertyDescriptor<IComponent>   entityRefQueryDescriptor;
  private IDisplayableAction                         findAction;
  private Map<String, Object>                        initializationMapping;
  private ILovViewDescriptorFactory                  lovViewDescriptorFactory;
  private ESelectionMode                             selectionMode;
  private IDisplayableAction                         okAction;
  private IAction                                    pagingAction;
  private String                                     defaultIconImageURL;
  private List<?>                                    staticComponentStore;
  private IComponentDescriptorRegistry               componentDescriptorRegistry;
  private IDisplayableAction                         createAction;


  /**
   * Constructs a new {@code LovAction} instance.
   */
  public LovAction() {
    setAutoquery(true);
  }

  /**
   * {@inheritDoc}
   *
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   * @return the boolean
   */
  @SuppressWarnings({"ConstantConditions", "unchecked"})
  @Override
  public boolean execute(final IActionHandler actionHandler, final Map<String, Object> context) {
    if (getStaticComponentStore() != null) {
      context.put(StaticQueryComponentsAction.COMPONENT_STORE_KEY, getStaticComponentStore());
    }
    IReferencePropertyDescriptor<IComponent> erqDescriptor = getEntityRefQueryDescriptor(context);
    context.put(CreateQueryComponentAction.COMPONENT_REF_DESCRIPTOR, erqDescriptor);
    context.put(REF_VIEW_DESCRIPTOR, getView(context).getDescriptor());
    IValueConnector viewConnector = getViewConnector(context);
    Object preselectedItem = context.get(LOV_PRESELECTED_ITEM);
    String autoCompletePropertyValue = getActionCommand(context);

    Object masterComponent = null;
    if (getModelDescriptor(context) instanceof IPropertyDescriptor && viewConnector.getParentConnector() != null) {
      if (preselectedItem == null) {
        preselectedItem = viewConnector.getConnectorValue();
      }
      // The following relies on a workaround used to determine the bean
      // model whenever the lov component is used inside a JTable.
      IValueConnector parentModelConnector = viewConnector.getParentConnector().getModelConnector();
      if (parentModelConnector instanceof IModelProvider) {
        masterComponent = ((IModelProvider) parentModelConnector).getModel();
      } else if (parentModelConnector instanceof ICollectionConnector) {
        int collectionIndex = ((ICollectionConnector) viewConnector.getParentConnector()).getSelectedIndices()[0];
        masterComponent = ((ICollectionConnector) parentModelConnector).getChildConnector(collectionIndex)
                                                                       .getConnectorValue();
      }
    } else {
      masterComponent = getSelectedModel(context);
    }
    context.put(CreateQueryComponentAction.MASTER_COMPONENT, masterComponent);

    IView<E> parentView = getView(new int[]{-1}, context);
    if (parentView != null && parentView.getDescriptor() instanceof ITableViewDescriptor) {
      context.put(FrontendAction.COMPONENT_TO_FOCUS, parentView.getPeer());
    }

    actionHandler.execute(createQueryComponentAction, context);
    IQueryComponent queryComponent = (IQueryComponent) context.get(IQueryComponent.QUERY_COMPONENT);
    queryComponent.setPageSize(getPageSize());

    autoCompletePropertyValue = queryComponent.refineValue(autoCompletePropertyValue, null);

    String autoCompletePropertyName = null;
    if (viewConnector instanceof IRenderableCompositeValueConnector
        && ((IRenderableCompositeValueConnector) viewConnector).getRenderingConnector() != null) {
      Map<String, ESort> lovOrderingProperties = new LinkedHashMap<>();
      IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) ((IRenderableCompositeValueConnector)
          viewConnector)
          .getRenderingConnector().getModelDescriptor();
      if (propertyDescriptor.isModifiable()) {
        autoCompletePropertyName = propertyDescriptor.getName();
      } else {
        autoCompletePropertyName = erqDescriptor.getReferencedDescriptor().getAutoCompleteProperty();
      }
      if (autoCompletePropertyValue != null && autoCompletePropertyValue.length() > 0 && !autoCompletePropertyValue
          .equals("*")) {
        queryComponent.put(autoCompletePropertyName, autoCompletePropertyValue);
        if (!containsNonLovTriggeringChar(autoCompletePropertyValue)) {
          // only modify sort ordering if we are in auto complete mode
          // see bug #549
          lovOrderingProperties.put(autoCompletePropertyName, ESort.ASCENDING);
        }
      }
      Map<String, ESort> legacyOrderingProperties = queryComponent.getOrderingProperties();
      if (legacyOrderingProperties != null) {
        lovOrderingProperties.putAll(legacyOrderingProperties);
      }
      queryComponent.setOrderingProperties(lovOrderingProperties);
      if (getModel(context) instanceof IQueryComponent) {
        if (containsNonLovTriggeringChar(autoCompletePropertyValue)) {
          // This is important since the typed in value (queryPropertyValue)
          // is only passed as
          // action parameter. We want to preserve it in the UI.
          Object connectorValue = viewConnector.getConnectorValue();
          if (connectorValue instanceof IQueryComponent) {
            // To cleanup the preceding values (e.g. ID)
            // see bug #986
            connectorValue = ((IQueryComponent) connectorValue).clone();
            ((IQueryComponent) connectorValue).clear();
            ((IQueryComponent) connectorValue).putAll(queryComponent);
            // will fire connector value change and trigger the action set on
            // the ref field if any see bug #910
            viewConnector.setConnectorValue(connectorValue);
          } else {
            viewConnector.setConnectorValue(queryComponent);
          }
          ((IRenderableCompositeValueConnector) viewConnector).getRenderingConnector().setConnectorValue(
              autoCompletePropertyValue);
          return true;
        }
      }
    }

    // We must bind before querying for potential lazy loading to happen during
    // the transaction.
    if (erqDescriptor.getReferencedDescriptor() instanceof RefQueryComponentDescriptor<?>) {
      IReferencePropertyDescriptor<IComponent> refinedErqDescriptor = ((BasicReferencePropertyDescriptor<IComponent>)
          erqDescriptor)
          .clone();
      // we are on a nested LOV => We must retrieve the real Entity descriptor
      ((BasicReferencePropertyDescriptor<IComponent>) refinedErqDescriptor).setReferencedDescriptor(
          (IComponentDescriptor<? extends IComponent>) getComponentDescriptorRegistry()
              .getComponentDescriptor(erqDescriptor.getModelType()));
      erqDescriptor = refinedErqDescriptor;
    }
    IView<E> lovView = getViewFactory(context).createView(createLovViewDescriptor(erqDescriptor, context),
        actionHandler, getLocale(context));
    IValueConnector queryEntityConnector = (IValueConnector) context.get(
        CreateQueryComponentAction.QUERY_MODEL_CONNECTOR);
    getMvcBinder(context).bind(lovView.getConnector(), queryEntityConnector);

    if (autoquery) {
      actionHandler.execute(getFindAction(), context);
      if (autoCompletePropertyValue != null && autoCompletePropertyValue.length() > 0 && !autoCompletePropertyValue
          .equals("*") && queryComponent.getQueriedComponents() != null) {
        if (queryComponent.getQueriedComponents().size() > 0) {
          Object selectedItem = null;
          Object firstItem = queryComponent.getQueriedComponents().get(0);
          if (queryComponent.getQueriedComponents().size() == 1) {
            selectedItem = firstItem;
          } else if (autoCompletePropertyName != null) {
            Object secondItem = queryComponent.getQueriedComponents().get(1);
            try {
              // Determine if it is a single exact match.
              String firstItemPropertyValue = getBackendController(context).getAccessorFactory().createPropertyAccessor(
                  autoCompletePropertyName, firstItem.getClass()).getValue(firstItem);
              String secondItemPropertyValue = getBackendController(context).getAccessorFactory()
                                                                            .createPropertyAccessor(
                                                                                autoCompletePropertyName,
                                                                                firstItem.getClass()).getValue(
                      secondItem);
              if (autoCompletePropertyValue.equalsIgnoreCase(firstItemPropertyValue) && !autoCompletePropertyValue
                  .equalsIgnoreCase(secondItemPropertyValue)) {
                selectedItem = firstItem;
              }
            } catch (Exception ex) {
              LOG.warn("Could not retrieve {} on {}", autoCompletePropertyName, firstItem, ex);
            }
          }
          if (selectedItem != null) {
            if (selectedItem instanceof IEntity) {
              selectedItem = getController(context).getBackendController().merge((IEntity) selectedItem,
                  EMergeMode.MERGE_LAZY);
            }
            context.put(LOV_SELECTED_ITEM, selectedItem);
            actionHandler.execute(getOkAction(), context);
            return true;
          }
        }
      }
      handlePreselectedItem(preselectedItem, queryComponent, lovView);
    }
    feedContextWithDialog(erqDescriptor, queryComponent, lovView, actionHandler, context);
    if (context.get(FrontendAction.COMPONENT_TO_FOCUS) == null) {
      // To return to the action field once the dialog closes if and only if
      // The focus has not been explicitly set to something else.
      context.put(FrontendAction.COMPONENT_TO_FOCUS, getSourceComponent(context));
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Create lov view descriptor.
   *
   * @param erqDescriptor
   *     the erq descriptor
   * @param context
   *     the context
   * @return the i view descriptor
   */
  protected IViewDescriptor createLovViewDescriptor(IReferencePropertyDescriptor<IComponent> erqDescriptor,
                                                    Map<String, Object> context) {
    return lovViewDescriptorFactory.createLovViewDescriptor(erqDescriptor, getSelectionMode(context), getOkAction(),
        context);
  }

  /**
   * Handle preselected item.
   *
   * @param preselection
   *     the preselected item or item collection.
   * @param queryComponent
   *     the query component.
   * @param lovView
   *     the lov view.
   */
  protected void handlePreselectedItem(Object preselection, IQueryComponent queryComponent, IView<E> lovView) {

    if (preselection == null || queryComponent.getQueriedComponents().isEmpty()) {
      return;
    }

    ICollectionConnector resultConnector = (ICollectionConnector) ((ICompositeValueConnector) lovView.getConnector())
        .getChildConnector(IQueryComponent.QUERIED_COMPONENTS);
    if (resultConnector == null) {
      return;
    }

    Set<?> preselectionCollection;
    if (preselection instanceof Collection) {
      preselectionCollection = new HashSet<>((Collection<?>) preselection);
    } else {
      preselectionCollection = Collections.singleton(preselection);
    }
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < queryComponent.getQueriedComponents().size(); i++) {
      if (preselectionCollection.contains(queryComponent.getQueriedComponents().get(i))) {
        indices.add(i);
      }
    }

    if (indices.isEmpty()) {
      return;
    }

    resultConnector.setSelectedIndices(ArrayUtils.toPrimitive(indices.toArray(new Integer[]{}), 0));
  }

  /**
   * Feed context with dialog.
   *
   * @param erqDescriptor
   *     the erq descriptor
   * @param queryComponent
   *     the query component
   * @param lovView
   *     the lov view
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   */
  protected void feedContextWithDialog(IReferencePropertyDescriptor<IComponent> erqDescriptor,
                                       IQueryComponent queryComponent, IView<E> lovView,
                                       final IActionHandler actionHandler, final Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<>();
    getViewConnector(context).setConnectorValue(getViewConnector(context).getConnectorValue());

    actions.add(getOkAction());
    if (getCreateAction() != null) {
      actions.add(getCreateAction());
    }
    actions.add(getFindAction());
    actions.add(getCancelAction());
    context.put(ModalDialogAction.DIALOG_ACTIONS, actions);
    context.put(ModalDialogAction.DIALOG_TITLE,
        getI18nName(getTranslationProvider(context), getLocale(context)) + " : " + erqDescriptor
            .getReferencedDescriptor().getI18nName(getTranslationProvider(context), getLocale(context)));
    context.put(ModalDialogAction.DIALOG_VIEW, lovView);
    if (lovView instanceof ICompositeView<?>) {
      context.put(ModalDialogAction.DIALOG_FOCUSED_COMPONENT,
          ((ICompositeView<E>) lovView).getChildren().get(1).getPeer());
    }

    if (pagingAction != null) {
      PropertyChangeListener paginationListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if (evt.getOldValue() != null && evt.getNewValue() != null) {
            try {
              context.put(AbstractQbeAction.PAGINATE, null);
              actionHandler.execute(pagingAction, context);
            } finally {
              context.remove(AbstractQbeAction.PAGINATE);
            }
          }
        }
      };
      queryComponent.addPropertyChangeListener(IPageable.PAGE, paginationListener);
    }
  }

  private static boolean containsNonLovTriggeringChar(String value) {
    if (value != null) {
      for (int i = 0; i < NON_LOV_TRIGGERING_CHARS.length(); i++) {
        if (value.indexOf(NON_LOV_TRIGGERING_CHARS.charAt(i)) >= 0) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @param translationProvider
   *     the translation provider
   * @param locale
   *     the locale
   * @return the i 18 n description
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider, Locale locale) {
    if (getDescription() == null) {
      if (entityDescriptor != null) {
        return translationProvider.getTranslation("lov.element.description",
            new Object[]{entityDescriptor.getI18nName(translationProvider, locale)}, locale);
      }
      return translationProvider.getTranslation("lov.description", locale);
    }
    return super.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   *
   * @param translationProvider
   *     the translation provider
   * @param locale
   *     the locale
   * @return the i 18 n name
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider, Locale locale) {
    if (getName() == null) {
      if (entityDescriptor != null) {
        return translationProvider.getTranslation("lov.element.name",
            new Object[]{entityDescriptor.getI18nName(translationProvider, locale)}, locale);
      }
      return translationProvider.getTranslation("lov.name", locale);
    }
    return super.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   *
   * @return the icon
   */
  @Override
  public Icon getIcon() {
    Icon icon = super.getIcon();
    if (icon == null) {
      if (entityDescriptor != null) {
        icon = entityDescriptor.getIcon();
      }
      if (icon == null) {
        icon = new Icon(getDefaultIconImageURL(), null);
      }
    }
    return icon;
  }

  /**
   * Whenever setting autoquery to {@code true}, the LOV action will
   * trigger the query as soon as it gets executed and the initial query filter
   * is not empty (e.g. the user typed something in the reference field). This
   * brings autocomplete feature to reference fields since, whenever the initial
   * filter limits to exactly 1 result, the LOV dialog won't even open. Defaults
   * to true.
   *
   * @param autoquery
   *     the autoquery to set.
   */
  public void setAutoquery(boolean autoquery) {
    this.autoquery = autoquery;
  }

  /**
   * Configures the action to be executed whenever the user cancels the LOV
   * dialog.
   *
   * @param cancelAction
   *     the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Gets cancel action.
   *
   * @return the cancel action
   */
  public IDisplayableAction getCancelAction() {
    return cancelAction;
  }

  /**
   * Configures the action used to create the filter query component based on
   * the type of entities backing the LOV.
   *
   * @param createQueryComponentAction
   *     the createQueryComponentAction to set.
   */
  public void setCreateQueryComponentAction(CreateQueryComponentAction createQueryComponentAction) {
    this.createQueryComponentAction = createQueryComponentAction;
  }

  /**
   * Configures explicitly the type of entities the LOV relies on. This is
   * automatically determined when installed on a reference field but must be set
   * in any other case.
   *
   * @param entityDescriptor
   *     the entityDescriptor to set.
   */
  public void setEntityDescriptor(IComponentDescriptor<? extends IComponent> entityDescriptor) {
    this.entityDescriptor = entityDescriptor;
    this.entityRefQueryDescriptor = createEntityRefQueryDescriptor(entityDescriptor, initializationMapping);
  }

  /**
   * Gets the entity descriptor to execute the LOV action for from the context.
   * It allows sub classes to override in order to define the descriptor at
   * runtime.
   *
   * @param context
   *     the action context.
   * @return the entity descriptor to execute the LOV action for.
   */
  @SuppressWarnings("UnusedParameters")
  protected IComponentDescriptor<? extends IComponent> getEntityDescriptor(Map<String, Object> context) {
    return entityDescriptor;
  }

  private IReferencePropertyDescriptor<IComponent> createEntityRefQueryDescriptor(
      IComponentDescriptor<? extends IComponent> descriptor, Map<String, Object> initMapping) {
    IReferencePropertyDescriptor<IComponent> refQueryDescriptor = null;
    if (descriptor != null) {
      refQueryDescriptor = new BasicReferencePropertyDescriptor<>();
      ((BasicReferencePropertyDescriptor<IComponent>) refQueryDescriptor).setReferencedDescriptor(descriptor);
      ((BasicReferencePropertyDescriptor<IComponent>) refQueryDescriptor).setInitializationMapping(initMapping);
    }
    return refQueryDescriptor;
  }

  /**
   * Configures the action to be executed whenever the user queries the
   * persistent store, either explicitly when using the action installed in the
   * LOV dialog or implicitly through the auto query feature.
   *
   * @param findAction
   *     the findAction to set.
   */
  public void setFindAction(IDisplayableAction findAction) {
    this.findAction = findAction;
  }

  /**
   * Gets find action.
   *
   * @return the find action
   */
  public IDisplayableAction getFindAction() {
    return findAction;
  }

  /**
   * Whenever the LOV action is not used on a reference field, in which case the
   * filter initialization mapping is taken from the reference field descriptor
   * (see {@code BasicReferencePropertyDescriptor} documentation), this
   * property allows to initialize some of the LOV filter properties.
   * Initialization is computed dynamically by transferring property values from
   * the view model to the filter.
   *
   * @param initializationMapping
   *     the initializationMapping to set.
   */
  public void setInitializationMapping(Map<String, Object> initializationMapping) {
    this.initializationMapping = initializationMapping;
    this.entityRefQueryDescriptor = createEntityRefQueryDescriptor(entityDescriptor, initializationMapping);
  }
  
  /**
   * Get initialization mapping.
   * @return the initialization mapping.
   */
  protected Map<String, Object> getInitializationMapping() {
    return initializationMapping;
  }

  /**
   * Configures the factory to be used to create the QBE view used in the
   * dialog.
   *
   * @param lovViewDescriptorFactory
   *     the lovViewDescriptorFactory to set.
   */
  public void setLovViewDescriptorFactory(ILovViewDescriptorFactory lovViewDescriptorFactory) {
    this.lovViewDescriptorFactory = lovViewDescriptorFactory;
  }

  /**
   * Configures the action to be executed whenever the user validates the LOV
   * selection.
   *
   * @param okAction
   *     the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Gets ok action.
   *
   * @return the ok action
   */
  public IDisplayableAction getOkAction() {
    return okAction;
  }

  /**
   * Gets the entityRefQueryDescriptor.
   *
   * @param context
   *     the action context.
   * @return the entityRefQueryDescriptor.
   */
  @SuppressWarnings("unchecked")
  protected IReferencePropertyDescriptor<IComponent> getEntityRefQueryDescriptor(Map<String, Object> context) {
    if (entityRefQueryDescriptor != null) {
      return entityRefQueryDescriptor;
    }
    IComponentDescriptor<? extends IComponent> descriptor = getEntityDescriptor(context);
    if (descriptor != null) {
      return createEntityRefQueryDescriptor(descriptor, initializationMapping);
    }
    IModelDescriptor modelDescriptor = getModelDescriptor(context);
    if (modelDescriptor instanceof IReferencePropertyDescriptor) {
      IReferencePropertyDescriptor<IComponent> returnedDescriptor = (IReferencePropertyDescriptor<IComponent>)
          modelDescriptor;
      if (initializationMapping != null && returnedDescriptor instanceof BasicReferencePropertyDescriptor) {
        returnedDescriptor = (BasicReferencePropertyDescriptor<IComponent>) (
            (IReferencePropertyDescriptor<IComponent>) modelDescriptor)
            .clone();
        ((BasicReferencePropertyDescriptor<IComponent>) returnedDescriptor).setInitializationMapping(
            initializationMapping);
      }
      return returnedDescriptor;
    }
    return null;
  }

  /**
   * Sets the pagingAction.
   *
   * @param pagingAction
   *     the pagingAction to set.
   */
  public void setPagingAction(IAction pagingAction) {
    this.pagingAction = pagingAction;
  }

  /**
   * Gets paging action.
   *
   * @return the paging action
   */
  public IAction getPagingAction() {
    return pagingAction;
  }

  /**
   * Gets the selectionMode of the result view.
   *
   * @param lovContext
   *     the LOV action context.
   * @return the selectionMode.
   */
  protected ESelectionMode getSelectionMode(Map<String, Object> lovContext) {
    if (selectionMode != null) {
      return selectionMode;
    }
    return getDefaultSelectionMode(lovContext);
  }

  /**
   * Allows to force the result view selection mode.
   *
   * @param selectionMode
   *     the result view selection mode. When     , the     default selection mode is applied.
   */
  public void setSelectionMode(ESelectionMode selectionMode) {
    this.selectionMode = selectionMode;
  }

  /**
   * Determines the default selection mode for the result view.
   *
   * @param lovContext
   *     the LOV context.
   * @return the default selection mode for the result view.
   */
  protected ESelectionMode getDefaultSelectionMode(Map<String, Object> lovContext) {
    if (getModel(lovContext) instanceof IQueryComponent) {
      // We are on a filter view that supports multi selection
      return ESelectionMode.MULTIPLE_INTERVAL_CUMULATIVE_SELECTION;
    }
    return ESelectionMode.SINGLE_SELECTION;
  }

  /**
   * Gets the defaultIconImageURL.
   *
   * @return the defaultIconImageURL.
   */
  protected String getDefaultIconImageURL() {
    return defaultIconImageURL;
  }

  /**
   * Sets the defaultIconImageURL.
   *
   * @param defaultIconImageURL
   *     the defaultIconImageURL to set.
   */
  public void setDefaultIconImageURL(String defaultIconImageURL) {
    this.defaultIconImageURL = defaultIconImageURL;
  }

  /**
   * Gets page size.
   *
   * @return the page size
   */
  protected Integer getPageSize() {
    return pageSize;
  }

  /**
   * Sets page size.
   *
   * @param pageSize
   *     the page size
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Gets static component store.
   *
   * @return the static component store
   */
  protected List<?> getStaticComponentStore() {
    return staticComponentStore;
  }

  /**
   * Sets static component store.
   *
   * @param staticComponentStore
   *     the static component store
   */
  public void setStaticComponentStore(List<?> staticComponentStore) {
    this.staticComponentStore = staticComponentStore;
  }

  /**
   * Gets component descriptor registry.
   *
   * @return the component descriptor registry
   */
  protected IComponentDescriptorRegistry getComponentDescriptorRegistry() {
    return componentDescriptorRegistry;
  }

  /**
   * Sets component descriptor registry.
   *
   * @param componentDescriptorRegistry
   *     the component descriptor registry
   */
  public void setComponentDescriptorRegistry(IComponentDescriptorRegistry componentDescriptorRegistry) {
    this.componentDescriptorRegistry = componentDescriptorRegistry;
  }

  /**
   * Gets create action.
   *
   * @return the create action
   */
  protected IDisplayableAction getCreateAction() {
    return createAction;
  }

  /**
   * Sets create action.
   *
   * @param createAction
   *     the create action
   */
  public void setCreateAction(IDisplayableAction createAction) {
    this.createAction = createAction;
  }
}
