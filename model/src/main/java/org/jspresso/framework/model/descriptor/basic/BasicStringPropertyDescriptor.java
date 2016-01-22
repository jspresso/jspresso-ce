/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
 * Describes a string based property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicStringPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IStringPropertyDescriptor {

  private Integer maxLength;
  private String  regexpPattern;
  private String  regexpPatternSample;
  private Boolean upperCase;
  private Boolean translatable;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicStringPropertyDescriptor clone() {
    BasicStringPropertyDescriptor clonedDescriptor = (BasicStringPropertyDescriptor) super.clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicStringPropertyDescriptor createQueryDescriptor() {
    BasicStringPropertyDescriptor queryDescriptor = (BasicStringPropertyDescriptor) super.createQueryDescriptor();
    // Don't set it to null because in that case, the default max length will apply.
    queryDescriptor.setMaxLength(-1);
    queryDescriptor.setRegexpPattern(null);
    queryDescriptor.setTranslatable(false);
    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getMaxLength() {
    if (maxLength != null) {
      return maxLength;
    }
    if (isComputed()) {
      return null;
    }
    return getDefaultMaxLength();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return String.class;
  }

  /**
   * Gets the regexpPattern.
   *
   * @return the regexpPattern.
   */
  @Override
  public String getRegexpPattern() {
    return regexpPattern;
  }

  /**
   * Gets the regexpPatternSample.
   *
   * @return the regexpPatternSample.
   */
  @Override
  public String getRegexpPatternSample() {
    return regexpPatternSample;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object interceptSetter(Object component, Object newValue) {
    String actualNewValue = (String) newValue;
    if (actualNewValue != null && isUpperCase()) {
      actualNewValue = actualNewValue.toUpperCase();
    }
    return super.interceptSetter(component, actualNewValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUpperCase() {
    if (upperCase != null) {
      return upperCase;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTranslatable() {
    if (translatable != null) {
      return translatable;
    }
    return false;
  }

  @Override
  public void preprocessSetter(final Object component, final Object newValue) {
    super.preprocessSetter(component, newValue);
    final String propertyValueAsString = getValueAsString(newValue);
    final Integer maxL = getMaxLength();
    if (propertyValueAsString != null && maxL != null && maxL > 0
        && propertyValueAsString.length() > maxL) {
      IntegrityException ie = new IntegrityException("[" + getName()
          + "] value (" + propertyValueAsString + ") is too long on ["
          + component + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider,
            Locale locale) {
          StringBuilder boundsSpec = new StringBuilder("l");
          boundsSpec.append(" <= ").append(maxL);
          return translationProvider.getTranslation(
              "integrity.property.toolong", new Object[] {
                  getI18nName(translationProvider, locale), boundsSpec,
                  component
              }, locale);
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
                  getRegexpPatternSample(), component
              }, locale);
        }

      };
      throw ie;
    }
  }

  /**
   * Configures the maximum string length this property allows. Default value is
   * {@code null} which means unlimited.
   *
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * Configures the regular expression pattern this string property allows.
   * Default is {@code null} which means constraint free.
   *
   * @param regexpPattern
   *          the regexpPattern to set.
   */
  public void setRegexpPattern(String regexpPattern) {
    this.regexpPattern = regexpPattern;
  }

  /**
   * Allows for providing a conforming sample for the regular expression
   * pattern. This human-readable example is used when the end-user has to be
   * notified that the incoming property value does not match the pattern
   * constraint.
   *
   * @param regexpPatternSample
   *          the regexpPatternSample to set.
   */
  public void setRegexpPatternSample(String regexpPatternSample) {
    this.regexpPatternSample = regexpPatternSample;
  }

  /**
   * This is a shortcut to implement the common use-case of handling upper-case
   * only properties. all incoming values will be transformed to uppercase as if
   * a property processor was registered to perform the transformation.
   *
   * @param upperCase
   *          the upperCase to set.
   */
  public void setUpperCase(boolean upperCase) {
    this.upperCase = upperCase;
  }

  /**
   * Configures the string property to be translatable. Jspresso will then implement a translation store for the
   * property so that it can be displayed in the connected user language. This is particularly useful for
   * non-european character sets like chinese, indian, and so on. In that case,
   * only the &quot;raw&quot; untranslated property value is stored in the object itself. the various translations
   * are stored in an extra store. translated properties support searching, ordering,
   * ... exactly like non-translatable properties.
   *
   * @param translatable the translatable
   */
  public void setTranslatable(boolean translatable) {
    this.translatable = translatable;
  }

  /**
   * Performs the necessary transformations to build a string out of a property
   * value.
   *
   * @param value
   *          the raw property value.
   * @return the resulting string.
   */
  protected String getValueAsString(Object value) {
    return (String) value;
  }

  /**
   * Gets default max length.
   *
   * @return the default max length
   */
  protected Integer getDefaultMaxLength() {
    return 255;
  }
}
