/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.MissingPropertyException;

/**
 * Creates a query component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CreateQueryComponentAction extends BackendAction {

  /**
   * A parametrized entity reference descriptor.
   */
  public static final String COMPONENT_REF_DESCRIPTOR = "COMPONENT_REF_DESCRIPTOR";

  /**
   * the connector of the query model.
   */
  public static final String QUERY_MODEL_CONNECTOR    = "QUERY_MODEL_CONNECTOR";

  /**
   * Creates a query component using the model descriptor passed in the context.
   * The action result contains the model connector holding the created query
   * entity with the key <code>ActionContextConstants.MODEL_CONNECTOR</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IReferencePropertyDescriptor erqDescriptor = (IReferencePropertyDescriptor) context
        .get(COMPONENT_REF_DESCRIPTOR);
    if (erqDescriptor == null) {
      IModelDescriptor modelDescriptor = getModelDescriptor(context);
      erqDescriptor = (IReferencePropertyDescriptor) modelDescriptor;
    }
    IQueryComponent queryComponent = getEntityFactory(context)
        .createQueryComponentInstance(
            erqDescriptor.getReferencedDescriptor().getQueryComponentContract());
    queryComponent.setPageSize(erqDescriptor.getPageSize());

    completeQueryComponent(queryComponent, erqDescriptor, context);
    ModelRefPropertyConnector modelConnector = (ModelRefPropertyConnector) getController(
        context).createModelConnector(
        ACTION_MODEL_NAME,
        new BasicQueryComponentDescriptor(erqDescriptor
            .getReferencedDescriptor()));
    context.put(QUERY_MODEL_CONNECTOR, modelConnector);
    modelConnector.setConnectorValue(queryComponent);
    String queryPropertyValue = getActionCommand(context);
    if (queryPropertyValue != null && !queryPropertyValue.equals("*")) {
      String propertyName = (String) getActionParameter(context);
      if (propertyName != null) {
        modelConnector.getChildConnector(propertyName).setConnectorValue(
            queryPropertyValue);
      } else {
        modelConnector.getChildConnector(
            erqDescriptor.getComponentDescriptor().getToStringProperty())
            .setConnectorValue(queryPropertyValue);
      }
    }
    context.put(IQueryComponent.QUERY_COMPONENT, queryComponent);
    return super.execute(actionHandler, context);
  }

  /**
   * Completes the query component before passing it along the chain. This
   * default implementation handles the reference property descriptor
   * initialization mapping.
   * 
   * @param queryComponent
   *          the query component.
   * @param erqDescriptor
   *          the reference property descriptor from which the initialization
   *          mappng is taken out.
   * @param context
   *          the action context.
   */
  protected void completeQueryComponent(IQueryComponent queryComponent,
      IReferencePropertyDescriptor<?> erqDescriptor, Map<String, Object> context) {
    Map<String, Object> initializationMapping = erqDescriptor
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
        for (Map.Entry<String, Object> initializedAttribute : initializationMapping
            .entrySet()) {
          IAccessor qCompAccessor = accessorFactory.createPropertyAccessor(
              initializedAttribute.getKey(), queryComponent.getQueryContract());
          try {
            Object initValue;
            if (initializedAttribute.getValue() instanceof String) {
              try {
                IAccessor masterAccessor = accessorFactory
                    .createPropertyAccessor((String) initializedAttribute
                        .getValue(), masterEntity.getComponentContract());
                initValue = masterAccessor.getValue(masterEntity);
              } catch (MissingPropertyException ex) {
                // the value in the initialization mapping is not a property.
                // Handle it as a constant value.
                initValue = initializedAttribute.getValue();
              }
            } else {
              initValue = initializedAttribute.getValue();
            }
            qCompAccessor.setValue(queryComponent, initValue);
          } catch (IllegalAccessException ex) {
            throw new ActionException(ex);
          } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof RuntimeException) {
              throw (RuntimeException) ex.getCause();
            }
            throw new ActionException(ex.getCause());
          } catch (NoSuchMethodException ex) {
            throw new ActionException(ex);
          }
        }
      }
    }
  }
}
