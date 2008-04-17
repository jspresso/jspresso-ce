/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application;

import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.i18n.ITranslationProvider;


/**
 * Base class for controllers. It holds a reference to the root connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractController implements IController {

  private IExceptionHandler    customExceptionHandler;
  private ITranslationProvider translationProvider;

  /**
   * {@inheritDoc}
   */
  public void checkAccess(ISecurable securable) {
    SecurityHelper.checkAccess(getApplicationSession().getSubject(), securable,
        getTranslationProvider(), getLocale());
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> createEmptyContext() {
    return new HashMap<String, Object>();
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  public ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  /**
   * Whenever the custom exception handler is set, delegates the exception to it
   * and returns its result. Otherwise, the method returns false, indicating
   * that the exception should be forwarded.
   * <p>
   * {@inheritDoc}
   */
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (customExceptionHandler != null) {
      return customExceptionHandler.handleException(ex, context);
    }
    return false;
  }

  /**
   * Sets the customExceptionHandler.
   * 
   * @param customExceptionHandler
   *            the customExceptionHandler to set.
   */
  public void setCustomExceptionHandler(IExceptionHandler customExceptionHandler) {
    this.customExceptionHandler = customExceptionHandler;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *            the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

}
