/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor.basic;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;

/**
 * Default implementation of a binary descriptor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicBinaryPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IBinaryPropertyDescriptor {

  private Map<String, List<String>> fileFilter;
  private Integer                   maxLength;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicBinaryPropertyDescriptor clone() {
    BasicBinaryPropertyDescriptor clonedDescriptor = (BasicBinaryPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, List<String>> getFileFilter() {
    return fileFilter;
  }

  /**
   * {@inheritDoc}
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return byte[].class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return false;
  }

  /**
   * Sets the fileFilter.
   * 
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Sets the maxLength property.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
}
