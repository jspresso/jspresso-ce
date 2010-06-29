/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.view.descriptor.IActionablePropertyViewDescriptor;

/**
 * This view descriptor allows to define an actionable property view, e.g. a
 * property view that can be clicked to trigger an action. As a preferred way of
 * rendering such a view, the UI channels should transform them into hyperlinks.
 * 
 * @version $LastChangedRevision: 2669 $
 * @author Vincent Vandenschrick
 */
public class BasicActionablePropertyViewDescriptor extends
    BasicPropertyViewDescriptor implements IActionablePropertyViewDescriptor {

  private IAction action;

  /**
   * Gets the action.
   * 
   * @return the action.
   */
  public IAction getAction() {
    return action;
  }

  /**
   * Configures the action to be triggered when the user activates (clicks) this
   * property view.
   * 
   * @param action
   *          the action to set.
   */
  public void setAction(IAction action) {
    this.action = action;
  }

  /**
   * An actionable property view is always read-only.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly() {
    return true;
  }

}
