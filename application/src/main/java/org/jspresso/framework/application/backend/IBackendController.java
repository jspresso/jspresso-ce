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

import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.accessor.IAccessorFactory;


/**
 * This interface establishes the contract of the backend controllers. Backend
 * controllers are controllers which act on the application domain model (as
 * oposed to frontend controllers which act on the application view).
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
public interface IBackendController extends IController {

  /**
   * Checks authorization for workspace access. It shoud throw a
   * SecurityException whenever access should not be granted.
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
   * Gets the appropriate accessor factory based on the targetted object.
   * 
   * @return the approriate accessor factory.
   */
  IAccessorFactory getAccessorFactory();

  /**
   * Gets the entityFactory for this backend controller.
   * 
   * @return the entityFactory for this backend controller.
   */
  IEntityFactory getEntityFactory();

  /**
   * Given a workspace identifier, this method returns the composite connector
   * used as model connector for the associated workspace.
   * 
   * @param workspaceName
   *            the workspace identifier.
   * @return the associated workspace connector.
   */
  IValueConnector getWorkspaceConnector(String workspaceName);

  /**
   * Installs the passed in workspaces into the backend controller.
   * 
   * @param workspaces
   *            the workspaces to install.
   */
  void installWorkspaces(Map<String, Workspace> workspaces);

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
   * @param startingLocale
   *            the locale this backend controller should start with.
   * @return true if the controller successfully started.
   */
  boolean start(Locale startingLocale);

  /**
   * Acts as a clipboard for storing component references along with their
   * descriptors.
   * 
   * @param components
   *            the component transfer structure to store.
   */
  void storeComponents(
      ComponentTransferStructure<? extends IComponent> components);
}
