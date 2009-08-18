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
package org.jspresso.framework.application.backend.entity;

import java.lang.reflect.Proxy;

import org.jspresso.framework.application.backend.component.ApplicationSessionAwareComponentInvocationHandler;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.basic.BasicComponentInvocationHandler;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.basic.BasicEntityInvocationHandler;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This entity invocation handler handles initialization of lazy loaded
 * properties like collections an entity references, delegating the
 * initialization job to the application session.
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
public class ApplicationSessionAwareEntityInvocationHandler extends
    BasicEntityInvocationHandler {

  private static final long   serialVersionUID = 3663517052427878204L;

  private IApplicationSession applicationSession;

  /**
   * Constructs a new
   * <code>ApplicationSessionAwareEntityInvocationHandler</code> instance.
   * 
   * @param entityDescriptor
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
   * @param applicationSession
   *          the current application session.
   */
  protected ApplicationSessionAwareEntityInvocationHandler(
      IComponentDescriptor<IComponent> entityDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory,
      IApplicationSession applicationSession) {
    super(entityDescriptor, inlineComponentFactory, collectionFactory,
        accessorFactory, extensionFactory);
    this.applicationSession = applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor) {
    applicationSession.initializePropertyIfNeeded((IEntity) proxy,
        propertyDescriptor);
    return super.getCollectionProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getReferenceProperty(Object proxy,
      IReferencePropertyDescriptor<IComponent> propertyDescriptor) {

    applicationSession.initializePropertyIfNeeded((IEntity) proxy,
        propertyDescriptor);
    return super.getReferenceProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isInitialized(Object objectOrProxy) {
    return applicationSession.isInitialized(objectOrProxy);
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
          && !(Proxy.getInvocationHandler(newPropertyValue) instanceof ApplicationSessionAwareComponentInvocationHandler)) {
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
}
