/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.accessor.IAccessorFactory;


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
            erqDescriptor.getReferencedDescriptor().getQueryComponentContract());

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
        IAccessorFactory accessorFactory = getAccessorFactory(context);
        for (Map.Entry<String, String> initializedAttribute : initializationMapping
            .entrySet()) {
          try {
            accessorFactory.createPropertyAccessor(
                initializedAttribute.getKey(),
                queryComponent.getQueryContract()).setValue(
                queryComponent,
                accessorFactory
                    .createPropertyAccessor(initializedAttribute.getValue(),
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
      modelConnector = (ModelRefPropertyConnector) getController(context)
          .createModelConnector(
              ACTION_MODEL_NAME,
              new BasicQueryComponentDescriptor(erqDescriptor
                  .getReferencedDescriptor()));
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
