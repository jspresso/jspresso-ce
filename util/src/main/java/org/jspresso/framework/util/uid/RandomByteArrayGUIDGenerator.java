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
package org.jspresso.framework.util.uid;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * An implementation of IGUIDGenerator based on Marc A. Mnich RandomGUID
 * implementation that returns a byte array.
 *
 * @author Vincent Vandenschrick
 */
public class RandomByteArrayGUIDGenerator implements IGUIDGenerator<ByteArray> {

  /**
   * Generates a GUID based on Marc A. Mnich RandomGUID implementation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ByteArray generateGUID() {
    String hex = new RandomGUID(false, null).toString();
    try {
      return new ByteArray(Hex.decodeHex(hex.toCharArray()));
    } catch (DecoderException ex) {
      throw new NestedRuntimeException(ex, "Unable to decode GUID from hex.");
    }
  }

}
