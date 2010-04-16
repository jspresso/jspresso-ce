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
package org.jspresso.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gate.AbstractModelGate;

/**
 * This is the base abstract class of gates whose opening rules are based on a
 * single model property value.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick the actual type of property.
 * @param <E>
 *          the actual property type.
 */
public abstract class AbstractPropertyModelGate<E> extends AbstractModelGate
    implements PropertyChangeListener, ISecurable {

  private IAccessorFactory   accessorFactory;
  private String             propertyName;
  private boolean            open;
  private boolean            openOnTrue;
  private Collection<String> grantedRoles;

  /**
   * Constructs a new <code>AbstractPropertyModelGate</code> instance.
   */
  public AbstractPropertyModelGate() {
    openOnTrue = true;
    open = !openOnTrue;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public AbstractPropertyModelGate<E> clone() {
    AbstractPropertyModelGate<E> clonedGate = (AbstractPropertyModelGate<E>) super
        .clone();
    clonedGate.open = !openOnTrue;
    return clonedGate;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isOpen() {
    return open;
  }

  /**
   * {@inheritDoc}
   * 
   * @internal
   */
  @SuppressWarnings("unchecked")
  @Override
  public void setModel(Object model) {
    Object oldModel = getModel();
    super.setModel(model);
    if (oldModel != model) {
      if (oldModel instanceof IPropertyChangeCapable) {
        ((IPropertyChangeCapable) oldModel).removePropertyChangeListener(
            propertyName, this);
      } else if (oldModel instanceof Collection<?>) {
        for (Object elt : (Collection<?>) oldModel) {
          if (elt instanceof IPropertyChangeCapable) {
            ((IPropertyChangeCapable) elt).removePropertyChangeListener(
                propertyName, this);
          }
        }
      }
      if (model instanceof IPropertyChangeCapable) {
        ((IPropertyChangeCapable) model).addPropertyChangeListener(
            propertyName, this);
      } else if (model instanceof Collection<?>) {
        for (Object elt : (Collection<?>) model) {
          if (elt instanceof IPropertyChangeCapable) {
            ((IPropertyChangeCapable) elt).addPropertyChangeListener(
                propertyName, this);
          }
        }
      }
      boolean oldOpen = isOpen();
      if (model != null) {
        try {
          if (model instanceof Collection<?>) {
            this.open = computeCollectionOpenState((Collection<?>) model);
          } else {
            IAccessor accessor = accessorFactory.createPropertyAccessor(
                propertyName, model.getClass());
            E modelValue = (E) accessor.getValue(model);
            this.open = shouldOpen(modelValue);
            if (!openOnTrue) {
              this.open = !this.open;
            }
          }
        } catch (IllegalAccessException ex) {
          throw new NestedRuntimeException(ex);
        } catch (InvocationTargetException ex) {
          if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          throw new NestedRuntimeException(ex.getCause());
        } catch (NoSuchMethodException ex) {
          throw new NestedRuntimeException(ex);
        }
      } else {
        this.open = !openOnTrue;
      }
      firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
    }
  }

  @SuppressWarnings("unchecked")
  private boolean computeCollectionOpenState(Collection<?> model)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (model.isEmpty()) {
      return !openOnTrue;
    }
    for (Object elt : (Collection<?>) model) {
      IAccessor accessor = accessorFactory.createPropertyAccessor(propertyName,
          elt.getClass());
      E modelValue = (E) accessor.getValue(elt);
      boolean eltOpen = shouldOpen(modelValue);
      if (!openOnTrue) {
        eltOpen = !eltOpen;
      }
      if (!eltOpen) {
        return false;
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      boolean oldOpen = isOpen();
      if (getModel() instanceof Collection<?>) {
        this.open = computeCollectionOpenState((Collection<?>) getModel());
      } else {
        this.open = shouldOpen((E) evt.getNewValue());
        if (!openOnTrue) {
          this.open = !this.open;
        }
      }
      firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
    } catch (IllegalAccessException ex) {
      throw new NestedRuntimeException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new NestedRuntimeException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new NestedRuntimeException(ex);
    }
  }

  /**
   * Configures the accessor factory to use to access the underlying model
   * property.
   * 
   * @param accessorFactory
   *          the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Configures the model property name to which this gate is attached. How the
   * property value is actually linked to the gate state is delegated to the
   * concrete implementations.
   * 
   * @param propertyName
   *          the propertyName to set.
   */
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   * This property allows to revert the standard behaviour of the gate, i.e.
   * close when it should normally have opened and the other way around.
   * 
   * @param openOnTrue
   *          the openOnTrue to set.
   */
  public void setOpenOnTrue(boolean openOnTrue) {
    this.openOnTrue = openOnTrue;
  }

  /**
   * Based on the underlying property value, determines if the gate should open
   * or close. The return value might be later changed by the openOntrue value.
   * 
   * @param propertyValue
   *          the model property value.
   * @return true if the gate should open (before applying the openOnTrue
   *         property).
   */
  protected abstract boolean shouldOpen(E propertyValue);

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Configures the roles for which the gate is installed. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s).
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

}
