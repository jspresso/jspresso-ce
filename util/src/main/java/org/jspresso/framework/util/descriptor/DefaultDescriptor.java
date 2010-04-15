/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.descriptor;

import java.util.Locale;

import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This is a utility class from which most named descriptors inherit for
 * factorization purpose. It provides translatable name and description.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultDescriptor implements IDescriptor, Cloneable {

  private String description;
  private String i18nNameKey;
  private String name;

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultDescriptor clone() {
    try {
      return (DefaultDescriptor) super.clone();
    } catch (CloneNotSupportedException ex) {
      // Cannot happen.
      return null;
    }
  }

  /**
   * The description getter.
   * 
   * @return the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return translationProvider.getTranslation(getDescription(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    if (i18nNameKey != null) {
      return translationProvider.getTranslation(i18nNameKey, locale);
    }
    return translationProvider.getTranslation(getName(), locale);
  }

  /**
   * The name getter.
   * 
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the description of this descriptor. Most of the descriptor
   * descriptions are used in conjunction with the Jspresso i18n layer so that
   * the description property set here is actually an i18n key used for
   * translation. Description is mainly used for UI (in tooltips for instance)
   * but may also be used for project technical documentation, contextual help,
   * and so on.
   * 
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the i18nNameKey.
   * 
   * @param nameKey
   *          the i18nNameKey to set.
   * @internal
   */
  public void setI18nNameKey(String nameKey) {
    i18nNameKey = nameKey;
  }

  /**
   * Sets the name of this descriptor. Most of the descriptor names are used in
   * conjunction with the Jspresso i18n layer so that the name property set here
   * is actually an i18n key used for translation. The descriptor name property
   * semantic may vary depending on the actual descriptor type. For instance, a
   * property descriptor name is the name of the property and a component
   * descriptor name is the fully qualified name of the underlying class.
   * 
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the i18nNameKey.
   * 
   * @return the i18nNameKey.
   */
  protected String getI18nNameKey() {
    return i18nNameKey;
  }
}
