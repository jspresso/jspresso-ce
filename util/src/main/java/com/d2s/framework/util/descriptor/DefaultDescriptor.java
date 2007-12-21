/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.descriptor;

import java.util.Locale;

import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * This class is a default convennience implementation of
 * <code>IDescriptor</code>.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultDescriptor implements IDescriptor {

  private String description;
  private String name;

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
   * The description setter.
   * 
   * @param description
   *            the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * The name setter.
   * 
   * @param name
   *            the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }
}
