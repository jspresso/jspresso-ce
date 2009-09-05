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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.security.auth.Subject;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISubjectAware;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gate.AbstractModelGate;

/**
 * This gate opens and closes based on the value of a boolean property of its
 * model.
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
public class BooleanPropertyModelGate extends AbstractModelGate implements
    PropertyChangeListener, ISubjectAware, ISecurable {

  private IAccessorFactory accessorFactory;
  private String           booleanPropertyName;
  private boolean          open;
  private boolean          openOnTrue;
  private Subject          subject;
  private List<String>     grantedRoles;

  /**
   * Constructs a new <code>BooleanPropertyModelGate</code> instance.
   */
  public BooleanPropertyModelGate() {
    openOnTrue = true;
    open = !openOnTrue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BooleanPropertyModelGate clone() {
    BooleanPropertyModelGate clonedGate = (BooleanPropertyModelGate) super
        .clone();
    clonedGate.open = !openOnTrue;
    return clonedGate;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isOpen() {
    return SecurityHelper.isSubjectGranted(subject, getGrantedRoles()) && open;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModel(Object model) {
    Object oldModel = getModel();
    super.setModel(model);
    if (oldModel != model) {
      if (oldModel instanceof IPropertyChangeCapable) {
        ((IPropertyChangeCapable) oldModel).removePropertyChangeListener(
            booleanPropertyName, this);
      }
      if (model instanceof IPropertyChangeCapable) {
        ((IPropertyChangeCapable) model).addPropertyChangeListener(
            booleanPropertyName, this);
      }
      boolean oldOpen = isOpen();
      if (model != null) {
        try {
          IAccessor accessor = accessorFactory.createPropertyAccessor(
              booleanPropertyName, model.getClass());
          Boolean modelValue = (Boolean) accessor.getValue(model);
          this.open = (modelValue != null && modelValue.booleanValue());
          if (!openOnTrue) {
            this.open = !this.open;
          }
        } catch (IllegalAccessException ex) {
          throw new NestedRuntimeException(ex);
        } catch (InvocationTargetException ex) {
          throw new NestedRuntimeException(ex);
        } catch (NoSuchMethodException ex) {
          throw new NestedRuntimeException(ex);
        }
      } else {
        this.open = !openOnTrue;
      }
      firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void propertyChange(PropertyChangeEvent evt) {
    boolean oldOpen = isOpen();
    this.open = evt.getNewValue() != null
        && ((Boolean) evt.getNewValue()).booleanValue();
    if (!openOnTrue) {
      this.open = !this.open;
    }
    firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
  }

  /**
   * Sets the accessorFactory.
   * 
   * @param accessorFactory
   *          the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets the booleanPropertyName.
   * 
   * @param booleanPropertyName
   *          the booleanPropertyName to set.
   */
  public void setBooleanPropertyName(String booleanPropertyName) {
    this.booleanPropertyName = booleanPropertyName;
  }

  /**
   * Sets the openOnTrue.
   * 
   * @param openOnTrue
   *          the openOnTrue to set.
   */
  public void setOpenOnTrue(boolean openOnTrue) {
    this.openOnTrue = openOnTrue;
  }

  /**
   * {@inheritDoc}
   */
  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  /**
   * Gets the subject.
   * 
   * @return the subject.
   */
  protected Subject getSubject() {
    return subject;
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public List<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(List<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }
}
