/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.bean.BeanConnector;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.IQueryEntity;

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

  /**
   * Creates a query entity using the model descriptor passed in the context.
   * The action result contains the model connector holding the created query
   * entity with the key <code>ActionContextConstants.MODEL_CONNECTOR</code>.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    IEntityDescriptor queryEntityDescriptor;
    if (getModelDescriptor() instanceof IReferencePropertyDescriptor) {
      queryEntityDescriptor = (IEntityDescriptor) ((IReferencePropertyDescriptor) getModelDescriptor())
          .getReferencedDescriptor();
    } else {
      queryEntityDescriptor = (IEntityDescriptor) getModelDescriptor();
    }
    IQueryEntity queryEntity = getEntityFactory().createQueryEntityInstance(
        queryEntityDescriptor.getComponentContract());
    BeanConnector modelConnector = getBeanConnectorFactory()
        .createBeanConnector("lovQueryEntity", queryEntity.getClass());
    modelConnector.setConnectorValue(queryEntity);
    Object queryPropertyValue = getContext().get(
        ActionContextConstants.ACTION_PARAM);
    if (queryPropertyValue != null && !queryPropertyValue.equals("%")) {
      modelConnector.getChildConnector(
          queryEntityDescriptor.getToStringProperty()).setConnectorValue(
          queryPropertyValue);
    }
    Map<String, Object> result = new HashMap<String, Object>();
    result.put(ActionContextConstants.MODEL_CONNECTOR, modelConnector);
    return result;
  }

}
