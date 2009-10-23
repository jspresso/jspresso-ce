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
package org.jspresso.framework.view;

import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptor;

/**
 * A factory for actions.
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
 * @param <E>
 *          the actual action class created.
 * @param <F>
 *          the actual component class the created actions are installed in.
 */
public interface IActionFactory<E, F> {

  /**
   * <code>TOOLTIP_ELLIPSIS</code> is "...".
   */
  String TOOLTIP_ELLIPSIS = "...";

  /**
   * Creates an action from its descriptor.
   * 
   * @param action
   *          the action descriptor.
   * @param actionHandler
   *          the handler responsible for executing the action.
   * @param sourceComponent
   *          the view component which the action is attached to.
   * @param modelDescriptor
   *          the model descriptor this action is triggered on.
   * @param viewConnector
   *          the view connector this action is created on.
   * @param locale
   *          the locale the action has to use.
   * @return the constructed action.
   */
  E createAction(IAction action, IActionHandler actionHandler,
      F sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector, Locale locale);

  /**
   * Creates an action from its descriptor.
   * 
   * @param action
   *          the action descriptor.
   * @param actionHandler
   *          the handler responsible for executing the action.
   * @param view
   *          the view which the action is attached to.
   * @param locale
   *          the locale the action has to use.
   * @return the constructed action.
   */
  E createAction(IAction action, IActionHandler actionHandler, IView<F> view,
      Locale locale);

  /**
   * Enabled or disables an action.
   * 
   * @param action
   *          the action to work on.
   * @param enabled
   *          true to enable, false otherwise.
   */
  void setActionEnabled(E action, boolean enabled);

  /**
   * Sets an action name.
   * 
   * @param action
   *          the action to work on.
   * @param name
   *          the action name.
   */
  void setActionName(E action, String name);

  /**
   * Creates the initial action context.
   * 
   * @param actionHandler
   *          the action handler.
   * @param modelDescriptor
   *          the model descriptor.
   * @param sourceComponent
   *          the source component.
   * @param viewConnector
   *          the view connector.
   * @param actionCommand
   *          the action command.
   * @param actionWidget
   *          the widget this action was triggered from.
   * @return the initial action context.
   */
  Map<String, Object> createActionContext(IActionHandler actionHandler,
      IModelDescriptor modelDescriptor, F sourceComponent,
      IValueConnector viewConnector, String actionCommand, F actionWidget);

}
