/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.module;

import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * A bean module is the base class of bean related modules.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanModule extends SubModule {

  private IPropertyChangeCapable                       moduleObject;
  private Collection<? extends IPropertyChangeCapable> moduleObjects;

  /**
   * Gets the module's projected object.
   * 
   * @return the projected object.
   */
  public IPropertyChangeCapable getModuleObject() {
    return moduleObject;
  }

  /**
   * Sets the module's projected object.
   * 
   * @param moduleObject
   *          the projected object.
   */
  public void setModuleObject(IPropertyChangeCapable moduleObject) {
    if (ObjectUtils.equals(this.moduleObject, moduleObject)) {
      return;
    }
    Object oldValue = getModuleObject();
    this.moduleObject = moduleObject;
    firePropertyChange("moduleObject", oldValue, getModuleObject());
  }

  /**
   * Gets the module's projected objects.
   * 
   * @return the projected objects.
   */
  public Collection<? extends IPropertyChangeCapable> getModuleObjects() {
    return moduleObjects;
  }

  /**
   * Sets the module's projected object collection.
   * 
   * @param moduleObjects
   *          the projected object collection.
   */
  public void setModuleObjects(
      Collection<? extends IPropertyChangeCapable> moduleObjects) {
    if (ObjectUtils.equals(this.moduleObjects, moduleObjects)) {
      return;
    }
    Object oldValue = getModuleObjects();
    this.moduleObjects = moduleObjects;
    firePropertyChange("moduleObjects", oldValue, getModuleObjects());
  }

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
        .append(getModuleObjects(), rhs.getModuleObjects()).isEquals();
  }

  /**
   * Hash code based on projected object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 53).append(getModuleObject()).append(
        getModuleObjects()).toHashCode();
  }
}
