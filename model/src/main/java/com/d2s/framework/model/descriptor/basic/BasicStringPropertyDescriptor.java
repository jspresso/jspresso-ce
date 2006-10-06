/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Locale;
import java.util.regex.Pattern;

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
  private String  regexpPattern;

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
   * Gets the regexpPattern.
   * 
   * @return the regexpPattern.
   */
  public String getRegexpPattern() {
    if (regexpPattern != null) {
      return regexpPattern;
    }
    if (getParentDescriptor() != null) {
      return ((IStringPropertyDescriptor) getParentDescriptor())
          .getRegexpPattern();
    }
    return regexpPattern;
  }

  /**
   * Sets the regexpPattern.
   * 
   * @param regexpPattern
   *          the regexpPattern to set.
   */
  public void setRegexpPattern(String regexpPattern) {
    this.regexpPattern = regexpPattern;
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
          + "] value (" + propertyValue + ") is too long on [" + component
          + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          return translationProvider.getTranslation(
              "integrity.property.outofbounds", new Object[] {
                  getI18nName(translationProvider, locale), propertyValue, component}, locale);
        }

      };
      throw ie;
    }
    if (propertyValue != null && getRegexpPattern() != null
        && !Pattern.matches(getRegexpPattern(), (String) propertyValue)) {
      IntegrityException ie = new IntegrityException("[" + getName()
          + "] value (" + propertyValue + ") does not match pattern ["
          + getRegexpPattern() + "] on [" + component + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          return translationProvider.getTranslation(
              "integrity.property.pattern", new Object[] {
                  getI18nName(translationProvider, locale), propertyValue, getRegexpPattern(),
                  component}, locale);
        }

      };
      throw ie;
    }
  }
}
