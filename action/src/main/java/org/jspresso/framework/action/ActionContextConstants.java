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
package org.jspresso.framework.action;

/**
 * well-known action context keys.
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
public final class ActionContextConstants {

  /**
   * An arbitrary action command.
   */
  public static final String ACTION_COMMAND           = "ACTION_COMMAND";

  /**
   * An arbitrary action param.
   */
  public static final String ACTION_PARAM             = "ACTION_PARAM";

  /**
   * Page offset.
   */
  public static final String PAGE_OFFSET              = "PAGE_OFFSET";

  /**
   * Ordering properties.
   */
  public static final String ORDERING_PROPERTIES      = "ORDERING_PROPERTIES";

  /**
   * The widget at the origin of the action.
   */
  public static final String ACTION_WIDGET            = "ACTION_WIDGET";

  /**
   * the backend controller.
   */
  public static final String BACK_CONTROLLER          = "BACK_CONTROLLER";

  /**
   * A parametrized entity descriptor.
   */
  public static final String COMPONENT_REF_DESCRIPTOR = "COMPONENT_REF_DESCRIPTOR";

  /**
   * The dialog actions.
   */
  public static final String DIALOG_ACTIONS           = "DIALOG_ACTIONS";

  /**
   * The dialog title.
   */
  public static final String DIALOG_TITLE             = "DIALOG_TITLE";

  /**
   * The dialog view.
   */
  public static final String DIALOG_VIEW              = "DIALOG_VIEW";

  /**
   * The dialog size.
   */
  public static final String DIALOG_SIZE              = "DIALOG_SIZE";

  /**
   * The the descriptor of the model collection element domain object the action
   * was triggered on.
   */
  public static final String ELEMENT_DESCRIPTOR       = "ELEMENT_DESCRIPTOR";

  /**
   * the frontend controller.
   */
  public static final String FRONT_CONTROLLER         = "FRONT_CONTROLLER";

  /**
   * The the descriptor of the model domain object the action was triggered on.
   */
  public static final String MODEL_DESCRIPTOR         = "MODEL_DESCRIPTOR";

  /**
   * The module view connector the action was triggered on.
   */
  public static final String MODULE_VIEW_CONNECTOR    = "MODULE_VIEW_CONNECTOR";

  /**
   * the connector of the query model.
   */
  public static final String QUERY_MODEL_CONNECTOR    = "QUERY_MODEL_CONNECTOR";

  /**
   * The selected indices of the view connector in case of a collection
   * connector. It may serve as storage key for a return value whenever an
   * action must be chained with selection update on the view.
   */
  public static final String SELECTED_INDICES         = "SELECTED_INDICES";

  /**
   * The source widget.
   */
  public static final String SOURCE_COMPONENT         = "SOURCE_COMPONENT";

  /**
   * The view connector the action was triggered on.
   */
  public static final String VIEW_CONNECTOR           = "VIEW_CONNECTOR";

  private ActionContextConstants() {
    // to prevent this class from being instanciated.
  }
}
