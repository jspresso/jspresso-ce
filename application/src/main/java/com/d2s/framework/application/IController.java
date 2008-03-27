/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application;

import java.util.Locale;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * This interface is implemented by the controllers of the application.
 * Controllers implement the interface since their main role is to execute
 * application actions.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IController extends IActionHandler {

  /**
   * Gets the applicationSession for this backend controller.
   * 
   * @return the current controller application session.
   */
  IApplicationSession getApplicationSession();

  /**
   * Gets the current controller locale.
   * 
   * @return the current controller locale.
   */
  Locale getLocale();

  /**
   * Gets the translation provider used by this controller.
   * 
   * @return the translation provider used by this controller.
   */
  ITranslationProvider getTranslationProvider();

  /**
   * Stops the controller. This method performs any necessary cleanup.
   * 
   * @return true if the stop was successful.
   */
  boolean stop();
}
