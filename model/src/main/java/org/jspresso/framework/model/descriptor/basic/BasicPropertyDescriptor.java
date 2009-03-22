/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.jspresso.framework.model.descriptor.DescriptorException;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.bean.integrity.IPropertyProcessor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Default implementation of a property descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicPropertyDescriptor extends DefaultDescriptor
    implements IPropertyDescriptor {

  private String                         delegateClassName;
  private Class<?>                       delegateClass;

  private List<String>                   integrityProcessorClassNames;
  private List<IPropertyProcessor<?, ?>> integrityProcessors;

  private Boolean                        mandatory;
  private IPropertyDescriptor            parentDescriptor;
  private Collection<IGate>              readabilityGates;
  private Boolean                        readOnly;
  private String                         unicityScope;
  private Collection<IGate>              writabilityGates;
  private Collection<String>             grantedRoles;

  private Boolean                        delegateWritable;

  /**
   * Sets the delegateWritable.
   * 
   * @param delegateWritable
   *          the delegateWritable to set.
   */
  public void setDelegateWritable(boolean delegateWritable) {
    this.delegateWritable = new Boolean(delegateWritable);
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
    registerIntegrityProcessorsIfNecessary();
    if (integrityProcessors != null) {
      return integrityProcessors;
    }
    if (getParentDescriptor() != null) {
      return getParentDescriptor().getIntegrityProcessors();
    }
    return integrityProcessors;
  }

  private synchronized void registerIntegrityProcessorsIfNecessary() {
    if (integrityProcessorClassNames != null) {
      // process creation of integrity processors.
      integrityProcessors = new ArrayList<IPropertyProcessor<?, ?>>();
      for (String integrityProcessorClassName : integrityProcessorClassNames) {
        try {
          integrityProcessors.add((IPropertyProcessor<?, ?>) Class.forName(
              integrityProcessorClassName).newInstance());
        } catch (InstantiationException ex) {
          throw new DescriptorException(ex);
        } catch (IllegalAccessException ex) {
          throw new DescriptorException(ex);
        } catch (ClassNotFoundException ex) {
          throw new DescriptorException(ex);
        }
      }
      integrityProcessorClassNames = null;
    }
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
    if (delegateWritable != null) {
      return delegateWritable.booleanValue();
    }
    return false;
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
      ((IPropertyProcessor<Object, Object>) processor).postprocessSetter(
          component, oldValue, newValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Object interceptSetter(Object component, Object newValue) {
    Object interceptedValue = newValue;
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return interceptedValue;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      interceptedValue = ((IPropertyProcessor<Object, Object>) processor)
          .interceptSetter(component, interceptedValue);
    }
    return interceptedValue;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void preprocessSetter(final Object component, Object newValue) {
    if (isMandatory() && newValue == null) {
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
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      ((IPropertyProcessor<Object, Object>) processor).preprocessSetter(
          component, newValue);
    }
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
   * Sets the integrityProcessorClassNames.
   * 
   * @param integrityProcessorClassNames
   *          the integrityProcessorClassNames to set.
   */
  public void setIntegrityProcessorClassNames(
      List<String> integrityProcessorClassNames) {
    this.integrityProcessorClassNames = integrityProcessorClassNames;
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
   * Sets the parentDescriptor.
   * 
   * @param parentDescriptor
   *          the parentDescriptor to set.
   */
  public void setParentDescriptor(IPropertyDescriptor parentDescriptor) {
    this.parentDescriptor = parentDescriptor;
  }

  /**
   * Sets the readabilityGates.
   * 
   * @param readabilityGates
   *          the readabilityGates to set.
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
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
   * Sets the unicityScope.
   * 
   * @param unicityScope
   *          the unicityScope to set.
   */
  public void setUnicityScope(String unicityScope) {
    this.unicityScope = unicityScope;
  }

  /**
   * Sets the writabilityGates.
   * 
   * @param writabilityGates
   *          the writabilityGates to set.
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
    queryPropertyDescriptor.integrityProcessors = null;
    queryPropertyDescriptor.setIntegrityProcessorClassNames(null);
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

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }
}
