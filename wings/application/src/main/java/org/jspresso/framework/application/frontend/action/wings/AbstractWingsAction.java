/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.wings;

import java.util.Map;

import javax.swing.Action;

import org.jspresso.framework.application.frontend.action.WrappingAction;
import org.jspresso.framework.util.wings.WingsUtil;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SDialog;
import org.wings.SIcon;


/**
 * This class serves as base class for wings actions. It provides accessors on
 * commonly used artifacts.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
public abstract class AbstractWingsAction extends
    WrappingAction<SComponent, SIcon, Action> {

  /**
   * If the ancestor of the action widget is a dialog, dispose it.
   * 
   * @param context
   *            the action context.
   */
  protected void closeDialog(Map<String, Object> context) {
    SContainer actionWindow = WingsUtil
        .getVisibleWindow(getActionWidget(context));
    if (actionWindow instanceof SDialog) {
      ((SDialog) actionWindow).dispose();
    }
  }
}
