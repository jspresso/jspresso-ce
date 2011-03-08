/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.util.descriptor.IDescriptor;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Abstract base class for controllers. Controllers role is to adapt the
 * application to its environment. Jspresso relies on two different types of
 * controllers :
 * <ul>
 * <li>The frontend controller is responsible for managing UI interactions.
 * Naturally, the type of frontend controller used depends on the UI channel.</li>
 * <li>The backend controller is responsible for managing the application
 * session as well as transactions and persistence operations.</li>
 * </ul>
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
    if (isAccessGranted(securable)) {
      return;
    }
    if (securable instanceof IDescriptor) {
      throw new SecurityException(getTranslationProvider().getTranslation(
          "access.denied.object",
          new Object[] {
            ((IDescriptor) securable).getI18nName(getTranslationProvider(),
                getLocale())
          }, getLocale()));
    }
    throw new SecurityException(getTranslationProvider().getTranslation(
        "access.denied", getLocale()));
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> createEmptyContext() {
    return new HashMap<String, Object>();
  }

  /**
   * Gets the subject out of the application session.
   * <p>
   * {@inheritDoc}
   */
  public Subject getSubject() {
    return getApplicationSession().getSubject();
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
   * {@inheritDoc}
   */
  public boolean isAccessGranted(ISecurable securable) {
    return SecurityHelper.isSubjectGranted(
        getApplicationSession().getSubject(), securable);
  }

  /**
   * Configures a custom exception handler on the controller. The controller
   * itself is an exception handler and is used as such across most of the
   * application layers. Jspresso philosophy is to use unchecked exceptions in
   * services, business rules, and so on, so that, whenever an exception occurs,
   * it climbs the call stack up to an exception handler (usually one of the
   * controller). Whenever a custom exception handler is configured, the
   * exception handling is delegated to it, allowing the exceptions to be
   * refined or handled differently than for the built-in case. The exception
   * handler can either :
   * <ul>
   * <li>return <code>true</code> if the exception was completely processed and
   * does not need any further action.</li>
   * <li>return <code>false</code> if the exception was either not or
   * un-completely processed and needs to continue the built-in handling.</li>
   * </ul>
   * 
   * @param customExceptionHandler
   *          the customExceptionHandler to set.
   */
  public void setCustomExceptionHandler(IExceptionHandler customExceptionHandler) {
    this.customExceptionHandler = customExceptionHandler;
  }

  /**
   * Configures the translation provider used to compute internationalized
   * messages and labels.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }
}
