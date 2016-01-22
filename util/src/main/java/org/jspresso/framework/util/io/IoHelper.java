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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is a simple helper class for IO operations.
 *
 * @author Vincent Vandenschrick
 */
public final class IoHelper {

  private IoHelper() {
    // private constructor for helper class.
  }

  /**
   * Copies the content of an input stream to an output stream.
   *
   * @param inputStream
   *          the input stream to read the bytes from.
   * @param outputStream
   *          the output stream to read the bytes from.
   * @throws IOException
   *           whenever an exception occurs during I/O operation.
   */
  public static void copyStream(InputStream inputStream,
      OutputStream outputStream) throws IOException {
    byte[] b = new byte[1024];
    int n;
    while ((n = inputStream.read(b)) != -1) {
      outputStream.write(b, 0, n);
    }
  }
}
