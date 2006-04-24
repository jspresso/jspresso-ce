/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Map;

import com.d2s.framework.binding.bean.BeanConnector;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;

/**
 * Creates a query entity.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CreateQueryEntityAction extends AbstractBackendAction {

  private IEntityDescriptor queryEntityDescriptor;

  /**
   * Creates a query entity using the model descriptor passed in the context.
   * The action result contains the model connector holding the created query
   * entity with the key <code>ActionContextConstants.MODEL_CONNECTOR</code>.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    IQueryEntity queryEntity = getEntityFactory(context)
        .createQueryEntityInstance(
            (Class<? extends IQueryEntity>) queryEntityDescriptor
                .getComponentContract());
    BeanConnector modelConnector = getBeanConnectorFactory(context)
        .createBeanConnector("lovQueryEntity", queryEntity.getClass());
    modelConnector.setConnectorValue(queryEntity);
    Object queryPropertyValue = context
        .get(ActionContextConstants.ACTION_PARAM);
    if (queryPropertyValue != null && !queryPropertyValue.equals("%")) {
      modelConnector.getChildConnector(
          queryEntityDescriptor.getToStringProperty()).setConnectorValue(
          queryPropertyValue);
    }
    context.put(ActionContextConstants.QUERY_MODEL_CONNECTOR, modelConnector);
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
