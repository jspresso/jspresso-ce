/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Locale;

import com.d2s.framework.model.descriptor.IDurationPropertyDescriptor;
import com.d2s.framework.util.bean.integrity.IntegrityException;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Default implementation of a duration descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicDurationPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IDurationPropertyDescriptor {

  private Long maxMillis;

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkValueIntegrity(final Object component,
      final Object propertyValue) {
    super.checkValueIntegrity(component, propertyValue);
    if (propertyValue != null && getMaxMillis() != null
        && ((Long) propertyValue).longValue() > getMaxMillis().longValue()) {
      IntegrityException ie = new IntegrityException("[" + getName()
          + "] value is too high on [" + component + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          StringBuffer boundsSpec = new StringBuffer();
          boundsSpec.append("x");
          if (getMaxMillis() != null) {
            boundsSpec.append(" <= ").append(getMaxMillis());
          }
          return translationProvider.getTranslation(
              "integrity.property.toobig", new Object[] {
                  getI18nName(translationProvider, locale), boundsSpec,
                  component}, locale);
        }

      };
      throw ie;
    }
  }

  /**
   * {@inheritDoc}
   */
  public Long getMaxMillis() {
    if (maxMillis != null) {
      return maxMillis;
    }
    if (getParentDescriptor() != null) {
      return ((IDurationPropertyDescriptor) getParentDescriptor())
          .getMaxMillis();
    }
    return maxMillis;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return Long.class;
  }

  /**
   * Sets the maxMillis property.
   * 
   * @param maxMillis
   *            the maxMillis to set.
   */
  public void setMaxMillis(long maxMillis) {
    this.maxMillis = new Long(maxMillis);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicDurationPropertyDescriptor clone() {
    BasicDurationPropertyDescriptor clonedDescriptor = (BasicDurationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
