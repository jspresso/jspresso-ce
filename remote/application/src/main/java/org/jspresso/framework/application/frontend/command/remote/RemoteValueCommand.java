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
package org.jspresso.framework.application.frontend.command.remote;

import java.io.Serializable;

/**
 * This command is used to update a remote peer value.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteValueCommand extends RemoteCommand {

  private static final long serialVersionUID = -3870216495926436036L;

  private String            description;
  private String            iconImageUrl;
  private Serializable      value;
  private Serializable      valueAsObject;

  /**
   * Gets the description.
   *
   * @return the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the iconImageUrl.
   *
   * @return the iconImageUrl.
   */
  public String getIconImageUrl() {
    return iconImageUrl;
  }

  /**
   * Gets the value.
   *
   * @return the value.
   */
  public Object getValue() {
    return value;
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the iconImageUrl.
   *
   * @param iconImageUrl
   *          the iconImageUrl to set.
   */
  public void setIconImageUrl(String iconImageUrl) {
    this.iconImageUrl = iconImageUrl;
  }

  /**
   * Sets the value.
   *
   * @param value
   *          the value to set.
   */
  public void setValue(Object value) {
    this.value = (Serializable) value;
  }

  /**
   * Gets the valueAsObject.
   *
   * @return the valueAsObject.
   */
  public Object getValueAsObject() {
    return valueAsObject;
  }

  /**
   * Sets the valueAsObject.
   *
   * @param valueAsObject
   *          the valueAsObject to set.
   */
  public void setValueAsObject(Object valueAsObject) {
    this.valueAsObject = (Serializable) valueAsObject;
  }

}
