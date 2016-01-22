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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * A filtered input stream that replaces placeholders on the fly based on a
 * substitution map.
 *
 * @author Vincent Vandenschrick
 */
public class PlaceholderInputStream extends FilterInputStream {

  private final Map<String, String> variables;
  private String              buffer;
  private int                 buffOffset;

  /**
   * Creates an PlaceholderInputStream..
   *
   * @param sourceStream
   *          the InputStream to be filtered.
   * @param variables
   *          the map of key/value pairs.
   */
  public PlaceholderInputStream(InputStream sourceStream,
      Map<String, String> variables) {
    super(sourceStream);
    this.variables = variables;
    this.buffOffset = -1;
  }

  /**
   * Reads the next byte from the stream per the general contract of
   * InputStream.read(). Returns -1 on error or end of stream.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    if (buffer != null) {
      if (buffOffset >= 0 && buffOffset < buffer.length()) {
        return buffer.charAt(buffOffset++);
      }
      buffer = null;
      buffOffset = -1;
    }
    int first = super.read();
    if ('$' == first) {
      int second = super.read();
      if ('{' == second) {
        StringBuilder temp = new StringBuilder();
        int next = super.read();
        while (next > 0 && next != '}') {
          temp.append((char) next);
          next = super.read();
        }
        if (next < 0) {
          buffer = "{" + temp.toString();
          buffOffset = 0;
          return '$';
        }
        if (temp.length() > 0) {
          buffer = temp.toString();
          if (variables.containsKey(buffer)) {
            buffer = variables.get(buffer);
          } else {
            buffer = "${" + buffer + "}";
          }
          if (buffer != null && buffer.length() > 0) {
            buffOffset = 1;
            return buffer.charAt(0);
          }
          return read();
        }
        return read();
      }
      buffer = new String(new char[] {
        (char) second
      });
      buffOffset = 0;
      return '$';
    }
    return first;
  }

  /**
   * Returns false.
   * @return false;
   */
  @Override
  public boolean markSupported() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    // return super.read(b, off, len);
    if (b == null) {
      throw new NullPointerException();
    }
    if ((off < 0) || (off > b.length) || (len < 0)
        || ((off + len) > b.length) || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    }
    if (len == 0) {
      return 0;
    }

    int c = read();
    if (c == -1) {
      return -1;
    }
    b[off] = (byte) c;

    int i = 1;
    try {
      for (; i < len; i++) {
        c = read();
        if (c == -1) {
          break;
        }
        b[off + i] = (byte) c;
      }
    } catch (IOException ee) {
      // Never
    }
    return i;
  }

  // public static void main(String[] args) throws IOException {
  // String test =
  // "Ceci ${missing} est un test ${good} [${null},${empty}]. ${missingclosing";
  // Map<String, String> variables = new HashMap<String, String>();
  // variables.put("good", "Vincent");
  // variables.put("null", null);
  // variables.put("empty", "");
  // InputStream is = new ByteArrayInputStream(test.getBytes());
  // is = new PlaceholderInputStream(is, variables);
  // int next = is.read();
  // while (next > 0) {
  // System.out.print((char) next);
  // next = is.read();
  // }
  // }
}
