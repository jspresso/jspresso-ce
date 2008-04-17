package org.jspresso.framework.util.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A file resource.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FileResource extends AbstractResource {

  private File resourceFile;

  /**
   * Constructs a new <code>FileResource</code> instance.
   * 
   * @param mimeType
   *            the resource mime type.
   * @param resourceFile
   *            the resource content.
   */
  public FileResource(String mimeType, File resourceFile) {
    super(mimeType);
    this.resourceFile = resourceFile;
  }

  /**
   * {@inheritDoc}
   */
  public InputStream getContent() throws IOException {
    return new FileInputStream(resourceFile);
  }

  /**
   * {@inheritDoc}
   */
  public int getLength() {
    return (int) resourceFile.length();
  }
}
