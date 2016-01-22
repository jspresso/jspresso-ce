/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptorFactory;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a query component to be used in filters or list of values. The
 * created query component is stored in the context under the key
 * {@code IQueryComponent.QUERY_COMPONENT}. Further explanations are given
 * about query components in the {@code QueryEntitiesAction} documentation.
 *
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

  private static final Logger              LOG                      = LoggerFactory
                                                                        .getLogger(CreateQueryComponentAction.class);


  /**
   * Constructs a new {@code CreateQueryComponentAction} instance.
   */
  public CreateQueryComponentAction() {
    queryComponentDescriptorFactory = new BasicQueryComponentDescriptorFactory();
  }

  /**
   * Creates a query component using the model descriptor passed in the context.
   * The action result contains the model connector holding the created query
   * entity with the key {@code QUERY_MODEL_CONNECTOR}.
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
      getEntityFactory(context).applyInitializationMapping(queryComponent, queryComponent.getQueryDescriptor(),
          masterComponent, initializationMapping);
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
