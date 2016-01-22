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
package org.jspresso.framework.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to manage object serialization
 * and deserialization.
 *
 * @author Maxime
 */
public abstract class SerializationUtil {

  private static final Logger logger = LoggerFactory.getLogger(SerializationUtil.class);

  /**
   * Serialize one object.
   *
   * @param object to serialize
   * @param zipped or not
   * @return the serialized object
   * @throws IOException the iO exception
   */
  public static byte[] serialize(Object object, boolean zipped) throws IOException {
    ByteArrayOutputStream bo = new ByteArrayOutputStream();

    ObjectOutputStream oo;
    if (zipped) {
      oo = new ObjectOutputStream(new GZIPOutputStream(bo));
    }
    else {
      oo = new ObjectOutputStream(bo);
    }

    oo.writeObject(object);
    oo.close();
    return bo.toByteArray();
  }

  /**
   * Deserialize a table of bytes to an Object instance.
   *
   * @param bytes serialized representation of the object
   * @param zipped or not
   * @return the Object created after
   * @throws IOException the iO exception
   * @throws ClassNotFoundException the class not found exception
   */
  public static Object deserialize(byte[] bytes, boolean zipped) throws IOException,
      ClassNotFoundException {

    ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
    ObjectInputStream oi;
    if (zipped) {
      oi = new ObjectInputStream(new GZIPInputStream(bi));
    }
    else {
      oi = new ObjectInputStream(bi);
    }
    Object o = oi.readObject();
    oi.close();
    return o;
  }

  /**
   * Translate a a String to a base 64 table of bytes.
   *
   * @param data table of bytes
   * @return the base 64 representation
   */
  public static String toBase64String(byte[] data) {
    byte[] decoded = new Base64(-1, new byte[] {}, true).encode(data);
    return new String(decoded);
  }

  /**
   * Translate a base 64 table of bytes to a String.
   *
   * @param base64 representation
   * @return the String after translation
   */
  public static byte[] fromBase64String(String base64) {
    return new Base64(true).decode(base64.getBytes());
  }

  /**
   * Serialize object to Base64 string representation.
   *
   * @param object to serialize
   * @param zipped or not
   * @return base 64 string representation
   * @throws IOException the iO exception
   */
  public static String serializeToBase64(Object object, boolean zipped) throws IOException {
    byte[] data;
    if (object instanceof Serializable) {
      data = SerializationUtil.serialize(object, zipped);
    } else {
      logger.warn("Unable to serialize object '" + object + "' ");
      return null;
    }
    return SerializationUtil.toBase64String(data);
  }

  /**
   * Deserialize Base64 string representation to Object.
   *
   * @param base64 the base 64
   * @param zipped or not
   * @return the deserialized object
   * @throws IOException the iO exception
   * @throws ClassNotFoundException the class not found exception
   */
  public static Object deserializeFromBase64(String base64, boolean zipped) throws IOException, ClassNotFoundException {
    byte[] data = SerializationUtil.fromBase64String(base64);
    return SerializationUtil.deserialize(data, zipped);
  }

}
