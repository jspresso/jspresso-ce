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
package org.jspresso.framework.application.frontend.command.remote;

import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;

/**
 * A command to trigger a modal remote dialog pop-up.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteDialogCommand extends RemoteCommand {

  private String     title;
  private RComponent view;
  private RAction[]  actions;
  
  /**
   * Gets the title.
   * 
   * @return the title.
   */
  public String getTitle() {
    return title;
  }
  
  /**
   * Sets the title.
   * 
   * @param title the title to set.
   */
  public void setTitle(String title) {
    this.title = title;
  }
  
  /**
   * Gets the view.
   * 
   * @return the view.
   */
  public RComponent getView() {
    return view;
  }
  
  /**
   * Sets the view.
   * 
   * @param view the view to set.
   */
  public void setView(RComponent view) {
    this.view = view;
  }
  
  /**
   * Gets the actions.
   * 
   * @return the actions.
   */
  public RAction[] getActions() {
    return actions;
  }
  
  /**
   * Sets the actions.
   * 
   * @param actions the actions to set.
   */
  public void setActions(RAction[] actions) {
    this.actions = actions;
  }

}
