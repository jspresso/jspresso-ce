/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Locale;

import com.d2s.framework.model.descriptor.IStringPropertyDescriptor;
import com.d2s.framework.model.integrity.IntegrityException;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Default implementation of a string descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicStringPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IStringPropertyDescriptor {

  private Integer maxLength;

  /**
   * {@inheritDoc}
   */
  public Integer getMaxLength() {
    if (maxLength != null) {
      return maxLength;
    }
    if (getParentDescriptor() != null) {
      return ((IStringPropertyDescriptor) getParentDescriptor()).getMaxLength();
    }
    return maxLength;
  }

  /**
   * Sets the maxLength property.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * {@inheritDoc}
   */
  public Class getModelType() {
    return String.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkValueIntegrity(final Object component,
      final Object propertyValue) {
    super.checkValueIntegrity(component, propertyValue);
    if (propertyValue != null && getMaxLength() != null
        && ((String) propertyValue).length() > getMaxLength().intValue()) {
      IntegrityException ie = new IntegrityException("[" + getName()
          + "] value is too long on [" + component + "].") {

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
