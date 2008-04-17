/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import java.util.Locale;

import org.jspresso.framework.model.descriptor.IDurationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.i18n.ITranslationProvider;


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
  public void preprocessSetter(final Object component, Object newValue) {
    super.preprocessSetter(component, newValue);
    if (newValue != null && getMaxMillis() != null
        && ((Long) newValue).longValue() > getMaxMillis().longValue()) {
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
  @Override
  public BasicDurationPropertyDescriptor clone() {
    BasicDurationPropertyDescriptor clonedDescriptor = (BasicDurationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
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
  public void setMaxMillis(Long maxMillis) {
    this.maxMillis = maxMillis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComparableQueryStructureDescriptor createQueryDescriptor() {
    BasicDurationPropertyDescriptor queryDescriptor = (BasicDurationPropertyDescriptor) super
        .createQueryDescriptor();
    queryDescriptor.setMaxMillis(null);
    return new ComparableQueryStructureDescriptor(queryDescriptor);
  }
}
