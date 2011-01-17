/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ILovViewDescriptorFactory;

/**
 * This is a standard &quot;List Of Values&quot; action for reference property
 * views. Although this action is used by default in view factories on reference
 * fields, it can also be used in a more standard way, i.e. registered as a view
 * action. In the latter, the <code>okAction</code> must be configured to
 * perform a custom treatment once the entity is chosen from the LOV.
 * Additionally you can statically configure the descriptor of the searched
 * entities using the <code>entityDescriptor</code> parameter so that the LOV
 * will act on this type of entities.
 * <p>
 * The LOV action prepares a QBE view (filter / result list) along with 3
 * actions that can be further refined : <code>findAction</code>,
 * <code>okAction</code> and <code>cancelAction</code>. It must the be linked to
 * a <code>ModalDialogAction</code> so that the LOV actually pops up.
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
public class LovAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * <code>LOV_PRESELECTED_ITEM</code>.
   */
  public static final String                    LOV_PRESELECTED_ITEM  = "LOV_PRESELECTED_ITEM";
  private boolean                               autoquery;
  private IDisplayableAction                    cancelAction;
  private CreateQueryComponentAction            createQueryComponentAction;
  private IComponentDescriptor<IEntity>         entityDescriptor;
  private IReferencePropertyDescriptor<IEntity> entityRefQueryDescriptor;
  private IDisplayableAction                    findAction;
  private Map<String, Object>                   initializationMapping;
  private ILovViewDescriptorFactory             lovViewDescriptorFactory;
  private IDisplayableAction                    okAction;
  private String                                nonLovTriggeringChars = "%;";

  /**
   * Constructs a new <code>LovAction</code> instance.
   */
  public LovAction() {
    setAutoquery(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    IReferencePropertyDescriptor<IEntity> erqDescriptor = getEntityRefQueryDescriptor(context);
    context.put(CreateQueryComponentAction.COMPONENT_REF_DESCRIPTOR,
        erqDescriptor);

    IValueConnector viewConnector = getViewConnector(context);
    String queryPropertyValue = getActionCommand(context);

    actionHandler.execute(createQueryComponentAction, context);
    IQueryComponent queryComponent = (IQueryComponent) context
    .get(IQueryComponent.QUERY_COMPONENT);

    if (viewConnector instanceof IRenderableCompositeValueConnector
        && ((IRenderableCompositeValueConnector) viewConnector)
            .getRenderingConnector() != null) {
      if (getModel(context) instanceof IQueryComponent) {
        if (nonLovTriggeringChars != null) {
          for (int i = 0; i < nonLovTriggeringChars.length(); i++) {
            if (queryPropertyValue != null
                && queryPropertyValue.indexOf(nonLovTriggeringChars.charAt(i)) >= 0) {
              // This is important since the typed in value (queryPropertyValue) is only passed as
              // action parameter. We want to preserve it in the UI.
              
              viewConnector.setConnectorValue(queryComponent);
              ((IRenderableCompositeValueConnector) viewConnector)
                  .getRenderingConnector()
                  .setConnectorValue(queryPropertyValue);
              return true;
            }
          }
        }
      }
    }

    if (autoquery) {
      actionHandler.execute(findAction, context);
      if (queryPropertyValue != null && !queryPropertyValue.equals("*")
          && queryComponent.getQueriedComponents() != null
          && queryComponent.getQueriedComponents().size() == 1) {
        IComponent selectedItem = queryComponent.getQueriedComponents().get(0);
        if (selectedItem instanceof IEntity) {
          selectedItem = getController(context).getBackendController().merge(
              (IEntity) selectedItem, EMergeMode.MERGE_CLEAN_LAZY);
        }
        context.put(LOV_PRESELECTED_ITEM, selectedItem);
        actionHandler.execute(okAction, context);
        return true;
      }
    }

    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();
    getViewConnector(context).setConnectorValue(
        getViewConnector(context).getConnectorValue());

    actions.add(findAction);
    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ModalDialogAction.DIALOG_ACTIONS, actions);
    IView<E> lovView = getViewFactory(context).createView(
        lovViewDescriptorFactory.createLovViewDescriptor(erqDescriptor,
            okAction), actionHandler, getLocale(context));
    context.put(ModalDialogAction.DIALOG_TITLE,
        getI18nName(getTranslationProvider(context), getLocale(context)));
    context.put(ModalDialogAction.DIALOG_VIEW, lovView);
    IValueConnector queryEntityConnector = (IValueConnector) context
        .get(CreateQueryComponentAction.QUERY_MODEL_CONNECTOR);
    getMvcBinder(context).bind(lovView.getConnector(), queryEntityConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() == null) {
      if (entityDescriptor != null) {
        return translationProvider.getTranslation("lov.element.description",
            new Object[] {entityDescriptor.getI18nName(translationProvider,
                locale)}, locale);
      }
      return translationProvider.getTranslation("lov.description", locale);
    }
    return super.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    if (getName() == null) {
      if (entityDescriptor != null) {
        return translationProvider.getTranslation("lov.element.name",
            new Object[] {entityDescriptor.getI18nName(translationProvider,
                locale)}, locale);
      }
      return translationProvider.getTranslation("lov.name", locale);
    }
    return super.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      if (entityDescriptor != null) {
        iconImageURL = entityDescriptor.getIconImageURL();
      }
      if (iconImageURL == null) {
        iconImageURL = "classpath:org/jspresso/framework/application/images/find-48x48.png";
      }
    }
    return iconImageURL;
  }

  /**
   * Whenever setting autoquery to <code>true</code>, the LOV action will
   * trigger the query as soon as it gets executed and the initial query filter
   * is not empty (e.g. the user typed something in the reference field). This
   * brings autocomplete feature to reference fields since, whenever the initial
   * filter limits to exactly 1 result, the LOV dialog won't even open. Defaults
   * to true.
   * 
   * @param autoquery
   *          the autoquery to set.
   */
  public void setAutoquery(boolean autoquery) {
    this.autoquery = autoquery;
  }

  /**
   * Configures the action to be executed whenever the user cancels the LOV
   * dialog.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Configures the action used to create the filter query component based on
   * the type of entities backing the LOV.
   * 
   * @param createQueryComponentAction
   *          the createQueryComponentAction to set.
   */
  public void setCreateQueryComponentAction(
      CreateQueryComponentAction createQueryComponentAction) {
    this.createQueryComponentAction = createQueryComponentAction;
  }

  /**
   * Configures explicitely the type of entities the LOV relies on. This is
   * automatically determined when installed on areference field but must be set
   * in any other case.
   * 
   * @param entityDescriptor
   *          the entityDescriptor to set.
   */
  public void setEntityDescriptor(IComponentDescriptor<IEntity> entityDescriptor) {
    this.entityDescriptor = entityDescriptor;
  }

  /**
   * Configures the action to be executed whenever the user queries the
   * persistent store, either explicitely when using the action installed in the
   * LOV dialog or implicitely through the auto query feature.
   * 
   * @param findAction
   *          the findAction to set.
   */
  public void setFindAction(IDisplayableAction findAction) {
    this.findAction = findAction;
  }

  /**
   * Whenever the LOV action is not used on a reference field, in which case the
   * filter initialization mapping is taken from the reference field descriptor
   * (see <code>BasicReferencePropertyDescriptor</code> documentation), this
   * property allows to initialize some of the LOV filter properties.
   * Initialization is computed dynamically by transferring property values from
   * the view model to the filter.
   * 
   * @param initializationMapping
   *          the initializationMapping to set.
   */
  public void setInitializationMapping(Map<String, Object> initializationMapping) {
    this.initializationMapping = initializationMapping;
  }

  /**
   * Configures the factory to be used to create the QBE view used in the
   * dialog.
   * 
   * @param lovViewDescriptorFactory
   *          the lovViewDescriptorFactory to set.
   */
  public void setLovViewDescriptorFactory(
      ILovViewDescriptorFactory lovViewDescriptorFactory) {
    this.lovViewDescriptorFactory = lovViewDescriptorFactory;
  }

  /**
   * Configures the action to be executed whenever the user validates the LOV
   * selection.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Gets the entityRefQueryDescriptor.
   * 
   * @param context
   *          the action context.
   * @return the entityRefQueryDescriptor.
   */
  @SuppressWarnings("unchecked")
  protected IReferencePropertyDescriptor<IEntity> getEntityRefQueryDescriptor(
      Map<String, Object> context) {
    if (entityDescriptor != null) {
      if (entityRefQueryDescriptor == null) {
        entityRefQueryDescriptor = new BasicReferencePropertyDescriptor<IEntity>();
        ((BasicReferencePropertyDescriptor<IEntity>) entityRefQueryDescriptor)
            .setReferencedDescriptor(entityDescriptor);
        ((BasicReferencePropertyDescriptor<IEntity>) entityRefQueryDescriptor)
            .setInitializationMapping(initializationMapping);
      }
      return entityRefQueryDescriptor;
    }
    IModelDescriptor modelDescriptor = getModelDescriptor(context);
    if (modelDescriptor instanceof IReferencePropertyDescriptor) {
      return (IReferencePropertyDescriptor<IEntity>) modelDescriptor;
    }
    return null;
  }
}
