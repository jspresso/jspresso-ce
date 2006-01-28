/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.List;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.integrity.IPropertyIntegrityProcessor;
import com.d2s.framework.util.descriptor.DefaultDescriptor;

/**
 * Default implementation of a property descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicPropertyDescriptor extends DefaultDescriptor
    implements IPropertyDescriptor {

  private boolean                           mandatory;
  private List<IPropertyIntegrityProcessor> integrityProcessors;
  private Class                             delegateClass;
  private String                            unicityScope;

  /**
   * {@inheritDoc}
   */
  public boolean isMandatory() {
    return mandatory;
  }

  /**
   * Sets the mandatory property.
   * 
   * @param mandatory
   *          the mandatory to set.
   */
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  /**
   * {@inheritDoc}
   */
  public List<IPropertyIntegrityProcessor> getIntegrityProcessors() {
    return integrityProcessors;
  }

  /**
   * Sets the integrityProcessors.
   * 
   * @param integrityProcessors
   *          the integrityProcessors to set.
   */
  public void setIntegrityProcessors(
      List<IPropertyIntegrityProcessor> integrityProcessors) {
    this.integrityProcessors = integrityProcessors;
  }

  /**
   * {@inheritDoc}
   */
  public Class getDelegateClass() {
    return delegateClass;
  }

  /**
   * Sets the extensionDelegate.
   * 
   * @param delegateClass
   *          The class of the extension delegate used to compute this property.
   */
  public void setDelegateClass(Class delegateClass) {
    this.delegateClass = delegateClass;
  }

  /**
   * {@inheritDoc}
   */
  public String getUnicityScope() {
    return unicityScope;
  }

  /**
   * Sets the unicityScope.
   * 
   * @param unicityScope
   *          the unicityScope to set.
   */
  public void setUnicityScope(String unicityScope) {
    this.unicityScope = unicityScope;
  }
}
