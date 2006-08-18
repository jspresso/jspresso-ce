/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Collection;
import java.util.List;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.integrity.IPropertyIntegrityProcessor;
import com.d2s.framework.util.IGate;
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

  private IPropertyDescriptor               parentDescriptor;

  private Boolean                           mandatory;
  private Boolean                           readOnly;
  private List<IPropertyIntegrityProcessor> integrityProcessors;
  private String                            delegateClassName;
  private Class                             delegateClass;
  private String                            unicityScope;
  private Collection<IGate>                 readabilityGates;
  private Collection<IGate>                 writabilityGates;

  /**
   * {@inheritDoc}
   */
  public boolean isMandatory() {
    if (mandatory != null) {
      return mandatory.booleanValue();
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().isMandatory();
    }
    return false;
  }

  /**
   * Sets the mandatory property.
   * 
   * @param mandatory
   *          the mandatory to set.
   */
  public void setMandatory(boolean mandatory) {
    this.mandatory = new Boolean(mandatory);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReadOnly() {
    if (readOnly != null) {
      return readOnly.booleanValue();
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().isReadOnly();
    }
    return false;
  }

  /**
   * Sets the readOnly property.
   * 
   * @param readOnly
   *          the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = new Boolean(readOnly);
  }

  /**
   * {@inheritDoc}
   */
  public List<IPropertyIntegrityProcessor> getIntegrityProcessors() {
    if (integrityProcessors != null) {
      return integrityProcessors;
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getIntegrityProcessors();
    }
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
      String className = getDelegateClassName();
      if (className != null) {
        try {
          delegateClass = Class.forName(className);
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
    if (delegateClassName != null) {
      return delegateClassName;
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getDelegateClassName();
    }
    return delegateClassName;
  }

  /**
   * {@inheritDoc}
   */
  public String getUnicityScope() {
    if (unicityScope != null) {
      return unicityScope;
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getUnicityScope();
    }
    return unicityScope;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    if (super.getDescription() != null) {
      return super.getDescription();
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getDescription();
    }
    return super.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    if (super.getName() != null) {
      return super.getName();
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getName();
    }
    return super.getName();
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
  public boolean isComputed() {
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

  /**
   * Gets the parentDescriptor.
   * 
   * @return the parentDescriptor.
   */
  protected IPropertyDescriptor getParentDescriptor() {
    return parentDescriptor;
  }

  /**
   * Sets the parentDescriptor.
   * 
   * @param parentDescriptor
   *          the parentDescriptor to set.
   */
  public void setParentDescriptor(IPropertyDescriptor parentDescriptor) {
    this.parentDescriptor = parentDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isOverload() {
    return parentDescriptor != null;
  }

  
  /**
   * Gets the readabilityGates.
   * 
   * @return the readabilityGates.
   */
  public Collection<IGate> getReadabilityGates() {
    if (readabilityGates != null) {
      return readabilityGates;
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getReadabilityGates();
    }
    return readabilityGates;
  }

  
  /**
   * Sets the readabilityGates.
   * 
   * @param readabilityGates the readabilityGates to set.
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  
  /**
   * Gets the writabilityGates.
   * 
   * @return the writabilityGates.
   */
  public Collection<IGate> getWritabilityGates() {
    if (writabilityGates != null) {
      return writabilityGates;
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getWritabilityGates();
    }
    return writabilityGates;
  }

  
  /**
   * Sets the writabilityGates.
   * 
   * @param writabilityGates the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }

}
