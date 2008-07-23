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
package org.jspresso.framework.model.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jspresso.framework.util.bean.IPropertyChangeCapable;


/**
 * This abstract class is a helper base class for components extensions.
 * Developpers should inherit from it and use the <code>getComponent()</code>
 * to access the extended component instance.
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
 * @param <T>
 *            the parametrized component class on which these extensions work
 *            on.
 */
public abstract class AbstractComponentExtension<T extends IComponent>
    implements IComponentExtension<T>, IComponentFactoryAware {

  private IComponentFactory componentFactory;
  private final T           extendedComponent;

  /**
   * Constructs a new <code>AbstractComponentExtension</code> instance.
   * 
   * @param component
   *            The extended component instance.
   */
  public AbstractComponentExtension(T component) {
    this.extendedComponent = component;
  }

  /**
   * {@inheritDoc}
   */
  public T getComponent() {
    return extendedComponent;
  }

  /**
   * Computes the entity type.
   * 
   * @return The entity type.
   */
  public String getType() {
    return getComponent().getContract().getName();
  }

  /**
   * Sets the componentFactory.
   * 
   * @param componentFactory
   *            the componentFactory to set.
   */
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
   *            the source bean.
   * @param sourceProperty
   *            the name of the source property.
   * @param forwardedProperty
   *            the name of the forwarded property.
   */
  protected void registerNotificationForwarding(
      IPropertyChangeCapable sourceBean, String sourceProperty,
      final String forwardedProperty) {
    sourceBean.addPropertyChangeListener(sourceProperty,
        new PropertyChangeListener() {

          public void propertyChange(PropertyChangeEvent evt) {
            getComponent().firePropertyChange(forwardedProperty,
                evt.getOldValue(), evt.getNewValue());
          }
        });
  }
}
