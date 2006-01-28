/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.projection;

import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * A bean projection is the base class of bean related projections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanProjection extends ChildProjection {

  private IPropertyChangeCapable                       projectedObject;
  private Collection<? extends IPropertyChangeCapable> projectedObjects;

  /**
   * Gets the projection's projected object.
   * 
   * @return the projected object.
   */
  public IPropertyChangeCapable getProjectedObject() {
    return projectedObject;
  }

  /**
   * Sets the projection's projected object.
   * 
   * @param projectedObject
   *          the projected object.
   */
  public void setProjectedObject(IPropertyChangeCapable projectedObject) {
    if (ObjectUtils.equals(this.projectedObject, projectedObject)) {
      return;
    }
    Object oldValue = getProjectedObject();
    this.projectedObject = projectedObject;
    firePropertyChange("projectedObject", oldValue, getProjectedObject());
  }

  /**
   * Gets the projection's projected objects.
   * 
   * @return the projected objects.
   */
  public Collection<? extends IPropertyChangeCapable> getProjectedObjects() {
    return projectedObjects;
  }

  /**
   * Sets the projection's projected object collection.
   * 
   * @param projectedObjects
   *          the projected object collection.
   */
  public void setProjectedObjects(
      Collection<? extends IPropertyChangeCapable> projectedObjects) {
    if (ObjectUtils.equals(this.projectedObjects, projectedObjects)) {
      return;
    }
    Object oldValue = getProjectedObjects();
    this.projectedObjects = projectedObjects;
    firePropertyChange("projectedObjects", oldValue, getProjectedObjects());
  }

  /**
   * Equality based on projected object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BeanProjection)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    BeanProjection rhs = (BeanProjection) obj;
    return new EqualsBuilder().append(getProjectedObject(),
        rhs.getProjectedObject()).append(getProjectedObjects(),
        rhs.getProjectedObjects()).isEquals();
  }

  /**
   * Hash code based on projected object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 53).append(getProjectedObject()).append(
        getProjectedObjects()).toHashCode();
  }
}
