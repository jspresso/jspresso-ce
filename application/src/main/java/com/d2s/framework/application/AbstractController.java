/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.security.ISecurable;
import com.d2s.framework.security.SecurityHelper;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Base class for controllers. It holds a reference to the root connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractController implements IController {

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
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }
}
