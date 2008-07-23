/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractBackendAction;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPasswordPropertyDescriptor;
import org.jspresso.framework.security.UserPrincipal;


/**
 * Changes a user password asking for the current and new password.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractChangePasswordAction extends
    AbstractBackendAction {

  /**
   * <code>PASSWD_CHANGE_DESCRIPTOR</code> is a unique reference to the model
   * descriptor of the change password action.
   */
  public static final IComponentDescriptor<Map<String, String>> PASSWD_CHANGE_DESCRIPTOR = createPasswordChangeModel();
  /**
   * <code>PASSWD_CURRENT</code>.
   */
  public static final String                                    PASSWD_CURRENT           = "password_current";
  /**
   * <code>PASSWD_RETYPED</code>.
   */
  public static final String                                    PASSWD_RETYPED           = "password_retyped";

  /**
   * <code>PASSWD_TYPED</code>.
   */
  public static final String                                    PASSWD_TYPED             = "password_typed";

  private static IComponentDescriptor<Map<String, String>> createPasswordChangeModel() {
    BasicComponentDescriptor<Map<String, String>> passwordChangeModel = new BasicComponentDescriptor<Map<String, String>>();
    BasicPasswordPropertyDescriptor currentPassword = new BasicPasswordPropertyDescriptor();
    currentPassword.setName(PASSWD_CURRENT);
    BasicPasswordPropertyDescriptor typedPassword = new BasicPasswordPropertyDescriptor();
    typedPassword.setName(PASSWD_TYPED);
    typedPassword.setMaxLength(new Integer(15));
    BasicPasswordPropertyDescriptor retypedPassword = new BasicPasswordPropertyDescriptor();
    retypedPassword.setName(PASSWD_RETYPED);
    retypedPassword.setMaxLength(new Integer(15));

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(currentPassword);
    propertyDescriptors.add(typedPassword);
    propertyDescriptors.add(retypedPassword);
    passwordChangeModel.setPropertyDescriptors(propertyDescriptors);

    return passwordChangeModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Map<String, Object> actionParam = (Map<String, Object>) context
        .get(ActionContextConstants.ACTION_PARAM);
    char[] typedPasswd = (char[]) actionParam.get(PASSWD_TYPED);
    char[] retypedPasswd = (char[]) actionParam.get(PASSWD_RETYPED);
    if (!Arrays.equals(typedPasswd, retypedPasswd)) {
      throw new ActionBusinessException(
          "Typed and retyped passwords are different.",
          "password.typed.retyped.different");
    }
    UserPrincipal principal = getApplicationSession(context).getPrincipal();
    if (changePassword(principal, (char[]) actionParam.get(PASSWD_CURRENT),
        typedPasswd)) {
      context.put(ActionContextConstants.ACTION_PARAM, getTranslationProvider(
          context)
          .getTranslation("password.change.success", getLocale(context)));
      return super.execute(actionHandler, context);
    }
    return false;
  }

  /**
   * Performs the effective password change depending on the underlying storage.
   * 
   * @param userPrincipal
   *            the connected user principal.
   * @param currentPassword
   *            the current password.
   * @param newPassword
   *            the new password.
   * @return true if password was changed succesfully.
   */
  protected abstract boolean changePassword(UserPrincipal userPrincipal,
      char[] currentPassword, char[] newPassword);
}
