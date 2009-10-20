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

import java.util.Locale;
import java.util.regex.Pattern;

import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Default implementation of a string descriptor.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class BasicStringPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IStringPropertyDescriptor {

  private Integer maxLength;
  private String  regexpPattern;
  private String  regexpPatternSample;
  private Boolean upperCase;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicStringPropertyDescriptor clone() {
    BasicStringPropertyDescriptor clonedDescriptor = (BasicStringPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicStringPropertyDescriptor createQueryDescriptor() {
    BasicStringPropertyDescriptor queryDescriptor = (BasicStringPropertyDescriptor) super
        .createQueryDescriptor();
    // queryDescriptor.setMaxLength(null);
    queryDescriptor.setRegexpPattern(null);
    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return String.class;
  }

  /**
   * Gets the regexpPattern.
   * 
   * @return the regexpPattern.
   */
  public String getRegexpPattern() {
    return regexpPattern;
  }

  /**
   * Gets the regexpPatternSample.
   * 
   * @return the regexpPatternSample.
   */
  public String getRegexpPatternSample() {
    return regexpPatternSample;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object interceptSetter(Object component, Object newValue) {
    String actualNewValue = (String) newValue;
    if (isUpperCase()) {
      actualNewValue = actualNewValue.toUpperCase();
    }
    return super.interceptSetter(component, actualNewValue);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUpperCase() {
    if (upperCase != null) {
      return upperCase.booleanValue();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void preprocessSetter(final Object component, Object newValue) {
    super.preprocessSetter(component, newValue);
    final String propertyValueAsString = getValueAsString(newValue);
    if (propertyValueAsString != null && getMaxLength() != null
        && propertyValueAsString.length() > getMaxLength().intValue()) {
      IntegrityException ie = new IntegrityException("[" + getName()
          + "] value (" + propertyValueAsString + ") is too long on ["
          + component + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          StringBuffer boundsSpec = new StringBuffer("l");
          if (getMaxLength() != null) {
            boundsSpec.append(" &lt= ").append(getMaxLength());
          }
          return translationProvider.getTranslation(
              "integrity.property.toolong", new Object[] {
                  getI18nName(translationProvider, locale), boundsSpec,
                  component}, locale);
        }

      };
      throw ie;
    }
    if (propertyValueAsString != null && getRegexpPattern() != null
        && !Pattern.matches(getRegexpPattern(), propertyValueAsString)) {
      IntegrityException ie = new IntegrityException("[" + getName()
          + "] value (" + propertyValueAsString + ") does not match pattern ["
          + getRegexpPatternSample() + "] on [" + component + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          return translationProvider.getTranslation(
              "integrity.property.pattern", new Object[] {
                  getI18nName(translationProvider, locale),
                  getRegexpPatternSample(), component}, locale);
        }

      };
      throw ie;
    }
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
   * Sets the regexpPattern.
   * 
   * @param regexpPattern
   *          the regexpPattern to set.
   */
  public void setRegexpPattern(String regexpPattern) {
    this.regexpPattern = regexpPattern;
  }

  /**
   * Sets the regexpPatternSample.
   * 
   * @param regexpPatternSample
   *          the regexpPatternSample to set.
   */
  public void setRegexpPatternSample(String regexpPatternSample) {
    this.regexpPatternSample = regexpPatternSample;
  }

  /**
   * Sets the upperCase.
   * 
   * @param upperCase
   *          the upperCase to set.
   */
  public void setUpperCase(boolean upperCase) {
    this.upperCase = new Boolean(upperCase);
  }

  /**
   * Performs the necessary transformations to build a tring out of a property
   * value.
   * 
   * @param value
   *          the raw property value.
   * @return the resulting string.
   */
  protected String getValueAsString(Object value) {
    return (String) value;
  }
}
