/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.List;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.integrity.IPropertyIntegrityProcessor;
import com.d2s.framework.util.descriptor.DefaultDescriptor;
import com.d2s.framework.util.exception.NestedRuntimeException;

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
  private String                            delegateClassName;
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
    if (delegateClass == null) {
      if (delegateClassName != null) {
        try {
          delegateClass = Class.forName(delegateClassName);
        } catch (ClassNotFoundException ex) {
          throw new NestedRuntimeException(ex);
        }
      }
    }
    return delegateClass;
  }

  /**
   * Sets the delegate class name.
   * 
   * @param delegateClassName
   *          The class name of the extension delegate used to compute this
   *          property.
   */
  public void setDelegateClassName(String delegateClassName) {
    this.delegateClassName = delegateClassName;
  }

  /**
   * Sets the delegate class name.
   * 
   * @return The class name of the extension delegate used to compute this
   *         property.
   */
  public String getDelegateClassName() {
    return delegateClassName;
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

  /**
   * {@inheritDoc}
   */
  public boolean isReadOnly() {
    return getDelegateClassName() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IPropertyDescriptor) {
      return getName().equals(((IPropertyDescriptor) obj).getName());
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isQueryable() {
    return false;
  }

}
