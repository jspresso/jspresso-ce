/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.security.Util;
import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPasswordPropertyDescriptor;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * This is the base class for implementing an action that performs actual
 * modification of a logged-in user password. This implementation delegates to
 * subclasses the actual change in the concrete JAAS store. This backend action
 * expects a Map&lt;String,Object&gt; in as action parameter in the context.
 * This map must contain :
 * <p>
 * <ul>
 * <li><code>password_current</code> entry containing current password. Entry
 * key can be referred to as PASSWD_CURRENT static constant.</li>
 * <li><code>password_typed</code> entry containing the new password. Entry key
 * can be referred to as PASSWD_TYPED static constant.</li>
 * <li><code>password_retyped</code> entry containing the new password retyped.
 * Entry key can be referred to as PASSWD_RETYPED static constant.</li>
 * </ul>
 * For the action to succeed, <code>current_password</code> must match the
 * logged-in user current password and <code>password_typed</code> and
 * <code>password_retyped</code> mut match between eachother. The only method to
 * be implemented by concrete subcasses is :
 * <p>
 * 
 * <pre>
 * protected abstract boolean changePassword(UserPrincipal userPrincipal,
 *           String currentPassword, String newPassword)
 * </pre>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractChangePasswordAction extends BackendAction {

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

  private String                                                digestAlgorithm;
  private String                                                hashEncoding;

  /**
   * Sets the digestAlgorithm to use to hash the password before storing it (MD5
   * for instance).
   * 
   * @param digestAlgorithm
   *          the digestAlgorithm to set.
   */
  public void setDigestAlgorithm(String digestAlgorithm) {
    this.digestAlgorithm = digestAlgorithm;
  }

  /**
   * Sets the hashEncoding to encode the password hash before storing it. You
   * may choose between :
   * <ul>
   * <li><code>BASE64</code> for base 64 encoding.</li>
   * <li><code>HEX</code> for base 16 encoding.</li>
   * <li><code>RFC2617</code> for RFC 2617 encoding.</li>
   * </ul>
   * Default encoding is <code>BASE64</code>.
   * 
   * @param hashEncoding
   *          the hashEncoding to set.
   */
  public void setHashEncoding(String hashEncoding) {
    this.hashEncoding = hashEncoding;
  }

  private static IComponentDescriptor<Map<String, String>> createPasswordChangeModel() {
    BasicComponentDescriptor<Map<String, String>> passwordChangeModel = new BasicComponentDescriptor<Map<String, String>>();
    BasicPasswordPropertyDescriptor currentPassword = new BasicPasswordPropertyDescriptor();
    currentPassword.setName(PASSWD_CURRENT);
    currentPassword.setMaxLength(new Integer(32));
    BasicPasswordPropertyDescriptor typedPassword = new BasicPasswordPropertyDescriptor();
    typedPassword.setName(PASSWD_TYPED);
    typedPassword.setMaxLength(new Integer(32));
    BasicPasswordPropertyDescriptor retypedPassword = new BasicPasswordPropertyDescriptor();
    retypedPassword.setName(PASSWD_RETYPED);
    retypedPassword.setMaxLength(new Integer(32));

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
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Map<String, Object> actionParam = (Map<String, Object>) getModelConnector(
        context).getConnectorValue();
    String typedPasswd = (String) actionParam.get(PASSWD_TYPED);
    String retypedPasswd = (String) actionParam.get(PASSWD_RETYPED);
    if (!ObjectUtils.equals(typedPasswd, retypedPasswd)) {
      throw new ActionBusinessException(
          "Typed and retyped passwords are different.",
          "password.typed.retyped.different");
    }
    UserPrincipal principal = getApplicationSession(context).getPrincipal();
    if (changePassword(principal, (String) actionParam.get(PASSWD_CURRENT),
        typedPasswd)) {
      setActionParameter(getTranslationProvider(context).getTranslation(
          "password.change.success", getLocale(context)), context);
      return super.execute(actionHandler, context);
    }
    return false;
  }

  /**
   * Hashes a char array using the algorithm parametered in the instance.
   * 
   * @param newPassword
   *          the new password to hash.
   * @return the password digest.
   * @throws NoSuchAlgorithmException
   *           when the digest algorithm is not supported.
   * @throws IOException
   *           whenever an I/O exception occurs.
   */
  protected String digestAndEncode(char[] newPassword)
      throws NoSuchAlgorithmException, IOException {
    if (getDigestAlgorithm() != null) {
      MessageDigest md = MessageDigest.getInstance(getDigestAlgorithm());
      md.reset();
      md.update(new String(newPassword).getBytes("UTF-8"));

      byte[] digest = md.digest();
      return getPasswordStorePrefix() + encode(digest);
    }
    return new String(newPassword);
  }

  /**
   * Returns a prefix to use before storing a password. An example usage is to
   * prefix the password hash with the type of hash, e.g. {MD5}.
   * 
   * @return a prefix to use before storing a password.
   */
  protected String getPasswordStorePrefix() {
    return "";
  }

  /**
   * Performs the effective password change depending on the underlying storage.
   * 
   * @param userPrincipal
   *          the connected user principal.
   * @param currentPassword
   *          the current password.
   * @param newPassword
   *          the new password.
   * @return true if password was changed succesfully.
   */
  protected abstract boolean changePassword(UserPrincipal userPrincipal,
      String currentPassword, String newPassword);

  /**
   * Gets the digestAlgorithm.
   * 
   * @return the digestAlgorithm.
   */
  protected String getDigestAlgorithm() {
    return digestAlgorithm;
  }

  /**
   * Gets the hashEncoding.
   * 
   * @return the hashEncoding.
   */
  protected String getHashEncoding() {
    return hashEncoding;
  }

  /**
   * Encodes the password hash based on the hash encoding parameter (either
   * Base64, Base16 or RFC2617). Defaults to Base64.
   * 
   * @param source
   *          the byte array (hash) to encode.
   * @return the encoded string.
   */
  protected String encode(byte[] source) {
    String he = getHashEncoding();
    if (Util.BASE64_ENCODING.equalsIgnoreCase(he)) {
      return Util.encodeBase64(source);
    } else if (Util.BASE16_ENCODING.equalsIgnoreCase(he)) {
      return Util.encodeBase16(source);
    } else if (Util.RFC2617_ENCODING.equalsIgnoreCase(he)) {
      return Util.encodeRFC2617(source);
    }
    // defaults to Base64
    return Util.encodeBase64(source);
  }
}
