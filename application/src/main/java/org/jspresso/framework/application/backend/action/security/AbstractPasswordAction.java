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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import org.jspresso.framework.application.backend.action.BackendAction;

/**
 * Abstract pasword class with base methods for encofding and hashing passwords.
 *
 * @author Vincent Vandenschrick
 */
public class AbstractPasswordAction extends BackendAction {
  /**
   * {@code BASE64_ENCODING} is &quot;BASE64&quot;.
   */
  public static final String                                    BASE64_ENCODING          = "BASE64";
  /**
   * {@code BASE16_ENCODING} is &quot;HEX&quot;.
   */
  public static final String                                    BASE16_ENCODING          = "BASE16";
  /**
   * {@code HEX_ENCODING} is &quot;HEX&quot;.
   */
  public static final String                                    HEX_ENCODING             = "HEX";

  private String digestAlgorithm;
  private String hashEncoding;

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
   * <li>{@code BASE64} for base 64 encoding.</li>
   * <li>{@code HEX} for base 16 encoding.</li>
   * </ul>
   * Default encoding is {@code BASE64}.
   *
   * @param hashEncoding
   *          the hashEncoding to set.
   */
  public void setHashEncoding(String hashEncoding) {
    this.hashEncoding = hashEncoding;
  }

  /**
   * Hashes a char array using the algorithm parametrised in the instance.
   *
   * @param newPassword
   *          the new password to hash.
   * @return the password digest.
   * @throws NoSuchAlgorithmException
   *           when the digest algorithm is not supported.
   * @throws IOException
   *           whenever an I/O exception occurs.
   */
  protected String digestAndEncode(char... newPassword)
      throws NoSuchAlgorithmException, IOException {
    if (getDigestAlgorithm() != null) {
      MessageDigest md = MessageDigest.getInstance(getDigestAlgorithm());
      md.reset();
      md.update(new String(newPassword).getBytes(StandardCharsets.UTF_8.name()));

      byte[] digest = md.digest();
      return getPasswordStorePrefix() + encode(digest);
    }
    return new String(newPassword);
  }

  /**
   * Encodes the password hash based on the hash encoding parameter (either
   * Base64, Base16). Defaults to Base64.
   *
   * @param source
   *          the byte array (hash) to encode.
   * @return the encoded string.
   */
  protected String encode(byte[] source) {
    String he = getHashEncoding();
    if (BASE64_ENCODING.equalsIgnoreCase(he)) {
      return Base64.encodeBase64String(source);
    }
    if (BASE16_ENCODING.equalsIgnoreCase(he)
        || HEX_ENCODING.equalsIgnoreCase(he)) {
      return Hex.encodeHexString(source);
    }
    // defaults to Base64
    return Base64.encodeBase64String(source);
  }

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
   * Returns a prefix to use before storing a password. An example usage is to
   * prefix the password hash with the type of hash, e.g. {MD5}.
   *
   * @return a prefix to use before storing a password.
   */
  protected String getPasswordStorePrefix() {
    return "";
  }
}
