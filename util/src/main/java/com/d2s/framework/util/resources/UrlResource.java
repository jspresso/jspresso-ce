package com.d2s.framework.util.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.d2s.framework.util.url.UrlHelper;

/**
 * An URL resource.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UrlResource extends AbstractResource {

  private URL url;

  /**
   * Constructs a new <code>UrlResource</code> instance.
   * 
   * @param mimeType
   *          the resource mime type.
   * @param urlSpec
   *          the url spec.
   */
  public UrlResource(String mimeType, String urlSpec) {
    super(mimeType);
    url = UrlHelper.createURL(urlSpec);
  }

  /**
   * {@inheritDoc}
   */
  public int getLength() {
    return -1; // unknown.
  }

  /**
   * {@inheritDoc}
   */
  public InputStream getContent() throws IOException {
    return url.openStream();
  }
}
