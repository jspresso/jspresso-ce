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

import org.jspresso.framework.binding.ConnectorBindingException;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.security.EAuthorization;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IGateAccessible;

/**
 * Default implementation for model connectors factory.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultModelConnectorFactory implements IModelConnectorFactory {

  private IAccessorFactory             accessorFactory;
  private IComponentDescriptorRegistry descriptorRegistry;

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createModelConnector(String id,
      Class<?> componentContract, ISecurityHandler securityHandler) {
    return createModelConnector(id, getDescriptorRegistry()
        .getComponentDescriptor(componentContract), securityHandler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor, ISecurityHandler securityHandler) {
    IValueConnector modelConnector = null;
    if (modelDescriptor instanceof IComponentDescriptor<?>) {
      modelConnector = new ModelConnector(id,
          (IComponentDescriptor<?>) modelDescriptor, this);
    } else if (modelDescriptor instanceof ICollectionDescriptor<?>) {
      modelConnector = new ModelCollectionConnector(id,
          (ICollectionDescriptor<?>) modelDescriptor, this);
    } else if (modelDescriptor instanceof IPropertyDescriptor) {
      if (modelDescriptor instanceof IReferencePropertyDescriptor<?>) {
        modelConnector = new ModelRefPropertyConnector(
            (IReferencePropertyDescriptor<?>) modelDescriptor, this);
      } else if (modelDescriptor instanceof ICollectionPropertyDescriptor<?>) {
        modelConnector = new ModelCollectionPropertyConnector(
            (ICollectionPropertyDescriptor<?>) modelDescriptor, this);
      } else if (modelDescriptor instanceof IScalarPropertyDescriptor) {
        if (modelDescriptor instanceof IIntegerPropertyDescriptor) {
          modelConnector = new ModelIntegerPropertyConnector(
              (IIntegerPropertyDescriptor) modelDescriptor, accessorFactory);
        } else {
          modelConnector = new ModelScalarPropertyConnector(
              (IScalarPropertyDescriptor) modelDescriptor, accessorFactory);
        }
      }
    }
    if (modelConnector == null) {
      throw new ConnectorBindingException("Could not create child connector ["
          + id + "].");
    }
    modelConnector.setSecurityHandler(securityHandler);
    if (modelDescriptor instanceof IGateAccessible) {
      boolean locallyWritable = !((IGateAccessible) modelDescriptor)
          .isReadOnly();
      if (locallyWritable && modelDescriptor instanceof ISecurable) {
        try {
          securityHandler.pushToSecurityContext(EAuthorization.ENABLED);
          locallyWritable = securityHandler
              .isAccessGranted((ISecurable) modelDescriptor);
        } finally {
          securityHandler.restoreLastSecurityContextSnapshot();
        }
      }
      modelConnector.setLocallyWritable(locallyWritable);
      if (((IGateAccessible) modelDescriptor).getReadabilityGates() != null) {
        for (IGate gate : ((IGateAccessible) modelDescriptor)
            .getReadabilityGates()) {
          if (!(gate instanceof ISecurable)
              || securityHandler.isAccessGranted((ISecurable) gate)) {
            modelConnector.addReadabilityGate(gate.clone());
          }
        }
      }
      if (((IGateAccessible) modelDescriptor).getWritabilityGates() != null) {
        for (IGate gate : ((IGateAccessible) modelDescriptor)
            .getWritabilityGates()) {
          if (!(gate instanceof ISecurable)
              || securityHandler.isAccessGranted((ISecurable) gate)) {
            modelConnector.addWritabilityGate(gate.clone());
          }
        }
      }
    }
    return modelConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  /**
   * Gets the descriptorRegistry.
   *
   * @return the descriptorRegistry.
   */
  @Override
  public IComponentDescriptorRegistry getDescriptorRegistry() {
    return descriptorRegistry;
  }

  /**
   * Sets the factory for the accessors used to access the model properties.
   *
   * @param accessorFactory
   *          The {@code IAccessorFactory} to use.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets the descriptorRegistry.
   *
   * @param descriptorRegistry
   *          the descriptorRegistry to set.
   */
  public void setDescriptorRegistry(
      IComponentDescriptorRegistry descriptorRegistry) {
    this.descriptorRegistry = descriptorRegistry;
  }
}
