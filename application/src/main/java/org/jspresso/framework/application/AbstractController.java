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
package org.jspresso.framework.application;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.gui.EClientType;
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
 * @author Vincent Vandenschrick
 */
public abstract class AbstractController extends AbstractPropertyChangeCapable implements IController {

  private final List<Map.Entry<IAction, Map<String, Object>>> delayedActions;
  private       IExceptionHandler                             customExceptionHandler;

  private final static Logger LOG = LoggerFactory.getLogger(AbstractController.class);
  private              String loginContextName;

  private boolean liveDebugUI;


  /**
   * Instantiates a new Abstract controller.
   */
  protected AbstractController() {
    delayedActions = Collections.synchronizedList(new ArrayList<Map.Entry<IAction, Map<String, Object>>>());
  }

  /**
   * Gets the subject out of the application session.
   * <p>
   * {@inheritDoc}
   *
   * @return the subject
   */
  @Override
  public Subject getSubject() {
    return getApplicationSession().getSubject();
  }

  /**
   * Gets the translationProvider.
   *
   * @return the translationProvider.
   *
   * @deprecated the controller is itself a translation provider. will return this.
   */
  @Override
  @Deprecated
  public ITranslationProvider getTranslationProvider() {
    return this;
  }

  /**
   * Whenever the custom exception handler is set, delegates the exception to it
   * and returns its result. Otherwise, the method returns false, indicating
   * that the exception should be forwarded.
   * <p>
   * {@inheritDoc}
   *
   * @param ex
   *     the ex
   * @param context
   *     the context
   * @return the boolean
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    LOG.debug("An exception was received on controller with context {} ", context, ex);
    if (customExceptionHandler != null) {
      return customExceptionHandler.handleException(ex, context);
    }
    return false;
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
   * <li>return {@code true} if the exception was completely processed and
   * does not need any further action.</li>
   * <li>return {@code false} if the exception was either not or
   * un-completely processed and needs to continue the built-in handling.</li>
   * </ul>
   *
   * @param customExceptionHandler
   *     the customExceptionHandler to set.
   */
  public void setCustomExceptionHandler(IExceptionHandler customExceptionHandler) {
    this.customExceptionHandler = customExceptionHandler;
  }

  /**
   * {@inheritDoc}
   *
   * @param locale
   *     the locale
   * @return the date pattern
   */
  @Override
  public String getDatePattern(Locale locale) {
    return getTranslation(DATE_FORMAT_KEY, locale);
  }

  /**
   * {@inheritDoc}
   *
   * @param locale
   *     the locale
   * @return the first day of week
   */
  @Override
  public int getFirstDayOfWeek(Locale locale) {
    return Integer.parseInt(getTranslation(FIRST_DAY_OF_WEEK_KEY, Integer.toString(1), locale));
  }

  /**
   * {@inheritDoc}
   *
   * @param locale
   *     the locale
   * @return the time pattern
   */
  @Override
  public String getTimePattern(Locale locale) {
    return getTranslation(TIME_FORMAT_KEY, locale);
  }

  /**
   * {@inheritDoc}
   *
   * @param locale
   *     the locale
   * @return the long time pattern
   */
  @Override
  public String getLongTimePattern(Locale locale) {
    return getTranslation(TIME_FORMAT_LONG_KEY, locale);
  }

  /**
   * {@inheritDoc}
   *
   * @param locale
   *     the locale
   * @return the short time pattern
   */
  @Override
  public String getShortTimePattern(Locale locale) {
    return getTranslation(TIME_FORMAT_SHORT_KEY, locale);
  }

  /**
   * {@inheritDoc}
   *
   * @param locale
   *     the locale
   * @return the decimal separator
   */
  @Override
  public String getDecimalSeparator(Locale locale) {
    return getTranslation(DECIMAL_SEPARATOR_KEY, locale);
  }

  /**
   * {@inheritDoc}
   *
   * @param locale
   *     the locale
   * @return the thousands separator
   */
  @Override
  public String getThousandsSeparator(Locale locale) {
    return getTranslation(THOUSANDS_SEPARATOR_KEY, locale);
  }

  /**
   * Utility method used to extract (recursively through the action chain) the
   * internal state of an action. This state should be used (using equals) in
   * order guarantee that the internal state of an action has not changed during
   * it's execution since actions are meant to be singletons this would violate
   * thread-safety.
   *
   * @param action
   *     the action to extract the internal state for.
   * @return the internal action chain state.
   *
   * @throws IllegalAccessException
   *     whenever an exception occurs accessing the internal private state     of the
   *     action through reflection.
   */
  public static Map<String, Object> extractInternalActionState(IAction action) throws IllegalAccessException {
    HashMap<String, Object> state = new HashMap<>();
    appendToInternalActionState(action.getClass(), action, state);
    return state;
  }

  private static void appendToInternalActionState(Class<?> clazz, IAction action, Map<String, Object> state)
      throws IllegalAccessException {
    for (Field field : clazz.getDeclaredFields()) {
      if (!field.isSynthetic()) {
        field.setAccessible(true);
        Object fieldValue = field.get(action);
        if (fieldValue instanceof IAction) {
          fieldValue = extractInternalActionState((IAction) fieldValue);
        }
        state.put(clazz.getName() + "." + field.getName(), fieldValue);
      }
    }
    Class<?> superClazz = clazz.getSuperclass();
    if (superClazz != null && !Object.class.equals(superClazz)) {
      appendToInternalActionState(superClazz, action, state);
    }
  }

  /**
   * Execute later.
   *
   * @param action
   *     the action
   * @param context
   *     the context
   */
  @Override
  public synchronized void executeLater(IAction action, Map<String, Object> context) {
    delayedActions.add(new AbstractMap.SimpleImmutableEntry<>(action, context));
  }

  /**
   * Execute delayed actions.
   *
   * @param actionHandler
   *     the action handler to execute the action. Might be different from this.
   */
  public void executeDelayedActions(IActionHandler actionHandler) {
    List<Map.Entry<IAction, Map<String, Object>>> iteratorCopy;
    synchronized (delayedActions) {
      iteratorCopy = new ArrayList<>(delayedActions);
      delayedActions.clear();
    }
    for (Map.Entry<IAction, Map<String, Object>> delayedActionEntry : iteratorCopy) {
      actionHandler.execute(delayedActionEntry.getKey(), delayedActionEntry.getValue());
    }
  }

  /**
   * Gets client type.
   *
   * @return the client type
   */
  @Override
  public EClientType getClientType() {
    return getApplicationSession().getClientType();
  }

  /**
   * To string.
   *
   * @return original toString completed with user name.
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("user", getApplicationSession().getUsername()).toString();
  }

  /**
   * Configures the name of the JAAS login context to use to authenticate users.
   * It must reference a valid JAAS context that is installed in the JVM, either
   * through setting the {@code java.security.auth.login.config} system
   * property or through server-specific configuration.
   *
   * @param loginContextName
   *     the loginContextName to set.
   */
  @Override
  public void setLoginContextName(String loginContextName) {
    this.loginContextName = loginContextName;
  }

  /**
   * Gets the loginContextName.
   *
   * @return the loginContextName.
   */
  protected String getLoginContextName() {
    return loginContextName;
  }

  /**
   * Perform jaas login subject.
   *
   * @param lch
   *     the lch
   * @return the subject
   */
  protected Subject performJAASLogin(CallbackHandler lch) {
    try {
      LoginContext lc;
      try {
        lc = new LoginContext(getLoginContextName(), lch);
      } catch (LoginException le) {
        LOG.error("Cannot create LoginContext.", le);
        return null;
      } catch (SecurityException se) {
        LOG.error("Cannot create LoginContext.", se);
        return null;
      }
      lc.login();
      return lc.getSubject();
    } catch (LoginException le) {
      // le.getCause() is always null, so cannot rely on it.
      // see bug #1019
      if (!(le instanceof FailedLoginException)) {
        String message = le.getMessage();
        if (message.indexOf(':') > 0) {
          String exceptionClassName = message.substring(0, message.indexOf(':'));
          try {
            if (Throwable.class.isAssignableFrom(Class.forName(exceptionClassName))) {
              LOG.error("A technical exception occurred on login module.", le);
            }
          } catch (ClassNotFoundException ignored) {
            // ignored.
          }
        }
      }
      return null;
    }
  }

  /**
   * Is live debug ui boolean.
   *
   * @return the boolean
   */
  @Override
  public boolean isLiveDebugUI() {
    return liveDebugUI;
  }

  /**
   * Sets live debug ui.
   *
   * @param liveDebugUI
   *     the live debug ui
   */
  @Override
  public void setLiveDebugUI(boolean liveDebugUI) {
    this.liveDebugUI = liveDebugUI;
  }
}
