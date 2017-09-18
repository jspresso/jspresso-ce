/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.security;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.security.AbstractResetPasswordAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.util.html.HtmlHelper;

/**
 * This is the frontend action to initiate a user password reset. The username to change the password for is
 * retrieved from the selected model using the usernameProperty parameterized in the action.
 * This action must wrap with a concrete subclass of backend {@code AbstractResetPasswordAction} that performs
 * the actual password reset depending on the authentication backend. Jspresso offers one concrete implementation :
 * <ul>
 * <li>{@code DatabaseChangePasswordAction} for JDBC based authentication
 * backend</li>
 * </ul>
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public class ResetPasswordAction<E, F, G> extends FrontendAction<E, F, G> {

  private String usernameProperty;

  /**
   * {@inheritDoc}
   *
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   * @return the boolean
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Object selectedModel = getSelectedModel(context);
    Object username;
    try {
      username = getBackendController(context).getAccessorFactory().createPropertyAccessor(getUsernameProperty(),
          selectedModel.getClass()).getValue(selectedModel);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
      throw new ActionException(ex, "Could not extract username from model");
    }
    context.put(AbstractResetPasswordAction.USERNAME_KEY, username);
    if (super.execute(actionHandler, context)) {
      getFrontendController(context).popupInfo(getSourceComponent(context),
          actionHandler.getTranslation("password.reset.title", getLocale(context)),
          getIconFactory(context).getInfoIconImageURL(), HtmlHelper.toHtml(
              actionHandler.getTranslation("password.reset.message", getLocale(context)) + HtmlHelper
                  .emphasis((String) context.get(AbstractResetPasswordAction.GENERATED_PASSWORD_KEY))));
      return true;
    }
    return false;
  }

  /**
   * Gets username property.
   *
   * @return the username property
   */
  protected String getUsernameProperty() {
    return usernameProperty;
  }

  /**
   * Sets username property.
   *
   * @param usernameProperty
   *     the username property
   */
  public void setUsernameProperty(String usernameProperty) {
    this.usernameProperty = usernameProperty;
  }
}
