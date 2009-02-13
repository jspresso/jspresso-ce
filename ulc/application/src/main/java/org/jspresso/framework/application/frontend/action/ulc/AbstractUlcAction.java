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
package org.jspresso.framework.application.frontend.action.ulc;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.WrappingAction;
import org.jspresso.framework.util.ulc.UlcUtil;

import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * This class serves as base class for swing actions. It provides accessors on
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
public abstract class AbstractUlcAction extends
    WrappingAction<ULCComponent, ULCIcon, IAction> {

  /**
   * If the ancestor of the action widget is a dialog, dispose it.
   * 
   * @param context
   *            the action context.
   */
  protected void closeDialog(Map<String, Object> context) {
    ULCWindow actionWindow = UlcUtil.getVisibleWindow(getActionWidget(context));
    if (actionWindow instanceof ULCDialog) {
      actionWindow.setVisible(false);
    }
  }
}
