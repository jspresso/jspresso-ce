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
package org.jspresso.framework.application.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.application.model.descriptor.BeanModuleDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * A bean module is the base class of bean related modules.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanModule extends Module implements PropertyChangeListener {

  /**
   * <code>MODULE_OBJECT</code> is "moduleObject".
   */
  public static final String           MODULE_OBJECT = "moduleObject";

  private IComponentDescriptor<Object> componentDescriptor;
  private Object                       moduleObject;

  /**
   * Equality based on projected object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BeanModule)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    BeanModule rhs = (BeanModule) obj;
    return new EqualsBuilder().append(getModuleObject(), rhs.getModuleObject())
        .isEquals();
  }

  /**
   * Gets the componentDescriptor.
   * 
   * @return the componentDescriptor.
   */
  @SuppressWarnings("unchecked")
  public IComponentDescriptor<Object> getComponentDescriptor() {
    if (componentDescriptor == null) {
      if (getProjectedViewDescriptor() != null
          && getProjectedViewDescriptor().getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
        return ((IComponentDescriptorProvider<Object>) getProjectedViewDescriptor()
            .getModelDescriptor()).getComponentDescriptor();
      }
    }
    return componentDescriptor;
  }

  /**
   * Gets the module's projected object.
   * 
   * @return the projected object.
   */
  public Object getModuleObject() {
    return moduleObject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getProjectedViewDescriptor() {
    IViewDescriptor projectedViewDescriptor = super
        .getProjectedViewDescriptor();
    if (componentDescriptor != null) {
      if (projectedViewDescriptor == null) {
        projectedViewDescriptor = new BasicComponentViewDescriptor();
        ((BasicComponentViewDescriptor) projectedViewDescriptor)
            .setModelDescriptor(componentDescriptor);
        ((BasicComponentViewDescriptor) projectedViewDescriptor)
            .setBorderType(EBorderType.TITLED);
        ((BasicComponentViewDescriptor) projectedViewDescriptor)
            .setName(componentDescriptor.getName());
        ((BasicComponentViewDescriptor) projectedViewDescriptor)
            .setColumnCount(3);
        setProjectedViewDescriptor(projectedViewDescriptor);
      }
      if (projectedViewDescriptor.getModelDescriptor() == null
          && projectedViewDescriptor instanceof BasicViewDescriptor) {
        ((BasicViewDescriptor) projectedViewDescriptor)
            .setModelDescriptor(componentDescriptor);
      }
    }
    return projectedViewDescriptor;
  }

  /**
   * Hash code based on projected object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 53).append(getModuleObject()).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  public void propertyChange(@SuppressWarnings("unused") PropertyChangeEvent evt) {
    String oldName = getName();
    String oldI18nName = getI18nName();
    setName(String.valueOf(this.moduleObject));
    firePropertyChange(NAME, oldName, getName());
    firePropertyChange(I18N_NAME, oldI18nName, getI18nName());
  }

  /**
   * Sets the componentDescriptor.
   * 
   * @param componentDescriptor
   *          the componentDescriptor to set.
   */
  public void setComponentDescriptor(
      IComponentDescriptor<Object> componentDescriptor) {
    this.componentDescriptor = componentDescriptor;
  }

  /**
   * Sets the module's projected object.
   * 
   * @param moduleObject
   *          the projected object.
   */
  public void setModuleObject(Object moduleObject) {
    if (ObjectUtils.equals(this.moduleObject, moduleObject)) {
      return;
    }
    if (this.moduleObject instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) this.moduleObject)
          .removePropertyChangeListener(this);
    }
    Object oldValue = getModuleObject();
    this.moduleObject = moduleObject;
    if (this.moduleObject instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) this.moduleObject)
          .addPropertyChangeListener(this);
    }
    firePropertyChange(MODULE_OBJECT, oldValue, getModuleObject());
  }

  /**
   * Returns the projectedViewDescriptor nested in a "moduleObject" property
   * view.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    if (getProjectedViewDescriptor() != null) {
      BeanModuleDescriptor beanModuleDescriptor = getDescriptor();
      BasicBorderViewDescriptor nestingViewDescriptor = new BasicBorderViewDescriptor();
      nestingViewDescriptor
          .setCenterViewDescriptor(getProjectedViewDescriptor());
      nestingViewDescriptor.setModelDescriptor(beanModuleDescriptor
          .getPropertyDescriptor(MODULE_OBJECT));
      BasicBorderViewDescriptor viewDescriptor = new BasicBorderViewDescriptor();
      viewDescriptor.setModelDescriptor(beanModuleDescriptor);
      viewDescriptor.setCenterViewDescriptor(nestingViewDescriptor);
      return viewDescriptor;
    }
    return null;
  }

  /**
   * Gets the module descriptor.
   * 
   * @return the module descriptor.
   */
  protected BeanModuleDescriptor getDescriptor() {
    return new BeanModuleDescriptor(getComponentDescriptor());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getGrantedRoles() {
    Collection<String> grantedRoles = super.getGrantedRoles();
    if (grantedRoles == null && componentDescriptor != null) {
      grantedRoles = componentDescriptor.getGrantedRoles();
    }
    return grantedRoles;
  }
}
