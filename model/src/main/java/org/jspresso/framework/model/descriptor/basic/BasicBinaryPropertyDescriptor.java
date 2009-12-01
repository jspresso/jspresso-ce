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
 * Describes a property used to store a binary value in the form of a byte
 * array.
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
   * This property allows to configure the file filter that has to be displayed
   * whenever a file system operation is initiated from the UI to operate on
   * this property. This includes :
   * <ul>
   * <li>setting the property binary value from a file loaded from the file
   * system</li>
   * <li>saving the property binary value to a file on the file system</li>
   * </ul>
   * Jspresso provides built-in actions that do the above and configure their UI
   * automatically based on the <code>fileFilter</code> property.
   * <p>
   * The incoming <code>Map</code> mus be structured like thhe following :
   * <ul>
   * <li>keys are translation keys that will be translated by Jspresso i18n
   * layer and presented to the user as the group name of the associated
   * extensions, e.g. <i>&quot;JPEG images&quot;</i></li>
   * <li>values are the extension list associated to a certain group name, e.g.
   * a list containing <i>[&quot;.jpeg&quot;,&quot;.jpg&quot;]</i></li>
   * </ul>
   * 
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Sets the max size (in bytes) of the property value.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
}
