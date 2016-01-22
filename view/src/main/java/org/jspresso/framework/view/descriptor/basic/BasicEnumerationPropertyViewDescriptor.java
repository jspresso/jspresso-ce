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
package org.jspresso.framework.view.descriptor.basic;

import java.util.Set;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.descriptor.EOrientation;
import org.jspresso.framework.view.descriptor.IEnumerationPropertyViewDescriptor;

/**
 * This specialized property view descriptor is used in order to be able to
 * refine the &quot;values&quot; that are taken from the model enumeration. You
 * can configure a set of allowed values from which the user can choose.
 *
 * @author Vincent Vandenschrick
 */
public class BasicEnumerationPropertyViewDescriptor extends BasicPropertyViewDescriptor
    implements IEnumerationPropertyViewDescriptor {

  private Set<String> allowedValues;
  private Set<String> forbiddenValues;
  private boolean     radio;
  private Dimension   enumIconDimension;
  private EOrientation orientation = EOrientation.VERTICAL;

  /**
   * Returns an optional forbidden set of values to restrict the model ones.
   * Only values belonging to the allowed ones should actually be made available
   * as a choice.
   *
   * @param allowedValues
   *     the allowedValues to set.
   */
  public void setAllowedValues(Set<String> allowedValues) {
    this.allowedValues = allowedValues;
  }

  /**
   * Gets the allowedValues.
   *
   * @return the allowedValues.
   */
  @Override
  public Set<String> getAllowedValues() {
    return allowedValues;
  }

  /**
   * Returns an optional forbidden set of values to restrict the model ones.
   * Only values not belonging to the forbidden ones should actually be made
   * available as a choice.
   *
   * @param forbiddenValues
   *     an optional forbidden set of values to restrict the model ones.
   */
  public void setForbiddenValues(Set<String> forbiddenValues) {
    this.forbiddenValues = forbiddenValues;
  }

  /**
   * Gets the forbiddenValues.
   *
   * @return the forbiddenValues.
   */
  @Override
  public Set<String> getForbiddenValues() {
    return forbiddenValues;
  }

  /**
   * Gets the radio.
   *
   * @return the radio.
   */
  @Override
  public boolean isRadio() {
    return radio;
  }

  /**
   * Configures the rendering of the enumeration property as radio buttons if
   * supported instead of combo box. Default value is {@code false}.
   *
   * @param radio
   *     the radio to set.
   */
  public void setRadio(boolean radio) {
    this.radio = radio;
  }

  /**
   * Gets the orientation.
   *
   * @return the orientation.
   */
  @Override
  public EOrientation getOrientation() {
    return orientation;
  }

  /**
   * Configures whether radio values be rendered horizontally or vertically.
   * {@code HORIZONTAL} if radio values should be rendered horizontally and
   * {@code VERTICAL} otherwise. Default value is {@code VERTICAL}.
   *
   * @param orientation
   *     the orientation to set.
   */
  public void setOrientation(EOrientation orientation) {
    this.orientation = orientation;
  }

  /**
   * Gets the enumeration icon dimension.
   *
   * @return the enumeration icon dimension.
   */
  @Override
  public Dimension getEnumIconDimension() {
    return enumIconDimension;
  }

  /**
   * Sets the icon dimension.
   *
   * @param enumIconDimension
   *     the icon dimension to set.
   */
  public void setEnumIconDimension(Dimension enumIconDimension) {
    this.enumIconDimension = enumIconDimension;
  }

  /**
   * Sets the enumeration icon width.
   *
   * @param iconWidth
   *     the icon width to set.
   */
  @SuppressWarnings("SuspiciousNameCombination")
  public void setEnumIconWidth(int iconWidth) {
    if (enumIconDimension == null) {
      enumIconDimension = new Dimension();
      enumIconDimension.setHeight(iconWidth);
    }
    enumIconDimension.setWidth(iconWidth);
  }

  /**
   * Sets the enumeration icon height.
   *
   * @param iconHeight
   *     the icon height to set.
   */
  @SuppressWarnings("SuspiciousNameCombination")
  public void setEnumIconHeight(int iconHeight) {
    if (enumIconDimension == null) {
      enumIconDimension = new Dimension();
      enumIconDimension.setWidth(iconHeight);
    }
    enumIconDimension.setHeight(iconHeight);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicEnumerationPropertyViewDescriptor clone() {
    BasicEnumerationPropertyViewDescriptor clone = (BasicEnumerationPropertyViewDescriptor) super.clone();
    if (enumIconDimension != null) {
      clone.enumIconDimension = enumIconDimension.clone();
    }
    return clone;
  }
}
