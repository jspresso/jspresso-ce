/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * This interface is implemented by web resources.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IResource {

  /**
   * Gets the resource mime type.
   * 
   * @return the resource mime type.
   */
  String getMimeType();

  /**
   * Gets the resource length.
   * 
   * @return the resource length.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  int getLength() throws IOException;

  /**
   * Gets the resource content input stream.
   * 
   * @return the resource content input stream.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  InputStream getContent() throws IOException;
}
