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
package org.jspresso.framework.binding.model;

import javax.security.auth.Subject;

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
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IGateAccessible;

/**
 * Default implementation for model connectors factory.
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
public class DefaultModelConnectorFactory implements IModelConnectorFactory {

  private IAccessorFactory             accessorFactory;
  private IComponentDescriptorRegistry descriptorRegistry;

  /**
   * {@inheritDoc}
   */
  public IValueConnector createModelConnector(String id,
      Class<?> componentContract, Subject subject) {
    return createModelConnector(id, getDescriptorRegistry()
        .getComponentDescriptor(componentContract), subject);
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor, Subject subject) {
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
    if (modelConnector != null) {
      modelConnector.setSubject(subject);
      if (modelDescriptor instanceof IGateAccessible) {
        modelConnector.setLocallyWritable(!((IGateAccessible) modelDescriptor)
            .isReadOnly());
        if (((IGateAccessible) modelDescriptor).getReadabilityGates() != null) {
          for (IGate gate : ((IGateAccessible) modelDescriptor)
              .getReadabilityGates()) {
            modelConnector.addReadabilityGate(gate.clone());
          }
        }
        if (((IGateAccessible) modelDescriptor).getWritabilityGates() != null) {
          for (IGate gate : ((IGateAccessible) modelDescriptor)
              .getWritabilityGates()) {
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
  public IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  /**
   * Gets the descriptorRegistry.
   * 
   * @return the descriptorRegistry.
   */
  public IComponentDescriptorRegistry getDescriptorRegistry() {
    return descriptorRegistry;
  }

  /**
   * Sets the factory for the accessors used to access the model properties.
   * 
   * @param accessorFactory
   *          The <code>IAccessorFactory</code> to use.
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
