/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import java.math.BigDecimal;
import java.util.Locale;

import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This is the abstract base descriptor of all numeric based properties.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicNumberPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements INumberPropertyDescriptor {

  private BigDecimal maxValue;
  private BigDecimal minValue;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicNumberPropertyDescriptor clone() {
    BasicNumberPropertyDescriptor clonedDescriptor = (BasicNumberPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPropertyDescriptor createQueryDescriptor() {
    BasicNumberPropertyDescriptor queryDescriptor = (BasicNumberPropertyDescriptor) super
        .createQueryDescriptor();
    queryDescriptor.setMinValue(null);
    queryDescriptor.setMaxValue(null);
    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public BigDecimal getMaxValue() {
    return maxValue;
  }

  /**
   * {@inheritDoc}
   */
  public BigDecimal getMinValue() {
    return minValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void preprocessSetter(final Object component, final Object newValue) {
    super.preprocessSetter(component, newValue);
    if (newValue != null) {
      if ((getMinValue() != null && compare(((Number) newValue), getMinValue()) < 0)
          || (getMaxValue() != null && compare(((Number) newValue),
              getMaxValue()) > 0)) {
        IntegrityException ie = new IntegrityException("[" + getName()
            + "] value is out of bounds on [" + component + "].") {

          private static final long serialVersionUID = 7459823123892198831L;

          @Override
          public String getI18nMessage(
              ITranslationProvider translationProvider, Locale locale) {
            StringBuffer boundsSpec = new StringBuffer();
            if (getMinValue() != null) {
              boundsSpec.append(getMinValue()).append(" <= ");
            }
            boundsSpec.append("x");
            if (getMaxValue() != null) {
              boundsSpec.append(" <= ").append(getMaxValue());
            }
            String messageKey = null;
            if (getMinValue() != null
                && compare(((Number) newValue), getMinValue()) < 0) {
              messageKey = "integrity.property.toosmall";
            } else {
              messageKey = "integrity.property.toobig";
            }
            return translationProvider.getTranslation(messageKey,
                new Object[] {getI18nName(translationProvider, locale),
                    boundsSpec, component}, locale);
          }

        };
        throw ie;
      }
    }
  }

  /**
   * Configures the upper bound of the allowed values. Default value is
   * <code>null</code>, meaning unbound.
   * 
   * @param maxValue
   *          the maxValue to set.
   */
  public void setMaxValue(BigDecimal maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Configures the lower bound of the allowed values. Default value is
   * <code>null</code>, meaning unbound.
   * 
   * @param minValue
   *          the minValue to set.
   */
  public void setMinValue(BigDecimal minValue) {
    this.minValue = minValue;
  }

  private int compare(Number value, BigDecimal bound) {
    if (value instanceof BigDecimal) {
      return ((BigDecimal) value).compareTo(bound);
    }
    return new BigDecimal(value.doubleValue()).compareTo(bound);
  }
}
