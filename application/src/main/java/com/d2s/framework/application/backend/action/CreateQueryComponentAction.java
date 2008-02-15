/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
import com.d2s.framework.binding.model.ModelRefPropertyConnector;
import com.d2s.framework.model.IModelProvider;
import com.d2s.framework.model.component.IQueryComponent;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicQueryComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * Creates a query component.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CreateQueryComponentAction extends AbstractBackendAction {

  /**
   * Creates a query component using the model descriptor passed in the context.
   * The action result contains the model connector holding the created query
   * entity with the key <code>ActionContextConstants.MODEL_CONNECTOR</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    IReferencePropertyDescriptor erqDescriptor = (IReferencePropertyDescriptor) context
        .get(ActionContextConstants.COMPONENT_REF_DESCRIPTOR);
    IModelDescriptor modelDescriptor = (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
    if (erqDescriptor == null) {
      erqDescriptor = (IReferencePropertyDescriptor) modelDescriptor;
    }
    IQueryComponent queryComponent = getEntityFactory(context)
        .createQueryComponentInstance(
            erqDescriptor.getReferencedDescriptor().getComponentContract());

    Map<String, String> initializationMapping = erqDescriptor
        .getInitializationMapping();
    if (initializationMapping != null) {
      IEntity masterEntity = null;
      // The following relies on a workaround used to determine the bean
      // model whenever the lov component is used inside a jtable.
      IConnector parentModelConnector = ((IValueConnector) context
          .get(ActionContextConstants.VIEW_CONNECTOR)).getParentConnector()
          .getModelConnector();
      if (parentModelConnector instanceof IModelProvider) {
        masterEntity = (IEntity) ((IModelProvider) parentModelConnector)
            .getModel();
      } else if (parentModelConnector instanceof ICollectionConnector) {
        int collectionIndex = ((ICollectionConnector) ((IValueConnector) context
            .get(ActionContextConstants.VIEW_CONNECTOR)).getParentConnector())
            .getSelectedIndices()[0];
        masterEntity = (IEntity) ((ICollectionConnector) parentModelConnector)
            .getChildConnector(collectionIndex).getConnectorValue();
      }
      if (masterEntity != null) {
        IAccessorFactory accessorFactory = getAccessorFactory(masterEntity,
            context);
        for (Map.Entry<String, String> initializedAttribute : initializationMapping
            .entrySet()) {
          try {
            accessorFactory.createPropertyAccessor(
                initializedAttribute.getKey(), queryComponent.getContract())
                .setValue(
                    queryComponent,
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
    ModelRefPropertyConnector modelConnector = (ModelRefPropertyConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR);
    if (modelConnector == null) {
      modelConnector = (ModelRefPropertyConnector) getBeanConnectorFactory(context)
          .createModelConnector(
              ACTION_MODEL_CONNECTOR_ID,
              new BasicQueryComponentDescriptor(erqDescriptor
                  .getReferencedDescriptor(), queryComponent.getClass()));
      context.put(ActionContextConstants.QUERY_MODEL_CONNECTOR, modelConnector);
    }
    modelConnector.setConnectorValue(queryComponent);
    Object queryPropertyValue = context
        .get(ActionContextConstants.ACTION_COMMAND);
    if (queryPropertyValue != null && !queryPropertyValue.equals("*")) {
      modelConnector.getChildConnector(
          erqDescriptor.getComponentDescriptor().getToStringProperty())
          .setConnectorValue(queryPropertyValue);
    }
    return super.execute(actionHandler, context);
  }
}
