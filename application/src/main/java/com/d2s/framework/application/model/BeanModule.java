/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * A bean module is the base class of bean related modules.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanModule extends SubModule implements PropertyChangeListener {

  private Object moduleObject;

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
   * Gets the module's projected object.
   * 
   * @return the projected object.
   */
  public Object getModuleObject() {
    return moduleObject;
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
  public void propertyChange(@SuppressWarnings("unused")
  PropertyChangeEvent evt) {
    String oldName = getName();
    String oldI18nName = getI18nName();
    setName(String.valueOf(this.moduleObject));
    firePropertyChange("name", oldName, getName());
    firePropertyChange("i18nName", oldI18nName, getI18nName());
  }

  /**
   * Sets the module's projected object.
   * 
   * @param moduleObject
   *            the projected object.
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
    firePropertyChange("moduleObject", oldValue, getModuleObject());
  }
}
