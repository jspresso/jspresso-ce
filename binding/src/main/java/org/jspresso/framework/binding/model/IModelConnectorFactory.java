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
package org.jspresso.framework.binding.model;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * Interface for all factories of model connectors.
 *
 * @author Vincent Vandenschrick
 */
public interface IModelConnectorFactory {

  /**
   * Creates a model connector based on a model type. It uses the descriptor
   * registry to locate the model descriptor based on its type.
   *
   * @param id
   *          the connector identifier.
   * @param componentContract
   *          the model type to create the connector for.
   * @param securityHandler
   *          the jspresso security handler used to manage security.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(String id, Class<?> componentContract,
      ISecurityHandler securityHandler);

  /**
   * Creates a model connector based on a model descriptor.
   *
   * @param id
   *          the connector identifier.
   * @param modelDescriptor
   *          the model descriptor to create the connector for.
   * @param securityHandler
   *          the jspresso security handler used to manage security.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor, ISecurityHandler securityHandler);

  /**
   * Gets the {@code IAccessorFactory} used.
   *
   * @return the used accessor factory
   */
  IAccessorFactory getAccessorFactory();

  /**
   * Gets the {@code IComponentDescriptorRegistry} used.
   *
   * @return the used descriptor registry
   */
  IComponentDescriptorRegistry getDescriptorRegistry();
}
