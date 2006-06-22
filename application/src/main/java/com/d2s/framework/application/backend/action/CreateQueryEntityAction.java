/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.IConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.bean.BeanConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.util.access.IAccessorFactory;
import com.d2s.framework.util.bean.IBeanProvider;

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
  @SuppressWarnings("unchecked")
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    IReferencePropertyDescriptor erqDescriptor = (IReferencePropertyDescriptor) context
        .get(ActionContextConstants.ENTITY_REF_DESCRIPTOR);
    IModelDescriptor modelDescriptor = (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
    if (erqDescriptor == null) {
      if (modelDescriptor instanceof IReferencePropertyDescriptor) {
        erqDescriptor = (IReferencePropertyDescriptor) modelDescriptor;
      }
    }
    IQueryEntity queryEntity = getEntityFactory(context)
        .createQueryEntityInstance(
            (Class<? extends IQueryEntity>) erqDescriptor
                .getReferencedDescriptor().getComponentContract());

    Map<String, String> initializationMapping = erqDescriptor
        .getInitializationMapping();
    if (initializationMapping != null) {
      IEntity masterEntity = null;
      // The following relies on a workaround used to determine the bean
      // model whenever the lov component is used inside a jtable.
      IConnector parentModelConnector = ((IValueConnector) context
          .get(ActionContextConstants.VIEW_CONNECTOR)).getParentConnector()
          .getModelConnector();
      if (parentModelConnector instanceof IBeanProvider) {
        masterEntity = (IEntity) ((IBeanProvider) parentModelConnector)
            .getBean();
      } else if (parentModelConnector instanceof ICollectionConnector) {
        int collectionIndex = ((ICollectionConnector) ((IValueConnector) context
            .get(ActionContextConstants.VIEW_CONNECTOR)).getParentConnector())
            .getSelectedIndices()[0];
        masterEntity = (IEntity) ((ICollectionConnector) parentModelConnector)
            .getChildConnector(collectionIndex).getConnectorValue();
      }
      if (masterEntity != null) {
        IAccessorFactory accessorFactory = getAccessorFactory(context);
        for (Map.Entry<String, String> initializedAttribute : initializationMapping
            .entrySet()) {
          try {
            accessorFactory.createPropertyAccessor(
                initializedAttribute.getKey(), queryEntity.getContract())
                .setValue(
                    queryEntity,
                    accessorFactory.createPropertyAccessor(
                        initializedAttribute.getValue(),
                        masterEntity.getContract()).getValue(masterEntity));
          } catch (IllegalAccessException ex) {
            throw new ActionException(ex);
          } catch (InvocationTargetException ex) {
            throw new ActionException(ex);
          } catch (NoSuchMethodException ex) {
            throw new ActionException(ex);
          }
        }
      }
    }
    BeanConnector modelConnector = getBeanConnectorFactory(context)
        .createBeanConnector("lovQueryEntity", queryEntity.getClass());
    modelConnector.setConnectorValue(queryEntity);
    Object queryPropertyValue = context
        .get(ActionContextConstants.ACTION_PARAM);
    if (queryPropertyValue != null && !queryPropertyValue.equals("%")) {
      modelConnector.getChildConnector(
          erqDescriptor.getComponentDescriptor().getToStringProperty())
          .setConnectorValue(queryPropertyValue);
    }
    context.put(ActionContextConstants.QUERY_MODEL_CONNECTOR, modelConnector);
    return true;
  }
}
