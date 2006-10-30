/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Locale;

import com.d2s.framework.model.descriptor.IDurationPropertyDescriptor;
import com.d2s.framework.model.integrity.IntegrityException;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Default implementation of a duration descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
  public long getMaxMillis() {
    if (maxMillis != null) {
      return maxMillis.longValue();
    }
    if (getParentDescriptor() != null) {
      return ((IDurationPropertyDescriptor) getParentDescriptor())
          .getMaxMillis();
    }
    return 0;
  }

  /**
   * Sets the maxMillis property.
   *
   * @param maxMillis
   *          the maxMillis to set.
   */
  public void setMaxMillis(long maxMillis) {
    this.maxMillis = new Long(maxMillis);
  }

  /**
   * {@inheritDoc}
   */
  public Class getModelType() {
    return Long.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkValueIntegrity(final Object component,
      final Object propertyValue) {
    super.checkValueIntegrity(component, propertyValue);
    if (propertyValue != null && getMaxMillis() > 0
        && ((Long) propertyValue).longValue() > getMaxMillis()) {
      IntegrityException ie = new IntegrityException("[" + getName()
          + "] value is too high on [" + component + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          return translationProvider.getTranslation(
              "integrity.property.outofbounds", new Object[] {
                  getI18nName(translationProvider, locale), component}, locale);
        }

      };
      throw ie;
    }
  }
}
