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

import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IReferencePropertyViewDescriptor;

/**
 * This specialized property view descriptor is used in order to be able to
 * refine the &quot;List of values&quot; action that gets automatically
 * installed by Jspresso when a reference property is displayed. You can then
 * customize this action when defining the corresponding column in a table view
 * or field in a component view.
 *
 * @author Vincent Vandenschrick
 */
public class BasicReferencePropertyViewDescriptor extends BasicStringPropertyViewDescriptor
    implements IReferencePropertyViewDescriptor {

  private IDisplayableAction lovAction;
  private boolean            autoCompleteEnabled;

  /**
   * Instantiates a new Basic reference property view descriptor.
   */
  public BasicReferencePropertyViewDescriptor() {
    autoCompleteEnabled = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IDisplayableAction getLovAction() {
    return lovAction;
  }

  /**
   * Allows to override the default &quot;List of values&quot; action attached
   * to this reference property view.
   * <p/>
   * A {@code null} value (default) keeps the standard action.
   *
   * @param lovAction
   *     the lovAction to set.
   */
  public void setLovAction(IDisplayableAction lovAction) {
    this.lovAction = lovAction;
  }


  /**
   * Is auto complete enabled.
   *
   * @return the boolean
   */
  @Override
  public boolean isAutoCompleteEnabled() {
    return autoCompleteEnabled;
  }

  /**
   * Sets auto complete enabled. I set to {@code false}, then the field will always be read-only whereas the LOV
   * action button will follow enabled / disabled rules.
   *
   * @param autoCompleteEnabled the auto complete enabled value.
   */
  public void setAutoCompleteEnabled(boolean autoCompleteEnabled) {
    this.autoCompleteEnabled = autoCompleteEnabled;
  }
}
