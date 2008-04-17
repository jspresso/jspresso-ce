/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is a simple helper class for IO operations.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
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
   *            the input stream to read the bytes from.
   * @param outputStream
   *            the output stream to read the bytes from.
   * @throws IOException
   *             whenever an exception occurs during I/O operation.
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
