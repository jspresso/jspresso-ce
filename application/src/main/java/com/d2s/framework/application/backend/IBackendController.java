/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend;

import java.util.Locale;
import java.util.Map;

import com.d2s.framework.application.IController;
import com.d2s.framework.application.model.Workspace;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.datatransfer.ComponentTransferStructure;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * This interface establishes the contract of the backend controllers. Backend
 * controllers are controllers which act on the application domain model (as
 * oposed to frontend controllers which act on the application view).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBackendController extends IController {

  /**
   * Checks authorization for workspace access. It shoud throw a SecurityException
   * whenever access should not be granted.
   * 
   * @param workspaceName
   *            the id of the workspace access to check.
   */
  void checkWorkspaceAccess(String workspaceName);

  /**
   * Creates a model connector out of a model descriptor. It should be either a
   * bean connector or a bean collection connector depending on the type of
   * model descriptor.
   * 
   * @param id
   *            the connector id.
   * @param modelDescriptor
   *            the model descriptor to create the connector for.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor);

  /**
   * Gets the beanAccessorFactory for this backend controller.
   * 
   * @return the beanAccessorFactory for this backend controller.
   */
  IAccessorFactory getBeanAccessorFactory();

  /**
   * Gets the beanConnectorFactory for this backend controller.
   * 
   * @return the beanConnectorFactory for this backend controller.
   */
  IModelConnectorFactory getBeanConnectorFactory();

  /**
   * Gets the entityFactory for this backend controller.
   * 
   * @return the entityFactory for this backend controller.
   */
  IEntityFactory getEntityFactory();

  /**
   * Gets the mapAccessorFactory for this backend controller.
   * 
   * @return the mapAccessorFactory for this backend controller.
   */
  IAccessorFactory getMapAccessorFactory();

  /**
   * Gets the mapConnectorFactory for this backend controller.
   * 
   * @return the mapConnectorFactory for this backend controller.
   */
  IModelConnectorFactory getMapConnectorFactory();

  /**
   * Given a workspace identifier, this method returns the composite connector used
   * as model connector for the associated workspace.
   * 
   * @param workspaceName
   *            the workspace identifier.
   * @return the associated workspace connector.
   */
  IValueConnector getWorkspaceConnector(String workspaceName);

  /**
   * Acts as a clipboard for retrieving previously stored component references
   * along with their descriptors.
   * 
   * @return components the component transfer structure to retrieve.
   */
  ComponentTransferStructure<? extends IComponent> retrieveComponents();

  /**
   * Asks this backend controller to perform any necessary action upon startup.
   * One of this action should be to construct the root connector based on the
   * root model descriptor.
   * 
   * @param locale
   *            the locale this backend controller should start with.
   * @return true if the controller successfully started.
   */
  boolean start(Locale locale);

  /**
   * Acts as a clipboard for storing component references along with their
   * descriptors.
   * 
   * @param components
   *            the component transfer structure to store.
   */
  void storeComponents(
      ComponentTransferStructure<? extends IComponent> components);

  /**
   * Installs the passed in workspaces into the backend controller.
   * 
   * @param workspaces
   *            the workspaces to install.
   */
  void installWorkspaces(Map<String, Workspace> workspaces);
}
