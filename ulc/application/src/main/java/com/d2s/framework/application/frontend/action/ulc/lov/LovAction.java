/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.IController;
import com.d2s.framework.application.backend.action.CreateQueryEntityAction;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.frontend.action.ulc.flow.ModalDialogAction;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.ILovViewDescriptorFactory;
import com.ulcjava.base.application.ULCComponent;

/**
 * A standard List of value action for reference property views. This action
 * should be used in view factories.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class LovAction extends ModalDialogAction {

  private ILovViewDescriptorFactory    lovViewDescriptorFactory;
  private CreateQueryEntityAction      createQueryEntityAction;
  private IDisplayableAction           okAction;
  private IDisplayableAction           cancelAction;
  private IDisplayableAction           findAction;
  private IReferencePropertyDescriptor entityRefQueryDescriptor;

  /**
   * Constructs a new <code>LovAction</code> instance.
   */
  public LovAction() {
    setName("lov.name");
    setDescription("lov.description");
    setIconImageURL("classpath:images/find-48x48.png");
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
    IReferencePropertyDescriptor erqDescriptor = getEntityRefQueryDescriptor(context);
    IView<ULCComponent> lovView = getViewFactory(context).createView(
        lovViewDescriptorFactory.createLovViewDescriptor(erqDescriptor),
        actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, lovView);
    context.put(ActionContextConstants.ENTITY_REF_DESCRIPTOR, erqDescriptor);
    actionHandler.execute(createQueryEntityAction, context);
    IValueConnector queryEntityConnector = (IValueConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR);
    getMvcBinder(context).bind(lovView.getConnector(), queryEntityConnector);
    findAction.putInitialContext(ActionContextConstants.QUERY_MODEL_CONNECTOR,
        queryEntityConnector);
    Object queryPropertyValue = context
        .get(ActionContextConstants.ACTION_COMMAND);
    if (queryPropertyValue != null && !queryPropertyValue.equals("%")) {
      actionHandler.execute(findAction, context);
      IQueryEntity queryEntity = (IQueryEntity) queryEntityConnector
          .getConnectorValue();
      if (queryEntity.getQueriedEntities() != null
          && queryEntity.getQueriedEntities().size() == 1) {
        IEntity selectedEntity = ((IController) actionHandler).merge(
            queryEntity.getQueriedEntities().get(0), MergeMode.MERGE_KEEP);
        getViewConnector(context).setConnectorValue(selectedEntity);
        return true;
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the lovViewDescriptorFactory.
   * 
   * @param lovViewDescriptorFactory
   *          the lovViewDescriptorFactory to set.
   */
  public void setLovViewDescriptorFactory(
      ILovViewDescriptorFactory lovViewDescriptorFactory) {
    this.lovViewDescriptorFactory = lovViewDescriptorFactory;
  }

  /**
   * Sets the createQueryEntityAction.
   * 
   * @param createQueryEntityAction
   *          the createQueryEntityAction to set.
   */
  public void setCreateQueryEntityAction(
      CreateQueryEntityAction createQueryEntityAction) {
    this.createQueryEntityAction = createQueryEntityAction;
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(AbstractChainedAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the findAction.
   * 
   * @param findAction
   *          the findAction to set.
   */
  public void setFindAction(IDisplayableAction findAction) {
    this.findAction = findAction;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(AbstractChainedAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Gets the entityRefQueryDescriptor.
   * 
   * @param context
   *          the action context.
   * @return the entityRefQueryDescriptor.
   */
  protected IReferencePropertyDescriptor getEntityRefQueryDescriptor(
      Map<String, Object> context) {
    if (entityRefQueryDescriptor != null) {
      return entityRefQueryDescriptor;
    }
    IModelDescriptor modelDescriptor = (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
    if (modelDescriptor instanceof IReferencePropertyDescriptor) {
      return (IReferencePropertyDescriptor) modelDescriptor;
    }
    return null;
  }

  /**
   * Sets the entityRefQueryDescriptor.
   * 
   * @param entityRefQueryDescriptor
   *          the entityRefQueryDescriptor to set.
   */
  public void setEntityRefQueryDescriptor(
      IReferencePropertyDescriptor entityRefQueryDescriptor) {
    this.entityRefQueryDescriptor = entityRefQueryDescriptor;
  }

}
