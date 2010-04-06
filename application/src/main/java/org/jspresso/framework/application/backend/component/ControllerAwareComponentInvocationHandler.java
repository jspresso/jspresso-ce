/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.component;

import java.lang.reflect.Proxy;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtension;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.basic.BasicComponentInvocationHandler;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.security.ISubjectAware;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This component invocation handler handles initialization of lazy loaded
 * properties like collections an entity references, delegating the
 * initialization job to the backend controller.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerAwareComponentInvocationHandler extends
    BasicComponentInvocationHandler {

  private static final long  serialVersionUID = -3613223267370638150L;

  private IBackendController backendController;

  /**
   * Constructs a new <code>ControllerAwareEntityInvocationHandler</code>
   * instance.
   * 
   * @param componentDescriptor
   *          The descriptor of the proxy entity.
   * @param inlineComponentFactory
   *          the factory used to create inline components.
   * @param collectionFactory
   *          The factory used to create empty entity collections from
   *          collection getters.
   * @param accessorFactory
   *          The factory used to access proxy properties.
   * @param extensionFactory
   *          The factory used to create entity extensions based on their
   *          classes.
   * @param backendController
   *          the current backend controller.
   */
  protected ControllerAwareComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory,
      IBackendController backendController) {
    super(componentDescriptor, inlineComponentFactory, collectionFactory,
        accessorFactory, extensionFactory);
    this.backendController = backendController;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor) {
    getBackendController().initializePropertyIfNeeded((IComponent) proxy,
        propertyDescriptor.getName());
    return super.getCollectionProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getReferenceProperty(Object proxy,
      IReferencePropertyDescriptor<IComponent> propertyDescriptor) {
    getBackendController().initializePropertyIfNeeded((IComponent) proxy,
        propertyDescriptor.getName());
    return super.getReferenceProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isInitialized(Object objectOrProxy) {
    return getBackendController().isInitialized(objectOrProxy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void storeReferenceProperty(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      Object oldPropertyValue, Object newPropertyValue) {
    if (newPropertyValue != null
        && isInlineComponentReference(propertyDescriptor)) {
      if (Proxy.isProxyClass(newPropertyValue.getClass())
          && Proxy.getInvocationHandler(newPropertyValue) instanceof BasicComponentInvocationHandler
          && !(Proxy.getInvocationHandler(newPropertyValue) instanceof ControllerAwareComponentInvocationHandler)) {
        IComponent sessionAwareComponent = getInlineComponentFactory()
            .createComponentInstance(
                ((IComponent) newPropertyValue).getComponentContract());
        sessionAwareComponent
            .straightSetProperties(((IComponent) newPropertyValue)
                .straightGetProperties());
        super.storeReferenceProperty(propertyDescriptor, oldPropertyValue,
            sessionAwareComponent);
      } else {
        super.storeReferenceProperty(propertyDescriptor, oldPropertyValue,
            newPropertyValue);
      }
    } else {
      super.storeReferenceProperty(propertyDescriptor, oldPropertyValue,
          newPropertyValue);
    }
  }

  /**
   * Sets the JAAS subject to subject aware extensions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void configureExtension(IComponentExtension<IComponent> extension) {
    super.configureExtension(extension);
    if (extension instanceof ISubjectAware && getBackendController() != null) {
      ((ISubjectAware) extension).setSubject(getBackendController()
          .getSubject());
    }
  }

  /**
   * Gets the backendController.
   * 
   * @return the backendController.
   */
  protected IBackendController getBackendController() {
    return backendController;
  }
}
