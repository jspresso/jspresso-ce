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
package org.jspresso.framework.application.frontend.action.ulc.flow;

import org.jspresso.framework.action.IAction;

/**
 * Action to ask a binary question to the user.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoAction extends AbstractFlowAction {

  private IAction noAction;
  private IAction yesAction;

  /**
   * Constructs a new <code>YesNoAction</code> instance.
   */
  protected YesNoAction() {
    super(YES_OPTION, NO_OPTION, null);
  }

  /**
   * Sets the noAction.
   * 
   * @param noAction
   *            the noAction to set.
   */
  public void setNoAction(IAction noAction) {
    this.noAction = noAction;
  }

  /**
   * Sets the yesAction.
   * 
   * @param yesAction
   *            the yesAction to set.
   */
  public void setYesAction(IAction yesAction) {
    this.yesAction = yesAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IAction getNextAction(String selectedOption) {
    if (YES_OPTION.equals(selectedOption)) {
      return yesAction;
    }
    return noAction;
  }

}
