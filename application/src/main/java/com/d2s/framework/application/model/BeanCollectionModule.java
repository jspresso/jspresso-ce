/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model;

import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A bean collection module is a module dealing with a collection of beans.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionModule extends Module {

  private IComponentDescriptor<Object> elementComponentDescriptor;
  private IViewDescriptor              elementViewDescriptor;
  private Collection<?>                moduleObjects;

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
    // do not rely on object equality (null lists would make it equal)
    // return new EqualsBuilder().append(getModuleObjects(),
    // rhs.getModuleObjects()).isEquals();
    return ObjectUtils.equals(getName(), rhs.getName());
  }

  /**
   * Gets the elementComponentDescriptor.
   * 
   * @return the elementComponentDescriptor.
   */
  public IComponentDescriptor<Object> getElementComponentDescriptor() {
    return elementComponentDescriptor;
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
    return new HashCodeBuilder(23, 53).append(getName()).toHashCode();
  }

  /**
   * Sets the elementComponentDescriptor.
   * 
   * @param elementComponentDescriptor
   *            the elementComponentDescriptor to set.
   */
  public void setElementComponentDescriptor(
      IComponentDescriptor<Object> elementComponentDescriptor) {
    this.elementComponentDescriptor = elementComponentDescriptor;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageUrl = super.getIconImageURL();
    if (iconImageUrl == null) {
      iconImageUrl = getElementComponentDescriptor().getIconImageURL();
      setIconImageURL(iconImageUrl);
    }
    return iconImageUrl;
  }
}
