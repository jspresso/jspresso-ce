/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import java.util.Locale;

import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.i18n.ITranslationProvider;


/**
 * Default implementation of a number descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicNumberPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements INumberPropertyDescriptor {

  private Double maxValue;
  private Double minValue;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void preprocessSetter(final Object component, final Object newValue) {
    super.preprocessSetter(component, newValue);
    if (newValue != null) {
      if ((getMinValue() != null && ((Number) newValue).doubleValue() < getMinValue()
          .doubleValue())
          || (getMaxValue() != null && ((Number) newValue).doubleValue() > getMaxValue()
              .doubleValue())) {
        IntegrityException ie = new IntegrityException("[" + getName()
            + "] value is out of bounds on [" + component + "].") {

          private static final long serialVersionUID = 7459823123892198831L;

          @Override
          public String getI18nMessage(
              ITranslationProvider translationProvider, Locale locale) {
            StringBuffer boundsSpec = new StringBuffer();
            if (getMinValue() != null) {
              boundsSpec.append(getMinValue()).append(" &lt= ");
            }
            boundsSpec.append("x");
            if (getMaxValue() != null) {
              boundsSpec.append(" &lt= ").append(getMaxValue());
            }
            String messageKey = null;
            if ((getMinValue() != null && ((Number) newValue)
                .doubleValue() < getMinValue().doubleValue())) {
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
  public Double getMaxValue() {
    if (maxValue != null) {
      return maxValue;
    }
    if (getParentDescriptor() != null) {
      return ((INumberPropertyDescriptor) getParentDescriptor()).getMaxValue();
    }
    return maxValue;
  }

  /**
   * {@inheritDoc}
   */
  public Double getMinValue() {
    if (minValue != null) {
      return minValue;
    }
    if (getParentDescriptor() != null) {
      return ((INumberPropertyDescriptor) getParentDescriptor()).getMinValue();
    }
    return minValue;
  }

  /**
   * Sets the maxValue property.
   * 
   * @param maxValue
   *            the maxValue to set.
   */
  public void setMaxValue(Double maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minValue property.
   * 
   * @param minValue
   *            the minValue to set.
   */
  public void setMinValue(Double minValue) {
    this.minValue = minValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComparableQueryStructureDescriptor createQueryDescriptor() {
    BasicNumberPropertyDescriptor queryDescriptor = (BasicNumberPropertyDescriptor) super
        .createQueryDescriptor();
    queryDescriptor.setMinValue(null);
    queryDescriptor.setMaxValue(null);
    return new ComparableQueryStructureDescriptor(queryDescriptor);
  }
}
