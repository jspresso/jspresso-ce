/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.i18n;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Improves the default Spring RB message source to handle nested property like
 * keys. Before falling back to the default, the
 * EnhancedResourceBundleMessageSource will try to follow the property chain to
 * find a translation, e.g. : to translate address.city.zip, it will try, in
 * order : <li>address.city.zip <li>city.zip <li>zip
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EnhancedResourceBundleMessageSource extends
    ResourceBundleMessageSource {

  private static final char DOT = '.';

  /**
   * Overriden so that we can explore the nested key chain for a translation
   * before actually going to the parent.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected MessageFormat resolveCode(String code, Locale locale) {
    String codePart = code;
    MessageFormat res = super.resolveCode(codePart, locale);
    int dotIndex = codePart.indexOf(DOT);

    while (res == null && dotIndex > 0) {
      codePart = codePart.substring(dotIndex + 1);
      res = super.resolveCode(codePart, locale);
      dotIndex = codePart.indexOf(DOT);
    }
    return res;
  }

  /**
   * Overriden so that we can explore the nested key chain for a translation
   * before actually going to the parent.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String resolveCodeWithoutArguments(String code, Locale locale) {
    String codePart = code;
    String res = super.resolveCodeWithoutArguments(codePart, locale);
    int dotIndex = codePart.indexOf(DOT);

    while (res == null && dotIndex > 0) {
      codePart = codePart.substring(dotIndex + 1);
      res = super.resolveCodeWithoutArguments(codePart, locale);
      dotIndex = codePart.indexOf(DOT);
    }
    return res;
  }
}
