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
package org.jspresso.framework.model.descriptor.basic;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;

/**
 * Describes a multi-line text property. This type of descriptor instructs
 * Jspresso to use a multi-line text component to interact with this type of
 * property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicTextPropertyDescriptor extends BasicStringPropertyDescriptor
    implements ITextPropertyDescriptor {

  private Map<String, List<String>> fileFilter;
  private String                    fileName;
  private String                    contentType;
  private boolean                   queryMultiline = false;

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
   * Overridden since text area are useless in query screens.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public BasicStringPropertyDescriptor createQueryDescriptor() {
    BasicStringPropertyDescriptor defaultQueryDescriptor = super
        .createQueryDescriptor();
    if (isQueryMultiline()) {
      return defaultQueryDescriptor;
    }

    BasicStringPropertyDescriptor queryDescriptor = new BasicStringPropertyDescriptor();
    queryDescriptor.setName(defaultQueryDescriptor.getName());
    queryDescriptor.setI18nNameKey(defaultQueryDescriptor.getI18nNameKey());
    queryDescriptor.setDescription(defaultQueryDescriptor.getDescription());
    queryDescriptor.setGrantedRoles(defaultQueryDescriptor.getGrantedRoles());
    queryDescriptor.setMandatory(defaultQueryDescriptor.isMandatory());
    queryDescriptor.setMaxLength(defaultQueryDescriptor.getMaxLength());
    queryDescriptor.setReadabilityGates(defaultQueryDescriptor
        .getReadabilityGates());
    queryDescriptor.setReadOnly(defaultQueryDescriptor.isReadOnly());
    queryDescriptor.setRegexpPattern(defaultQueryDescriptor.getRegexpPattern());
    queryDescriptor.setRegexpPatternSample(defaultQueryDescriptor
        .getRegexpPatternSample());
    queryDescriptor.setSqlName(defaultQueryDescriptor.getSqlName());
    queryDescriptor.setUpperCase(defaultQueryDescriptor.isUpperCase());
    queryDescriptor.setWritabilityGates(defaultQueryDescriptor
        .getWritabilityGates());

    queryDescriptor.setDelegateClassName(defaultQueryDescriptor
        .getDelegateClassName());
    queryDescriptor.setComputed(defaultQueryDescriptor.isComputed());

    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
   * automatically based on the {@code fileFilter} property.
   * <p>
   * The incoming {@code Map} must be structured like following :
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

  /**
   * Gets the fileName.
   *
   * @return the fileName.
   */
  @Override
  public String getFileName() {
    return fileName;
  }

  /**
   * Configures the default file name to use when downloading the property
   * content as a file.
   *
   * @param fileName
   *          the fileName to set.
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Gets the contentType.
   *
   * @return the contentType.
   */
  @Override
  public String getContentType() {
    return contentType;
  }

  /**
   * Configures the default content type to use when downloading the property
   * content as a file.
   *
   * @param contentType
   *          the contentType to set.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Tests whether this text should be kept multi-line when building a filter screen.
   *
   * @return {@code true} if this text should be transformed into a
   *         multi-line text area when building a filter screen.
   */
  protected boolean isQueryMultiline() {
    return queryMultiline;
  }

  /**
   * This property allows to control if the text property view should be
   * transformed into a multi-line text view in order to allow for multi-line
   * text in filters. Default value is {@code false}.
   *
   * @param queryMultiline
   *          the queryMultiline to set.
   */
  public void setQueryMultiline(boolean queryMultiline) {
    this.queryMultiline = queryMultiline;
  }

  /**
   * Gets default max length.
   *
   * @return the default max length
   */
  @Override
  protected Integer getDefaultMaxLength() {
    return 8192;
  }
}
