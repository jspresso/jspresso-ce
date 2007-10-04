/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.model;

import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A bean collection module is a module dealing with a collection of beans.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionModule extends SubModule {

  private IViewDescriptor elementViewDescriptor;
  private Collection<?>   moduleObjects;

  /**
   * Equality based on projected object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BeanCollectionModule)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    BeanCollectionModule rhs = (BeanCollectionModule) obj;
    return new EqualsBuilder().append(getModuleObjects(),
        rhs.getModuleObjects()).isEquals();
  }

  /**
   * Gets the elementViewDescriptor.
   * 
   * @return the elementViewDescriptor.
   */
  public IViewDescriptor getElementViewDescriptor() {
    return elementViewDescriptor;
  }

  /**
   * Gets the module's projected objects.
   * 
   * @return the projected objects.
   */
  public Collection<?> getModuleObjects() {
    return moduleObjects;
  }

  /**
   * Hash code based on projected object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 53).append(getModuleObjects()).toHashCode();
  }

  /**
   * Sets the elementViewDescriptor.
   * 
   * @param elementViewDescriptor
   *            the elementViewDescriptor to set.
   */
  public void setElementViewDescriptor(IViewDescriptor elementViewDescriptor) {
    this.elementViewDescriptor = elementViewDescriptor;
  }

  /**
   * Sets the module's projected object collection.
   * 
   * @param moduleObjects
   *            the projected object collection.
   */
  public void setModuleObjects(Collection<?> moduleObjects) {
    if (ObjectUtils.equals(this.moduleObjects, moduleObjects)) {
      return;
    }
    Object oldValue = getModuleObjects();
    this.moduleObjects = moduleObjects;
    firePropertyChange("moduleObjects", oldValue, getModuleObjects());
  }
}
