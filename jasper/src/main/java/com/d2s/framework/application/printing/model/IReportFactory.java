/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.printing.model;

import java.util.Locale;

import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * A factory to create report instances out of report descriptors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IReportFactory {

  /**
   * Creates a report instance based on a report descriptor.
   * 
   * @param reportDescriptor
   *            the report descriptor to create the report for.
   * @param translationProvider
   *            the translation provider to use.
   * @param locale
   *            the locale to create the report for.
   * @return the created report instance.
   */
  IReport createReportInstance(IReportDescriptor reportDescriptor,
      ITranslationProvider translationProvider, Locale locale);

}
