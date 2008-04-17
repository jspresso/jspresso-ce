/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;

/**
 * Abstract implementation of an enumeration descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractEnumerationPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IEnumerationPropertyDescriptor {

  private String  enumerationName;
  private Integer maxLength;

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractEnumerationPropertyDescriptor clone() {
    AbstractEnumerationPropertyDescriptor clonedDescriptor = (AbstractEnumerationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public String getEnumerationName() {
    if (enumerationName != null) {
      return enumerationName;
    }
    if (getParentDescriptor() != null) {
      return ((IEnumerationPropertyDescriptor) getParentDescriptor())
          .getEnumerationName();
    }
    return enumerationName;
  }

  /**
   * Gets the maxLength.
   * 
   * @return the maxLength.
   */
  public Integer getMaxLength() {
    if (maxLength != null) {
      return maxLength;
    }
    if (getParentDescriptor() != null) {
      return ((IEnumerationPropertyDescriptor) getParentDescriptor())
          .getMaxLength();
    }
    return maxLength;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return String.class;
  }

  /**
   * Sets the enumerationName.
   * 
   * @param enumerationName
   *            the enumerationName to set.
   */
  public void setEnumerationName(String enumerationName) {
    this.enumerationName = enumerationName;
  }

  /**
   * Sets the maxLength.
   * 
   * @param maxLength
   *            the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
}
