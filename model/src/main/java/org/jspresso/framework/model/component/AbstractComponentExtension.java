/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.hibernate.Hibernate;
import org.jspresso.framework.util.accessor.bean.BeanAccessorFactory;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * This abstract class is a helper base class for components extensions.
 * Developpers should inherit from it and use the <code>getComponent()</code> to
 * access the extended component instance.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <T>
 *          the parametrized component class on which these extensions work on.
 */
public abstract class AbstractComponentExtension<T extends IComponent>
    implements IComponentExtension<T>, IComponentFactoryAware {

  private IComponentFactory componentFactory;
  private final T           extendedComponent;

  /**
   * Constructs a new <code>AbstractComponentExtension</code> instance.
   * 
   * @param component
   *          The extended component instance.
   */
  public AbstractComponentExtension(T component) {
    this.extendedComponent = component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T getComponent() {
    return extendedComponent;
  }

  /**
   * Default implementation is empty.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void postCreate() {
    // Empty implementation
  }

  /**
   * Sets the componentFactory.
   * 
   * @param componentFactory
   *          the componentFactory to set.
   */
  @Override
  public void setComponentFactory(IComponentFactory componentFactory) {
    this.componentFactory = componentFactory;
  }

  /**
   * Gets the componentFactory.
   * 
   * @return the componentFactory.
   */
  protected IComponentFactory getComponentFactory() {
    return componentFactory;
  }

  /**
   * Registers a property change listener to forward property changes.
   * 
   * @param sourceBean
   *          the source bean.
   * @param sourceProperty
   *          the name of the source property.
   * @param forwardedProperty
   *          the name of the forwarded property.
   */
  protected void registerNotificationForwarding(
      IPropertyChangeCapable sourceBean, String sourceProperty,
      String forwardedProperty) {
    registerNotificationForwarding(sourceBean, sourceProperty, new String[] {
      forwardedProperty
    });
  }

  /**
   * Registers a property change listener to forward property changes.
   * 
   * @param sourceBean
   *          the source bean.
   * @param sourceProperty
   *          the name of the source property.
   * @param forwardedProperty
   *          the name of the forwarded property.
   */
  protected void registerNotificationForwarding(
      IPropertyChangeCapable sourceBean, String sourceProperty,
      String... forwardedProperty) {
    sourceBean.addPropertyChangeListener(sourceProperty,
        new ForwardingPropertyChangeListener(forwardedProperty));
  }

  /**
   * Registers notification forwarding from a collection's child property
   * 
   * @param sourceBean
   *          the source bean.
   * @param sourceCollectionProperty
   *          the collection property to listen to.
   * @param sourceElementProperty
   *          the collection elements property to listen to.
   * @param forwardedProperty
   *          the name of the forwarded property.
   */
  protected void registerNotificationCollectionForwarding(
      IPropertyChangeCapable sourceBean, String sourceCollectionProperty,
      String sourceElementProperty, String forwardedProperty) {
    registerNotificationCollectionForwarding(sourceBean,
        sourceCollectionProperty, sourceElementProperty, new String[] {
          forwardedProperty
        });
  }

  /**
   * Registers notification forwarding from a collection's child property
   * 
   * @param sourceBean
   *          the source bean.
   * @param sourceCollectionProperty
   *          the collection property to listen to.
   * @param sourceElementProperty
   *          the collection elements property to listen to.
   * @param forwardedProperty
   *          the name of the forwarded property.
   */
  protected void registerNotificationCollectionForwarding(
      final IPropertyChangeCapable sourceBean,
      final String sourceCollectionProperty,
      final String sourceElementProperty, final String... forwardedProperty) {

    // listen normally to collection changes
    registerNotificationForwarding(sourceBean, sourceCollectionProperty,
        forwardedProperty);

    // setup collection listener to attach / detach property change listeners on
    // elements
    sourceBean.addPropertyChangeListener(sourceCollectionProperty,
        new PropertyChangeListener() {

          @SuppressWarnings("rawtypes")
          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            if (getComponentFactory().getAccessorFactory() != null) {
              // add listeners
              if (evt.getNewValue() != null
                  && evt.getNewValue() instanceof Collection<?>
                  && Hibernate.isInitialized(evt.getNewValue())) {
                Collection<IPropertyChangeCapable> newChildren = new HashSet<IPropertyChangeCapable>(
                    (Collection<IPropertyChangeCapable>) evt.getNewValue());
                if (evt.getOldValue() != null
                    && evt.getOldValue() instanceof Collection<?>
                    && Hibernate.isInitialized(evt.getOldValue())) {
                  newChildren.removeAll((Collection<?>) evt.getOldValue());
                }
                for (IPropertyChangeCapable child : newChildren) {
                  registerNotificationForwarding(child, sourceElementProperty,
                      forwardedProperty);
                }
              }
              // remove listeners
              if (evt.getOldValue() != null
                  && evt.getOldValue() instanceof Collection<?>
                  && Hibernate.isInitialized(evt.getOldValue())) {
                Collection<IPropertyChangeCapable> removedChildren = new HashSet<IPropertyChangeCapable>(
                    (Collection<IPropertyChangeCapable>) evt.getOldValue());

                if (evt.getNewValue() != null
                    && evt.getNewValue() instanceof Collection<?>
                    && Hibernate.isInitialized(evt.getNewValue())) {
                  removedChildren.removeAll((Collection<?>) evt.getNewValue());
                }
                for (IPropertyChangeCapable child : removedChildren) {
                  for (PropertyChangeListener listener : child
                      .getPropertyChangeListeners(sourceElementProperty)) {
                    if (listener instanceof AbstractComponentExtension<?>.ForwardingPropertyChangeListener) {
                      if (Arrays
                          .equals(
                              ((AbstractComponentExtension.ForwardingPropertyChangeListener) listener)
                                  .getForwardedProperties(), forwardedProperty)) {
                        child.removePropertyChangeListener(
                            sourceElementProperty, listener);
                      }
                    }
                  }
                }
              }
            }
          }
        });

    // Setup listener for initial collection if it exists
    Collection<IPropertyChangeCapable> initialChildren;
    if (sourceBean instanceof IComponent) {
      initialChildren = (Collection<IPropertyChangeCapable>) ((IComponent) sourceBean)
          .straightGetProperty(sourceCollectionProperty);
    } else {
      try {
        initialChildren = (Collection<IPropertyChangeCapable>) new BeanAccessorFactory()
            .createPropertyAccessor(sourceCollectionProperty,
                sourceBean.getClass()).getValue(sourceBean);
      } catch (IllegalAccessException ex) {
        throw new NestedRuntimeException(ex);
      } catch (InvocationTargetException ex) {
        throw new NestedRuntimeException(ex);
      } catch (NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    if (Hibernate.isInitialized(initialChildren)) {
      for (IPropertyChangeCapable child : initialChildren) {
        registerNotificationForwarding(child, sourceElementProperty,
            forwardedProperty);
      }
    }
  }

  private class ForwardingPropertyChangeListener implements
      PropertyChangeListener {

    private String[] forwardedProperties;

    /**
     * Constructs a new <code>ForwardingPropertyChangeListener</code> instance.
     * 
     * @param forwardedProperties
     */
    public ForwardingPropertyChangeListener(String[] forwardedProperties) {
      this.forwardedProperties = forwardedProperties;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (getComponentFactory().getAccessorFactory() != null) {
        try {
          for (String prop : forwardedProperties) {
            if (getComponent().hasListeners(prop)) {
              Object newValue = getComponentFactory()
                  .getAccessorFactory()
                  .createPropertyAccessor(prop,
                      getComponent().getComponentContract())
                  .getValue(getComponent());
              getComponent().firePropertyChange(prop,
                  IPropertyChangeCapable.UNKNOWN, newValue);
            }
          }
        } catch (IllegalAccessException ex) {
          throw new NestedRuntimeException(ex);
        } catch (InvocationTargetException ex) {
          if (ex.getTargetException() instanceof RuntimeException) {
            throw (RuntimeException) ex.getTargetException();
          }
          throw new NestedRuntimeException(ex);
        } catch (NoSuchMethodException ex) {
          throw new NestedRuntimeException(ex);
        }
      }
    }

    /**
     * Gets the forwardedProperties.
     * 
     * @return the forwardedProperties.
     */
    public String[] getForwardedProperties() {
      return forwardedProperties;
    }
  }
}
