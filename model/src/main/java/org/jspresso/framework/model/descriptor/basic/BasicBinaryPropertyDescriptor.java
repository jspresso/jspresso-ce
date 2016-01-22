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
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Describes a property used to store a binary value in the form of a byte
 * array.
 *
 * @author Vincent Vandenschrick
 */
public class BasicBinaryPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IBinaryPropertyDescriptor {

  private Map<String, List<String>> fileFilter;
  private Integer                   maxLength;
  private String                    fileName;
  private String                    contentType;

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
  @Override
  public Map<String, List<String>> getFileFilter() {
    return fileFilter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
   * automatically based on the {@code fileFilter} property.
   * <p>
   * The incoming {@code Map} must be structured like following :
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

  /**
   * Handles max length and regular expression.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void preprocessSetter(final Object component, final Object newValue) {
    super.preprocessSetter(component, newValue);
    if (newValue instanceof byte[]) {
      // watch out for java serializable property
      final byte[] propertyValueAsByteArray = (byte[]) newValue;
      if (getMaxLength() != null
          && propertyValueAsByteArray.length > getMaxLength()) {
        IntegrityException ie = new IntegrityException("[" + getName()
            + "] value is too long on ["
            + component + "].") {

          private static final long serialVersionUID = 7459823123892198831L;

          @Override
          public String getI18nMessage(
              ITranslationProvider translationProvider, Locale locale) {
            StringBuilder boundsSpec = new StringBuilder("l");
            if (getMaxLength() != null) {
              boundsSpec.append(" <= ").append(getMaxLength());
            }
            return translationProvider.getTranslation(
                "integrity.property.toolong", new Object[] {
                    getI18nName(translationProvider, locale), boundsSpec,
                    component
                }, locale);
          }

        };
        throw ie;
      }
    }
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
   * Returns {@code false}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultSortablility() {
    return false;
  }
}
