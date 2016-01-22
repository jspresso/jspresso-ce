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
package org.jspresso.framework.application.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.jspresso.framework.application.backend.IBackendController;
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
 * This type of module keeps a reference on a single bean. There is no
 * assumption made on whether this bean is actually a persistent entity or any
 * other type of java bean.
 * <p>
 * Bean modules must have their referenced bean initialized somehow. So it's
 * rather common to have the module content initialized through a startup action
 * depending on the session state or dynamically constructed by a standard
 * action like {@code AddBeanAsSubModuleAction}.
 * <p>
 * This type of module is definitely the one that offers maximum flexibility to
 * handle arbitrary models.
 *
 * @author Vincent Vandenschrick
 */
public class BeanModule extends Module implements PropertyChangeListener {

  /**
   * {@code MODULE_OBJECT} is "moduleObject".
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
    EqualsBuilder equalsBuilder = new EqualsBuilder().appendSuper(super.equals(obj));
    if (moduleObject != null) {
      equalsBuilder.append(moduleObject, rhs.moduleObject);
    }
    return equalsBuilder.isEquals();
  }

  /**
   * Gets the componentDescriptor.
   *
   * @return the componentDescriptor.
   */
  @SuppressWarnings("unchecked")
  public IComponentDescriptor<?> getComponentDescriptor() {
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
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String oldI18nName = getI18nName();
    setI18nName(String.valueOf(this.moduleObject));
    firePropertyChange(I18N_NAME, oldI18nName, getI18nName());
  }

  /**
   * Configures the type of bean this module manages. A bunch of default values
   * are inferred from this component descriptor. For instance, icon image URL
   * or even granted roles can be inferred from the configured component
   * descriptor. The latter means that bean modules based on forbidden entities
   * will automatically be excluded from the workspace of the logged-in user.
   * <p>
   * However, when not set, the component descriptor it self can be inferred from
   * the configured projected view descriptor model.
   *
   * @param componentDescriptor
   *          the componentDescriptor to set.
   */
  public void setComponentDescriptor(
      IComponentDescriptor<Object> componentDescriptor) {
    this.componentDescriptor = componentDescriptor;
  }

  /**
   * Assigns the bean this module manages. The projected view will automatically
   * reflect this change since a &quot;moduleObject&quot; property change will
   * be fired.
   *
   * @param moduleObject
   *          the projected object.
   */
  public void setModuleObject(Object moduleObject) {
    if (ObjectUtils.equals(this.moduleObject, moduleObject)) {
      return;
    }
    String toStringProperty = getComponentDescriptor().getToStringProperty();
    if (getName() == null
        && this.moduleObject instanceof IPropertyChangeCapable) {
      if (toStringProperty != null) {
        ((IPropertyChangeCapable) this.moduleObject)
            .removePropertyChangeListener(toStringProperty, this);
      } else {
        ((IPropertyChangeCapable) this.moduleObject)
            .removePropertyChangeListener(this);
      }
    }
    Object oldValue = getModuleObject();
    this.moduleObject = moduleObject;
    if (getName() == null
        && this.moduleObject instanceof IPropertyChangeCapable) {
      if (toStringProperty != null) {
        ((IPropertyChangeCapable) this.moduleObject)
            .addPropertyChangeListener(toStringProperty, this);
      } else {
        ((IPropertyChangeCapable) this.moduleObject)
            .addPropertyChangeListener(this);
      }
    }
    firePropertyChange(MODULE_OBJECT, oldValue, getModuleObject());
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
  public BeanModule clone() {
    BeanModule clone = (BeanModule) super.clone();
    clone.moduleObject = null;
    return clone;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isLocallyDirty(IBackendController backendController) {
    boolean locallyDirty = backendController
        .isAnyDirtyInDepth(Collections.singleton(getModuleObject()));
    return locallyDirty;
  }
}
