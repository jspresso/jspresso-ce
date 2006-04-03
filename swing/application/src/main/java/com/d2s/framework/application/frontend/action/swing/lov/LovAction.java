/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.lov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import com.d2s.framework.application.IController;
import com.d2s.framework.application.backend.action.CreateQueryEntityAction;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.frontend.action.swing.flow.ModalDialogAction;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.view.ILovViewFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.action.IDisplayableAction;

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

  private ILovViewFactory<JComponent> lovViewFactory;
  private CreateQueryEntityAction     createQueryEntityAction;
  private IDisplayableAction          okAction;
  private IDisplayableAction          cancelAction;
  private IDisplayableAction          findAction;
  private IEntityDescriptor           queryEntityDescriptor;

  /**
   * Constructs a new <code>LovAction</code> instance.
   */
  public LovAction() {
    setName("LOV_NAME");
    setDescription("LOV_DESCRIPTION");
    setIconImageURL("classpath:images/find-48x48.png");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();
    Map<String, Object> okInitialContext = new HashMap<String, Object>();
    getViewConnector()
        .setConnectorValue(getViewConnector().getConnectorValue());
    okInitialContext.put(ActionContextConstants.SOURCE_VIEW_CONNECTOR,
        getViewConnector());
    okInitialContext.put(ActionContextConstants.SOURCE_MODEL_DESCRIPTOR,
        getContext().get(ActionContextConstants.MODEL_DESCRIPTOR));
    okAction.setContext(okInitialContext);
    actions.add(findAction);
    actions.add(okAction);
    actions.add(cancelAction);
    setActions(actions);
    IView<JComponent> lovView = lovViewFactory.createLovView(
        getQueryEntityDescriptor(), actionHandler, getLocale());
    setMainView(lovView);
    createQueryEntityAction.setContext(getContext());
    createQueryEntityAction
        .setQueryEntityDescriptor(getQueryEntityDescriptor());
    Map<String, Object> queryEntityCreationResult = actionHandler
        .execute(createQueryEntityAction);
    IValueConnector queryEntityConnector = (IValueConnector) queryEntityCreationResult
        .get(ActionContextConstants.MODEL_CONNECTOR);
    getMvcBinder().bind(lovView.getConnector(), queryEntityConnector);
    Object queryPropertyValue = getContext().get(
        ActionContextConstants.ACTION_PARAM);
    if (queryPropertyValue != null && !queryPropertyValue.equals("%")) {
      findAction.setContext(queryEntityCreationResult);
      actionHandler.execute(findAction);
      IQueryEntity queryEntity = (IQueryEntity) queryEntityConnector
          .getConnectorValue();
      if (queryEntity.getQueriedEntities() != null
          && queryEntity.getQueriedEntities().size() == 1) {
        IEntity selectedEntity = ((IController) actionHandler).merge(
            queryEntity.getQueriedEntities().get(0), MergeMode.MERGE_KEEP);
        getViewConnector().setConnectorValue(selectedEntity);
        return null;
      }
    }
    return super.execute(actionHandler);
  }

  /**
   * Sets the lovViewFactory.
   * 
   * @param lovViewFactory
   *          the lovViewFactory to set.
   */
  public void setLovViewFactory(ILovViewFactory<JComponent> lovViewFactory) {
    this.lovViewFactory = lovViewFactory;
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
   * Gets the queryEntityDescriptor.
   * 
   * @return the queryEntityDescriptor.
   */
  protected IEntityDescriptor getQueryEntityDescriptor() {
    if (queryEntityDescriptor != null) {
      return queryEntityDescriptor;
    }
    IModelDescriptor modelDescriptor = (IModelDescriptor) getContext().get(
        ActionContextConstants.MODEL_DESCRIPTOR);
    if (modelDescriptor instanceof IReferencePropertyDescriptor) {
      return (IEntityDescriptor) ((IReferencePropertyDescriptor) modelDescriptor)
          .getReferencedDescriptor();
    } else if (modelDescriptor instanceof IEntityDescriptor) {
      return (IEntityDescriptor) modelDescriptor;
    }
    return null;
  }

  /**
   * Sets the queryEntityDescriptor.
   * 
   * @param queryEntityDescriptor
   *          the queryEntityDescriptor to set.
   */
  public void setQueryEntityDescriptor(IEntityDescriptor queryEntityDescriptor) {
    this.queryEntityDescriptor = queryEntityDescriptor;
  }
}
