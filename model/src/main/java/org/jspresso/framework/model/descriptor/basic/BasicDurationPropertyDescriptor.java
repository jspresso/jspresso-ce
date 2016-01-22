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

import org.jspresso.framework.model.descriptor.IDurationPropertyDescriptor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Describes a property used to store a duration value. Duration is stored in
 * the form of a number of milliseconds. duration properties are cleanly handled
 * by Jspresso UI layer for both displaying / editing duration properties in a
 * convenient human format.
 *
 * @author Vincent Vandenschrick
 */
public class BasicDurationPropertyDescriptor extends BasicTimeAwarePropertyDescriptor
    implements IDurationPropertyDescriptor {

  private Long maxMillis;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicDurationPropertyDescriptor clone() {
    BasicDurationPropertyDescriptor clonedDescriptor = (BasicDurationPropertyDescriptor) super.clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPropertyDescriptor createQueryDescriptor() {
    BasicDurationPropertyDescriptor queryDescriptor = (BasicDurationPropertyDescriptor) super.createQueryDescriptor();
    queryDescriptor.setMaxMillis(null);
    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getMaxMillis() {
    return maxMillis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return Long.class;
  }

  /**
   * Handles max value.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void preprocessSetter(final Object component, final Object newValue) {
    super.preprocessSetter(component, newValue);
    if (newValue != null && getMaxMillis() != null && (Long) newValue > getMaxMillis()) {
      IntegrityException ie = new IntegrityException("[" + getName() + "] value is too high on [" + component + "].") {

        private static final long serialVersionUID = 7459823123892198831L;

        @Override
        public String getI18nMessage(ITranslationProvider translationProvider, Locale locale) {
          StringBuilder boundsSpec = new StringBuilder();
          boundsSpec.append("x");
          if (getMaxMillis() != null) {
            boundsSpec.append(" <= ").append(getMaxMillis());
          }
          return translationProvider.getTranslation("integrity.property.toobig",
              new Object[]{getI18nName(translationProvider, locale), boundsSpec, component}, locale);
        }

      };
      throw ie;
    }
  }

  /**
   * Configures the maximum duration value this property accepts in
   * milliseconds. Default value is {@code null}, meaning unbound.
   *
   * @param maxMillis
   *     the maxMillis to set.
   */
  public void setMaxMillis(Long maxMillis) {
    this.maxMillis = maxMillis;
  }

  /**
   * Is default filter comparable.
   *
   * @return {@code true}
   */
  @Override
  protected boolean isDefaultFilterComparable() {
    return true;
  }
}
