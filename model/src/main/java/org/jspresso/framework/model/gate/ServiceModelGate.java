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
package org.jspresso.framework.model.gate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.service.DependsOnHelper;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gate.AbstractModelGate;
import org.jspresso.framework.util.lang.IContextAware;

/**
 * This is a gate whose opening rules are based on an arbitrary method returning a boolean value.
 *
 * @author Vincent Vandenschrick
 */
public class ServiceModelGate extends AbstractModelGate implements ISecurable, PropertyChangeListener, IContextAware {

  private Collection<String> grantedRoles;
  private boolean            open;
  private boolean            openOnTrue;
  private String             methodName;
  private boolean            negatedByName;
  private IAccessorFactory   accessorFactory;

  private Map<String, Object> context;

  /**
   * Constructs a new {@code ServiceModelGate} instance.
   */
  public ServiceModelGate() {
    openOnTrue = true;
    open = false;
  }

  /**
   * {@inheritDoc}
   *
   * @return the service model gate
   */
  @SuppressWarnings("unchecked")
  @Override
  public ServiceModelGate clone() {
    ServiceModelGate clonedGate = (ServiceModelGate) super.clone();
    clonedGate.open = /* !openOnTrue */false;
    return clonedGate;
  }

  /**
   * Gets the grantedRoles.
   *
   * @return the grantedRoles.
   */
  @Override
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isOpen() {
    return open;
  }

  /**
   * Configures the roles for which the gate is installed. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s).
   *
   * @param grantedRoles
   *     the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

  /**
   * {@inheritDoc}
   *
   * @param model
   *     the model
   * @internal
   */
  @Override
  public void setModel(Object model) {
    Object oldModel = getModel();
    super.setModel(model);
    if (oldModel != model) {
      Method serviceMethod = recomputeGateState(model);
      if (serviceMethod != null) {
        if (oldModel instanceof IPropertyChangeCapable) {
          new DependsOnHelper(this).unregisterDependsOnListeners(serviceMethod, (IPropertyChangeCapable) oldModel,
              accessorFactory);
        }
        if (model instanceof IPropertyChangeCapable) {
          new DependsOnHelper(this).registerDependsOnListeners(serviceMethod, (IPropertyChangeCapable) model,
              accessorFactory);
        }
      }
    }
  }

  private Method recomputeGateState(Object model) {
    Method serviceMethod = null;
    boolean oldOpen = isOpen();
    if (model != null) {
      try {
        Class<?> modelContract;
        if (model instanceof IComponent) {
          modelContract = ((IComponent) model).getComponentContract();
        } else {
          modelContract = model.getClass();
        }
        try {
          serviceMethod = modelContract.getMethod(getMethodName(), Map.class);
        } catch (NoSuchMethodException nsme) {
          serviceMethod = modelContract.getMethod(getMethodName(), null);
        }
        Object[] arguments = null;
        if (serviceMethod.getParameterTypes().length > 0) {
          arguments = new Object[]{context};
        }
        Object serviceValue = serviceMethod.invoke(model, arguments);
        this.open = shouldOpen(serviceValue);
        if (!openOnTrue) {
          this.open = !this.open;
        }
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new NestedRuntimeException(ex.getCause());
      }
    } else {
      this.open = /* !openOnTrue */false;
    }
    firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
    return serviceMethod;
  }

  /**
   * {@inheritDoc}
   *
   * @param evt
   *     the evt
   */
  @SuppressWarnings("unchecked")
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    recomputeGateState(getModel());
  }

  /**
   * This property allows to revert the standard behaviour of the gate, i.e.
   * close when it should normally have opened and the other way around.
   *
   * @param openOnTrue
   *     the openOnTrue to set.
   */
  public void setOpenOnTrue(boolean openOnTrue) {
    this.openOnTrue = openOnTrue;
  }

  /**
   * Configures the model service method name to which this gate is attached.
   *
   * @param methodName
   *     the methodName to set.
   */
  public void setMethodName(String methodName) {
    if (methodName != null && methodName.startsWith("!")) {
      this.methodName = methodName.substring(1);
      negatedByName = true;
    } else {
      this.methodName = methodName;
      negatedByName = false;
    }
  }

  /**
   * Gets property name.
   *
   * @return The property name
   */
  protected String getMethodName() {
    return methodName;
  }

  /**
   * Based on the underlying service value, determines if the gate should open
   * or close. The return value might be later changed by the openOnTrue value.
   *
   * @param serviceValue
   *     the model service value.
   * @return true if the gate should open (before applying the openOnTrue property).
   */
  protected boolean shouldOpen(Object serviceValue) {
    boolean shouldOpen = serviceValue != null;
    if (serviceValue instanceof Boolean) {
      shouldOpen = (Boolean) serviceValue;
    } else if (serviceValue instanceof Collection<?>) {
      shouldOpen = !((Collection<?>) serviceValue).isEmpty();
    }
    if (negatedByName) {
      return !shouldOpen;
    }
    return shouldOpen;

  }

  /**
   * Configures the accessor factory to use to access the underlying model
   * properties.
   *
   * @param accessorFactory
   *     the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets context.
   *
   * @param context
   *     the context
   */
  @Override
  public void setContext(Map<String, Object> context) {
    this.context = context;
  }
}
