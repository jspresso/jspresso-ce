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
package org.jspresso.framework.application.backend.action.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPasswordPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * This is the base class for implementing an action that performs actual
 * reset of a logged-in user password. This implementation delegates to
 * subclasses the actual change in the concrete JAAS store. This backend action
 * will generate a random String that will replace the user password in the store.
 *
 * The only method to be implemented by concrete subclasses is :
 * <p>
 *
 * <pre>
 * protected abstract boolean resetPassword(String username, String newPassword)
 * </pre>
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractResetPasswordAction extends AbstractPasswordAction {

  public static final String USERNAME_KEY = "username";
  public static final String GENERATED_PASSWORD_KEY = "generatedPassword";
  public static final String ONE_TIME_PREFIX = "{ONE_TIME}";

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    String username = (String) context.get(USERNAME_KEY);
    String generatedPassword = RandomStringUtils.randomAscii(16);
    generatedPassword = ONE_TIME_PREFIX + generatedPassword;
    context.put(GENERATED_PASSWORD_KEY, generatedPassword);
    return changePassword(username, generatedPassword);
  }


  /**
   * Performs the effective password reset depending on the underlying storage.
   *
   * @param username
   *          the username to change.
   * @param generatedPassword
   *          the generated password.
   * @return true if password was changed successfully.
   */
  protected abstract boolean changePassword(String username, String generatedPassword);
}
