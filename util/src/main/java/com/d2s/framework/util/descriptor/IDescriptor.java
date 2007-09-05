/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.descriptor;

import java.util.Locale;

import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * This interface is implemented by anything which can be described.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDescriptor {

  /**
   * <code>DESCRIPTION</code>="description".
   */
  String DESCRIPTION = "description";
  /**
   * <code>NAME</code>="name".
   */
  String NAME        = "name";

  /**
   * Gets the end-user understandable description.
   * 
   * @return The user-friendly description
   */
  String getDescription();

  /**
   * Gets the internationalized end-user understandable description.
   * 
   * @param translationProvider
   *          the translation provider which can be used by the descriptor to
   *          compute its internationalized description.
   * @param locale
   *          the locale in which the descriptor must compute its
   *          internationalized description.
   * @return The user-friendly description
   */
  String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale);

  /**
   * Gets the internationalized name of this descriptor.
   * 
   * @param translationProvider
   *          the translation provider which can be used by the descriptor to
   *          compute its internationalized name.
   * @param locale
   *          the locale in which the descriptor must compute its
   *          internationalized name.
   * @return The internationalized name of this descripted object
   */
  String getI18nName(ITranslationProvider translationProvider, Locale locale);

  /**
   * Gets the name of this descriptor. Depending on the implementation, this
   * name can be technically meaningful (e.g. a method name, a property name,
   * ...).
   * 
   * @return The name of this descripted object
   */
  String getName();
}
