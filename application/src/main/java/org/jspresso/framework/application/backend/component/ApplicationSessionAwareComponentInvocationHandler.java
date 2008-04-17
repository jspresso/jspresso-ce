/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.component;

import java.lang.reflect.Proxy;

import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.basic.BasicComponentInvocationHandler;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;


/**
 * This component invocation handler handles initialization of lazy loaded
 * properties like collections an entity references, delegating the
 * initialization job to the application session.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ApplicationSessionAwareComponentInvocationHandler extends
    BasicComponentInvocationHandler {

  private static final long   serialVersionUID = -3613223267370638150L;

  private IApplicationSession applicationSession;

  /**
   * Constructs a new
   * <code>ApplicationSessionAwareEntityInvocationHandler</code> instance.
   * 
   * @param componentDescriptor
   *            The descriptor of the proxy entity.
   * @param inlineComponentFactory
   *            the factory used to create inline components.
   * @param collectionFactory
   *            The factory used to create empty entity collections from
   *            collection getters.
   * @param accessorFactory
   *            The factory used to access proxy properties.
   * @param extensionFactory
   *            The factory used to create entity extensions based on their
   *            classes.
   * @param applicationSession
   *            the current application session.
   */
  protected ApplicationSessionAwareComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory,
      IApplicationSession applicationSession) {
    super(componentDescriptor, inlineComponentFactory, collectionFactory,
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
    applicationSession.initializePropertyIfNeeded((IComponent) proxy,
        propertyDescriptor);
    return super.getCollectionProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getReferenceProperty(Object proxy,
      IReferencePropertyDescriptor<IComponent> propertyDescriptor) {
    applicationSession.initializePropertyIfNeeded((IComponent) proxy,
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
      IReferencePropertyDescriptor<?> propertyDescriptor, Object propertyValue) {
    if (propertyValue != null && isInlineComponentReference(propertyDescriptor)) {
      if (Proxy.isProxyClass(propertyValue.getClass())
          && Proxy.getInvocationHandler(propertyValue) instanceof BasicComponentInvocationHandler
          && !(Proxy.getInvocationHandler(propertyValue) instanceof ApplicationSessionAwareComponentInvocationHandler)) {
        IComponent sessionAwareComponent = getInlineComponentFactory()
            .createComponentInstance(((IComponent) propertyValue).getContract());
        sessionAwareComponent
            .straightSetProperties(((IComponent) propertyValue)
                .straightGetProperties());
        super.storeReferenceProperty(propertyDescriptor, sessionAwareComponent);
      } else {
        super.storeReferenceProperty(propertyDescriptor, propertyValue);
      }
    } else {
      super.storeReferenceProperty(propertyDescriptor, propertyValue);
    }
  }
}
