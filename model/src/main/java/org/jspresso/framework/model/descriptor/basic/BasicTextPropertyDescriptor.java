/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;

/**
 * Describes a multi-line text property. This type of descriptor instructs
 * Jspresso to use a multi-line text component to interact with this type of
 * property.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTextPropertyDescriptor extends BasicStringPropertyDescriptor
    implements ITextPropertyDescriptor {

  private Map<String, List<String>> fileFilter;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicTextPropertyDescriptor clone() {
    BasicTextPropertyDescriptor clonedDescriptor = (BasicTextPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * Overriden since textarea are useless in query screens.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public BasicStringPropertyDescriptor createQueryDescriptor() {
    BasicStringPropertyDescriptor defaultQueryDecriptor = super
        .createQueryDescriptor();

    BasicStringPropertyDescriptor queryDescriptor = new BasicStringPropertyDescriptor();
    queryDescriptor.setName(defaultQueryDecriptor.getName());
    queryDescriptor.setI18nNameKey(defaultQueryDecriptor.getI18nNameKey());
    queryDescriptor.setDescription(defaultQueryDecriptor.getDescription());
    queryDescriptor.setGrantedRoles(defaultQueryDecriptor.getGrantedRoles());
    queryDescriptor.setMandatory(defaultQueryDecriptor.isMandatory());
    queryDescriptor.setMaxLength(defaultQueryDecriptor.getMaxLength());
    queryDescriptor.setReadabilityGates(defaultQueryDecriptor
        .getReadabilityGates());
    queryDescriptor.setReadOnly(defaultQueryDecriptor.isReadOnly());
    queryDescriptor.setRegexpPattern(defaultQueryDecriptor.getRegexpPattern());
    queryDescriptor.setRegexpPatternSample(defaultQueryDecriptor
        .getRegexpPatternSample());
    queryDescriptor.setSqlName(defaultQueryDecriptor.getSqlName());
    queryDescriptor.setUpperCase(defaultQueryDecriptor.isUpperCase());
    queryDescriptor.setUpperCase(defaultQueryDecriptor.isUpperCase());
    queryDescriptor.setWritabilityGates(defaultQueryDecriptor
        .getWritabilityGates());

    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, List<String>> getFileFilter() {
    return fileFilter;
  }

  /**
   * This property allows to configure the file filter that has to be displayed
   * whenever a file system operation is initiated from the UI to operate on
   * this property. This includes :
   * <ul>
   * <li>setting the property value from a text file loaded from the file system
   * </li>
   * <li>saving the property text value to a file on the file system</li>
   * </ul>
   * Jspresso provides built-in actions that do the above and configure their UI
   * automatically based on the <code>fileFilter</code> property.
   * <p>
   * The incoming <code>Map</code> must be structured like following :
   * <ul>
   * <li>keys are translation keys that will be translated by Jspresso i18n
   * layer and presented to the user as the group name of the associated
   * extensions, e.g. <i>&quot;HTML files&quot;</i></li>
   * <li>values are the extension list associated to a certain group name, e.g.
   * a list containing <i>[&quot;.html&quot;,&quot;.htm&quot;]</i></li>
   * </ul>
   * 
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }
}
