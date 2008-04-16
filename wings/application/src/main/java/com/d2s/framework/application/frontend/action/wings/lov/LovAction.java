/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.wings.SComponent;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.CreateQueryComponentAction;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.action.wings.std.ModalDialogAction;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.component.IQueryComponent;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.ILovViewDescriptorFactory;

/**
 * A standard List of value action for reference property views. This action
 * should be used in view factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class LovAction extends ModalDialogAction {

  private boolean                               autoquery;
  private IDisplayableAction                    cancelAction;
  private CreateQueryComponentAction            createQueryComponentAction;
  private IReferencePropertyDescriptor<IEntity> entityRefQueryDescriptor;
  private IComponentDescriptor<IEntity>         entityDescriptor;
  private Map<String, String>                   initializationMapping;
  private IDisplayableAction                    findAction;
  private ILovViewDescriptorFactory             lovViewDescriptorFactory;
  private IDisplayableAction                    okAction;

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
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      if (entityDescriptor != null) {
        iconImageURL = entityDescriptor.getIconImageURL();
      }
      if (iconImageURL == null) {
        iconImageURL = "classpath:com/d2s/framework/application/images/find-48x48.png";
      }
    }
    return iconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();
    getViewConnector(context).setConnectorValue(
        getViewConnector(context).getConnectorValue());
    okAction.putInitialContext(ActionContextConstants.SOURCE_VIEW_CONNECTOR,
        getViewConnector(context));
    actions.add(findAction);
    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ActionContextConstants.DIALOG_ACTIONS, actions);
    IReferencePropertyDescriptor<IEntity> erqDescriptor = getEntityRefQueryDescriptor(context);
    IView<SComponent> lovView = getViewFactory(context).createView(
        lovViewDescriptorFactory.createLovViewDescriptor(erqDescriptor),
        actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, lovView);
    context.put(ActionContextConstants.COMPONENT_REF_DESCRIPTOR, erqDescriptor);
    actionHandler.execute(createQueryComponentAction, context);
    IValueConnector queryEntityConnector = (IValueConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR);
    getMvcBinder(context).bind(lovView.getConnector(), queryEntityConnector);
    findAction.putInitialContext(ActionContextConstants.QUERY_MODEL_CONNECTOR,
        queryEntityConnector);
    Object queryPropertyValue = context
        .get(ActionContextConstants.ACTION_COMMAND);
    if (autoquery && queryPropertyValue != null
        && !queryPropertyValue.equals("*")) {
      actionHandler.execute(findAction, context);
      IQueryComponent queryComponent = (IQueryComponent) queryEntityConnector
          .getConnectorValue();
      if (queryComponent.getQueriedComponents() != null
          && queryComponent.getQueriedComponents().size() == 1) {
        IEntity selectedEntity = getController(context).getApplicationSession()
            .merge((IEntity) queryComponent.getQueriedComponents().get(0),
                MergeMode.MERGE_KEEP);
        getViewConnector(context).setConnectorValue(selectedEntity);
        return true;
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the autoquery.
   * 
   * @param autoquery
   *            the autoquery to set.
   */
  public void setAutoquery(boolean autoquery) {
    this.autoquery = autoquery;
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *            the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the createQueryComponentAction.
   * 
   * @param createQueryComponentAction
   *            the createQueryComponentAction to set.
   */
  public void setCreateQueryComponentAction(
      CreateQueryComponentAction createQueryComponentAction) {
    this.createQueryComponentAction = createQueryComponentAction;
  }

  /**
   * Sets the findAction.
   * 
   * @param findAction
   *            the findAction to set.
   */
  public void setFindAction(IDisplayableAction findAction) {
    this.findAction = findAction;
  }

  /**
   * Sets the lovViewDescriptorFactory.
   * 
   * @param lovViewDescriptorFactory
   *            the lovViewDescriptorFactory to set.
   */
  public void setLovViewDescriptorFactory(
      ILovViewDescriptorFactory lovViewDescriptorFactory) {
    this.lovViewDescriptorFactory = lovViewDescriptorFactory;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *            the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Gets the entityRefQueryDescriptor.
   * 
   * @param context
   *            the action context.
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
    IModelDescriptor modelDescriptor = (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
    if (modelDescriptor instanceof IReferencePropertyDescriptor) {
      return (IReferencePropertyDescriptor<IEntity>) modelDescriptor;
    }
    return null;
  }

  /**
   * Sets the entityDescriptor.
   * 
   * @param entityDescriptor the entityDescriptor to set.
   */
  public void setEntityDescriptor(IComponentDescriptor<IEntity> entityDescriptor) {
    this.entityDescriptor = entityDescriptor;
  }

  
  /**
   * Sets the initializationMapping.
   * 
   * @param initializationMapping the initializationMapping to set.
   */
  public void setInitializationMapping(Map<String, String> initializationMapping) {
    this.initializationMapping = initializationMapping;
  }
}
