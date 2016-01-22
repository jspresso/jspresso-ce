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
package org.jspresso.framework.util.resources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * a byte array resource.
 *
 * @author Vincent Vandenschrick
 */
public class MemoryResource extends AbstractResource {

  private final String name;
  private final byte[] resourceBytes;

  /**
   * Constructs a new {@code MemoryResource} instance.
   *
   * @param name
   *          the name of the resource.
   * @param mimeType
   *          the resource mime type.
   * @param resourceBytes
   *          the resource content.
   */
  public MemoryResource(String name, String mimeType, byte[] resourceBytes) {
    super(mimeType);
    this.name = name;
    this.resourceBytes = resourceBytes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream getContent() {
    return new ByteArrayInputStream(resourceBytes);
  }

  /**
   * Gets the name.
   *
   * @return the name.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getSize() {
    return resourceBytes.length;
  }

}
