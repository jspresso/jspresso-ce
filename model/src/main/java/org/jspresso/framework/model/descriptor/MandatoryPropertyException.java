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
package org.jspresso.framework.model.descriptor;

import java.util.Locale;

import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.descriptor.IDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * An exception occurring whenever a mandatory property constraint is violated.
 *
 * @author Vincent Vandenschrick
 */
public class MandatoryPropertyException extends IntegrityException {

  private static final long serialVersionUID = 2718861269346880833L;
  private final IDescriptor       descriptor;
  private final Object            targetComponent;

  /**
   * Constructs a new {@code MandatoryPropertyException} instance.
   *
   * @param descriptor
   *          the violated property descriptor.
   * @param targetComponent
   *          the target component.
   */
  public MandatoryPropertyException(IPropertyDescriptor descriptor,
      Object targetComponent) {
    super("Mandatory property [" + descriptor.getName() + "] on component ["
        + targetComponent + "].");
    this.descriptor = descriptor;
    this.targetComponent = targetComponent;
  }

  /**
   * Uses the {@code integrity.property.mandatory} key to translate the
   * message.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getI18nMessage(ITranslationProvider translationProvider,
      Locale locale) {
    return translationProvider.getTranslation("integrity.property.mandatory",
        new Object[] {
            descriptor.getI18nName(translationProvider, locale),
            targetComponent
        }, locale);
  }
}
