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
package org.jspresso.framework.util.uid;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

/**
 * Wrapper class around a byte array.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ByteArray implements Serializable, Comparable<ByteArray> {

  private static final long serialVersionUID = 4906103696506778514L;

  private final byte[]      bytes;
  private int               cachedHashcode;

  /**
   * Constructs a new <code>Bytes</code> instance.
   * 
   * @param bytes
   *          the bytes content.
   */
  public ByteArray(byte[] bytes) {
    this.bytes = bytes;
    cachedHashcode = Hex.encodeHexString(bytes).hashCode();
  }

  /**
   * Arrays compare the 2 underlying byte arrays.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof ByteArray)) {
      return false;
    }
    return Arrays.equals(((ByteArray) other).bytes, bytes);
  }

  /**
   * Returns the hashcode based on the hexa representation of the underlying
   * byte array.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return cachedHashcode;
  }

  /**
   * Returns a cloned array.
   * 
   * @return a cloned byte array.
   */
  public byte[] getBytes() {
    byte[] clone = new byte[bytes.length];
    System.arraycopy(bytes, 0, clone, 0, bytes.length);
    return clone;
  }

  /**
   * Returns the hexa representation of the byte array.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return Hex.encodeHexString(bytes);
  }

  /**
   * Binary compare of byte arrays.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int compareTo(ByteArray o) {
    if (o == null) {
      return 1;
    }
    for (int i = bytes.length - 1; i > 0; i--) {
      if (bytes[i] < o.bytes[i]) {
        return -1;
      } else if (bytes[i] > o.bytes[i]) {
        return 1;
      }
    }
    return 0;
  }
}
