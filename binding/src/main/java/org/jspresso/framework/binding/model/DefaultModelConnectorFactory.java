/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.model;

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


/**
 * Default implementation for model connectors factory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
      IModelDescriptor modelDescriptor) {
    IValueConnector modelConnector = null;
    if (modelDescriptor instanceof IComponentDescriptor) {
      modelConnector = new ModelConnector(id,
          (IComponentDescriptor<?>) modelDescriptor, this);
    } else if (modelDescriptor instanceof ICollectionDescriptor) {
      modelConnector = new ModelCollectionConnector(id,
          (ICollectionDescriptor<?>) modelDescriptor, this);
    } else if (modelDescriptor instanceof IPropertyDescriptor) {
      if (modelDescriptor instanceof IReferencePropertyDescriptor) {
        modelConnector = new ModelRefPropertyConnector(
            (IReferencePropertyDescriptor<?>) modelDescriptor, this);
      } else if (modelDescriptor instanceof ICollectionPropertyDescriptor) {
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
      if (modelConnector != null) {
        if (((IPropertyDescriptor) modelDescriptor).isReadOnly()) {
          modelConnector.setLocallyWritable(false);
        }
        if (((IPropertyDescriptor) modelDescriptor).getReadabilityGates() != null) {
          for (IGate gate : ((IPropertyDescriptor) modelDescriptor)
              .getReadabilityGates()) {
            modelConnector.addReadabilityGate(gate.clone());
          }
        }
        if (((IPropertyDescriptor) modelDescriptor).getWritabilityGates() != null) {
          for (IGate gate : ((IPropertyDescriptor) modelDescriptor)
              .getWritabilityGates()) {
            modelConnector.addWritabilityGate(gate.clone());
          }
        }
      }
    }
    // if (modelConnector != null) {
    // modelConnector
    // .addConnectorValueChangeListener(new IConnectorValueChangeListener() {
    //
    // public void connectorValueChange(ConnectorValueChangeEvent evt) {
    // System.out.println(evt.getSource().getConnectorPath());
    // }
    // });
    // }
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
   *            The <code>IAccessorFactory</code> to use.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets the descriptorRegistry.
   * 
   * @param descriptorRegistry
   *            the descriptorRegistry to set.
   */
  public void setDescriptorRegistry(
      IComponentDescriptorRegistry descriptorRegistry) {
    this.descriptorRegistry = descriptorRegistry;
  }
}
