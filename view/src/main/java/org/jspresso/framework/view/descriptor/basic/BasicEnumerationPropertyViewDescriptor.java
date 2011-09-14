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
package org.jspresso.framework.view.descriptor.basic;

import java.util.Set;

import org.jspresso.framework.view.descriptor.IEnumerationPropertyViewDescriptor;

/**
 * This specialized property view descriptor is used in order to be able to
 * refine the &quot;values&quot; that are taken from the model enumeration. You
 * can configure a set of allowed values from which the user can choose.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEnumerationPropertyViewDescriptor extends
    BasicPropertyViewDescriptor implements IEnumerationPropertyViewDescriptor {

  private Set<String> allowedValues;

  /**
   * Configures an optional filter set of values to restrict the model ones.
   * Only values belonging to the filter should be actually made available as a
   * choice.
   * 
   * @param allowedValues
   *          the allowedValues to set.
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

}
