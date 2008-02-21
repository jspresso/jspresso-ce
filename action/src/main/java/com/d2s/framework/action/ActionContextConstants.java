/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.action;

/**
 * well-known action context keys.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   * The dialog view.
   */
  public static final String DIALOG_VIEW              = "DIALOG_VIEW";

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
   * The next action.
   */
  public static final String NEXT_ACTION              = "NEXT_ACTION";

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
   * The view descriptor at the origin of the action chain.
   */
  public static final String SOURCE_MODEL_DESCRIPTOR  = "SOURCE_MODEL_DESCRIPTOR";

  /**
   * The view connector at the origin of the action chain.
   */
  public static final String SOURCE_VIEW_CONNECTOR    = "SOURCE_VIEW_CONNECTOR";

  /**
   * The view connector the action was triggered on.
   */
  public static final String VIEW_CONNECTOR           = "VIEW_CONNECTOR";

  private ActionContextConstants() {
    // to prevent this class from being instanciated.
  }
}
