/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptorFactory;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.MissingPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a query component to be used in filters or list of values. The
 * created query component is stored in the context under the key
 * <code>IQueryComponent.QUERY_COMPONENT</code>. Further explanations are given
 * about query components in the <code>QueryEntitiesAction</code> documentation.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CreateQueryComponentAction extends BackendAction {

  /**
   * A parametrized entity reference descriptor.
   */
  public static final String               COMPONENT_REF_DESCRIPTOR = "COMPONENT_REF_DESCRIPTOR";

  /**
   * The master component key from which the LOV has been triggered.
   */
  public static final String               MASTER_COMPONENT         = "MASTER_COMPONENT";

  /**
   * The connector of the query model.
   */
  public static final String               QUERY_MODEL_CONNECTOR    = "QUERY_MODEL_CONNECTOR";

  private IQueryComponentDescriptorFactory queryComponentDescriptorFactory;

  private IQueryComponentRefiner           queryComponentRefiner;

  private static final String              SESSION_PROPERTY_PREFIX  = "session.";

  private static final Logger              LOG                      = LoggerFactory
                                                                        .getLogger(CreateQueryComponentAction.class);
  
  
  /**
   * Constructs a new <code>CreateQueryComponentAction</code> instance.
   */
  public CreateQueryComponentAction() {
    queryComponentDescriptorFactory = new BasicQueryComponentDescriptorFactory();
  }

  /**
   * Creates a query component using the model descriptor passed in the context.
   * The action result contains the model connector holding the created query
   * entity with the key <code>QUERY_MODEL_CONNECTOR</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IReferencePropertyDescriptor<IComponent> erqDescriptor = (IReferencePropertyDescriptor<IComponent>) context
        .get(COMPONENT_REF_DESCRIPTOR);
    if (erqDescriptor == null) {
      IModelDescriptor modelDescriptor = getModelDescriptor(context);
      erqDescriptor = (IReferencePropertyDescriptor<IComponent>) modelDescriptor;
    }
    Class<? extends IComponent> queriedContract;
    if (erqDescriptor.getReferencedDescriptor() instanceof IQueryComponentDescriptor) {
      queriedContract = ((IQueryComponentDescriptor) erqDescriptor
          .getReferencedDescriptor()).getQueriedComponentsDescriptor()
          .getComponentContract();
    } else {
      queriedContract = erqDescriptor.getReferencedDescriptor()
          .getComponentContract();
    }
    IQueryComponent queryComponent = getEntityFactory(context)
        .createQueryComponentInstance(queriedContract);
    queryComponent.setPageSize(erqDescriptor.getPageSize());

    completeQueryComponent(queryComponent, erqDescriptor, context);
    ModelRefPropertyConnector modelConnector = (ModelRefPropertyConnector) getController(
        context).createModelConnector(
        ACTION_MODEL_NAME,
        getQueryComponentDescriptorFactory().createQueryComponentDescriptor(
            erqDescriptor));
    context.put(QUERY_MODEL_CONNECTOR, modelConnector);
    modelConnector.setConnectorValue(queryComponent);
    String queryPropertyValue = getActionCommand(context);
    if (queryPropertyValue != null && !queryPropertyValue.equals("*")
        && queryPropertyValue.length() > 0) {
      modelConnector.getChildConnector(
          erqDescriptor.getComponentDescriptor().getAutoCompleteProperty())
          .setConnectorValue(queryPropertyValue);
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
   *          mapping is taken out.
   * @param context
   *          the action context.
   */
  @SuppressWarnings("ConstantConditions")
  protected void completeQueryComponent(IQueryComponent queryComponent,
      IReferencePropertyDescriptor<?> erqDescriptor, Map<String, Object> context) {

    LOG.debug("Completing query component from initialization mapping and refiner...");

    Object masterComponent = context.get(MASTER_COMPONENT);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Retrieved master component from context : " + masterComponent);
    }

    Map<String, Object> initializationMapping = erqDescriptor
        .getInitializationMapping();
    if (initializationMapping != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("initializationMapping : " + initializationMapping);
      }
      IAccessorFactory accessorFactory = getAccessorFactory(context);
      for (Map.Entry<String, Object> initializedAttribute : initializationMapping
          .entrySet()) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("initializing property : " + initializedAttribute.getKey()
              + " from " + initializedAttribute.getKey());
        }
        IAccessor qCompAccessor = accessorFactory.createPropertyAccessor(
            initializedAttribute.getKey(), queryComponent.getQueryContract());
        try {
          Object initValue;
          if (initializedAttribute.getValue() instanceof String
              && ((String) initializedAttribute.getValue())
                  .startsWith(SESSION_PROPERTY_PREFIX)) {
            String sessionProperty = ((String) initializedAttribute.getValue())
                .substring(SESSION_PROPERTY_PREFIX.length());
            IApplicationSession applicationSession = getApplicationSession(context);
            if (applicationSession != null) {
              Class<?> sessionContract = applicationSession.getClass();
              try {
                IAccessor sessionAccessor = accessorFactory
                    .createPropertyAccessor(sessionProperty, sessionContract);
                initValue = sessionAccessor.getValue(masterComponent);
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Session contract : " + sessionContract.getName());
                  LOG.debug("Init value computed from session : " + initValue);
                }
              } catch (MissingPropertyException ex) {
                // the value in the initialization mapping is not a session
                // value. Handle it as null.
                initValue = null;
                if (LOG.isDebugEnabled()) {
                  LOG.debug(
                      "Init value '{}' not found on application session. Assigning null.",
                      sessionProperty);
                }
              }
            } else {
              initValue = null;
              if (LOG.isDebugEnabled()) {
                LOG.debug("Application session is null. Assigning null.");
              }
            }
          } else if (masterComponent != null
              && initializedAttribute.getValue() instanceof String) {
            Class<?> masterComponentContract;
            if (masterComponent instanceof IComponent) {
              masterComponentContract = ((IComponent) masterComponent)
                  .getComponentContract();
            } else if (masterComponent instanceof IQueryComponent) {
              masterComponentContract = ((IQueryComponent) masterComponent)
                  .getQueryContract();
            } else {
              masterComponentContract = masterComponent.getClass();
            }
            try {
              IAccessor masterAccessor = accessorFactory
                  .createPropertyAccessor(
                      (String) initializedAttribute.getValue(),
                      masterComponentContract);
              initValue = masterAccessor.getValue(masterComponent);
              if (LOG.isDebugEnabled()) {
                LOG.debug("Master component contract : "
                    + masterComponentContract.getName());
                LOG.debug("Init value computed from master component : "
                    + initValue);
              }
            } catch (MissingPropertyException ex) {
              // the value in the initialization mapping is not a property.
              // Handle it as a constant value.
              initValue = initializedAttribute.getValue();
              if (LOG.isDebugEnabled()) {
                LOG.debug("Init value computed from static value : "
                    + initValue);
              }
            }
          } else {
            initValue = initializedAttribute.getValue();
            if (LOG.isDebugEnabled()) {
              LOG.debug("Init value computed from static value : " + initValue);
            }
          }
          if (initValue != null) {
            if (initValue instanceof String
                && (((String) initValue).endsWith("null") || ((String) initValue)
                    .endsWith(IQueryComponent.NULL_VAL))) {
              if (((String) initValue).startsWith(IQueryComponent.NOT_VAL)) {
                initValue = IQueryComponent.NULL_VAL + IQueryComponent.NULL_VAL;
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Init value set to not null");
                }
              } else {
                initValue = IQueryComponent.NULL_VAL;
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Init value set to null");
                }
              }
            } else {
              IPropertyDescriptor initializedPropertyDescriptor = queryComponent
                  .getQueryDescriptor().getPropertyDescriptor(
                      initializedAttribute.getKey());

              if (initializedPropertyDescriptor != null) {
                Class<?> expectedType = initializedPropertyDescriptor
                    .getModelType();
                Class<?> initValueType = initValue.getClass();
                if (!QueryComponent.class.isAssignableFrom(initValueType)
                    && !expectedType.isAssignableFrom(initValueType)) {
                  if (Boolean.TYPE.equals(expectedType)) {
                    expectedType = Boolean.class;
                  }
                  if (LOG.isDebugEnabled()) {
                    LOG.debug("Init value needs to be refined to match expected type : "
                        + expectedType.getName());
                  }
                  try {
                    initValue = expectedType.getConstructor(new Class<?>[] {
                      String.class
                    }).newInstance(initValue.toString());
                    // Whenever an exception occurs, just try to set it
                    // normally
                    // though.
                    if (LOG.isDebugEnabled()) {
                      LOG.debug("Refined init value : " + initValue);
                    }
                  } catch (IllegalArgumentException ex) {
                    // throw new NestedRuntimeException(ex,
                    // "Invalid initialization mapping for property "
                    // + initializedAttribute.getKey());
                  } catch (SecurityException ex) {
                    // throw new NestedRuntimeException(ex,
                    // "Invalid initialization mapping for property "
                    // + initializedAttribute.getKey());
                  } catch (InstantiationException ex) {
                    // throw new NestedRuntimeException(ex,
                    // "Invalid initialization mapping for property "
                    // + initializedAttribute.getKey());
                  }
                }
              }
            }
            // } else {
            // initValue = IQueryComponent.NULL_VAL;
            // if (LOG.isDebugEnabled()) {
            // LOG.debug("Init value set to null");
            // }
          }
          qCompAccessor.setValue(queryComponent, initValue);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Init value assigned.");
          }
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
    if (queryComponentRefiner != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Refining query component...");
      }
      queryComponentRefiner.refineQueryComponent(queryComponent, context);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Query component refined.");
      }
    }
    LOG.debug("Completed query component from initialization mapping and refiner.");
  }

  /**
   * Gets the queryComponentDescriptorFactory.
   * 
   * @return the queryComponentDescriptorFactory.
   */
  protected IQueryComponentDescriptorFactory getQueryComponentDescriptorFactory() {
    return queryComponentDescriptorFactory;
  }

  /**
   * Sets the queryComponentDescriptorFactory.
   * 
   * @param queryComponentDescriptorFactory
   *          the queryComponentDescriptorFactory to set.
   */
  public void setQueryComponentDescriptorFactory(
      IQueryComponentDescriptorFactory queryComponentDescriptorFactory) {
    this.queryComponentDescriptorFactory = queryComponentDescriptorFactory;
  }

  /**
   * Sets the queryComponentRefiner.
   * 
   * @param queryComponentRefiner
   *          the queryComponentRefiner to set.
   */
  public void setQueryComponentRefiner(
      IQueryComponentRefiner queryComponentRefiner) {
    this.queryComponentRefiner = queryComponentRefiner;
  }
}
