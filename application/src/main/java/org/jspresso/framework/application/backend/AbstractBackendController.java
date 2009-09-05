/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.AbstractController;
import org.jspresso.framework.application.backend.entity.ApplicationSessionAwareProxyEntityFactory;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.backend.session.basic.BasicApplicationSession;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.model.descriptor.WorkspaceDescriptor;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * Base class for backend application controllers. It provides the implementor
 * with commonly used accessors as well as a reference to the root model
 * descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendController extends AbstractController
    implements IBackendController {

  private IApplicationSession                              applicationSession;
  private IEntityFactory                                   entityFactory;
  private IModelConnectorFactory                           modelConnectorFactory;
  private ComponentTransferStructure<? extends IComponent> transferStructure;

  private Map<String, IValueConnector>                     workspaceConnectors;

  /**
   * {@inheritDoc}
   */
  public void checkWorkspaceAccess(String workspaceName) {
    checkAccess((ISecurable) getWorkspaceConnector(workspaceName)
        .getConnectorValue());
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor) {
    return modelConnectorFactory.createModelConnector(id, modelDescriptor,
        getApplicationSession().getSubject());
  }

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    SecurityHelper.checkAccess(getApplicationSession().getSubject(), action,
        getTranslationProvider(), getLocale());
    Map<String, Object> actionContext = getInitialActionContext();
    if (context != null) {
      context.putAll(actionContext);
    }
    return action.execute(this, context);
  }

  /**
   * {@inheritDoc}
   */
  public IAccessorFactory getAccessorFactory() {
    return modelConnectorFactory.getAccessorFactory();
  }

  /**
   * {@inheritDoc}
   */
  public IApplicationSession getApplicationSession() {
    return applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  public IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Contains the current application session.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<String, Object>();
    initialActionContext.put(ActionContextConstants.BACK_CONTROLLER, this);
    return initialActionContext;
  }

  /**
   * Gets the locale used by this controller. The locale is actually held by the
   * session.
   * 
   * @return locale used by this controller.
   */
  public Locale getLocale() {
    return applicationSession.getLocale();
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getWorkspaceConnector(String workspaceName) {
    return workspaceConnectors.get(workspaceName);
  }

  /**
   * Sets the model controller workspaces. These workspaces are not kept as-is.
   * Their connectors are.
   * 
   * @param workspaces
   *          A map containing the workspaces indexed by a well-known key used
   *          to bind them with their views.
   */
  public void installWorkspaces(Map<String, Workspace> workspaces) {
    workspaceConnectors = new HashMap<String, IValueConnector>();
    for (Map.Entry<String, Workspace> workspaceEntry : workspaces.entrySet()) {
      IModelDescriptor workspaceDescriptor;
      workspaceDescriptor = WorkspaceDescriptor.WORKSPACE_DESCRIPTOR;
      IValueConnector nextWorkspaceConnector = modelConnectorFactory
          .createModelConnector(workspaceEntry.getKey(), workspaceDescriptor,
              getApplicationSession().getSubject());
      nextWorkspaceConnector.setConnectorValue(workspaceEntry.getValue());
      workspaceConnectors.put(workspaceEntry.getKey(), nextWorkspaceConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  public ComponentTransferStructure<? extends IComponent> retrieveComponents() {
    return transferStructure;
  }

  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession
   *          the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    if (!(applicationSession instanceof BasicApplicationSession)) {
      throw new IllegalArgumentException(
          "applicationSession must be a BasicApplicationSession.");
    }
    this.applicationSession = applicationSession;
    linkSessionArtifacts();
  }

  /**
   * Sets the entityFactory.
   * 
   * @param entityFactory
   *          the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    if (!(entityFactory instanceof ApplicationSessionAwareProxyEntityFactory)) {
      throw new IllegalArgumentException(
          "entityFactory must be an ApplicationSessionAwareProxyEntityFactory.");
    }
    this.entityFactory = entityFactory;
    linkSessionArtifacts();
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *          the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * {@inheritDoc}
   */
  public boolean start(Locale startingLocale) {
    applicationSession.setLocale(startingLocale);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void storeComponents(
      ComponentTransferStructure<? extends IComponent> components) {
    this.transferStructure = components;
  }

  private void linkSessionArtifacts() {
    if (getApplicationSession() != null && getEntityFactory() != null) {
      ((ApplicationSessionAwareProxyEntityFactory) getEntityFactory())
          .setApplicationSession(getApplicationSession());
      ((BasicApplicationSession) getApplicationSession())
          .setEntityFactory(getEntityFactory());
    }
  }
}
