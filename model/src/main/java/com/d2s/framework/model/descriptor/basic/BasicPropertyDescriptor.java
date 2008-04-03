/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.util.bean.PropertyHelper;
import com.d2s.framework.util.bean.integrity.IPropertyProcessor;
import com.d2s.framework.util.bean.integrity.IntegrityException;
import com.d2s.framework.util.descriptor.DefaultDescriptor;
import com.d2s.framework.util.exception.NestedRuntimeException;
import com.d2s.framework.util.gate.IGate;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Default implementation of a property descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicPropertyDescriptor extends DefaultDescriptor
    implements IPropertyDescriptor {

  private Class<?>                                delegateClass;

  private String                                  delegateClassName;
  private List<IPropertyProcessor<?, ?>> integrityProcessors;
  private Boolean                                 mandatory;
  private IPropertyDescriptor                     parentDescriptor;
  private Collection<IGate>                       readabilityGates;
  private Boolean                                 readOnly;
  private String                                  unicityScope;
  private Collection<IGate>                       writabilityGates;

  /**
   * {@inheritDoc}
   */
  public void checkValueIntegrity(final Object component,
      final Object propertyValue) {
    if (isMandatory() && propertyValue == null) {
      IntegrityException ie = new IntegrityException("Mandatory property ["
          + getName() + "] on component [" + component + "].") {

        private static final long serialVersionUID = 5518554460713051123L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          return translationProvider.getTranslation(
              "integrity.property.mandatory", new Object[] {
                  getI18nName(translationProvider, locale), component}, locale);
        }

      };
      throw ie;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPropertyDescriptor clone() {
    return (BasicPropertyDescriptor) super.clone();
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
  public Class<?> getDelegateClass() {
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
  public List<IPropertyProcessor<?, ?>> getIntegrityProcessors() {
    if (integrityProcessors != null) {
      return integrityProcessors;
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getIntegrityProcessors();
    }
    return integrityProcessors;
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
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return getName().hashCode();
  }

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
   * {@inheritDoc}
   */
  public boolean isModifiable() {
    if (getDelegateClassName() == null) {
      return true;
    }
    try {
      return PropertyHelper.getPropertyDescriptor(
          Class.forName(getDelegateClassName()), getName()).getWriteMethod() != null;
    } catch (ClassNotFoundException ex) {
      throw new NestedRuntimeException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean isOverload() {
    return parentDescriptor != null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isQueryable() {
    return false;
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
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void postprocessSetter(Object component, Object oldValue,
      Object newValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      ((IPropertyProcessor<Object, Object>) processor)
          .postprocessSetter(component, oldValue, newValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Object interceptSetter(Object component, Object oldValue,
      Object newValue) {
    Object interceptedValue = newValue;
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return interceptedValue;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      interceptedValue = ((IPropertyProcessor<Object, Object>) processor)
          .interceptSetter(component, oldValue, interceptedValue);
    }
    return interceptedValue;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void preprocessSetter(Object component, Object oldValue,
      Object newValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      ((IPropertyProcessor<Object, Object>) processor)
          .preprocessSetter(component, oldValue, newValue);
    }
  }

  /**
   * Sets the delegate class name.
   * 
   * @param delegateClassName
   *            The class name of the extension delegate used to compute this
   *            property.
   */
  public void setDelegateClassName(String delegateClassName) {
    this.delegateClassName = delegateClassName;
  }

  /**
   * Sets the integrityProcessors.
   * 
   * @param integrityProcessors
   *            the integrityProcessors to set.
   */
  public void setIntegrityProcessors(
      List<IPropertyProcessor<?, ?>> integrityProcessors) {
    this.integrityProcessors = integrityProcessors;
  }

  /**
   * Sets the mandatory property.
   * 
   * @param mandatory
   *            the mandatory to set.
   */
  public void setMandatory(boolean mandatory) {
    this.mandatory = new Boolean(mandatory);
  }

  /**
   * Sets the parentDescriptor.
   * 
   * @param parentDescriptor
   *            the parentDescriptor to set.
   */
  public void setParentDescriptor(IPropertyDescriptor parentDescriptor) {
    this.parentDescriptor = parentDescriptor;
  }

  /**
   * Sets the readabilityGates.
   * 
   * @param readabilityGates
   *            the readabilityGates to set.
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * Sets the readOnly property.
   * 
   * @param readOnly
   *            the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = new Boolean(readOnly);
  }

  /**
   * Sets the unicityScope.
   * 
   * @param unicityScope
   *            the unicityScope to set.
   */
  public void setUnicityScope(String unicityScope) {
    this.unicityScope = unicityScope;
  }

  /**
   * Sets the writabilityGates.
   * 
   * @param writabilityGates
   *            the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  public BasicPropertyDescriptor createQueryDescriptor() {
    BasicPropertyDescriptor queryPropertyDescriptor = clone();
    queryPropertyDescriptor.setMandatory(false);
    queryPropertyDescriptor.setReadOnly(false);
    return queryPropertyDescriptor;
  }

  /**
   * Gets the parentDescriptor.
   * 
   * @return the parentDescriptor.
   */
  protected IPropertyDescriptor getParentDescriptor() {
    return parentDescriptor;
  }
}
