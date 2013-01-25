/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.component.query;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * A simple query structure which holds selection tick, a value and a
 * translation.
 * 
 * @version $LastChangedRevision: 5621 $
 * @author Vincent Vandenschrick
 */
public class EnumValueQueryStructure extends AbstractPropertyChangeCapable {

  private boolean selected;
  private String  value;

  /**
   * Constructs a new <code>EnumValueQueryStructure</code> instance.
   */
  public EnumValueQueryStructure() {
    this.selected = false;
  }

  /**
   * Gets the selected.
   * 
   * @return the selected.
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Sets the selected.
   * 
   * @param selected
   *          the selected to set.
   */
  public void setSelected(boolean selected) {
    boolean oldSelected = this.selected;
    this.selected = selected;
    firePropertyChange("selected", oldSelected, selected);
  }

  /**
   * Gets the value.
   * 
   * @return the value.
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value.
   * 
   * @param value
   *          the value to set.
   */
  public void setValue(String value) {
    String oldValue = this.value;
    this.value = value;
    firePropertyChange("value", oldValue, value);
  }
}
