/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.resources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * a byte array resource.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MemoryResource extends AbstractResource {

  private byte[] resourceBytes;

  /**
   * Constructs a new <code>MemoryResource</code> instance.
   * 
   * @param mimeType
   *            the resource mime type.
   * @param resourceBytes
   *            the resource content.
   */
  public MemoryResource(String mimeType, byte[] resourceBytes) {
    super(mimeType);
    this.resourceBytes = resourceBytes;
  }

  /**
   * {@inheritDoc}
   */
  public InputStream getContent() {
    return new ByteArrayInputStream(resourceBytes);
  }

  /**
   * {@inheritDoc}
   */
  public int getLength() {
    return resourceBytes.length;
  }

}
