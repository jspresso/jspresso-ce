/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.util.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Default implementation of a property descriptor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicPropertyDescriptor extends DefaultDescriptor
    implements IPropertyDescriptor, BeanFactoryAware {

  private BeanFactory                    beanFactory;

  private Class<?>                       delegateClass;
  private String                         delegateClassName;

  private Boolean                        delegateWritable;
  private Collection<String>             grantedRoles;

  private List<String>                   integrityProcessorClassNames;
  private List<String>                   integrityProcessorBeanNames;

  private List<IPropertyProcessor<?, ?>> integrityProcessors;
  private Boolean                        mandatory;
  private Collection<IGate>              readabilityGates;
  private Boolean                        readOnly;
  private String                         unicityScope;

  private Collection<IGate>              writabilityGates;
  private boolean                        computed;

  private String                         sqlName;

  /**
   * Constructs a new <code>BasicPropertyDescriptor</code> instance.
   */
  public BasicPropertyDescriptor() {
    computed = false;
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
  public BasicPropertyDescriptor createQueryDescriptor() {
    BasicPropertyDescriptor queryPropertyDescriptor = clone();
    queryPropertyDescriptor.setMandatory(false);
    queryPropertyDescriptor.setReadOnly(false);
    queryPropertyDescriptor.integrityProcessors = null;
    queryPropertyDescriptor.setIntegrityProcessorClassNames(null);
    return queryPropertyDescriptor;
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
    return delegateClassName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return super.getDescription();
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
   * {@inheritDoc}
   */
  public List<IPropertyProcessor<?, ?>> getIntegrityProcessors() {
    registerIntegrityProcessorsIfNecessary();
    return integrityProcessors;
  }

  /**
   * Gets the readabilityGates.
   * 
   * @return the readabilityGates.
   */
  public Collection<IGate> getReadabilityGates() {
    return readabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  public String getUnicityScope() {
    return unicityScope;
  }

  /**
   * Gets the writabilityGates.
   * 
   * @return the writabilityGates.
   */
  public Collection<IGate> getWritabilityGates() {
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
  public boolean isMandatory() {
    if (mandatory != null) {
      return mandatory.booleanValue();
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
   * Sets the delegateWritable.
   * 
   * @param delegateWritable
   *          the delegateWritable to set.
   */
  public void setDelegateWritable(boolean delegateWritable) {
    this.delegateWritable = new Boolean(delegateWritable);
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Sets the integrityProcessorClassNames.
   * 
   * @param integrityProcessorClassNames
   *          the integrityProcessorClassNames to set.
   */
  public void setIntegrityProcessorClassNames(
      List<String> integrityProcessorClassNames) {
    this.integrityProcessorClassNames = StringUtils
        .ensureSpaceFree(integrityProcessorClassNames);
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

  private synchronized void registerIntegrityProcessorsIfNecessary() {
    if (integrityProcessorClassNames != null) {
      // process creation of integrity processors.
      for (String integrityProcessorClassName : integrityProcessorClassNames) {
        try {
          registerIntegrityProcessor((IPropertyProcessor<?, ?>) Class.forName(
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
    if (integrityProcessorBeanNames != null && beanFactory != null) {
      // process creation of integrity processors.
      for (String integrityProcessorBeanName : integrityProcessorBeanNames) {
        registerIntegrityProcessor((IPropertyProcessor<?, ?>) beanFactory
            .getBean(integrityProcessorBeanName, IPropertyProcessor.class));
      }
      integrityProcessorClassNames = null;
    }
  }

  private void registerIntegrityProcessor(
      IPropertyProcessor<?, ?> integrityProcessor) {
    if (integrityProcessors == null) {
      integrityProcessors = new ArrayList<IPropertyProcessor<?, ?>>();
    }
    integrityProcessors.add(integrityProcessor);
  }

  /**
   * Sets the beanFactory.
   * 
   * @param beanFactory
   *          the beanFactory to set.
   */
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  /**
   * Sets the integrityProcessorBeanNames.
   * 
   * @param integrityProcessorBeanNames
   *          the integrityProcessorBeanNames to set.
   */
  public void setIntegrityProcessorBeanNames(
      List<String> integrityProcessorBeanNames) {
    this.integrityProcessorBeanNames = StringUtils
        .ensureSpaceFree(integrityProcessorBeanNames);
  }

  /**
   * Returns true if a delegate class is registered to compute the property
   * value. A property can be made <code>computed</code> even if its delegate
   * class is null by calling <code>setComputed(true)</code>. This way, the
   * property should be ignored by the ORM.
   * <p>
   * {@inheritDoc}
   */
  public boolean isComputed() {
    return getDelegateClassName() != null || computed;
  }

  /**
   * Sets the computed.
   * 
   * @param computed
   *          the computed to set.
   */
  public void setComputed(boolean computed) {
    this.computed = computed;
  }

  /**
   * Sets the sqlName.
   * 
   * @param sqlName
   *          the sqlName to set.
   */
  public void setSqlName(String sqlName) {
    this.sqlName = sqlName;
  }

  /**
   * Gets the sqlName.
   * 
   * @return the sqlName.
   */
  public String getSqlName() {
    return sqlName;
  }
}
